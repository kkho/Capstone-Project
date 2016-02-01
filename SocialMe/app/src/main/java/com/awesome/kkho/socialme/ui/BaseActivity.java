package com.awesome.kkho.socialme.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.FacebookUser;
import com.awesome.kkho.socialme.ui.activity.FavouriteEventItemActivity;
import com.awesome.kkho.socialme.ui.activity.FavouriteVenueItemActivity;
import com.awesome.kkho.socialme.ui.activity.GeoMapActivity;
import com.awesome.kkho.socialme.ui.activity.HomeActivity;
import com.awesome.kkho.socialme.ui.activity.ItemEventActivity;
import com.awesome.kkho.socialme.ui.activity.ItemVenueActivity;
import com.awesome.kkho.socialme.ui.activity.LoginActivity;
import com.awesome.kkho.socialme.ui.fragment.FavouriteEventFragment;
import com.awesome.kkho.socialme.ui.fragment.FavouriteVenueFragment;
import com.awesome.kkho.socialme.ui.fragment.HomeFragment;
import com.awesome.kkho.socialme.ui.fragment.LocationMapFragment;
import com.awesome.kkho.socialme.ui.fragment.VenueFragment;
import com.awesome.kkho.socialme.util.SharedPrefUtil;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by kkho on 10.01.2016.
 */
public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mFragment;
    public static final String ARG_URI = "_uri";
    public DrawerLayout mDrawer;
    public ActionBarDrawerToggle mToggle;
    public NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if ((this instanceof ItemEventActivity) || (this instanceof GeoMapActivity)
                || (this instanceof ItemVenueActivity) || (this instanceof FavouriteEventItemActivity)
                || (this instanceof FavouriteVenueItemActivity)) {
            setContentView(R.layout.activity_base_no_drawer);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getTitle());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (!(this instanceof ItemEventActivity) && !(this instanceof GeoMapActivity)
                && !(this instanceof ItemVenueActivity) && !(this instanceof FavouriteEventItemActivity)
                && !(this instanceof FavouriteVenueItemActivity)) {

            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            mToggle = new ActionBarDrawerToggle(
                    this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawer.setDrawerListener(mToggle);
            mToggle.syncState();

            mNavigationView = (NavigationView) findViewById(R.id.nav_view);
            mNavigationView.setNavigationItemSelectedListener(this);
            setupNavigationViewInformation();
            if (this instanceof HomeActivity) {
                mNavigationView.getMenu().getItem(0).setChecked(true);
            }
        }

        if (savedInstanceState == null) {
            mFragment = onCreatePane();
            mFragment.setArguments(intentToFragmentArguments(getIntent()));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root_container, mFragment, "single_pane")
                    .commit();
        } else {
            mFragment = getSupportFragmentManager().findFragmentByTag("single_pane");
        }
    }

    protected abstract Fragment onCreatePane();

    public Fragment getFragment() {
        return mFragment;
    }

    public void setupNavigationViewInformation() {
        FacebookUser user = SharedPrefUtil.getUserProfile(this);
        View headerView = mNavigationView.getHeaderView(0);
        CircleImageView profileImageView = (CircleImageView) headerView.findViewById(R.id.profile_picture);
        TextView profileEmailTextView = (TextView) headerView.findViewById(R.id.profile_email);
        TextView profileNameTextView = (TextView) headerView.findViewById(R.id.profile_name);

        Picasso.with(this)
                .load(user.getPicture().getData().getUrl())
                .fit().centerInside()
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.venue)
                .into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });

        profileEmailTextView.setText(user.getEmail());
        profileNameTextView.setText(user.getFirst_name() + " " + user.getLast_name());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (this instanceof HomeActivity) {
                    return false;
                }

                if (this instanceof ItemEventActivity || this instanceof FavouriteEventItemActivity
                        || this instanceof ItemVenueActivity || this instanceof FavouriteVenueItemActivity) {
                    supportFinishAfterTransition();
                    return true;
                }

                if (this instanceof GeoMapActivity) {
                    finish();
                    return true;
                }

                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer == null) {
            super.onBackPressed();
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_event) {
            mFragment = new HomeFragment();
        } else if (id == R.id.nav_location) {
            mFragment = new LocationMapFragment();
        } else if (id == R.id.nav_venue) {
            mFragment = new VenueFragment();
        } else if (id == R.id.nav_favourite_event) {
            mFragment = new FavouriteEventFragment();
        } else if (id == R.id.nav_favourite_venue) {
            mFragment = new FavouriteVenueFragment();
        } else if (id == R.id.nav_logout) {
            SharedPrefUtil.removeUserProfile(this);
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        mFragment.setArguments(intentToFragmentArguments(getIntent()));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.root_container, mFragment, "single_pane")
                .commit();
        return true;
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
