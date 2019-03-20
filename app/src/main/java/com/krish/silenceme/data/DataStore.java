package com.krish.silenceme.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.krish.silenceme.R;
import com.krish.silenceme.beans.MyLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataStore {

    //private Context context = null;
    private String SAVED_LOCATIONS_KEY = "MY_SAVED_LOCATIONS";
    SharedPreferences pref = null;
    Editor editor = null;

    /*public DataStore(Context context){
        this.context = context;
        pref = context.getSharedPreferences(R.string.app_name+"Pref", 0);
        editor = pref.edit();
        //removeAllPreferences();
    }*/

    public DataStore(Context context){
        this.pref = context.getSharedPreferences(R.string.shared_pref_name+"", 0);;
        editor = pref.edit();
        //removeAllPreferences();
    }

    public void removeAllPreferences() {
        editor.clear().commit();
    }

    public void saveLocation(MyLocation location) {
        editor = pref.edit();
        Set set = pref.getStringSet(SAVED_LOCATIONS_KEY, null);
        if(set==null){
            set = new HashSet <String>();
        }

        set.add(getJSONStr(location));

        editor.putStringSet(SAVED_LOCATIONS_KEY,set);
        editor.commit();
    }

    public void updateLocation(MyLocation newLocation) {
        Set set = pref.getStringSet(SAVED_LOCATIONS_KEY, null);
        if(set==null){
            return;
        }

        MyLocation existingLocation = getExistingLocation(newLocation.getLocationName());
        if (existingLocation != null) {
            set.remove(getJSONStr(existingLocation));
            set.add(getJSONStr(newLocation));
            editor.putStringSet(SAVED_LOCATIONS_KEY,set);
            editor.commit();
        }
    }

    public void removeLocation(MyLocation location){
        Set set = pref.getStringSet(SAVED_LOCATIONS_KEY,null);
        set.remove(getJSONStr(location));
        editor.putStringSet(SAVED_LOCATIONS_KEY,set);
        editor.commit();
    }

    public MyLocation getExistingLocation(String locName){
        Set<String> set = pref.getStringSet(SAVED_LOCATIONS_KEY,null);

        if(set!=null) {

            for(String jsonStr:set){
                MyLocation locObj= getLocatonObj(jsonStr);
                if(locObj.getLocationName().equals(locName))
                    return locObj;
            }

        }

        return null;
    }

    public ArrayList<MyLocation> getLocations(){
        ArrayList<MyLocation> list = null;
        Set<String> set = pref.getStringSet(SAVED_LOCATIONS_KEY, null);

        if(set!=null) {
            list = new ArrayList<>();

            for(String jsonStr:set){
                list.add(getLocatonObj(jsonStr));
            }

        } else {
            list = new ArrayList<MyLocation>();
        }
        return list;
    }

    private String getJSONStr(MyLocation location){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(location);

        return jsonStr;
    }

    private MyLocation getLocatonObj(String jsonStr){
        Gson gson = new Gson();
        MyLocation location = gson.fromJson(jsonStr, MyLocation.class);
        return location;
    }

}
