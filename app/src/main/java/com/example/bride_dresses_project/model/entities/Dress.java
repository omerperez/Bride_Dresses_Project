package com.example.bride_dresses_project.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Entity
public class Dress implements Parcelable {
    public static final String DRESS_COLLECTION_NAME = "Dresses";
    @PrimaryKey
    @NonNull
    private String id;
    private String price;
    private String imageUrl;
    private String type;
    private String ownerId;
    private Long updateDate;
    private boolean deleted;

    public Dress() {
        this.id = UUID.randomUUID().toString();
        type = "";
        price = "";
        imageUrl = "";
        ownerId = "";
    }

    public Dress(String type, String price, String imageUrl, String ownerId) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.price = price + "$";
        this.imageUrl = imageUrl;
        this.updateDate = System.currentTimeMillis();
        this.ownerId = ownerId;
    }

    public Dress(String type, String price, String id, String ownerId, Boolean deleted) {
        this.type = type;
        this.price = price;
        this.id = id;
        this.deleted = deleted;
        this.ownerId = ownerId;
    }

    protected Dress(Parcel in) {
        id = in.readString();
        price = in.readString();
        imageUrl = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            updateDate = null;
        } else {
            updateDate = in.readLong();
        }
        ownerId = in.readString();
        deleted = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", id);
        json.put("type", type);
        json.put("price", price);
        json.put("deleted", deleted);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("imageUrl", imageUrl);
        json.put("ownerId", ownerId);
        return json;
    }

    public Map<String, Object> toEditJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("type", type);
        json.put("price", price);
        json.put("ownerId", price);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("imageUrl", imageUrl);
        return json;
    }

    public static Dress create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String type = (String) json.get("type");
        String price = (String) json.get("price");
        String ownerId = (String) json.get("ownerId");
        Boolean deleted = (Boolean) json.get("deleted");
        Long updateDate = 0L;
        try {
            Timestamp ts = (Timestamp) json.get("updateDate");
            updateDate = ts.getSeconds();
        }catch (Exception exception){

        }
        String imageUrl = (String) json.get("imageUrl");
        Dress dress = new Dress(type, price, id, ownerId, deleted);
        dress.setUpdateDate(updateDate);
        dress.setImageUrl(imageUrl);
        return dress;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("price", price);
        result.put("imageUrl", imageUrl);
        result.put("deleted", deleted);
        result.put("ownerId", ownerId);
        result.put("updateDate", updateDate);
        return result;
    }


    public static final Creator<Dress> CREATOR = new Creator<Dress>() {
        @Override
        public Dress createFromParcel(Parcel in) {
            return new Dress(in);
        }

        @Override
        public Dress[] newArray(int size) {
            return new Dress[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(price);
        parcel.writeString(imageUrl);
        parcel.writeString(type);
        if (updateDate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(updateDate);
        }
        parcel.writeString(ownerId);
        parcel.writeByte((byte) (deleted ? 1 : 0));
    }

}
