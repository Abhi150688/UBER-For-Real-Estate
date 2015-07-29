package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.nexchanges.hailyo.model.SessionRecorder;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.utils.Logger;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.widget.Toast;

import java.net.URL;
import java.util.List;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.SignUpCallback;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Abhishek on 28/04/15.
 */
public class LoginActivity1 extends Activity {


    Button enter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_splash);
        context=this;

        enter = (Button) findViewById(R.id.enterbut);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Digits.authenticate(authCallback, R.style.DigitsLoginTheme);
            }
        });



    }


    @Override
    protected void onStop() {
        super.onStop();
        authCallback = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, final String phoneNumber) {


            SessionRecorder.recordSessionActive("Login: digits account active", session);
            // Create a ParseUser object to create a new user
            navigateToHome(phoneNumber);



            //

            Logger.writeLogs(Logger.LogLevel.INFO, "Success");
        }

        @Override
        public void failure(DigitsException e) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.toast_twitter_digits_fail),
                    Toast.LENGTH_SHORT).show();

            }
    };

    public void navigateToHome(String phoneNumber) {
        Intent intent = new Intent(LoginActivity1.this, ChooseRoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SharedPrefs.save(context, SharedPrefs.MY_MOBILE_KEY, phoneNumber);
        startActivity(intent);
        finish();
    }



    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());
        // Logs 'app deactivate' App Event.
    }

}



















