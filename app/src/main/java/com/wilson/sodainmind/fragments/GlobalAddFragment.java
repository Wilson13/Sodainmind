package com.wilson.sodainmind.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.sodainmind.DBFlow.CountryDBModel;
import com.wilson.sodainmind.DBFlow.DBHelper;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.adapters.GlobalAddAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class GlobalAddFragment extends Fragment implements TextWatcher {

    private static final String TAG = "GlobalAddFragment";
    private GlobalAddAdapter globalAddAdapter;
    private List<CountryDBModel> countryModelList = new ArrayList<>();
    @BindView(R.id.rv_country) RecyclerView countryRV;

    public static final GlobalAddFragment newInstance()
    {
        return new GlobalAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global_add, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        Log.d(TAG, "onViewCreated");
        globalAddAdapter = new GlobalAddAdapter(countryModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        countryRV.setLayoutManager(mLayoutManager);
        countryRV.setAdapter(globalAddAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        countryModelList.clear();
        countryModelList.addAll(DBHelper.getAllCountries());
        if (countryRV.getAdapter() == null) {
            globalAddAdapter = new GlobalAddAdapter(countryModelList);
        }
        globalAddAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @OnTextChanged(value = R.id.tv_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    @Override
    public void afterTextChanged(Editable editable) {
        countryModelList.clear();
        countryModelList.addAll(DBHelper.getAllCountries());
        if (countryRV.getAdapter() == null) {
            globalAddAdapter = new GlobalAddAdapter(countryModelList);
        }
        globalAddAdapter.notifyDataSetChanged();
    }
}
