package it.unimore.dipi.iot.http.api.model;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 04/10/2020 - 17:01
 */
public class LocationDescriptor {

    private String id;

    private double latitude;

    private double longitude;

    private String city;

    private String address;

    private String country;

    private String name;

    public LocationDescriptor() {
    }

    public LocationDescriptor(String id, double latitude, double longitude, String city, String address, String country, String name) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.address = address;
        this.country = country;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LocationDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", city='").append(city).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
