package com.awesome.kkho.socialme.ui.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.ui.activity.GeoMapActivity;
import com.awesome.kkho.socialme.ui.activity.HomeActivity;
import com.awesome.kkho.socialme.util.Config;
import com.awesome.kkho.socialme.util.AddFavouriteItem;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkho on 10.01.2016.
 */
public class ItemEventFragment extends Fragment implements Palette.PaletteAsyncListener {
    @Bind(R.id.detailed_image)
    ImageView mEventImage;
    @Bind(R.id.event_title)
    TextView mTitleTextView;
    @Bind(R.id.description_event)
    TextView mEventDescription;
    @Bind(R.id.start_date_text)
    TextView mStartDateText;
    @Bind(R.id.venue_address)
    TextView mVenueAddress;
    @Bind(R.id.frame_bar_info)
    FrameLayout mFragmeLayoutInfo;
    @Bind(R.id.add_event)
    Button mFavouriteButton;

    FloatingActionButton mMapButton;
    Intent mShareIntent;

    private ShareActionProvider mShareActionProvider;

    private Event mEvent;
    private String mSerializedLocationJson;

    Toolbar mToolbar;
    Gson mGson = new Gson();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(savedInstanceState != null) {
            mEvent = (Event) savedInstanceState.getSerializable(Config.EVENTS);
        }

        if(getArguments().size() > 0) {
            mEvent = mGson.fromJson(getArguments()
                    .getString(Config.EVENTS), Event.class);
        }

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, viewRoot);
        mTitleTextView.setText(mEvent.getTitle());
        if (mEvent.getDescription() != null) {
            mEventDescription.setText(Html.fromHtml(mEvent.getDescription()));
        }
        if (AddFavouriteItem.getEvents(getActivity()).contains(mEvent.getId())) {
            mFavouriteButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

        mMapButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab_map);
        mFavouriteButton.setOnClickListener(new OnActionButtonClick());
        mMapButton.setOnClickListener(new OnActionButtonClick());
        mStartDateText.setText(mEvent.getStart_time());
        mVenueAddress.setText(mEvent.getVenue_address() + "," + mEvent.getCity_name()
                + ", " + mEvent.getCountry_name());
        Location location = new Location("");
        location.setLatitude(mEvent.getLatitude());
        location.setLongitude(mEvent.getLongitude());
        mSerializedLocationJson = new Gson().toJson(location);

        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bitmap bitmap = HomeFragment.sPhotoCache.get(0);
        showEventCover(bitmap);
    }

    public void showEventCover(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            Intent i = new Intent(getActivity(), HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(i);
            return;
        }
        mEventImage.setImageBitmap(bitmap);
        new Palette.Builder(bitmap)
                .maximumColorCount(24)
                .generate(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_item_event, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "An event that I am interested in: " + mEvent.getTitle() + "\n in : " + mVenueAddress.getText());
                getActivity().startActivity(shareIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGenerated(Palette palette) {

        if (palette != null) {
            final Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
            final Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
            final Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
            final Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
            final Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

            final Palette.Swatch backgroundAndContentColors = (darkVibrantSwatch != null)
                    ? darkVibrantSwatch : darkMutedSwatch;

            final Palette.Swatch titleAndFabColors = (darkVibrantSwatch != null)
                    ? lightVibrantSwatch : lightMutedSwatch;
            setBackgroundAndFabContentColors(backgroundAndContentColors);
            setHeadersTiltColors(titleAndFabColors);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Config.EVENTS, mEvent);
        super.onSaveInstanceState(outState);
    }

    public void setBackgroundAndFabContentColors(Palette.Swatch swatch) {
        if (swatch != null) {
            int rgb = swatch.getRgb();
            if (mEventDescription != null) {
                mEventDescription.setTextColor(rgb);
            }
            mMapButton.setBackgroundTintList(ColorStateList.valueOf(rgb));
            mFavouriteButton.setBackgroundColor(rgb);
            mFragmeLayoutInfo.setBackgroundColor(rgb);
            mToolbar.setBackgroundColor(rgb);
            mMapButton.setBackgroundColor(rgb);
        }
    }


    public void setHeadersTiltColors(Palette.Swatch swatch) {
        if (swatch != null) {
            int rgb = swatch.getRgb();
            mTitleTextView.setTextColor(rgb);
            mToolbar.setTitleTextColor(rgb);
        }
    }

    private class OnActionButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_event:
                    if (!AddFavouriteItem.getEvents(getActivity()).contains(mEvent.getId())) {
                        AddFavouriteItem.storeEvent(getActivity(), mEvent);
                        mFavouriteButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        mFavouriteButton.invalidate();
                        Snackbar.make(getView(), "Event added", Snackbar.LENGTH_LONG)
                                .setAction("Dismiss", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                    } else {
                        AddFavouriteItem.deleteEvent(getActivity(), mEvent);
                        mFavouriteButton.getBackground().setColorFilter(null);
                        mFavouriteButton.invalidate();
                    }
                    break;
                case R.id.fab_map:
                    Intent intent = new Intent(getActivity(), GeoMapActivity.class);
                    intent.putExtra(Config.EVENTS, mGson.toJson(mEvent));
                    intent.putExtra(Config.LOCATION, mSerializedLocationJson);
                    startActivity(intent);
                    break;
            }
        }
    }
}
