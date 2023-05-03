package restAPIs;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import moodleConnection.MoodleDAO;

/**
 * REST Web Service
 *
 * @author lamar
 */
@Path("MoodleTareas")
public class MoodleTareasResource {

    @Context
    private UriInfo context;
    
    MoodleDAO moodleDAO = new MoodleDAO();

    /**
     * Creates a new instance of MoodleTareasResource
     */
    public MoodleTareasResource() {
    }

    /**
     * Retrieves representation of an instance of moodleConnection.MoodleTareasResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        //TODO return proper representation object
        return Response.status(Response.Status.OK).entity(moodleDAO.getDetallesTarea(0)).build();
    }

    /**
     * PUT method for updating or creating an instance of MoodleTareasResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
