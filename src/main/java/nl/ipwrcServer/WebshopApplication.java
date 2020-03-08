package nl.ipwrcServer;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.dao.UserDAO;
import nl.ipwrcServer.model.User;
import nl.ipwrcServer.resources.UserResource;
import nl.ipwrcServer.service.AuthenticatorService;
import nl.ipwrcServer.service.AuthorizeService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static void main(String[] args) throws Exception{
        new WebshopApplication().run(args);
    }

    @Override
    public void run(WebshopConfiguration webshopConfiguration, Environment environment) throws Exception {
        registerResources(environment, settingUpJDBI(webshopConfiguration, environment));
    }

    public Jdbi settingUpJDBI(WebshopConfiguration webshopConfiguration, Environment environment){
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, webshopConfiguration.getDataSourceFactory(), "mysql");

        return jdbi;
    }

    public void registerResources(Environment environment, Jdbi jdbi){
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        environment.jersey().register(new UserResource(userDAO));

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new AuthenticatorService(userDAO))
                        .setAuthorizer(new AuthorizeService(userDAO))
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

}
