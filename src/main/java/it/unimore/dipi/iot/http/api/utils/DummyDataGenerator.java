package it.unimore.dipi.iot.http.api.utils;

import it.unimore.dipi.iot.http.api.model.DeviceDescriptor;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
import it.unimore.dipi.iot.http.api.persistence.IIoInventoryDataManager;

import java.util.HashMap;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 12:04
 */
public class DummyDataGenerator {

    public static void generateDummyLocations(IIoInventoryDataManager inventoryDataManager){

        try{
            LocationDescriptor mantovaLocation = new LocationDescriptor();
            mantovaLocation.setId("000001");
            mantovaLocation.setLatitude(45.160217);
            mantovaLocation.setLongitude(10.7877205);
            mantovaLocation.setName("Foundation Universita' di Mantova");
            mantovaLocation.setAddress("Via Angelo Scarsellini, 2, 46100");
            mantovaLocation.setCity("Mantova");
            mantovaLocation.setCountry("Italy");

            LocationDescriptor reggioEmiliaLocation = new LocationDescriptor();
            reggioEmiliaLocation.setId("000002");
            reggioEmiliaLocation.setLatitude(44.6875772);
            reggioEmiliaLocation.setLongitude(10.667381);
            reggioEmiliaLocation.setName("Dipartimento di Scienze e Metodi dell'Ingegneria  - UniMore");
            reggioEmiliaLocation.setAddress("via Giovanni Amendola, 2 - Padiglione Morselli, 42122");
            reggioEmiliaLocation.setCity("Reggio Emilia");
            reggioEmiliaLocation.setCountry("Italy");

            inventoryDataManager.createNewLocation(mantovaLocation);
            inventoryDataManager.createNewLocation(reggioEmiliaLocation);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void generateDummyDevices(IIoInventoryDataManager inventoryDataManager){

        try{

            DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
            deviceDescriptor.setUuid("device00001");
            deviceDescriptor.setLocationId("000001");
            deviceDescriptor.setType("dev:dummy:temperature");
            deviceDescriptor.setName("Demo Temperature Sensor");
            deviceDescriptor.setAttributes(new HashMap<String, Object>() {
                {
                    put("manufacturer", "ACME Corporation");
                    put("software_version", "0.0.1");
                    put("battery", false);
                    put("unit", "C");
                    put("max_value", 200);
                    put("min_value", -200);
                }
            });

            inventoryDataManager.createNewDevice(deviceDescriptor);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
