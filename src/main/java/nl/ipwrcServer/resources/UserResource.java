package nl.ipwrcServer.resources;

import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.model.User;
import nl.ipwrcServer.persistence.UserDAO;
import nl.ipwrcServer.service.JsonViewService;
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
    @Path("/getUsers")
    @Produces({MediaType.APPLICATION_JSON})
    @JsonView(JsonViewService.Public.class)
    public List<User> getUsers(){

        return userDAO.getUsers();
    }

}
