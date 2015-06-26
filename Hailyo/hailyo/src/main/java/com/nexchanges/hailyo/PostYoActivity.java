package com.nexchanges.hailyo;


/**
 * Created by AbhishekWork on 21/06/15.
 */

        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.location.Location;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Vibrator;
        import android.support.v4.app.ActionBarDrawerToggle;

        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarActivity;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RatingBar;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.model.LatLng;
        import com.nexchanges.hailyo.model.SharedPrefs;
        import com.nexchanges.hailyo.ui.CustomMapFragment;
        import com.nexchanges.hailyo.custom.GetCurrentLocation;

        import java.util.concurrent.TimeUnit;

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class PostYoActivity extends ActionBarActivity
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
    long ltimer,ltimer1;

    LatLng currentLocation;

    Button call, message;
    ImageButton cancel;
    TextView timerTv, brokerTv;
    RatingBar ratingTv;
    String phone, brokerName, timer, rating, body,phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_yo);
        context = this;

        Intent PostYoIntent = getIntent();
        Bundle extras = PostYoIntent.getExtras();
        phone = extras.getString("Phone");
        brokerName = extras.getString("Broker_Name");
        timer = extras.getString("Timer");
        rating = extras.getString("Rating");


           call  = (Button) findViewById(R.id.call);

           message  = (Button) findViewById(R.id.message);

           cancel  = (ImageButton) findViewById(R.id.cancel);

           timerTv  = (TextView) findViewById(R.id.timer);

           brokerTv  = (TextView) findViewById(R.id.bname);

           ratingTv  = (RatingBar) findViewById(R.id.ratingBar);


        float ratingVal = Float.parseFloat(rating);
        ratingTv.setRating(ratingVal);
        brokerTv.setText(brokerName);

        ltimer = Long.parseLong(timer);
        ltimer1 = ltimer*1000*60;

        new CountDownTimer(ltimer1, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTv.setText(""+String.format("%d min",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)-
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                timerTv.setText(brokerName + " should have arrived!");
                Intent DuringVisitActivity=new Intent(context, DuringVisitActivity.class);
                startActivity(DuringVisitActivity);
                finish();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        }.start();


        message.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View view) {

                                           Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                                           smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                           smsIntent.setType("vnd.android-dir/mms-sms");
                                           smsIntent.setData(Uri.parse("sms:" + phone));
                                           startActivity(smsIntent);
                                       }
                                   });


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                body = "Your site visit Id: ST123453 with Mr. Abhishek has just been cancelled";


                sendSMSMessage(phone, body);
                Intent MainActivity = new Intent (context, MainActivity.class);
                startActivity(MainActivity);
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Uri call = Uri.parse("tel:" + phone);
                Intent callintent = new Intent(Intent.ACTION_DIAL, call);
                startActivity(callintent);
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
        //  LinearLayout myRoot = new LinearLayout();
        View vi = inflate.inflate(R.layout.left_nav_header, null);
        drawerLeft.addHeaderView(vi);

        TextView smallname = (TextView) vi.findViewById(R.id.mynamesmall);

        TextView smallemail = (TextView) vi.findViewById(R.id.myemailsmall);

        ImageView smallphoto = (ImageView) vi.findViewById(R.id.smallphoto);


        String fetchname = SharedPrefs.getString(this, SharedPrefs.NAME_KEY, "No_Name");

        String fetchemail = SharedPrefs.getString(this, SharedPrefs.EMAIL_KEY, "No_Email");

        String fetchphoto = SharedPrefs.getString(this, SharedPrefs.PHOTO_KEY);


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

        CustomMapFragment customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);

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

    protected void sendSMSMessage(String phoneNo,String message) {
        Log.i("Send SMS", "");

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "VISIT CANCELLED", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}

