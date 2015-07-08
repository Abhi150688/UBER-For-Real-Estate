package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import me.alexrs.wavedrawable.WaveDrawable;

public class SearchSplashActivity extends Activity {

    Context context;
    String phone, brokerName, timer, rating;
    private WaveDrawable waveDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_search);
        context = this;

        TextView hailyo = (TextView)findViewById(R.id.hailyo);
        waveDrawable = new WaveDrawable(Color.parseColor("#FF0000"), 1000);
        hailyo.setBackgroundDrawable(waveDrawable);


        Interpolator interpolator;
        interpolator = new LinearInterpolator();
        waveDrawable.setWaveInterpolator(interpolator);
        waveDrawable.startAnimation();





        Intent PostYoIntent = getIntent();
        Bundle extras = PostYoIntent.getExtras();
        phone = extras.getString("Phone");
        brokerName = extras.getString("Broker_Name");
        timer = extras.getString("Timer");
        rating = extras.getString("Rating");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent PostYoAct = new Intent(context, PostYoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("Phone", phone);
                extras.putString("Broker_Name", brokerName);
                extras.putString("Timer", timer);
                extras.putString("Rating", rating);
                PostYoAct.putExtras(extras);
                context.startActivity(PostYoAct);
                SearchSplashActivity.this.finish();

            }
        }, 5000);


    }


}
