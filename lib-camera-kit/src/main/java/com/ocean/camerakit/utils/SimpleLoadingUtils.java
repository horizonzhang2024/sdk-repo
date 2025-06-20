package com.ocean.camerakit.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * SimpleLoadingUtils
 */
public class SimpleLoadingUtils {

    private static ProgressDialog progressDialog;
    private static final Object lock = new Object();
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    // 显示 Loading
    public static void showLoading(final Context context) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }

        final Activity activity = (Activity) context;

        mainHandler.post(() -> {
            synchronized (lock) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("正在加载...");
                    progressDialog.setCancelable(false); // 不允许用户点击外部取消
                    progressDialog.setIndeterminate(true); // 设置为不确定进度
                }

                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        });
    }

    // 关闭 Loading
    public static void dismissLoading() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    // 清理资源
    public static void clear() {
        mainHandler.post(() -> {
            synchronized (lock) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    progressDialog = null;
                }
            }
        });
    }
}