package com.nexchanges.hailyo;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Abhishek on 28/04/15.
 */

public class MyApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "lwYQJ7C00XfZvyFrVSyN2Dy3Y";
    private static final String TWITTER_SECRET = "yGGbIty4LI1FE9CZc247vTVna4Vgr97yqI4vu3m29Wy0kTiWnn";


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oH91zJa39ixjVYKNPHl4vpKRdW27NwEFrzSF8j2o", "jaLWrQ34xPUwYc0iUZuXdONI8ZwGCCnyh1fbdwyY");

    }
}
