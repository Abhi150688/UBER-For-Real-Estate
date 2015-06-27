package com.nexchanges.hailyo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
public class MainBrokerActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private DrawerLayout drawerLayout;

    String[] listItems;

    Context context;

    public static final String TAG = MainActivity.class.getSimpleName();

    private ListView drawerLeft;

    private ListView drawerRight;

    private ActionBarDrawerToggle drawerToggle;

    GoogleMap map_hail;
    LinearLayout searchLocation;
    TextView SiteVisitAddressBar, smallname, smallemail, textview1, textview2;
    ImageButton SetSiteVisitLocation;

    LatLng currentLocation;

    LatLng selectedLocation;
    String selectedLocation_Name;
    ViewFlipper VF, VF2;
    String fetchname, fetchemail, fetchphoto;

    Button yo, hail, deals,visits;
    SeekBar avlreq;
    ImageView smallphoto;

    private ArrayList<MyMarker> mMyMarkersArray_Hail = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap_hail;
    int flipper_index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_main);
        context = this;
        avlreq = (SeekBar) findViewById(R.id.seekBar2);
        avlreq.setOnSeekBarChangeListener(this);

        textview1 = (TextView) findViewById(R.id.textView1);

        textview2 = (TextView) findViewById(R.id.textView2);

        yo = (Button) findViewById(R.id.yomode);

        visits = (Button) findViewById(R.id.activevisits);

        deals = (Button) findViewById(R.id.activedeals);

        hail = (Button) findViewById(R.id.hailmode);

        VF = (ViewFlipper) findViewById(R.id.ViewFlipper01);
        VF2 = (ViewFlipper) findViewById(R.id.ViewFlipper02);

        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);
        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);



        flipper_index = SharedPrefs.getInt(context, SharedPrefs.CURRENT_FLIPPER_VIEW, 0);
        VF.setDisplayedChild(flipper_index);

        switch (VF.getDisplayedChild()) {
            case 0:
                yo.setBackgroundColor(Color.parseColor("#33b5e5"));
                visits.setBackgroundColor(Color.BLACK);
                hail.setBackgroundColor(Color.BLACK);
                deals.setBackgroundColor(Color.BLACK);
                break;

            case 1:
                visits.setBackgroundColor(Color.parseColor("#33b5e5"));
                deals.setBackgroundColor(Color.BLACK);
                yo.setBackgroundColor(Color.BLACK);
                hail.setBackgroundColor(Color.BLACK);break;

            case 2:
                deals.setBackgroundColor(Color.parseColor("#33b5e5"));
                visits.setBackgroundColor(Color.BLACK);
                yo.setBackgroundColor(Color.BLACK);
                hail.setBackgroundColor(Color.BLACK);break;
            case 3:
                hail.setBackgroundColor(Color.parseColor("#33b5e5"));
                visits.setBackgroundColor(Color.BLACK);
                yo.setBackgroundColor(Color.BLACK);
                deals.setBackgroundColor(Color.BLACK);break;
        }


        SetSiteVisitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.save(context, SharedPrefs.CURRENT_LOC_KEY, SiteVisitAddressBar.getText().toString());
                Intent EnterConfigActivity = new Intent(context, EnterConfigActivity.class);
                startActivity(EnterConfigActivity);

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
                VF.setDisplayedChild(0);
                yo.setBackgroundColor(Color.parseColor("#33b5e5"));

                visits.setBackgroundColor(Color.BLACK);
                hail.setBackgroundColor(Color.BLACK);
                deals.setBackgroundColor(Color.BLACK);

            }
        });

        hail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(3);
                hail.setBackgroundColor(Color.parseColor("#33b5e5"));
                visits.setBackgroundColor(Color.BLACK);

                yo.setBackgroundColor(Color.BLACK);
                deals.setBackgroundColor(Color.BLACK);
            }
        });

        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(2);
                deals.setBackgroundColor(Color.parseColor("#33b5e5"));
                visits.setBackgroundColor(Color.BLACK);

                yo.setBackgroundColor(Color.BLACK);
                hail.setBackgroundColor(Color.BLACK);
            }
        });


        visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VF.setDisplayedChild(1);
                visits.setBackgroundColor(Color.parseColor("#33b5e5"));
                deals.setBackgroundColor(Color.BLACK);

                yo.setBackgroundColor(Color.BLACK);
                hail.setBackgroundColor(Color.BLACK);
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
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude())).title(myMarker.getmLabel());
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.req_icon1));

                Marker currentMarker = map_hail.addMarker(markerOption);
                mMarkersHashMap_hail.put(currentMarker, myMarker);

                map_hail.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            MyMarker myMarker_Hail = mMarkersHashMap_hail.get(marker);

            TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);


            markerLabel.setText(myMarker_Hail.getmLabel());

            return v;
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
            }

            @Override
            public void onComplete(boolean result, LatLng location, String placeName) {
                if (result == true) {
                    SiteVisitAddressBar.setText(placeName);
                } else {
                    SiteVisitAddressBar.setText("Sorry, No Such Location, Please Try Again..");
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
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromTouch) {
                if (progress == 0) {
                    VF2.setDisplayedChild(0);
                    textview1.setTextColor(Color.RED);
                    textview2.setTextColor(Color.BLACK);

                } else if (progress == 100) {

                    VF2.setDisplayedChild(1);
                    textview1.setTextColor(Color.BLACK);
                    textview2.setTextColor(Color.RED);

                }

        }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

                int mProgress = seekBar.getProgress();
                if (mProgress > 0 & mProgress < 51) {
                    seekBar.setProgress(0);
                } else seekBar.setProgress(100);

    }
}
