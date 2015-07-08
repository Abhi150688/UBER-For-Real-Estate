package com.nexchanges.hailyo.custom;

/**
 * Created by Abhishek on 08/05/15.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nexchanges.hailyo.ui.WebClientDevWrapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.URLEncoder;

public class GetAutoPredictions{

    public GetAutoPredictionsCallback callback;

    public interface GetAutoPredictionsCallback {
        void onStart();
        void onComplete(boolean status, String requestedSearchName, JSONArray predictions);
    }

    public GetAutoPredictions(final String searchName, final LatLng nearLocation, final String googleAPIKey, GetAutoPredictionsCallback call_back){

        callback = call_back;

        new AsyncTask<Void, Void, Void>() {

            String response_string = "";
            JSONObject response_json = new JSONObject();
            int status_code;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onStart();
            }

            @Override
            protected Void doInBackground(Void... params) {

                String link = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
                try {
                    link = link + URLEncoder.encode(searchName, "utf-8") + "&location=" + nearLocation.latitude + "," + nearLocation.longitude + "&radius=10000&key=" +googleAPIKey;
                } catch (Exception e) {
                }

                DefaultHttpClient client = (DefaultHttpClient) WebClientDevWrapper.getNewHttpClient();
                HttpPost post = new HttpPost(link);
                HttpResponse response = null;

                try {
                    response = client.execute(post);
                    status_code = response.getStatusLine().getStatusCode();
                    response_string = EntityUtils.toString(response.getEntity());
                    response_json = new JSONObject(response_string);

                } catch (Exception e) { e.printStackTrace();    }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (status_code == 200) {
                    Log.d("http", " [AUTO-COMPLETE_SUCCESS] :  , Predictions  : " + response_string);
                    JSONArray predictions = null;
                    try {
                        predictions = response_json.getJSONArray("predictions");
                    } catch (Exception e) { e.printStackTrace(); }

                    callback.onComplete(true, searchName, predictions);

                } else if (status_code == 401) {
                    Log.d("http", " [AUTO-COMPLETE FAILURE] :  Msg : " + response_string);
                    callback.onComplete(false, searchName, null);
                } else if (status_code == 500) {
                    Log.d("http", " [AUTO-COMPLETE ERROR] :  , Msg : " + response_string);
                    callback.onComplete(false, searchName, null);
                }
            }

        }.execute();

    }
}