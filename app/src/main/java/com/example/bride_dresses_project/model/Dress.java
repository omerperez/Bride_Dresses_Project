package com.example.bride_dresses_project.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Entity
public class Dress
{
    @PrimaryKey
    @NonNull
    private String id;
    private String price;
    private String imageUrl;
    private String type;
    private Long updateDate;
    private String ownerId;
    private boolean deleted;

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
        this.updateDate=System.currentTimeMillis();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("price", price);
        result.put("type", type);
        result.put("imageUrl", imageUrl);
        result.put("deleted", deleted);
        result.put("ownerId", ownerId);
        result.put("updateDate",updateDate);
        return result;
    }
    //---------------------------------//

}
