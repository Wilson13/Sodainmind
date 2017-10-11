package com.wilson.sodainmind.adapters;

import android.graphics.drawable.Animatable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilson.sodainmind.DBFlow.CountryDBModel;
import com.wilson.sodainmind.R;

import java.util.List;

public class GlobalAddAdapter extends RecyclerView.Adapter<GlobalAddAdapter.GlobalCountryHolder> {

    private static final String TAG = "GlobalCountryAdapter";
    private List<CountryDBModel> countryModelList;

    public GlobalAddAdapter(List<CountryDBModel> countryModelList) {
        this.countryModelList = countryModelList;
    }

    @Override
    public GlobalCountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_global_country_item, parent, false);
        return new GlobalCountryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final GlobalCountryHolder holder, int position) {

        // Get date from list of global weather items at corresponding position
        CountryDBModel currentItem = countryModelList.get(position);
        String weatherCountry = currentItem.getName();

        // Display the data
        holder.countryTV.setText(weatherCountry);

        // Display selected state
        if (currentItem.isSelected()) {
            //holder.checkIV.setImageResource(R.drawable.avd_check);
        } else {
            holder.checkIV.setImageResource(R.drawable.circle_blank);
        }

        // Set up click listener
        holder.countrySelectLL.setOnClickListener(new CheckBoxListener(holder.checkIV, currentItem));
        holder.checkIV.setOnClickListener(new CheckBoxListener(holder.checkIV, currentItem));
    }

    @Override
    public int getItemCount() {
        return countryModelList.size();
    }

    class GlobalCountryHolder extends RecyclerView.ViewHolder {

        private LinearLayout countrySelectLL;
        TextView countryTV;
        ImageView checkIV;

        GlobalCountryHolder(View itemView) {
            super(itemView);
            countryTV = itemView.findViewById(R.id.tv_country);
            checkIV = itemView.findViewById(R.id.iv_check);
            countrySelectLL = itemView.findViewById(R.id.rl_country_select);
        }
    }

    private class CheckBoxListener implements View.OnClickListener {

        private ImageView checkIV;
        private CountryDBModel countryDBModel;

        private CheckBoxListener(ImageView checkIV, CountryDBModel countryDBModel) {
            this.checkIV = checkIV;
            this.countryDBModel = countryDBModel;
        }

        @Override
        public void onClick(View view) {
            if (countryDBModel.isSelected()) {
                checkIV.setImageResource(R.drawable.circle_blank);
                countryDBModel.setSelected(false);
                countryDBModel.save();
            } else {
                //checkIV.setImageResource(R.drawable.avd_check);
                countryDBModel.setSelected(true);
                countryDBModel.save();
                Animatable anim = (Animatable) checkIV.getDrawable();
                anim.start();
            }
        }
    }
}
