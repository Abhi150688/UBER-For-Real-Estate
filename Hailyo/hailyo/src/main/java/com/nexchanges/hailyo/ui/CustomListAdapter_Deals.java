package com.nexchanges.hailyo.ui;

/**
 * Created by AbhishekWork on 27/06/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nexchanges.hailyo.MyApplication;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.DealData;

import java.util.List;

public class CustomListAdapter_Deals extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DealData> dealItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomListAdapter_Deals(Activity activity, List<DealData> dealItems) {
        this.activity = activity;
        this.dealItems = dealItems;
    }

    @Override
    public int getCount() {
        return dealItems.size();
    }

    @Override
    public Object getItem(int location) {
        return dealItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.all_deals_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView User_Name = (TextView) convertView.findViewById(R.id.user_name);
        TextView Offer_Up_Date = (TextView) convertView.findViewById(R.id.offer_up_date);
        TextView Apartment_Name = (TextView) convertView.findViewById(R.id.apartment_name);
        TextView Rent = (TextView) convertView.findViewById(R.id.rent_amount);

        TextView Deposit = (TextView) convertView.findViewById(R.id.deposit_amount);

        TextView Ag_StartDate = (TextView) convertView.findViewById(R.id.agr_start_date);





        // getting movie data for the row
        DealData m = dealItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        User_Name.setText(m.getUserName());

        Apartment_Name.setText(m.getApartmentName());

        Rent.setText("Rent: " + m.getRent());

        Deposit.setText("Deposit: " + m.getDeposit());

        Ag_StartDate.setText("Start Date: " + m.getStartDate());


        // release year
        Offer_Up_Date.setText(String.valueOf(m.getOfferDate()));

        return convertView;
    }

}
