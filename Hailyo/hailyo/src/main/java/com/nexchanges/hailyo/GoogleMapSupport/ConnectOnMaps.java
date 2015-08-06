package com.nexchanges.hailyo.GoogleMapSupport;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TP on 06/08/15.
 */
public class ConnectOnMaps {

    String url;
    GoogleMap maps, maps2;

    ArrayList<LatLng> markerPoints;
    ArrayList<String> A1 = new ArrayList<String>();
    ArrayList<String> A2 = new ArrayList<String>();
    ArrayList<String> A3 = new ArrayList<String>();

    String distance, duration;
    private static final String TAG = ConnectOnMaps.class.getSimpleName();


    public ArrayList connectonMap(final GoogleMap map, LatLng point, LatLng point1) {
        Log.i(TAG,"Entered Connect Maps");
        markerPoints = new ArrayList<LatLng>();

        if (markerPoints.size() > 1) {
            markerPoints.clear();
            map.clear();
        }

        markerPoints.add(point);
        markerPoints.add(point1);
        MarkerOptions options = new MarkerOptions();
        options.position(point);

        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }



        // Add new marker to the Google Map Android API V2
        map.addMarker(options);

        if (markerPoints.size() >= 2) {
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);
            Log.i(TAG, "url for getting directions is as below" + url);

            MyTaskParams params = new MyTaskParams(url,map);

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(params);


        }

        return A3;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.i(TAG, "data string url in http request is" + data);

            br.close();

        } catch (Exception e) {
            Log.i(TAG, "Exception while downloading url" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


       public class DownloadTask extends AsyncTask<MyTaskParams, Void, String> {

            // Downloading data in non-ui thread
            @Override
            protected String doInBackground(MyTaskParams...params) {

                // For storing data from web service
                String data = "";
                url = params[0].Url;
                maps = params[0].map;

                try {
                    // Fetching the data from web service
                    data = downloadUrl(url);
                    Log.i(TAG,"Data downloaded from downloadURL is this" + data);
                } catch (Exception e) {
                    Log.i("Background Task", e.toString());
                }
                return data;
            }

            // Executes in UI thread, after the execution of
            // doInBackground()
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.i(TAG, "Calling parse task method");
                MyTaskParams2 params2 = new MyTaskParams2(result,maps);

                ParserTask parserTask = new ParserTask();
                parserTask.execute(params2);

            }
        }

    /**
     * A class to parse the Google Places in JSON format
     */
  private class ParserTask extends AsyncTask<MyTaskParams2, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(MyTaskParams2... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            String result = jsonData[0].Url;
            maps2 = jsonData[0].map;

            try {
                jObject = new JSONObject(result);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
           // PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            if (result.size() < 1) {
                Log.i(TAG,"No data to plot in parse method");
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                Log.i(TAG,"Plotting data in parse");
                points = new ArrayList<LatLng>();
               // lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        Log.i(TAG,"Distance is" + distance);
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        Log.i(TAG,"Duration is" + duration);
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    Log.i(TAG,"Adding points on map");
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
               // lineOptions.addAll(points);
               // lineOptions.width(2);
               // lineOptions.color(Color.RED);
                Log.i(TAG, "Done plotting on map with red line");
            }



            // Drawing polyline in the Google Map for the i-th route
           // maps2.addPolyline(lineOptions);
            A1.add(distance);
            A1.add(duration);
            Log.i(TAG, "Calculated Distance is" + distance);
            Log.i(TAG, "Calculated duration is" + duration);

        }
    }
    //    return A1;


    private static class MyTaskParams{
        String Url;
        GoogleMap map;

        MyTaskParams(String Url, GoogleMap map)
        {
            this.Url = Url;
            this.map = map;
        }

    }

    private static class MyTaskParams2{
        String Url;
        GoogleMap map;

        MyTaskParams2(String Url, GoogleMap map)
        {
            this.Url = Url;
            this.map = map;
        }

    }

}

