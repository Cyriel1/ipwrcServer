package nl.ipwrcServer.service;

import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Authorizer;
import nl.ipwrcServer.dao.UserDAO;
import nl.ipwrcServer.model.User;

public class AuthorizeService implements Authorizer<User> {

    private UserDAO userDAO;

    public AuthorizeService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public boolean authorize(User user, String roleName) {

        return userDAO.checkUserRole(user).getRole().equals(roleName);
    }

}
