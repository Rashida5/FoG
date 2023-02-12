package com.example.fogonstreet.data;
import com.example.fogonstreet.model.Post;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("add")
    public Call<Post> storePost(@Body Post post);

    @GET("Cars/all")
    public Call<List<Post>> getPost();

   /* @PATCH("Cars/{email}")
    public Call<Post> patchPost(@Path("email") String email, @Body Post post);*/

    /*  @PATCH("Cars/{email}")
      public Observable<Post> patchPost(@Path("email") String email, @Body Post post);*/
    @PATCH("Cars/{email}")
    public Observable<List<Post>> patchPost(@Path("email") String email, @Body Post post);

  /*  @DELETE("Cars/{email}")
    public Call<Post> deletePost(@Path("email") String email);*/

    @DELETE("Cars/{email}")
    public Observable<Post> deletePost(@Path("email") String email);

    @GET("findNearestCar/{email}")
    public Observable<List<Post>> Getnearby(@Path("email") String email);


}

