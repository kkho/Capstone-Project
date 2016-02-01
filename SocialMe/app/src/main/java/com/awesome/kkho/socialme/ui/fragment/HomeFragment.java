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
import com.awesome.kkho.socialme.adapter.EventAdapter;
import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.model.EventResponse;
import com.awesome.kkho.socialme.model.FacebookUser;
import com.awesome.kkho.socialme.model.Image;
import com.awesome.kkho.socialme.receiver.ServiceReceiver;
import com.awesome.kkho.socialme.service.EventIntentService;
import com.awesome.kkho.socialme.storage.SocialContract;
import com.awesome.kkho.socialme.ui.activity.ItemEventActivity;
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

public class HomeFragment extends Fragment implements ServiceReceiver.Listener,
        RecyclerViewClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static boolean mHasLoadedContent = false;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int mTotal_pages;
    private static boolean mStateHasRunShowTime = false;
    private int mPageNumber = 1;
    private ServiceReceiver mReceiver;
    private List<Event> mEventItems = new ArrayList<Event>();
    private EventResponse mEventResponseWrapper;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private Snackbar mSnackBar;
    private EventAdapter mEventAdapter;
    private boolean mIsLoading;

    @Bind(R.id.general_recyclerview)
    RecyclerView mEventRecyclerView;
    @Bind(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static SparseArray<Bitmap> sPhotoCache = new SparseArray<>(1);

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventItems = new ArrayList<Event>();
        mTotal_pages = SharedPrefUtil.getCurrentPageNumberEvent(getActivity());
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

        clearEventList();
        Loader loader = getActivity().getLoaderManager().getLoader(0);
        if (loader != null && loader.isReset() ) {
            getActivity().getLoaderManager().restartLoader(0, null, this);
        } else {
            getActivity().getLoaderManager().initLoader(0, null, this);
        }

        mStateHasRunShowTime = false;
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void initShowEventIntentService() {
        Intent intent = new Intent(getActivity(), EventIntentService.class);
        intent.putExtra(Config.LOCATION, SharedPrefUtil.getUserProfile(getActivity()).getSelectedLocation());
        intent.putExtra(Config.PAGE_NUMBER, mPageNumber);
        intent.putExtra(Config.RECEIVER, mReceiver);
        getActivity().startService(intent);

    }

    private void clearEventList() {
        mEventItems.clear();
        if (mEventAdapter != null) {
            mEventAdapter.clearEvents();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
        initRecyclerView(mEventItems);
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

    public void initRecyclerView(List<Event> eventItems) {
        if (mEventRecyclerView != null) {
            mEventAdapter = new EventAdapter(getActivity(), eventItems);
            mEventAdapter.setRecyclerListListener(this);
            mEventRecyclerView.setHasFixedSize(true);
            mEventItems = eventItems;
            mEventRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mEventRecyclerView.setAdapter(mEventAdapter);
            mEventRecyclerView.addOnScrollListener(mRecyclerScrollListener);
        }
    }

    private void refreshContent() {
        mPageNumber = 1;
        if (NetworkUtil.isNetworkOn(getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearEventList();
                    getActivity().getContentResolver().delete(SocialContract.EventDB.CONTENT_URI, null, null);
                    initShowEventIntentService();
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
        mEventAdapter.clearEvents();
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
                SocialContract.EventDB.CONTENT_URI, true, mObserver);
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
        mEventResponseWrapper = (EventResponse) resultData.getSerializable(Config.EVENTS);
        mPageNumber++;
        // add to adapter¨
        if (mEventResponseWrapper.getEvents() != null) {
            mEventItems = mEventResponseWrapper.getEvents().getEvent();
            if (mEventAdapter.getItemCount() < 1) {
                mEventAdapter.setEventItems(mEventItems);
            } else {
                mEventAdapter.appendEventItems(mEventItems);
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
                SocialContract.EventDB.CONTENT_URI,
                Config.EventQuery.PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getActivity() == null) return;

        if(!data.moveToNext()) {
            initShowEventIntentService();
            return;
        }

        Gson gson = new Gson();
        mEventItems = new ArrayList<>();
        while (data.moveToNext()) {
            Event event = new Event();
            event.setId(data.getString(Config.EventQuery.ID));
            event.setCity_name(data.getString(Config.EventQuery.CITY_NAME));
            event.setVenue_name(data.getString(Config.EventQuery.VENUE_NAME));
            event.setVenue_display(data.getString(Config.EventQuery.VENUE_DISPLAY));
            event.setCountry_name(data.getString(Config.EventQuery.COUNTRY_NAME));
            event.setRegion_name(data.getString(Config.EventQuery.REGION_NAME));
            event.setTitle(data.getString(Config.EventQuery.TITLE));
            event.setPerformers
                    (gson.fromJson(data.getString(Config.EventQuery.PERFORMERS), Object.class));
            event.setCreated(data.getString(Config.EventQuery.CREATED));
            event.setDescription(data.getString(Config.EventQuery.DESCRIPTION));
            event.setVenue_id(data.getString(Config.EventQuery.VENUE_ID));
            event.setAll_day(data.getString(Config.EventQuery.ALL_DAY));
            event.setLongitude(data.getDouble(Config.EventQuery.LONGITUDE));
            event.setLatitude(data.getDouble(Config.EventQuery.LATITUDE));
            event.setStop_time(data.getString(Config.EventQuery.STOP_TIME));
            event.setImage(gson.fromJson(data.getString(Config.EventQuery.IMAGEURL), Image.class));
            event.setVenue_url(data.getString(Config.EventQuery.VENUE_URL));
            event.setUrl(data.getString(Config.EventQuery.URL));
            event.setVenue_address(data.getString(Config.EventQuery.VENUE_ADDRESS));
            event.setPostal_code(data.getString(Config.EventQuery.POSTAL_CODE));
            event.setStart_time(data.getString(Config.EventQuery.START_TIME));
            mEventItems.add(event);
        }

        mTotal_pages = SharedPrefUtil.getCurrentPageNumberEvent(getActivity());
        if (mEventItems.size() > 0) {
            mStateHasRunShowTime = true;
            if(mEventAdapter != null) {
                mEventAdapter.setEventItems(mEventItems);
            }
            setPageNumber();
            mIsLoading = false;
        }
    }

    private void setPageNumber() {
        mPageNumber = 1;
        for (int i = 0; i < mEventItems.size(); i++) {
            if (i % 10 == 0) {
                mPageNumber++;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventAdapter.setEventItems(null);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        FacebookUser user = SharedPrefUtil.getUserProfile(getActivity());
        if (user.getSelectedLocation() == null &&
                (!mStateHasRunShowTime && NetworkUtil.isNetworkOn(getActivity()))) {
            clearEventList();
            getActivity().getContentResolver().delete(SocialContract.EventDB.CONTENT_URI, null, null);
            initShowEventIntentService();
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

        Event event = mEventAdapter.getEvents().get(position);
        ImageView eventCover = (ImageView) v.findViewById(R.id.ivPosterImage);
        Intent intent = new Intent(getActivity(), ItemEventActivity.class);
        intent.putExtra(Config.EVENTS, gson.toJson(event));
        BitmapDrawable mBitMapDrawable = (BitmapDrawable) eventCover
                .getDrawable();
        if (event.isEventReady() && mBitMapDrawable != null) {
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
            if (recyclerView == null || mEventRecyclerView.getLayoutManager() == null) {
                return;
            }

            super.onScrolled(recyclerView, dx, dy);

            int topRowVerticalPosition =
                    (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
            mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            visibleItemCount = mEventRecyclerView.getLayoutManager().getChildCount();
            totalItemCount = mEventRecyclerView.getLayoutManager().getItemCount();
            pastVisiblesItems = ((GridLayoutManager) mEventRecyclerView.getLayoutManager())
                    .findFirstVisibleItemPosition();

            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && !mIsLoading) {
                if (mTotal_pages >= mPageNumber) {
                    initShowEventIntentService();
                    showLoadingLabel();
                    mIsLoading = true;
                }
            }
        }
    };

}
