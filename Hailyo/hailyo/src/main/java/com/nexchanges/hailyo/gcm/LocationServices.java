package com.nexchanges.hailyo.gcm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.nexchanges.hailyo.MyApplication;
import com.nexchanges.hailyo.apiSupport.SendLocationUpdate;
import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by TP on 06/08/15.
 */
public class LocationServices extends Service {
    LocationManager lm;
    Thread triggerService;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        addLocationListener();
    }

    private void addLocationListener()
    {
        triggerService = new Thread(new Runnable(){
            public void run(){
                try{
                    Looper.prepare();//Initialise the current thread as a looper.
                    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_COARSE);

                    final String PROVIDER = lm.getBestProvider(c, true);

                    MyLocationListener myLocationListener = new MyLocationListener();
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000, 150, myLocationListener);

                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,180000,150
                            ,myLocationListener);


                    Log.d("LOC_SERVICE", "Service RUNNING!");
                    Looper.loop();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, "LocationThread");
        triggerService.start();
    }


    class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            updateLocation(location);
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
    }

    public void updateLocation(Location location)
    {
        Context appCtx = getApplicationContext();

        double latitude, longitude;

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String Str_lat = String.valueOf(latitude);
        String Str_lon = String.valueOf(longitude);
        SharedPrefs.save(appCtx, SharedPrefs.MY_CUR_LAT, Str_lat);
        SharedPrefs.save(appCtx, SharedPrefs.MY_CUR_LNG, Str_lon);
        String user_id = SharedPrefs.getString(appCtx,SharedPrefs.MY_USER_ID);
        String user_role = SharedPrefs.getString(appCtx,SharedPrefs.MY_ROLE_KEY);

        SendLocationUpdate sendLocationUpdate = new SendLocationUpdate();
        sendLocationUpdate.sendPostRequest(user_id, Str_lat, Str_lon, user_role);

    }

}

