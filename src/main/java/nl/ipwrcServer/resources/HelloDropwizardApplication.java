package nl.ipwrcServer.resources;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.resources.resources.HelloWorldResource;

public class HelloDropwizardApplication extends Application<HelloDropwizardConfiguration> {

    public static void main(String[] args) throws Exception{
        new HelloDropwizardApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<HelloDropwizardConfiguration> bootstrap){

    }

    @Override
    public void run(HelloDropwizardConfiguration helloDropwizardConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new HelloWorldResource());

    }
}
