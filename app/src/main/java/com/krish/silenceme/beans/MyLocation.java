package com.krish.silenceme.beans;

import java.util.Objects;

public class MyLocation {
    private String locationName;
    private double latitude;
    private double longitude;
    private String soundMode;
    //private boolean alerted;
    private boolean active;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /*public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }*/

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSoundMode(){
        return this.soundMode;
    }

    public void setSoundMode(String soundMode){
        this.soundMode = soundMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLocation myLocation = (MyLocation) o;
        return locationName == myLocation.locationName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationName);
    }

    @Override
    public String toString() {
        return "MyLocation{" +
                "locationName=" + locationName +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
               // ", alerted='" + alerted + '\'' +
                ", soundMode='" + soundMode + '\'' +
                '}';
    }
}
