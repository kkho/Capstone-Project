package com.awesome.kkho.socialme.ui.activity;

import android.support.v4.app.Fragment;
import android.view.View;

import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.ui.fragment.ItemEventFragment;

/**
 * Created by kkho on 24.01.2016.
 */
public class ItemEventActivity extends BaseActivity {
    @Override
    protected Fragment onCreatePane() {
        return new ItemEventFragment();
    }
}
