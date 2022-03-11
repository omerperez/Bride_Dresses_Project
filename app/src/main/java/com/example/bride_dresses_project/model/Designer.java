package com.example.bride_dresses_project.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class Designer {
    @PrimaryKey
    @NonNull
    private String phone;
    private String fullName;
    private String password;
    private String streetAddress;
    private String state;
    private String country;
    private String image;
    Long updateDate = new Long(0);

    public Designer(){}

    public Designer(String fullName, String phone, String password, String streetAddress, String state, String country, String image) {
        this.fullName = fullName;
        this.phone = phone;
        this.password = password;
        this.streetAddress = streetAddress;
        this.state = state;
        this.country = country;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("phone", this.phone);
        json.put("fullName", this.fullName);
        json.put("password", this.password);
        json.put("streetAddress", this.streetAddress);
        json.put("state", this.state);
        json.put("country", this.country);
        json.put("image", this.image);

        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static Designer create(Map<String, Object> json) {
        String phone = (String) json.get("phone");
        String fullName = (String) json.get("fullName");
        String password = (String) json.get("password");
        String streetAddress = (String) json.get("streetAddress");
        String state = (String) json.get("state");
        String country = (String) json.get("country");
        String image = (String) json.get("image");

        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Designer designer = new Designer(fullName, phone, password, streetAddress, state, country, image);
        designer.setUpdateDate(updateDate);
        return designer;
    }

}
