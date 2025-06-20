package com.zhanchen.main.ui.process.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhanchen.main.R;
import com.zhanchen.main.ui.process.TeaPickerView;

import java.util.List;

public class DataAdapter extends BaseAdapter {
    private List<String> mDatas;
    private Context context;
    private String checkStr = "";

    public DataAdapter(Context context, List<String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.data_textview, parent, false);
            viewHolder.data_layout = convertView.findViewById(R.id.data_layout);
            viewHolder.textView = convertView.findViewById(R.id.data_text);
            viewHolder.data_img = convertView.findViewById(R.id.data_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setTextSize(TeaPickerView.dataSize);
        viewHolder.textView.setTextColor(TeaPickerView.dataColor);
        viewHolder.textView.setText(mDatas.get(position));
        if (checkStr.equals(mDatas.get(position)) && TeaPickerView.discolour) {
            viewHolder.textView.setTextColor(TeaPickerView.discolourColor);
        }
        if (checkStr.equals(mDatas.get(position)) && TeaPickerView.discolourHook) {
            viewHolder.data_img.setVisibility(View.VISIBLE);
        } else {
            viewHolder.data_img.setVisibility(View.GONE);
        }
        if (TeaPickerView.customHook != null) {
            viewHolder.data_img.setImageDrawable(TeaPickerView.customHook);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, TeaPickerView.dataHeight);
        viewHolder.textView.setLayoutParams(params);
        return convertView;
    }

    public void setList(List<String> datas, String toString) {
        if (datas != null && datas.size() > 0) {
            mDatas = datas;
        }
        checkStr = toString;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView textView;
        LinearLayout data_layout;
        ImageView data_img;
    }
}
