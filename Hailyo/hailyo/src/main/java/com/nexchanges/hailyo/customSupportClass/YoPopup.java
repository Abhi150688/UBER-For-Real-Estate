package com.nexchanges.hailyo.customSupportClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nexchanges.hailyo.MainBrokerActivity;
import com.nexchanges.hailyo.PostYoActivity;
import com.nexchanges.hailyo.PostYoActivity_Broker;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.SharedPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by TP on 01/08/15.
 */
public class YoPopup {
    int timer_int;
    Context myContext;
    String timer_value;
    Boolean min_press = false;
    Button min10,min15,SendYo;
    AlertDialog alertD;
    MainBrokerActivity mainBrokerActivity;


    public void inflateYo(final Context context, final String spec, String U_tp) {
        mainBrokerActivity = new MainBrokerActivity();

        this.myContext = context;


                LayoutInflater layoutInflater = LayoutInflater.from(this.myContext);
                View promptView = layoutInflater.inflate(R.layout.yo_timer, null);

                min10 = (Button) promptView.findViewById(R.id.mins10);
                min15 = (Button) promptView.findViewById(R.id.mins15);

                alertD = new AlertDialog.Builder(myContext).create();

                SendYo = (Button) promptView.findViewById(R.id.sendYo);
                TextView spec1 = (TextView) promptView.findViewById(R.id.tvspec);
                String spec_fin;
                if (U_tp.equalsIgnoreCase("broker"))
                    spec_fin = "Plus-"+spec;
                else
                spec_fin = "Direct-"+spec;
                spec1.setText(spec_fin);


                final TextView timerval = (TextView) promptView.findViewById(R.id.tvtimerValue);

                min10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_press = true;
                        SendYo.setVisibility(View.VISIBLE);
                        timer_value = 10 + "";
                        timer_int = 10;
                        timerval.setText(timer_value + " mins");
                        min10.setTextColor(Color.WHITE);
                        min10.setBackgroundColor(Color.parseColor("#FFA500"));

                        min15.setTextColor(Color.BLACK);
                        min15.setBackgroundResource(R.drawable.button_border);

                    }
                });

                min15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_press=true;
                        SendYo.setVisibility(View.VISIBLE);

                        timer_value = 15 + "";
                        timer_int=15;
                        timerval.setText(timer_value + " mins");
                        min15.setTextColor(Color.WHITE);
                        min15.setBackgroundColor(Color.parseColor("#FFA500"));

                        min10.setTextColor(Color.BLACK);
                        min10.setBackgroundResource(R.drawable.button_border);

                    }
                });


                SendYo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mainBrokerActivity.sendYo();
                        alertD.dismiss();

                    }
                });

                alertD.setView(promptView);
                alertD.show();

            }


  }


