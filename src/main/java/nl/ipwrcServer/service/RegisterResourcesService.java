package nl.ipwrcServer.service;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.model.User;
import nl.ipwrcServer.persistence.ProductDAO;
import nl.ipwrcServer.persistence.UserDAO;
import nl.ipwrcServer.resources.ProductResource;
import nl.ipwrcServer.resources.UserResource;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;

public class RegisterResourcesService {

    private Jdbi jdbi;
    private Environment environment;

    private UserDAO userDAO;
    private ProductDAO productDAO;

    public RegisterResourcesService(Jdbi jdbi, Environment environment){
        this.jdbi = jdbi;
        this.environment = environment;
    }

    public void bundle(){
        attachingDaoToJdbi();
        registerResources();
        registerAuthentication();
    }

    public void attachingDaoToJdbi(){
        userDAO = jdbi.onDemand(UserDAO.class);
        productDAO = jdbi.onDemand(ProductDAO.class);
    }

    public void registerResources(){
        environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new ProductResource(productDAO));
    }

    public void registerAuthentication(){
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new AuthenticatorService(userDAO))
                        .setAuthorizer(new AuthorizeService(userDAO))
                        .setRealm("WEBSHOP ACCOUNT")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

}
