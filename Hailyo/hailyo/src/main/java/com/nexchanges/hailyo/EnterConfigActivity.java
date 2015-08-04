package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;
import com.hrules.horizontalnumberpicker.HorizontalNumberPickerListener;
import com.nexchanges.hailyo.customSupportClass.ConfigSpecCode;
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
 * Created by AbhishekWork on 17/06/15.
 */
public class EnterConfigActivity extends Activity implements HorizontalNumberPickerListener, SeekBar.OnSeekBarChangeListener{


    //declare variables
    SeekBar mSeekbar_Rent, mSeekbar_Sale;
    Boolean success;
    String URL = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000/1/lets/hail";

    int bhkval, value;
    Dialog alertD;
    StringEntity se;
    public static final String TAG = EnterConfigActivity.class.getSimpleName();

    TextView result;
    TextView pasloc;
    final int step_size1 = 5000;
    final int step_size2 = 50000;
    final int step_size3 = 100000;

    final int step_Sale1 = 2500000;
    final int step_Sale2 = 5000000;
    final int step_Sale3 = 10000000;


    Button hailBtn;
    private String message, role, user_id, lng,lat;
    Button seeProp,showProp;
    Boolean isOnePressed = true, isSecondPlace = false, rentSelected = true, saleSelected = false;
    Context context;
    int max=5, min=0,val_check;
    String fetchloc, msg1,msg2,msg3,msg4,message1,str1="seeshow",str2="buyrent",str3="bhk",str4="price",mesFinal;
    ConfigSpecCode spec_code;
    private static LayoutInflater inflate =null;
    TableLayout renttab, saletab;
    Boolean is_see_show_pressed = false, is_buy_sell_pressed = false;

    HorizontalNumberPicker horizontalNumberPicker3;
    CheckBox rent,sale;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_config_layout);
        context = this;
        spec_code = new ConfigSpecCode();

        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);
        user_id = SharedPrefs.getString(context, SharedPrefs.MY_USER_ID);


        lng = SharedPrefs.getString(context, SharedPrefs.MY_POINTER_LNG);
        lat = SharedPrefs.getString(context, SharedPrefs.MY_POINTER_LAT);


        horizontalNumberPicker3 = (HorizontalNumberPicker) findViewById(R.id.horizontal_number_picker3);
        horizontalNumberPicker3.setBackgroundColor(getResources().getColor(android.R.color.white));
        horizontalNumberPicker3.getTextValueView().setTextColor(getResources().getColor(android.R.color.black));
        horizontalNumberPicker3.getTextValueView().setTextSize(22);
        horizontalNumberPicker3.setMaxValue(max);
        horizontalNumberPicker3.setMinValue(min);


        horizontalNumberPicker3.setHorizontalNumberPickerListener(this);



        result = (TextView) this.findViewById(R.id.tvResult);

        rent =   (CheckBox) this.findViewById(R.id.checkRENT);

        sale=   (CheckBox) this.findViewById(R.id.checkSALE);

        pasloc = (TextView) this.findViewById(R.id.tvLoc);

        fetchloc = SharedPrefs.getString(this, SharedPrefs.CURRENT_LOC_KEY, "Location Unavailable");

        pasloc.setText(fetchloc);

        mSeekbar_Rent = (SeekBar) findViewById(R.id.rentbar);
        mSeekbar_Rent.setOnSeekBarChangeListener(this);

        mSeekbar_Sale = (SeekBar) findViewById(R.id.salebar);
        mSeekbar_Sale.setOnSeekBarChangeListener(this);

        seeProp = (Button) findViewById(R.id.seeprop);
        showProp = (Button) findViewById(R.id.showprop);

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
                is_see_show_pressed = true;
                showProp.setTextColor(Color.WHITE);
                msg1 = "Avl";
                updateSpecCode(str1, msg1);
                SharedPrefs.save(context, SharedPrefs.CURRENT_CUST_TYPE, "avl");

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
                rentSelected = true;
                sale.setChecked(false);
                saleSelected = false;
                msg2 = "L/L";
                updateSpecCode(str2, msg2);
                result.setText("");
                mSeekbar_Sale.setProgress(0);

                SharedPrefs.save(context, SharedPrefs.CURRENT_INTENT, "rent");
                mSeekbar_Rent.setVisibility(View.VISIBLE);
                renttab.setVisibility(View.VISIBLE);

                mSeekbar_Sale.setVisibility(View.GONE);
                saletab.setVisibility(View.GONE);

            }
        });

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saleSelected = true;
                rent.setChecked(false);
                rentSelected = false;
                msg2 = "OUT";
                updateSpecCode(str2, msg2);
                result.setText("");
                mSeekbar_Rent.setProgress(0);

                SharedPrefs.save(context, SharedPrefs.CURRENT_INTENT, "out");
                mSeekbar_Rent.setVisibility(View.GONE);
                renttab.setVisibility(View.GONE);

                mSeekbar_Sale.setVisibility(View.VISIBLE);
                saletab.setVisibility(View.VISIBLE);

            }
        });



        hailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {



                mesFinal = updateSpecCode("he", "he");
                int pass = validationCheck();

                if (pass == 0) {
                    sendPostRequest(user_id, role, lng, lat, mesFinal);
                  //  successfulHail();

                    }

                else Toast.makeText(getApplicationContext(), "Please enter all fields before clicking Hail",
                        Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int i) {
        bhkval = i;
        msg3 = (horizontalNumberPicker.getValue()+"BHK");
        updateSpecCode(str3,msg3);
        spec_code.BHK = i+"BHK";

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        int x,y;
        double y1;
        int minxr = 0, maxxr=1000000;
        int minxs=0,maxxs=150000000;


        switch (seekBar.getId()) {

            case R.id.rentbar:
                mSeekbar_Sale.setProgress(0);

                 if (progress < 300000) {
                   // x=progress;
                   // y1=Math.exp(Math.log(minxr)+(x-minxr)*(Math.log(maxxr) - Math.log(minxr))/(maxxr-minxr));
                    // y=(int)y1;

                    progress = ((int) Math.round(progress / step_size1)) * step_size1;
                    value = progress;
                     msg4 = Integer.toString(value);
                     updateSpecCode(str4, msg4);

                     result.setText(" Rent:" + value);
                } else if (progress > 305000 && progress < 600000) {

                     progress = ((int) Math.round(progress / step_size2)) * step_size2;
                    value = progress;
                     msg4 = Integer.toString(value);
                     updateSpecCode(str4, msg4);

                     result.setText(" Rent:" + value);

                } else{
                     progress = ((int) Math.round(progress / step_size3)) * step_size3;
                value = progress;
                msg4 = Integer.toString(value);
                updateSpecCode(str4, msg4);

                result.setText(" Rent:" + value);
                 }
                break;

            case R.id.salebar:
                mSeekbar_Rent.setProgress(0);
                result.setText("");
                if (progress < 10000000) {
                    progress = ((int) Math.round(progress / step_Sale1)) * step_Sale1;
                    value = progress;
                    msg4 = Integer.toString(value);
                    updateSpecCode(str4,msg4);

                    result.setText(" Price:" + value);
                 } if (progress > 10000000 && progress < 100000000) {
                progress = ((int) Math.round(progress / step_Sale2)) * step_Sale2;
                value = progress;
                msg4 = Integer.toString(value);
                updateSpecCode(str4,msg4);

                result.setText(" Price:" + value);
                 } else
                progress = ((int) Math.round(progress / step_Sale3)) * step_Sale3;
                value = progress;
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

        if (rentSelected == true) {
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
        Log.i(TAG, "VALUE OF MSG3 IS: " + msg3);
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

    private void sendPostRequest(final String U_id, final String U_role, final String U_Lat, final String U_Lng, final String spec)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                showSplashScreen();
            }


            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("user_id", U_id);
                    jsonObject.accumulate("user_role", U_role);
                    jsonObject.accumulate("spec_code", spec);
                    jsonObject.accumulate("long", U_Lng);
                    jsonObject.accumulate("lat", U_Lat);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(URL);


                try {
                    se = new StringEntity(jsonObject.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                se.setContentType(new BasicHeader("Content-Type", "application/json"));

                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");

                try {
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    int response = httpResponse.getStatusLine().getStatusCode();
                    System.out.print("Value of response code is: " + response);

                    if (response == 200 || response == 201) {
                        success = true;
                    } else {
                        System.out.print("Hail Failed, Try again later");
                        success = false;
                    }

                    InputStream inputStream = httpResponse.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("First Exception coz of HttpResponese :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Second Exception coz of HttpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                removeSplashScreen();
                if (success == false) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Hail Timed Out, No Service Provder responded \n Please try again!",
                            Toast.LENGTH_LONG).show();
                } else
                {
                    if (result != null) {

                    try {
                        JSONObject jObject = new JSONObject(result);
                        String tim_val = jObject.getString("time_to_meet");
                        String yoed = jObject.getString("yo_resp");
                        String bname = jObject.getString("name");
                        String mobile = jObject.getString("mobile_no");

                        JSONArray rating = jObject.getJSONArray("rating");
                        String br_wow = rating.getString(0);
                        String br_not_wow = rating.getString(1);

                        String cl_wow = rating.getString(2);
                        String cl_not_wow = rating.getString(3);


                        if (yoed.equalsIgnoreCase("true"))
                            successfulHail(mobile, bname, tim_val, br_wow, br_not_wow);
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No Response for given Hail. \n Try changing requirement specs and Hailing again!",
                                    Toast.LENGTH_LONG).show();


                        }
                    } catch (JSONException e) {
                        Log.e("JSONException", "Error: " + e.toString());
                    }

                }
            }
            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(U_id, U_role, U_Lat, U_Lng, spec);
    }


    protected void showSplashScreen() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View View = layoutInflater.inflate(R.layout.splashscreen, null);

        alertD = new Dialog(context);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertD.setContentView(View);
        alertD.setCancelable(false);
        alertD.show();
        alertD.getWindow().setLayout(500, 600);

    }

    protected void removeSplashScreen() {
        if (alertD != null) {
            alertD.dismiss();
            alertD = null;
        }
    }

private void successfulHail(String mob, String nam, String valT,String wow, String not_wow)
{

    SharedPrefs.save(context, SharedPrefs.CURRENT_SPEC, message);

    if (role.equalsIgnoreCase("broker")) {
        message = "Plus-" + mesFinal;
        Intent PostYoActB = new Intent(context, PostYoActivity_Broker.class);
        Bundle extrasB = new Bundle();
        extrasB.putString("phone", mob);
        extrasB.putString("broker_Name", nam);
        extrasB.putString("timer", valT);
        extrasB.putString("rating_wow",wow);
        extrasB.putString("rating_not_wow",not_wow);

        PostYoActB.putExtras(extrasB);
        context.startActivity(PostYoActB);
        finish();


    }
    else {
        message = "Direct-" + mesFinal;
        Intent PostYoAct = new Intent(context, PostYoActivity.class);
        Bundle extrasB = new Bundle();
        extrasB.putString("phone", mob);
        extrasB.putString("broker_Name", nam);
        extrasB.putString("timer", valT);
        extrasB.putString("rating_wow",wow);
        extrasB.putString("rating_not_wow",not_wow);

        PostYoAct.putExtras(extrasB);
        context.startActivity(PostYoAct);
        finish();
    }
}
}