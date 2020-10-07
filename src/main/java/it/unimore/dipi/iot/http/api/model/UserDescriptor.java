package it.unimore.dipi.iot.http.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 07/10/2020 - 12:35
 */
public class UserDescriptor {

    @JsonProperty("id")
    private String internalId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private String role = "GUEST";

    @JsonProperty("email")
    private String emailAddress;

    @JsonProperty("allowed_locations")
    private List<String> allowedLocationIdList;

    @JsonProperty("allowed_devices")
    private List<String> allowedDeviceIdList;

    public UserDescriptor() {
    }

    public UserDescriptor(String internalId, String username, String password, String role, String emailAddress, List<String> allowedLocationIdList, List<String> allowedDeviceIdList) {
        this.internalId = internalId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.emailAddress = emailAddress;
        this.allowedLocationIdList = allowedLocationIdList;
        this.allowedDeviceIdList = allowedDeviceIdList;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getAllowedLocationIdList() {
        return allowedLocationIdList;
    }

    public void setAllowedLocationIdList(List<String> allowedLocationIdList) {
        this.allowedLocationIdList = allowedLocationIdList;
    }

    public List<String> getAllowedDeviceIdList() {
        return allowedDeviceIdList;
    }

    public void setAllowedDeviceIdList(List<String> allowedDeviceIdList) {
        this.allowedDeviceIdList = allowedDeviceIdList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserDescriptor{");
        sb.append("internalId='").append(internalId).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", allowedLocationIdList=").append(allowedLocationIdList);
        sb.append(", allowedDeviceIdList=").append(allowedDeviceIdList);
        sb.append('}');
        return sb.toString();
    }
}
