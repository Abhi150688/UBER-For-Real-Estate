package com.nexchanges.hailyo.DrawerClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nexchanges.hailyo.R;
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
 * Created by Abhishek on 08/05/15.
 */
public class ProfileActivity extends Activity {

    String URL = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000/1/user/updatemydetails";
    private static final String TAG = ProfileActivity.class.getSimpleName();
    StringEntity se;
    Context context;
    ImageView myphoto;
    TextView edit, myemail,mymobile, myrole;
    String myname1, myemail1,mymobile1, myrole1,myphoto1, picturePath, user_id,subphone,finalName = "";
    private static final int SELECT_PHOTO = 1;
    EditText myname;
    Button save;
  Boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        context = this;

        myphoto = (ImageView) findViewById(R.id.myphoto);
        myemail = (TextView) findViewById(R.id.myemail);
        myname = (EditText) findViewById(R.id.myname);
        edit = (TextView) findViewById(R.id.edit);
        myrole = (TextView) findViewById(R.id.myrole);
        mymobile = (TextView) findViewById(R.id.mymobile);
        save = (Button) findViewById(R.id.save);

        myname1 = SharedPrefs.getString(context,SharedPrefs.NAME_KEY);
        myemail1 = SharedPrefs.getString(context,SharedPrefs.EMAIL_KEY);
        myrole1=   SharedPrefs.getString(context,SharedPrefs.MY_ROLE_KEY);
        mymobile1 = SharedPrefs.getString(context,SharedPrefs.MY_MOBILE_KEY);
        myphoto1 = SharedPrefs.getString(context,SharedPrefs.PHOTO_KEY);
        user_id = SharedPrefs.getString(context,SharedPrefs.MY_USER_ID);
        subphone = mymobile1.substring(3);


        myemail.setText(myemail1);
        myname.setText(myname1);
        myrole.setText(myrole1);
        mymobile.setText(mymobile1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();
                sendPostRequest(user_id,subphone, "+91", myemail1,finalName);

            }
        });

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


    private void validationCheck() {

        if (myname.getText().toString().trim().equalsIgnoreCase("")) {
            myname.setError("Please enter name");
            return;
        }

        myname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                myname.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                finalName = myname.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                myname.setError(null);
                finalName = myname.getText().toString();

            }
        });

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

    void update_success() {
        SharedPrefs.save(context, SharedPrefs.PHOTO_KEY, picturePath);
        SharedPrefs.save(context, SharedPrefs.NAME_KEY, finalName);

            }


    private void sendPostRequest(final String UID, final String Phone, final String Code, final String SEmail, final String Sname)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {

            }


            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("user_id", UID);
                    jsonObject.accumulate("mobile_no", Phone);
                    jsonObject.accumulate("mobile_code", Code);
                    jsonObject.accumulate("email", SEmail);
                    jsonObject.accumulate("name", Sname);

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
                        System.out.print("LoginFailed Try again");
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
                if (success==true)
                    update_success();
                else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Signup failed, Please try again",
                            Toast.LENGTH_LONG).show();
                }

            }


        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(UID, Phone, Code, SEmail, Sname);
    }

    }
