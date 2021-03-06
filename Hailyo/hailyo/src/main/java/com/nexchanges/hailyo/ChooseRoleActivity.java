package com.nexchanges.hailyo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationResult;
import com.nexchanges.hailyo.apiSupport.SendLocationUpdate;
import com.nexchanges.hailyo.gcm.LocationServices;
import com.nexchanges.hailyo.model.SharedPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by AbhishekWork on 22/06/15.
 */
public class ChooseRoleActivity extends Activity {
    final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //declare variables
    private String Semail, Sname, user_role, server_pic,Sphoto, C = "Customer", B = "Broker", my_user_id, my_gcm_id, num;
    private static final String TAG = ChooseRoleActivity.class.getSimpleName();
    String URL = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000/1/user/signup";
    StringEntity se;
    CheckBox clientBut, brokerBut;
    Dialog alertD;
    Context context;
    EditText name, email;
    ImageView myphoto;
    String picturePath, mobile;
    private static final int SELECT_PHOTO = 1;
    GoogleCloudMessaging gcm;
    Button submitBut;
    String regid, GCMID;
    String PROJECT_NUMBER = "250185285941";
    TextView edit, fbdata;
    Boolean success = false, is_role_selected=false, validation_success, email_success;
    String subphone=null;
    LocationManager mLocationManager;
    SendLocationUpdate sendLocationUpdate;
    LocationResult locationResult;
    String Str_Lng="",Str_Lat="",shortPhone,pnum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);
        Boolean service = checkGPlayService();
        if (service == false) {
            Toast.makeText(
                    getApplicationContext(),
                    "Google Play Services are not working, Please update and try again",
                    Toast.LENGTH_LONG).show();

        }

        registerGCM();


        context = this;
        shortPhone = SharedPrefs.getString(context, SharedPrefs.MY_SHORTMOBILE_KEY);
        name = (EditText) findViewById(R.id.etname);
        email = (EditText) findViewById(R.id.etemail);

        clientBut = (CheckBox) findViewById(R.id.iamclient);
        brokerBut = (CheckBox) findViewById(R.id.iambroker);

        submitBut = (Button) findViewById(R.id.submitprofile);

        myphoto = (ImageView) findViewById(R.id.myphoto);

        edit = (TextView) findViewById(R.id.edit);

        mobile = SharedPrefs.getString(context, SharedPrefs.MY_MOBILE_KEY);

        if (shortPhone.isEmpty())
        {

        subphone = mobile.substring(3);
        SharedPrefs.save(context, SharedPrefs.MY_SHORTMOBILE_KEY, subphone);
    }

        else subphone = shortPhone;
        // subphone = mobile.substring(3);
        Log.i(TAG, "value of phone 10 digit is " + subphone);


        String acc_email = getEmail(context);
        email.setText(acc_email);

        Sname = name.getText().toString();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);

        mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);

        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

          enableLocationDialog();



        clientBut.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Log.i(TAG, "Client button clicked");
                                             user_role = "client";

                                             clientBut.setChecked(true);
                                             brokerBut.setChecked(false);
                                             is_role_selected = true;
                                         }
                                     }

        );

        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();
                validation_success = roleSelected();
                email_success = isEmailValid(Semail);


                Log.i(TAG, "Validation success");

                if (validation_success && email_success) {

                    Sname = name.getText().toString();
                    Semail = email.getText().toString();

                    Log.i(TAG, "Sending post request");

                    if (!Str_Lat.isEmpty() && !Str_Lng.isEmpty())
                        sendPostRequest(subphone, "+91", Semail, Sname, user_role, regid, Str_Lng, Str_Lat, picturePath);
                    else

                        Toast.makeText(
                                getApplicationContext(),
                                "Please enable location services",
                                Toast.LENGTH_LONG).show();

                    //signup_success();
                }

            }
        });


        brokerBut.setOnClickListener(new View.OnClickListener()

                                     {

                                         @Override
                                         public void onClick(View v) {

                                             Log.i(TAG, "Broker button clicked");
                                             user_role = "broker";

                                             clientBut.setChecked(false);
                                             brokerBut.setChecked(true);
                                             is_role_selected = true;


                                         }
                                     }

        );

        edit.setOnClickListener(new View.OnClickListener()

                                {

                                    @Override
                                    public void onClick(View v) {
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        // photoPickerIntent.putExtra("crop", "true");
                                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);


                                    }
                                }

        );

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null != data) {

            Uri selectedImageUri = data.getData();
            String[] FilePathColumn = {MediaStore.Images.Media.DATA};

            Cursor SelectedCursor = getContentResolver().query(selectedImageUri, FilePathColumn, null, null, null);
            SelectedCursor.moveToFirst();

            int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
            picturePath = SelectedCursor.getString(columnIndex);
            SelectedCursor.close();

            myphoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            SharedPrefs.save(context, SharedPrefs.PHOTO_KEY, picturePath);


        }
    }

    static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    private boolean roleSelected()
    {
        if (is_role_selected == true)
            return true;
        else
        {     Toast.makeText(
                getApplicationContext(),
                "Please select your role - CLIENT / BROKER",
                Toast.LENGTH_LONG).show();


            return false;
        }
    }

    private void validationCheck() {

        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("Please enter name");
           return;
        }


        Semail = email.getText().toString();

        /*if (email.getText().toString().trim().equalsIgnoreCase("")) {
            email.setError("Please enter email-id");
            return;
        }*/

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                name.setError(null);

            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             //   email.setError(null);


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                //email.setError(null);
                //isEmailValid(Semail);

            }
        });
    }

    private void sendPostRequest(final String mobile, final String code, final String Semail, final String Sname, final String user_role, final String regid, final String lon, final String lat, final String photo)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
             //   showSplashScreen();
            }


            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {
                    Log.i(TAG, "Sending JSON");

                    jsonObject.accumulate("mobile_no", subphone);
                    jsonObject.accumulate("mobile_code", code);
                    jsonObject.accumulate("email", Semail);
                    jsonObject.accumulate("name", Sname);
                    jsonObject.accumulate("user_role", user_role);
                    jsonObject.accumulate("long", lon);
                    jsonObject.accumulate("lat", lat);
                    jsonObject.accumulate("my_photo", photo);


                    jsonObject.accumulate("gcm_id", regid);


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
                    Log.i(TAG, "Response received, value is" + response);


                    if (response == 200 || response == 201) {
                        success = true;
                        Log.i(TAG, "Successful Response");

                    } else {
                        System.out.print("LoginFailed Try again");
                        Log.i(TAG, "Failure response");

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
                    Log.i(TAG,"First Exception coz of HttpResponese :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    Log.i(TAG,"Second Exception coz of HttpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //removeSplashScreen();

//parse json response
                if (result != null) {

                    try {
                        Log.i(TAG, "Parsing JSON response value is" + result);

                        JSONObject jObject = new JSONObject(result);
                        JSONObject responseDataObject = jObject.getJSONObject("responseData");
                        my_user_id = responseDataObject.getString("user_id");
                        server_pic = responseDataObject.getString("my_photo");

                        if (success==true)
                        {
                            Log.i(TAG, "Entered JSON response success");

                            signup_success();}
                        else {
                            Log.i(TAG, "JSON Response failure");

                            Toast.makeText(
                                getApplicationContext(),
                                "Signup failed, Please try again",
                                Toast.LENGTH_LONG).show();


                        }
                    } catch (JSONException e) {
                        Log.e("JSONException", "Error: " + e.toString());
                    }
                }
            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mobile, code, Semail, Sname, user_role, regid,lon,lat);
    }

    void signup_success() {
        SharedPrefs.save(context, SharedPrefs.MY_ROLE_KEY, user_role);
        SharedPrefs.save(context, SharedPrefs.NAME_KEY, Sname);
        SharedPrefs.save(context, SharedPrefs.EMAIL_KEY, Semail);
        SharedPrefs.save(context, SharedPrefs.MY_GCM_ID, regid);
        SharedPrefs.save(context, SharedPrefs.MY_USER_ID, my_user_id);
        SharedPrefs.save(context, SharedPrefs.SERVER_PHOTO_KEY, server_pic);

        isMyServiceRunning(LocationServices.class);

        //if (my_user_id != null) {

        if (user_role.equalsIgnoreCase("client")) {
            Log.i(TAG, "Starting Client activity");

            Intent NextActivity = new Intent(context, MainActivity.class);
            startActivity(NextActivity);
            finish();

        } else if (user_role.equalsIgnoreCase("broker")) {
            Log.i(TAG, "Starting Broker activity");
            Intent NextBroActivity = new Intent(context, MainBrokerActivity.class);
            startActivity(NextBroActivity);
            finish();

        }

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


    private boolean checkGPlayService() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } /*else {
            Toast.makeText(
                    getApplicationContext(),
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }*/
        return true;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        checkGPlayService();
        Intent i = new Intent(context,LocationServices.class);
        startService(i);
    }


    private void registerGCM()
    {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging
                                .getInstance(context);
                    }
                    regid = gcm
                            .register(PROJECT_NUMBER);
                    msg = regid;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

           /* @Override
            protected void onPostExecute(String msg) {
                if (msg!=null) {
                    SharedPrefs.save(context, SharedPrefs.MY_GCM_ID,regid);
                    Toast.makeText(
                            context,
                            "Registered with GCM Server successfully.\n\n"
                                   + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            context,
                            "There is a problem with your right now. Please try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }*/
        }.execute(null, null, null);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());
        Intent i = new Intent(context,LocationServices.class);
        stopService(i);

    }

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            findMyLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void findMyLocation(final Location location) {

        class TestAsync extends AsyncTask<String, Void, Void> {

            protected Void doInBackground(String... params) {

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                Str_Lng = String.valueOf(lng);
                Str_Lat = String.valueOf(lat);

                Log.i(TAG,"Value of Lat is"  + Str_Lat);
                Log.i(TAG,"Value of Long is"  + Str_Lng);

                return null;
            }
        }
        TestAsync TestAsync = new TestAsync();
        TestAsync.execute();
    }

    private void isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
        Log.i(TAG,"Service Running");
            }
        }
        Intent mServiceIntent = new Intent(this, LocationServices.class);
        startService(mServiceIntent);

    }

    private boolean isEmailValid(CharSequence email) {
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Please enter a valid email address",
                    Toast.LENGTH_LONG).show();

            return false;
        }
    }

    private void enableLocationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}
