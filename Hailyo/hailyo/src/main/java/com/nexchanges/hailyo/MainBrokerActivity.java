package com.nexchanges.hailyo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nexchanges.hailyo.custom.MyMarker;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.nexchanges.hailyo.ui.CustomMapFragment;
import com.nexchanges.hailyo.custom.GetCurrentLocation;
import com.nexchanges.hailyo.custom.GetPlaceName;
import com.nexchanges.hailyo.custom.MapWrapperLayout;
import com.nexchanges.hailyo.custom.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AbhishekWork on 23/06/15.
 */

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class MainBrokerActivity extends ActionBarActivity
{

    private DrawerLayout drawerLayout;

    String []listItems;

    Context context;

    public static final String TAG = MainActivity.class.getSimpleName();

    private ListView drawerLeft;

    private ListView drawerRight;

    private ActionBarDrawerToggle drawerToggle;

    GoogleMap map_req, map_avl,map_hail;
    LinearLayout searchLocation;
    TextView SiteVisitAddressBar, tv1, tv2,tv3,smallname, smallemail;
    ImageButton SetSiteVisitLocation;

    LatLng currentLocation, curLatLng;
    List<Address> addresses;

    LatLng selectedLocation;
    String selectedLocation_Name;
    ViewFlipper VF, VF2;
    String fetchname, fetchemail,fetchphoto;
    Location curLoc;

    Button Avl, Req, yo, hail;
    ImageView smallphoto;

    private ArrayList<MyMarker> mMyMarkersArray_Avl = new ArrayList<MyMarker>();
    private ArrayList<MyMarker> mMyMarkersArray_Hail = new ArrayList<MyMarker>();

    private ArrayList<MyMarker> mMyMarkersArray_Req = new ArrayList<MyMarker>();

    private HashMap<Marker, MyMarker> mMarkersHashMap_Req;
    private HashMap<Marker, MyMarker> mMarkersHashMap_Avl;

    private HashMap<Marker, MyMarker> mMarkersHashMap_hail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_main);
        context = this;


        Avl = (Button)findViewById(R.id.seeavail);

        Req = (Button)findViewById(R.id.seerequire);

        yo = (Button)findViewById(R.id.yomode);

        hail = (Button)findViewById(R.id.hailmode);

        VF = (ViewFlipper) findViewById(R.id.ViewFlipper01);
        VF2 = (ViewFlipper) findViewById(R.id.ViewFlipper02);

        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);

        //curLoc = map_hail.getMyLocation();
        //curLatLng = new LatLng(curLoc.getLatitude(), curLoc.getLongitude());


        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);
        SetSiteVisitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedLocation != null) {

                    Intent EnterConfigActivity = new Intent(context, EnterConfigActivity.class);
                    EnterConfigActivity.putExtra("selectedLocation", selectedLocation);
                    startActivity(EnterConfigActivity);
                }
                else
                {

                    Intent EnterConfigActivity = new Intent(context, EnterConfigActivity.class);
                    EnterConfigActivity.putExtra("selectedLocation", selectedLocation);
                    startActivity(EnterConfigActivity);

                }

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

        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);


        Req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF2.setDisplayedChild(0);
                Req.setBackgroundColor(Color.parseColor("#33b5e5"));
                Req.setTextColor(Color.WHITE);

                Avl.setBackgroundColor(Color.WHITE);
                Avl.setTextColor(Color.BLACK);
            }
        });

        Avl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF2.setDisplayedChild(1);
                Avl.setBackgroundColor(Color.parseColor("#33b5e5"));
                Avl.setTextColor(Color.WHITE);

                Req.setBackgroundColor(Color.WHITE);
                Req.setTextColor(Color.BLACK);
            }
        });


        yo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(0);
                yo.setBackgroundColor(Color.BLACK);
                yo.setTextColor(Color.WHITE);

                hail.setBackgroundColor(Color.WHITE);
                hail.setTextColor(Color.BLACK);
            }
        });

        hail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(1);
                hail.setBackgroundColor(Color.BLACK);
                hail.setTextColor(Color.WHITE);

                yo.setBackgroundColor(Color.WHITE);
                yo.setTextColor(Color.BLACK);
            }
        });



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

        //View header = getLayoutInflater().inflate(R.layout.left_nav_header,
          //      null);
        //drawerLeft.addHeaderView(header);



        LayoutInflater inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  LinearLayout myRoot = new LinearLayout();
        View vi = inflate.inflate(R.layout.left_nav_header,null);
        drawerLeft.addHeaderView(vi);

        smallname =  (TextView)vi.findViewById(R.id.mynamesmall);

        smallemail =  (TextView)vi.findViewById(R.id.myemailsmall);

        smallphoto =  (ImageView)vi.findViewById(R.id.smallphoto);



        fetchname = SharedPrefs.getString(this, SharedPrefs.NAME_KEY, "No_Name");

        fetchemail = SharedPrefs.getString(this, SharedPrefs.EMAIL_KEY, "No_Email");

        fetchphoto = SharedPrefs.getString(this, SharedPrefs.PHOTO_KEY);


        smallname.setText(fetchname);
        smallemail.setText(fetchemail);
        //smallphoto.setImageBitmap(BitmapFactory.decodeFile(fetchphoto));



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
                        Intent historyAct = new Intent(context, HistoryActivity.class);
                        startActivity(historyAct);
                        break;

                    case 3:
                        Intent helpAct = new Intent(context, HelpActivity.class);
                        startActivity(helpAct);
                        break;

                    case 4:
                        Intent promotionsAct = new Intent(context, PromotionsActivity.class);
                        startActivity(promotionsAct);
                        break;

                    case 5:
                        Intent aboutAct = new Intent(context, AboutActivity.class);
                        startActivity(aboutAct);
                        break;

                    case 6:
                        Intent settingsAct = new Intent(context, SettingsActivity.class);
                        startActivity(settingsAct);
                        break;

                    default:
                        break;
                }


            }
        });


        // Google Map Req -Yo..


        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap_Req = new HashMap<Marker, MyMarker>();


        mMyMarkersArray_Req.add(new MyMarker("Abhishek-3BHK-1Lac", "icon1", Double.parseDouble("19.116612"), Double.parseDouble("72.910285")));
        mMyMarkersArray_Req.add(new MyMarker("Sachin-2BHK-50K", "icon2", Double.parseDouble("19.114427"), Double.parseDouble("72.911102")));
        mMyMarkersArray_Req.add(new MyMarker("Hemant-4BHK-2Lac", "icon3", Double.parseDouble("19.117774"), Double.parseDouble("72.9076828")));
        mMyMarkersArray_Req.add(new MyMarker("Shlok-3BHK-1.5Lac", "icon4", Double.parseDouble("19.1148607"), Double.parseDouble("72.8999415")));


        CustomMapFragment customMapFragment_Req = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapreq));

        customMapFragment_Req.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map_req = googleMap;
                map_req.setMyLocationEnabled(true);

                plotReqMarkers(mMyMarkersArray_Req);
            }
        });




// Current Location ..

       // map_req.setMyLocationEnabled(true);

        //end of req yo map


        // start of yo-aval map


        mMarkersHashMap_Avl = new HashMap<Marker, MyMarker>();

        mMyMarkersArray_Avl.add(new MyMarker("3BHK-1Lac", "icon1", Double.parseDouble("19.116612"), Double.parseDouble("72.910285")));
        mMyMarkersArray_Avl.add(new MyMarker("2BHK-50K", "icon2", Double.parseDouble("19.114427"), Double.parseDouble("72.911102")));
        mMyMarkersArray_Avl.add(new MyMarker("4BHK-2Lac", "icon3", Double.parseDouble("19.117774"), Double.parseDouble("72.9076828")));


        CustomMapFragment customMapFragment_Avl = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapavl));

        customMapFragment_Avl.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map_avl = googleMap;
                map_avl.setMyLocationEnabled(true);

                plotAvlMarkers(mMyMarkersArray_Avl);
            }
        });




// Current Location ..
        //map_avl.setMyLocationEnabled(true);


        // end of yo-avl map


        //start of hail-map


        mMarkersHashMap_hail = new HashMap<Marker, MyMarker>();


        mMyMarkersArray_Hail.add(new MyMarker("3BHK-1Lac", "icon1", Double.parseDouble("19.116612"), Double.parseDouble("72.910285")));
        mMyMarkersArray_Hail.add(new MyMarker("2BHK-50K", "icon2", Double.parseDouble("19.114427"), Double.parseDouble("72.911102")));
        mMyMarkersArray_Hail.add(new MyMarker("4BHK-2Lac", "icon3", Double.parseDouble("19.117774"), Double.parseDouble("72.9076828")));
        mMyMarkersArray_Hail.add(new MyMarker("3BHK-1.5Lac", "icon4", Double.parseDouble("19.1148607"), Double.parseDouble("72.8999415")));


        CustomMapFragment customMapFragment_Hail = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.maphail));

        customMapFragment_Hail.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map_hail = googleMap;
                map_hail.setMyLocationEnabled(true);

                plotHailMarkers(mMyMarkersArray_Hail);
            }
        });

        //drag hail map

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

//current location hail map

        // Current Location ..

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

        // end of hail map
    }




    private void plotReqMarkers(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.req_icon1));

                Marker currentMarker = map_req.addMarker(markerOption);
                mMarkersHashMap_Req.put(currentMarker, myMarker);

                map_req.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }


    private void plotHailMarkers(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude())).title(myMarker.getmLabel());
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.req_icon1));

                Marker currentMarker = map_hail.addMarker(markerOption);
                mMarkersHashMap_hail.put(currentMarker, myMarker);

                map_hail.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }



    private void plotAvlMarkers(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude())).title(myMarker.getmLabel());;
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.req_icon1));

                Marker currentMarker = map_req.addMarker(markerOption);
                mMarkersHashMap_Avl.put(currentMarker, myMarker);

                map_req.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }



  /* private int manageMarkerIcon(String markerIcon)
    {
        if (markerIcon.equals("icon1"))
            return R.drawable.client_1;
        else if(markerIcon.equals("icon2"))
            return R.drawable.client_1;
        else if(markerIcon.equals("icon3"))
            return R.drawable.broker_plus_1;
        else if(markerIcon.equals("icon4"))
            return R.drawable.client_1;
        else return R.drawable.client_1;
         }
*/



   public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            MyMarker myMarker_Req = mMarkersHashMap_Req.get(marker);
            MyMarker myMarker_Avl = mMarkersHashMap_Avl.get(marker);



            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);


            markerLabel.setText(myMarker_Req.getmLabel());
            markerLabel.setText(myMarker_Avl.getmLabel());

            return v;
        }
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

                    map_hail.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
                    map_hail.animateCamera(CameraUpdateFactory.zoomTo(15));

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


}
