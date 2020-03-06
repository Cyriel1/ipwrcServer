package nl.ipwrcServer.resources.resources;

import nl.ipwrcServer.resources.dao.UserDAO;
import nl.ipwrcServer.resources.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
public class UserResource {

    private UserDAO userDAO;

    public UserResource(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @GET
    @Path("/getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> getAll(){

        return userDAO.getAll();
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getGreeting(@PathParam("name") String name){

        return "Hello, " + name + "!";
    }

}
