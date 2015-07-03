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
import com.nexchanges.hailyo.model.VisitData;
import com.nexchanges.hailyo.model.YoData;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter_Yo extends BaseAdapter {
    private Activity activity;
    String sm_spec_code,user_type;
    private LayoutInflater inflater;
    private List<YoData> yoItems;
    private ArrayList<YoData> arraylist;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomListAdapter_Yo(Activity activity, List<YoData> yoItems) {
        this.activity = activity;
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
            convertView = inflater.inflate(R.layout.all_visits_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView User_Name = (TextView) convertView.findViewById(R.id.user_name);
        TextView Visit_Count = (TextView) convertView.findViewById(R.id.visit_count);
        TextView Spec_Code = (TextView) convertView.findViewById(R.id.speccode);
        TextView Rating = (TextView) convertView.findViewById(R.id.user_rating);
        TextView User_Type = (TextView) convertView.findViewById(R.id.user_type);




        // getting movie data for the row
        YoData m = yoItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        User_Name.setText(m.getUserName());

        User_Type.setText(m.getUserType());

        Spec_Code.setText(parse(m.getSpecCode()));
        // rating
        Rating.setText("Rating: " + String.valueOf(m.getRating()) + "/5");
        // release year
        Visit_Count.setText("Hails done" + String.valueOf(m.getVisitCount()));

        return convertView;
    }

    public String parse (String spec_code)
    {
      String str = spec_code;
      String parts[]= str.split("-");
      user_type = parts[0];
      String TransactionType = parts[1];
      String Budget = parts[3];
      String Configura = parts[2];
      sm_spec_code = TransactionType + "-" + Configura + "-" + Budget;
      return sm_spec_code;
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
