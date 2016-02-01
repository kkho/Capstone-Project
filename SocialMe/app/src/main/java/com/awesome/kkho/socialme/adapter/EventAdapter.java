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
 * Created by kkho on 24.01.2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private RecyclerViewClickListener mRecyclerClickListener;
    private List<Event> mEventItems;
    public Context mContext;
    public ImageLoader mImageLoader;

    public EventAdapter(Context context, List<Event> events) {
        mContext = context;
        mEventItems = events;
        mImageLoader = new ImageLoader(mContext, R.drawable.image_placeholder);
        notifyDataSetChanged();
    }

    public void setRecyclerListListener(RecyclerViewClickListener mRecyclerClickListener) {
        this.mRecyclerClickListener = mRecyclerClickListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.eventitem_layout, parent, false);
        return new EventViewHolder(mItemView, mRecyclerClickListener);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        final Event event = mEventItems.get(position);
        holder.vEventNameText.setText(event.getTitle());
        if(event.getImage() != null) {
            final String posterURL = event.getImage().getMedium().getUrl();
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
                            event.setEventReady(true);
                            holder.vMovieProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.vPosterImage.setImageResource(R.drawable.venue);
                            holder.vMovieProgressBar.setVisibility(View.GONE);
                        }
                    });
        }
        else {
            holder.vPosterImage.setImageResource(R.drawable.venue);
            holder.vMovieProgressBar.setVisibility(View.GONE);
            event.setEventReady(true);
        }
    }

    public void appendEventItems(List<Event> eventList) {
        Iterator<Event> itr = eventList.iterator();
        Event event;
        mEventItems.addAll(eventList);
        notifyDataSetChanged();
    }

    public void setEventItems(List<Event> events) {
        mEventItems = new ArrayList<Event>();
        if(events == null) {
            notifyDataSetChanged();
            return;
        }

        Set<Event> noDups = new HashSet<>();
        noDups.addAll(events);
        mEventItems = new ArrayList<Event>(noDups);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mEventItems != null ? mEventItems.size() : 0;
    }

    public List<Event> getEvents() {
        return mEventItems;
    }

    public void clearEvents() {
        mEventItems.clear();
        this.notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private final RecyclerViewClickListener onClickListener;
        @Bind(R.id.event_progressbar)
        ProgressBar vMovieProgressBar;
        @Bind(R.id.ivPosterImage)
        ImageButton vPosterImage;
        @Bind(R.id.event_title)
        TextView vEventNameText;

        public EventViewHolder(View itemView, RecyclerViewClickListener onClickListener) {
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
