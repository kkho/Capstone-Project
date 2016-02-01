package com.awesome.kkho.socialme.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.awesome.kkho.socialme.adapter.VenueAdapter;
import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.model.Venue;
import com.awesome.kkho.socialme.ui.activity.FavouriteEventItemActivity;
import com.awesome.kkho.socialme.ui.activity.FavouriteVenueItemActivity;
import com.awesome.kkho.socialme.util.AddFavouriteItem;
import com.awesome.kkho.socialme.util.Config;
import com.awesome.kkho.socialme.util.RecyclerViewClickListener;
import com.awesome.kkho.socialme.util.UiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkho on 31.01.2016.
 */
public class FavouriteVenueFragment extends Fragment implements
        RecyclerViewClickListener {
    private VenueAdapter mVenueAdapter;

    @Bind(R.id.favourite_recyclerview)
    RecyclerView mVenueRecyclerView;

    public static SparseArray<Bitmap> sPhotoCache = new SparseArray<>(1);

    public FavouriteVenueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, viewRoot);
        initRecyclerView(AddFavouriteItem.getVenues(getActivity()));
        return viewRoot;
    }


    public void initRecyclerView(List<Venue> venueItems) {
        if (mVenueRecyclerView != null) {
            mVenueAdapter = new VenueAdapter(getActivity(), venueItems);
            mVenueAdapter.setRecyclerListListener(this);
            mVenueRecyclerView.setHasFixedSize(true);
            mVenueRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mVenueRecyclerView.setAdapter(mVenueAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        sPhotoCache.clear();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v, int position, float x, float y) {
        Gson gson = new Gson();

        Venue venue = mVenueAdapter.getVenues().get(position);
        ImageView eventCover = (ImageView) v.findViewById(R.id.ivPosterImage);
        Intent intent = new Intent(getActivity(), FavouriteVenueItemActivity.class);
        intent.putExtra(Config.EVENTS, gson.toJson(venue));
        BitmapDrawable mBitMapDrawable = (BitmapDrawable) eventCover
                .getDrawable();
        if (venue.isVenueReady() && mBitMapDrawable != null) {
            sPhotoCache.put(0, mBitMapDrawable.getBitmap());
            UiUtils.startTransitionSharedElement(v, getActivity(), position,
                    intent);
        } else {
            Toast.makeText(getActivity(),
                    "Venue is loading, please wait...", Toast.LENGTH_SHORT).show();
        }
    }
}