package com.nexchanges.hailyo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by Abhishek on 18/04/15.
 */
public class GeoFetcher extends AsyncTask<LatLng, Void, List<Address>> {

    private boolean isExecuting = false;
    private Context context;
    private OnLocationAddressAvailableListener listener;

    public GeoFetcher(Context context) {
        this.context = context;
    }

    public void setListener(OnLocationAddressAvailableListener listener) {
        this.listener = listener;
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    @Override
    protected List<Address> doInBackground(LatLng... latLngs) {
        LatLng latLng = latLngs[0];
        isExecuting = true;
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            return geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        super.onPostExecute(addresses);
        isExecuting = false;
        if(listener!=null){
            listener.onLocationAddressAvailable(addresses);
        }
    }

    public static interface OnLocationAddressAvailableListener{
        public void onLocationAddressAvailable(List<Address>  addresses);
    }
}
