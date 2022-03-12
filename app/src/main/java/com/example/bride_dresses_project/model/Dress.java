package com.example.bride_dresses_project.model;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.UUID;

public class Dress
{
    String id;
    String price;
    String imageUrl;
    String type;
    boolean isAvailable;
    Designer designer;
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
