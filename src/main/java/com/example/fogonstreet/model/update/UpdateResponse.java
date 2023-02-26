package com.example.fogonstreet.model.update;

import com.example.fogonstreet.model.Dist;
import com.example.fogonstreet.model.pointSchema;

public class UpdateResponse {
    String first_name;
    String last_name;
    String email;
  //  int __v;
    String emergencyState;
    pointSchema location;
    Dist dist;
public  UpdateResponse(){

}
    public UpdateResponse(String first_name, String last_name, String email,  String emergencyState, pointSchema location, Dist dist) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.emergencyState = emergencyState;
        this.location = location;
        this.dist = dist;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyState() {
        return emergencyState;
    }

    public void setEmergencyState(String emergencyState) {
        this.emergencyState = emergencyState;
    }

    public pointSchema getLocation() {
        return location;
    }

    public void setLocation(pointSchema location) {
        this.location = location;
    }

    public Dist getDist() {
        return dist;
    }

    public void setDist(Dist dist) {
        this.dist = dist;
    }
}
