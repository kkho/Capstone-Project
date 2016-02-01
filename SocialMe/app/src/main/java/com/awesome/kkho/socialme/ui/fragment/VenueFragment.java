package com.awesome.kkho.socialme.ui.fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.adapter.VenueAdapter;
import com.awesome.kkho.socialme.model.FacebookUser;
import com.awesome.kkho.socialme.model.Image;
import com.awesome.kkho.socialme.model.Venue;
import com.awesome.kkho.socialme.model.VenueResponse;
import com.awesome.kkho.socialme.receiver.ServiceReceiver;
import com.awesome.kkho.socialme.service.VenueIntentService;
import com.awesome.kkho.socialme.storage.SocialContract;
import com.awesome.kkho.socialme.ui.activity.ItemVenueActivity;
import com.awesome.kkho.socialme.util.Config;
import com.awesome.kkho.socialme.util.NetworkUtil;
import com.awesome.kkho.socialme.util.RecyclerViewClickListener;
import com.awesome.kkho.socialme.util.SharedPrefUtil;
import com.awesome.kkho.socialme.util.UiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkho on 29.01.2016.
 */
public class VenueFragment extends Fragment implements ServiceReceiver.Listener,
        RecyclerViewClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static boolean mHasLoadedContent = false;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static boolean mStateHasRunShowTime = false;
    private int mPageNumber = 1;
    private int mTotalPageCount = 0;
    private ServiceReceiver mReceiver;
    private List<Venue> mVenueItems = new ArrayList<Venue>();
    private VenueResponse mVenueResponseWrapper;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private Snackbar mSnackBar;
    private VenueAdapter mVenueAdapter;
    private boolean mIsLoading;

    @Bind(R.id.general_recyclerview)
    RecyclerView mVenueRecyclerView;
    @Bind(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static SparseArray<Bitmap> sPhotoCache = new SparseArray<>(1);
    private int mTotal_pages = 0;

    public VenueFragment() {
        // Required empty public constructor

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVenueItems = new ArrayList<Venue>();
        mReceiver = new ServiceReceiver(new Handler());

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(10 * 1000); // 1 second, in milliseconds

        clearVenueList();
        mStateHasRunShowTime = false;
        setRetainInstance(true);
        setHasOptionsMenu(true);
        getActivity().getLoaderManager().restartLoader(0, null, this);
    }

    private void initShowVenueIntentService() {
        FacebookUser user = SharedPrefUtil.getUserProfile(getActivity());
        Intent intent = new Intent(getActivity(), VenueIntentService.class);
        intent.putExtra(Config.LOCATION, user.getSelectedLocation());
        intent.putExtra(Config.PAGE_NUMBER, mPageNumber);
        intent.putExtra(Config.RECEIVER, mReceiver);
        getActivity().startService(intent);

    }

    private void clearVenueList() {
        getActivity().getContentResolver().delete(SocialContract.EventDB.CONTENT_URI, null, null);
        mVenueItems.clear();
        if (mVenueAdapter != null) {
            mVenueAdapter.clearEvents();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mReceiver.setListener(this);
        View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, viewRoot);
        initSwipeRefresh();
        initRecyclerView(mVenueItems);

        return viewRoot;
    }

    public void initSwipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    public void initRecyclerView(List<Venue> venueItems) {
        if (mVenueRecyclerView != null) {
            mVenueAdapter = new VenueAdapter(getActivity(), venueItems);
            mVenueAdapter.setRecyclerListListener(this);
            mVenueRecyclerView.setHasFixedSize(true);
            mVenueItems = venueItems;
            mVenueRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mVenueRecyclerView.setAdapter(mVenueAdapter);
            mVenueRecyclerView.addOnScrollListener(mRecyclerScrollListener);
        }
    }

    private void refreshContent() {
        mPageNumber = 1;
        if (NetworkUtil.isNetworkOn(getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearVenueList();
                    initShowVenueIntentService();
                    mHasLoadedContent = true;
                    mStateHasRunShowTime = false;
                }
            }, 2200);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStateHasRunShowTime = false;
        ButterKnife.unbind(this);
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        sPhotoCache.clear();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getContentResolver().registerContentObserver(
                SocialContract.VenueDB.CONTENT_URI, true, mObserver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Config.EVENTS, mVenueResponseWrapper);
        super.onSaveInstanceState(outState);
    }

    public void showLoadingLabel() {
        showSnackbar("Loading more events");
    }

    @Override
    public void onDetach() {
        getActivity().getContentResolver().unregisterContentObserver(mObserver);
        super.onDetach();
    }

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if (getActivity() == null) {
                return;
            }
            Loader<Cursor> loader = getActivity().getLoaderManager().getLoader(0);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    };

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        mVenueResponseWrapper = (VenueResponse) resultData.getSerializable(Config.VENUES);
        mPageNumber++;
        // add to adapter¨
        if (mVenueResponseWrapper.getVenues() != null) {
            mVenueItems = mVenueResponseWrapper.getVenues().getVenue();
            if (mVenueAdapter.getItemCount() < 1) {
                mVenueAdapter.setVenueItems(mVenueItems);
            } else {
                mVenueAdapter.appendVenues(mVenueItems);
            }
        }

        mIsLoading = false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Config.REQUEST_LOCATION);
            return;
        }

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        } else {
            onLocationChanged(mLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                SocialContract.VenueDB.CONTENT_URI,
                Config.VenueQuery.PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getActivity() == null) return;
        mVenueItems = new ArrayList<>();

        if(!data.moveToNext()) {
            initShowVenueIntentService();
            return;
        }

        Gson gson = new Gson();
        while (data.moveToNext()) {
            Venue venue = new Venue();
            venue.setId(data.getString(Config.VenueQuery.ID));
            venue.setCity_name(data.getString(Config.VenueQuery.CITYNAME));
            venue.setName(data.getString(Config.VenueQuery.NAME));
            venue.setVenue_name(data.getString(Config.VenueQuery.VENUE_NAME));
            venue.setImage(gson.fromJson(data.getString(Config.VenueQuery.IMAGEURL), Image.class));
            venue.setCountry_name(data.getString(Config.VenueQuery.COUNTRYNAME));
            venue.setUrl(data.getString(Config.VenueQuery.URL));
            venue.setVenue_type(data.getString(Config.VenueQuery.VENUE_TYPE));
            venue.setRegion_name(data.getString(Config.VenueQuery.REGION_NAME));
            venue.setAddress(data.getString(Config.VenueQuery.ADDRESS));
            venue.setDescription(data.getString(Config.VenueQuery.DESCRIPTION));
            venue.setPostal_code(data.getString(Config.VenueQuery.POSTALCODE));

            venue.setLongitude(data.getDouble(Config.VenueQuery.LONGITUDE));
            venue.setLatitude(data.getDouble(Config.VenueQuery.LATITUDE));
            mVenueItems.add(venue);
        }

        mTotal_pages = SharedPrefUtil.getCurrentPageNumberVenue(getActivity());
        if (mVenueItems.size() > 0) {
            mStateHasRunShowTime = true;
            if(mVenueItems != null) {
                mVenueAdapter.setVenueItems(mVenueItems);
            }
            setPageNumber();
            mIsLoading = false;
        }
    }

    private void setPageNumber() {
        mPageNumber = 1;
        for (int i = 0; i < mVenueItems.size(); i++) {
            if (i % 10 == 0) {
                mPageNumber++;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        FacebookUser user = SharedPrefUtil.getUserProfile(getActivity());
        if (user.getSelectedLocation() == null &
                !mStateHasRunShowTime && NetworkUtil.isNetworkOn(getActivity())) {
            clearVenueList();
            initShowVenueIntentService();
            mStateHasRunShowTime = true;
            if (user.getSelectedLocation() == null) {
                user.setSelectedLocation(mLocation);
                SharedPrefUtil.storeProfileDescription(user, getActivity());
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), 9000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            //Log.i("Out", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Config.REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackbar("Permission granted");

            } else {
                showSnackbar("Permission not granted!");
            }
        }
    }

    public void showSnackbar(String message) {
        mSnackBar = UiUtils.initSnackBar(getView(), message, getActivity());
        View view = mSnackBar.getView();
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSnackBar.setAction(R.string.general_cancel_button_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mSnackBar.show();
    }

    @Override
    public void onClick(View v, int position, float x, float y) {
        Gson gson = new Gson();

        Venue venue = mVenueAdapter.getVenues().get(position);
        ImageView eventCover = (ImageView) v.findViewById(R.id.ivPosterImage);
        Intent intent = new Intent(getActivity(), ItemVenueActivity.class);
        intent.putExtra(Config.VENUES, gson.toJson(venue));
        BitmapDrawable mBitMapDrawable = (BitmapDrawable) eventCover
                .getDrawable();
        if (venue.isVenueReady() && mBitMapDrawable != null) {
            sPhotoCache.put(0, mBitMapDrawable.getBitmap());
            UiUtils.startTransitionSharedElement(v, getActivity(), position,
                    intent);
        } else {
            Toast.makeText(getActivity(),
                    "Event is loading, please wait...", Toast.LENGTH_SHORT).show();
        }
    }

    private RecyclerView.OnScrollListener mRecyclerScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView == null || mVenueRecyclerView.getLayoutManager() == null) {
                return;
            }

            super.onScrolled(recyclerView, dx, dy);

            int topRowVerticalPosition =
                    (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
            mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            visibleItemCount = mVenueRecyclerView.getLayoutManager().getChildCount();
            totalItemCount = mVenueRecyclerView.getLayoutManager().getItemCount();
            pastVisiblesItems = ((GridLayoutManager) mVenueRecyclerView.getLayoutManager())
                    .findFirstVisibleItemPosition();

            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && !mIsLoading) {
                if (mTotal_pages >= mPageNumber) {
                    initShowVenueIntentService();
                    showLoadingLabel();
                    mIsLoading = true;
                }
            }
        }
    };
}
