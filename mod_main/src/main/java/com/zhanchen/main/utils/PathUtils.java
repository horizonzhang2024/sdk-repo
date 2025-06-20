package com.zhanchen.main.utils;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;

public class PathUtils {

    public static Uri getUriFromFile(Context context, String dir) {
        Uri uri;
        File file = new File(dir);
        if (isExternalStoragePath(file.getAbsolutePath())) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        return uri;
    }

    public static boolean isExternalStoragePath(String path) {
        // 检查路径是否是外部存储路径，例如：/storage/emulated/0 或者 /sdcard
        String[] externalStoragePaths = {"/storage/emulated/0", "/sdcard"};

        for (String externalStoragePath : externalStoragePaths) {
            if (path.startsWith(externalStoragePath)) {
                return true;
            }
        }
        return false;
    }
}