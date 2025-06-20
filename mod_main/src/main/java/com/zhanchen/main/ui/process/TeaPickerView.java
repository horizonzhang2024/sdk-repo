package com.zhanchen.main.ui.process;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhanchen.main.R;
import com.zhanchen.main.ui.process.adapter.DataAdapter;
import com.zhanchen.main.ui.process.entity.PickerData;
import com.zhanchen.main.ui.process.listener.OnPickerClickListener;

import java.util.List;
import java.util.Map;

public class TeaPickerView extends PopupWindow implements View.OnClickListener {
    private FrameLayout contentLayout;
    private RadioButton mTextFirst, mTextSecond, mTextThird, mTextFourth;
    private RadioGroup groupSelect;
    private View line;
    private ListView pickerList;
    private TextView pickerTitleName;
    private TextView pickerConfirm;
    private View view;
    private int index = 1;
    private List<String> currData;
    private DataAdapter adapter;
    private Activity context;
    private OnPickerClickListener listener;
    public PickerData pickerData;

    private int height;
    private int radius = 0;
    private int backgroundColor = Color.parseColor("#ffffff");
    public static int dataSize = 20; //内容栏单行高度
    public static int dataColor;
    public static int dataHeight;
    private float alpha = 0.5f;
    public static boolean contentLine = false;
    public static Drawable contentLineDrawable;
    public static boolean scrollBal = false;
    public static boolean discolour = true;
    public static int discolourColor;
    public static boolean discolourHook = false;
    public static Drawable customHook;

    public TeaPickerView(Activity context, PickerData pickerData) {
        super(context);
        this.context = context;
        this.pickerData = pickerData;
        initView();
    }


    public PickerData getPickerData() {
        return pickerData;
    }

    public void setPickerData(PickerData pickerData) {
        this.pickerData = pickerData;
        initView();
    }

    //显示具体的高度(dp),设置0是自适应
    public TeaPickerView setHeights(int mHeight) {
        if (mHeight > 0) {
            height = getPixelsFromDp(mHeight);
        }
        return this;
    }

    //显示的高度占屏幕的百分比,高度没有默认值，需要主动设置
    public TeaPickerView setScreenH(int num) {
        if (num > 0) {
            height = getScreenH(context) / num;
        }
        return this;
    }

    //设置整体的背景颜色 默认是#ffffff
    public TeaPickerView setBackground(int color) {
        backgroundColor = color;
        return this;
    }

    //设置圆角，默认0
    public TeaPickerView setRadius(int mRadius) {
        this.radius = mRadius;
        return this;
    }

    //内容栏的背景颜色 默认是#ffffff
    public TeaPickerView setContentBackground(int color) {
        GradientDrawable drawable = new GradientDrawable();
        //设置圆角大小
        drawable.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        //设置shape背景色
        drawable.setColor(color);
        contentLayout.setBackground(drawable);
        return this;
    }

    //内容栏的高度(dp) 默认是50dp
    public TeaPickerView setContentHeight(int mHeight) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) groupSelect.getLayoutParams();
        params.height = getPixelsFromDp(mHeight);
        groupSelect.setLayoutParams(params);
        return this;
    }

    //内容栏字体的大小和颜色, 默认是16sp,#0aa666，此方法不会变换颜色
    public TeaPickerView setContentText(int size, int color) {
        mTextFirst.setTextSize(size);
        mTextFirst.setTextColor(color);
        mTextSecond.setTextSize(size);
        mTextSecond.setTextColor(color);
        mTextThird.setTextSize(size);
        mTextThird.setTextColor(color);
        mTextFourth.setTextSize(size);
        mTextFourth.setTextColor(color);
        return this;
    }

    //自定义内容栏字体颜色变换器 在res目录下创建color文件夹用selector 默认颜色#555 选中颜色#0aa666
    public TeaPickerView setContentText(ColorStateList colorStateList) {
        mTextFirst.setTextColor(colorStateList);
        mTextSecond.setTextColor(colorStateList);
        mTextThird.setTextColor(colorStateList);
        mTextFourth.setTextColor(colorStateList);
        return this;
    }

    //内容栏选中是否有下划线 默认不开启
    public TeaPickerView setContentLine(boolean bl) {
        contentLine = bl;
        return this;
    }

    //自定义内容栏下划线用layer-list 默认是下边框描边 颜色#0fbc72 高度1dp
    public TeaPickerView setContentLineColor(Drawable drawable) {
        contentLineDrawable = drawable;
        return this;
    }

    //分割线的高度和颜色 默认是0.5dp #e5e5e5
    public TeaPickerView setLine(int mHeight, int color) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
        params.height = getPixelsFromDp(mHeight);
        line.setLayoutParams(params);
        line.setBackgroundColor(color);
        return this;
    }

    //设置list的item的高度(dp) 默认是40dp
    public TeaPickerView setitemHeight(int mHeight) {
        dataHeight = getPixelsFromDp(mHeight);
        return this;
    }

    //设置list的字体大小和颜色 默认是15 #555
    public TeaPickerView setListText(int size, int color) {
        dataSize = size;
        dataColor = color;
        return this;
    }

    //设置list是否显示滚动条,默认false
    public TeaPickerView setScrollBal(boolean bl) {
        scrollBal = bl;
        return this;
    }

    //设置阴影层的透明度 默认是0.5f
    public TeaPickerView setAlpha(float mFloat) {
        alpha = mFloat;
        return this;
    }

    //设置选中项是否加色，默认true
    public TeaPickerView setDiscolour(boolean bl) {
        discolour = bl;
        return this;
    }

    //设置选中项加色的颜色值，默认#0aa666
    public TeaPickerView setDiscolourColor(int color) {
        discolourColor = color;
        return this;
    }

    //设置选中项是否有√图标，默认false
    public TeaPickerView setDiscolourHook(boolean bl) {
        discolourHook = bl;
        return this;
    }

    //自定义√图标
    public TeaPickerView setCustomHook(Drawable drawable) {
        customHook = drawable;
        return this;
    }

    //参数设置完毕，一定要build一下
    public void build() {
        initPicker();
        initData();
    }

    private GradientDrawable getSharp() {
        GradientDrawable drawable = new GradientDrawable();
        //设置圆角大小
        drawable.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        //设置shape背景色
        drawable.setColor(backgroundColor);
        return drawable;
    }

    public void setOnPickerClickListener(OnPickerClickListener listener) {
        this.listener = listener;
    }

    private void initPicker() {
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        if (height > 0) {
            this.setHeight(height);
        }
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        this.setBackgroundDrawable(getSharp());
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });
    }

    private void initView() {
        //默认参数
        dataColor = context.getResources().getColor(R.color.picker_text_color);
        dataHeight = getPixelsFromDp(50); //内容栏高度
        discolourColor = context.getResources().getColor(R.color.picker_select_text_color);
        contentLineDrawable = context.getResources().getDrawable(R.drawable.tab_sharp);
        //加载view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.picker_view, null);
        contentLayout = view.findViewById(R.id.contentLayout);
        pickerTitleName = view.findViewById(R.id.pickerTitleName);
        pickerConfirm = view.findViewById(R.id.pickerConfirm);
        groupSelect = view.findViewById(R.id.groupSelect);
        mTextFirst = view.findViewById(R.id.mTextFirst);
        mTextSecond = view.findViewById(R.id.mTextSecond);
        mTextThird = view.findViewById(R.id.mTextThird);
        mTextFourth = view.findViewById(R.id.mTextFourth);
        line = view.findViewById(R.id.line);
        pickerList = view.findViewById(R.id.pickerList);
        mTextFirst.setOnClickListener(this);
        mTextSecond.setOnClickListener(this);
        mTextThird.setOnClickListener(this);
        mTextFourth.setOnClickListener(this);
        pickerConfirm.setOnClickListener(this);


        if (!TextUtils.isEmpty(pickerData.getPickerTitleName())) {
            pickerTitleName.setText(pickerData.getPickerTitleName());
        }

    }

    public void show(View view) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
        showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        switch (index) {
            case 1:
                adapter.setList(currData, mTextFirst.getText().toString());
                underlineState();
                break;
            case 2:
                adapter.setList(currData, mTextSecond.getText().toString());
                underlineState();
                break;
            case 3:
                adapter.setList(currData, mTextThird.getText().toString());
                underlineState();
                break;
            case 4:
                adapter.setList(currData, mTextFourth.getText().toString());
                underlineState();
                break;
        }

    }

    private void initData() {
        currData = pickerData.getCurrDatas(index, "");
        adapter = new DataAdapter(context, currData);
        pickerList.setVerticalScrollBarEnabled(scrollBal);
        pickerList.setAdapter(adapter);
        if (!TextUtils.isEmpty(pickerData.getFirstText())) {
            mTextFirst.setText(pickerData.getFirstText());
            if (!TextUtils.isEmpty(pickerData.getSecondText())) {
                mTextSecond.setText(pickerData.getSecondText());
                if (!TextUtils.isEmpty(pickerData.getThirdText())) {
                    mTextThird.setText(pickerData.getThirdText());
                    if (!TextUtils.isEmpty(pickerData.getFourthText()))
                        mTextFourth.setText(pickerData.getFourthText());
                }
            }
            mTextFirst.setChecked(true);
        }
        pickerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currData == null) {
                    return;
                }
                String currText = currData.get(position);
                //清空上次的选择信息
                pickerData.clearSelectText(index);
                mTextFirst.setText(pickerData.getFirstText());
                mTextSecond.setText(pickerData.getSecondText());
                mTextThird.setText(pickerData.getThirdText());
                mTextFourth.setText(pickerData.getFourthText());
                if (index == 1) {
                    pickerData.setFirstText(currText);
                    mTextFirst.setText(currText);
                    mTextSecond.setText(pickerData.getSecondText());
                    mTextThird.setText(pickerData.getThirdText());
                    mTextFourth.setText(pickerData.getFourthText());
                    groupSelect.check(mTextFirst.getId());
                    //更新二级列表
                    new UpdateData(currText, pickerData.getSecondDatas()).invoke();
                } else if (index == 2) {
                    pickerData.setSecondText(currText);
                    mTextSecond.setText(currText);
                    mTextThird.setText(pickerData.getThirdText());
                    mTextFourth.setText(pickerData.getFourthText());
                    groupSelect.check(mTextSecond.getId());
                    //更新三级列表
                    //如果三级列表 只需要根据二级列表确定
//                    new UpdateData(currText, pickerData.getSecondDatas()).invoke();
                    //如果三级列表 需要一级+二级共同确定
//                    new UpdateData(pickerData.getFirstText() + "&" + currText, pickerData.getSecondDatas()).invoke();
                    //如果三级列表 需要info+二级列表共同确定 UpdateData一个参数是key 第二个参数上一级列表添加的value
                    new UpdateData(PickerData.preText + "&" + currText, pickerData.getSecondDatas()).invoke();
                } else if (index == 3) {
                    pickerData.setThirdText(currText);
                    mTextThird.setText(currText);
                    mTextFourth.setText(pickerData.getFourthText());
                    groupSelect.check(mTextThird.getId());
                    //更新四级列表
                    new UpdateData(currText, pickerData.getSecondDatas()).invoke();
                } else if (index == 4) {
                    pickerData.setFourthText(currText);
                    mTextFourth.setText(currText);
                    groupSelect.check(mTextFourth.getId());
                    if (listener != null) {
                        listener.OnPickerClick(pickerData);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mTextFirst) {
            index = 1;
            currData = pickerData.getCurrDatas(index, "");
            adapter.setList(currData, mTextFirst.getText().toString());
            underlineState();
        } else if (id == R.id.mTextSecond) {
            index = 2;
            currData = pickerData.getCurrDatas(index, mTextFirst.getText().toString());
            adapter.setList(currData, mTextSecond.getText().toString());
            underlineState();
        } else if (id == R.id.mTextThird) {
            index = 3;
            currData = pickerData.getCurrDatas(index, mTextSecond.getText().toString());
            adapter.setList(currData, mTextThird.getText().toString());
            underlineState();
        } else if (id == R.id.mTextFourth) {
            index = 4;
            currData = pickerData.getCurrDatas(index, mTextFourth.getText().toString());
            adapter.setList(currData, mTextFourth.getText().toString());
            underlineState();
        }
        /*else if (id == R.id.pickerConfirm) {
            if (listener != null) {
                dismiss();
                listener.OnPickerConfirmClick(pickerData);
            }
        }*/
    }

    private void underlineState() {
        if (!contentLine) {
            return;
        }
        switch (index) {
            case 1:
                mTextFirst.setBackground(contentLineDrawable);
                mTextSecond.setBackground(null);
                mTextThird.setBackground(null);
                mTextFourth.setBackground(null);
                break;
            case 2:
                mTextFirst.setBackground(null);
                mTextSecond.setBackground(contentLineDrawable);
                mTextThird.setBackground(null);
                mTextFourth.setBackground(null);
                break;
            case 3:
                mTextFirst.setBackground(null);
                mTextSecond.setBackground(null);
                mTextThird.setBackground(contentLineDrawable);
                mTextFourth.setBackground(null);
                break;
            case 4:
                mTextFirst.setBackground(null);
                mTextSecond.setBackground(null);
                mTextThird.setBackground(null);
                mTextFourth.setBackground(context.getResources().getDrawable(R.drawable.tab_sharp));
                break;
        }
    }

    private class UpdateData {
        private String text;
        private Map<String, List<String>> data;

        UpdateData(String text, Map<String, List<String>> data) {
            this.text = text;
            this.data = data;
        }


        void invoke() {
            if (!data.isEmpty()) {
                List<String> data = pickerData.getCurrDatas(index + 1, text);
                if (data != null && data.size() > 0) {
                    currData = data;
                    adapter.setList(currData, text);
                    underlineState();
                    index++;
                } else {
                    if (listener != null) {
                        listener.OnPickerClick(pickerData);
                    }
                }
            } else {
                if (listener != null) {
                    listener.OnPickerClick(pickerData);
                }
            }
        }
    }


    public int getScreenW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }


    public int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private int getPixelsFromDp(int size) {

        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
