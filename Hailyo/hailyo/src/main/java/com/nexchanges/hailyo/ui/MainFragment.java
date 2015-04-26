package com.nexchanges.hailyo.ui;

/**
 * Created by Abhishek on 24/04/15.
 */



        import android.app.Fragment;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;
        import android.text.SpannableString;
        import android.text.style.ForegroundColorSpan;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
        import com.google.android.gms.maps.MapView;
        import com.google.android.gms.maps.MapsInitializer;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.nexchanges.hailyo.PaymentPopup;
        import com.nexchanges.hailyo.R;
        import com.nexchanges.hailyo.custom.CustomActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class MainFragment is the base fragment that shows the Google Map. You
 * can add your code to do whatever you want related to Map functions for your
 * app. For example you can add Map markers here or can show places on map.
 */
public class MainFragment extends Fragment implements OnClickListener
{

    /** The map view. */
    private MapView mMapView;

    /** The Google map. */
    private GoogleMap mMap;

    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.main_container, null);

        initMap(v, savedInstanceState);

        initButtons(v);
        return v;
    }

    /**
     * Initialize the buttons and set the Touch and Click listner for each
     * button of this view. You can add or change the buttons here.
     *
     * @param v
     *            the v
     */
    private void initButtons(View v)
    {
        View b = v.findViewById(R.id.btnSearch);
        b.setOnTouchListener(CustomActivity.TOUCH);
        b.setOnClickListener(this);

        //b = v.findViewById(R.id.btn   id.btnCall);
        //b.setOnTouchListener(CustomActivity.TOUCH);
        //b.setOnClickListener(this);

        //b = v.findViewById(R.id.btnCheckin);
        //b.setOnTouchListener(CustomActivity.TOUCH);
        //b.setOnClickListener(this);
    }

    /**
     * Initialize the Map view.
     *
     * @param v
     *            the v
     * @param savedInstanceState
     *            the saved instance state object passed from OnCreateView
     *            method of fragment.
     */
    private void initMap(View v, Bundle savedInstanceState)
    {
        MapsInitializer.initialize(getActivity());

		/*try
		{
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e)
		{
			e.printStackTrace();
		} */

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
    }

    /**
     * This method can be used to show the markers on the map. Current
     * implementation of this method will show only a single Pin with title and
     * snippet. You must customize this method to show the pins as per your
     * need.
     */
    private void setupMapMarkers()
    {

        mMap.clear();

        MarkerOptions opt = new MarkerOptions();
        LatLng l = new LatLng(37.42, -122.084);
        opt.position(l).title("Taxi 00689").snippet("8 min");
        opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
        mMap.addMarker(opt).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 16));
    }

    /**
     * This class creates a Custom a InfoWindowAdapter that is used to show
     * popup on map when user taps on a pin on the map. Current implementation
     * of this class will show a Title and a snippet.
     *
     */
    private class CustomInfoWindowAdapter implements InfoWindowAdapter
    {

        /** The contents view. */
        private final View mContents;

        /**
         * Instantiates a new custom info window adapter.
         */
        CustomInfoWindowAdapter()
        {

            mContents = getActivity().getLayoutInflater().inflate(
                    R.layout.map_popup, null);
        }

        /* (non-Javadoc)
         * @see com.google.android.gms.maps.GoogleMap.InfoWindowAdapter#getInfoWindow(com.google.android.gms.maps.model.Marker)
         */
        @Override
        public View getInfoWindow(Marker marker)
        {

            render(marker, mContents);
            return mContents;
        }

        /* (non-Javadoc)
         * @see com.google.android.gms.maps.GoogleMap.InfoWindowAdapter#getInfoContents(com.google.android.gms.maps.model.Marker)
         */
        @Override
        public View getInfoContents(Marker marker)
        {

            return null;
        }

        /**
         * Render the marker content on Popup view. Customize this as per your
         * need.
         *
         * @param marker
         *            the marker
         * @param view
         *            the content view
         */
        private void render(final Marker marker, View view)
        {

            String title = marker.getTitle();
            TextView titleUi = (TextView) view.findViewById(R.id.title);
            if (title != null)
            {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                        titleText.length(), 0);
                titleUi.setText(titleText);
            }
            else
            {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = (TextView) view.findViewById(R.id.snippet);
            if (snippet != null)
            {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                        snippet.length(), 0);
                snippetUi.setText(snippetText);
            }
            else
            {
                snippetUi.setText("");
            }

        }
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onResume()
     */
    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();

        mMap = mMapView.getMap();
        if (mMap != null)
        {
            mMap.setMyLocationEnabled(true);
            // mMap.setOnInfoWindowClickListener(this);
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            setupMapMarkers();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onPause()
     */
    @Override
    public void onPause()
    {

        mMapView.onPause();
        if (mMap != null)
            mMap.setInfoWindowAdapter(null);
        super.onPause();
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy()
    {
        mMapView.onDestroy();
        super.onDestroy();
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onLowMemory()
     */
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnSearch)
            SearchDialog.show((FragmentActivity) getActivity());
       // else if (v.getId() == R.id.btnCall)
         //   CallDialog.show((FragmentActivity) getActivity());
        //else if (v.getId() == R.id.btnCheckin)
          //  startActivity(new Intent(getActivity(), PaymentPopup.class));
    }

}
