package it.unimore.dipi.iot.http.api.client.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import it.unimore.dipi.iot.http.api.model.DeviceDescriptor;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
public class GetDevicesProcess {

    final static protected Logger logger = LoggerFactory.getLogger(GetDevicesProcess.class);

    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;
    private String baseUrl;

    private final String RESOURCE_PATH = "device";

    public GetDevicesProcess(String baseUrl){
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.custom()
                .build();
    }

    public void getDevice(String deviceId){

        try{

            String targetUrl = String.format("%s%s/%s", this.baseUrl, RESOURCE_PATH, deviceId);

            //Create the HTTP GET Request
            HttpGet getRequest = new HttpGet(targetUrl);

            //Add Request Header
            getRequest.addHeader(HttpHeaders.USER_AGENT, "DemoIoTInventoryClient-0.0.1");

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(getRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                //Obtain response body as a String
                String bodyString = EntityUtils.toString(response.getEntity());

                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
                logger.info("Raw Response Body: {}", bodyString);

                //Deserialize Json String and Log obtained Devices
                DeviceDescriptor deviceDescriptor = this.objectMapper.readValue(bodyString, DeviceDescriptor.class);
                logger.info("Device Descriptor for Id: {} -> {}", deviceId, deviceDescriptor);

            }
            else {
                logger.error(String.format("Error executing the request ! Status Code: %d", response != null ? response.getStatusLine().getStatusCode() : -1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getDeviceList(){

        try{

            String targetUrl = String.format("%s%s", this.baseUrl, RESOURCE_PATH);

            //Create the HTTP GET Request
            HttpGet getListRequest = new HttpGet(targetUrl);

            //Add Request Header
            getListRequest.addHeader(HttpHeaders.USER_AGENT, "DemoIoTInventoryClient-0.0.1");

            //Execute the GetRequest
            CloseableHttpResponse response = httpClient.execute(getListRequest);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                //Obtain response body as a String
                String bodyString = EntityUtils.toString(response.getEntity());

                logger.info("Response Code: {}", response.getStatusLine().getStatusCode());
                logger.info("Raw Response Body: {}", bodyString);

                //Deserialize Json String and Log obtained devices
                //Define Custom List Type
                CollectionType myListType = this.objectMapper.getTypeFactory().constructCollectionType(List.class, DeviceDescriptor.class);
                List<DeviceDescriptor> deviceList = this.objectMapper.readValue(bodyString, myListType);
                for(DeviceDescriptor deviceDescriptor : deviceList)
                    logger.info("Device Descriptor: {}", deviceDescriptor);

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

        GetDevicesProcess apiDeviceProcess = new GetDevicesProcess(baseUrl);
        apiDeviceProcess.getDeviceList();
        apiDeviceProcess.getDevice("device00001");
    }

}
