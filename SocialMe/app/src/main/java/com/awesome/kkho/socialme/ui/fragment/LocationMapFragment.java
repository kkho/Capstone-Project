package com.awesome.kkho.socialme.ui.fragment;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.FacebookUser;
import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.util.GeocoderUtil;
import com.awesome.kkho.socialme.util.SharedPrefUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kkho on 29.01.2016.
 */
public class LocationMapFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng mLatLng;
    private MarkerOptions options;
    private Location mLocation;
    private FacebookUser mUser;
    private List<Address> mAddresses;

    @Bind(R.id.current_location_button)
    Button mCurrentLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharedPrefUtil.getUserProfile(getActivity());
        mLocation = mUser.getSelectedLocation();
        mAddresses = GeocoderUtil.GetAddressOfLocation(getActivity(), mLocation);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_location, container, false);
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

    @OnClick({R.id.current_location_button})
    public void onButtonClick(View view) {
        mUser.setSelectedLocation(null);
        SharedPrefUtil.storeProfileDescription(mUser, getActivity());
        MenuItem actionRestart =  ((BaseActivity)getActivity()).mNavigationView.getMenu().getItem(0);
        actionRestart.setChecked(true);
        ((BaseActivity)getActivity()).onNavigationItemSelected(actionRestart);

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

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            options = new MarkerOptions()
                                    .position(latLng);
                            mMap.clear();
                            mMap.addMarker(options);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            mLocation.setLatitude(latLng.latitude);
                            mLocation.setLongitude(latLng.longitude);
                            mUser.setSelectedLocation(mLocation);
                            SharedPrefUtil.storeProfileDescription(mUser, getActivity());
                        }
                    });
                    handleNewLocation(mLocation);
                }
            });
        }
    }

    private void setUpMap() {
        //add markers to the location of movies around you
        mMap.clear();
        CameraUpdate cameraUpdate;
        mMap.addMarker(options);
        LatLng bounds = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(bounds, 4);
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
            options = new MarkerOptions()
                    .position(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            setUpMap();
        }
    }
}