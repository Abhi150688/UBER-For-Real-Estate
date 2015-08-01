package com.nexchanges.hailyo.customSupportClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexchanges.hailyo.MainBrokerActivity;
import com.nexchanges.hailyo.PostYoActivity_Broker;
import com.nexchanges.hailyo.R;

/**
 * Created by TP on 01/08/15.
 */
public class YoPopup {
    String timer_value;
    int timer_int;
    Context myContext;

    public void inflateYo(final Context context, final String spec) {


                this.myContext = context;
                LayoutInflater layoutInflater = LayoutInflater.from(this.myContext);
                View promptView = layoutInflater.inflate(R.layout.yo_timer, null);

                final Button min10 = (Button) promptView.findViewById(R.id.mins10);
                final Button min15 = (Button) promptView.findViewById(R.id.mins15);

                final AlertDialog alertD = new AlertDialog.Builder(myContext).create();

                ImageView SendYo = (ImageView) promptView.findViewById(R.id.sendYo);
                TextView spec1 = (TextView) promptView.findViewById(R.id.tvspec);

                spec1.setText(spec);

                final TextView timerval = (TextView) promptView.findViewById(R.id.tvtimerValue);

                min10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        String rate = "3.5";
                        Intent AfterYoBroker = new Intent(myContext, PostYoActivity_Broker.class);
                        Bundle extras = new Bundle();
                        extras.putInt("timer", timer_int);
                        extras.putString("spec", spec);
                        extras.putString("rating", rate);
                        extras.putString("phone", "9967307197");
                        extras.putString("broker_Name", "Abhishek");
                        AfterYoBroker.putExtras(extras);
                        myContext.startActivity(AfterYoBroker);
                        alertD.dismiss();
                        ((Activity) myContext).finish();

                    }
                });

                alertD.setView(promptView);
                alertD.show();

            }


  }


