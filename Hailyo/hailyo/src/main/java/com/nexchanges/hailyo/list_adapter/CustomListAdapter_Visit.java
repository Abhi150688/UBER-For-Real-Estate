package com.nexchanges.hailyo.list_adapter;

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

import java.util.List;

public class CustomListAdapter_Visit extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<VisitData> visitItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomListAdapter_Visit(Activity activity, List<VisitData> visitItems) {
        this.activity = activity;
        this.visitItems = visitItems;
    }

    @Override
    public int getCount() {
        return visitItems.size();
    }

    @Override
    public Object getItem(int location) {
        return visitItems.get(location);
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
        TextView Prop_Count = (TextView) convertView.findViewById(R.id.prop_count);
        //TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView Visit_Date = (TextView) convertView.findViewById(R.id.visitdate);
        TextView Spec_Code = (TextView) convertView.findViewById(R.id.speccode);
        TextView Location = (TextView) convertView.findViewById(R.id.location);
        TextView Dealing_Room = (TextView) convertView.findViewById(R.id.activedealingroom);




        // getting movie data for the row
        VisitData m = visitItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        User_Name.setText(m.getUserName());

        Spec_Code.setText(m.getSpecCode());

        Location.setText(m.getLocation());

        if (m.getDealingRoom().contentEquals("Yes")) {
            Dealing_Room.setText("Dealing Room Initiated");
            Dealing_Room.setTextColor(Color.GREEN);
        }
        else
            Dealing_Room.setText("Dealing Room Not Initiated");
            Dealing_Room.setTextColor(Color.RED);


        // rating
        Prop_Count.setText("Properties Visited: " + String.valueOf(m.getPropsCount()));

        // genre
        /*String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);*/



        // release year
        Visit_Date.setText(String.valueOf(m.getVisitDate()));

        return convertView;
    }

}
