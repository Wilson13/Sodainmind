package com.wilson.sodainmind.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.facebook.samples.zoomable.ZoomableDraweeView;
import com.wilson.sodainmind.others.ShareInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static com.wilson.sodainmind.others.ShareInstance.FULL_SCREEN_POSITION_KEY;
import static com.wilson.sodainmind.others.ShareInstance.FULL_SCREEN_SCROLLED_KEY;

public class FullScreenActivity extends FragmentActivity {

    private static final String TAG ="FullScreenActivity";
    @BindView(R.id.vp_fullscreen) ViewPager fullscreenVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        hideStatusBar();
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);
        showFullScreenImage();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(FULL_SCREEN_SCROLLED_KEY, fullscreenVP.getCurrentItem());
        setResult(Activity.RESULT_OK, resultIntent);
        super.onBackPressed();
    }

    private void hideStatusBar() {
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void showFullScreenImage() {
        int pos = getIntent().getExtras().getInt(FULL_SCREEN_POSITION_KEY);
        final PagerAdapter adapter = new CustomAdapter(this, ShareInstance.getPhotoURL());
        fullscreenVP.setAdapter(adapter);
        fullscreenVP.setCurrentItem(pos);
    }

    private class CustomAdapter extends PagerAdapter {

        private List<String> photoURL;
        private LayoutInflater inflater;

        public CustomAdapter(Context context, List<String> photoURL) {
            this.photoURL = photoURL;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.pageadapter_item, view, false);
            view.addView(imageLayout, 0);

            ZoomableDraweeView fullScreenZDV = imageLayout.findViewById(R.id.zdv_fullscreen);
            String url = photoURL.get(position);

            if(url != null && url.length() > 0) {
                Uri uri = Uri.parse(url);
                DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(
                        uri).setTapToRetryEnabled(false).build();
                fullScreenZDV.setController(ctrl);
            }
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return photoURL.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
