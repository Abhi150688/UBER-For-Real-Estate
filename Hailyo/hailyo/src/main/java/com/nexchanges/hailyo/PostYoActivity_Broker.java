package com.nexchanges.hailyo;


/**
 * Created by AbhishekWork on 21/06/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.nexchanges.hailyo.GoogleMapSupport.GetCurrentLocation;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.ui.CustomMapFragment;

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
import java.util.concurrent.TimeUnit;

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class PostYoActivity_Broker extends FragmentActivity
{

    Context context;
    public static final String TAG = PostYoActivity_Broker.class.getSimpleName();
    GoogleMap map;
    long ltimer1;
    int timer_int;
    boolean is_transaction;

    LatLng currentLocation;

    Button call, message, allVisits,allDeals,hail,yo;
    ImageButton cancel;
    TextView timerTv, brokerTv;
    RatingBar brokerRating;
    String phone, brokerName,rating, body,role,user_id,lng,lat,yoed = "true";
    float ratingVal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_yo_broker);
        context = this;

        user_id = SharedPrefs.getString(context, SharedPrefs.MY_USER_ID);


        lng = SharedPrefs.getString(context, SharedPrefs.MY_CUR_LNG);
        lat = SharedPrefs.getString(context, SharedPrefs.MY_CUR_LAT);


        Intent PostYoIntent = getIntent();
        Bundle extras = PostYoIntent.getExtras();
        phone = extras.getString("phone");
        brokerName = extras.getString("broker_Name");
        timer_int = extras.getInt("timer");
        rating = extras.getString("rating");
        Log.i(TAG,"Phone fetched from intent " + phone);

        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);

        call  = (Button) findViewById(R.id.call);

        message  = (Button) findViewById(R.id.message);


        hail  = (Button) findViewById(R.id.hailmode);


        yo  = (Button) findViewById(R.id.yomode);

        cancel  = (ImageButton) findViewById(R.id.cancel);

        timerTv  = (TextView) findViewById(R.id.timer);

        brokerTv  = (TextView) findViewById(R.id.bname);

        brokerRating  = (RatingBar) findViewById(R.id.ratingBar);


        ratingVal = Float.parseFloat(rating);
        Log.i(TAG,"String value of rating is " + rating);
        Log.i(TAG, "Float value of rating is " + ratingVal);

        brokerRating.setRating(Float.parseFloat(rating));
        brokerTv.setText(brokerName);


        ltimer1 = timer_int*1000*60;

        allDeals = (Button) findViewById(R.id.activedeals);

        allVisits = (Button) findViewById(R.id.activevisits);



        new CountDownTimer(ltimer1, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTv.setText(""+String.format("%d min",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                timerTv.setText(brokerName + " should have arrived!");
                Intent DuringVisitActivity=new Intent(context, DuringVisitActivity.class);
                startActivity(DuringVisitActivity);
                finish();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        }.start();



        allDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,2);
                SharedPrefs.save(context, SharedPrefs.SUCCESSFUL_HAIL, "true");
                startActivity(BMainActivity);

//                finish();
            }
        });


        allVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 1);
                SharedPrefs.save(context, SharedPrefs.SUCCESSFUL_HAIL, "true");

                startActivity(BMainActivity);


            }
        });



        message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + phone));
                startActivity(smsIntent);
            }
        });




        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Uri call = Uri.parse("tel:" + phone);
                Intent callintent = new Intent(Intent.ACTION_CALL, call);
                startActivity(callintent);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                body = "Your site visit Id: ST123453 with Mr. Abhishek has just been cancelled";


                sendSMSMessage(phone, body);
                SharedPrefs.save(context, SharedPrefs.SUCCESSFUL_HAIL, "false");
                    Intent MainBActivity = new Intent (context, MainBrokerActivity.class);
                    startActivity(MainBActivity);
            }
        });

        CustomMapFragment customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);

            }
        });

// Current Location ..

        new GetCurrentLocation(context, new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(15));

                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        return super.onKeyDown(keyCode, event);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
    }

    protected void sendSMSMessage(String phoneNo,String message) {
        Log.i("Send SMS", "");

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "VISIT CANCELLED", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());

    }

}
