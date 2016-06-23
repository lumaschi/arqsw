package usuarios;

import Usuario.UsuarioBean;
import Usuario.UsuarioBeanLocal;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author vgalarza
 */
@Path("usuarios")
public class UsuarioResource {

   @Context
   private UriInfo context;
    
   @EJB
   UsuarioBeanLocal usrBean;

    public UsuarioResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "Hola Mundo";
    }
    
    @GET
    @Path("prueba")
    public String prueba() {
        return "Hola Prueba";
    }

    @POST
    @Path("/addUser")
    public Response addUser(@QueryParam("nombre") String nombre) {
        try {
            
           System.out.println("usrBean"+usrBean);
           usrBean.crearUsuario(nombre, "","");
           return Response.ok().build();// "El usuario se creo con exito.";
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.serverError().build();
        }
    }
}
