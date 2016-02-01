package com.awesome.kkho.socialme.ui.activity;

import android.support.v4.app.Fragment;

import com.awesome.kkho.socialme.receiver.ServiceReceiver;
import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.ui.fragment.HomeFragment;

/**
 * Created by kkho on 13.01.2016.
 */
public class HomeActivity extends BaseActivity {
    @Override
    protected Fragment onCreatePane() {
        return HomeFragment.newInstance();
    }
}
