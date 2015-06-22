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
import android.widget.ImageButton;

import java.text.DateFormat;

/**
 * Created by AbhishekWork on 22/06/15.
 */
public class ActionBeforeMain extends Activity {

    Context context;
    Button newBid,newVisit, allDeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_before_main);
        context = this;

        newBid = (Button)findViewById(R.id.newbid);

        newVisit = (Button)findViewById(R.id.newvisit);

        allDeals = (Button)findViewById(R.id.trans);

        newBid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View promptView = layoutInflater.inflate(R.layout.submit_bid, null);

                        final EditText rowtv1 = (EditText)findViewById(R.id.rowtv1);
                        final EditText rowtv2 = (EditText)findViewById(R.id.rowtv2);
                        final EditText rowtv3 = (EditText)findViewById(R.id.rowtv3);
                        //final DatePicker rowtv4 = (DatePicker)findViewById(R.id.rowtv4);
                        final EditText rowtv5 = (EditText)findViewById(R.id.rowtv5);
                        final EditText rowtv6 = (EditText)findViewById(R.id.rowtv6);
                        final EditText rowtv7 = (EditText)findViewById(R.id.rowtv7);
                        final EditText rowtv8 = (EditText)findViewById(R.id.rowtv8);

                        final AlertDialog alertD = new AlertDialog.Builder(context).create();


                        Button submit = (Button) promptView.findViewById(R.id.submit);

                        Button reset = (Button) promptView.findViewById(R.id.reset);

                        Button cancel = (Button) promptView.findViewById(R.id.cencel);


                        submit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {


                                DatePicker myDatePicker = (DatePicker) findViewById(R.id.rowtv4);
                                String selectedDate = DateFormat.getDateInstance().format(myDatePicker.getCalendarView().getDate());

                                String sub = rowtv1.getText().toString();
                                String subject = "Offer for: " + sub;

                                StringBuilder body = new StringBuilder();
                                body.append("Property: " + rowtv1.getText().toString());
                                body.append("\nPrice-Rent: " + rowtv2.getText().toString());
                                body.append("\nPrice-Deposit: " + rowtv3.getText().toString());
                                body.append("\nStart Date: "+selectedDate);
                                body.append("\nTenure: "+rowtv5.getText().toString());
                                body.append("\nLockin: "+rowtv6.getText().toString());
                                body.append("\nLicense Type: "+rowtv7.getText().toString());
                                body.append("\nOccupant Name: " + rowtv8.getText().toString());

                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "abhishek@nexchanges.com", null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
                                emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                                 alertD.dismiss();

                            }
                        });

                        reset.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                rowtv1.getText().clear();
                                rowtv2.getText().clear();
                                rowtv3.getText().clear();
                                // rowtv4.getText().clear();
                                rowtv5.getText().clear();
                                rowtv6.getText().clear();
                                rowtv7.getText().clear();
                                rowtv8.getText().clear();
                            }
                        });


                        cancel.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                alertD.dismiss();
                            }
                        });

                        alertD.setView(promptView);

                        alertD.show();

                    }
        });


        newVisit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent MainActivity = new Intent(context, MainActivity.class);
                startActivity(MainActivity);

            }
        });


        allDeals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent History = new Intent(context, HistoryActivity.class);
                startActivity(History);

            }
        });


    }


    }
