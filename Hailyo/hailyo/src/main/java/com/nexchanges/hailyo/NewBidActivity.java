package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.nexchanges.hailyo.customSupportClass.ShowToastMessage;
import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by AbhishekWork on 23/06/15.
 */
public class NewBidActivity extends Activity {

    Context context;
    TextView bidbrokername;
    EditText srowtv1,srowtv2,srowtv3,srowtv4,srowtv1_Sale, srowtv2_Sale,srowtv3_Sale;
    String text_to_display,role, intent,sanc_status;
    Button submit, later;
    LinearLayout loanquest;
    RadioButton loan_sanc,loan_not_sanc;
    RadioGroup group_Loan;
    ShowToastMessage showToastMessage = new ShowToastMessage();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        role = SharedPrefs.getString(context, SharedPrefs.MY_ROLE_KEY);

        intent = SharedPrefs.getString(context, SharedPrefs.CURRENT_INTENT);

        String br_name = SharedPrefs.getString(context,SharedPrefs.MY_CURRENT_BROKER);

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

        /*srowtv3_Sale.addTextChangedListener(new TextWatcher() {
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
*/


        bidbrokername = (TextView)findViewById(R.id.bname);
        bidbrokername.setText(br_name);


        submit = (Button) findViewById(R.id.ssubmit);
        later = (Button) findViewById(R.id.scancel);


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (intent.equalsIgnoreCase("rent"))
                {
                    validationCheck_rent();
                    sendshortEmail();
                }
                else {sendSaleEmail();
                    validationCheck_sale();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Congratulations, your bid was submitted successfully!\n\n Do you wish to submit another bid?");
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (role.equalsIgnoreCase("client")) {
                            Intent GiveNewBid = new Intent(context, MainActivity.class);
                            SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 1);
                            startActivity(GiveNewBid);
                            text_to_display = "Select a Visit / Broker to give an offer to";
                            showToastMessage.displayToast(context,text_to_display);

                            finish();

                        } else {
                            Intent BGiveNewBid = new Intent(context, MainBrokerActivity.class);
                            SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 1);
                            startActivity(BGiveNewBid);
                            text_to_display = "Select a Visit / Broker to give an offer to";
                            showToastMessage.displayToast(context,text_to_display);
                            finish();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (role.equalsIgnoreCase("client")) {
                            Intent BacktoHomeClient = new Intent(context, MainActivity.class);
                            SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);
                            startActivity(BacktoHomeClient);
                            finish();

                        } else {
                            Intent BacktoHomeBroker = new Intent(context, MainBrokerActivity.class);
                            SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);
                            startActivity(BacktoHomeBroker);
                            finish();
                        }

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();




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
                    SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);

                    startActivity(MainActivity);
                    finish();
                } else {
                    Intent BMainActivity = new Intent(context, MainBrokerActivity.class);
                    SharedPrefs.save(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);

                    startActivity(BMainActivity);
                    finish();

                }

                // }
            }
        });


    }


    public void sendshortEmail() {
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

    private void validationCheck_rent() {


        if (srowtv1.getText().toString().trim().equalsIgnoreCase("")) {
            srowtv1.setError("Please enter property name");
            return;
        }


        if (srowtv2.getText().toString().trim().equalsIgnoreCase("")) {
            srowtv2.setError("Please enter rent offer");
            return;
        }

        if (srowtv3.getText().toString().trim().equalsIgnoreCase("")) {
            srowtv3.setError("Please enter deposit amount");
            return;
        }


        srowtv1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                srowtv1.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                srowtv1.setError(null);

            }
        });


        srowtv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                srowtv2.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                srowtv2.setError(null);

            }
        });

        srowtv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                srowtv3.setError(null);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                srowtv3.setError(null);

            }
        });

    }



    private void validationCheck_sale() {

        if (srowtv1_Sale.getText().toString().trim().equalsIgnoreCase("")) {
            srowtv1_Sale.setError("Please enter property name");
            return;
        }


        if (srowtv2_Sale.getText().toString().trim().equalsIgnoreCase("")) {
            srowtv2_Sale.setError("Please enter Price Offer");
            return;
        }

        srowtv1_Sale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                srowtv1_Sale.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                srowtv1_Sale.setError(null);
            }
        });

        srowtv2_Sale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                srowtv2_Sale.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                srowtv2_Sale.setError(null);
            }
        });
    }

}