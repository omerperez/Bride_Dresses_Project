package com.example.bride_dresses_project.model.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Entity
public class Dress
{
    public static final String DRESS_COLLECTION_NAME ="Dresses" ;
    @PrimaryKey
   @NonNull
    private String id;
    private String price;
    private  String imageUrl;
    private  String type;
    private Long updateDate;
    private String ownerId;
    private  boolean deleted;

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

    public Dress(String type,String price, String id, Boolean deleted) {
        this.type = type;
        this.price=price;
        this.id = id;
        this.deleted = deleted;
    }

//  @NonNull
    public  String getId() {
        return id;
    }

    public void setId( String id) {
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

    public  Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("type",type);
        json.put("price",price);
        json.put("deleted",deleted);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("imageUrl",imageUrl);
        return json;
    }

    public static Dress create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String type = (String) json.get("type");
        String price = (String) json.get("price");
        Boolean deleted = (Boolean) json.get("deleted");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String imageUrl = (String)json.get("imageUrl");
        Dress dress = new Dress(type,price,id,deleted);
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
        result.put("updateDate",updateDate);
        return result;
    }
    //---------------------------------//

}
