package nl.ipwrcServer.service;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import nl.ipwrcServer.persistence.UserDAO;
import nl.ipwrcServer.model.User;

import java.util.Optional;

public class AuthenticatorService implements Authenticator<BasicCredentials, User> {

    private UserDAO userDAO;

    public AuthenticatorService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public Optional<User> authenticate(BasicCredentials credentials){

        return userDAO.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword());
    }
}
