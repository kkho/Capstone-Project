package com.awesome.kkho.socialme.ui.activity;

import android.support.v4.app.Fragment;

import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.ui.fragment.ItemEventFragment;
import com.awesome.kkho.socialme.ui.fragment.ItemVenueFragment;

/**
 * Created by kkho on 31.01.2016.
 */
public class ItemVenueActivity extends BaseActivity {
    @Override
    protected Fragment onCreatePane() {
        return new ItemVenueFragment();
    }
}