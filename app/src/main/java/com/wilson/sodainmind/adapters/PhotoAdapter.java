package com.wilson.sodainmind.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.activities.FullScreenActivity;
import com.wilson.sodainmind.others.Constants;
import com.wilson.sodainmind.others.VolleySingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private static final String TAG = "PhotoAdapter";
    private Context context;
    private List<String> urlList;
    private Bitmap bitmap;
    public PhotoAdapter(List<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_photo_item, parent, false);
        context = parent.getContext();
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        Log.d(TAG, "url: " + urlList.get(position));
        String url = urlList.get(position);
        NetworkImageView niv = holder.photoNIV;

        if(url != null && url.length() > 0) {
            niv.setImageUrl(url, VolleySingleton.getInstance(context).getImageLoader());
            niv.setDefaultImageResId(R.drawable.default_placeholder);
            niv.setErrorImageResId(R.drawable.error_placeholder);
            niv.setTag(url);
            // Set up OnClickListener
            niv.setOnClickListener(new FullScreenListener(niv));
        }
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    private void saveImage(Bitmap bitmap) {
        File path = new File(context.getFilesDir(), File.separator + Constants.IMAGE_FOLDER);
        path.mkdirs();
        File imageFile = new File(path, Constants.IMAGE_NAME);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile, false);
            // PNG which is lossless, will ignore the quality setting
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.w(TAG, "Could not resolve file stream: " + e);
        }
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        NetworkImageView photoNIV;

        PhotoHolder(View itemView) {
            super(itemView);
            photoNIV = itemView.findViewById(R.id.niv_photo);
        }
    }

    private class FullScreenListener implements View.OnClickListener {

        private NetworkImageView niv;

        public FullScreenListener(NetworkImageView niv) {
            this.niv = niv;
        }

        @Override
        public void onClick(View view) {
            Bitmap bm = ((BitmapDrawable) niv.getDrawable()).getBitmap();
            if (bm != null) {
                saveImage(bm);
                Log.d(TAG, "niv: " + niv.getTag());
                Intent fullScreenIntent = new Intent(context, FullScreenActivity.class);
                context.startActivity(fullScreenIntent);
            }
        }
    }
}
