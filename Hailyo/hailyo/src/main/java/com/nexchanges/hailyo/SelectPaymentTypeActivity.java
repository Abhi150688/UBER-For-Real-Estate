package com.nexchanges.hailyo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.*;

import com.nexchanges.hailyo.PaytmWalletActivity;
import com.nexchanges.hailyo.R;


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
            Intent paytmWalletActivity=new Intent(context, PaytmWalletActivity.class);
            startActivity(paytmWalletActivity);
            //finish();
        }

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.  menu_select_payment_type, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


}

