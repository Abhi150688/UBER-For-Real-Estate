package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;
import com.hrules.horizontalnumberpicker.HorizontalNumberPickerListener;
import com.nexchanges.hailyo.custom.Words;
import com.nexchanges.hailyo.ui.GetPlaceName;


/**
 * Created by AbhishekWork on 17/06/15.
 */
public class EnterConfigActivity extends Activity implements OnSeekBarChangeListener, HorizontalNumberPickerListener{

    //declare variables
    SeekBar seekbar1, seekbar2;
    int value;
    int bhkval;
    int endValue;
    TextView result;
    TextView configresult;

    TextView pasloc;
    final int step_size1 = 5000;
    final int step_size2 = 50000;
    final int step_size3 = 100000;

    final int step_Sale1 = 2500000;
    final int step_Sale2 = 5000000;
    final int step_Sale3 = 10000000;


    Button hailBtn;
    private String message;
    Button seeProp;
    Button showProp;
    Boolean isOnePressed = true, isSecondPlace = false;
    LatLng j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_config_layout);

        HorizontalNumberPicker horizontalNumberPicker3 = (HorizontalNumberPicker) findViewById(R.id.horizontal_number_picker3);
        horizontalNumberPicker3.setBackgroundColor(getResources().getColor(android.R.color.white));
        horizontalNumberPicker3.getTextValueView().setTextColor(getResources().getColor(android.R.color.black));
        horizontalNumberPicker3.getTextValueView().setTextSize(22);

        horizontalNumberPicker3.setHorizontalNumberPickerListener(this);



        seekbar1 = (SeekBar)this.findViewById(R.id.sbBar);

        seekbar2 = (SeekBar)this.findViewById(R.id.sbSBar);


        result = (TextView)this.findViewById(R.id.tvResult);

        configresult = (TextView)this.findViewById(R.id.tvconfigResult);

        pasloc = (TextView)this.findViewById(R.id.tvLoc);


        //set change listener
        seekbar1.setOnSeekBarChangeListener(this);
        seekbar2.setOnSeekBarChangeListener(this);

        Intent locationIntent= getIntent();
        Bundle b = locationIntent.getExtras();

        if(b!=null)
        {
            j =(LatLng) b.get("selectedLocation");

            getPlaceName(j);
            //pasloc.setText(j);
        }


        seeProp = (Button) findViewById(R.id.seeprop);
        showProp = (Button) findViewById(R.id.showprop);



        seeProp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                seeProp.setBackgroundColor(Color.BLACK);
                seeProp.setTextColor(Color.WHITE);
                if (isSecondPlace) {
                    showProp.setBackgroundColor(Color.LTGRAY);
                    showProp.setTextColor(Color.BLACK);

                    isSecondPlace = false;
                }

            }
        });
        showProp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showProp.setBackgroundColor(Color.BLACK);
                showProp.setTextColor(Color.WHITE);

                isSecondPlace = true;
                if (isOnePressed) {
                    seeProp.setBackgroundColor(Color.LTGRAY);
                    seeProp.setTextColor(Color.BLACK);
                    isOnePressed = false;
                }

            }
        });



        hailBtn = (Button) findViewById(R.id.hail);


        hailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String msg1 = pasloc.getText().toString();
                String msg2 = result.getText().toString();
                String msg3 = configresult.getText().toString();
               // String msg4 = j.toString();

                message = msg1 + " " + msg2 + " " + msg3;

                System.out.println("Message is:" + message);
            }
        });
    }





    public void getPlaceName(LatLng location){
        new GetPlaceName(location, new GetPlaceName.GetPlaceNameCallback() {
            @Override
            public void onStart() {
                pasloc.setText("Fetching Site Visit Location, wait..");
            }

            @Override
            public void onComplete(boolean result, LatLng location, String placeName) {
                if ( result == true ) {
                    pasloc.setText(placeName);
                }else{
                    pasloc.setText("Sorry, No Such Location, Please Try Again..");
                }
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

        switch (seekBar.getId()) {

            case R.id.sbBar:
                seekbar2.setProgress(0);
            if (progress < 300000) {
                progress = ((int) Math.round(progress / step_size1)) * step_size1;
                value = progress;
                result.setText(" Rent:" + value);
            } else if (progress > 305000 && progress < 600000) {
                progress = ((int) Math.round(progress / step_size2)) * step_size2;
                value = progress;
                result.setText(" Rent:" + value);
            } else
                progress = ((int) Math.round(progress / step_size3)) * step_size3;
            value = progress;
            result.setText(" Rent:" + value);
                break;

            case R.id.sbSBar:
                seekbar1.setProgress(0);
                if (progress < 10000000) {
                    progress = ((int) Math.round(progress / step_Sale1)) * step_Sale1;
                    value = progress;
                    result.setText(" Price:" + value);
                } else if (progress > 10000000 && progress < 100000000) {
                    progress = ((int) Math.round(progress / step_Sale2)) * step_Sale2;
                    value = progress;
                    result.setText(" Price:" + value);
                } else
                    progress = ((int) Math.round(progress / step_Sale3)) * step_Sale3;
                value = progress;
                result.setText(" Price:" + value);
                break;
        }


}
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int i) {
       bhkval = i;
       configresult.setText(":" + bhkval + "BHK");

    }
}




