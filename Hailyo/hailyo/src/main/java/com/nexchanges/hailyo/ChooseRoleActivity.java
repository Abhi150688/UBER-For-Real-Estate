package com.nexchanges.hailyo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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
    private String Semail, Sname, user_role, Sphoto, C = "Customer", B = "Broker", my_user_id, my_gcm_id, num;
    String URL = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000/1/user/signup";
    StringEntity se;
    Button clientBut, brokerBut;
    Dialog alertD;
    Context context;
    EditText name, email;
    ImageView myphoto;
    String picturePath, mobile;
    private static final int SELECT_PHOTO = 1;
    GoogleCloudMessaging gcm;
    String regid, GCMID;
    String PROJECT_NUMBER = "250185285941";
    TextView edit, fbdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);
        checkGCMService();
        registerGCM();

        context = this;
        name = (EditText) findViewById(R.id.etname);
        email = (EditText) findViewById(R.id.etemail);

        clientBut = (Button) findViewById(R.id.iamclient);
        brokerBut = (Button) findViewById(R.id.iambroker);

        myphoto = (ImageView) findViewById(R.id.myphoto);

        edit = (TextView) findViewById(R.id.edit);

        mobile = SharedPrefs.getString(context, SharedPrefs.MY_MOBILE_KEY);


        String acc_email = getEmail(context);
        email.setText(acc_email);

        Sname = name.getText().toString();


        clientBut.setOnClickListener(new View.OnClickListener()

                                     {

                                         @Override
                                         public void onClick(View v) {
                                             validationCheck();


                                             if ((name.getText().toString().length() > 0) &&
                                                     (email.getText().toString().length() > 0)) {
                                                 // TODO Auto-generated method stub
                                                 clientBut.setBackgroundColor(Color.parseColor("#FFA500"));
                                                 clientBut.setTextColor(Color.WHITE);
                                                 brokerBut.setBackgroundColor(Color.LTGRAY);
                                                 brokerBut.setTextColor(Color.BLACK);

                                                 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                                                 Sname = name.getText().toString();
                                                 Semail = email.getText().toString();

                                                 user_role = "client";

                                                sendPostRequest(mobile, "+91", Semail, Sname, user_role, regid);

                                                // signup_success();
                                             }


                                         }
                                     }

        );


        brokerBut.setOnClickListener(new View.OnClickListener()

                                     {

                                         @Override
                                         public void onClick(View v) {

                                             validationCheck();

                                             if ((name.getText().toString().length() > 0) &&
                                                     (email.getText().toString().length() > 0)) {
                                                 // TODO Auto-generated method stub
                                                 brokerBut.setBackgroundColor(Color.parseColor("#FFA500"));
                                                 brokerBut.setTextColor(Color.WHITE);
                                                 clientBut.setBackgroundColor(Color.LTGRAY);
                                                 clientBut.setTextColor(Color.BLACK);


                                                 Sname = name.getText().toString();
                                                 Semail = email.getText().toString();
                                                 user_role = "broker";
                                                 sendPostRequest(mobile, "+91", Semail, Sname, user_role,regid);
                                                 //signup_success();
                                             }

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

    private void validationCheck() {

        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("Please enter name");
            return;
        }


        Semail = email.getText().toString();

        if (email.getText().toString().trim().equalsIgnoreCase("")) {
            email.setError("Please enter email-id");
            return;
        }

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
                email.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                email.setError(null);

            }
        });
    }

    private void sendPostRequest(final String mobile, final String code, final String Semail, final String Sname, final String user_role, final String regid)
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
                    jsonObject.accumulate("mobile_no", mobile);
                    jsonObject.accumulate("mobile_code", code);
                    jsonObject.accumulate("email", Semail);
                    jsonObject.accumulate("name", Sname);
                    jsonObject.accumulate("user_role", user_role);

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

                    if (response == 200 || response == 201) {
                       signup_success();
                    } else {
                        System.out.print("LoginFailed Try again");
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

//parse json response
                if (result != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        for (int i = 0; i < jsonObject.length(); i++) {

                            my_user_id = jsonObject.getString("user_id");


                        } //
                        // End Loop
                        SharedPrefs.save(context, SharedPrefs.MY_USER_ID, my_user_id);

                    } catch (JSONException e) {
                        Log.e("JSONException", "Error: " + e.toString());
                    }
                }
            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mobile, code, Semail, Sname, user_role, regid);
    }

    void signup_success() {
        SharedPrefs.save(context, SharedPrefs.MY_ROLE_KEY, user_role);
        SharedPrefs.save(context, SharedPrefs.NAME_KEY, Sname);
        SharedPrefs.save(context, SharedPrefs.EMAIL_KEY, Semail);
        SharedPrefs.save(context, SharedPrefs.MY_GCM_ID, regid);

        //if (my_user_id != null) {

            if (user_role.equalsIgnoreCase("client")) {
                Intent NextActivity = new Intent(context, MainActivity.class);
                startActivity(NextActivity);
                finish();

            } else if (user_role.equalsIgnoreCase("broker")) {
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


    private boolean checkGCMService() {
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
        checkGCMService();
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

    private String getMyNumber()
    {
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (mPhoneNumber.isEmpty())
            return " ";
        else
        num = mPhoneNumber;
        return num;
    }

}
