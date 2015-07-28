package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;
import com.hrules.horizontalnumberpicker.HorizontalNumberPickerListener;
import com.nexchanges.hailyo.customSupportClass.ConfigSpecCode;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.customWidget.SeekArc;


/**
 * Created by AbhishekWork on 17/06/15.
 */
public class EnterConfigActivity extends Activity implements HorizontalNumberPickerListener, SeekBar.OnSeekBarChangeListener{


    private SeekArc mSeekArc_Rent, mSeekArc_Sale;
    private TextView mSeekArcProgress_Rent, mSeekArcProgress_Sale;

    //declare variables
    SeekBar mSeekbar_Rent, mSeekbar_Sale;
    int bhkval, value;
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
    private String message, role;
    Button seeProp,showProp, rent, buy;
    Boolean isOnePressed = true, isSecondPlace = false, rentPressed = true, salePressed = false;
    String choice="LL";
    Context context;
    int max=5, min=1;
    String fetchloc, msg1="LL",msg2,msg3,msg4="Req";
    ConfigSpecCode spec_code;
    private static LayoutInflater inflate =null;
    TableLayout renttab, saletab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_config_layout);
        context = this;
        spec_code = new ConfigSpecCode();

        role = SharedPrefs.getString(context,SharedPrefs.MY_ROLE_KEY);





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

       mSeekbar_Rent = (SeekBar)findViewById(R.id.rentbar);

        mSeekbar_Sale = (SeekBar)findViewById(R.id.salebar);




        seeProp = (Button) findViewById(R.id.seeprop);
        showProp = (Button) findViewById(R.id.showprop);

        rent = (Button) findViewById(R.id.rent);
        buy = (Button) findViewById(R.id.sell);


        renttab = (TableLayout) findViewById(R.id.rentpricetext);

        saletab = (TableLayout) findViewById(R.id.salepricetext);




        seeProp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                msg4="Req";
                SharedPrefs.save(context,SharedPrefs.CURRENT_CUST_TYPE,"req");

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
                SharedPrefs.save(context,SharedPrefs.CURRENT_CUST_TYPE,"avl");

                isSecondPlace = true;
                if (isOnePressed) {
                    seeProp.setBackgroundResource(R.drawable.button_border);
                    seeProp.setTextColor(Color.BLACK);
                    isOnePressed = false;
                }

            }
        });



        rent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rentPressed = true;
                msg1="L/L";
                SharedPrefs.save(context,SharedPrefs.CURRENT_INTENT,"rent");


                rent.setBackgroundColor(Color.parseColor("#FFA500"));
                rent.setTextColor(Color.WHITE);
                if (salePressed) {
                    buy.setBackgroundResource(R.drawable.button_border);
                    buy.setTextColor(Color.BLACK);

                    salePressed = false;

                }

            }
        });


        buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                salePressed = true;
                msg1 = "OUT";
                SharedPrefs.save(context,SharedPrefs.CURRENT_INTENT,"out");

                buy.setBackgroundColor(Color.parseColor("#FFA500"));
                buy.setTextColor(Color.WHITE);
                if (rentPressed) {
                    rent.setBackgroundResource(R.drawable.button_border);
                    rent.setTextColor(Color.BLACK);

                    rentPressed = false;

                }

            }
        });


        if (role.equalsIgnoreCase("broker"))
        {
        message = "Plus-" + msg4 + "-" + msg1 + "-" + msg3 + "-" + msg2;}


        else if (role.equalsIgnoreCase("client"))
        {
            message = "Direct-" + msg4 + "-" + msg1 + "-" + msg3 + "-" + msg2;}

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

        switch (seekBar.getId()) {

            case R.id.rentbar:
                mSeekArc_Sale.setProgress(0);
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

            case R.id.salebar:
                mSeekArc_Rent.setProgress(0);
                if (progress < 10000000) {
                    progress = ((int) Math.round(progress / step_Sale1)) * step_Sale1;
                    value = progress;
                    result.setText(" Price:" + value);
                } if (progress > 10000000 && progress < 100000000) {
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

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}




