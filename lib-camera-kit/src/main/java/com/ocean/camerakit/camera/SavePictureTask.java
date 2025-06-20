package com.ocean.camerakit.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File: SavePictureTask.java
 * Author: zhanchen
 * Date: 2024/8/28
 * Description: 拍摄后保存图片视频的task
 */
public class SavePictureTask extends AsyncTask<byte[], Void, String> {
    private final String pictureFilePath;
    private final CameraHelper.CameraResultCallback callback;
    private final Context mContext;
    private final String watermarkText; // 新增水印文本参数

    public SavePictureTask(Context context, String pictureFilePath, String watermarkText, CameraHelper.CameraResultCallback callback) {
        this.pictureFilePath = pictureFilePath;
        this.watermarkText = watermarkText;
        this.callback = callback;
        this.mContext = context.getApplicationContext(); // 使用 applicationContext 避免内存泄漏
    }

    @Override
    protected String doInBackground(byte[]... data) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data[0], 0, data[0].length);
            bitmap = ImageWaterMarkUtil.getInstance().addWatermarkToBitmap(bitmap, watermarkText);
            try (FileOutputStream fos = new FileOutputStream(pictureFilePath)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
            }
        } catch (IOException e) {
            if (callback != null) {
                callback.onError("Error saving picture: " + e.getMessage());
            }
            return null;
        }
        return pictureFilePath;
    }

    @Override
    protected void onPostExecute(String pictureFilePath) {
        super.onPostExecute(pictureFilePath);
        if (pictureFilePath != null) {
            // 通知媒体扫描器扫描新文件
            notifyMediaScanner(mContext, pictureFilePath);

            // 显示 Toast
            Toast.makeText(mContext, "拍照完成: " + pictureFilePath, Toast.LENGTH_SHORT).show();

            // 调用回调方法
            if (callback != null) {
                callback.onPictureTaken(pictureFilePath);
            }
        } else {
            if (callback != null) {
                callback.onError("Failed to save picture.");
            }
        }
    }

    /**
     * 刷新图片到相册
     *
     * @param context
     * @param filePath
     */
    public static void notifyMediaScanner(Context context, String filePath) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                // Activity 已经销毁或正在销毁
                return;
            }
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        Context appContext = (context instanceof Activity) ? ((Activity) context).getApplicationContext() : context;
        MediaScannerConnection.scanFile(appContext, new String[]{filePath}, null, (s, uri) -> {
            Log.i("SavePhotoTask", "Scanned " + s + ":");
            Log.i("SavePhotoTask", "-> uri=" + uri);
        });
    }
}