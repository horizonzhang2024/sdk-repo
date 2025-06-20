package com.ocean.camerakit.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP工具类
 */
public class SPUtil {

    private static final String PREF_NAME = "CameraSettings"; // SharedPreferences的文件名

    // 保存设置
    public static void saveSetting(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply(); // 异步提交
    }

    // 获取设置
    public static String getSetting(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    // 删除设置
    public static void removeSetting(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply(); // 异步提交
    }

    // 清除所有设置
    public static void clearSettings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply(); // 异步提交
    }
}