package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by AbhishekWork on 23/06/15.
 */
public class NewBidActivity extends Activity {

    Context context;
    TextView bidbrokername;
    EditText srowtv1,srowtv2,srowtv3,srowtv4;
    String role;
    Button submit, later;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_bid_2);
        context = this;


        srowtv1 = (EditText) findViewById(R.id.srowtv1);
        srowtv2 = (EditText) findViewById(R.id.srowtv2);
        srowtv3 = (EditText) findViewById(R.id.srowtv3);
        srowtv4 = (EditText)findViewById(R.id.srowtv4);

        bidbrokername = (TextView)findViewById(R.id.bname);




        final AlertDialog alertD = new AlertDialog.Builder(context).create();



        submit = (Button) findViewById(R.id.ssubmit);
        later = (Button) findViewById(R.id.scancel);
        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendshortEmail();

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.action_before_main, null);

                Button newBid = (Button)promptView.findViewById(R.id.newbid);

                Button newVisit = (Button)promptView.findViewById(R.id.newvisit);

                Button allDeals = (Button)promptView.findViewById(R.id.trans);




                final AlertDialog alertD = new AlertDialog.Builder(context).create();

                newBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent RestartNewBid = new Intent(context,NewBidActivity.class);
                        startActivity(RestartNewBid);
                        finish();

                    }
                });

                newVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (role.matches("Customer")) {
                            Intent MainActivity = new Intent(context, MainActivity.class);
                            startActivity(MainActivity);
                            finish();
                        }
                        else{
                            Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                            startActivity(BMainActivity);
                            finish();

                        }
                    }
                });

                allDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent SettingsActivity = new Intent(context, com.nexchanges.hailyo.DrawerClass.SettingsActivity.class);
                        SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,2);
                        startActivity(SettingsActivity);
                        finish();
                    }
                });


                alertD.setView(promptView);
                alertD.show();

            }
        });



        later.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (role.matches("Customer")) {
                    Intent MainActivity = new Intent(context, MainActivity.class);
                    startActivity(MainActivity);
                    finish();
                }
                else{
                    Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                    startActivity(BMainActivity);
                    finish();

                }
            }
        });


    }



    public void sendshortEmail()
    {
        String sub = srowtv1.getText().toString();
        String subject = "Offer for: " + sub;

        StringBuilder body = new StringBuilder();
        body.append("Property: " + srowtv1.getText().toString());
        body.append("\nPrice-Rent: " + srowtv2.getText().toString());
        body.append("\nPrice-Deposit: " + srowtv3.getText().toString());
        body.append("\nStart Date: " + srowtv4.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abhishek@nexchanges.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}
