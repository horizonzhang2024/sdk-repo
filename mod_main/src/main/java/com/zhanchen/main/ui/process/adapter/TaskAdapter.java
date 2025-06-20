package com.zhanchen.main.ui.process.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhanchen.main.R;
import com.zhanchen.main.ui.process.entity.KeyValueBean;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private List<KeyValueBean> data;

    public TaskAdapter(List<KeyValueBean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_key_value, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        KeyValueBean item = data.get(position);
        holder.keyTextView.setText(item.key);
        if (item.value == 1) {
            holder.valueTextView.setText("已完成");
            holder.valueTextView.setTextColor(Color.parseColor("#97FFFF"));
        } else {
            holder.valueTextView.setText("未完成");
            holder.valueTextView.setTextColor(Color.parseColor("#FFFACD"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}