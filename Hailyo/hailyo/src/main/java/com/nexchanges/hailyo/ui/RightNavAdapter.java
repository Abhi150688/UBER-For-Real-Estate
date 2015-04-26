package com.nexchanges.hailyo.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.Feed;

// TODO: Auto-generated Javadoc
/**
 * The Adapter class for the ListView displayed in the left navigation drawer.
 */
public class RightNavAdapter extends BaseAdapter
{

	/** The items. */
	private ArrayList<Feed> items;

	/** The context. */
	private Context context;

	/**
	 * Instantiates a new left navigation adapter.
	 * 
	 * @param context
	 *            the context of activity
	 * @param items
	 *            the array of items to be displayed on ListView
	 */
	public RightNavAdapter(Context context, ArrayList<Feed> items)
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.right_nav_item, null);

		Feed f = getItem(position);
		TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
		lbl.setText(f.getTitle());
		lbl = (TextView) convertView.findViewById(R.id.lbl2);
		lbl.setText(f.getDesc());

		ImageView img = (ImageView) convertView.findViewById(R.id.img);
		img.setImageResource(f.getImage());

		img = (ImageView) convertView.findViewById(R.id.status);
		img.setImageResource(f.isOnline() ? R.drawable.online
				: R.drawable.offline);

		return convertView;
	}

}
