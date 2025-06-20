package com.zhanchen.main.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;

import java.io.IOException;

public class PreviewUtils {

    // 生成图片预览图
    public static Bitmap generateImagePreview(String imagePath) {
        int desiredWidth = 200;
        Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        float aspectRatio = (float) originalWidth / originalHeight;
        int desiredHeight = (int) (desiredWidth / aspectRatio);
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(originalBitmap, desiredWidth, desiredHeight);
        return thumbnail;
    }

    // 生成视频预览图
    public static Bitmap generateVideoPreview(String videoPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap thumbnail = retriever.getFrameAtTime();
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return thumbnail;
    }
}
