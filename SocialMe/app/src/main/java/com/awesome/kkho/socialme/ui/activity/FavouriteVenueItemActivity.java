package com.awesome.kkho.socialme.ui.activity;

import android.support.v4.app.Fragment;

import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.ui.fragment.FavouriteItemEventFragment;

/**
 * Created by kkho on 01.02.2016.
 */
public class FavouriteVenueItemActivity extends BaseActivity {
    @Override
    protected Fragment onCreatePane() {
        return new FavouriteItemEventFragment();
    }
}