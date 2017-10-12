package com.wilson.sodainmind.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.fragments.MainFragment;
import com.wilson.sodainmind.fragments.PhotoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG ="MainActivity";
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.iv_menu) ImageView menuIV;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fl_main) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_main, MainFragment.newInstance()).commit();

            // Not very clear about this but SupportPlaceAutocompleteFragment seems not
            // to be working hence PlaceAutocompleteFragment is used and can't be nested
            // in another fragment.
            autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);
                    if (displayFragment instanceof MainFragment) {
                        ((MainFragment)displayFragment).zoomInLocation(place.getLatLng(), place.getName().toString());
                    }
                }

                @Override
                public void onError(Status status) {
                    Log.e(TAG, "An error occurred: " + status);
                }
            });
        }
    }

    @OnClick({R.id.iv_menu})
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.iv_menu:
                if (mDrawerLayout.isDrawerOpen(Gravity.START))
                    mDrawerLayout.closeDrawer(Gravity.START);
                else
                    mDrawerLayout.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } //else if (displayFragment instanceof ) {

        //}
    }

    private void init() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!(displayFragment instanceof MainFragment)) {
                            // Show maim fragment if not displayed
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fl_main, MainFragment.newInstance()).commit();

                            // Show search box
                            autocompleteFragment.getView().setVisibility(View.VISIBLE);
                            // Close drawer after actions finished
                            mDrawerLayout.closeDrawer(Gravity.START);
                        } else {
                            // Close drawer if user selected same page
                            mDrawerLayout.closeDrawer(Gravity.START);
                        }
                        break;
                    case R.id.nav_global:
                        if (!(displayFragment instanceof PhotoFragment)) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fl_main, PhotoFragment.newInstance()).commit();

                            // Hide search box
                            autocompleteFragment.getView().setVisibility(View.GONE);

                            // Close drawer after actions finished
                            mDrawerLayout.closeDrawer(Gravity.START);
                        } else {
                            // Close drawer after actions finished
                            mDrawerLayout.closeDrawer(Gravity.START);
                        }
                        break;
                }
                return false;
            }
        });
    }
}
