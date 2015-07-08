package com.nexchanges.hailyo.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.Feed;

// TODO: Auto-generated Javadoc
/**
 * The Adapter class for the ListView displayed in the left navigation drawer.
 */
public class LeftNavAdapter extends BaseAdapter
{

	/** The items. */
	private ArrayList<Feed> items;

	/** The context. */
	private Context context;

	/** The is first. */
	private boolean isFirst;

	/**
	 * Instantiates a new left navigation adapter.
	 * 
	 * @param context
	 *            the context of activity
	 * @param items
	 *            the array of items to be displayed on ListView
	 */
	public LeftNavAdapter(Context context, ArrayList<Feed> items)
	{
		this.context = context;
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Feed getItem(int arg0)
	{
		return items.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(context).inflate(R.layout.left_nav_item, null);


		Feed f = getItem(position);
		TextView lbl = (TextView) convertView;
		lbl.setText(f.getTitle());
		lbl.setCompoundDrawablesWithIntrinsicBounds(f.getImage(), 0, 0, 0);
		if (f.getDesc() != null)
			convertView.setBackgroundColor(context.getResources().getColor(R.color.theme_yellow));

		else
			convertView.setBackgroundColor(Color.TRANSPARENT);

		return convertView;
	}

}
