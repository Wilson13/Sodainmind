package com.wilson.sodainmind.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    @BindView(R.id.tv_title) TextView titleTV;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPlaceAutoComplete();
        initNavigation();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fl_main) != null) {
            // Not restored from previous state
            if (savedInstanceState == null) {
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_main, MainFragment.newInstance()).commit();
            } else {
                Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);
                if (displayFragment != null && displayFragment instanceof  PhotoFragment) {
                    // Change title
                    titleTV.setText(R.string.nav_photo);
                    // Need to re-hide search box if restored from previous state
                    autocompleteFragment.getView().setVisibility(View.GONE);
                }
            }
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
        Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else if (displayFragment instanceof MainFragment) {
            showExitDialog();
        } else if (displayFragment instanceof PhotoFragment) {
            // Show main fragment if not displayed
            showHomePage();
        }
    }

    private void initPlaceAutoComplete() {
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

    private void initNavigation() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!(displayFragment instanceof MainFragment)) {
                            showHomePage();
                        }
                        // Close drawer after home page displayed or if user selected same page
                        mDrawerLayout.closeDrawer(Gravity.START);
                        break;
                    case R.id.nav_photo:
                        if (!(displayFragment instanceof PhotoFragment)) {
                            showPhotoPage();
                        }
                        // Close drawer after photo page displayed or if user selected same page
                        mDrawerLayout.closeDrawer(Gravity.START);
                        break;
                }
                return false;
            }
        });
    }

    private void showHomePage() {
        // Show maim fragment if not displayed
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main, MainFragment.newInstance()).commit();

        // Change title
        titleTV.setText(R.string.nav_home);
        // Show search box
        autocompleteFragment.getView().setVisibility(View.VISIBLE);
    }

    private void showPhotoPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main, PhotoFragment.newInstance()).commit();

        // Change title
        titleTV.setText(R.string.nav_photo);
        // Hide search box
        autocompleteFragment.getView().setVisibility(View.GONE);
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_exit)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()
                .show();
    }
}
