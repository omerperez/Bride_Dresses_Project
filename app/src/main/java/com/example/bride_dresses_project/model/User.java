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
    private String id;
    private String email;
    private String phone;
    private String fullName;
    private String streetAddress;
    private String state;
    private String country;
    private String imageUrl;

    Long updateDate = new Long(0);

    public User(){}

    public User(String id,String email,String fullName, String phone, String streetAddress, String state, String country) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.state = state;
        this.country = country;
    }

    public User(String email,String fullName, String phone, String streetAddress, String state, String country) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.state = state;
        this.country = country;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
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
        json.put("email", this.email);
        json.put("phone", this.phone);
        json.put("fullName", this.fullName);
        json.put("streetAddress", this.streetAddress);
        json.put("state", this.state);
        json.put("country", this.country);
        json.put("imageUrl", imageUrl);
        Log.d("tag",imageUrl);

        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }
/*
    public static User create(Map<String, Object> json) {
        String email = (String) json.get("email");
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

        User user = new User(id,email,fullName, phone, password, streetAddress, state, country);
        user.setUpdateDate(updateDate);
        user.setImageUrl(imageUrl);
        Log.d("tag","img"+imageUrl);
        return user;
    }

 */

}
