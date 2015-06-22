package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AbhishekWork on 22/06/15.
 */

public class GiveRatingActivity extends Activity {

    TextView clock;
    String spec_code;
    Button endBut;
    Context context;
    ImageButton sendBid;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.during_visit);
        context = this;

        clock = (TextView)findViewById(R.id.clock);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy K:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        clock.setText(date);

        sendBid = (ImageButton)findViewById(R.id.sendbid);

        rating = (RatingBar)findViewById(R.id.ratingBar);

        rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = rating.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    rating.setRating(stars);

                    Intent MainActivity =  new Intent(context, MainActivity.class);
                    startActivity(MainActivity);

                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});



            sendBid.setOnClickListener(new View.OnClickListener()

                                       {
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


                                                       Intent Morebids = new Intent(context, ActionBeforeMain.class);
                                                       startActivity(Morebids);

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
                                       }

            );

        }


}
