package nl.ipwrcServer.resources;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.resources.dao.UserDAO;
import nl.ipwrcServer.resources.resources.UserResource;
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
    }

}
