package it.unimore.dipi.iot.http.api.persistence;

import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerConflict;
import it.unimore.dipi.iot.http.api.exception.IoTInventoryDataManagerException;
import it.unimore.dipi.iot.http.api.model.DeviceDescriptor;
import it.unimore.dipi.iot.http.api.model.LocationDescriptor;
import it.unimore.dipi.iot.http.api.model.UserDescriptor;

import java.util.List;
import java.util.Optional;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:44
 */
public interface IIoInventoryDataManager {

    //Location Management

    public List<LocationDescriptor> getLocationList() throws IoTInventoryDataManagerException;

    public List<LocationDescriptor> getLocationListByCity(String city, String country) throws IoTInventoryDataManagerException;

    public Optional<LocationDescriptor> getLocationByName(String name) throws IoTInventoryDataManagerException;

    public Optional<LocationDescriptor> getLocation(String locationId) throws IoTInventoryDataManagerException;

    public LocationDescriptor createNewLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public LocationDescriptor updateLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException;

    public LocationDescriptor deleteLocation(String locationId) throws IoTInventoryDataManagerException;

    //Device Management

    public List<DeviceDescriptor> getDeviceList() throws IoTInventoryDataManagerException;

    public List<DeviceDescriptor> getDeviceListByFilter(String deviceType, String locationId) throws IoTInventoryDataManagerException;

    public Optional<DeviceDescriptor> getDeviceByUuid(String deviceUuid) throws IoTInventoryDataManagerException;

    public Optional<DeviceDescriptor> getDevice(String deviceId) throws IoTInventoryDataManagerException;

    public DeviceDescriptor createNewDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public DeviceDescriptor updateDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException;

    public DeviceDescriptor deleteDevice(String deviceId) throws IoTInventoryDataManagerException;

    //User Management
    public List<UserDescriptor> getUserList() throws IoTInventoryDataManagerException;

    public Optional<UserDescriptor> getUserById(String userId) throws IoTInventoryDataManagerException;

    //public Optional<UserDescriptor> getUserByEmail() throws IoTInventoryDataManagerException;

    public UserDescriptor createNewUser(UserDescriptor userDescriptor) throws IoTInventoryDataManagerException;

    public UserDescriptor updateUser(UserDescriptor userDescriptor) throws IoTInventoryDataManagerException;

    public UserDescriptor deleteUser(String userId) throws IoTInventoryDataManagerException;

}
