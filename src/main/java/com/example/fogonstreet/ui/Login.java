package com.example.fogonstreet.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fogonstreet.R;
import com.example.fogonstreet.data.ApiInterface;
import com.example.fogonstreet.model.Post;
import com.example.fogonstreet.model.pointSchema;
import com.example.fogonstreet.viewmodel.PostViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    Button btn;
    EditText api;
    protected double latitude=-1,longitude=-1;
    Post post;
    pointSchema pointschema;
    PostViewModel postViewModel;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn=findViewById(R.id.btn);
        api=findViewById(R.id.api);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
      //  ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://gradproject.onrender.com/Cars/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                Log.d("msg1", String.valueOf(post.getLocation().getCoordinates()[0]));
                if (api.getText().toString().isEmpty() == false) {
                    s=api.getText().toString();
                    pointschema=new pointSchema();
                    pointschema.setType("Point");
                    double [] arr= new double[2];
                 //   if(longitude==-1&&latitude==-1){
                  //      Toast.makeText(Login.this, "We Try to GET Your Location Try Again", Toast.LENGTH_LONG).show();
                   // }
              //      else {
                        arr[0] = longitude;
                        arr[1] = latitude;
                        pointschema.setCoordinates(arr);
                        post = new Post(api.getText().toString(), pointschema);

                        Call<Post> call = apiInterface.storePost(post);
                        call.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                //       Log.d("Class",post.getEmail());
                                if (response.code() == 400) {
                                    Toast.makeText(Login.this, "Enter anther Email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Post Successfully", Toast.LENGTH_LONG).show();
                                    postViewModel.setEmail(post.getEmail());
                                    String str = post.getEmail();
                                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                    i.putExtra("message_key", str);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                //  Log.d("responce","Failed");
                                Toast.makeText(Login.this, "Try Again", Toast.LENGTH_LONG).show();
                            }
                        });
                    //}
                }
                else{
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}