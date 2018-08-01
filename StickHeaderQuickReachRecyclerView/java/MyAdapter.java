package com.example.haojun.nationcodedemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context mContext;
    private List<Country> countries;
    private String selectedCode;

    public MyAdapter(Context mContext) {
        this.mContext = mContext;
        this.countries = new ArrayList<>();
    }

    public void setSelectedCode(String code) {
        if (code.isEmpty()) return;
        if (code.equals(selectedCode)) {
            selectedCode = null;
        } else {
            selectedCode = code;
            SPHelper.setCode(mContext, selectedCode);
        }
        notifyDataSetChanged();
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_country_body, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.countryName.setText(country.getName());
        holder.countryDialCode.setText(country.getDialCode());
        holder.checked.setVisibility(country.getCode().equals(selectedCode) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView countryName;
        TextView countryDialCode;
        View checked;

        ViewHolder(View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.tv_item_country_body_name);
            countryDialCode = itemView.findViewById(R.id.tv_item_country_body_dial_code);
            checked = itemView.findViewById(R.id.iv_item_country_body_checked);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            setSelectedCode(countries.get(getAdapterPosition()).getCode());

        }
    }
}