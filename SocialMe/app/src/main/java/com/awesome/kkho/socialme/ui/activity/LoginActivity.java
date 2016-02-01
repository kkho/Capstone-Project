package com.awesome.kkho.socialme.ui.activity;

import android.support.v4.app.Fragment;

import com.awesome.kkho.socialme.ui.BaseActivity;
import com.awesome.kkho.socialme.ui.NoSupportBaseActivity;
import com.awesome.kkho.socialme.ui.fragment.LoginFragment;

/**
 * Created by kkho on 13.01.2016.
 */
public class LoginActivity extends NoSupportBaseActivity {
    @Override
    protected android.app.Fragment onCreatePane() {
        return LoginFragment.newInstance();
    }
}
