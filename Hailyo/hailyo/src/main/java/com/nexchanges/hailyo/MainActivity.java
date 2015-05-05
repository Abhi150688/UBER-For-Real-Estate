package com.nexchanges.hailyo;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.nexchanges.hailyo.services.MyService;
import com.nexchanges.hailyo.ui.AboutFragment;
import com.nexchanges.hailyo.ui.HelpFragment;
import com.nexchanges.hailyo.ui.HistoryFragment;
import com.nexchanges.hailyo.ui.MyProfileFragment;
import com.nexchanges.hailyo.ui.PaymentFragment;
import com.nexchanges.hailyo.ui.PromotionsFragment;
import com.nexchanges.hailyo.ui.SettingsFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pubnub.api.*;
import com.nexchanges.hailyo.custom.CustomActivity;
import com.nexchanges.hailyo.model.Feed;
import com.nexchanges.hailyo.ui.LeftNavAdapter;
import com.nexchanges.hailyo.ui.MainFragment;
import com.nexchanges.hailyo.ui.RightNavAdapter;
import org.json.*;
import java.util.ArrayList;

/**
 * The Activity MainActivity will launched at the start of the app.
 */
public class MainActivity extends CustomActivity
{

	/** The drawer layout. */
	private DrawerLayout drawerLayout;

    public static final String TAG = MainActivity.class.getSimpleName();

	/** ListView for left side drawer. */
	private ListView drawerLeft;

	/** ListView for left side drawer. */
	private ListView drawerRight;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;

    private LoginActivity l;


	/* (non-Javadoc)
	 * @see com.news.feeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();


       /* if (currentUser == null) {
            // It's an anonymous user, hence show the login screen
            navigateToLogin();
        }
        else {
            // The user is logged in, yay!!
            Log.i(TAG, currentUser.getUsername());
        }
*/
		setupActionBar();
		setupDrawer();
		setupContainer();

        	}

    public void startService(View v){

        Intent i = new Intent(this, MyService.class);
        startService(i);
    }


    public void stopService(View v){
        stopService(new Intent(this, MyService.class));
    }


    /**
	 * This method will setup the top title bar (Action bar) content and display
	 * values. It will also setup the custom background theme for ActionBar. You
	 * can override this method to change the behavior of ActionBar for
	 * particular Activity
	 */
	protected void setupActionBar()
	{
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.theme_black);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.action_bar_bg));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);


		// getActionBar().setBackgroundDrawable(getResources().getDrawable(theme));
	}

	/**
	 * Setup the drawer layout. This method also includes the method calls for
	 * setting up the Left & Right side drawers.
	 */
	private void setupDrawer()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout12);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.home_icon, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view)
			{
				getActionBar().setTitle(R.string.app_name);
				//getActionBar().setLogo(R.drawable.theme_red_light);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				if (drawerView == drawerLeft)
				{
					getActionBar().setTitle(R.string.home);
				}
				else
				{
					//getActionBar().setLogo(R.drawable.ic_chat);
					//getActionBar().setTitle(R.string.online_taxi_driver);
                    getActionBar().setTitle("Hailyo");
				}
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.closeDrawers();

		setupLeftNavDrawer();
		setupRightNavDrawer();
	}

	/**
	 * Setup the left navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the left side drawer. It will also setup
	 * the Header and Footer contents of left drawer. This method also apply the
	 * Theme for components of Left drawer.
	 */
	private void setupLeftNavDrawer2()
	{
		drawerLeft = (ListView) findViewById(R.id.left_drawer);

		View header = getLayoutInflater().inflate(R.layout.left_nav_header,
				null);

		drawerLeft.addHeaderView(header);

		final ArrayList<Feed> al = new ArrayList<Feed>();
		//al.add(new Feed("My Profile", "", R.drawable.ic_left1));
        al.add(new Feed("Payment", "", R.drawable.ic_left1));
		al.add(new Feed("History", null, R.drawable.ic_left2));
		al.add(new Feed("Help", null, R.drawable.ic_left3));
		al.add(new Feed("Promotions", null, R.drawable.ic_left4));
		al.add(new Feed("About", null, R.drawable.ic_left5));
		al.add(new Feed("Settings", null, R.drawable.ic_left6));

		final LeftNavAdapter adp = new LeftNavAdapter(this, al);
		drawerLeft.setAdapter(adp);
		drawerLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View arg1, int arg2,
					long arg3)
			{
				if (arg2 == 0)
					return;
				for (Feed f : al)
					f.setDesc(null);
				al.get(arg2 - 1).setDesc("");
				adp.notifyDataSetChanged();
				// drawerLayout.closeDrawers();
			}
		});

	}


    private void setupLeftNavDrawer()
    {
        drawerLeft = (ListView) findViewById(R.id.left_drawer);

        View header = getLayoutInflater().inflate(R.layout.left_nav_header,
                null);

        drawerLeft.addHeaderView(header);

        final ArrayList<Feed> al = new ArrayList<Feed>();
       // al.add(new Feed("My Profile", "", R.drawable.ic_left1));
        al.add(new Feed("Payment", "", R.drawable.ic_left1));
        al.add(new Feed("History", null, R.drawable.ic_left2));
        al.add(new Feed("Help", null, R.drawable.ic_left3));
        al.add(new Feed("Promotions", null, R.drawable.ic_left4));
        al.add(new Feed("About", null, R.drawable.ic_left5));
        al.add(new Feed("Settings", null, R.drawable.ic_left6));

        drawerLeft.setOnItemClickListener(new SlideMenuClickListener());

        final LeftNavAdapter adp = new LeftNavAdapter(this, al);
        drawerLeft.setAdapter(adp);

    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }



    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            //case 0:
              //  fragment = new MyProfileFragment();
                //break;
            case 0:
                fragment = new PaymentFragment();

                break;
            case 1:
                fragment = new HistoryFragment();
                break;
            case 2:
                fragment = new HelpFragment();
                break;
            case 3:
                fragment = new PromotionsFragment();
                break;
            case 4:
                fragment = new AboutFragment();
                break;

            case 5:
                fragment = new SettingsFragment();
                break;

            default:
                fragment = new MainFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            //update selected item and title, then close the drawer
            drawerLeft.setItemChecked(position, true);
            drawerLeft.setSelection(position);
           //setTitle(navMenuTitles[position]);
            drawerLayout.closeDrawer(drawerLeft);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }



	/**
	 * Setup the right navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the right side drawer. It will also setup
	 * the Header contents of right drawer.
	 */
	private void setupRightNavDrawer()
	{
		drawerRight = (ListView) findViewById(R.id.right_drawer);

		View header = getLayoutInflater().inflate(R.layout.rigth_nav_header,
				null);
		drawerRight.addHeaderView(header);

		ArrayList<Feed> al = new ArrayList<Feed>();
		al.add(new Feed("Broker Smith", "touch to chat", R.drawable.img_f1, true));
		al.add(new Feed("Broker James", "unavailable for chat",
				R.drawable.img_f2, false));
		al.add(new Feed("Broker Andy", "unavailable for chat",
				R.drawable.img_f3, false));
		al.add(new Feed("Broker Ricky", "touch to chat", R.drawable.img_f4, true));

		drawerRight.setAdapter(new RightNavAdapter(this, al));
	}

	/**
	 * Setup the container fragment for drawer layout. This method will setup
	 * the grid view display of main contents. You can customize this method as
	 * per your need to display specific content.
	 */
	private void setupContainer()
	{
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new MainFragment()).commit();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerRight);
		menu.findItem(R.id.menu_chat).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
		{
			if (drawerLayout.isDrawerOpen(drawerRight))
				drawerLayout.closeDrawers();
			return true;
		}
		if (item.getItemId() == R.id.menu_chat)
		{
			drawerLayout.closeDrawer(drawerLeft);
			drawerLayout.openDrawer(drawerRight);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.e("KEy", "YEs");
        Intent i = new Intent(this, MyService.class);
        startService(i);

        return super.onKeyDown(keyCode, event);
	}

    private void navigateToLogin() {
        // Launch the login activity

        Intent intent = new Intent(this, LoginActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
