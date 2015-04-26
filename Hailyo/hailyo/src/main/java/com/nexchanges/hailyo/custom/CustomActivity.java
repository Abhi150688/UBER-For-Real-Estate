package com.nexchanges.hailyo.custom;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.nexchanges.hailyo.utils.TouchEffect;

// TODO: Auto-generated Javadoc
/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like setting a Theme to activity.
 */
public class CustomActivity extends FragmentActivity implements OnClickListener
{

	/**
	 * Apply this Constant as touch listener for views to provide alpha touch
	 * effect. The view must have a Non-Transparent background.
	 */
	public static final TouchEffect TOUCH = new TouchEffect();

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

	}

}
