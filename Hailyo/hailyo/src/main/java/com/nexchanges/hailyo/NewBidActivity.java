package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by AbhishekWork on 23/06/15.
 */
public class NewBidActivity extends Activity {

    Context context;
    TextView bidbrokername;
    EditText rowtv1,rowtv2,rowtv3,rowtv4,rowtv5,rowtv6,rowtv7,rowtv8,srowtv1,srowtv2,srowtv3,srowtv4;
    ViewFlipper VF;
    String fetchprop,fetchrent,fetchdep,fetchdate,role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_bid_2);
        context = this;


        rowtv1 = (EditText) findViewById(R.id.rowtv1);
        rowtv2 = (EditText) findViewById(R.id.rowtv2);
        rowtv3 = (EditText) findViewById(R.id.rowtv3);
        rowtv4 = (EditText)findViewById(R.id.rowtv4);
        rowtv5 = (EditText) findViewById(R.id.rowtv5);
        rowtv6 = (EditText) findViewById(R.id.rowtv6);
        rowtv7 = (EditText) findViewById(R.id.rowtv7);
        rowtv8 = (EditText) findViewById(R.id.rowtv8);

        srowtv1 = (EditText) findViewById(R.id.srowtv1);
        srowtv2 = (EditText) findViewById(R.id.srowtv2);
        srowtv3 = (EditText) findViewById(R.id.srowtv3);
        srowtv4 = (EditText)findViewById(R.id.srowtv4);

        bidbrokername = (TextView)findViewById(R.id.bname);



        VF = (ViewFlipper)findViewById(R.id.VF);


        final AlertDialog alertD = new AlertDialog.Builder(context).create();


        Button submit = (Button) findViewById(R.id.submit);

        Button reset = (Button) findViewById(R.id.reset);

        Button cancel = (Button) findViewById(R.id.cancel);

        Button ssubmit = (Button) findViewById(R.id.ssubmit);
        Button scancel = (Button) findViewById(R.id.scancel);
        ImageButton plus = (ImageButton)findViewById(R.id.plus);
        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);

        ImageButton minus = (ImageButton)findViewById(R.id.minus);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(1);
                fetchprop = srowtv1.getText().toString();
                fetchrent = srowtv2.getText().toString();
                fetchdep = srowtv3.getText().toString();
                fetchdate = srowtv4.getText().toString();

                rowtv1.setText(fetchprop);
                rowtv2.setText(fetchrent);
                rowtv3.setText(fetchdep);
                rowtv4.setText(fetchdate);

            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(0);
                fetchprop = rowtv1.getText().toString();
                fetchrent = rowtv2.getText().toString();
                fetchdep = rowtv3.getText().toString();
                fetchdate = rowtv4.getText().toString();

                srowtv1.setText(fetchprop);
                srowtv2.setText(fetchrent);
                srowtv3.setText(fetchdep);
                srowtv4.setText(fetchdate);

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendEmail();

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.action_before_main, null);

                Button newBid = (Button)promptView.findViewById(R.id.newbid);

                Button newVisit = (Button)promptView.findViewById(R.id.newvisit);

                Button allDeals = (Button)promptView.findViewById(R.id.trans);




                final AlertDialog alertD = new AlertDialog.Builder(context).create();

                newBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent RestartNewBid = new Intent(context,NewBidActivity.class);
                        startActivity(RestartNewBid);
                        finish();

                    }
                });

                newVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (role.matches("Customer")) {
                            Intent MainActivity = new Intent(context, MainActivity.class);
                            startActivity(MainActivity);
                            finish();
                        }
                        else{
                            Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                            startActivity(BMainActivity);
                            finish();

                        }
                    }
                });

                allDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent SettingsActivity = new Intent(context, SettingsActivity.class);
                        startActivity(SettingsActivity);
                        finish();
                    }
                });


                alertD.setView(promptView);
                alertD.show();

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                rowtv1.getText().clear();
                rowtv2.getText().clear();
                rowtv3.getText().clear();
                rowtv4.getText().clear();
                rowtv5.getText().clear();
                rowtv6.getText().clear();
                rowtv7.getText().clear();
                rowtv8.getText().clear();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (role.matches("Customer")) {
                    Intent MainActivity = new Intent(context, MainActivity.class);
                    startActivity(MainActivity);
                    finish();
                }
                else{
                    Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                    startActivity(BMainActivity);
                    finish();

                }
            }
        });

        ssubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendshortEmail();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.action_before_main, null);

                Button newBid = (Button)promptView.findViewById(R.id.newbid);

                Button newVisit = (Button)promptView.findViewById(R.id.newvisit);

                Button allDeals = (Button)promptView.findViewById(R.id.trans);




                final AlertDialog alertD = new AlertDialog.Builder(context).create();

                newBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent RestartNewBid = new Intent(context,NewBidActivity.class);
                        startActivity(RestartNewBid);
                        finish();

                    }
                });

                newVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (role.matches("Customer")) {
                            Intent MainActivity = new Intent(context, MainActivity.class);
                            startActivity(MainActivity);
                            finish();
                        }
                        else{
                            Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                            startActivity(BMainActivity);
                            finish();

                        }
                    }
                });

                allDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent SettingsActivity = new Intent(context, SettingsActivity.class);
                        startActivity(SettingsActivity);
                        finish();
                    }
                });


                alertD.setView(promptView);
                alertD.show();


            }
        });


        scancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (role.matches("Customer")) {
                    Intent MainActivity = new Intent(context, MainActivity.class);
                    startActivity(MainActivity);
                    finish();
                }
                else{
                    Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                    startActivity(BMainActivity);
                    finish();

                }
            }
        });


    }

    public void sendEmail()
    {
        String sub = rowtv1.getText().toString();
        String subject = "Offer for: " + sub;

        StringBuilder body = new StringBuilder();
        body.append("Property: " + rowtv1.getText().toString());
        body.append("\nPrice-Rent: " + rowtv2.getText().toString());
        body.append("\nPrice-Deposit: " + rowtv3.getText().toString());
        body.append("\nStart Date: " + rowtv4.getText().toString());
        body.append("\nTenure: " + rowtv5.getText().toString());
        body.append("\nLockin: " + rowtv6.getText().toString());
        body.append("\nLicense Type: " + rowtv7.getText().toString());
        body.append("\nOccupant Name: " + rowtv8.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abhishek@nexchanges.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }


    public void sendshortEmail()
    {
        String sub = srowtv1.getText().toString();
        String subject = "Offer for: " + sub;

        StringBuilder body = new StringBuilder();
        body.append("Property: " + srowtv1.getText().toString());
        body.append("\nPrice-Rent: " + srowtv2.getText().toString());
        body.append("\nPrice-Deposit: " + srowtv3.getText().toString());
        body.append("\nStart Date: " + srowtv4.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abhishek@nexchanges.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}
