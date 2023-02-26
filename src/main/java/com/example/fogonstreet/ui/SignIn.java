package com.example.fogonstreet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fogonstreet.R;
import com.example.fogonstreet.data.ApiInterface;
import com.example.fogonstreet.model.token.Data;
import com.example.fogonstreet.model.token.getToken;
import com.example.fogonstreet.model.login;

import com.example.fogonstreet.viewmodel.PostViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {
    Button btn;
    EditText api;
    EditText pass;
    protected double latitude = -1, longitude = -1;
    PostViewModel postViewModel;
    String s, password;
    login lg;
    String Token,ID;
    NotificationManagerCompat notificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        notificationManagerCompat=NotificationManagerCompat.from(this);

        btn = findViewById(R.id.btn);
        api = findViewById(R.id.api);
        pass = findViewById(R.id.password);
        Data d;
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gradproject.onrender.com/Users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("msg1", String.valueOf(post.getLocation().getCoordinates()[0]));
                if (api.getText().toString().isEmpty() == false && pass.getText().toString().isEmpty() == false) {
                    s = api.getText().toString();
                    password = pass.getText().toString();
               //     check();
                   lg=new login(s,password);
                 // Log.d("IDFound",getId("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYzZjY3ZTVlZGQzOGFhNTFkNzE3ZTNjYiIsImlhdCI6MTY3NzQxMzY2NSwiZXhwIjoxNjc3NTAwMDY1fQ.QXE-yQS-nJnGG2qJtR0-xuuLG_KDYRvpVH_oO8I7Xps"));
                    Call<getToken> call = apiInterface.login(lg);
                    call.enqueue(new Callback<getToken>() {
                        @Override
                        public void onResponse(Call<getToken> call, Response<getToken> response) {
                            //       Log.d("Class",post.getEmail());
                            if (response.code() == 400) {
                                Toast.makeText(SignIn.this, "Enter anther Email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignIn.this, "Post Successfully", Toast.LENGTH_LONG).show();
                                if(response.body()!=null){
                                    Token=response.body().getData().getToken();
                                }
                               Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                if(Token!=null) {
                                    ID = getId(Token);
                                    if (ID != "notFound") {
                                        i.putExtra("message_key", Token);
                                        i.putExtra("ID_key",ID);
                                        Log.d("IDFound",ID);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(SignIn.this, "There is Problem with System Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(SignIn.this, "There is Problem with System Try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<getToken> call, Throwable t) {
                            //  Log.d("responce","Failed");
                            Toast.makeText(SignIn.this, "Try Again", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(SignIn.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    String getId(String token){
        String ID="notFound";
        String payload = "";
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decoder = Base64.getUrlDecoder();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             payload = new String(decoder.decode(chunks[1]));
            try {
                JSONObject json = new JSONObject(payload);
               ID=handleJSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

 return ID;
    }
    String handleJSONObject(JSONObject jsonObject) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(jsonObject.has("id")){
                try {
                    String id= jsonObject.getString("id");
                 return id;
                } catch (JSONException e) {
                    return "notFound";
                //    e.printStackTrace();
                }
            }
        }
        else{
            return "notFound";
        }
        return "notFound";
    }


}