package com.ocean.camerakit.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastCenterUtils {

    /**
     * 显示一个居中的 Toast。
     *
     * @param context 上下文
     * @param message 要显示的消息
     */
    public static void showToast(Context context, String message) {
        // 创建 Toast 实例
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

        // 获取 Toast 的视图
        View toastView = toast.getView();

        // 设置视图的布局参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        toastView.setLayoutParams(params);

        // 设置 Toast 的显示位置
        toast.setGravity(Gravity.CENTER, 0, 0);

        // 显示 Toast
        toast.show();
    }
}