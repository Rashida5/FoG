package com.example.fogonstreet.model;

public class pointSchema {
    String type;
    private double [] coordinates;
    //private ArrayList<Double> coordinates;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
