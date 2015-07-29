package com.nexchanges.hailyo.paymentGateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.*;

import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.paymentGateway.PaytmWalletActivity;


/**
 * Created by Abhishek on 07/05/15.
 */
public class SelectPaymentTypeActivity extends ActionBarActivity implements View.OnClickListener{

    Context context;
    Button usePaytm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_type_layout);
        context =  this;

        usePaytm = (Button) findViewById(R.id.usePaytm);
        usePaytm.setOnClickListener(this);

    }

    @Override
    public void onClick(View selected) {

        if ( selected == usePaytm ){

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.create_add_paytm, null);

            Button accept = (Button)promptView.findViewById(R.id.accept);

            Button cancel = (Button)promptView.findViewById(R.id.cencelpaytm);


            final AlertDialog alertD = new AlertDialog.Builder(context).create();

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // paytm logic to check if account exists or create a new one.

                    Intent paytmWalletActivity=new Intent(context, PaytmWalletActivity.class);
                    startActivity(paytmWalletActivity);

                }
            });


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertD.dismiss();
                }
            });


            alertD.setView(promptView);
            alertD.show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


}