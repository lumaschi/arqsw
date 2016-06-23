package gosoccer.reserva;

import com.gosoccer.reserva.Reserva;
import com.gosoccer.reserva.ReservaBeanLocal;
import com.gosoccer.cancha.CanchaBeanLocal;
import com.google.gson.Gson;
import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.usuario.UsuarioBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("reservas")
public class ReservaResource {

    @Context
    private UriInfo context;

    @EJB
    CanchaBeanLocal canchaBean;

    @EJB
    UsuarioBeanLocal usuarioBean;

    @EJB
    ReservaBeanLocal reservaBean;

    private final Gson gson = new Gson();

    public ReservaResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearReserva(String json) {
        reservaBean.procesarReserva(json);
        return Response.status(Response.Status.ACCEPTED).entity("Se esta procesando.").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerReservasPorUsurio(@PathParam("id") Long id) throws EntidadNoExisteException {
        List<Reserva> list = reservaBean.obtenerReservas(id);
        return Response.ok().entity(gson.toJson(list)).build();
    }

}
