package com.nexchanges.hailyo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.nexchanges.hailyo.services.MyService;
import com.nexchanges.hailyo.ui.CustomMapFragment;
import com.nexchanges.hailyo.ui.GetCurrentLocation;
import com.nexchanges.hailyo.ui.GetPlaceName;
import com.nexchanges.hailyo.ui.MapWrapperLayout;
import com.nexchanges.hailyo.ui.SearchActivity;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener
{

    SeekBar sb1;
	/** The drawer layout. */
	private DrawerLayout drawerLayout;

    String []listItems;

    Context context;

    public static final String TAG = MainActivity.class.getSimpleName();

	/** ListView for left side drawer. */
	private ListView drawerLeft;

	/** ListView for left side drawer. */
	private ListView drawerRight;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;


    GoogleMap map;
    LinearLayout searchLocation;
    TextView SiteVisitAddressBar;
    ImageButton SetSiteVisitLocation;

    LatLng currentLocation;
    List<Address> addresses;

    LatLng selectedLocation;
    String selectedLocation_Name;
    ViewFlipper VF;


    boolean continuosProgress;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Intent i = new Intent(this, MyService.class);
        startService(i);


        searchLocation = (LinearLayout) findViewById(R.id.searchLocation);
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);

        SetSiteVisitLocation = (ImageButton) findViewById(R.id.ic_launcher);
        SetSiteVisitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent EnterConfigActivity=new Intent(context, EnterConfigActivity.class);
                EnterConfigActivity.putExtra("selectedLocation", selectedLocation);
                startActivity(EnterConfigActivity);
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

        sb1 = (SeekBar)findViewById(R.id.seekBar2);
        sb1.setOnSeekBarChangeListener(this);

        VF = (ViewFlipper) findViewById(R.id.ViewFlipper01);



        ParseUser currentUser = ParseUser.getCurrentUser();

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

        View header = getLayoutInflater().inflate(R.layout.left_nav_header,
                null);
        drawerLeft.addHeaderView(header);

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
        SiteVisitAddressBar = (TextView) findViewById(R.id.SiteVisitAddressBar);


        CustomMapFragment customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map = customMapFragment.getMap();
        map.setMyLocationEnabled(true);

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


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromTouch) {
        if (progress == 0) {
            VF.setDisplayedChild(0);
        } else if (progress == 50) {
            VF.setDisplayedChild(1);
        } else VF.setDisplayedChild(0);
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
		Log.e("KEy", "YEs");
        Intent i = new Intent(this, MainActivity.class);
        startService(i);
        finish();

        return super.onKeyDown(keyCode, event);
	}

    private void navigateToLogin() {
        // Launch the login activity

        Intent intent = new Intent(this, LoginActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
