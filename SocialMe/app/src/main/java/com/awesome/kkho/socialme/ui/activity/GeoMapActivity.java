package com.awesome.kkho.socialme.ui.activity;

import android.support.v4.app.Fragment;

import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.ui.fragment.GeoMapFragment;

/**
 * Created by kkho on 27.01.2016.
 */
public class GeoMapActivity extends BaseActivity {
    @Override
    protected Fragment onCreatePane() {
        GeoMapFragment fragment = new GeoMapFragment();
        return fragment;
    }
}
