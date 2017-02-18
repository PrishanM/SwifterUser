
package com.evensel.swyftr.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "name",
    "email",
    "phone_no",
    "location",
    "home_address",
    "office_address",
    "profile_image"
})
public class Details {

    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_no")
    private String phoneNo;
    @JsonProperty("location")
    private String location;
    @JsonProperty("home_address")
    private String homeAddress;
    @JsonProperty("office_address")
    private String officeAddress;
    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("phone_no")
    public String getPhoneNo() {
        return phoneNo;
    }

    @JsonProperty("phone_no")
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("home_address")
    public String getHomeAddress() {
        return homeAddress;
    }

    @JsonProperty("home_address")
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    @JsonProperty("office_address")
    public String getOfficeAddress() {
        return officeAddress;
    }

    @JsonProperty("office_address")
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    @JsonProperty("profile_image")
    public String getProfileImage() {
        return profileImage;
    }

    @JsonProperty("profile_image")
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
