package com.nexchanges.hailyo.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nexchanges.hailyo.MainActivity;
import com.nexchanges.hailyo.MainBrokerActivity;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by AbhishekWork on 18/07/15.
 */

public class GcmMessageHandler extends IntentService {

    public static int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String mes, message;
    private Handler handler;
    PendingIntent brokerIntent, clientIntent;
    int color = 0xff123456, requestID;
    public static final String HANDLEGCM = "com.nexchanges.hailyo";
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;



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
        message = extras.getString("message");

        // showToast();
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

               /* Log.i(TAG, "Received: " + extras.toString());
                Intent broadcastIntent = new Intent("com.nexchanges.hailyo");
                broadcastIntent.setAction(HANDLEGCM);
                broadcastIntent.putExtra("gcm", mes);
                this.sendBroadcast(broadcastIntent);*/

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);


    }


    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        requestID = (int) System.currentTimeMillis();
        brokerIntent = PendingIntent.getActivity(this, requestID,
                new Intent(this, MainBrokerActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        clientIntent = PendingIntent.getActivity(this, requestID,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)

                        .setContentTitle(mes)

                        .setSmallIcon(R.drawable.hailyo_mono)
                        .setDefaults(defaults)
                        .setTicker("New Hailyo Notification")
                        .setAutoCancel(true)
                        .setColor(color)
                        .setContentTitle(message);

        String role = SharedPrefs.getString(this, SharedPrefs.MY_ROLE_KEY);
        System.out.print("My role is" + role);

        if (role.equalsIgnoreCase("broker")) {
            mBuilder.setContentIntent(brokerIntent);
        } else if (role.equalsIgnoreCase("client")) {
            mBuilder.setContentIntent(clientIntent);
        }
        addView();
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);


        // Post a notification
        if (NOTIFICATION_ID > 1073741824) {
            NOTIFICATION_ID = 0;
        }

        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
    }

    public void addView() {
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.centerhailyo);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(chatHead, params);

        try{
            chatHead.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    if(System.currentTimeMillis()-touchStartTime> ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
                        windowManager.removeView(chatHead);
                        stopSelf();
                        return false;
                    }
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, params);
                            break;
                    }
                    return false;
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}

