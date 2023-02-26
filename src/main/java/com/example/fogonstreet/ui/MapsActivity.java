package com.example.fogonstreet.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fogonstreet.R;
import com.example.fogonstreet.model.pointSchema;
import com.example.fogonstreet.model.update.Update;
import com.example.fogonstreet.model.update.UpdateResponse;
import com.example.fogonstreet.viewmodel.PostViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.fogonstreet.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 200;
    private static final String TAG = "MapsActivity";
    LocationRequest locationRequest;
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;
    Switch sw1;
    PostViewModel postViewModel;
   // public Post post1;
    public pointSchema pointschema1;
    public double[] arr1;
    List<UpdateResponse>Data;
    List<UpdateResponse>EventResponse;
    NotificationManagerCompat notificationManagerCompat;
    Marker [] mark;
    //TextView Long,Lat;
    String La,Lo,str,ID;
    Update upd1;
    boolean userEmergency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sw1=findViewById(R.id.switch1);
        notificationManagerCompat=NotificationManagerCompat.from(this);
        EventResponse= new ArrayList<>();
  Data = new ArrayList<>();
        Intent intent = getIntent();
        //String str="MapsActivity";
          str = intent.getStringExtra("message_key");
          ID=intent.getStringExtra("ID_key");
          Log.d("GetToken",str);
       // Toast.makeText(MapsActivity.this, str, Toast.LENGTH_LONG).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //---------------------------------Pusher-------------------------------------------------------------------//
        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        //  Pusher pusher = new Pusher("7b7b8d3e290e0b06fb2d", options);
        Pusher pusher= new Pusher("8f885166fac44cd34323",options);
        pusher.connect(new ConnectionEventListener() {
                           @Override
                           public void onConnectionStateChange(ConnectionStateChange change) {
                               Log.i("Pusher", "State changed from " + change.getPreviousState() +
                                       " to " + change.getCurrentState());
                           }

                           @Override
                           public void onError(String message, String code, Exception e) {
                               Log.i("Pusher", "There was a problem connecting! " +
                                       "\ncode: " + code +
                                       "\nmessage: " + message +
                                       "\nException: " + e
                               );
                           }
                       }, ConnectionState.ALL
        );
        Channel channel = pusher.subscribe("User");
        channel.bind("updated", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {

                if (event != null) {
                    //      Toast.makeText(MapsActivity.this, event.toString(), Toast.LENGTH_SHORT).show();
                       Log.i("Pusher", "Received event with data: " + event.getData());
                       Log.i("Pusher","Received event with class: "+event.getData().getClass().getSimpleName());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(SendNotify(event.toString())==true){
                                    Log.d("SendNotify","DataArrive");
                                    buildNotify();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                     //       Toast.makeText(MapsActivity.this, "Data Arrive From Pusher", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        //-------------------------------------create Update & setToken------------------------------------------------------//
        arr1=new double[2];
        arr1[0]=-1;
        arr1[1]=-1;
        pointschema1=new pointSchema();
        pointschema1.setCoordinates(arr1);
        pointschema1.setType("Point");
       upd1 = new Update(pointschema1,false);
        postViewModel.setToken(str);
        postViewModel.setValueUpdate(upd1);
        //--------------------------------------update View Model----------------------------------------------- //
       postViewModel.putUpdate();
      //  postViewModel.CallUpdate();
     /*   final Observer<Boolean> nameObserver1 = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean==true){
                    Toast.makeText(MapsActivity.this, "Car need help", Toast.LENGTH_SHORT).show();
                }
            }
        };
        postViewModel.getValueSend().observe(this,nameObserver1);*/
       final Observer<List<UpdateResponse>> nameObserver = new Observer<List<UpdateResponse>>() {
            @Override
            public void onChanged(@Nullable final List<UpdateResponse> newName) {
                // Update the UI, in this case, a TextView.//locations add as markers
                Log.d("ListSize", String.valueOf(newName.size()));
                if (mMap != null) {
                    if(Data!=null) {
                        for (int i = 1; i < Data.size(); i++) {
                            if (mark[i] != null) {
                                Log.d("MarkerRemoved", "Success");
                                mark[i].remove();
                            }
                        }
                    }
                   mark = new Marker[newName.size()];
                   for(int i=1;i<newName.size();i++){
                       Log.d("MarkerAdd", String.valueOf(newName.get(i).getLocation().getCoordinates()[0]));
                       mark[i] = mMap.addMarker(new MarkerOptions().position(new LatLng(newName.get(i).getLocation().getCoordinates()[1], newName.get(i).getLocation().getCoordinates()[0])).title(newName.get(i).getEmail()));
                   }
                     //  Log.d("ListSize2","Success");
                }
                Data = newName;
            }
        };
        postViewModel.getPutMutableLiveData().observe(this,nameObserver);
      //  userEmergency=sw1.isChecked();
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              //  Log.d("sw1Checked", String.valueOf(isChecked));
                if (upd1 != null) {
                    upd1.setEmergencyState(isChecked);
                    Log.d("sw1Checked", String.valueOf(isChecked));
                    postViewModel.setValueUpdate(upd1);
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            enableUserLocation();
//            zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else  {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
            try {
                List<Address> addresses = geocoder.getFromLocationName("Egypt", 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    LatLng Egypt = new LatLng(address.getLatitude(), address.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(Egypt)
                            .title(address.getLocality());
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Egypt, 16));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
       /* mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    //locationCallback callback for receiving notifications from the FusedLocationProviderClient.
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());

            if (mMap != null &&postViewModel!=null) {
                setUserLocationMarker(locationResult.getLastLocation());
                arr1[0]=locationResult.getLastLocation().getLongitude();
                arr1[1]=locationResult.getLastLocation().getLatitude();
                pointschema1.setCoordinates(arr1);
                if(sw1!=null){
                    userEmergency=sw1.isChecked();
                }
                else{
                    userEmergency=false;
                }
                upd1 = new Update(pointschema1,userEmergency);


                postViewModel.setValueUpdate(upd1);
            }
        }
    };
    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.rotation(location.getBearing());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        } else  {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }
    //startLocation function pass to fusedLocationProvider locationRequest and locationCallback
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
//stopLocation function pass to fusedLocationProvider locationCallback
    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //onStart startLocation update by call startLocation update function
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // you need to request permissions...
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //onStop call stopLocation update
        stopLocationUpdates();
    }
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //We can show a dialog that permission is not granted...
            }
        }
    }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: ");
        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }

    Boolean SendNotify(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        String Arr= json.getString("data");
        JSONArray jsonArray = new JSONArray(Arr);
        for (int i = 0; i < jsonArray.length(); i++) {
            // store each object in JSONObject
            JSONObject getID = jsonArray.getJSONObject(i);
            String getIDStr=getID.getString("_id");
            String emergency=getID.getString("emergencyState");
           // boolean emergency=getID.getBoolean("emergencyState");
            if(getIDStr!=null&&getIDStr==ID&&emergency=="false"){
             return true;
            }
            Log.d("IDFound",getIDStr);

        }

        return false;
    }
    void buildNotify(){
        Log.d("Notification_build","True");
        String Channel_1_id="Channel";
        NotificationChannel channel;
        Log.d("channel_ID","found");
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            channel=new NotificationChannel(Channel_1_id,"Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);}
        Notification notification=  new NotificationCompat.Builder(this, Channel_1_id)
                .setSmallIcon(R.drawable.redcar).setContentTitle("Notification")
                .setContentText("Car Need Help").setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1,notification);
    }

}