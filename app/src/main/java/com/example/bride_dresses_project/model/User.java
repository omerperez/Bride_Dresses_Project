package com.example.bride_dresses_project.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String phone;
    private String fullName;
    private String password;
    private String streetAddress;
    private String state;
    private String country;
    private String imageUrl;

    Long updateDate = new Long(0);

    public User(){}

    public User(String fullName, String phone, String password, String streetAddress, String state, String country) {
        this.fullName = fullName;
        this.phone = phone;
        this.password = password;
        this.streetAddress = streetAddress;
        this.state = state;
        this.country = country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        json.put("imageUrl", imageUrl);
        Log.d("tag",imageUrl);

        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static User create(Map<String, Object> json) {
        String phone = (String) json.get("phone");
        String fullName = (String) json.get("fullName");
        String password = (String) json.get("password");
        String streetAddress = (String) json.get("streetAddress");
        String state = (String) json.get("state");
        String country = (String) json.get("country");
        String imageUrl = (String) json.get("imageUrl");
        Log.d("tag",imageUrl);

        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        User user = new User(fullName, phone, password, streetAddress, state, country);
        user.setUpdateDate(updateDate);
        user.setImageUrl(imageUrl);
        Log.d("tag","img"+imageUrl);
        return user;
    }

}
