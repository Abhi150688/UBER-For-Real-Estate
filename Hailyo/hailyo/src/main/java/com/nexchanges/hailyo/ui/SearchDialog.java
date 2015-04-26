package com.nexchanges.hailyo.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.nexchanges.hailyo.PaymentPopup;
import com.nexchanges.hailyo.R;

import eu.inmite.android.lib.dialogs.BaseDialogFragment;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchDialog creates the dialog fragment for Search section of this
 * application. This class uses 3rd party open source library to Style the
 * Dialog. Currently this will styled to show the Yellow separator for title of
 * Dialog. You can utilize this open source library to give more styled theme
 * for your dialogs.
 */
public class SearchDialog extends SimpleDialogFragment implements
		OnClickListener
{

	/** The count variable holds the current tab selected for count tab group. */
	private View count;

	/**
	 * The variable holds the current tab selected for payment option tab
	 * group..
	 */
	private View tab;

	/**
	 * This static method is used to show the SearchDialog fragment.
	 * 
	 * @param activity
	 *            the activity in which this fragment dialog will appear.
	 */
	public static void show(FragmentActivity activity)
	{
		new SearchDialog().show(activity.getSupportFragmentManager(), "Search");
	}

	/**
	 * Builds the dialog fragment.
	 * 
	 * @param builder
	 *            the builder
	 * @return the base dialog fragment builder
	 */
	@Override
	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder)
	{

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.search,
				null);

		setupViewComponents(v);

		builder.setTitle(R.string.search, R.drawable.ic_search_black);
		builder.setView(v);
		builder.setPositiveButton(R.string.reserve, new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				ISimpleDialogListener listener = getDialogListener();
				if (listener != null)
				{
					listener.onPositiveButtonClicked(0);
				}
				startActivity(new Intent(getActivity(), PaymentPopup.class));
				dismiss();
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

	/**
	 * Set up the view components.
	 * 
	 * @param v
	 *            the parent view container.
	 */
	private void setupViewComponents(View v)
	{
		count = v.findViewById(R.id.count1);
		count.setOnClickListener(this);
		count.setEnabled(false);

		v.findViewById(R.id.count2).setOnClickListener(this);
		v.findViewById(R.id.count3).setOnClickListener(this);

		tab = v.findViewById(R.id.tab2);
		tab.setOnClickListener(this);
		tab.setEnabled(false);

		v.findViewById(R.id.tab1).setOnClickListener(this);
		v.findViewById(R.id.tab3).setOnClickListener(this);
	}

	/**
	 * Called when a view has been clicked. Specified by: onClick(...) in
	 * OnClickListener
	 * 
	 * @param v
	 *            the view that was clicked
	 */
	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.tab1 || v.getId() == R.id.tab2
				|| v.getId() == R.id.tab3)
		{
			tab.setEnabled(true);
			tab = v;
			tab.setEnabled(false);
		}
		else if (v.getId() == R.id.count1 || v.getId() == R.id.count2
				|| v.getId() == R.id.count3)
		{
			count.setEnabled(true);
			count = v;
			count.setEnabled(false);
		}
	}
}