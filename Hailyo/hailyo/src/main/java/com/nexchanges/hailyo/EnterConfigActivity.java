package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Intent;
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
import com.nexchanges.hailyo.ui.GetPlaceName;


/**
 * Created by AbhishekWork on 17/06/15.
 */
public class EnterConfigActivity extends Activity implements OnSeekBarChangeListener, HorizontalNumberPickerListener{

    //declare variables
    SeekBar seekbar1;
    int value;
    int bhkval;
    int endValue;
    TextView result;
    TextView configresult;

    TextView pasloc;
    final int step_size1 = 5000;
    final int step_size2 = 50000;
    final int step_size3 = 100000;
    Button hailBtn;
    private String message;

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


        result = (TextView)this.findViewById(R.id.tvResult);

        configresult = (TextView)this.findViewById(R.id.tvconfigResult);

        pasloc = (TextView)this.findViewById(R.id.tvLoc);


        //set change listener
        seekbar1.setOnSeekBarChangeListener(this);
        Intent locationIntent= getIntent();
        Bundle b = locationIntent.getExtras();

        if(b!=null)
        {
            LatLng j =(LatLng) b.get("selectedLocation");

            getPlaceName(j);
            //pasloc.setText(j);
        }



        hailBtn = (Button) findViewById(R.id.hail);


        hailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String msg1 = pasloc.getText().toString();
                String msg2 = result.getText().toString();
                String msg3 = configresult.getText().toString();

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
        if(progress <300000)
        {
        progress = ((int)Math.round(progress/step_size1))*step_size1;
        value = progress;
        result.setText (":"+value);}
        else if (progress > 305000 && progress < 600000)
        {
        progress = ((int)Math.round(progress/step_size2))*step_size2;
        value = progress;
        result.setText (":"+value);}
        else
        progress = ((int)Math.round(progress/step_size3))*step_size3;
        value = progress;
        result.setText (":"+value);



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




