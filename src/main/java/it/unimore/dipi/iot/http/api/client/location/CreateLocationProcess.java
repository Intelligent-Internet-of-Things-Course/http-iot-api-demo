package it.unimore.dipi.iot.http.api.client.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 16:53
 */
public class CreateLocationProcess {

    final static protected Logger logger = LoggerFactory.getLogger(CreateLocationProcess.class);

    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;
    private String baseUrl;

    public CreateLocationProcess(String baseUrl){
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.custom()
                .build();
    }

    public void createNewLocation(LocationDescriptor locationDescriptor){

        try{

            String targetUrl = String.format("%slocation", this.baseUrl);

            logger.info("Target Url: {}", targetUrl);

            String jsonBody = this.objectMapper.writeValueAsString(locationDescriptor);

            //Create the HTTP Post Request
            HttpPost createLocationRequest = new HttpPost(targetUrl);

            //Add Content Type Header
            createLocationRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            //Set Payload
            createLocationRequest.setEntity(new StringEntity(jsonBody));

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(createLocationRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED){

                //Obtain response body as a String
                String bodyString = EntityUtils.toString(response.getEntity());

                //Extract the Location Header
                String locationHeader = Arrays.stream(response.getHeaders("Location")).findFirst().get().getValue();

                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
                logger.info("Response Location Header: {}", locationHeader);
                logger.info("Raw Response Body: {}", bodyString);
            }
            else {
                logger.error(String.format("Error executing the request ! Status Code: %d -> Response Body: %s",
                        response != null ? response.getStatusLine().getStatusCode() : -1,
                        response != null ? EntityUtils.toString(response.getEntity()) : null));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        logger.info("Starting IoT Inventory Location Create Tester ...");

        String baseUrl = "http://127.0.0.1:7070/api/iot/inventory/";

        Random random = new Random();
        int randomLocationNumber = random.nextInt();

        LocationDescriptor randomLocation = new LocationDescriptor();
        randomLocation.setLatitude(40.6892494);
        randomLocation.setLongitude(-74.0466891);
        randomLocation.setName(String.format("Location - %d", randomLocationNumber));
        randomLocation.setAddress(String.format("Avenue %d", randomLocationNumber));
        randomLocation.setCity("Los Angeles");
        randomLocation.setCountry("USA");

        CreateLocationProcess apiLocationProcess = new CreateLocationProcess(baseUrl);

        //Create new Location
        apiLocationProcess.createNewLocation(randomLocation);
    }

}
