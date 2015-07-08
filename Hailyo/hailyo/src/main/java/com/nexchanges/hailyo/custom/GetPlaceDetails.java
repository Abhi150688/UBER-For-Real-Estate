package com.nexchanges.hailyo.custom;

/**
 * Created by Abhishek on 08/05/15.
 */
import android.content.Intent;
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

public class GetPlaceDetails {

    public GetPlaceDetailsCallback callback;

    public interface GetPlaceDetailsCallback {
        void onStart();
        void onComplete(JSONObject placeDetails);
    }

    public GetPlaceDetails(final String placeId, final String googleAPIKey, final GetPlaceDetailsCallback call_back){

        callback = call_back;

        new AsyncTask<Void, Void, Void>() {

            String response_string = "";
            JSONObject response_json = new JSONObject();
            int status_code;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                String link = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
                try {
                    link = link + URLEncoder.encode(placeId, "utf-8") + "&key=" + googleAPIKey;
                } catch (Exception e) { e.printStackTrace();    }

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

                    Log.d("http", " [PLACE-DETAILS_SUCCESS] :  , pDetails  : " + response_string);

                    JSONObject details=null;
                    try{
                        details = response_json.getJSONObject("result");
                    }catch (Exception e){   e.printStackTrace();  }

                    callback.onComplete(details);

                } else if (status_code == 401) {
                    Log.d("http", " [PLACE-DETAILS FAILURE] :  Msg : " + response_string);
                } else if (status_code == 500) {
                    Log.d("http", " [PLACE-DETAILS ERROR] :  , Msg : " + response_string);
                }
            }

        }.execute();

    }
}