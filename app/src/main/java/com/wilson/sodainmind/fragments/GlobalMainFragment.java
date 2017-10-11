package com.wilson.sodainmind.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wilson.sodainmind.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

;

public class GlobalMainFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "GlobalMainFragment";
    private GlobalWeatherFragment globalWeatherFragment;
    private GlobalAddFragment globalAddFragment;
    @BindView(R.id.tv_title) TextView titleTV;
    @BindView(R.id.iv_add) ImageButton addIV;

    public static final GlobalMainFragment newInstance()
    {
        return new GlobalMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }
        init();
    }

    @OnClick({R.id.iv_add})
    @Override
    public void onClick(View view) {
        Log.d(TAG, "clicked");
        switch(view.getId()) {
            case R.id.iv_add:
                Log.d(TAG, "iv add clicked");
                // If GlobalAddFragment wasn't displayed, show it.
                if (!addIV.isSelected()) {
                    addIV.animate().rotation(135);
                    addIV.setSelected(true);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fl_global_main, globalAddFragment).commit();
                } else {
                    addIV.animate().rotation(0);
                    addIV.setSelected(false);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fl_global_main, globalWeatherFragment).commit();
                }
                break;
        }
    }

    private void init() {
        Typeface nowayRegular = Typeface.createFromAsset(getActivity().getAssets(), "noway_regular.otf");
        titleTV.setTypeface(nowayRegular);

        // Initialize fragments. Saved in global variable for reusing.
        globalWeatherFragment = globalWeatherFragment.newInstance();
        globalAddFragment = GlobalAddFragment.newInstance();

        // Initialize view
        Fragment displayFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (!(displayFragment instanceof GlobalWeatherFragment)) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_global_main, globalWeatherFragment).commit();
        }
    }
}
