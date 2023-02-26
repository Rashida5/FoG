package com.example.fogonstreet.google.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlacesResults implements Serializable {
    @SerializedName("html_attributions")
    private List<Object> htmlAtrributions = new ArrayList<>();

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("results")
    private List<Result> results = new ArrayList<>();

    @SerializedName("status")
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAtrributions;
    }

    public List<Result> getResults() {
        return results;
    }
}
