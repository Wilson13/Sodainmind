package com.wilson.sodainmind.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.facebook.samples.zoomable.ZoomableDraweeView;
import com.wilson.sodainmind.others.Constants;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class FullScreenActivity extends FragmentActivity {

    private static final String TAG ="FullScreenActivity";
    @BindView(R.id.zdv_fullscreen) ZoomableDraweeView fullScreenZDV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFullScreenImage();
    }

    private void showFullScreenImage() {
        File path = new File(getFilesDir(), File.separator + Constants.IMAGE_FOLDER);
        File imageFile = new File(path, Constants.IMAGE_NAME);

        if (imageFile.exists()) {
            try {
            Uri uri = Uri.fromFile(imageFile);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            // Evicting from cache to display a newly saved Image
            Fresco.getImagePipeline().evictFromCache(uri);

                DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(
                        uri).setTapToRetryEnabled(true).build();
                GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .setProgressBarImage(new ProgressBarDrawable())
                        .build();

                fullScreenZDV.setController(ctrl);
                fullScreenZDV.setHierarchy(hierarchy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }
}
