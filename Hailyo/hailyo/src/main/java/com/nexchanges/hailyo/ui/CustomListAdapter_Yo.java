package com.nexchanges.hailyo.ui;

/**
 * Created by AbhishekWork on 27/06/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nexchanges.hailyo.MyApplication;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.SuccessfulYo;
import com.nexchanges.hailyo.model.YoData;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter_Yo extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<YoData> yoItems;
    private ArrayList<YoData> arraylist;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    String U_type;
    YoData m;
    int timer_val;

    public CustomListAdapter_Yo(Activity activity, List<YoData> yoItems) {
        this.activity = activity;
        //yoItems.clear();
        this.yoItems = yoItems;
    }

    @Override
    public int getCount() {
        return yoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return yoItems.get(location);
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
            convertView = inflater.inflate(R.layout.all_yo_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView User_Name = (TextView) convertView.findViewById(R.id.user_name);
        TextView Visit_Count = (TextView) convertView.findViewById(R.id.visit_count);
        TextView Spec_Code = (TextView) convertView.findViewById(R.id.speccode);
        TextView Rating = (TextView) convertView.findViewById(R.id.user_rating);
        final TextView User_Type = (TextView) convertView.findViewById(R.id.user_type);




        // getting movie data for the row
        m = yoItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        User_Name.setText(m.getUserName());

        User_Type.setText(m.getUserType());

        U_type = m.getUserType();

        Spec_Code.setText(m.getSpecCode());
        // rating
        Rating.setText("Rating: " + String.valueOf(m.getRating()) + "/5");
        // release year
        Visit_Count.setText("Hails done" + String.valueOf(m.getVisitCount()));




        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final String spec = m.getSpecCode();
                final String U_type = m.getUserType();



                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                View promptView = layoutInflater.inflate(R.layout.yo_timer, null);


                final Button min10 = (Button)promptView.findViewById(R.id.mins10);
                final Button min15 = (Button)promptView.findViewById(R.id.mins15);

                final AlertDialog alertD = new AlertDialog.Builder(activity).create();


                ImageView SendYo = (ImageView) promptView.findViewById(R.id.sendYo);
                TextView spec1 = (TextView) promptView.findViewById(R.id.tvspec);

                spec1.setText(spec);

                final TextView timerval = (TextView) promptView.findViewById(R.id.tvtimerValue);

                min10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer_val=10;
                        timerval.setText(timer_val + " mins");
                        min10.setTextColor(Color.WHITE);
                        min10.setBackgroundColor(Color.parseColor("#FFA500"));

                        min15.setTextColor(Color.BLACK);
                        min15.setBackgroundResource(R.drawable.button_border);

                    }
                });

                min15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer_val=15;
                        timerval.setText(timer_val + " mins");
                        min15.setTextColor(Color.WHITE);
                        min15.setBackgroundColor(Color.parseColor("#FFA500"));

                        min10.setTextColor(Color.BLACK);
                        min10.setBackgroundResource(R.drawable.button_border);

                    }
                });


                SendYo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent AfterYoBroker = new Intent(activity, SuccessfulYo.class);
                        Bundle extras = new Bundle();
                        extras.putInt("timer", timer_val);
                        extras.putString("spec", spec);
                        extras.putString("user_type", U_type);
                        AfterYoBroker.putExtras(extras);
                        activity.startActivity(AfterYoBroker);
                        activity.finish();

                    }
                });

                alertD.setView(promptView);
                alertD.show();

            }

        });


        return convertView;
    }



    // Filter Class
   /* public void filter(String charText) {
        yoItems.clear();
            for (YoData yo_D : arraylist) {
                if (yo_D.getUserType().toLowerCase()
                        .contains(charText)) {
                    yoItems.add(yo_D);
                }
            }

        notifyDataSetChanged();
    }*/


}
