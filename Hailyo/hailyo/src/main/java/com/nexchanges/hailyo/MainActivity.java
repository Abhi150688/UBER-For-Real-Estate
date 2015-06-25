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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
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
import com.nexchanges.hailyo.services.MyService;

import com.nexchanges.hailyo.ui.CustomMapFragment;
import com.nexchanges.hailyo.custom.GetCurrentLocation;
import com.nexchanges.hailyo.custom.GetPlaceName;
import com.nexchanges.hailyo.custom.MapWrapperLayout;
import com.nexchanges.hailyo.custom.SearchActivity;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener
{

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
    List<Address> addresses;

    LatLng selectedLocation,curLatLng;
    Location curLoc;
    String selectedLocation_Name;
    String fetchname, fetchemail,fetchphoto;
    ViewFlipper VF;
    ImageView smallphoto;

    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private static LayoutInflater inflate =null;



    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        Intent i = new Intent(this, MyService.class);
        startService(i);

        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);

        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);


        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);


        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivity=new Intent(context, SearchActivity.class);
                searchActivity.putExtra("nearLocation", currentLocation);
                startActivityForResult(searchActivity, 1);
            }
        });

        sb1 = (SeekBar)findViewById(R.id.seekBar2);
        sb1.setOnSeekBarChangeListener(this);

      VF = (ViewFlipper) findViewById(R.id.ViewFlipper01);




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
                    Intent historyAct = new Intent(context, HistoryActivity.class);
                    startActivity(historyAct);

                    case 3:
                    Intent helpAct = new Intent(context, HelpActivity.class);
                    startActivity(helpAct);

                    case 4:
                    Intent promotionsAct = new Intent(context, PromotionsActivity.class);
                    startActivity(promotionsAct);

                    case 5:
                    Intent aboutAct = new Intent(context, AboutActivity.class);
                    startActivity(aboutAct);

                    case 6:
                    Intent settingsAct = new Intent(context, SettingsActivity.class);
                    startActivity(settingsAct);

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

                                                      plotMarkers(mMyMarkersArray);
                                                  }
                                              });

        //curLoc = map.getMyLocation();
        //curLatLng = new LatLng(curLoc.getLatitude(), curLoc.getLongitude());

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




        CustomMapFragment customMapFragment2 = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2));

        customMapFragment2.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map2 = googleMap;
                map2.setMyLocationEnabled(true);

                plotMarkers2(mMyMarkersArray);
            }
        });


        CustomMapFragment customMapFragment3 = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3));

        customMapFragment3.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map3 = googleMap;
                map3.setMyLocationEnabled(true);

                plotMarkers3(mMyMarkersArray);
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


        customMapFragment3.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    SiteVisitAddressBar.setText("Fetching...");
                } else {
                    selectedLocation = map3.getCameraPosition().target;
                    selectedLocation_Name = "Lat: " + selectedLocation.latitude + ", Lng: " + selectedLocation.longitude;
                    getPlaceName(selectedLocation);
                }
            }
        });


        customMapFragment2.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    SiteVisitAddressBar.setText("Fetching...");
                } else {
                    selectedLocation = map2.getCameraPosition().target;
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


        new GetCurrentLocation(context, new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map2.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map2.animateCamera(CameraUpdateFactory.zoomTo(15));

                    getPlaceName(currentLocation);


                }
            }
        });



        new GetCurrentLocation(context, new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map3.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map3.animateCamera(CameraUpdateFactory.zoomTo(15));

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

                Marker currentMarker = map2.addMarker(markerOption);

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

                Marker currentMarker = map3.addMarker(markerOption);

                mMarkersHashMap.put(currentMarker, myMarker);


            }
        }
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromTouch) {
        if (progress == 0) {
           VF.setDisplayedChild(0);
            tv1.setTextColor(Color.RED);
            tv2.setTextColor(Color.BLACK);
            tv3.setTextColor(Color.BLACK);

        } else if (progress == 50) {

           VF.setDisplayedChild(1);
            tv1.setTextColor(Color.BLACK);
            tv2.setTextColor(Color.RED);
            tv3.setTextColor(Color.BLACK);

        } else if (progress ==100)
        {VF.setDisplayedChild(2);
        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.RED);}

    }




    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
         }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        int mProgress = seekBar.getProgress();
        if(mProgress > 0 & mProgress < 34) {
            seekBar.setProgress(0);
            }
        else if (mProgress >33 & mProgress <68){
            seekBar.setProgress(50);
            }
          else seekBar.setProgress(100);

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


                    map2.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
                    map2.animateCamera(CameraUpdateFactory.zoomTo(15));


                    map3.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
                    map3.animateCamera(CameraUpdateFactory.zoomTo(15));


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

