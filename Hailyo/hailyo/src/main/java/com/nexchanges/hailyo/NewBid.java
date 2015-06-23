package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;

/**
 * Created by AbhishekWork on 23/06/15.
 */
public class NewBid extends Activity {

    Context context;
    EditText rowtv1,rowtv2,rowtv3,rowtv4,rowtv5,rowtv6,rowtv7,rowtv8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_bid);
        context = this;


        rowtv1 = (EditText) findViewById(R.id.rowtv1);
        rowtv2 = (EditText) findViewById(R.id.rowtv2);
        rowtv3 = (EditText) findViewById(R.id.rowtv3);
        rowtv4 = (EditText)findViewById(R.id.rowtv4);
        rowtv5 = (EditText) findViewById(R.id.rowtv5);
        rowtv6 = (EditText) findViewById(R.id.rowtv6);
        rowtv7 = (EditText) findViewById(R.id.rowtv7);
        rowtv8 = (EditText) findViewById(R.id.rowtv8);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();


        Button submit = (Button) findViewById(R.id.submit);

        Button reset = (Button) findViewById(R.id.reset);

        Button cancel = (Button) findViewById(R.id.cencel);


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
                        Intent RestartNewBid = new Intent(context,NewBid.class);
                        startActivity(RestartNewBid);

                    }
                });

                newVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent MainActivity = new Intent(context, MainActivity.class);
                        startActivity(MainActivity);

                    }
                });

                allDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent SettingsActivity = new Intent(context, SettingsActivity.class);
                        startActivity(SettingsActivity);
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

                Intent MainActivity = new Intent(context, MainActivity.class);
                startActivity(MainActivity);

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
}
