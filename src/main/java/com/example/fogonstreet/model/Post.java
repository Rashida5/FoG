package com.example.fogonstreet.model;

public class Post {
    private String email;
    private pointSchema location;

    public Post(String email, pointSchema location) {
        this.email = email;
        this.location = location;
    }
    private dist calculated;
    public String getEmail() {
        return email;
    }

    public dist getCalculated() {
        return calculated;
    }

    public void setCalculated(dist calculated) {
        this.calculated = calculated;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public pointSchema getLocation() {
        return location;
    }

    public void setLocation(pointSchema location) {
        this.location = location;
    }
}
