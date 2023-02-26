package com.example.fogonstreet.data;
import com.example.fogonstreet.model.token.getToken;
import com.example.fogonstreet.model.login;
import com.example.fogonstreet.model.update.Update;
import com.example.fogonstreet.model.update.UpdateResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("login")
    public Call<getToken> login(@Body login lg);
    @PUT("Users")
    Observable<List<UpdateResponse>> PutLocation(@Header("Authorization") String token, @Body Update update);
    @PUT("Users")
    public Call<List<UpdateResponse>> CallLocation( @Header("Authorization") String token , @Body Update update);
    /* @PATCH("Cars/{email}")
    public Call<Post> patchPost(@Path("email") String email, @Body Post post);*/
}

