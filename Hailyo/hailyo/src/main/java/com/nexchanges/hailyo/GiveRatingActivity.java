package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nexchanges.hailyo.model.SharedPrefs;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AbhishekWork on 22/06/15.
 */

public class GiveRatingActivity extends Activity {

    TextView clock;
    Context context;
    Button but1,but2,but3,but4;
    int prop_count = 0;
    ImageView happy, sad;
    StringEntity se;
    String my_id,counter_id,hailyo_id,cust_type,role;
    boolean rating;
    String URL = "http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000/1/give/rating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_rating);
        context = this;

        SharedPrefs.save(context, SharedPrefs.SUCCESSFUL_HAIL, "false");

        cust_type = SharedPrefs.getString(context,SharedPrefs.CURRENT_CUST_TYPE);

        role = SharedPrefs.getString(context,SharedPrefs.MY_ROLE_KEY);


        clock = (TextView) findViewById(R.id.clock);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy K:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        clock.setText(date);

        //rating = (RatingBar) findViewById(R.id.ratingBar);

        happy = (ImageView) findViewById(R.id.happy);

        sad = (ImageView) findViewById(R.id.sad);

        if (cust_type.equalsIgnoreCase("avl"))
        {
            but1.setClickable(false);
            but2.setClickable(false);
            but3.setClickable(false);
            but4.setClickable(false);
        }

        else if (cust_type.equalsIgnoreCase("req"))
        {
            but1.setClickable(true);
            but2.setClickable(true);
            but3.setClickable(true);
            but4.setClickable(true);


        }


        but1 = (Button) findViewById(R.id.value1);

        but2 = (Button) findViewById(R.id.value2);

        but3 = (Button) findViewById(R.id.value3);

        but4 = (Button) findViewById(R.id.value4);


        my_id = SharedPrefs.getString(context, SharedPrefs.MY_USER_ID," ");
        counter_id = SharedPrefs.getString(context, SharedPrefs.MY_CURRENT_BROKER," ");

        hailyo_id = SharedPrefs.getString(context, SharedPrefs.MY_CURRENT_HAILYO," ");


        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prop_count = 1;
                but1.setBackgroundColor(Color.parseColor("#FFA500"));
                but1.setTextColor(Color.WHITE);
                but2.setBackgroundResource(R.drawable.button_border);
                but2.setTextColor(Color.BLACK);
                but3.setBackgroundResource(R.drawable.button_border);
                but3.setTextColor(Color.BLACK);
                but4.setBackgroundResource(R.drawable.button_border);
                but4.setTextColor(Color.BLACK);
            }
        });


        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prop_count = 2;
                but2.setBackgroundColor(Color.parseColor("#FFA500"));
                but2.setTextColor(Color.WHITE);
                but1.setBackgroundResource(R.drawable.button_border);
                but1.setTextColor(Color.BLACK);
                but3.setBackgroundResource(R.drawable.button_border);
                but3.setTextColor(Color.BLACK);
                but4.setBackgroundResource(R.drawable.button_border);
                but4.setTextColor(Color.BLACK);


            }
        });


        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prop_count = 3;
                but3.setBackgroundColor(Color.parseColor("#FFA500"));
                but3.setTextColor(Color.WHITE);
                but2.setBackgroundResource(R.drawable.button_border);
                but2.setTextColor(Color.BLACK);
                but1.setBackgroundResource(R.drawable.button_border);
                but1.setTextColor(Color.BLACK);
                but4.setBackgroundResource(R.drawable.button_border);
                but4.setTextColor(Color.BLACK);
            }
        });


        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prop_count = 4;
                but4.setBackgroundColor(Color.parseColor("#FFA500"));
                but4.setTextColor(Color.WHITE);
                but2.setBackgroundResource(R.drawable.button_border);
                but2.setTextColor(Color.BLACK);
                but3.setBackgroundResource(R.drawable.button_border);
                but3.setTextColor(Color.BLACK);
                but1.setBackgroundResource(R.drawable.button_border);
                but1.setTextColor(Color.BLACK);
            }
        });




        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = true;
                String rating1 = "" + 1;
                sendPostRequest(my_id, counter_id, hailyo_id,rating1);

            }
        });


        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = false;
                String rating1 = " " + 0;
                sendPostRequest(my_id, counter_id, hailyo_id,rating1);

            }
        });

    } //end of oncreate


    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void submitrating()
    {
        if (cust_type.equalsIgnoreCase("req"))
        {
            Intent NewBidActivity = new Intent(context, NewBidActivity.class);
            startActivity(NewBidActivity);
            finish();
        } else
        {
            if (role.equalsIgnoreCase("client"))
            {    Intent MainActivity = new Intent(context, MainActivity.class);
                startActivity(MainActivity);
                finish();
            }
            else if (role.equalsIgnoreCase("broker"))
            {
                Intent MainBrokerActivity = new Intent(context, MainBrokerActivity.class);
                startActivity(MainBrokerActivity);
                finish();

            }

        }

    }

    private void sendPostRequest(final String my_id, final String counter_id, final String hailyo_id, final String rating) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("user_id", counter_id);
                    jsonObject.accumulate("hailyo_id", hailyo_id);
                    jsonObject.accumulate("rating", rating);
                    jsonObject.accumulate("rater_id", my_id);

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
                        submitrating();
                    } else {
                        System.out.print("LoginFailed Try again");
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

//parse json response

                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObject = jArray.getJSONObject(i);

                        String name = jObject.getString("name");
                        String tab1_text = jObject.getString("tab1_text");
                        int active = jObject.getInt("active");

                    } // End Loop

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }

            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(my_id, counter_id, hailyo_id, rating);
    }

}
