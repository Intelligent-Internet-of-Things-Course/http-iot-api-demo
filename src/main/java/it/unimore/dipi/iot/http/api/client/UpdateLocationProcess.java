package it.unimore.dipi.iot.http.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
public class UpdateLocationProcess {

    final static protected Logger logger = LoggerFactory.getLogger(UpdateLocationProcess.class);

    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;
    private String baseUrl;

    public UpdateLocationProcess(String baseUrl){
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.custom()
                .build();
    }

    public void updateExistingLocation(LocationDescriptor locationDescriptor){

        try{

            //Update the URL using the Location Id
            String targetUrl = String.format("%slocation/%s", this.baseUrl, locationDescriptor.getId());

            logger.info("Target Url: {}", targetUrl);

            String jsonBody = this.objectMapper.writeValueAsString(locationDescriptor);

            //Create the HTTP GET Request
            HttpPut updateLocationRequest = new HttpPut(targetUrl);

            //Add Content Type Header
            updateLocationRequest.addHeader("content-type", "application/json");

            //Set Payload
            updateLocationRequest.setEntity(new StringEntity(jsonBody));

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(updateLocationRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT){
                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
            }
            else {
                logger.error(String.format("Error executing the request ! Status Code: %d", response != null ? response.getStatusLine().getStatusCode() : -1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        logger.info("Starting IoT Inventory Create & Update Tester ...");

        String baseUrl = "http://127.0.0.1:7070/api/iot/inventory/";

        LocationDescriptor mantovaLocation = new LocationDescriptor();
        mantovaLocation.setId("000001");
        mantovaLocation.setLatitude(45.160217);
        mantovaLocation.setLongitude(10.7877205);
        mantovaLocation.setName("Foundation Universita' di Mantova (FUM)");
        mantovaLocation.setAddress("Via Angelo Scarsellini, 2, 46100");
        mantovaLocation.setCity("Mantova");
        mantovaLocation.setCountry("Italy");

        UpdateLocationProcess apiLocationProcess = new UpdateLocationProcess(baseUrl);

        //Update the Location
        apiLocationProcess.updateExistingLocation(mantovaLocation);

    }

}
