package it.unimore.dipi.iot.http.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 16:53
 */
public class GetLocationsProcess {

    final static protected Logger logger = LoggerFactory.getLogger(GetLocationsProcess.class);

    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;
    private String baseUrl;

    public GetLocationsProcess(String baseUrl){
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.custom()
                .build();
    }

    public void getLocation(String locationId){

        try{

            String targetUrl = String.format("%slocation/%s", this.baseUrl, locationId);

            //Create the HTTP GET Request
            HttpGet getLocationListRequest = new HttpGet(targetUrl);

            //Add Request Header
            getLocationListRequest.addHeader(HttpHeaders.USER_AGENT, "DemoIoTInventoryClient-0.0.1");

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(getLocationListRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                //Obtain response body as a String
                String bodyString = EntityUtils.toString(response.getEntity());

                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
                logger.info("Raw Response Body: {}", bodyString);

                //Deserialize Json String and Log obtained locations
                //Define Custom List Type
                LocationDescriptor locationDescriptor = this.objectMapper.readValue(bodyString, LocationDescriptor.class);
                logger.info("Location Descriptor for Id: {} -> {}", locationId, locationDescriptor);

            }
            else {
                logger.error(String.format("Error executing the request ! Status Code: %d", response != null ? response.getStatusLine().getStatusCode() : -1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void getLocationList(){

        try{

            String targetUrl = String.format("%slocation", this.baseUrl);

            //Create the HTTP GET Request
            HttpGet getLocationListRequest = new HttpGet(targetUrl);

            //Add Request Header
            getLocationListRequest.addHeader(HttpHeaders.USER_AGENT, "DemoIoTInventoryClient-0.0.1");

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(getLocationListRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                //Obtain response body as a String
                String bodyString = EntityUtils.toString(response.getEntity());

                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
                logger.info("Raw Response Body: {}", bodyString);

                //Deserialize Json String and Log obtained locations
                //Define Custom List Type
                CollectionType myListType = this.objectMapper.getTypeFactory().constructCollectionType(List.class, LocationDescriptor.class);
                List<LocationDescriptor> locationList = this.objectMapper.readValue(bodyString, myListType);
                for(LocationDescriptor locationDescriptor : locationList)
                    logger.info("Location Descriptor: {}", locationDescriptor);

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

    public void getLocationList(String city, String country){

        try{

            String targetUrl = String.format("%slocation", this.baseUrl);

            // URI Builder with Parameters
            URIBuilder builder = new URIBuilder(targetUrl);
            builder.setParameter("city", city)
                    .setParameter("country", country);

            logger.info("URI: {}", builder.toString());

            //Create the HTTP GET Request
            HttpGet getLocationListRequest = new HttpGet(builder.build());

            //Add Request Header
            getLocationListRequest.addHeader(HttpHeaders.USER_AGENT, "DemoIoTInventoryClient-0.0.1");

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(getLocationListRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                //Obtain response body as a String
                String bodyString = EntityUtils.toString(response.getEntity());

                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
                logger.info("Raw Response Body: {}", bodyString);

                //Deserialize Json String and Log obtained locations
                //Define Custom List Type
                CollectionType myListType = this.objectMapper.getTypeFactory().constructCollectionType(List.class, LocationDescriptor.class);
                List<LocationDescriptor> locationList = this.objectMapper.readValue(bodyString, myListType);
                for(LocationDescriptor locationDescriptor : locationList)
                    logger.info("Location Descriptor: {}", locationDescriptor);

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
        logger.info("Starting IoT Inventory GET Tester ...");

        String baseUrl = "http://127.0.0.1:7070/api/iot/inventory/";

        GetLocationsProcess apiLocationProcess = new GetLocationsProcess(baseUrl);
        apiLocationProcess.getLocationList();
        apiLocationProcess.getLocationList("Mantova", "Italy");
        apiLocationProcess.getLocation("000001");
    }

}
