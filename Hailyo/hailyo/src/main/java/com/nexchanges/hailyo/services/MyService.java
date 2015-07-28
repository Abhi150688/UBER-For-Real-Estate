package com.nexchanges.hailyo.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by Abhishek on 27/04/15.
 */
public class MyService extends IntentService {
    public MyService() {
        super("My Service");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Service Created",Toast.LENGTH_SHORT).show();
        //new PubnubHandler(this).subscribe();
        //new PubnubHandler(this).publish("Received from Dev Console.....hahahahhahahahahah");
        //new PubnubHandler(this).herenow();
        //new PubnubHandler(this).presence();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent arg0) {

    }
}

