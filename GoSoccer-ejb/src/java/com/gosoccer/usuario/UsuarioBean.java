
package com.gosoccer.usuario;

import com.gosoccer.excepcion.DatoErroneoException;
import com.gosoccer.token.TokenBean;
import com.gosoccer.token.Token;
import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.excepcion.EntidadYaExisteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UsuarioBean implements UsuarioBeanLocal {

    @PersistenceContext
    public EntityManager em;

    @EJB
    private TokenBean tokenBean;

    public List<UsuarioDto> obtenerUsuarios() {
        return convertirListaADto(em.createQuery("select u from Usuario u").getResultList(), false);
    }

    private List<UsuarioDto> convertirListaADto(List<Usuario> usuarios, boolean includeBooks) {
        List<UsuarioDto> dtos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            dtos.add(convertirADto(usuario));
        }
        return dtos;
    }

    public UsuarioDto crearUsuario(UsuarioDto dto, String contrasena) throws EntidadYaExisteException, EntidadNoExisteException, DatoErroneoException {
        Usuario usuario = null;
        try {
            usuario = convertirAusuario(dto);
            usuario.setContrasena(contrasena);
            em.persist(usuario);
            System.out.println("usuario" + usuario);
            System.out.println("contrasena" + contrasena);
            Token t = iniciarSesion(usuario.getNombre(), contrasena, false);
            return convertirADto(usuario);
        } catch (EntityExistsException e) {
            throw new EntidadYaExisteException("El usuario" + dto.getNombre() + " ya existe.");
        } catch (Exception e) {
            throw e;
        }
    }

    private UsuarioDto convertirADto(Usuario entidad) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        System.out.println("json" + dto.getNombre() + "," + dto.getId());
        return dto;
    }

    @Override
    public Usuario convertirAusuario(UsuarioDto dto) {
        System.out.println("gosoccer.usuario.UsuarioBean.convertirAusuario()");
        Usuario entidad = new Usuario();
        entidad.setNombre(dto.getNombre());
        entidad.setEmail(dto.getEmail());
        entidad.setEstado(true);
        return entidad;
    }

    @Override
    public UsuarioDto obtenerUsuarioDto(Long id) throws EntidadNoExisteException {
        try {
            return convertirADto(em.find(Usuario.class, id));
        } catch (Exception e) {
            throw new EntidadNoExisteException("Entidad no existe");
        }
    }

    @Override
    public Usuario obtenerUsuario(Long id) throws EntidadNoExisteException {
        try {
            return em.find(Usuario.class, id);
        } catch (Exception e) {
            throw new EntidadNoExisteException("Entidad no existe");
        }
    }

    private Usuario obtenerUsuarioPorNombre(String nombreUsuario) throws EntidadNoExisteException {
        try {
            return (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.nombre LIKE :nombreU")
                    .setParameter("nombreU", nombreUsuario)
                    .getSingleResult();
        } catch (Exception e) {
            throw new EntidadNoExisteException("Entidad no existe");
        }
    }

    @Override
    public void editarUsuario(Usuario unUsuario) throws EntidadNoExisteException {
        if (estaLogueado(unUsuario)) {
            System.out.println("unUsuario" + unUsuario.getId());
            Usuario usr = obtenerUsuario(unUsuario.getId());
            System.out.println("usr" + usr.getId());
            usr.setEmail(unUsuario.getEmail());
            usr.setEstado(true);
            usr.setNombre(unUsuario.getNombre());
            em.merge(usr);
            em.flush();
        }
    }

    @Override
    public void darDeBaja(Usuario unUsuario) throws EntidadNoExisteException {
        if (estaLogueado(unUsuario)) {
            System.out.println("unUsuario" + unUsuario.getId());
            cerrarSesion(unUsuario.getNombre());
            Usuario usr = obtenerUsuario(unUsuario.getId());
            System.out.println("usr" + usr.getId());
            usr.setEstado(false);
            em.merge(usr);
            em.flush();
        }
    }

    @Override
    public void cerrarSesion(String nombreUsuario) throws EntidadNoExisteException {
        try {
            Usuario usuario = this.obtenerUsuarioPorNombre(nombreUsuario);
            System.out.println("ID USUARIO " + usuario.getId());
            Token t = tokenBean.obtenerTokenPorUsuario(usuario.getId());
            System.out.println("TOKEN " + t);
            tokenBean.borrarToken(t);
        } catch (Exception e) {
            throw new EntidadNoExisteException(e.getMessage());
        }
    }

    @Override
    public boolean estaLogueado(Usuario usuario) throws EntidadNoExisteException {
        if (tokenBean.obtenerTokenPorUsuario(usuario.getId()) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Token iniciarSesion(String nombreUsuario, String contrasena, boolean modoEdit) throws EntidadNoExisteException, DatoErroneoException {
        String token = "";
        Token t = null;
        try {
            Usuario usuario = obtenerUsuarioPorNombre(nombreUsuario);
            System.out.println("CONTRASENAAAA" + usuario.getContrasena());
            System.out.println("contrasena" + contrasena);
            System.out.println("nombreUsuario" + nombreUsuario);
            if (modoEdit) {
                if (!estaLogueado(usuario)) {
                    t = crearToken(usuario, contrasena);
                    return t;
                }
            } else {
                t = crearToken(usuario, contrasena);
                return t;
            }
        } catch (EntityExistsException e) {
            throw new EntidadNoExisteException("El usuario " + nombreUsuario + " no fue encontrado.");
        } catch (Exception e) {
            throw e;
        }
        return t;
    }

    private Token crearToken(Usuario usuario, String contrasena) throws DatoErroneoException {
        String token = " ";
        if (usuario.getContrasena().equals(contrasena)) {
            token = UUID.randomUUID().toString();
            Token t = new Token();
            t.setToken(token);
            t.setUsuario(usuario);
            tokenBean.registarToken(t);
            return t;
        } else {
            throw new DatoErroneoException("Las contraseñas no coinciden.");
        }
    }
}
