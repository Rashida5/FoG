package com.example.fogonstreet.google.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpeningHours implements Serializable {
    @SerializedName("open_now")
    private Boolean openNow;

    @SerializedName("weekday_text")
    private List<Object> weekdayText = new ArrayList<Object>();
public Boolean getOpenNow(){
    return openNow;
}

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<Object> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }
}
