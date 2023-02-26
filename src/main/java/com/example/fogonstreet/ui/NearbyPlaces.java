package com.example.fogonstreet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fogonstreet.R;
import com.example.fogonstreet.adapters.PlacesListAdapter;
import com.example.fogonstreet.google.GoogleClient;
import com.example.fogonstreet.google.GoogleInterface;
import com.example.fogonstreet.google.model.PlacesResults;
import com.example.fogonstreet.google.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyPlaces extends AppCompatActivity {
 private EditText editTextKeyword;
 private Button buttonSearch;
 private ListView listViewPlaces;
 private Spinner spinnerType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        editTextKeyword = findViewById(R.id.editTextKeyword);
        buttonSearch= findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
buttonSearch_onClick(view);
            }
        });
        listViewPlaces= findViewById(R.id.listViewPlaces);
        spinnerType = findViewById(R.id.spinnerType);
        loadData();
    }
    private void buttonSearch_onClick(View view){
       String keyword = editTextKeyword.getText().toString();
       String key = getText(R.string.nearby_places).toString();
       String location ="-33.8670522%2C151.1957362";
       int radius=1500;
       String type = spinnerType.getSelectedItem().toString();
        GoogleInterface googleInterface= GoogleClient.getClient().create(GoogleInterface.class);
        googleInterface.getNearBy(location,radius,type,keyword,key).enqueue(new Callback<PlacesResults>() {
            @Override
            public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                if(response.isSuccessful()){
                    Log.d("ResponseList", String.valueOf(response.code()));
                 List<Result> results = response.body().getResults();
                 Log.d("ListOfResults",response.body().toString());
                 Log.d("ListOfResults", String.valueOf(results.size()));
                    PlacesListAdapter placesListAdapter = new PlacesListAdapter(getApplicationContext(),results);
                    listViewPlaces.setAdapter(placesListAdapter);
                }
                else{
                    Toast.makeText(NearbyPlaces.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PlacesResults> call, Throwable t) {
                Toast.makeText(NearbyPlaces.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loadData(){
        List<String> types = new ArrayList<>();
        types.add("ATM");
        types.add("Cafe");
        types.add("Hotel");
        types.add("Restaurant");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,types);
        spinnerType.setAdapter(arrayAdapter);
    }

}