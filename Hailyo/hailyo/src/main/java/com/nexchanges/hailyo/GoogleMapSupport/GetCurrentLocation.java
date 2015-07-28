package com.nexchanges.hailyo.GoogleMapSupport;

/**
 * Created by Abhishek on 08/05/15.
 */
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GetCurrentLocation  implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    Context context;

    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long ONE_MIN = 1000 * 60;
    private static final float MINIMUM_ACCURACY = 60.0f;

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    private Location location;

    public CurrentLocationCallback currentLocationCallback;


    public interface CurrentLocationCallback {
        void onComplete(Location location);
    }

    public GetCurrentLocation(Context context, CurrentLocationCallback callback){

        currentLocationCallback = callback;

        this.context = context;

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        location = fusedLocationProviderApi.getLastLocation(googleApiClient);
        Log.d("currentLocation", "\n ======> init.Location : "+location);

        if ( location != null ) {
            currentLocationCallback.onComplete(location);
        }else{
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("currentLocation", "\n ======> onLocationChange : "+location);
        if ( location != null ) {
            currentLocationCallback.onComplete(location);
        }
        fusedLocationProviderApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
