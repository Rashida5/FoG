package com.example.fogonstreet.google;

import com.example.fogonstreet.google.model.PlacesResults;
import com.example.fogonstreet.google.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleInterface {
 @GET("place/nearbysearch/json")
    Call<PlacesResults> getNearBy(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("keyword") String keyword,
            @Query("key") String key
    );
}
