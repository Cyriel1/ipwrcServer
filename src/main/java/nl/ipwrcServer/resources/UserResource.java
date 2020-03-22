package nl.ipwrcServer.resources;

import nl.ipwrcServer.persistence.UserDAO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/user")
public class UserResource {

    private UserDAO userDAO;

    public UserResource(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getGreeting(@PathParam("name") String name){

        return "Hello, " + name + "!";
    }

}
