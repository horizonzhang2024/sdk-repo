package com.zhanchen.main.ui.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> dataList;
    private ArrayList<String> filteredList;

    public CustomAutoCompleteAdapter(Context context, ArrayList<String> dataList) {
        super(context, android.R.layout.simple_dropdown_item_1line, dataList);
        this.dataList = dataList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public String getItem(int position) {
        return filteredList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    filteredList.clear();
                    for (String item : dataList) {
                        // 判断是否包含输入的 constraint
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}