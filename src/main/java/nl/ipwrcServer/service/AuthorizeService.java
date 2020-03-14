package nl.ipwrcServer.service;

import io.dropwizard.auth.Authorizer;
import nl.ipwrcServer.persistence.dao.UserDAO;
import nl.ipwrcServer.model.User;
import java.util.List;

public class AuthorizeService implements Authorizer<User> {

    private UserDAO userDAO;

    public AuthorizeService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    private boolean hasRole(String roleName, List<User> roles)
    {
        if (roles != null)
        {
            for(User role : roles)
            {
                if(roleName.equals(role.getRole()))
                {

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean authorize(User user, String roleName) {

        return hasRole(roleName, userDAO.checkUserRole(user));
    }

}
