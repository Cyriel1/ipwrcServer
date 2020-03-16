package nl.ipwrcServer;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.service.RegisterMappersService;
import nl.ipwrcServer.service.RegisterResourcesService;
import org.eclipse.jetty.servlets.CrossOriginFilter;
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
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, webshopConfiguration.getDataSourceFactory(), "mysql");
        new RegisterResourcesService(jdbi, environment).bundle();
        new RegisterMappersService(jdbi).registerMappersToModels();

        configureCORS(environment);
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
