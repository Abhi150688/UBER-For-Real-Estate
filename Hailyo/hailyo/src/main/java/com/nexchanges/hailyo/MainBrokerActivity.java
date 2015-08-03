package com.nexchanges.hailyo;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.nexchanges.hailyo.DrawerClass.AboutActivity;
import com.nexchanges.hailyo.DrawerClass.HelpActivity;
import com.nexchanges.hailyo.DrawerClass.ProfileActivity;
import com.nexchanges.hailyo.customSupportClass.CheckLocationServices;
import com.nexchanges.hailyo.customSupportClass.MyMarker;
import com.nexchanges.hailyo.apiSupport.PlotMyNeighboursHail;
import com.nexchanges.hailyo.apiSupport.SendLocationUpdate;
import com.nexchanges.hailyo.customSupportClass.YoPopup;
import com.nexchanges.hailyo.gcm.GcmMessageHandler;
import com.nexchanges.hailyo.list_adapter.NavDrawerListAdapter;
import com.nexchanges.hailyo.model.DealData;
import com.nexchanges.hailyo.model.NavDrawerItem;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.model.VisitData;
import com.nexchanges.hailyo.model.YoData;
import com.nexchanges.hailyo.list_adapter.CustomListAdapter_Deals;
import com.nexchanges.hailyo.list_adapter.CustomListAdapter_Visit;
import com.nexchanges.hailyo.list_adapter.CustomListAdapter_Yo;
import com.nexchanges.hailyo.DrawerClass.SelectPaymentTypeActivity;
import com.nexchanges.hailyo.ui.CustomMapFragment;
import com.nexchanges.hailyo.GoogleMapSupport.GetCurrentLocation;
import com.nexchanges.hailyo.GoogleMapSupport.GetPlaceName;
import com.nexchanges.hailyo.customSupportClass.MapWrapperLayout;
import com.nexchanges.hailyo.GoogleMapSupport.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AbhishekWork on 23/06/15.
 */

/**
 * The Activity MainActivity will launched at the start of the app.
 */public class MainBrokerActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {

    PlotMyNeighboursHail plotMyNeighboursHail = new PlotMyNeighboursHail();
    CheckLocationServices checkLocationServices = new CheckLocationServices();
    private static final String TAG = MainBrokerActivity.class.getSimpleName();
    SendLocationUpdate sendLocationUpdate = new SendLocationUpdate();
    Boolean location_read = false, avail_press = false, req_press = true;

    private static final String url = "https://api.myjson.com/bins/nk0q";
    private List<VisitData> visitList = new ArrayList<VisitData>();
    private ListView listView;
    private CustomListAdapter_Visit adapter;
    LocationManager mLocationManager;
    String type_user,is_transaction,Str_Lng,Str_Lat;
    BroadcastReceiver ReceivefromGCM;

    private static final String urlD = "https://api.myjson.com/bins/3r0d6";
    private ProgressDialog pDialog_Deal;
    private List<DealData> dealList = new ArrayList<DealData>();
    private ListView listViewD;
    private CustomListAdapter_Deals adapterD;

    private static final String urlYo = "https://api.myjson.com/bins/1wke2";
    private ProgressDialog pDialog_Yo;
    private List<YoData> yoList = new ArrayList<YoData>();
    //private ListView listViewYo;
    private GridView mGridView;
    private CustomListAdapter_Yo adapterYo;

    private DrawerLayout drawerLayout;

    String[] listItems;
    Context context;
    private ListView drawerLeft;
    private ActionBarDrawerToggle drawerToggle;

    GoogleMap map_hail;
    LinearLayout searchLocation;
    TextView SiteVisitAddressBar, smallname, smallemail, textview1, textview2;
    ImageButton SetSiteVisitLocation;
    SwipeRefreshLayout yo_refresh, visit_refresh, deal_refresh;

    LatLng currentLocation, selectedLocation;
    String selectedLocation_Name, my_user_id,my_role;
    ViewFlipper VF;
    String fetchname, fetchemail, fetchphoto;
    Button yo, hail, deals,visits,availability, requirement;
    ImageView smallphoto;
    IntentFilter Intentfilter;

    private ArrayList<MyMarker> mMyMarkersArray_Hail = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap_hail;
    int flipper_index=0;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter navadapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_main);
        context = this;

        my_role = SharedPrefs.getString(context,SharedPrefs.MY_ROLE_KEY);
        my_user_id = SharedPrefs.getString(context,SharedPrefs.MY_USER_ID);

        is_transaction = SharedPrefs.getString(context, SharedPrefs.SUCCESSFUL_HAIL);
        checkLocationServices.checkGpsStatus(context);

        yo_refresh = (SwipeRefreshLayout)findViewById(R.id.yo_refresh);
        visit_refresh = (SwipeRefreshLayout)findViewById(R.id.visit_refresh);
        deal_refresh = (SwipeRefreshLayout)findViewById(R.id.deal_refresh);

        yo_refresh.setOnRefreshListener(this);

        visit_refresh.setOnRefreshListener(this);

        deal_refresh.setOnRefreshListener(this);
        deal_refresh.setColorScheme(
                R.color.red, R.color.darkturquoise,
                R.color.green, R.color.blue);

        visit_refresh.setColorScheme(
                R.color.red, R.color.darkturquoise,
                R.color.green, R.color.blue);

        yo_refresh.setColorScheme(
                R.color.red, R.color.darkturquoise,
                R.color.green, R.color.blue);



        textview1 = (TextView) findViewById(R.id.textView1);

        textview2 = (TextView) findViewById(R.id.textView2);

        yo = (Button) findViewById(R.id.yomode);

        visits = (Button) findViewById(R.id.activevisits);

        availability = (Button) findViewById(R.id.availability);
        requirement = (Button) findViewById(R.id.requirement);


        deals = (Button) findViewById(R.id.activedeals);

        hail = (Button) findViewById(R.id.hailmode);

        VF = (ViewFlipper) findViewById(R.id.ViewFlipper10);

        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);
        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);

        //All Visits Frame 2
        listView = (ListView) findViewById(R.id.visitlist);
        adapter = new CustomListAdapter_Visit(this, visitList);
        listView.setAdapter(adapter);

        listViewD = (ListView) findViewById(R.id.dealslist);
        adapterD = new CustomListAdapter_Deals(this, dealList);
        listViewD.setAdapter(adapterD);

        mGridView = (GridView) findViewById(R.id.yo_grid);

        adapterYo = new CustomListAdapter_Yo(this, yoList);
        mGridView.setAdapter(adapterYo);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                YoData yo_item = (YoData) parent.getItemAtPosition(position);
                final String spec = yo_item.getSpecCode();
                final String U_tp = yo_item.getUserType();
                YoPopup yoPopup = new YoPopup();
                yoPopup.inflateYo(context,spec,U_tp);

            }
        });




        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,10
                ,mLocationListener);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000,10
                ,mLocationListener);

        //Yo Data

        flipper_index = SharedPrefs.getInt(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);
        VF.setDisplayedChild(flipper_index);

        switch (VF.getDisplayedChild()) {
            case 0:
                yo.setTextColor(Color.WHITE);
                yo.setBackgroundColor(Color.parseColor("#FFA500"));

                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);
                refreshYo("Req");
                break;
            case 1:
                visits.setBackgroundColor(Color.parseColor("#FFA500"));
                visits.setTextColor(Color.WHITE);
                yo.setBackgroundResource(R.drawable.button_border);
                yo.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);
                refresh_visit();
                break;

            case 2:
                deals.setBackgroundColor(Color.parseColor("#FFA500"));
                deals.setTextColor(Color.WHITE);
                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                yo.setBackgroundResource(R.drawable.button_border);
                yo.setTextColor(Color.BLACK);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);
                refresh_deal();
                break;

            case 3:
                hail.setBackgroundColor(Color.parseColor("#FFA500"));
                hail.setTextColor(Color.WHITE);
                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);
                yo.setBackgroundResource(R.drawable.button_border);
                yo.setTextColor(Color.BLACK);
                mMyMarkersArray_Hail = plotMyNeighboursHail.markerpos(my_user_id,Str_Lng,Str_Lat,"broker",my_role);
                plotHailMarkers(mMyMarkersArray_Hail);

                break;
        }


        SetSiteVisitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY, SiteVisitAddressBar.getText().toString());
                if (location_read == true) {
                    Intent EnterConfigActivity = new Intent(context, EnterConfigActivity.class);
                    startActivity(EnterConfigActivity);
                } else {
                    Toast.makeText(getApplicationContext(), "Your Location is not available \n Please try again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listView != null && listView.getChildCount() > 0) {

                    if ((firstVisibleItem == 0) && (listView.getChildAt(0).getTop() == 0))
                        visit_refresh.setEnabled(true);
                    else
                        visit_refresh.setEnabled(false);
                }
            }
        });

        listViewD.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(listViewD != null && listViewD.getChildCount() > 0)
                {

                if ((firstVisibleItem ==0) && (listViewD.getChildAt(0).getTop()==0))
                    deal_refresh.setEnabled(true);
                    else
                    deal_refresh.setEnabled(false);
            }
         }

        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mGridView != null && mGridView.getChildCount() > 0)
                {

                    if ((firstVisibleItem ==0) && (mGridView.getChildAt(0).getTop()==0))
                        yo_refresh.setEnabled(true);
                    else
                        yo_refresh.setEnabled(false);
                }
            }
        });

        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivity = new Intent(context, SearchActivity.class);
                searchActivity.putExtra("nearLocation", currentLocation);
                startActivityForResult(searchActivity, 1);
            }
        });



        yo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_transaction.equalsIgnoreCase("true")) {
                    Intent PostYoActivityBroker = new Intent(context, PostYoActivity_Broker.class);
                    startActivity(PostYoActivityBroker);
                } else {
                    VF.setDisplayedChild(0);

                    yo.setBackgroundColor(Color.parseColor("#FFA500"));
                    yo.setTextColor(Color.WHITE);
                    visits.setBackgroundResource(R.drawable.button_border);
                    visits.setTextColor(Color.BLACK);
                    hail.setBackgroundResource(R.drawable.button_border);
                    hail.setTextColor(Color.BLACK);
                    deals.setBackgroundResource(R.drawable.button_border);
                    deals.setTextColor(Color.BLACK);

                    yo_refresh.setRefreshing(true);
                    refreshYo("Req");

                }
            }
        });

        hail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(is_transaction.equalsIgnoreCase("true"))
                {
                    Intent PostYoActivityBroker=new Intent(context, PostYoActivity_Broker.class);
                    startActivity(PostYoActivityBroker);}

                else {

                    VF.setDisplayedChild(3);
                    hail.setBackgroundColor(Color.parseColor("#FFA500"));
                    hail.setTextColor(Color.WHITE);
                    visits.setBackgroundResource(R.drawable.button_border);
                    visits.setTextColor(Color.BLACK);
                    yo.setBackgroundResource(R.drawable.button_border);
                    yo.setTextColor(Color.BLACK);
                    deals.setBackgroundResource(R.drawable.button_border);
                    deals.setTextColor(Color.BLACK);
                    mMyMarkersArray_Hail = plotMyNeighboursHail.markerpos(my_user_id,Str_Lng,Str_Lat,"broker",my_role);
                  plotHailMarkers(mMyMarkersArray_Hail);


                }
            }
        });

        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(2);
                deals.setBackgroundColor(Color.parseColor("#FFA500"));
                deals.setTextColor(Color.WHITE);
                visits.setBackgroundResource(R.drawable.button_border);
                visits.setTextColor(Color.BLACK);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);
                yo.setBackgroundResource(R.drawable.button_border);
                yo.setTextColor(Color.BLACK);

                deal_refresh.setRefreshing(true);
                refresh_deal();


            }
        });


        visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(1);
                visits.setBackgroundColor(Color.parseColor("#FFA500"));
                visits.setTextColor(Color.WHITE);
                yo.setBackgroundResource(R.drawable.button_border);
                yo.setTextColor(Color.BLACK);
                hail.setBackgroundResource(R.drawable.button_border);
                hail.setTextColor(Color.BLACK);
                deals.setBackgroundResource(R.drawable.button_border);
                deals.setTextColor(Color.BLACK);

                visit_refresh.setRefreshing(true);
                refresh_visit();

            }
        });


        requirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                refreshYo("Req");
                req_press = true;
                avail_press=false;
                requirement.setBackgroundColor(Color.BLACK);
                requirement.setTextColor(Color.WHITE);
                availability.setBackgroundColor(Color.WHITE);
                availability.setTextColor(Color.BLACK);
            }
        });

        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshYo("Avl");
                avail_press = true;
                req_press = false;
                availability.setBackgroundColor(Color.BLACK);
                availability.setTextColor(Color.WHITE);
                requirement.setBackgroundColor(Color.WHITE);
                requirement.setTextColor(Color.BLACK);
            }
        });


        Intentfilter = new IntentFilter(GcmMessageHandler.HANDLEGCM);
        ReceivefromGCM = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {

            }
        };
        registerReceiver(ReceivefromGCM, Intentfilter);


        //Nav Drawer

        // listItems = getResources().getStringArray(R.array.listItems);
        navMenuTitles = getResources().getStringArray(R.array.listItems);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout12);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        drawerLeft = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        navMenuIcons.recycle();

        navadapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
       drawerLeft.setAdapter(navadapter);

        //  drawerLeft.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems));

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

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflate.inflate(R.layout.left_nav_header, null);
        drawerLeft.addHeaderView(vi);

        smallname = (TextView) vi.findViewById(R.id.mynamesmall);

        smallemail = (TextView) vi.findViewById(R.id.myemailsmall);

        smallphoto = (ImageView) vi.findViewById(R.id.smallphoto);


        fetchname = SharedPrefs.getString(this, SharedPrefs.NAME_KEY, "No_Name");

        fetchemail = SharedPrefs.getString(this, SharedPrefs.EMAIL_KEY, "No_Email");

        fetchphoto = SharedPrefs.getString(this, SharedPrefs.PHOTO_KEY);


        smallname.setText(fetchname);
        smallemail.setText(fetchemail);
        smallemail.setTextColor(Color.BLACK);
        smallname.setTextColor(Color.BLACK);

        //smallphoto.setImageBitmap(BitmapFactory.decodeFile(fetchphoto));


        drawerLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawerLayout.closeDrawer(drawerLeft);


                switch (position) {

                    case 1:
                        Intent selectPaymentAct = new Intent(context, SelectPaymentTypeActivity.class);
                        startActivity(selectPaymentAct);
                        break;

                    case 2:
                        Intent profileAct = new Intent(context, ProfileActivity.class);
                        startActivity(profileAct);
                        break;

                    case 3:
                        Intent helpAct = new Intent(context, HelpActivity.class);
                        startActivity(helpAct);
                        break;

                    case 4:
                        Intent aboutAct = new Intent(context, AboutActivity.class);
                        startActivity(aboutAct);
                        break;


                    default:
                        break;
                }


            }
        });

        mMarkersHashMap_hail = new HashMap<Marker, MyMarker>();

        CustomMapFragment customMapFragment_Hail = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        customMapFragment_Hail.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map_hail = googleMap;
                map_hail.setMyLocationEnabled(true);

                mMyMarkersArray_Hail = plotMyNeighboursHail.markerpos(my_user_id,Str_Lng,Str_Lat,"broker",my_role);
              plotHailMarkers(mMyMarkersArray_Hail);

            }
        });


        customMapFragment_Hail.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    SiteVisitAddressBar.setText("Fetching...");
                } else {
                    selectedLocation = map_hail.getCameraPosition().target;
                    selectedLocation_Name = "Lat: " + selectedLocation.latitude + ", Lng: " + selectedLocation.longitude;
                    getPlaceName(selectedLocation);
                }
            }
        });

        new GetCurrentLocation(context, new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map_hail.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map_hail.animateCamera(CameraUpdateFactory.zoomTo(15));

                    getPlaceName(currentLocation);


                }
            }
        });

    }

    private void plotHailMarkers(ArrayList<MyMarker> markers) {
        if (markers!=null)
            {

        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude())).title(myMarker.getmLabel());
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.req_icon1));
                map_hail.clear();
                Marker currentMarker = map_hail.addMarker(markerOption);
                mMarkersHashMap_hail.put(currentMarker, myMarker);

            }
        }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            try {
                LatLng placeLatLng = data.getParcelableExtra("placeLatLng");
                String placeName = data.getStringExtra("placeName");
                if (placeLatLng != null && placeName != null) {
                    selectedLocation = placeLatLng;
                    selectedLocation_Name = placeName;

                    SiteVisitAddressBar.setText(selectedLocation_Name);

                    map_hail.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
                    map_hail.animateCamera(CameraUpdateFactory.zoomTo(15));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void getPlaceName(LatLng location) {
        new GetPlaceName(location, new GetPlaceName.GetPlaceNameCallback() {
            @Override
            public void onStart() {
                SiteVisitAddressBar.setText("Fetching Site Visit Location, wait..");
                location_read=false;
            }

            @Override
            public void onComplete(boolean result, LatLng location, String placeName) {
                if (result == true) {
                    SiteVisitAddressBar.setText(placeName);
                    SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY, SiteVisitAddressBar.getText().toString());
                    location_read=true;

                } else {
                    SiteVisitAddressBar.setText("Sorry, No Such Location, Please Try Again..");
                    location_read=false;
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //hidePDialog();
    }



    private void refreshYo(final String User_Type)
    {

        yo_refresh.setRefreshing(true);
        JsonArrayRequest yoReq = new JsonArrayRequest(urlYo,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        yo_refresh.setRefreshing(false);
                        yoList.clear();



                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                type_user = obj.getString("user_type");
                                if (type_user.equalsIgnoreCase(User_Type)) {
                                    YoData yo = new YoData();
                                    yo.setUserName(obj.getString("user_name"));
                                    yo.setThumbnailUrl(obj.getString("image"));
                                    yo.setSpecCode(obj.getString("spec_code"));
                                    yo.setVisitCount(obj.getInt("visits_done"));
                                    yo.setRating(obj.getInt("rating"));
                                    yo.setUserType(obj.getString("user_type"));
                                    yoList.add(yo);
                                }
                                // adding movie to movies array


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterYo.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                yo_refresh.setRefreshing(false);


            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(yoReq);


    }

    public void refresh_visit()
    {


        visit_refresh.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest visitReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        visit_refresh.setRefreshing(false);

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

                visit_refresh.setRefreshing(false);

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(visitReq);


    }

    public void refresh_deal()
    {

        deal_refresh.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest dealReq = new JsonArrayRequest(urlD,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        deal_refresh.setRefreshing(false);


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

                deal_refresh.setRefreshing(false);


            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(dealReq);

    }

    @Override
    public void onRefresh(){

        int index = VF.getDisplayedChild();
        if(index == 0){

            yo_refresh.setRefreshing(true);
            if (req_press==true)
            {
                refreshYo("Req");}
            else if (avail_press==true)
            {
                refreshYo("Avl");
            }
        }

        else if (index == 1)
        {deal_refresh.setRefreshing(true);
            refresh_deal();}

        else if (index ==2)
        {
            visit_refresh.setRefreshing(true);
            refresh_visit();}

    }

   private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            findMyLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void findMyLocation(final Location location) {

        class TestAsync extends AsyncTask<String, Void, Void> {

            protected Void doInBackground(String... params) {

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                Str_Lng = String.valueOf(lng);
                Str_Lat = String.valueOf(lat);
                Log.i(TAG,"Value of Lat is"  + Str_Lat);
                Log.i(TAG,"Value of Long is"  + Str_Lng);

                SharedPrefs.save(context,SharedPrefs.MY_CUR_LAT,Str_Lat);
                SharedPrefs.save(context,SharedPrefs.MY_CUR_LNG,Str_Lng);

                sendLocationUpdate.sendPostRequest(my_user_id,Str_Lat,Str_Lng,my_role );
                return null;
            }
        }
        TestAsync TestAsync = new TestAsync();
        TestAsync.execute();
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());

        try {
            unregisterReceiver(ReceivefromGCM);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                Log.i("TAG","Tried to unregister the reciver when it's not registered");
            }
            else
            {
                throw e;
            }
        }
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(ReceivefromGCM, Intentfilter);
        //the first parameter is the name of the inner class we created.
    }
}


