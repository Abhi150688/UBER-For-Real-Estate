package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nexchanges.hailyo.model.SharedPrefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AbhishekWork on 22/06/15.
 */

public class GiveRatingActivity extends Activity implements RatingBar.OnRatingBarChangeListener {

    TextView clock;
    Context context;
    RatingBar rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_rating);
        context = this;

        clock = (TextView) findViewById(R.id.clock);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy K:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        clock.setText(date);

        rating = (RatingBar) findViewById(R.id.ratingBar);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent NewBidActivity = new Intent(context, NewBidActivity.class);
                startActivity(NewBidActivity);
                finish();

            }
        }); //end of rating on click


    } //end of oncreate


    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }


}
