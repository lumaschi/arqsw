package gosoccer.usuario;

import com.gosoccer.usuario.UsuarioBeanLocal;
import com.gosoccer.usuario.Usuario;
import com.gosoccer.usuario.UsuarioDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gosoccer.excepcion.DatoErroneoException;
import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.token.Token;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//* REST Web Service
 
@Path("usuarios")
public class UsuarioResource {

    @Context
    private UriInfo context;

    @EJB
    UsuarioBeanLocal usrBean;

    private final Gson gson = new Gson();

    public UsuarioResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(String json) {
        System.out.println("USUARIO JSON " + json);
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        String contrasena = map.get("contrasena").toString();
        UsuarioDto usuario;
        try {
            usuario = usrBean.crearUsuario(gson.fromJson(json, UsuarioDto.class), contrasena);
        } catch (Exception ex) {
            return Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
        }
        System.out.println("usuario" + usuario.getNombre());
        return Response.status(Response.Status.CREATED).entity(gson.toJson(usuario)).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") Long id) throws EntidadNoExisteException {
        UsuarioDto dto = usrBean.obtenerUsuarioDto(id);
        return Response.ok().entity(gson.toJson(dto)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarUsuario(String json) throws EntidadNoExisteException {
        Gson gson = new GsonBuilder().create();
        Usuario usuario = gson.fromJson(json, Usuario.class);
        System.out.println("IDUS" + usuario.getId());

        if (usrBean.estaLogueado(usuario)) {
            usrBean.editarUsuario(usuario);
            return Response.ok().entity(gson.toJson(usuario)).build();
        } else {
            new EntidadNoExisteException("Tiene que estar logueado.");
        }
        return Response.ok().entity(gson.toJson(usuario)).build();// cambiar retorno
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response darDeBajaUsuario(@PathParam("id") Long id) throws EntidadNoExisteException {
        Usuario usuario = usrBean.obtenerUsuario(id);
        System.out.println("IDUS" + usuario.getId());
        if (usrBean.estaLogueado(usuario)) {
            usrBean.darDeBaja(usuario);
            return Response.ok().entity(gson.toJson(usuario)).build();
        } else {
            new EntidadNoExisteException("Tiene que estar logueado.");
        }
        return Response.ok().entity(gson.toJson(usuario)).build();// cambiar retorno
    }

    @POST
    @Path("/iniciarSesion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarSesion(String json) throws EntidadNoExisteException, DatoErroneoException {
        System.out.println("USUARIO JSON " + json);
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        String contrasena = map.get("contrasena").toString();
        String usuario = map.get("usuario").toString();
        System.out.println(contrasena + " y " + usuario);
        Token t = usrBean.iniciarSesion(usuario, contrasena, false);
        return Response.ok().entity(gson.toJson(t)).build();
    }

    @DELETE
    @Path("/cerrarSesion/{usuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cerrarSesion(@PathParam("usuario") String nombreUsuario) throws EntidadNoExisteException {
        System.out.println("CERRAR SESION " + nombreUsuario);
        usrBean.cerrarSesion(nombreUsuario);
        return Response.ok().entity(gson.toJson(nombreUsuario)).build();
    }
}
