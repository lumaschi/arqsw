
package gosoccer.cancha;

import com.gosoccer.cancha.Cancha;
import com.gosoccer.cancha.CanchaBeanLocal;
import com.google.gson.Gson;
import com.gosoccer.excepcion.DatoErroneoException;
import com.gosoccer.excepcion.EntidadNoExisteException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


// REST Web Service

@Path("canchas")
@RequestScoped
public class CanchaResource {

    @Context
    private UriInfo context;

    @EJB
    CanchaBeanLocal canchaBean;

    private final Gson gson = new Gson();

    public CanchaResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCanchas() {
        List<Cancha> list = canchaBean.obtenerCanchas();
        return Response.ok().entity(gson.toJson(list)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registratCancha(String json) {
        System.out.println("json" + json);
        Cancha cancha = null;
        try {
            cancha = canchaBean.crearCancha(gson.fromJson(json, Cancha.class));
        } catch (DatoErroneoException ex) {
           Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
        }
         return Response.status(Response.Status.CREATED).entity(gson.toJson(cancha)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCancha(@PathParam("id") Long id) throws EntidadNoExisteException {
        // try {
        //return Response.status(Response.Status.NOT_FOUND).build();
        //400 client error
        Cancha cancha = canchaBean.obtenerCancha(id);
        return Response.ok().entity(gson.toJson(cancha)).build();
        /*    } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.serverError().build();
        }*/
    }
}
