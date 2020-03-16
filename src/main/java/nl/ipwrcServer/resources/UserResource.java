package nl.ipwrcServer.resources;

import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import nl.ipwrcServer.service.JsonViewService;
import nl.ipwrcServer.persistence.UserDAO;
import nl.ipwrcServer.model.User;
import javax.annotation.security.RolesAllowed;
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
    @JsonView(JsonViewService.Public.class)
    @RolesAllowed({"KLANT", "ADMIN"})
    public List<User> getAll(@Auth User user){

        return userDAO.getAllUsers();
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getGreeting(@PathParam("name") String name){

        return "Hello, " + name + "!";
    }

}
