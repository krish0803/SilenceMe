package com.krish.silenceme.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.krish.silenceme.R;
import com.krish.silenceme.adapters.LocationsAdapter;
import com.krish.silenceme.beans.MyLocation;
import com.krish.silenceme.common.Constants;
import com.krish.silenceme.helper.GPSHelper;
import com.krish.silenceme.service.AlarmService;
import com.krish.silenceme.service.LocationService;
import com.krish.silenceme.service.DisplayService;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    LocationService locationService = null;
    LocationsAdapter locationsAdapter;
    RecyclerView recList;

    private static final String[] INITIAL_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivityForResult(new Intent(MainActivity.this,LocationActivity.class), Constants.NEW_LOC_ACT_REQUEST_OK);
            }
        });

        List<MyLocation> locations = getSavedLocations();

        recList = (RecyclerView) findViewById(R.id.my_recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        locationsAdapter = new LocationsAdapter(this,locations);
        recList.setAdapter(locationsAdapter);

        checkGPS();

        GPSHelper.getInstance(this);

        // Start background process, which runs indefinitely
        //Intent intent = new Intent(this, RingerBackgroundService.class);
        //startService(intent);
        new AlarmService().setAlarm(getApplicationContext());

        isMyServiceRunning(DisplayService.class);

        if (Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {
            startService(new Intent(this,DisplayService.class));
        } else {
            //Launch notification access in the settings...
            Intent intent = new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getApplicationContext().startActivity(intent);
        }
    }

    private boolean canAccessLocaiton() {
        boolean ret = false;
        ret = (PackageManager.PERMISSION_GRANTED == getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        return ret;
    }

    private void checkGPS() {

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(
                Context.LOCATION_SERVICE);

        if(!canAccessLocaiton()){ // Make sure to turn the GPS on from UI, not from Background Service
            requestPermissions(INITIAL_PERMS,1337);
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            askEnableGPS();
        }

    }

    private void askEnableGPS() {
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        getApplicationContext().startActivity(gpsOptionsIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private List<MyLocation> getSavedLocations(){
        List<MyLocation> locations = getLocationService().getSavedLocations();

        return locations;
    }

    public LocationService getLocationService(){

        if(locationService == null){
            //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(R.string.app_name+"Pref", 0);
            locationService = new LocationService(getApplicationContext());
        }

        return locationService;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NEW_LOC_ACT_REQUEST_OK || requestCode == Constants.DELETE_LOC_ACT_REQUEST_OK) {
            //if (resultCode == RESULT_FIRST_USER) {
            //locationsAdapter.setLocations(getSavedLocations());
            locationsAdapter.notifyDataSetChanged();
            locationsAdapter = new LocationsAdapter(this, getSavedLocations());
            recList.setAdapter(locationsAdapter);
            recList.invalidate();
            //}
        }
    }
}
