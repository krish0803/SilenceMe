package com.krish.silenceme.activity;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.krish.silenceme.R;
import com.krish.silenceme.beans.MyLocation;
import com.krish.silenceme.common.Constants;
import com.krish.silenceme.service.LocationService;

public class LocationActivity extends AppCompatActivity {

    String TAG = "LocationActivity";
    Place selectedPlace = null;
    LocationService locationService = null;
    MyLocation myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        String locObjStr = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            locObjStr = extras.getString("locObject");
        }

        myLocation = new Gson().fromJson(locObjStr, MyLocation.class);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());

                selectedPlace = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteLoation();
            }
        });
    }

    /**
     * Save New location and turn it on by default
     */
    private void saveLocation(){
        MyLocation myLocation = new MyLocation();
        LatLng latlong= selectedPlace.getLatLng();

        myLocation.setLatitude(latlong.latitude);
        myLocation.setLongitude(latlong.longitude);

        TextView nameTxt = findViewById(R.id.locName);
        myLocation.setLocationName(nameTxt.getText().toString());

        Spinner soundMode = findViewById(R.id.soundMode);
        myLocation.setSoundMode(soundMode.getSelectedItem().toString());

        myLocation.setActive(true); // Turn it on by default

        getLocationService().saveLocation(myLocation);

        setResult(Constants.NEW_LOC_ACT_REQUEST_OK);
        finish(); // Return to previous activity
    }

    private void deleteLoation(){
        if(myLocation!=null)
            getLocationService().deleteLocation(myLocation);

        setResult(Constants.DELETE_LOC_ACT_REQUEST_OK);
        finish(); // Return to previous activity
    }

    private LocationService getLocationService(){

        if(locationService == null){
            //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(R.string.app_name+"Pref", 0);
            locationService = new LocationService(getApplicationContext());
        }

        return locationService;
    }

    /**
     * Edit saved location - set location values
     * @param location
     */
    private void setDataToView(MyLocation location){



    }
}
