package com.krish.silenceme.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.krish.silenceme.beans.MyLocation;
import com.krish.silenceme.data.GsonDataStore;

import java.util.List;

public class LocationService {

    private GsonDataStore dataStore = null;

    public LocationService(Context context) {
        this.dataStore = new GsonDataStore(context);
    }

    public List<MyLocation> getSavedLocations() {
        return dataStore.getLocations();
    }

    public void saveLocation(MyLocation location){
        dataStore.saveLocation(location);
    }

    public void deleteLocation(MyLocation location){
        dataStore.removeLocation(location);
    }

    public MyLocation getSavedLocation(String locName){
        return dataStore.getExistingLocation(locName);
    }

}
