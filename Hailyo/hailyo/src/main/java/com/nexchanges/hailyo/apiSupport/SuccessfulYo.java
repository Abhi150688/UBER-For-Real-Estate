package com.nexchanges.hailyo.apiSupport;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.nexchanges.hailyo.PostYoActivity;
import com.nexchanges.hailyo.PostYoActivity_Broker;
import com.nexchanges.hailyo.R;

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

/**
 * Created by TP on 04/08/15.
 */
public class SuccessfulYo {
    Dialog alertD;
    StringEntity se;
    boolean is_transaction,success;
    String URL = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000/1/yo/accept";
    public Context myContext;
    String timer_str;
    int timer_val;

    public void sendPostRequest(final String U_id, final String U_role, final String U_Lng, final String U_Lat, final String yo, Context context)
    {
       myContext = context;

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                showSplashScreen();
            }


            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("user_id", U_id);
                    jsonObject.accumulate("user_role", U_role);
                    jsonObject.accumulate("yoed", yo);
                    jsonObject.accumulate("long", U_Lng);
                    jsonObject.accumulate("lat", U_Lat);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(URL);


                try {
                    se = new StringEntity(jsonObject.toString());
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
                    System.out.print("Value of response code is: " + response);

                    if (response == 200 || response == 201) {
                        success = true;
                    } else {
                        System.out.print("Yo Failed, Please try again");
                        success = false;
                    }

                    InputStream inputStream = httpResponse.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("First Exception coz of HttpResponese :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Second Exception coz of HttpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                removeSplashScreen();
                if (success == false) {
                    Toast.makeText(
                            myContext,
                            "Yo Timed Out, \n Please try again!",
                            Toast.LENGTH_LONG).show();
                } else
                {
                    if (result != null) {

                        try {
                            JSONObject jObject = new JSONObject(result);
                            String tim_val = jObject.getString("time_to_meet");
                            String yoed = jObject.getString("yoed");
                            String bname = jObject.getString("name");
                            String mobile = jObject.getString("mobile_no");
                            String spec = jObject.getString("spec_code");
                            JSONArray rating = jObject.getJSONArray("rating");
                            String br_wow = rating.getString(0);
                            String br_not_wow = rating.getString(1);

                            String cl_wow = rating.getString(2);
                            String cl_not_wow = rating.getString(3);


                            if (yoed.equalsIgnoreCase("true"))
                                successfulYo(mobile, bname, tim_val, cl_wow, cl_not_wow,spec);
                            else {
                                Toast.makeText(
                                        myContext,
                                        "Too Late! \n User User already engaged \n Choose another requirement to Yo!",
                                        Toast.LENGTH_LONG).show();


                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", "Error: " + e.toString());
                        }

                    }
                }
            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(U_id, U_role, U_Lat, U_Lng, yo);
    }


    protected void showSplashScreen() {

        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        View View = layoutInflater.inflate(R.layout.splashscreen, null);

        alertD = new Dialog(myContext);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertD.setContentView(View);
        alertD.setCancelable(false);
        alertD.show();
        alertD.getWindow().setLayout(500, 600);

    }

    protected void removeSplashScreen() {
        if (alertD != null) {
            alertD.dismiss();
            alertD = null;
        }
    }

    private void successfulYo(String mob, String nam, String valT,String wow, String not_wow, String spec)
    {

        timer_val = Integer.valueOf(valT);
        Intent AfterYoBroker = new Intent(myContext, PostYoActivity_Broker.class);
        Bundle extras = new Bundle();
        extras.putInt("timer", timer_val);
        extras.putString("spec", spec);
        extras.putString("wow_rating", wow);
        extras.putString("not_wow_rating", not_wow);
        extras.putString("phone", mob);
        extras.putString("broker_Name", nam);
        AfterYoBroker.putExtras(extras);
        myContext.startActivity(AfterYoBroker);
        alertD.dismiss();
        ((Activity) myContext).finish();
        }
    }

