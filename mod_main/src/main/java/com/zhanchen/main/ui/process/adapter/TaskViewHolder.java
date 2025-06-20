package com.zhanchen.main.ui.process.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zhanchen.main.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView keyTextView;
    TextView valueTextView;

    public TaskViewHolder(View itemView) {
        super(itemView);
        keyTextView = itemView.findViewById(R.id.tv_key);
        valueTextView = itemView.findViewById(R.id.tv_value);
    }
}