package com.example.bride_dresses_project.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.UUID;


@Entity
public class Dress
{
    @PrimaryKey
    @NonNull
    String id;


    String price;
    String imageUrl;
    String type;
    boolean isAvailable;
    String userPhone;
    public Dress()
    {
        this.id = UUID.randomUUID().toString();
        type = "";
        price = "";
        imageUrl = "";
    }
    public Dress(String type,String price,String imageUrl)
    {
        this.id = UUID.randomUUID().toString();
        this.type=type;
        this.price=price+"$";
        this.imageUrl=imageUrl;
    }

    public String getIdOfUser() {
        return userPhone;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdOfUser(String idOfUser) {
        this.userPhone = idOfUser;
    }

    public String getType() {
        return type;
    }
    public String getId() {
        return id.toString();
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //---------------------------------//

}
