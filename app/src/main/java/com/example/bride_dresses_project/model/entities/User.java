package com.example.bride_dresses_project.model;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String LAST_UPDATED = "UserLastUpdated";
    public static final String UPDATE_FIELD = "updateDate";

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
        json.put("email", email);
        json.put("phone", phone);
        json.put("fullName",fullName);
        json.put("streetAddress", streetAddress);
        json.put("state", state);
        json.put("country", country);
        json.put("imageUrl", imageUrl);

        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static User create(Map<String, Object> json) {
        String email = (String) json.get("email");
        String phone = (String) json.get("phone");
        String fullName = (String) json.get("fullName");
        String streetAddress = (String) json.get("streetAddress");
        String state = (String) json.get("state");
        String country = (String) json.get("country");
        String imageUrl = (String) json.get("imageUrl");

        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        User user = new User(email,fullName, phone, streetAddress, state, country);
        user.setUpdateDate(updateDate);
        user.setImageUrl(imageUrl);
        Log.d("tag","img"+imageUrl);
        return user;
    }

    public static void setLocalLastUpdated(Long timestamp) {
        SharedPreferences.Editor ed = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        ed.putLong(User.LAST_UPDATED, timestamp);
        ed.apply();
    }

    public static Long getLocalLastUpdated() {
        SharedPreferences sp = MyApplication
                .getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sp.getLong(User.LAST_UPDATED, 0);
    }


}
