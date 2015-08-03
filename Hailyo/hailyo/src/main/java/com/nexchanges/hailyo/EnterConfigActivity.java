package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final String TAG = EnterConfigActivity.class.getSimpleName();

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
    int max=5, min=0,val_check;
    String fetchloc, msg1,msg2,msg3,msg4,message1,str1="seeshow",str2="buyrent",str3="bhk",str4="price";
    ConfigSpecCode spec_code;
    private static LayoutInflater inflate =null;
    TableLayout renttab, saletab;
    Boolean is_see_show_pressed = false, is_buy_sell_pressed = false;

    HorizontalNumberPicker horizontalNumberPicker3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_config_layout);
        context = this;
        spec_code = new ConfigSpecCode();

        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);


        horizontalNumberPicker3 = (HorizontalNumberPicker) findViewById(R.id.horizontal_number_picker3);
        horizontalNumberPicker3.setBackgroundColor(getResources().getColor(android.R.color.white));
        horizontalNumberPicker3.getTextValueView().setTextColor(getResources().getColor(android.R.color.black));
        horizontalNumberPicker3.getTextValueView().setTextSize(22);
        horizontalNumberPicker3.setMaxValue(max);
        horizontalNumberPicker3.setMinValue(min);


        horizontalNumberPicker3.setHorizontalNumberPickerListener(this);


        result = (TextView) this.findViewById(R.id.tvResult);

        configresult = (TextView) this.findViewById(R.id.tvconfigResult);
        configresult.setText("1BHK");

        pasloc = (TextView) this.findViewById(R.id.tvLoc);

        fetchloc = SharedPrefs.getString(this, SharedPrefs.CURRENT_LOC_KEY, "Location Unavailable");

        pasloc.setText(fetchloc);

        inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflate.inflate(R.layout.left_nav_header, null);

        mSeekbar_Rent = (SeekBar) findViewById(R.id.rentbar);
        mSeekbar_Rent.setOnSeekBarChangeListener(this);

        mSeekbar_Sale = (SeekBar) findViewById(R.id.salebar);
        mSeekbar_Sale.setOnSeekBarChangeListener(this);

        seeProp = (Button) findViewById(R.id.seeprop);
        showProp = (Button) findViewById(R.id.showprop);

       // rent = (Button) findViewById(R.id.rent);
      //  buy = (Button) findViewById(R.id.sell);


        renttab = (TableLayout) findViewById(R.id.rentpricetext);

        saletab = (TableLayout) findViewById(R.id.salepricetext);
        hailBtn = (Button) findViewById(R.id.hail);


        seeProp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                is_see_show_pressed = true;
                msg1 = "Req";
                updateSpecCode(str1,msg1);
                SharedPrefs.save(context, SharedPrefs.CURRENT_CUST_TYPE, "req");

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
                is_see_show_pressed=true;
                showProp.setTextColor(Color.WHITE);
                msg1 = "Avl";
                updateSpecCode(str1,msg1);
                SharedPrefs.save(context, SharedPrefs.CURRENT_CUST_TYPE, "avl");

                isSecondPlace = true;
                if (isOnePressed) {
                    seeProp.setBackgroundResource(R.drawable.button_border);
                    seeProp.setTextColor(Color.BLACK);
                    isOnePressed = false;
                }

            }
        });


        /*rent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rentPressed = true;
                is_buy_sell_pressed = true;
                msg2 = "L/L";
                updateSpecCode(str2,msg2);
                result.setText("");

                mSeekbar_Sale.setProgress(0);

                SharedPrefs.save(context, SharedPrefs.CURRENT_INTENT, "rent");
                mSeekbar_Rent.setVisibility(View.VISIBLE);
                renttab.setVisibility(View.VISIBLE);

                mSeekbar_Sale.setVisibility(View.GONE);
                saletab.setVisibility(View.GONE);

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
                is_buy_sell_pressed = true;
                msg2 = "OUT";
                updateSpecCode(str2,msg2);
                result.setText("");
                mSeekbar_Rent.setProgress(0);

                SharedPrefs.save(context, SharedPrefs.CURRENT_INTENT, "out");
                mSeekbar_Sale.setVisibility(View.VISIBLE);
                saletab.setVisibility(View.VISIBLE);

                mSeekbar_Rent.setVisibility(View.GONE);
                renttab.setVisibility(View.GONE);


                buy.setBackgroundColor(Color.parseColor("#FFA500"));
                buy.setTextColor(Color.WHITE);
                if (rentPressed) {
                    rent.setBackgroundResource(R.drawable.button_border);
                    rent.setTextColor(Color.BLACK);

                    rentPressed = false;

                }

            }
        });*/


        hailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                String mesFinal = updateSpecCode("he", "he");
                int pass = validationCheck();

                if (pass == 0)

                {
                    if (role.equalsIgnoreCase("broker")) {
                        message = "Plus-" + mesFinal;
                        Intent PostYoActBroker = new Intent(context, PostYoActivity_Broker.class);
                        Bundle extrasB = new Bundle();
                        extrasB.putString("phone", "9967307197");
                        extrasB.putString("broker_Name", "Rajiv Lakhpati");
                        extrasB.putString("timer", "1");
                        extrasB.putString("rating", "3");

                        PostYoActBroker.putExtras(extrasB);
                        context.startActivity(PostYoActBroker);
                        finish();

                    } else if (role.equalsIgnoreCase("client")) {
                        message = "Direct-" + mesFinal;
                        Intent PostYoAct = new Intent(context, PostYoActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("phone", "9967307197");
                        extras.putString("broker_Name", "Rajiv Lakhpati");
                        extras.putString("timer", "1");
                        extras.putString("rating", "3");

                        PostYoAct.putExtras(extras);
                        context.startActivity(PostYoAct);
                        finish();

                    }
                SharedPrefs.save(context, SharedPrefs.CURRENT_SPEC, message);
            }//

                else Toast.makeText(getApplicationContext(), "Please enter all fields before clicking Hail",
                        Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int i) {
        bhkval = i;
        configresult.setText(":" + bhkval + "BHK");
        msg3 = (horizontalNumberPicker.getValue()+"BHK");
        updateSpecCode(str3,msg3);
        spec_code.BHK = i+"BHK";

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

        switch (seekBar.getId()) {

            case R.id.rentbar:
                mSeekbar_Sale.setProgress(0);
                 if (progress < 300000) {
                    progress = ((int) Math.round(progress / step_size1)) * step_size1;
                    value = progress;
                  //   int prog = mSeekbar_Rent.getProgress();
                     msg4 = Integer.toString(value);
                     updateSpecCode(str4,msg4);

                     result.setText(" Rent:" + value);
                } else if (progress > 305000 && progress < 600000) {
                    progress = ((int) Math.round(progress / step_size2)) * step_size2;
                    value = progress;
                    // int prog = mSeekbar_Rent.getProgress();
                     msg4 = Integer.toString(value);
                     updateSpecCode(str4,msg4);


                     result.setText(" Rent:" + value);

                } else
                    progress = ((int) Math.round(progress / step_size3)) * step_size3;
                value = progress;
               // int prog = mSeekbar_Rent.getProgress();
                msg4 = Integer.toString(value);
                updateSpecCode(str4,msg4);

                result.setText(" Rent:" + value);
                break;

            case R.id.salebar:
                mSeekbar_Rent.setProgress(0);
                result.setText("");
                if (progress < 10000000) {
                    progress = ((int) Math.round(progress / step_Sale1)) * step_Sale1;
                    value = progress;
                    //int prog1 = mSeekbar_Sale.getProgress();
                    msg4 = Integer.toString(value);
                    updateSpecCode(str4,msg4);

                    result.setText(" Price:" + value);
                 } if (progress > 10000000 && progress < 100000000) {
                progress = ((int) Math.round(progress / step_Sale2)) * step_Sale2;
                value = progress;
                //int prog1 = mSeekbar_Sale.getProgress();
                msg4 = Integer.toString(value);
                updateSpecCode(str4,msg4);

                result.setText(" Price:" + value);
                 } else
                progress = ((int) Math.round(progress / step_Sale3)) * step_Sale3;
                value = progress;
               // int prog1 = mSeekbar_Sale.getProgress();
                msg4 = Integer.toString(value);
                updateSpecCode(str4,msg4);

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

    private String updateSpecCode(String where, String value)

    {
        message1 = msg1 + "-" + msg2 + "-" + msg3 + "-" + msg4;

        if (rentPressed == true) {
            int prog = mSeekbar_Rent.getProgress();
            msg4 = Integer.toString(prog);
        } else{
            int prog1 = mSeekbar_Sale.getProgress();
            msg4 = Integer.toString(prog1);
        }

        if (where.equalsIgnoreCase("seeshow")) {
            msg1 = value;
            message1 = msg1 + "-" + msg2 + "-" + msg3 + "-" + msg4;
        }
        else if (where.equalsIgnoreCase("buyrent"))
        {
            msg2 = value;
            message1 = msg1 + "-" + msg2 + "-" + msg3 + "-" + msg4;

        }
        else  if (where.equalsIgnoreCase("bhk"))
        {
            msg3 = value;
            message1 = msg1 + "-" + msg2 + "-" + msg3 + "-" + msg4;

        }

        else if (where.equalsIgnoreCase("price"))
        {
            msg4 = value;
            message1 = msg1 + "-" + msg2 + "-" + msg3 + "-" + msg4;

        }

        else message1 = msg1 + "-" + msg2 + "-" + msg3 + "-" + msg4;


        Log.i(TAG,"VALUE OF MSG1 IS: " + msg1);
        Log.i(TAG,"VALUE OF MSG2 IS: " + msg2);
        Log.i(TAG,"VALUE OF MSG3 IS: " + msg3);
        Log.i(TAG,"VALUE OF MSG4 IS: " + msg4);

        return message1;
    }
    @Override
    protected void onPause()
    {
        super.onPause();
       // SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());
    }

    private int validationCheck() {


         val_check = 0;

        if (is_see_show_pressed == false) {
            Toast.makeText(getApplicationContext(), "Chose your profile \n Owner or Seeker?",
                    Toast.LENGTH_LONG).show();
            val_check = 1;
        }
        if (is_buy_sell_pressed == false) {
            Toast.makeText(getApplicationContext(), "Please select your intent \n Do you want to Buy/Sell or Rent?",
                    Toast.LENGTH_LONG).show();
            val_check = 1;
        }

        if (horizontalNumberPicker3.getValue() == 0) {
            Toast.makeText(getApplicationContext(), "Please select a valid configuration BHK",
                    Toast.LENGTH_LONG).show();
            val_check = 1;
        }

        if (mSeekbar_Rent.getProgress() == 0 && mSeekbar_Sale.getProgress() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a valid price",
                    Toast.LENGTH_LONG).show();
            val_check = 1;
        }

        return val_check;
    }


}




