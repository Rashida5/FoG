package com.example.fogonstreet.google.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
@SerializedName("height")
    private Integer height;
@SerializedName("html_attributions")
    private List<String> htmlAttributions= new ArrayList<String>();
@SerializedName("photo_reference")
    private String photoRefernce;
@SerializedName("width")
    private Integer width;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoRefernce() {
        return photoRefernce;
    }

    public void setPhotoRefernce(String photoRefernce) {
        this.photoRefernce = photoRefernce;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
