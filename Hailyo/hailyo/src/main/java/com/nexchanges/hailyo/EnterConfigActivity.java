package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;
import com.hrules.horizontalnumberpicker.HorizontalNumberPickerListener;
import com.nexchanges.hailyo.custom.ConfigSpecCode;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.ui.SeekArc;


/**
 * Created by AbhishekWork on 17/06/15.
 */
public class EnterConfigActivity extends Activity implements HorizontalNumberPickerListener{


    private SeekArc mSeekArc_Rent, mSeekArc_Sale;
    private TextView mSeekArcProgress_Rent, mSeekArcProgress_Sale;

    //declare variables
    SeekBar seekbar1, seekbar2;
    int value;
    int bhkval;
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
    String choice="LL";
    Context context;
    int max=5, min=1;
    String fetchloc, msg2,msg3,msg4="Req";
    ConfigSpecCode spec_code;
    private static LayoutInflater inflate =null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_config_layout);
        context = this;
        spec_code = new ConfigSpecCode();





        HorizontalNumberPicker horizontalNumberPicker3 = (HorizontalNumberPicker) findViewById(R.id.horizontal_number_picker3);
        horizontalNumberPicker3.setBackgroundColor(getResources().getColor(android.R.color.white));
        horizontalNumberPicker3.getTextValueView().setTextColor(getResources().getColor(android.R.color.black));
        horizontalNumberPicker3.getTextValueView().setTextSize(22);
        horizontalNumberPicker3.setMaxValue(max);
        horizontalNumberPicker3.setMinValue(min);



        horizontalNumberPicker3.setHorizontalNumberPickerListener(this);



        result = (TextView)this.findViewById(R.id.tvResult);

        configresult = (TextView)this.findViewById(R.id.tvconfigResult);

        pasloc = (TextView)this.findViewById(R.id.tvLoc);

        fetchloc = SharedPrefs.getString(this, SharedPrefs.CURRENT_LOC_KEY, "Location Unavailable");

        pasloc.setText(fetchloc);

        inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflate.inflate(R.layout.left_nav_header,null);

        mSeekArc_Rent = (SeekArc)findViewById(R.id.seekRent);

        mSeekArc_Sale = (SeekArc) findViewById(R.id.seekSale);


       // mSeekArc_Sale = (SeekArc) findViewById(R.id.seekSale);
        mSeekArcProgress_Rent = (TextView) findViewById(R.id.seekArcProgress_Rent);

        mSeekArcProgress_Sale = (TextView) findViewById(R.id.seekArcProgress_Sale);


        mSeekArc_Rent.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mSeekArcProgress_Rent.setText("Rent: \n" + String.valueOf(progress));
            }
        });

        mSeekArc_Sale.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mSeekArcProgress_Sale.setText("Price: \n" + String.valueOf(progress));
            }
        });





        seeProp = (Button) findViewById(R.id.seeprop);
        showProp = (Button) findViewById(R.id.showprop);



        seeProp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                msg4="Req";

                seeProp.setBackgroundColor(Color.parseColor("#FFA500"));
                seeProp.setTextColor(Color.WHITE);
                if (isSecondPlace) {
                    showProp.setBackgroundResource(R.drawable.button_border);
                    showProp.setTextColor(Color.BLACK);

                    isSecondPlace = false;

                }

            }
        });
        showProp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showProp.setBackgroundColor(Color.parseColor("#FFA500"));
                showProp.setTextColor(Color.WHITE);
                msg4 = "Avl";
                isSecondPlace = true;
                if (isOnePressed) {
                    seeProp.setBackgroundResource(R.drawable.button_border);
                    seeProp.setTextColor(Color.BLACK);
                    isOnePressed = false;
                }

            }
        });



        message = msg4 + "-" + choice + "-" + msg3 + "-" + msg2;

        hailBtn = (Button) findViewById(R.id.hail);


        hailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                SharedPrefs.save(context, SharedPrefs.CURRENT_SPEC, message);



                Intent SearchSplashScreen =new Intent(context, PostYoActivity.class);

                //Intent SearchSplashScreen =new Intent(context, splash_mock.class);

                Intent PostYoAct = new Intent(context, PostYoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("Phone", "9967307197");
                extras.putString("Broker_Name", "Rajiv Lakhpati");
                extras.putString("Timer", "2");
                extras.putString("Rating", "3");
                PostYoAct.putExtras(extras);
                context.startActivity(PostYoAct);
                finish();

            }
        });
    }




    @Override
    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int i) {
       bhkval = i;
       configresult.setText(":" + bhkval + "BHK");
       msg3 = i+"BHK";
       spec_code.BHK = i+"BHK";

    }
}




