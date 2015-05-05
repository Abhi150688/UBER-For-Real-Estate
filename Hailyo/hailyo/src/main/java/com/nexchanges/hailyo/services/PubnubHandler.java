package com.nexchanges.hailyo.services;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pubnub.api.*;
import com.pubnub.api.Callback;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by Abhishek on 27/04/15.
 */
public class PubnubHandler {
    public static final String GLOBAL_CHANNEL = "demo_tutorial";
    public static final String PUBLISH_KEY =
            "demo";
    public static final String SUBSCRIBE_KEY =
            "demo";
    private Context context;
    private Pubnub pubnub;


    public PubnubHandler(Context context) {

        this.context = context;
        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
        pubnub.setRetryInterval(1000);
    }

    public void notifyUser(String message) {

        final String msg = message;
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void subscribe() {

        try {
            pubnub.subscribe("demo_tutorial", new Callback () {


            @Override
            public void connectCallback(String channel, Object message) {
                Log.d("Service Message", "Subscribed");

            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                Log.d("Service Message", "Disconnected");
            }

            public void reconnectCallback(String channel, Object message) {
                Log.d("Service Message", "Reconnected");
            }

            @Override
            public void successCallback(String channel, final Object message) {
                Log.d("Service Message", "Message : "+message.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.d("Service Message", "error"+error.toString());
            }
        });

               } catch (PubnubException e) {
            System.out.println(e.toString());
        }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        pubnub.publish("demo", "Hello World !!" , callback);
    }

    public void unsubscribe() {
        pubnub.unsubscribe(GLOBAL_CHANNEL);
    }

    public void publish(String message) {

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {


            }
            public void errorCallback(String channel, PubnubError error) {

                notifyUser("Something went wrong. Try again.");
            }
        };
        pubnub.publish(GLOBAL_CHANNEL, message , callback);


    }

    JSONObject state = new JSONObject();

    public void presence()
    {
        try {
            state.put("name", "presence-tutorial-user");
            state.put("timestamp", (new Date()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pubnub.setState("demo_tutorial", pubnub.getUUID(), state, new Callback() {});

        try {
            pubnub.subscribe("demo_tutorial", new Callback() {
                public void successCallback(String channel, Object message) {
                    System.out.println(message);
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }

        try {
            pubnub.presence("demo_tutorial", new Callback() {
                public void successCallback(String channel, Object message) {
                    System.out.println(message);
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }

    public void herenow()
    {
        pubnub.hereNow("demo_tutorial", true, true, new Callback() {
            public void successCallback(String channel, Object message) {
                System.out.println(message);
            }
        });
    }

}

