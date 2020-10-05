package it.unimore.dipi.iot.http.api.model;

import java.util.Map;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:38
 */
public class DeviceDescriptor {

    private String uuid;

    private String name;

    private String locationId;

    private String type;

    private Map<String, Object> attributes;

    public DeviceDescriptor() {
    }

    public DeviceDescriptor(String uuid, String name, String locationId, String type, Map<String, Object> attributes) {
        this.uuid = uuid;
        this.name = name;
        this.locationId = locationId;
        this.type = type;
        this.attributes = attributes;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeviceDescriptor{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", locationId='").append(locationId).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }
}

