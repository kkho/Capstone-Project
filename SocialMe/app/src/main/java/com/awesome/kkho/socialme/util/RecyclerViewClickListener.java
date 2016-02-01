package com.awesome.kkho.socialme.util;

import android.view.View;

/**
 * Created by kkho on 10.01.2016.
 */
public interface RecyclerViewClickListener {

    /**
     * Callback method to be invoked when a item in a
     * RecyclerView is clicked
     *
     * @param v        The view within the RecyclerView.Adapter
     * @param position The position of the view in the adapter
     * @param x
     * @param y
     */
    public void onClick(View v, int position, float x, float y);
}
