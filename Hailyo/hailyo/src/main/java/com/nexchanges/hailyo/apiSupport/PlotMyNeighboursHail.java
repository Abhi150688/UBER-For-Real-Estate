package com.nexchanges.hailyo.apiSupport;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.Marker;
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
    private ArrayList<MyMarker> mMyMarkersArray2 = new ArrayList<MyMarker>();
    private ArrayList<MyMarker> mMyMarkersArray3 = new ArrayList<MyMarker>();


    private HashMap<Marker, MyMarker> mMarkersHashMap;

    public ArrayList markerpos(String my_user_id, String lat, String lng, String brokerType, String user_role)

    {
        System.out.print(my_user_id + "Hurray " +lat + " hurrah again" + lng );
        mMyMarkersArray = sendPostRequest(my_user_id,lat,lng,brokerType, user_role);

      /*  for (int i = 0; i<mMyMarkersArray.size(); i++)
        {
            System.out.println("Lat returned by post is " + mMyMarkersArray.get(i).getmLatitude());
            System.out.println("Long returned by post is" + mMyMarkersArray.get(i).getmLongitude());

        }*/
        return mMyMarkersArray;

    }

    private ArrayList sendPostRequest(final String my_user_id, final String lat, final String lng, final String brokerType, final String user_role)
    {


        class SendPostReqAsyncTask extends AsyncTask<String, Void,String> {

            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {

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

                // In a POST request, we don't pass the values in the URL.
                //Therefore we use only the web page URL as the parameter of the HttpPost argument
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

                        System.out.print("Here is the HTTP response" + response);
                        InputStream inputStream = httpResponse.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }
                        String result = stringBuilder.toString();

                        System.out.print("Final String result of asynch task is " + result);

                        mMyMarkersArray2 = createMarkerHash(result);
                      /*  for (int i = 0; i<mMyMarkersArray2.size(); i++)
                        {
                            System.out.println("Lat returned by array 2 is " + mMyMarkersArray2.get(i).getmLatitude());
                            System.out.println("Long returned by array2 is" + mMyMarkersArray2.get(i).getmLongitude());

                        }
*/
                    }

                    else
                    {
                        mMyMarkersArray2 = null;

                        System.out.print("my marker array 2 is null");
                    }


                } catch (ClientProtocolException cpe) {
                    System.out.println("First Exception coz of HttpResponese :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Second Exception coz of HttpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(my_user_id, lat, lng, brokerType);
        return mMyMarkersArray2;
    }


    private ArrayList createMarkerHash(String result)
    {

        try {
            System.out.println("Got successful lat lng response in json file");


            JSONObject jObject = new JSONObject(result);
            JSONArray user__list = jObject.getJSONArray("users");

            for (int j = 0;j<user__list.length();j++)
            {

                JSONObject attri = user__list.getJSONObject(j);
                String lat = attri.getString("lat");
                String lng = attri.getString("long");

                System.out.print("Lat is" + lat);
                System.out.print("Long is" + lng);
                mMarkersHashMap = new HashMap<Marker, MyMarker>();
                mMyMarkersArray3.add(new MyMarker(Double.parseDouble(lat), Double.parseDouble(lng)));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Printing data of MyArray 3");


       /* for (int i = 0; i<mMyMarkersArray3.size(); i++)
        {
            System.out.println("Lat returned by array 3 is " + mMyMarkersArray3.get(i).getmLatitude());
            System.out.println("Long returned by array3 is" + mMyMarkersArray3.get(i).getmLongitude());

        }*/


        return mMyMarkersArray3;
    }



}