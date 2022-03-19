package com.example.bride_dresses_project.model.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.bride_dresses_project.ContextApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    private static final String LAST_UPDATED = "UserLastUpdated";
    public static final String UPDATE_FIELD = "updateDate";

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
    private Long updateDate = new Long(0);

    public User(){}

    @Ignore
    public User(String fullName, String email, String phone, String streetAddress, String state, String country) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.state = state;
        this.country = country;
    }

    public User(String id, String fullName, String email, String phone, String streetAddress, String state, String country) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Map<String, Object> convertUserToJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("email", this.email);
        json.put("phone", this.phone);
        json.put("fullName", this.fullName);
        json.put("streetAddress", this.streetAddress);
        json.put("state", this.state);
        json.put("country", this.country);
        json.put("imageUrl", imageUrl);
        json.put("updateDate", FieldValue.serverTimestamp());

        return json;
    }

    public static Long getLocalLastUpdated() {
        SharedPreferences sp = ContextApplication
                .getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sp.getLong(User.LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdated(Long timestamp) {
        SharedPreferences.Editor ed = ContextApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        ed.putLong(User.LAST_UPDATED, timestamp);
        ed.apply();
    }

}
