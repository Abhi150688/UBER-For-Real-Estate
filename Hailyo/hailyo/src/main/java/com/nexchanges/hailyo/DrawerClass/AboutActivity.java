package com.nexchanges.hailyo.DrawerClass;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.nexchanges.hailyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Abhishek on 08/05/15.
 */
public class AboutActivity extends Activity {
    Button fblike, visitSite, playstore_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_hailyo);
        playstore_rate = (Button) findViewById(R.id.app_store);

        fblike = (Button) findViewById(R.id.facebook_like);

        visitSite = (Button) findViewById(R.id.website);

        playstore_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.nexchanges.hailyo"));
                startActivity(intent);
            }
        });

        fblike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/426253597411506"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
                }

            }
        });

        visitSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.hailyo.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
}
