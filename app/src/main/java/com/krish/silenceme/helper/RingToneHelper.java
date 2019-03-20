package com.krish.silenceme.helper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;

import com.krish.silenceme.activity.MainActivity;
import com.krish.silenceme.beans.MyLocation;
import com.krish.silenceme.common.Constants;
import com.krish.silenceme.service.LocationService;
import com.krish.silenceme.util.CommonUtil;

import java.util.List;

public class RingToneHelper {

    private Context context;
    CommonUtil commonUtil = new CommonUtil(context);

    public RingToneHelper(Context context){
        this.context = context;
        checkDoNotDisturbPermission();
    }

    public void updateRingToneStatus(Location curLocation){
        //LocationService locationService = ((MainActivity)context).getLocationService();

        LocationService locationService = new LocationService(context);

        List<MyLocation> savedLocations = locationService.getSavedLocations();

        Location prevLocation = new Location("");

        for(MyLocation savedLoc : savedLocations){

            prevLocation.setLatitude(savedLoc.getLatitude());
            prevLocation.setLongitude(savedLoc.getLongitude());

            float distInMeters = prevLocation.distanceTo(curLocation);
            double distInMiles = distInMeters/1609.344;

            if(distInMiles < 0.5 && savedLoc.isActive()){
                if(savedLoc.getSoundMode().equals(Constants.SOUND_MODE_VIBRATE)){
                    setToVibrateMode();
                    //commonUtil.showMessage("Setting to Vibrate Mode");
                } else if(savedLoc.getSoundMode().equals(Constants.SOUND_MODE_RING) && !isRingMode()){
                    setToRingMode();
                    //commonUtil.showMessage("Setting to Ring Mode");
                } else if(savedLoc.getSoundMode().equals(Constants.SOUND_MODE_SILENT)){
                    setToSilentMode();
                    //commonUtil.showMessage("Setting to Silent Mode");
                }

            } else if (distInMiles > 0.5 && isVibrateOrSilentMode()){
                setToRingMode();
                //commonUtil.showMessage("Setting to Ring Mode");
            }
        }
    }

    private boolean isRingMode(){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL)
            return true;
        return false;
    }

    private boolean isVibrateOrSilentMode(){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE || audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)
            return true;
        return false;
    }

    private void setToVibrateMode(){
        NotificationManager n = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(n.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE)
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }else{
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            context.startActivity(intent);
        }
    }

    private void setToRingMode(){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(am.getRingerMode()!= AudioManager.RINGER_MODE_NORMAL)
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    private void setToSilentMode(){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(am.getRingerMode()!= AudioManager.RINGER_MODE_SILENT)
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    private void checkDoNotDisturbPermission() {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            context.startActivity(intent);
        }
    }

}
