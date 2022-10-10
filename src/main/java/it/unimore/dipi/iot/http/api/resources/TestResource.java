package it.unimore.dipi.iot.http.api.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.http.api.model.DeviceDescriptor;
import it.unimore.dipi.iot.http.api.services.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/iot/inventory/test")
public class TestResource {

    private AppConfig conf;

    public TestResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevices(@Context ContainerRequestContext req) {

        try {

            DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
            deviceDescriptor.setUuid("testDevice00001");
            deviceDescriptor.setName("TestDevice");
            deviceDescriptor.setLocationId("location0001");
            deviceDescriptor.setType("demo-device");

            return Response.ok(deviceDescriptor).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

}
