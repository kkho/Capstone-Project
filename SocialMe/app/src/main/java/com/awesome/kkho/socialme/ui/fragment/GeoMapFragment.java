package com.awesome.kkho.socialme.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.model.Venue;
import com.awesome.kkho.socialme.util.Config;
import com.awesome.kkho.socialme.util.GeocoderUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by kkh on 20.03.2015.
 */
public class GeoMapFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng mLatLng;
    ArrayList<MarkerOptions> mMarkerForLocations;
    private MarkerOptions options;
    private Location mLocation;
    private Event mEvent;
    private Venue mVenue;
    private String mAddressInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArguments = this.getArguments();

        if (mArguments != null) {

        }

        Gson gson = new Gson();
        mLocation = gson.fromJson(mArguments.getString(Config.LOCATION), Location.class);
        if(mArguments.getString(Config.EVENTS) != null) {
            mEvent = gson.fromJson(mArguments.getString(Config.EVENTS), Event.class);
            mAddressInfo = mEvent.getVenue_address() + "," + mEvent.getCity_name() + ", "
                    + mEvent.getCountry_name();
        }
        else if(mArguments.getString(Config.VENUES) != null) {
            mVenue = gson.fromJson(mArguments.getString(Config.VENUES), Venue.class);
            mAddressInfo = mVenue.getAddress() + "," + mVenue.getCity_name() + ", "
                    + mVenue.getCountry_name();
        }
        mMarkerForLocations = new ArrayList<>();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_layout, container, false);
        ButterKnife.bind(this, view);
        setUpMapIfNeeded();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setIndoorEnabled(true);
                    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity())
                            == ConnectionResult.SUCCESS) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                    }

                    handleNewLocation(mLocation);
                }
            });


        }
    }

    private void setUpMap() {
        //add markers to the location of movies around you
        mMap.clear();
        CameraUpdate cameraUpdate;
        mMap.addMarker(mMarkerForLocations.get(0));
        LatLng bounds = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(bounds, 14);
        mMap.moveCamera(cameraUpdate);
    }

    public void setLocation(Location location) {
        this.mLocation = location;

    }

    private void handleNewLocation(Location location) {
        if (location != null) {
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);
            //
            String address = mAddressInfo;
            options = new MarkerOptions()
                    .position(latLng)
                    .title(address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            if (mMarkerForLocations != null && !mMarkerForLocations.contains(options)) {
                mMarkerForLocations.add(options);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            setUpMap();
        }
    }
}
