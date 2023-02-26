package com.example.fogonstreet.data;
import android.util.Log;

import com.example.fogonstreet.model.update.Update;
import com.example.fogonstreet.model.update.UpdateResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    public Observable<List<UpdateResponse>> putUpdate(String Token, Update update){

        return apiinterface.PutLocation("bearer "+Token,update);
    }

  public Call<List<UpdateResponse>> CallUpdate( String Token,Update update){

      return apiinterface.CallLocation( "bearer "+Token,update);
  }
}
