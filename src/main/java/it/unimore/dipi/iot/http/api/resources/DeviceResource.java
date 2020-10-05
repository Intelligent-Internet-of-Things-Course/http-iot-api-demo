package it.unimore.dipi.iot.http.api.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.http.api.dto.DeviceCreationRequest;
import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerConflict;
import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerException;
import it.unimore.dipi.iot.http.api.model.DeviceDescriptor;
import it.unimore.dipi.iot.http.api.services.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/api/iot/inventory/device")
@Api("IoT Inventory Device Endpoint")
public class DeviceResource {


    final protected Logger logger = LoggerFactory.getLogger(DeviceResource.class);

    @SuppressWarnings("serial")
    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public DeviceResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get all available IoT devices or filter according to their")
    public Response getDevices(@Context ContainerRequestContext req,
                               @QueryParam("type") String deviceType,
                               @QueryParam("locationId") String locationId) {

        try {

            logger.info("Loading all stored IoT Inventory Devices filtered by Type: {}", deviceType);

            List<DeviceDescriptor> deviceList = null;

            //No filter applied
            if(deviceType == null)
                deviceList = this.conf.getInventoryDataManager().getDeviceList();
            else
                deviceList = this.conf.getInventoryDataManager().getDeviceListByFilter(deviceType, locationId);

            if(deviceList == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Devices Not Found !")).build();

            return Response.ok(deviceList).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{device_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get a Single Location")
    public Response getDevice(@Context ContainerRequestContext req,
                              @PathParam("device_id") String deviceId) {

        try {

            logger.info("Loading Device Info for id: {}", deviceId);

            //Check the request
            if(deviceId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Device Id Provided !")).build();

            Optional<DeviceDescriptor> deviceDescriptor = this.conf.getInventoryDataManager().getDevice(deviceId);

            if(!deviceDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device Not Found !")).build();

            return Response.ok(deviceDescriptor.get()).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @POST
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Create a new Location")
    public Response createDevice(@Context ContainerRequestContext req,
                                   @Context UriInfo uriInfo,
                                   DeviceCreationRequest deviceCreationRequest) {

        try {

            logger.info("Incoming Device Creation Request: {}", deviceCreationRequest);

            //Check the request
            if(!isDeviceCreationRequestValid(deviceCreationRequest))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload ! Check UUID and/or LocationId")).build();

            DeviceDescriptor newDeviceDescriptor = (DeviceDescriptor)deviceCreationRequest;
            newDeviceDescriptor = this.conf.getInventoryDataManager().createNewDevice(newDeviceDescriptor);

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),newDeviceDescriptor.getUuid()))).build();

        } catch (IoTInventoryDataManagerConflict e){
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.CONFLICT.getStatusCode(),"Location with the same name already available !")).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    private boolean isDeviceCreationRequestValid(DeviceCreationRequest deviceCreationRequest) throws IoTInventoryDataManagerException {

        if(deviceCreationRequest != null &&
                deviceCreationRequest.getUuid() != null &&
                isDeviceUuidValid(deviceCreationRequest.getUuid()) &&
                (deviceCreationRequest.getLocationId() != null && this.conf.getInventoryDataManager().getLocation(deviceCreationRequest.getLocationId()).isPresent())
        )
            return true;
        else
            return false;
    }

    private boolean isDeviceUuidValid(String deviceUuid){
        return !deviceUuid.contains(" ");
    }

}

