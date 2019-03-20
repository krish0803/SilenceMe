package com.krish.silenceme.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.krish.silenceme.activity.MainActivity;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class GPSHelper implements LocationListener {

    private LocationManager locationManager;
    private String provider;
    private Context context;
    private static GPSHelper gpsHelper;

    private static final String[] INITIAL_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int ACCESS_FINE_LOCATION_CODE = 1337;
    private boolean canRequestLocationUpdates = true;

    private GPSHelper() {

    }
    private GPSHelper(Context context) {
        this.context = context;
        init();
    }

    public static GPSHelper getInstance(Context context){
       if(gpsHelper == null){
           gpsHelper = new GPSHelper(context);
       }

       return gpsHelper;
    }

    private void init(){

        /*if (!(context instanceof MainActivity)) {
            canRequestLocationUpdates = false;
            return;
        }

        canRequestLocationUpdates = true;




        if(!canAccessLocaiton() && context instanceof MainActivity){ // Make sure to turn the GPS on from UI, not from Background Service
            requestPermissions((MainActivity)context,INITIAL_PERMS,1337);
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            askEnableGPS();
        }*/
        Criteria criteria = new Criteria();
        locationManager = (LocationManager) context.getSystemService(
                context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);

        // Get the location from the given provider
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Request location updates every minute
        /*locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1 * 60 * 1000, 0, this); //10 minutes*/

        //requestLocationUpdate(locationManager);
    }

    public LocationManager getLocationManager(){
        return locationManager;
    }

    public void requestLocationUpdate(LocationManager locationManager) throws SecurityException {
       if(canRequestLocationUpdates == true){
           locationManager.requestLocationUpdates(
                   LocationManager.GPS_PROVIDER,1000*60*5, 0, this);
       }
    }

    @Override
    public void onLocationChanged(Location curLocation) {
        new RingToneHelper(context).updateRingToneStatus(curLocation);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE:
                //locationStatusCheck();
            default:
                // do nothing
        }

    }

    private boolean canAccessLocaiton() {
        boolean ret = false;
        ret = (PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        return ret;
    }

    /**
     * check if GPS enabled or not, if not ask user to enable it
     */
    private void askEnableGPS() {
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(gpsOptionsIntent);
    }
}
