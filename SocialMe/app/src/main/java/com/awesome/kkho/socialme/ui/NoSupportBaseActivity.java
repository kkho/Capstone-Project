package com.awesome.kkho.socialme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.ui.activity.GeoMapActivity;
import com.awesome.kkho.socialme.ui.activity.HomeActivity;
import com.awesome.kkho.socialme.ui.activity.ItemEventActivity;
import com.awesome.kkho.socialme.ui.activity.LoginActivity;
import com.facebook.FacebookSdk;

/**
 * Created by kkho on 28.01.2016.
 */
public abstract class NoSupportBaseActivity extends Activity {

    private Fragment mFragment;
    public static final String ARG_URI = "_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            mFragment = onCreatePane();
            mFragment.setArguments(intentToFragmentArguments(getIntent()));
            getFragmentManager().beginTransaction()
                    .add(R.id.root_container, mFragment, "single_pane")
                    .commit();
        } else {
            mFragment = getFragmentManager().findFragmentByTag("single_pane");
        }
    }

    protected abstract Fragment onCreatePane();

    public Fragment getFragment() {
        return mFragment;
    }

    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable(ARG_URI, data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }
        return arguments;
    }

    public static Intent fragmentArgumentToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable(ARG_URI);
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra(ARG_URI);
        return intent;
    }
}
