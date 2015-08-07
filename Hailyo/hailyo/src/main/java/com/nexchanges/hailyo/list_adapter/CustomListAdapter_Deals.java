package com.nexchanges.hailyo.list_adapter;

/**
 * Created by AbhishekWork on 27/06/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nexchanges.hailyo.MyApplication;
import com.nexchanges.hailyo.NewBidActivity;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.DealData;
import com.nexchanges.hailyo.model.SharedPrefs;

import java.util.List;
import java.util.regex.Pattern;

public class CustomListAdapter_Deals extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DealData> dealItems;
    String deal_type;
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
        TextView RentOrPriceV = (TextView) convertView.findViewById(R.id.rent_amountv);

        TextView DepositORLoanPerV = (TextView) convertView.findViewById(R.id.deposit_amountv);

        TextView StartDateOrLoanSanctionedV = (TextView) convertView.findViewById(R.id.agr_start_datev);

        // getting movie data for the row
        final DealData m = dealItems.get(position);


        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        User_Name.setText(m.getUserName());
        Apartment_Name.setText(m.getApartmentName());
        Offer_Up_Date.setText(String.valueOf(m.getOfferDate()));


        deal_type = m.getDealType();

        if (deal_type.equalsIgnoreCase("rent"))

        {
            RentOrPriceV.setText("RENT:");
            Rent.setText(Integer.toString(m.getRent()));

            DepositORLoanPerV.setText("DEPOSIT: ");
            Deposit.setText(Integer.toString(m.getDeposit()));

            StartDateOrLoanSanctionedV.setText("START DATE: ");
            Ag_StartDate.setText(m.getStartDate());

        }

        else {

            RentOrPriceV.setText("PRICE:");
            Rent.setText(Integer.toString(m.getOfferPrice()));

            DepositORLoanPerV.setText("LOAN %: ");
            Deposit.setText(Integer.toString(m.getLoanCom()));

            StartDateOrLoanSanctionedV.setText("LOAN STATUS: ");
            Ag_StartDate.setText(m.getLoanStatus());

        }

        convertView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
                SharedPrefs.save(activity,SharedPrefs.UPDATE_DEAL,"true");
                SharedPrefs.save(activity, SharedPrefs.MY_CURRENT_BROKER, m.getUserName());

                if (deal_type.equalsIgnoreCase("rent")) {

                    SharedPrefs.save(activity, SharedPrefs.CURRENT_INTENT, "rent");
                    Intent newBid = new Intent(activity, NewBidActivity.class);
                    newBid.putExtra("rent",m.getRent());
                    newBid.putExtra("deposit",m.getDeposit());
                    newBid.putExtra("apt_name",m.getApartmentName());
                    newBid.putExtra("start_date",m.getStartDate());
                    activity.startActivity(newBid);

                }


                else{
                    SharedPrefs.save(activity, SharedPrefs.CURRENT_INTENT, "out");
                    Intent newSaleBid = new Intent(activity, NewBidActivity.class);

                    newSaleBid.putExtra("price",m.getOfferPrice());
                    newSaleBid.putExtra("loan_com",m.getLoanCom());
                    newSaleBid.putExtra("apt_name",m.getApartmentName());
                    newSaleBid.putExtra("loan_status", m.getLoanStatus());
                    activity.startActivity(newSaleBid);
                }

            }

        });

        return convertView;
    }

}
