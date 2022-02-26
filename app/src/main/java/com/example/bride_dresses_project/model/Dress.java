package com.example.bride_dresses_project.model;

public class Dress
{
    String id;
    String price;
    String imageUrl;
    String type;
    Long updateDate = new Long(0);
    boolean isAvailable;
    boolean deleted = false;
    // Designer designer

    public Dress()
    {
        type = "";
        price = "";
        imageUrl = "";


    }
    public String getName() {
        return type;
    }
    public void setName(String name) {
        this.type = name;
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
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public Long getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
    //---------------------------------//

}
