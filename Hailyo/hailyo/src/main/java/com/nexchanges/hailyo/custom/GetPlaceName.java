package com.nexchanges.hailyo.custom;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetPlaceName{

    public GetPlaceNameCallback callback;

    public interface GetPlaceNameCallback {
        void onStart();
        void onComplete(boolean status, LatLng location, String placeName);
    }

    public GetPlaceName(final LatLng location, GetPlaceNameCallback call_back){

        callback = call_back;

        new AsyncTask<Void, Void, Void>() {

            String response_string="";
            JSONObject response_json = new JSONObject();
            int status_code;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onStart();
            }

            @Override
            protected Void doInBackground(Void... params) {
                String link="http://maps.googleapis.com/maps/api/geocode/json?latlng="+location.latitude+","+location.longitude+"&sensor=true";

                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(link);
                HttpResponse response=null;

                try {
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");

                    response = client.execute(post);
                    status_code = response.getStatusLine().getStatusCode();
                    response_string = EntityUtils.toString(response.getEntity());
                    response_json = new JSONObject(response_string);

                }catch (Exception e){ e.printStackTrace();}

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                boolean status = false;
                String pName = "NA";

                if (status_code == 200){
                    JSONArray results=null;
                    try{
                        results=response_json.getJSONArray("results");
                    }catch (Exception e){}

                    JSONObject addressJSON=null;
                    if( results != null ){
                        try{addressJSON = results.getJSONObject(0);}catch (Exception e){}
                    }

                    if( addressJSON != null ){
                        try{
                            pName = (String) addressJSON.get("formatted_address");
                            status = true;
                        }catch (Exception e){e.printStackTrace();}
                    }

                    Log.d("http", " [GEOCODE_SUCCESS] :  , pName : " + pName);
                }
                else if (status_code == 401){
                    Log.d("http", " [GEOCODE FAILURE] :  Msg : " + response_string);
                }

                else if (status_code == 500){
                    Log.d("http", " [GEOCODE ERROR] :  , Msg : " + response_string);
                }
                callback.onComplete(status, location, pName);

            }

        }.execute();

    }
}