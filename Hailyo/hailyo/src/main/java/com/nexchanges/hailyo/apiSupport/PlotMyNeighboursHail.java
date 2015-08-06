package com.nexchanges.hailyo.apiSupport;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nexchanges.hailyo.MainActivity;
import com.nexchanges.hailyo.MainBrokerActivity;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.customSupportClass.MyMarker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AbhishekWork on 16/07/15.
 */public class PlotMyNeighboursHail {
    String URL = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000/1/hailyo/bboxbrokers";
    StringEntity se;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private static final String TAG = PlotMyNeighboursHail.class.getSimpleName();


    private HashMap<Marker, MyMarker> mMarkersHashMap;

    public void markerpos(String my_user_id, String lat, String lng, String brokerType, String user_role, GoogleMap map)

    {

        //Log.i(TAG, "Lat is " + lat + " Lng is" + lng);
        sendPostRequest(my_user_id,lat,lng,brokerType, user_role, map);

    }

    private void sendPostRequest(final String my_user_id, final String lat, final String lng, final String brokerType, final String user_role, final GoogleMap map)
    {

        //Log.i(TAG, "BBOX post called");


        class SendPostReqAsyncTask extends AsyncTask<String, Void,String> {

            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {

                    //Log.i(TAG, "Packaging & sending JSON");

                    System.out.print("We are in JSON Success");
                    jsonObject.accumulate("user_id", my_user_id);
                    jsonObject.accumulate("lat", lat);
                    jsonObject.accumulate("long", lng);
                    jsonObject.accumulate("search_for", brokerType);
                    jsonObject.accumulate("user_role", user_role);


                } catch (JSONException e) {
                    System.out.print("We are in JSON Exception");
                    e.printStackTrace();
                }

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(URL);


                try {
                    se = new StringEntity(jsonObject.toString());

                    System.out.print("Constructed String Entity is" + se);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                se.setContentType(new BasicHeader("Content-Type", "application/json"));
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");

                try {
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    int response = httpResponse.getStatusLine().getStatusCode();

                    if (response == 200 || response == 201)

                    {

                      //  Log.i(TAG,"Here is the HTTP response" + response);
                        InputStream inputStream = httpResponse.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }
                        String result = stringBuilder.toString();
                      //  Log.i(TAG,"Final String result of asynch task is " + result);

                        return result;


                        //mMyMarkersArray2 = createMarkerHash(result);

                    }

                    else
                    {

                        Log.i(TAG,"my marker array 2 is null");
                    }


                } catch (ClientProtocolException cpe) {
                    Log.i(TAG,"First Exception coz of HttpResponese :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    Log.i(TAG, "Second Exception coz of HttpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                createMarkerHash(result,user_role,brokerType, map);

            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(my_user_id, lat, lng, brokerType);
    }


    private void createMarkerHash(String result, String role, String type, GoogleMap map1)
    {

        try {
            //Log.i(TAG, "Got successful lat lng response from server in json file");
            mMyMarkersArray.clear();


            if(result!=null)
            {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jsonObject = jArray.getJSONObject(i);
                JSONArray jloc = jsonObject.getJSONArray("loc");
                String lng = jloc.getString(1);
                String lat = jloc.getString(0);

                //Log.i(TAG, "Lat exctracted,value is" + lat);
               // Log.i(TAG, "Lng exctracted,value is" + lng);
                mMarkersHashMap = new HashMap<Marker, MyMarker>();
                mMyMarkersArray.add(new MyMarker(Double.parseDouble(lat), Double.parseDouble(lng)));

                if (role.equalsIgnoreCase("broker"))
                    plotBMarkers(mMyMarkersArray, map1);
                else
                    plotCMarkers(mMyMarkersArray, type, map1);


            }
        }



         /*   JSONObject jObject = new JSONObject(result);
            JSONArray user__list = jObject.getJSONArray("users");

            for (int j = 0;j<user__list.length();j++)
            {

                Log.i(TAG,"Parsing latng object inside response");
                JSONObject attri = user__list.getJSONObject(j);
                JSONArray loc = attri.getJSONArray("loc");

//                String lng = loc.getString(0);
  //              String lat = loc.getString(1);

                mMarkersHashMap = new HashMap<Marker, MyMarker>();
                mMyMarkersArray3.add(new MyMarker(Double.parseDouble(lat), Double.parseDouble(lng)));


/*
                String Loc = loc.getString(0);
                String delims = "[,]";
                String[] tokens = Loc.split(delims);
                String Lat = tokens[0];
                String Lng = tokens[1];
                System.out.print("Lat is" + Lat);
                System.out.print("Long is" + Lng);
                mMarkersHashMap = new HashMap<Marker, MyMarker>();
                mMyMarkersArray3.add(new MyMarker(Double.parseDouble(Lat), Double.parseDouble(Lng)));




            }*/


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.i(TAG, "Printing data of MyArray 3");
    }


    private void plotCMarkers(ArrayList<MyMarker> markers, String type, GoogleMap map2) {
        if (markers != null)
        {
           // Log.i(TAG,"Entered client markers, marker is not null");
           // Log.i(TAG,"marker size is" + markers.size());

            if (markers.size() > 0) {

                for (MyMarker myMarker : markers) {
                  //  Log.i(TAG,"Entered for loop for plotting markers");


                    if (type.equalsIgnoreCase("broker")) {
                        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));

                        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));

                        Marker currentMarker = map2.addMarker(markerOption);

                        mMarkersHashMap.put(currentMarker, myMarker);

                    } else if (type.equalsIgnoreCase("auction")) {
                        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));
                        Marker currentMarker = map2.addMarker(markerOption);
                        mMarkersHashMap.put(currentMarker, myMarker);
                    } else {
                        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));
                        Marker currentMarker = map2.addMarker(markerOption);
                        mMarkersHashMap.put(currentMarker, myMarker);
                    }

                }
            }
        }
    }


    private void plotBMarkers(ArrayList<MyMarker> markers, GoogleMap map2) {
        if (markers!=null)
        {

            if (markers.size() > 0) {
                for (MyMarker myMarker : markers) {

                    // Create user marker with custom icon and other options
                    MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude())).title(myMarker.getmLabel());
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));
                    map2.clear();
                    Marker currentMarker = map2.addMarker(markerOption);
                    mMarkersHashMap.put(currentMarker, myMarker);

                }
            }
        }
    }




}