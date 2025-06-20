package com.zhanchen.main.ui.process.sdk;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class ZFileUtils {

    /**
     * 确保传入的文件或目录存在。如果不存在，则创建它。
     *
     * @param file 需要检查或创建的文件或目录
     * @return 是否确保存在（创建成功或已存在）
     */
    public static boolean ensureFileExists(File file) {
        if (file == null) {
            return false;
        }

        if (file.isDirectory()) {
            // 如果是目录，确保目录及其父目录存在
            return file.exists() || file.mkdirs();
        } else {
            // 如果是文件，确保文件的父目录存在，然后创建文件
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    // 如果创建父目录失败，返回 false
                    System.out.println("目录创建失败: " + parentDir.getAbsolutePath());
                    return false;
                }
            }

            try {
                if (file.createNewFile()) {
                    // 文件成功创建
                    return true;
                } else {
                    // 文件已存在
                    return true; // 文件已存在，返回 true
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false; // 文件创建失败
            }
        }
    }

    /**
     * 检查传入的文件或目录是否存在。
     *
     * @param file 需要检查的文件或目录
     * @return 是否存在
     */
    public static boolean exists(File file) {
        return file != null && file.exists();
    }


    /**
     * 检测是否有外置SD卡 返回外置或者内置的DCIM目录
     * SD卡优先级高于内部存储
     *
     * @param context
     * @return
     */
    public static File getDCIM(Context context) {
        File mainDir;
        File[] externalFilesDirs = context.getExternalFilesDirs(null);
        if (externalFilesDirs.length > 1 && externalFilesDirs[1] != null) {
            Log.e("ZFileUtils", "发现外置存储卡目录::" + externalFilesDirs[1]);
            //检测到SD卡 设置到SD卡目录
            File externalSdCard = externalFilesDirs[1];
            String sdCardPath = externalSdCard.getAbsolutePath();
            String sdCardRootPath = sdCardPath.substring(0, sdCardPath.indexOf("/Android/data"));
            mainDir = new File(sdCardRootPath + File.separator + "DCIM");
            if (!mainDir.exists()) {
                mainDir.mkdirs();
            }
            //此时mainDir就成了SD卡根目录下的DCIM文件夹  展示位U盘/DCIM
        } else {
            //没有检测到SD卡 设置为系统内存目录DCIM
            mainDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        }


        return mainDir;
    }
}