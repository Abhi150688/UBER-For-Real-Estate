package com.nexchanges.hailyo;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.LatLng;
import com.nexchanges.hailyo.DrawerClass.AboutActivity;
import com.nexchanges.hailyo.DrawerClass.HelpActivity;
import com.nexchanges.hailyo.DrawerClass.ProfileActivity;
import com.nexchanges.hailyo.GoogleMapSupport.ConnectOnMaps;
import com.nexchanges.hailyo.customSupportClass.CheckLocationServices;
import com.nexchanges.hailyo.apiSupport.PlotMyNeighboursHail;
import com.nexchanges.hailyo.apiSupport.SendLocationUpdate;
import com.nexchanges.hailyo.gcm.GcmMessageHandler;
import com.nexchanges.hailyo.gcm.LocationServices;
import com.nexchanges.hailyo.list_adapter.NavDrawerListAdapter;
import com.nexchanges.hailyo.model.DealData;
import com.nexchanges.hailyo.model.NavDrawerItem;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.model.VisitData;

import com.nexchanges.hailyo.list_adapter.CustomListAdapter_Deals;
import com.nexchanges.hailyo.list_adapter.CustomListAdapter_Visit;
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
import java.util.List;

public class MainActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener
{

    PlotMyNeighboursHail plotMyNeighboursHail = new PlotMyNeighboursHail();
    CheckLocationServices checkLocationServices = new CheckLocationServices();
    double p_lat, p_lng,lat,lng;
    LatLng ll;

    private static final String url = "https://api.myjson.com/bins/nk0q";
    private ProgressDialog pDialog;
    private List<VisitData> visitList = new ArrayList<VisitData>();
    private ListView listView;
    private CustomListAdapter_Visit adapter;
    Boolean location_read=false, broker_press = true, auction_press = false,builder_press = false;
    SwipeRefreshLayout visit_refresh, deal_refresh;
    SendLocationUpdate sendLocationUpdate = new SendLocationUpdate();



    private static final String urlD = "https://api.myjson.com/bins/3r0d6";
    private ProgressDialog pDialog_Deal;
    private List<DealData> dealList = new ArrayList<DealData>();
    private ListView listViewD;
    private CustomListAdapter_Deals adapterD;
    BroadcastReceiver ReceivefromGCM;
    IntentFilter Intentfilter;

    private DrawerLayout drawerLayout;
    String []listItems;
    Context context;

    public static final String TAG = MainActivity.class.getSimpleName();

    private ListView drawerLeft;

    private ActionBarDrawerToggle drawerToggle;

    GoogleMap map;
    LinearLayout searchLocation;
    TextView SiteVisitAddressBar, tv1, tv2,tv3,smallname, smallemail;
    ImageButton SetSiteVisitLocation,mapmyloc;
    LatLng currentLocation;
    LatLng selectedLocation;
    String selectedLocation_Name,is_transaction, my_role,my_user_id, which_type="broker",Str_Lat,Str_Lng, fetchname, fetchemail,fetchphoto,pointer_lat, pointer_lng;
    ViewFlipper VF10;
    ImageView smallphoto;
    LocationManager mLocationManager;

    private static LayoutInflater inflate =null;
    Button hail, deals,visits,broker,auction,builder;
    int flipper_index=0;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter navadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        checkLocationServices.checkGpsStatus(context);
        is_transaction = SharedPrefs.getString(context, SharedPrefs.SUCCESSFUL_HAIL);
        my_role = SharedPrefs.getString(context,SharedPrefs.MY_ROLE_KEY);
        my_user_id = SharedPrefs.getString(context,SharedPrefs.MY_USER_ID);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 150, mLocationListener);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000,150
                ,mLocationListener);


            enableLocationDialog();


        visit_refresh = (SwipeRefreshLayout)findViewById(R.id.visit_refresh);
        SharedPrefs.save(context, SharedPrefs.LAST_ACTIVITY_KEY, getClass().getName());

        deal_refresh = (SwipeRefreshLayout)findViewById(R.id.deal_refresh);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        hail = (Button)findViewById(R.id.hailmode);
        visits = (Button)findViewById(R.id.activevisits);
        mapmyloc= (ImageButton)findViewById(R.id.mapmylocation);
        broker = (Button)findViewById(R.id.broker_ret);
        auction = (Button)findViewById(R.id.auctionbroker);
        builder = (Button)findViewById(R.id.builderbroker);
        deals = (Button)findViewById(R.id.activedeals);
        visit_refresh.setOnRefreshListener(this);

        deal_refresh.setOnRefreshListener(this);

        deal_refresh.setColorScheme(
                R.color.red, R.color.yellow,
                R.color.green, R.color.blue);

        visit_refresh.setColorScheme(
                R.color.red, R.color.yellow,
                R.color.green, R.color.blue);

        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);
        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);
        VF10 = (ViewFlipper) findViewById(R.id.vf_client);
        flipper_index = SharedPrefs.getInt(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);
        VF10.setDisplayedChild(flipper_index);

        switch (VF10.getDisplayedChild()) {
            case 0:
                hail.setBackgroundColor(Color.parseColor("#FFA500"));
                hail.setTextColor(Color.WHITE);
                visits.setBackgroundColor(Color.WHITE);
                visits.setTextColor(Color.BLACK);
                deals.setBackgroundColor(Color.WHITE);
                deals.setTextColor(Color.BLACK);
                break;

            case 1:
                visits.setBackgroundColor(Color.parseColor("#FFA500"));
                deals.setBackgroundColor(Color.WHITE);
                hail.setBackgroundColor(Color.WHITE);
                visits.setTextColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);
                deals.setTextColor(Color.BLACK);
                break;

            case 2:
                deals.setBackgroundColor(Color.parseColor("#FFA500"));
                visits.setBackgroundColor(Color.WHITE);
                hail.setBackgroundColor(Color.WHITE);
                deals.setTextColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);
                visits.setTextColor(Color.BLACK);
                break;
        }

        hail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableLocationDialog();

                if(is_transaction.equalsIgnoreCase("true"))
                {
                    Intent PostYoActivity=new Intent(context, PostYoActivity.class);
                    startActivity(PostYoActivity);}

                else{
                    VF10.setDisplayedChild(0);
                    hail.setBackgroundColor(Color.parseColor("#FFA500"));
                    hail.setTextColor(Color.WHITE);
                    visits.setBackgroundColor(Color.WHITE);
                    visits.setTextColor(Color.BLACK);
                    deals.setBackgroundColor(Color.WHITE);
                    deals.setTextColor(Color.BLACK);
                }}
        });

        broker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                which_type = "broker";
                plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                broker.setBackgroundColor(Color.BLACK);
                broker.setTextColor(Color.WHITE);
                builder.setBackgroundColor(Color.WHITE);
                builder.setTextColor(Color.BLACK);
                auction.setBackgroundColor(Color.WHITE);
                auction.setTextColor(Color.BLACK);
            }
        });

        builder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                which_type="builder";
                plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                builder.setBackgroundColor(Color.BLACK);
                builder.setTextColor(Color.WHITE);
                broker.setBackgroundColor(Color.WHITE);
                broker.setTextColor(Color.BLACK);
                auction.setBackgroundColor(Color.WHITE);;
                auction.setTextColor(Color.BLACK);

            }
        });

        auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_type="auction";
                map.clear();
                plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                 auction.setBackgroundColor(Color.BLACK);
                auction.setTextColor(Color.WHITE);
                builder.setBackgroundColor(Color.WHITE);
                builder.setTextColor(Color.BLACK);
                broker.setBackgroundColor(Color.WHITE);;
                broker.setTextColor(Color.BLACK);
            }
        });


        listViewD = (ListView) findViewById(R.id.dealslist);
        adapterD = new CustomListAdapter_Deals(this, dealList);
        listViewD.setAdapter(adapterD);

        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF10.setDisplayedChild(2);
                deals.setBackgroundColor(Color.parseColor("#FFA500"));
                deals.setTextColor(Color.WHITE);
                visits.setBackgroundColor(Color.WHITE);
                visits.setTextColor(Color.BLACK);
                hail.setBackgroundColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);

                deal_refresh.setRefreshing(true);
                refresh_deal();

            }
        });

        listView = (ListView) findViewById(R.id.visitlist);
        adapter = new CustomListAdapter_Visit(this, visitList);
        listView.setAdapter(adapter);

        visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF10.setDisplayedChild(1);
                visits.setBackgroundColor(Color.parseColor("#FFA500"));
                visits.setTextColor(Color.WHITE);
                hail.setBackgroundColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);
                deals.setBackgroundColor(Color.WHITE);
                deals.setTextColor(Color.BLACK);
                visit_refresh.setRefreshing(true);
                refresh_visit();
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

        SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY, SiteVisitAddressBar.getText().toString());

        //Nav Drawer
        inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflate.inflate(R.layout.left_nav_header,null);
        drawerLeft = (ListView) findViewById(R.id.left_drawer);
        drawerLeft.addHeaderView(vi);

        navMenuTitles = getResources().getStringArray(R.array.listItems);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout12);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        navMenuIcons.recycle();

        navadapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        drawerLeft.setAdapter(navadapter);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.home_icon, R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawers();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        smallname =  (TextView)vi.findViewById(R.id.mynamesmall);
        smallemail =  (TextView)vi.findViewById(R.id.myemailsmall);
        smallphoto = (ImageView)vi.findViewById(R.id.smallphoto);
        fetchname = SharedPrefs.getString(this, SharedPrefs.NAME_KEY, "No_Name");
        fetchemail = SharedPrefs.getString(this, SharedPrefs.EMAIL_KEY, "No_Email");
        fetchphoto = SharedPrefs.getString(this, SharedPrefs.PHOTO_KEY);
        smallname.setText(fetchname);
        smallemail.setText(fetchemail);
        smallemail.setTextColor(Color.BLACK);
        smallname.setTextColor(Color.BLACK);
        smallphoto.setImageBitmap(BitmapFactory.decodeFile(fetchphoto));


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

        Intentfilter = new IntentFilter(GcmMessageHandler.HANDLEGCM);
        ReceivefromGCM = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {

            }
        };
        registerReceiver(ReceivefromGCM, Intentfilter);

        CustomMapFragment customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);
                plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);

            }
        });

        SetSiteVisitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                if (listViewD != null && listViewD.getChildCount() > 0) {

                    if ((firstVisibleItem == 0) && (listViewD.getChildAt(0).getTop() == 0))
                        deal_refresh.setEnabled(true);
                    else
                        deal_refresh.setEnabled(false);
                }
            }
        });

        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                if (motionEvent.getAction() ==  MotionEvent.ACTION_MOVE) {

                    SiteVisitAddressBar.setText("Fetching...");
                } else {
                    getPointerLatLnt();
                    selectedLocation = map.getCameraPosition().target;
                    selectedLocation_Name = "Lat: " + selectedLocation.latitude + ", Lng: " + selectedLocation.longitude;
                    getPlaceName(selectedLocation);
                    plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                    ConnectOnMaps connectOnMaps = new ConnectOnMaps();
                    LatLng point1 = new LatLng(p_lat, p_lng);
                    LatLng point = new LatLng(lat, lng);

                    connectOnMaps.connectonMap(map, point, point1, context);
                    Log.i(TAG, "Control back in Main Activity from Connect Maps");

                    String dist = SharedPrefs.getString(context,SharedPrefs.MY_COUNTER_DISTANCE);
                    String dura = SharedPrefs.getString(context,SharedPrefs.MY_COUNTER_DURATION);

                    Log.i(TAG, "Distance in Main Activity is" + dist);
                    Log.i(TAG, "Duration in Main Activity is" + dura);


                }
            }
        });

        mapmyloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng mylocation;
                mylocation = new LatLng(lat, lng);
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
                map.animateCamera(CameraUpdateFactory.zoomTo(16));

            }
        });


        new GetCurrentLocation(context, new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(16));
                    getPlaceName(currentLocation);
                }
            }
        });
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
                    map.animateCamera(CameraUpdateFactory.zoomTo(16));



                }
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public void getPlaceName(LatLng location){
        new GetPlaceName(location, new GetPlaceName.GetPlaceNameCallback() {
            @Override
            public void onStart() {
                SiteVisitAddressBar.setText("Fetching Site Visit Location, wait..");
                location_read=false;

            }

            @Override
            public void onComplete(boolean result, LatLng location, String placeName) {
                if ( result == true ) {
                    SiteVisitAddressBar.setText(placeName);
                    SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY, SiteVisitAddressBar.getText().toString());

                    location_read=true;

                }else{
                    SiteVisitAddressBar.setText("Sorry, No Such Location, Please Try Again..");
                    location_read=false;

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
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    @Override
    public void onRefresh(){

        int index = VF10.getDisplayedChild();
        if (index == 2)
        {deal_refresh.setRefreshing(true);
            refresh_deal();}

        else if (index ==1)
        {
            visit_refresh.setRefreshing(true);
            refresh_visit();}

    }


    public void refresh_deal()
    {

        deal_refresh.setRefreshing(true);

        JsonArrayRequest dealReq = new JsonArrayRequest(urlD,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        deal_refresh.setRefreshing(false);

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
                               // deal.setDealType(obj.getString("deal_type"));
                                //deal.setOfferPrice(obj.getInt("offer_price"));
                                //deal.setLoanCom(obj.getInt("loan_comp"));
                                //deal.setLoanStatus(obj.getString("loan_status"));




                                dealList.add(deal);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

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

                                visitList.add(visit);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

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

                lat = location.getLatitude();
                lng = location.getLongitude();
               // getPointerLatLnt();

               Str_Lat = String.valueOf(lat);
               Str_Lng = String.valueOf(lng);
               SharedPrefs.save(context,SharedPrefs.MY_CUR_LAT,Str_Lat);
               SharedPrefs.save(context,SharedPrefs.MY_CUR_LNG,Str_Lng);

               sendLocationUpdate.sendPostRequest(my_user_id,Str_Lat,Str_Lng,my_role);
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
        isMyServiceRunning(LocationServices.class);

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
        Intent i = new Intent(context,LocationServices.class);
        stopService(i);

    }

   private void getPointerLatLnt()
    {
        Log.i(TAG,"getPointerLatLng has been called");
        ll = map.getCameraPosition().target;
        p_lat = ll.latitude;
        p_lng = ll.longitude;
        pointer_lat = Double.toString(p_lat);
        pointer_lng = Double.toString(p_lng);
        SharedPrefs.save(context, SharedPrefs.MY_POINTER_LAT, pointer_lat);
        SharedPrefs.save(context, SharedPrefs.MY_POINTER_LNG, pointer_lng);
    }
    private void isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG,"Service Running");
            }
        }
        Intent mServiceIntent = new Intent(this, LocationServices.class);
        startService(mServiceIntent);

    }

    private void enableLocationDialog()
    {
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        else return;
    }

}