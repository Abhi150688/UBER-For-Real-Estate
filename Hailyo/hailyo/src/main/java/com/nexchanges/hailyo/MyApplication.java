package com.nexchanges.hailyo;

import android.app.Application;
import com.parse.Parse;
/**
 * Created by Abhishek on 28/04/15.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oH91zJa39ixjVYKNPHl4vpKRdW27NwEFrzSF8j2o", "jaLWrQ34xPUwYc0iUZuXdONI8ZwGCCnyh1fbdwyY");

    }
}
