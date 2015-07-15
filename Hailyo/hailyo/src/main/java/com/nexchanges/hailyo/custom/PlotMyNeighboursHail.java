package com.nexchanges.hailyo.custom;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.nexchanges.hailyo.MainActivity;
import com.nexchanges.hailyo.MainBrokerActivity;
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
 * Created by AbhishekWork on 16/07/15.
 */
public class PlotMyNeighboursHail {

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
                    SharedPrefs.save(context, SharedPrefs.MY_USER_ID, my_user_id);

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
