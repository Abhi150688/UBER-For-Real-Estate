package com.nexchanges.hailyo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nexchanges.hailyo.GCMBroadcastReceiver;
import com.nexchanges.hailyo.model.SharedPrefs;

import java.io.IOException;

/**
 * Created by AbhishekWork on 18/07/15.
 */

public class GcmMessageHandler extends IntentService  {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String mes;
    private Handler handler;
    PendingIntent contentIntent;
    public static final String HANDLEGCM = "com.nexchanges.hailyo";
    Context context;


    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                sendNotification(extras.toString());

                Log.i(TAG, "Received: " + extras.toString());
                Intent broadcastIntent = new Intent("com.nexchanges.hailyo");
                broadcastIntent.setAction(HANDLEGCM);
                broadcastIntent.putExtra("gcm", mes);
                context.sendBroadcast(broadcastIntent);

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);



    }


    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (SharedPrefs.MY_ROLE_KEY.equalsIgnoreCase("broker"))
        {
        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainBrokerActivity.class), 0);}

        else if (SharedPrefs.MY_ROLE_KEY.equalsIgnoreCase("client"))

        {contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);}


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)

                        .setContentTitle("Hailyo Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);



        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mBuilder.setDefaults(defaults);
        // Set the content for Notification
        mBuilder.setContentText("New message from Server");
        // Set autocancel
        mBuilder.setAutoCancel(true);

                // Post a notification

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
