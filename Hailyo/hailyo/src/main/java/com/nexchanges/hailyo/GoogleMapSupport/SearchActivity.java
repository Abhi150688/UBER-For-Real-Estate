package com.nexchanges.hailyo.GoogleMapSupport;

/**
 * Created by Abhishek on 08/05/15.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.nexchanges.hailyo.GoogleMapSupport.GetAutoPredictions;
import com.nexchanges.hailyo.GoogleMapSupport.GetPlaceDetails;
import com.nexchanges.hailyo.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class SearchActivity extends ActionBarActivity {

    Context context;
    EditText searchLocation;
    LinearLayout searchLayout;

    ImageView clearSearch;

    String searchName;

    LatLng nearLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        context=this;

        // Get Near Location from Intent ...

        Intent inputData = getIntent();
        nearLocation = inputData.getParcelableExtra("nearLocation");
        if ( nearLocation == null ){    nearLocation = new LatLng(17.3700, 78.4800);    }

        //...

        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchLocation = (EditText) findViewById(R.id.searchLocation);

        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final String inputText = searchLocation.getText().toString();

                if ( inputText != null && inputText.length() > 1 ) {

                    searchName = inputText;

                    new GetAutoPredictions(searchName, nearLocation, getResources().getString(R.string.api_key), new GetAutoPredictions.GetAutoPredictionsCallback() {
                        @Override
                        public void onStart() {

                            searchLayout.removeAllViews();

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 15;
                            params.leftMargin = 15;
                            TextView searching = new TextView(context);
                            searching.setLayoutParams(params);
                            searching.setText("Searching , wait...");
                            searchLayout.addView(searching);

                        }

                        @Override
                        public void onComplete(boolean status, String requestedSearchName, JSONArray predictions) {
                            if ( status == true ) {
                                if (requestedSearchName.equalsIgnoreCase(searchName)) {
                                    if (predictions != null) {
                                        showPredictions(predictions);
                                    }else{
                                        searchLayout.removeAllViews();
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        clearSearch = (ImageView) findViewById(R.id.clearSearch);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation.setText("");
                searchLayout.removeAllViews();
            }
        });
    }

    public Button cButton(String text){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 2;params.leftMargin = 2;
        Button button = new Button(context);
        button.setLayoutParams(params);
        button.setText(text);
        return button;
    }

    public LinearLayout cLayout(String text){

        String []split_text = text.split(",", 2);

        LinearLayout holder = new LinearLayout(context);
        holder.setBackground(getResources().getDrawable(R.drawable.custom_border));
        holder.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 135);
        params.topMargin = 1;
        holder.setLayoutParams(params);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.topMargin = 22;params1.leftMargin = 60;
        TextView address1 = new TextView(context);
        address1.setLayoutParams(params1);
        address1.setText(split_text[0]);
        address1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        address1.setTextColor(Color.BLACK);
        holder.addView(address1);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
        params2.leftMargin = 60;
        TextView address2 = new TextView(context);
        address2.setLayoutParams(params2);
        address2.setText(split_text[1]);
        //address2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        //address2.setTextColor(Color.BLACK);
        holder.addView(address2);



        return holder;
    }

    public void showPredictions(JSONArray predictions) {

        searchLayout.removeAllViews();

        Toast.makeText(getApplicationContext(), "Found : " + predictions.length(), Toast.LENGTH_SHORT).show();

        int found = predictions.length();

        if (found > 0) {
            for (int i = 0; i < found; i++) {
                try {
                    final JSONObject addressJSON = predictions.getJSONObject(i);

                    if (addressJSON != null) {

                        final String placeDescription = (String) addressJSON.get("description");

                        if (placeDescription != null && placeDescription.length() > 0) {

                            //Button placeName = cButton(placeDescription);
                            LinearLayout placeName = cLayout(placeDescription);
                            searchLayout.addView(placeName);

                            placeName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String placeID = "";
                                    try {
                                        placeID = (String) addressJSON.get("place_id");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (placeID != null && placeID.length() > 0) {
                                        new GetPlaceDetails(placeID, getResources().getString(R.string.api_key), new GetPlaceDetails.GetPlaceDetailsCallback() {
                                            @Override
                                            public void onStart() {
                                            }

                                            @Override
                                            public void onComplete(JSONObject placeDetails) {
                                                try {
                                                    JSONObject geometry = placeDetails.getJSONObject("geometry");
                                                    if (geometry != null) {
                                                        JSONObject coordinates = geometry.getJSONObject("location");
                                                        if (coordinates != null) {
                                                            String lat = coordinates.getString("lat");
                                                            String lng = coordinates.getString("lng");

                                                            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                                            Intent placeData = new Intent();
                                                            placeData.putExtra("placeLatLng", latLng);
                                                            placeData.putExtra("placeName", placeDescription);
                                                            setResult(1, placeData);
                                                            finish();//finishing activity
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            LinearLayout noResults = cLayout("No Results Found, for query \""+searchName+"\"");
            searchLayout.addView(noResults);

        }
    }
}