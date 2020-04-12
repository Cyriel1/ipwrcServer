package nl.ipwrcServer;

import io.dropwizard.Application;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.service.RegisterMappersService;
import nl.ipwrcServer.service.RegisterResourcesService;
import org.jdbi.v3.core.Jdbi;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static void main(String[] args) throws Exception{
        new WebshopApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<WebshopConfiguration> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(WebshopConfiguration webshopConfiguration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, webshopConfiguration.getDataSourceFactory(), "mysql");
        new RegisterResourcesService(jdbi, environment, webshopConfiguration).bundle();
        new RegisterMappersService(jdbi).registerMappersToModels();
    }

}
