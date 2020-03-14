package nl.ipwrcServer;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.persistence.dao.ProductDAO;
import nl.ipwrcServer.persistence.dao.UserDAO;
import nl.ipwrcServer.model.User;
import nl.ipwrcServer.resources.ProductResource;
import nl.ipwrcServer.resources.UserResource;
import nl.ipwrcServer.service.AuthenticatorService;
import nl.ipwrcServer.service.AuthorizeService;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static void main(String[] args) throws Exception{
        new WebshopApplication().run(args);
    }

    @Override
    public void run(WebshopConfiguration webshopConfiguration, Environment environment) throws Exception {
        registerResources(environment, settingUpJDBI(webshopConfiguration, environment));
        configureCORS(environment);
    }

    public Jdbi settingUpJDBI(WebshopConfiguration webshopConfiguration, Environment environment){
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, webshopConfiguration.getDataSourceFactory(), "mysql");

        return jdbi;
    }

    public void registerResources(Environment environment, Jdbi jdbi){
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        final ProductDAO productDAO = jdbi.onDemand(ProductDAO.class);
        environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new ProductResource(productDAO));

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new AuthenticatorService(userDAO))
                        .setAuthorizer(new AuthorizeService(userDAO))
                        .setRealm("WEBSHOP ACCOUNT")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

    public void configureCORS(Environment environment){
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());
    }

}
