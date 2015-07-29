package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by AbhishekWork on 23/06/15.
 */
public class NewBidActivity extends Activity {

    Context context;
    TextView bidbrokername;
    EditText srowtv1,srowtv2,srowtv3,srowtv4,srowtv1_Sale, srowtv2_Sale,srowtv3_Sale;
    String role, intent,sanc_status;
    Button submit, later;
    LinearLayout loanquest;
    RadioButton loan_sanc,loan_not_sanc;
    RadioGroup group_Loan;
    Boolean success_hail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);

        intent = SharedPrefs.getString(context, SharedPrefs.CURRENT_INTENT);

       // success_hail = SharedPrefs.getBoolean(context, SharedPrefs.SUCCESSFUL_HAIL);

        int layoutId=0;
        if(intent.equalsIgnoreCase("rent")) {
            layoutId = R.layout.submit_bid_2_rent;
        } else {
            layoutId = R.layout.submit_bid_sale;
        }
        setContentView(layoutId);


        srowtv1 = (EditText) findViewById(R.id.srowtv1_r);
        srowtv2 = (EditText) findViewById(R.id.srowtv2_r);
        srowtv3 = (EditText) findViewById(R.id.srowtv3_r);
        srowtv4 = (EditText)findViewById(R.id.srowtv4_r);

        srowtv1_Sale = (EditText) findViewById(R.id.srowtv1);
        srowtv2_Sale = (EditText) findViewById(R.id.srowtv2);
        srowtv3_Sale = (EditText) findViewById(R.id.srowtv3);
        loanquest = (LinearLayout) findViewById(R.id.loanquest);

        group_Loan = (RadioGroup) findViewById(R.id.radioLoan);
        loan_sanc = (RadioButton) findViewById(R.id.sanc);
        loan_not_sanc = (RadioButton) findViewById(R.id.notSanc);

        srowtv3_Sale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                if (Integer.parseInt(value)>0) {
                    loanquest.setVisibility(View.VISIBLE);
                    group_Loan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId) {
                                case R.id.sanc:
                                    sanc_status = "Loan Sanctioned";
                                    break;
                                case R.id.notSanc:
                                    sanc_status = "Loan Not Sanctioned";

                                    break;

                            }
                        }
                    });

                }
                else
                    loanquest.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        bidbrokername = (TextView)findViewById(R.id.bname);


        submit = (Button) findViewById(R.id.ssubmit);
        later = (Button) findViewById(R.id.scancel);


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (intent.equalsIgnoreCase("rent"))
                    sendshortEmail();
                else sendSaleEmail();

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.action_before_main, null);

                Button newBid = (Button)promptView.findViewById(R.id.newbid);


                final AlertDialog alertD = new AlertDialog.Builder(context).create();

                newBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (role.equalsIgnoreCase("client")) {
                            Intent GiveNewBid = new Intent(context,MainActivity.class);
                            SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,1);
                            startActivity(GiveNewBid);
                            finish();

                        } else {
                            Intent BGiveNewBid = new Intent(context, MainBrokerActivity.class);
                            SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,1);
                            startActivity(BGiveNewBid);
                            finish();
                        }


                    }
                });

                alertD.setView(promptView);
                alertD.show();

            }
        });



        later.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               /* if (SharedPrefs.getBoolean(context,SharedPrefs.SUCCESSFUL_HAIL)==true) {

                    if (role.equalsIgnoreCase("client")) {
                        Intent CHailProgress  = new Intent(context, PostYoActivity.class);
                        startActivity(CHailProgress);
                        finish();
                    } else {
                        Intent BHailProgress = new Intent(context, PostYoActivity_Broker.class);
                        startActivity(BHailProgress);
                        finish();

                    }
                }
                else
                {*/
                    if (role.equalsIgnoreCase("client")) {
                        Intent MainActivity = new Intent(context, MainActivity.class);
                        SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,0);

                        startActivity(MainActivity);
                        finish();
                    } else {
                        Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                        SharedPrefs.save(context,SharedPrefs.CURRENT_FLIPPER_VIEW,0);

                        startActivity(BMainActivity);
                        finish();

                    }

               // }
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

    public void sendSaleEmail()
    {
        String sub = srowtv1_Sale.getText().toString();
        String subject = "Offer for: " + sub;

        StringBuilder body = new StringBuilder();
        body.append("Property: " + srowtv1_Sale.getText().toString());
        body.append("\nOffer-Price: " + srowtv2_Sale.getText().toString());
        body.append("\nLoan Component: " + srowtv3_Sale.getText().toString());
        body.append("\nStatus: " + sanc_status);

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

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());

    }

}