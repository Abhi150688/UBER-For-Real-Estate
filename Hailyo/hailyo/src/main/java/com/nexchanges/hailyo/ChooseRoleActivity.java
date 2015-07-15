package com.nexchanges.hailyo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nexchanges.hailyo.model.SharedPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AbhishekWork on 22/06/15.
 */
public class ChooseRoleActivity extends Activity {

    //declare variables
    private String Semail, Sname, user_role, Sphoto,C = "Customer",B ="Broker", my_user_id;
    String URL = "http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000/1/user/signup";
    StringEntity se;
    Button clientBut, brokerBut;

    Context context;
    EditText name, email;
    ImageButton myphoto;
    String picturePath, mobile;
    private static final int SELECT_PHOTO = 1;
    TextView edit,fbdata;
    //CallbackManager callbackManager;
    //ImageButton fb;
    //LoginManager loginManager;
    //String fbfirstname, fblastname,fbpic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);



        context = this;
        name = (EditText) findViewById(R.id.etname);
        email = (EditText) findViewById(R.id.etemail);
       // fbdata = (TextView)findViewById(R.id.fbtext);
//        fb = (ImageButton)findViewById(R.id.fb);


        clientBut = (Button) findViewById(R.id.iamclient);
        brokerBut = (Button) findViewById(R.id.iambroker);

        myphoto = (ImageButton) findViewById(R.id.myphoto);

        edit = (TextView)findViewById(R.id.edit);



    //    FacebookSdk.sdkInitialize(context);
  //      callbackManager = CallbackManager.Factory.create();
  //      FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
    //    loginManager = LoginManager.getInstance();
//        loginManager.setDefaultAudience(DefaultAudience.EVERYONE);


  /*fb.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          fblogn();
      }
  });

        fbdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogn();
            }
        });
*/


        String acc_email = getEmail(context);
        email.setText(acc_email);

        Sname = name.getText().toString();



        clientBut.setOnClickListener(new View.OnClickListener()

                                             {

                                                 @Override
                                                 public void onClick(View v) {
                                                     validationCheck();


                                                     if ((name.getText().toString().length()>0) &&
                                                             ( email.getText().toString().length()>0))
                                                     {
                                                         // TODO Auto-generated method stub
                                                        // clientBut.setBackgroundColor(Color.parseColor("#FFA500"));
                                                         //clientBut.setTextColor(Color.WHITE);
                                                         String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                                                         Sname = name.getText().toString();
                                                         Semail = email.getText().toString();

                                                         user_role = "client";
                                                         mobile = SharedPrefs.getString(context, SharedPrefs.MY_MOBILE_KEY);
                                                         sendPostRequest(mobile, "+91", Semail,Sname,user_role);
                                                     }


                                                 }
                                             }

                );


            brokerBut.setOnClickListener(new View.OnClickListener()

            {

                @Override
                public void onClick (View v){

                 validationCheck();

                    if ((name.getText().toString().length()>0) &&
                            ( email.getText().toString().length()>0))
                    {
                        // TODO Auto-generated method stub
                       // brokerBut.setBackgroundColor(Color.parseColor("#FFA500"));
                        //brokerBut.setTextColor(Color.WHITE);


                        Sname = name.getText().toString();
                        Semail = email.getText().toString();
                        user_role = "broker";
                        sendPostRequest(mobile, "+91", Semail,Sname,user_role);
                    }

            }
            }

            );

            edit.setOnClickListener(new View.OnClickListener()

            {

                @Override
                public void onClick (View v){
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                // photoPickerIntent.putExtra("crop", "true");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }
            }

            );

        }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null!= data) {

                Uri selectedImageUri = data.getData();
                String[] FilePathColumn = {MediaStore.Images.Media.DATA };

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
        } return account;
    }

    private void validationCheck()
    {

        if(name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("Please enter name");
            return;
        }


        Semail = email.getText().toString();

        if(email.getText().toString().trim().equalsIgnoreCase("")) {
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

    private void sendPostRequest(final String mobile, final String code, final String Semail, final String Sname, final String user_role)
    {


        class SendPostReqAsyncTask extends AsyncTask<String, Void,String> {

            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("mobile_no", mobile);
                    jsonObject.accumulate("mobile_code", code);
                    jsonObject.accumulate("email", Semail);
                    jsonObject.accumulate("name", Sname);
                    jsonObject.accumulate("user_role", user_role);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpClient httpClient = new DefaultHttpClient();

                // In a POST request, we don't pass the values in the URL.
                //Therefore we use only the web page URL as the parameter of the HttpPost argument
                HttpPost httpPost = new HttpPost(URL);


                try {
                    se = new StringEntity(jsonObject.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


               /* // Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
                //uniquely separate by the other end.
                //To achieve that we use BasicNameValuePair
                //Things we need to pass with the POST request
                BasicNameValuePair mobileBasicNameValuePair = new BasicNameValuePair("parammobile", parammobile);
                BasicNameValuePair codeBasicNameValuePAir = new BasicNameValuePair("paramcode", paramcode);
                BasicNameValuePair emailBasicNameValuePAir = new BasicNameValuePair("paramemail", paramemail);
                BasicNameValuePair nameBasicNameValuePAir = new BasicNameValuePair("paramname", paramname);
                BasicNameValuePair roleBasicNameValuePAir = new BasicNameValuePair("paramrole", paramrole);

                // We add the content that we want to pass with the POST request to as name-value pairs
                //Now we put those sending details to an ArrayList with type safe of NameValuePair
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(mobileBasicNameValuePair);
                nameValuePairList.add(codeBasicNameValuePAir);
                nameValuePairList.add(emailBasicNameValuePAir);
                nameValuePairList.add(nameBasicNameValuePAir);
                nameValuePairList.add(roleBasicNameValuePAir);*/


                // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs.
                //This is typically useful while sending an HTTP POST request.
                //UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(se);

                // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
                se.setContentType(new BasicHeader("Content-Type", "application/json"));

                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");

                try {
                    // HttpResponse is an interface just like HttpPost.
                    //Therefore we can't initialize them
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    int response = httpResponse.getStatusLine().getStatusCode();
                    System.out.print("Value of response code is: " + response);

                    if (response == 200 || response == 201)
                    {
                        signup_success();
                    }

                    else
                    {
                        System.out.print("LoginFailed Try again");
                    }

                    // According to the JAVA API, InputStream constructor do nothing.
                    //So we can't initialize InputStream although it is not an interface
                    InputStream inputStream = httpResponse.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
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

//parse json response

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    for(int i=0; i < jsonObject.length(); i++) {

                        my_user_id = jsonObject.getString("user_id");


                    } //
                   // End Loop
                SharedPrefs.save(context,SharedPrefs.MY_USER_ID,my_user_id);

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }

            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mobile,code,Semail,Sname,user_role);
    }

    void signup_success()
    {
        SharedPrefs.save(context, SharedPrefs.MY_ROLE_KEY, user_role);
        SharedPrefs.save(context, SharedPrefs.NAME_KEY, Sname);
        SharedPrefs.save(context, SharedPrefs.EMAIL_KEY, Semail);

        if (user_role.equalsIgnoreCase("client"))
        {
            Intent NextActivity = new Intent(context, MainActivity.class);
            startActivity(NextActivity);
            finish();
        }

        else if (user_role.equalsIgnoreCase("broker"))
        {
            Intent NextActivity = new Intent(context, MainBrokerActivity.class);
            startActivity(NextActivity);
            finish();
        }
        // editor.putString(MyPhoto, encodeTobase64(bitphoto));

    }
}




