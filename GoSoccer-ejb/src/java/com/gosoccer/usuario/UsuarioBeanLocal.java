
package com.gosoccer.usuario;

import com.gosoccer.excepcion.DatoErroneoException;
import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.excepcion.EntidadYaExisteException;
import com.gosoccer.token.Token;
import java.util.List;
import javax.ejb.Local;

@Local
public interface UsuarioBeanLocal {

    //void crearUsuario(String nombre, String email, String contrasena);
    UsuarioDto obtenerUsuarioDto(Long id) throws EntidadNoExisteException;

    UsuarioDto crearUsuario(UsuarioDto dto, String contrasena) throws EntidadYaExisteException, EntidadNoExisteException, DatoErroneoException;

    List<UsuarioDto> obtenerUsuarios();

    void editarUsuario(Usuario unUsuario) throws EntidadNoExisteException;

    boolean estaLogueado(Usuario usuario) throws EntidadNoExisteException;

    Usuario convertirAusuario(UsuarioDto dto);

    Usuario obtenerUsuario(Long id) throws EntidadNoExisteException;

    Token iniciarSesion(String nombreUsuario, String contrasena, boolean modoEdit) throws EntidadNoExisteException, DatoErroneoException;

    void cerrarSesion(String token) throws EntidadNoExisteException;
    
    void darDeBaja(Usuario unUsuario) throws EntidadNoExisteException;

}
