package com.example.fogonstreet.data;
import com.example.fogonstreet.model.Post;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostClient {
    private static final String BASE_URL = "https://gradproject.onrender.com/";
    private ApiInterface apiinterface;
    private static PostClient INSTANCE;

    public PostClient() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiinterface=retrofit.create(ApiInterface.class);
    }
    public static PostClient getINSTANCE() {
        if (null == INSTANCE){
            INSTANCE = new PostClient();
        }
        return INSTANCE;
    }
    public Call<List<Post>> getPosts(){
        return apiinterface.getPost();
    }

    public Observable<List<Post>> getNearby(String s){
        return apiinterface.Getnearby(s);
    }
    /*public Observable<Post> patchPost(String email,Post post){
        return apiinterface.patchPost(email,post);
    }*/
    public Observable<List<Post>> patchPost(String email,Post post){
        return apiinterface.patchPost(email,post);
    }
    public Observable<Post> deletePost(String email){
        return apiinterface.deletePost(email);
    }
}
