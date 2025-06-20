package com.ocean.camerakit.camera;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFileUtils {

    private static final String TAG = "CameraFileUtils";

    /**
     * 确保文件存在，如果文件不存在则创建它
     *
     * @param file 要确保存在的文件
     * @return true 如果文件存在或成功创建；false 如果创建失败
     */
    public static boolean ensureFileExists(File file) {
        // 检查 file 是否为 null
        if (file == null) {
            Log.e(TAG, "传入的文件对象为 null");
            return false; // 直接返回 false
        }

        // 检查目标是否存在
        if (file.exists()) {
            Log.i(TAG, "已经存在: " + file.getAbsolutePath());
            return true; // 如果目标已存在，直接返回 true
        }

        // 获取父目录并检查其存在性
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            // 创建父目录，如果需要
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    Log.e(TAG, "创建父目录失败: " + parentDir.getAbsolutePath());
                    return false; // 父目录创建失败
                } else {
                    Log.i(TAG, "成功创建父目录: " + parentDir.getAbsolutePath());
                }
            }
        } else {
            Log.e(TAG, "父目录为 null，无法创建目标: " + file.getAbsolutePath());
            return false; // 父目录为 null
        }

        // 根据文件后缀判断并创建文件或目录
        try {
            String fileName = file.getName();
            if (fileName.endsWith(".")) {
                Log.e(TAG, "无效的文件名，无法创建: " + file.getAbsolutePath());
                return false; // 空后缀或只包含点的文件名无效
            }

            if (fileName.contains(".")) {
                // 如果文件名包含后缀名，则创建文件
                if (file.createNewFile()) {
                    Log.i(TAG, "成功创建文件: " + file.getAbsolutePath());
                    return true; // 文件成功创建
                } else {
                    Log.e(TAG, "文件已存在: " + file.getAbsolutePath());
                    return false; // 文件已经存在
                }
            } else {
                // 如果没有后缀名，则创建目录
                if (file.mkdirs()) {
                    Log.i(TAG, "成功创建目录: " + file.getAbsolutePath());
                    return true; // 目录成功创建
                } else {
                    Log.e(TAG, "目录已存在或创建失败: " + file.getAbsolutePath());
                    return false; // 目录已经存在或创建失败
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "创建目标时发生异常: " + e.getMessage(), e);
            return false; // 创建目标失败
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
            Log.e(TAG, "发现外置存储卡目录::" + externalFilesDirs[1]);
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

        //SD卡 第一次访问成功 第二次开始 创建目录会失败 现强制使用电筒内置目录
//        mainDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Log.e(TAG, "存储目录是否存在::" + mainDir.exists());
        return mainDir;
    }


    /**
     * 图片视频音频存储路径
     *
     * @param type     0：IMAGE 1:VIDEO 2:AUDIO 3:TXT
     * @param fileName fileName 必须_结尾  prefix_filename_timestamp_suffix
     * @param dirName  dirName 根目录下的文件夹层级  JW7137/DIGITAL/2024-09-13/dirName/
     * @return
     */
    public static File getOutputMediaFile(Context mContext, int type, String dirName, String fileName) {
        File mediaStorageDir = getOutPutMediaDir(mContext, dirName);
        if (mediaStorageDir == null) return null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String prefix = "";
        String suffix = "";
        if (type == 0) {
            prefix = "IMG_";
            suffix = ".jpg";
        } else if (type == 1) {
            prefix = "VID_";
            suffix = ".mp4";
        } else if (type == 2) {
            prefix = "AUD_";
            suffix = ".mp3";
        } else if (type == 3) {
            prefix = "RESULT_";
            suffix = ".txt";
        }

        File ff = new File(mediaStorageDir.getPath() + File.separator + prefix + fileName + timeStamp + suffix);
        if (ensureFileExists(ff)) {
            return ff;
        } else {
            return null;
        }
    }


    /**
     * 获取目录
     *
     * @param mContext
     * @param dirName
     * @return
     */
    public static @Nullable File getOutPutMediaDir(Context mContext, String dirName) {
        // 获取当前年月日
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        File dcimDir = getDCIM(mContext);
        //直接操作DCIM目录需要申请权限
        File mediaStorageDir = new File(dcimDir, "DIGITAL/" + currentDate + "/" + dirName);
        if (!ensureFileExists(mediaStorageDir)) {
            Log.d(TAG, "Failed to create directory");
            return null;
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory");
                return null;
            }
        }
        return mediaStorageDir;
    }
}