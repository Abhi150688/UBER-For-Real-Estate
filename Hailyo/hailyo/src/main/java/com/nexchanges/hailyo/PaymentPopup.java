package com.nexchanges.hailyo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.nexchanges.hailyo.custom.CustomActivity;

// TODO: Auto-generated Javadoc
/**
 * The Activity PaymentPopup is launched when user proceed for the payment of
 * Taxi he/she want to book. Currently it will be launched when user tap on
 * middle button located at the bottom of the Map screen. It will also launched
 * when user click on Reserve button in Search dialog. You must customize and
 * enhance this as per your needs.
 */
public class PaymentPopup extends CustomActivity
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_popup);

		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.gravity = Gravity.BOTTOM;

		setupView();
	}

	/**
	 * Setup the click & other events listeners for the view components of this
	 * screen. You can add your logic for Binding the data to TextViews and
	 * other views as per your need.
	 */
	private void setupView()
	{
		View b = findViewById(R.id.btnCancel);
		b.setOnTouchListener(TOUCH);
		b.setOnClickListener(this);

		b = findViewById(R.id.btnPay);
		b.setOnTouchListener(TOUCH);
		b.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see com.nexchanges.hailyo.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		finish();
	}
}
