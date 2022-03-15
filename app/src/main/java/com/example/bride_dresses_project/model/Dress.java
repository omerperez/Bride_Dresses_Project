package com.example.bride_dresses_project.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dress
{
    @PrimaryKey
    @NonNull
    Integer id;
    String price;
    String imageUrl;
    String type;
    boolean isAvailable;
    //User user;
    public Dress()
    {
        id = (int)Math.floor(Math.random()*(100-50+1)+50);
        type = "";
        price = "";
        imageUrl = "";
    }
    public Dress(String type,String price,String imageUrl)
    {
        this.id = (int)Math.floor(Math.random()*(100-50+1)+50);
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
