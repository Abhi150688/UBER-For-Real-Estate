package com.nexchanges.hailyo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.digits.sdk.android.Digits;
import com.nexchanges.hailyo.model.SessionRecorder;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.utils.Logger;

import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.*;


/**
 * Created by Abhishek on 28/04/15.
 */
public class LoginActivity extends Activity {


    Button enter;
    private static final String TAG = InitialActivity.class.getSimpleName();
    String namAcc, subNameAcc;
    Boolean ab;
    Context context;
    String finnum, pnum, provideDigitsNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_splash);
        context=this;

        provideDigitsNumber = getMyNumber();

        enter = (Button) findViewById(R.id.enterbut);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Final value obtained is " + provideDigitsNumber);

                if (provideDigitsNumber.isEmpty())
                {
                    Log.i(TAG, "inside empty! ");

                    Digits.authenticate(authCallback, R.style.DigitsLoginTheme);}
                else
                    Digits.authenticate(authCallback, provideDigitsNumber);
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
        Intent intent = new Intent(LoginActivity.this, ChooseRoleActivity.class);
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

    private String getMyNumber()
    {
        finnum = "";
        TelephonyManager tm = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String mPhoneNumber = tm.getLine1Number();
        if (!mPhoneNumber.isEmpty())
        {
            finnum = mPhoneNumber;
         }
        else {
            String pn;
            pn = getNumberfromApp();
            finnum = pn;
             }
        return finnum ;
    }

    private String getNumberfromApp()
    {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();

        pnum = " ";
        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;
            // Take your time to look at all available accounts
            System.out.println("Accounts : " + acname + ", " + actype);
            if (actype.equalsIgnoreCase("com.viber.voip.account") || actype.equalsIgnoreCase("com.facebook.auth.login"))
            {   String namAcc = ac.name;

                if (namAcc.startsWith("+")) {
                    subNameAcc = namAcc.substring(3);
                    ab = android.text.TextUtils.isDigitsOnly(subNameAcc);
                    Log.i(TAG, "Boolean value of ab is " + ab);

                    if (ab)
                        pnum = subNameAcc;
                }

                else if (android.text.TextUtils.isDigitsOnly(ac.name))
                {
                    pnum = ac.name;
                }

                else
                pnum = "";
            }

            else if (acname.startsWith("91"))
            {
                pnum = ac.name;
            }
            else pnum = "";
        }
        Log.i(TAG, "Final value returned is " + pnum);

        return pnum;
    }

}


















