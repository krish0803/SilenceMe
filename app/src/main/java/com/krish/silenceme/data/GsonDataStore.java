package com.krish.silenceme.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krish.silenceme.R;
import com.krish.silenceme.beans.MyLocation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonDataStore {

    private String SAVED_LOCATIONS_KEY = "MY_LOCATIONS";
    SharedPreferences pref = null;
    SharedPreferences.Editor editor = null;

    public GsonDataStore(Context context){
        this.pref = context.getSharedPreferences(R.string.shared_pref_name+"",Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    private void saveAll(Map locations){
        Type baseType = new TypeToken<Map<String,MyLocation>>(){}.getType();
        Gson gson = new Gson();
        String json = gson.toJson(locations, baseType);
        editor.putString(SAVED_LOCATIONS_KEY, json);
        editor.commit();
    }

    private Map<String,MyLocation> getAll(){
        Type baseType = new TypeToken<Map<String,MyLocation>>() {}.getType();
        Gson gson = new Gson();
        String json = pref.getString(SAVED_LOCATIONS_KEY,"");
        Map<String,MyLocation> locationMap = gson.fromJson(json, baseType);
        return locationMap;
    }

    public void saveLocation(MyLocation location){
        Map<String,MyLocation> allLocationsMap = getAll();

        if(allLocationsMap == null){
            allLocationsMap = new HashMap();
        }

        MyLocation existingLocation = (MyLocation) allLocationsMap.get(location.getLocationName());

        if(existingLocation == null){ // new location
            allLocationsMap.put(location.getLocationName(), location);
        } else { // Update existing location
            allLocationsMap.remove(location.getLocationName());
            allLocationsMap.put(location.getLocationName(),location);
        }

        saveAll(allLocationsMap);
    }

    public void removeLocation(MyLocation location){
        Map<String,MyLocation> allLocationsMap = getAll();

        MyLocation myLocation = (MyLocation) allLocationsMap.get(location.getLocationName());

        if(myLocation != null){ // new location
            allLocationsMap.remove(location.getLocationName());
        }

        saveAll(allLocationsMap);
    }

    public MyLocation getExistingLocation(String locName){
        MyLocation location = null;
        Map<String,MyLocation> locationsMap = getAll();

        if(locationsMap != null){
            location = (MyLocation) locationsMap.get(locName);
        }

        return location;
    }

    public List<MyLocation> getLocations(){
        List<MyLocation> locList = new ArrayList<>();
        Map<String,MyLocation> allLocationsMap = getAll();

        if(allLocationsMap!=null) {
            locList = new ArrayList<>(allLocationsMap.values());
        }

        return locList;
    }

}
