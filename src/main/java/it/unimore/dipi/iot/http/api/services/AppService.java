package it.unimore.dipi.iot.http.api.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.dipi.iot.http.api.resources.DeviceResource;
import it.unimore.dipi.iot.http.api.resources.LocationResource;
import it.unimore.dipi.iot.http.api.resources.UserResource;
import it.unimore.dipi.iot.http.api.utils.DummyDataGenerator;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class AppService extends Application<AppConfig> {

    public static void main(String[] args) throws Exception{

        new AppService().run(new String[]{"server", args.length > 0 ? args[0] : "configuration.yml"});
    }

    public void run(AppConfig appConfig, Environment environment) throws Exception {

        //Create Demo Locations, Device and Users
        DummyDataGenerator.generateDummyLocations(appConfig.getInventoryDataManager());
        DummyDataGenerator.generateDummyDevices(appConfig.getInventoryDataManager());
        DummyDataGenerator.generateDummyUsers(appConfig.getInventoryDataManager());

        //Add our defined resources
        environment.jersey().register(new LocationResource(appConfig));
        environment.jersey().register(new DeviceResource(appConfig));
        environment.jersey().register(new UserResource(appConfig));

        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }

    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<AppConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfig configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }
}