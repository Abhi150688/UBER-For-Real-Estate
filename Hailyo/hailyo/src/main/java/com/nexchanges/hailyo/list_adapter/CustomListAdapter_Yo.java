package com.nexchanges.hailyo.list_adapter;

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
import com.nexchanges.hailyo.PostYoActivity_Broker;
import com.nexchanges.hailyo.R;
import com.nexchanges.hailyo.model.YoData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter_Yo extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<YoData> yoItems;
    private ArrayList<YoData> arraylist;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    String U_type;
    String rate;
    YoData m;
    String timer_val;

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

    public static class Holder {
      TextView U_Name;
        TextView V_Cnt;
        TextView Spec;
        TextView U_Rate;
        NetworkImageView image;
        TextView U_Type;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.all_yo_row1, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();


        holder.U_Name = (TextView) convertView.findViewById(R.id.user_name);
        holder.V_Cnt = (TextView) convertView.findViewById(R.id.visit_count);;
        holder.Spec = (TextView) convertView.findViewById(R.id.speccode);
        holder.U_Rate = (TextView) convertView.findViewById(R.id.user_rating);
        holder.image = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        holder.U_Type = (TextView) convertView.findViewById(R.id.user_type);

        m = yoItems.get(position);

        holder.image.setImageUrl(m.getThumbnailUrl(), imageLoader);

        holder.U_Name.setText(m.getUserName());

        if (m.getUserType().equalsIgnoreCase("broker"))

            holder.U_Type.setText("Plus Deal\n +");

        else

        holder.U_Type.setText("Direct Deal\n D");


        holder.Spec.setText(m.getSpecCode());
        holder.Spec.setBackgroundColor(Color.parseColor("#FFA500"));
        holder.Spec.setTextColor(Color.WHITE);

   //       holder.U_Type.setText("Plus Deal: \n" + String.valueOf(m.getRating()) + "/5");
        holder.V_Cnt.setText("Hail Count: \n" + String.valueOf(m.getVisitCount()));

        return convertView;
    }

}
