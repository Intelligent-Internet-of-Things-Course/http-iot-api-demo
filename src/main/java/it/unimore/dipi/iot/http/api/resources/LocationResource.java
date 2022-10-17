package it.unimore.dipi.iot.http.api.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.http.api.dto.LocationCreationRequest;
import it.unimore.dipi.iot.http.api.dto.LocationUpdateRequest;
import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerConflict;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
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

@Path("/api/iot/inventory/location")
@Api("IoT Location Inventory Endpoint")
public class LocationResource {

    final protected Logger logger = LoggerFactory.getLogger(LocationResource.class);

    @SuppressWarnings("serial")
    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public LocationResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get all available locations or filter according to city AND country")
    public Response getLocations(@Context ContainerRequestContext req,
                                 @QueryParam("city") String city,
                                 @QueryParam("country") String country) {

        try {

            logger.info("Loading all stored IoT Inventory Location filtered by City: {} and Country: {}", city, country);

            List<LocationDescriptor> locationList = null;

            //No filter applied
            if(city == null && country == null)
                locationList = this.conf.getInventoryDataManager().getLocationList();
            else if(city != null && country != null)
                locationList = this.conf.getInventoryDataManager().getLocationListByCity(city, country);
            else
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"City and Country are both required for filtering !")).build();

            if(locationList == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Locations Not Found !")).build();

            return Response.ok(locationList).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{location_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get a Single Location")
    public Response getLocation(@Context ContainerRequestContext req,
                                @PathParam("location_id") String locationId) {

        try {

            logger.info("Loading Location Info for id: {}", locationId);

            //Check the request
            if(locationId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Location Id Provided !")).build();

            Optional<LocationDescriptor> locationDescriptor = this.conf.getInventoryDataManager().getLocation(locationId);

            if(!locationDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Location Not Found !")).build();

            return Response.ok(locationDescriptor.get()).build();

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
    public Response createLocation(@Context ContainerRequestContext req,
                                   @Context UriInfo uriInfo,
                                   LocationCreationRequest locationCreationRequest) {

        try {

            logger.info("Incoming Location Creation Request: {}", locationCreationRequest);

            //Check the request
            if(locationCreationRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload")).build();

            LocationDescriptor newLocationDescriptor = (LocationDescriptor)locationCreationRequest;
            //Reset Id to null. The ID can not be provided by the client. It is generated by the server
            newLocationDescriptor.setId(null);

            newLocationDescriptor = this.conf.getInventoryDataManager().createNewLocation(newLocationDescriptor);

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),newLocationDescriptor.getId()))).build();

        } catch (IoTInventoryDataManagerConflict e){
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.CONFLICT.getStatusCode(),"Location with the same name already available !")).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/{location_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Update an existing Location")
    public Response updateLocation(@Context ContainerRequestContext req,
                                   @Context UriInfo uriInfo,
                                   @PathParam("location_id") String locationId,
                                   LocationUpdateRequest locationUpdateRequest) {

        try {

            logger.info("Incoming Location ({}) Update Request: {}", locationId, locationUpdateRequest);

            //Check if the request is valid
            if(locationUpdateRequest == null || !locationUpdateRequest.getId().equals(locationId))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Location Id")).build();

            //Check if the device is available and correctly registered otherwise a 404 response will be sent to the client
            if(!this.conf.getInventoryDataManager().getLocation(locationId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Location not found !")).build();

            LocationDescriptor locationDescriptor = (LocationDescriptor) locationUpdateRequest;
            this.conf.getInventoryDataManager().updateLocation(locationDescriptor);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/{location_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Delete a Single Location")
    public Response deleteDevice(@Context ContainerRequestContext req,
                                 @PathParam("location_id") String locationId) {

        try {

            logger.info("Deleting Location with id: {}", locationId);

            //Check the request
            if(locationId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Location Id Provided !")).build();

            //Check if the device is available or not
            if(!this.conf.getInventoryDataManager().getLocation(locationId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Location Not Found !")).build();

            //Delete the location
            this.conf.getInventoryDataManager().deleteLocation(locationId);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

}

