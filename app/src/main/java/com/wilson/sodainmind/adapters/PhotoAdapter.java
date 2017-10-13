package com.wilson.sodainmind.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wilson.sodainmind.R;

import java.util.List;

import static com.wilson.sodainmind.others.ShareInstance.CLIPBOARD_URL;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private static final String TAG = "PhotoAdapter";
    private Context context;
    private List<String> urlList;
    private PhotoadapterListener delegate;

    public interface PhotoadapterListener {
        void fullScreenClick(int pos);
    }

    public PhotoAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_photo_item, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        final int pos = position;
        final String url = urlList.get(position);
        SimpleDraweeView zdv = holder.photoZDV;

        if(url != null && url.length() > 0) {
            Uri uri = Uri.parse(url);
            DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(
                    uri).setTapToRetryEnabled(false).build();

            zdv.setController(ctrl);
            // Click to enter full screen
            zdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent fullScreenIntent = new Intent(context, FullScreenActivity.class);
//                    Bundle mBundle = new Bundle();
//                    mBundle.putInt(FULL_SCREEN_POSITION_KEY, pos);
//                    fullScreenIntent.putExtras(mBundle);
                    delegate.fullScreenClick(pos);
                }
            });
            zdv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(CLIPBOARD_URL, url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context.getApplicationContext(), R.string.copy_clipboard, Toast.LENGTH_SHORT).show();
                    // Consume this event so as not to trigger onClick
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView photoZDV;

        PhotoHolder(View itemView) {
            super(itemView);
            photoZDV = itemView.findViewById(R.id.zdv_photo);
        }
    }

    public void setPhotoAdapterListener(PhotoadapterListener delegate) {
        this.delegate = delegate;
    }
}
