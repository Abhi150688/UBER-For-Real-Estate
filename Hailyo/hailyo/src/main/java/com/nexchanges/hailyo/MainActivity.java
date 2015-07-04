package com.nexchanges.hailyo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nexchanges.hailyo.custom.MyMarker;
import com.nexchanges.hailyo.model.DealData;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.model.VisitData;

import com.nexchanges.hailyo.ui.CustomListAdapter_Deals;
import com.nexchanges.hailyo.ui.CustomListAdapter_Visit;
import com.nexchanges.hailyo.ui.CustomMapFragment;
import com.nexchanges.hailyo.custom.GetCurrentLocation;
import com.nexchanges.hailyo.custom.GetPlaceName;
import com.nexchanges.hailyo.custom.MapWrapperLayout;
import com.nexchanges.hailyo.custom.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener
{

    //ViewAll Visits

    // Visit json url
   // private static final String url = "http://api.androidhive.info/json/movies.json";
    private static final String url = "https://api.myjson.com/bins/nk0q";
    private ProgressDialog pDialog;
    private List<VisitData> visitList = new ArrayList<VisitData>();
    private ListView listView;
    private CustomListAdapter_Visit adapter;


    //View All Deals

    private static final String urlD = "https://api.myjson.com/bins/3r0d6";
    //private static final String urlD = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog_Deal;
    private List<DealData> dealList = new ArrayList<DealData>();
    private ListView listViewD;
    private CustomListAdapter_Deals adapterD;



    SeekBar sb1;
	private DrawerLayout drawerLayout;
    String []listItems;
    Context context;

    public static final String TAG = MainActivity.class.getSimpleName();

	private ListView drawerLeft;

	private ListView drawerRight;

	private ActionBarDrawerToggle drawerToggle;




    GoogleMap map,map2,map3;
    LinearLayout searchLocation;
    TextView SiteVisitAddressBar, tv1, tv2,tv3,smallname, smallemail;
    ImageButton SetSiteVisitLocation;

    LatLng currentLocation;

    LatLng selectedLocation;
    Location curLoc;
    String selectedLocation_Name;
    String fetchname, fetchemail,fetchphoto;
    ViewFlipper VF10;
    ImageView smallphoto;

    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private static LayoutInflater inflate =null;
    Button hail, deals,visits;
    int flipper_index=0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        sb1 = (SeekBar)findViewById(R.id.seekBar2);
        sb1.setOnSeekBarChangeListener(this);


        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        hail = (Button)findViewById(R.id.hailmode);
        visits = (Button)findViewById(R.id.activevisits);

        deals = (Button)findViewById(R.id.activedeals);



        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);


        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);


        VF10 = (ViewFlipper) findViewById(R.id.ViewFlipper10);
        flipper_index = SharedPrefs.getInt(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);
        VF10.setDisplayedChild(flipper_index);

        switch (VF10.getDisplayedChild()) {
            case 0:
                hail.setBackgroundColor(Color.parseColor("#FFA500"));
                hail.setTextColor(Color.WHITE);
                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);
                break;

            case 1:
                visits.setBackgroundColor(Color.parseColor("#FFA500"));
                deals.setBackgroundResource(R.drawable.button_border);
                hail.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);
                deals.setTextColor(Color.BLACK);
                break;

            case 2:
                deals.setBackgroundColor(Color.parseColor("#FFA500"));
                visits.setBackgroundResource(R.drawable.button_border);
                hail.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);
                visits.setTextColor(Color.BLACK);

                break;

        }


        hail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF10.setDisplayedChild(0);
                hail.setBackgroundColor(Color.parseColor("#FFA500"));
                hail.setTextColor(Color.WHITE);
                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);
                 }
        });



        listViewD = (ListView) findViewById(R.id.dealslist);
        adapterD = new CustomListAdapter_Deals(this, dealList);
        listViewD.setAdapter(adapterD);


        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VF10.setDisplayedChild(1);

                deals.setBackgroundColor(Color.parseColor("#FFA500"));
                deals.setTextColor(Color.WHITE);
                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);


                //Frame 3 - All Deals - Logic



                pDialog_Deal = new ProgressDialog(context);
                // Showing progress dialog before making http request
                pDialog_Deal.setMessage("Fetching Dealing Room Data..");
                pDialog_Deal.show();



                // Creating volley request obj
                JsonArrayRequest dealReq = new JsonArrayRequest(urlD,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                hidePDialogDeal();

                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        DealData deal = new DealData();
                                        deal.setUserName(obj.getString("user_name"));
                                        deal.setThumbnailUrl(obj.getString("image"));
                                        deal.setOfferDate(obj.getString("offer_date"));
                                        deal.setApartmentName(obj.getString("apt_name"));
                                        deal.setRent(obj.getInt("rent_amt"));
                                        deal.setDeposit(obj.getInt("deposit_amt"));
                                        deal.setStartDate(obj.getString("agr_start_date"));



                                        // adding movie to movies array
                                        dealList.add(deal);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapterD.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialogDeal();

                    }
                });

                // Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(dealReq);



                // Frame 3 All Deals
            }
        });

        listView = (ListView) findViewById(R.id.visitlist);
        adapter = new CustomListAdapter_Visit(this, visitList);
        listView.setAdapter(adapter);


        visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF10.setDisplayedChild(2);
                visits.setBackgroundColor(Color.parseColor("#FFA500"));
                visits.setTextColor(Color.WHITE);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);


                //Frame 2 - All Visits - Logic

                pDialog = new ProgressDialog(context);
                // Showing progress dialog before making http request
                pDialog.setMessage("Fetching Site Visit Data..");
                pDialog.show();



                // Creating volley request obj
                JsonArrayRequest visitReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                hidePDialog();

                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        VisitData visit = new VisitData();
                                        visit.setUserName(obj.getString("user_name"));
                                        visit.setThumbnailUrl(obj.getString("image"));
                                        visit.setPropsCount(obj.getInt("prop_count"));
                                        visit.setVisitDate(obj.getString("visit_date"));
                                        visit.setLocation(obj.getString("location"));
                                        visit.setSpecCode(obj.getString("spec_code"));
                                        visit.setDealingRoom(obj.getString("dealing_room_status"));

                                        // adding movie to movies array
                                        visitList.add(visit);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();

                    }
                });

                // Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(visitReq);



                // Frame 2 All Visits



            }
        });



        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivity=new Intent(context, SearchActivity.class);
                searchActivity.putExtra("nearLocation", currentLocation);
                startActivityForResult(searchActivity, 1);
            }
        });


        SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY,SiteVisitAddressBar.getText().toString());


        //Nav Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout12);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        listItems = getResources().getStringArray(R.array.listItems);
        drawerLeft = (ListView) findViewById(R.id.left_drawer);

        drawerLeft.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.home_icon, R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawers();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



       inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflate.inflate(R.layout.left_nav_header,null);
        drawerLeft.addHeaderView(vi);

       smallname =  (TextView)vi.findViewById(R.id.mynamesmall);

       smallemail =  (TextView)vi.findViewById(R.id.myemailsmall);

        smallphoto = (ImageView)vi.findViewById(R.id.smallphoto);


        fetchname = SharedPrefs.getString(this, SharedPrefs.NAME_KEY, "No_Name");

        fetchemail = SharedPrefs.getString(this, SharedPrefs.EMAIL_KEY, "No_Email");

        fetchphoto = SharedPrefs.getString(this, SharedPrefs.PHOTO_KEY);



        smallname.setText(fetchname);

        smallemail.setText(fetchemail);

        smallphoto.setImageBitmap(BitmapFactory.decodeFile(fetchphoto));


        drawerLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawerLayout.closeDrawer(drawerLeft);


                switch(position){

                    case 1:
                        Intent selectPaymentAct = new Intent(context, SelectPaymentTypeActivity.class);
                        startActivity(selectPaymentAct);
                        break;

                    case 2:
                    Intent helpAct = new Intent(context, HelpActivity.class);
                    startActivity(helpAct);
                    break;

                    case 3:
                    Intent promotionsAct = new Intent(context, PromotionsActivity.class);
                    startActivity(promotionsAct);
                        break;

                    case 4:
                    Intent aboutAct = new Intent(context, AboutActivity.class);
                    startActivity(aboutAct);
                        break;

                    case 5:
                    Intent settingsAct = new Intent(context, SettingsActivity.class);
                    startActivity(settingsAct);
                        break;

                    default:
                        break;
                }


            }
        });

        // Google Map ..

        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap = new HashMap<Marker, MyMarker>();

        mMyMarkersArray.add(new MyMarker("3BHK-75K", "icon1", Double.parseDouble("19.116612"), Double.parseDouble("72.910285")));
        mMyMarkersArray.add(new MyMarker("2BHK-50K", "icon2", Double.parseDouble("19.114427"), Double.parseDouble("72.911102")));
        mMyMarkersArray.add(new MyMarker("4BHK-2Lac", "icon3", Double.parseDouble("19.117774"), Double.parseDouble("72.9076828")));
        mMyMarkersArray.add(new MyMarker("4BHK-2.5Lac", "icon4", Double.parseDouble("19.1148607"), Double.parseDouble("72.8999415")));


//Map Fragment 1

        CustomMapFragment customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

                customMapFragment.getMapAsync(new OnMapReadyCallback() {
                                                  @Override
                                                  public void onMapReady(GoogleMap googleMap) {
                                                      map = googleMap;
                                                      map.setMyLocationEnabled(true);


                                                      //plotMarkers(mMyMarkersArray);
                                                  }
                                              });

        SetSiteVisitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent EnterConfigActivity = new Intent(context, EnterConfigActivity.class);
                    startActivity(EnterConfigActivity);
                    SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY,SiteVisitAddressBar.getText().toString());

            }
        });





        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                @Override
                public void onDrag(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        SiteVisitAddressBar.setText("Fetching...");
                    } else {
                        selectedLocation = map.getCameraPosition().target;
                        selectedLocation_Name = "Lat: " + selectedLocation.latitude + ", Lng: " + selectedLocation.longitude;
                        getPlaceName(selectedLocation);
                    }
                }
            });




// Current Location ..

        new GetCurrentLocation(context, new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(15));

                    getPlaceName(currentLocation);


                }
            }
        });

    }


    private void plotMarkers(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));

                Marker currentMarker = map.addMarker(markerOption);

                mMarkersHashMap.put(currentMarker, myMarker);


            }
        }
    }

    private void plotMarkers2(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));

                Marker currentMarker = map.addMarker(markerOption);

                mMarkersHashMap.put(currentMarker, myMarker);


            }
        }
    }

    private void plotMarkers3(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon1));

                Marker currentMarker = map.addMarker(markerOption);

                mMarkersHashMap.put(currentMarker, myMarker);


            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromTouch) {

                if (progress == 0) {
                    map.clear();
                    plotMarkers(mMyMarkersArray);
                    tv1.setTextColor(Color.RED);
                    tv2.setTextColor(Color.BLACK);
                    tv3.setTextColor(Color.BLACK);

                } else if (progress == 50) {

                    map.clear();
                    plotMarkers2(mMyMarkersArray);

                    tv1.setTextColor(Color.BLACK);
                    tv2.setTextColor(Color.RED);
                    tv3.setTextColor(Color.BLACK);

                } else if (progress == 100) {
                    map.clear();
                    plotMarkers3(mMyMarkersArray);

                    tv1.setTextColor(Color.BLACK);
                    tv2.setTextColor(Color.BLACK);
                    tv3.setTextColor(Color.RED);
                }


            }





    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
         }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

                int mProgress = seekBar.getProgress();
                if (mProgress > 0 & mProgress < 34) {
                    seekBar.setProgress(0);
                } else if (mProgress > 33 & mProgress < 68) {
                    seekBar.setProgress(50);
                } else seekBar.setProgress(100);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == 1 ){
            try {
                LatLng placeLatLng = data.getParcelableExtra("placeLatLng");
                String placeName = data.getStringExtra("placeName");
                if ( placeLatLng != null && placeName != null) {
                    selectedLocation = placeLatLng;
                    selectedLocation_Name = placeName;

                    SiteVisitAddressBar.setText(selectedLocation_Name);

                    map.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(15));



                }
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public void getPlaceName(LatLng location){
        new GetPlaceName(location, new GetPlaceName.GetPlaceNameCallback() {
            @Override
            public void onStart() {
                SiteVisitAddressBar.setText("Fetching Site Visit Location, wait..");
            }

            @Override
            public void onComplete(boolean result, LatLng location, String placeName) {
                if ( result == true ) {
                  SiteVisitAddressBar.setText(placeName);
                }else{
                    SiteVisitAddressBar.setText("Sorry, No Such Location, Please Try Again..");
                }
            }
        });
    }





	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return super.onKeyDown(keyCode, event);
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( drawerToggle.onOptionsItemSelected(item) ) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void hidePDialogDeal() {
        if (pDialog_Deal != null) {
            pDialog_Deal.dismiss();
            pDialog_Deal = null;
        }
    }



}

