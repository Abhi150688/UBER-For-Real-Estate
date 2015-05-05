package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

/**
 * Created by Abhishek on 28/04/15.
 */
public class LoginActivity1 extends Activity {


    Button loginButtonView;
 //   publicString phoneNumber;
    private TwitterLoginButton loginButton;
    private DigitsAuthButton d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, final String phoneNumber) {

                // Create a ParseUser object to create a new user
                final ParseUser user = new ParseUser();
                user.setUsername(phoneNumber);
                user.setPassword("Fake Password");

                // First query to check whether a ParseUser with
                // the given phone number already exists or not
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", phoneNumber);

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {

                        if (e == null) {
                            // Successful Query

                            // User already exists ? then login
                            if (parseUsers.size() > 0) {
                                loginUser(phoneNumber, "Fake Password");
                            }
                            else {
                                // No user found, so signup
                                signupUser(user);
                            }
                        }
                        else {
                            // Shit happened!
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity1.this);
                            builder.setMessage(e.getMessage())
                                    .setTitle("Oops-ZO!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });

                navigateToHome();




            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
            }
        });






            } //end of oncreate.

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    navigateToHome();

                } else {
                    // Login failed!
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity1.this);
                    builder.setMessage(e.getMessage())
                            .setTitle("Oops-hol!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    private void signupUser(ParseUser user) {
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Signup successful!

                    navigateToHome();
                } else {
                    // Fail!
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity1.this);
                    builder.setMessage(e.getMessage())
                            .setTitle("Oops-A!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    public void navigateToHome() {
        // Let's go to the MainActivity

        Intent intent = new Intent(LoginActivity1.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra("Mobile",sphoneNumber);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_chat) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void logout(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }


}







