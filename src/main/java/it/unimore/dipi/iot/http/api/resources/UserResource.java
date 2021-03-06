package it.unimore.dipi.iot.http.api.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.http.api.dto.UserCreationRequest;
import it.unimore.dipi.iot.http.api.model.UserDescriptor;
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

@Path("/api/iot/inventory/user")
@Api("IoT Inventory User Endpoint")
public class UserResource {

    final protected Logger logger = LoggerFactory.getLogger(UserResource.class);

    @SuppressWarnings("serial")
    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public UserResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get all registered IoT Users")
    public Response getUsers(@Context ContainerRequestContext req) {

        try {

            logger.info("Loading all stored IoT Inventory Users ...");

            List<UserDescriptor> userList = this.conf.getInventoryDataManager().getUserList();

            if(userList == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Devices Not Found !")).build();

            return Response.ok(userList).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{user_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get User by Id")
    public Response getUserById(@Context ContainerRequestContext req,
                                @PathParam("user_id") String userId) {

        try {

            logger.info("Loading User Info for id: {}", userId);

            //Check the request
            if(userId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid User Id Provided !")).build();

            Optional<UserDescriptor> userDescriptor = this.conf.getInventoryDataManager().getUserById(userId);

            if(!userDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device Not Found !")).build();

            return Response.ok(userDescriptor.get()).build();

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
    @ApiOperation(value="Create a new User")
    public Response createUser(@Context ContainerRequestContext req,
                               @Context UriInfo uriInfo,
                               UserCreationRequest userCreationRequest) {

        try {

            logger.info("Incoming User Creation Request: {}", userCreationRequest);

            //Check the request
            if(userCreationRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload")).build();

            UserDescriptor userDescriptor = (UserDescriptor)userCreationRequest;
            //Reset Id to null. The ID can not be provided by the client. It is generated by the server
            userDescriptor.setInternalId(null);
            userDescriptor = this.conf.getInventoryDataManager().createNewUser(userDescriptor);

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),userDescriptor.getInternalId()))).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    //TODO Add PUT and DELETE Methods

}

