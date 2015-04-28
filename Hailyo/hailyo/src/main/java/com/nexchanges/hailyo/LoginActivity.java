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
import com.parse.Parse.*;
import com.nexchanges.hailyo.MainActivity;
import com.nexchanges.hailyo.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 28/04/15.
 */
public class LoginActivity extends Activity {


    Button loginButtonView;
    private String sphoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // All the views from our login form
        final EditText firstNameView = (EditText) findViewById(R.id.firstName);
        final EditText lastNameView = (EditText) findViewById(R.id.lastName);
        final Spinner countryView = (Spinner) findViewById(R.id.country);
        final EditText countryCodeView = (EditText) findViewById(R.id.countryCode);
        final EditText phoneNumberView = (EditText) findViewById(R.id.phoneNumber);
        loginButtonView = (Button) findViewById(R.id.loginButton);


        // Set items for the Spinner dropdown
        ArrayList<String> countries = new ArrayList<String>();
        countries.add("Australia");
        countries.add("Brazil");
        countries.add("China");
        countries.add("Canada");
        countries.add("India");
        countries.add("Russia");
        countries.add("Singapore");
        countries.add("United States");

        // Create the adapter for the spinner
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinner
        countryView.setAdapter(adapter);


        // On login button click
        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get the values of all the form field


                sphoneNumber = phoneNumberView.getText().toString().trim();
                String firstName = firstNameView.getText().toString().trim();
                String lastName = lastNameView.getText().toString().trim();
                String countryCode = countryCodeView.getText().toString().trim();
                String country = countryView.getSelectedItem().toString().trim();



                //Intent intent = new Intent(getParent(), MainActivity.class);
                //intent.putExtra("Phone", phoneNumber);
                //startActivity(intent);


                // Simple validation: if any field is empty then don't let the form submit
                // and show an alert dialog with error message
                if (sphoneNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || countryCode.isEmpty() || country.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Please make sure you entered all the fields correctly.")
                            .setTitle("Oops-1!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return;
                }

                // Create a ParseUser object to create a new user
                final ParseUser user = new ParseUser();

                user.setUsername(sphoneNumber);
                user.setPassword("Fake Password");
                user.put("firstName", firstName);
                user.put("lastName", lastName);
                user.put("country", country);
                user.put("countryCode", countryCode);

                // First query to check whether a ParseUser with
                // the given phone number already exists or not
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", sphoneNumber);

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {

                        if (e == null) {
                            // Successful Query

                            // User already exists ? then login
                            if (parseUsers.size() > 0) {
                                loginUser(sphoneNumber, "Fake Password");
                            }
                            else {
                                // No user found, so signup
                                signupUser(user);
                            }
                        }
                        else {
                            // Shit happened!
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(e.getMessage())
                                    .setTitle("Oops-ZO!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    navigateToHome();

                } else {
                    // Login failed!
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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



        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra("Mobile",sphoneNumber);
        startActivity(intent);
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



}





