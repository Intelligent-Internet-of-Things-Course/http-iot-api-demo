package it.unimore.dipi.iot.http.api.persistence;

import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerConflict;
import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerException;
import it.unimore.dipi.iot.http.api.model.DeviceDescriptor;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Demo IoT Inventory Data Manager handling all data in a local cache implemented through Maps and Lists
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:48
 */
public class DefaultIotInventoryDataManger implements IIoInventoryDataManager{

    private HashMap<String, LocationDescriptor> locationMap;
    private HashMap<String, DeviceDescriptor> deviceMap;

    public DefaultIotInventoryDataManger(){
        this.locationMap = new HashMap<>();
        this.deviceMap = new HashMap<>();
    }

    //LOCATION MANAGEMENT

    @Override
    public List<LocationDescriptor> getLocationList() throws IoTInventoryDataManagerException {
        return new ArrayList<>(this.locationMap.values());
    }

    @Override
    public List<LocationDescriptor> getLocationListByCity(String city, String country) throws IoTInventoryDataManagerException {
        return this.locationMap.values().stream()
                .filter(locationDescriptor -> locationDescriptor != null && locationDescriptor.getCity().equals(city) && locationDescriptor.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LocationDescriptor> getLocationByName(String name) throws IoTInventoryDataManagerException {
        return this.locationMap.values().stream()
                .filter(locationDescriptor -> locationDescriptor.getName().equals(name))
                .findAny();
    }

    @Override
    public Optional<LocationDescriptor> getLocation(String locationId) throws IoTInventoryDataManagerException {
        return Optional.of(this.locationMap.get(locationId));
    }

    @Override
    public LocationDescriptor createNewLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {

        if(locationDescriptor.getName() != null && this.getLocationByName(locationDescriptor.getName()).isPresent())
            throw new IoTInventoryDataManagerConflict("Location with the same name already available!");

        //Set the locationId to a random UUID value
        if(locationDescriptor.getId() == null)
            locationDescriptor.setId(UUID.randomUUID().toString());

        this.locationMap.put(locationDescriptor.getId(), locationDescriptor);
        return locationDescriptor;
    }

    @Override
    public LocationDescriptor updateLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException {
        this.locationMap.put(locationDescriptor.getId(), locationDescriptor);
        return locationDescriptor;
    }

    @Override
    public LocationDescriptor deleteLocation(String locationId) throws IoTInventoryDataManagerException {
        return this.locationMap.remove(locationId);
    }

    //DEVICE MANAGEMENT

    @Override
    public List<DeviceDescriptor> getDeviceList() throws IoTInventoryDataManagerException {
        return new ArrayList<>(this.deviceMap.values());
    }

    @Override
    public List<DeviceDescriptor> getDeviceListByFilter(String deviceType, String locationId) throws IoTInventoryDataManagerException {

        List<DeviceDescriptor> resultList = this.getDeviceList();

        if(deviceType != null)
            resultList = this.deviceMap.values().stream()
                .filter(deviceDescriptor -> deviceDescriptor != null && deviceDescriptor.getType().equals(deviceType))
                .collect(Collectors.toList());

        if(locationId != null)
            resultList = this.deviceMap.values().stream()
                    .filter(deviceDescriptor -> deviceDescriptor != null && deviceDescriptor.getLocationId().equals(locationId))
                    .collect(Collectors.toList());

        return resultList;

    }

    @Override
    public Optional<DeviceDescriptor> getDeviceByUuid(String deviceUuid) throws IoTInventoryDataManagerException {
        return this.deviceMap.values().stream()
                .filter(deviceDescriptor -> deviceDescriptor.getUuid().equals(deviceUuid))
                .findAny();
    }

    @Override
    public Optional<DeviceDescriptor> getDevice(String deviceId) throws IoTInventoryDataManagerException {
        return Optional.of(this.deviceMap.get(deviceId));
    }

    @Override
    public DeviceDescriptor createNewDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {

        if(deviceDescriptor != null && this.getDeviceByUuid(deviceDescriptor.getUuid()).isPresent())
            throw new IoTInventoryDataManagerConflict("Device with the same UUID already available!");

        this.deviceMap.put(deviceDescriptor.getUuid(), deviceDescriptor);
        return deviceDescriptor;
    }

    @Override
    public DeviceDescriptor updateDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException {
        this.deviceMap.put(deviceDescriptor.getUuid(), deviceDescriptor);
        return deviceDescriptor;
    }

    @Override
    public DeviceDescriptor deleteDevice(String deviceId) throws IoTInventoryDataManagerException {
        return this.deviceMap.remove(deviceId);
    }
}
