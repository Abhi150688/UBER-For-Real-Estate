package com.nexchanges.hailyo.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.nexchanges.hailyo.R;


import eu.inmite.android.lib.dialogs.BaseDialogFragment;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class CallDialog creates the dialog fragment for Call section of this
 * application. This class uses 3rd party open source library to Style the
 * Dialog. Currently this will styled to show the Yellow separator for title of
 * Dialog. You can utilize this open source library to give more styled theme
 * for your dialogs.
 */
public class CallDialog extends SimpleDialogFragment
{

	/**
	 * This static method is used to show the CallDialog fragment.
	 * 
	 * @param activity
	 *            the activity in which this fragment dialog will appear.
	 */
	public static void show(FragmentActivity activity)
	{
		new CallDialog().show(activity.getSupportFragmentManager(), "Call");
	}

	/* (non-Javadoc)
	 * @see eu.inmite.android.lib.dialogs.SimpleDialogFragment#build(eu.inmite.android.lib.dialogs.BaseDialogFragment.Builder)
	 */
	@Override
	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder)
	{
		builder.setTitle(R.string.call, R.drawable.ic_call_black);
		builder.setMessage("+44 8718718710");
		builder.setPositiveButton(R.string.call, new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				ISimpleDialogListener listener = getDialogListener();
				if (listener != null)
				{
					listener.onPositiveButtonClicked(0);
				}
				dismiss();

				Intent call = new Intent(Intent.ACTION_CALL);
				call.setData(Uri.parse("tel:" + "+44 8718718710"));
				startActivity(call);
			}
		});
		builder.setNegativeButton(R.string.cancel, new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		return builder;
	}
}