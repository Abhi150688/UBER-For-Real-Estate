package com.nexchanges.hailyo;


/**
 * Created by AbhishekWork on 21/06/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
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
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.ui.CustomMapFragment;
import com.nexchanges.hailyo.GoogleMapSupport.GetCurrentLocation;

import java.util.concurrent.TimeUnit;

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class PostYoActivity extends FragmentActivity
{


    Context context;

    public static final String TAG = MainActivity.class.getSimpleName();

    GoogleMap map;
    long ltimer,ltimer1;

    LatLng currentLocation;

    Button call, message, allVisits,allDeals,hail;
    ImageButton cancel;
    TextView timerTv, brokerTv;
    RatingBar ratingTv;
    String phone, brokerName, timer, rating, body,role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_yo);
        context = this;

        Intent PostYoIntent = getIntent();
        Bundle extras = PostYoIntent.getExtras();
        phone = extras.getString("phone");
        brokerName = extras.getString("broker_Name");
        timer = extras.getString("timer");
        rating = extras.getString("rating");
        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);

        call  = (Button) findViewById(R.id.call);

        message  = (Button) findViewById(R.id.message);

        hail  = (Button) findViewById(R.id.hailmode);


        cancel  = (ImageButton) findViewById(R.id.cancel);

        timerTv  = (TextView) findViewById(R.id.timer);

        brokerTv  = (TextView) findViewById(R.id.bname);

        ratingTv  = (RatingBar) findViewById(R.id.ratingBar);


        float ratingVal = Float.parseFloat(rating);
        ratingTv.setRating(ratingVal);
        brokerTv.setText(brokerName);

        ltimer = Long.parseLong(timer);
        ltimer1 = ltimer*1000*60;


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
                Intent ViewDeals = new Intent(context, MainActivity.class);
                SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,2);
                SharedPrefs.save(context,SharedPrefs.SUCCESSFUL_HAIL,"true");


                startActivity(ViewDeals);}

//                finish();

        });


        allVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ViewDeals = new Intent(context, MainActivity.class);
                SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 1);
                SharedPrefs.save(context,SharedPrefs.SUCCESSFUL_HAIL,"true");


                startActivity(ViewDeals);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to Cancel the Site Visit?");
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes-Cancel Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();


                        body = "Your site visit Id: ST123453 with Mr. Abhishek has just been cancelled";

                        sendSMSMessage(phone, body);
                        SharedPrefs.save(context,SharedPrefs.SUCCESSFUL_HAIL,"false");
                        if (role.equalsIgnoreCase("customer"))
                        {Intent MainActivity = new Intent (context, MainActivity.class);
                            startActivity(MainActivity);
                            finish();}
                        else if (role.equalsIgnoreCase("broker"))
                        {
                            Intent MainBActivity = new Intent (context, MainBrokerActivity.class);
                            startActivity(MainBActivity);
                        finish();}
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });



        // Google Map ..


        // Initialize the HashMap for Markers and MyMarker object

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
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
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
