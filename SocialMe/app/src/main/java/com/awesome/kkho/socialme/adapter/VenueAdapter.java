package com.awesome.kkho.socialme.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.model.Venue;
import com.awesome.kkho.socialme.util.ColorUtil;
import com.awesome.kkho.socialme.util.ImageLoader;
import com.awesome.kkho.socialme.util.RecyclerViewClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkho on 31.01.2016.
 */
public class VenueAdapter  extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {
    private RecyclerViewClickListener mRecyclerClickListener;
    private List<Venue> mVenueItems;
    public Context mContext;
    public ImageLoader mImageLoader;

    public VenueAdapter(Context context, List<Venue> venue) {
        mContext = context;
        mVenueItems = venue;
        mImageLoader = new ImageLoader(mContext, R.drawable.image_placeholder);
    }

    public void setRecyclerListListener(RecyclerViewClickListener mRecyclerClickListener) {
        this.mRecyclerClickListener = mRecyclerClickListener;
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.eventitem_layout, parent, false);
        return new VenueViewHolder(mItemView, mRecyclerClickListener);
    }

    @Override
    public void onBindViewHolder(final VenueViewHolder holder, int position) {
        final Venue venue = mVenueItems.get(position);
        holder.vEventNameText.setText(venue.getName());
        if (venue.getImage() != null) {
            final String posterURL = venue.getImage().getMedium().getUrl();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                holder.vPosterImage.setTransitionName("cover" + position);

            Picasso.with(mContext)
                    .load(posterURL)
                    .fit().centerInside()
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.venue)
                    .into(holder.vPosterImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            venue.setVenueReady(true);
                            holder.vMovieProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.vPosterImage.setImageResource(R.drawable.venue);
                            holder.vMovieProgressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            holder.vPosterImage.setImageResource(R.drawable.venue);
            holder.vMovieProgressBar.setVisibility(View.GONE);
            venue.setVenueReady(true);
        }
    }

    public void appendVenues(List<Venue> venueList) {
        Iterator<Venue> itr = venueList.iterator();
        Event event;
        mVenueItems.addAll(venueList);
        notifyDataSetChanged();
    }

    public void setVenueItems(List<Venue> venues) {
        mVenueItems = new ArrayList<Venue>();
        if (venues == null) {
            notifyDataSetChanged();
            return;
        }

        Set<Venue> noDups = new HashSet<>();
        noDups.addAll(venues);
        mVenueItems = new ArrayList<Venue>(noDups);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mVenueItems != null ? mVenueItems.size() : 0;
    }

    public List<Venue> getVenues() {
        return mVenueItems;
    }

    public void clearEvents() {
        mVenueItems.clear();
        this.notifyDataSetChanged();
    }

    public class VenueViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private final RecyclerViewClickListener onClickListener;
        @Bind(R.id.event_progressbar)
        ProgressBar vMovieProgressBar;
        @Bind(R.id.ivPosterImage)
        ImageButton vPosterImage;
        @Bind(R.id.event_title)
        TextView vEventNameText;

        public VenueViewHolder(View itemView, RecyclerViewClickListener onClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            vPosterImage.setDrawingCacheEnabled(true);
            vPosterImage.setOnTouchListener(this);
            ColorUtil.colorProgressBar(vMovieProgressBar, mContext);
            this.onClickListener = onClickListener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {
                onClickListener.onClick(v, getLayoutPosition(), event.getX(), event.getY());
            }
            return true;
        }
    }
}