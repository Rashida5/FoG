package com.example.fogonstreet.model.update;

import com.example.fogonstreet.model.Dist;
import com.example.fogonstreet.model.pointSchema;

public class Update {
    private pointSchema location;
     boolean emergencyState;

    public Update(pointSchema location, boolean emergencyState) {
        this.location = location;
        this.emergencyState = emergencyState;
    }

    public pointSchema getLocation() {
        return location;
    }

    public void setLocation(pointSchema location) {
        this.location = location;
    }

    public boolean isEmergencyState() {
        return emergencyState;
    }

    public void setEmergencyState(boolean emergencyState) {
        this.emergencyState = emergencyState;
    }
    //  private Dist calculated;


}
