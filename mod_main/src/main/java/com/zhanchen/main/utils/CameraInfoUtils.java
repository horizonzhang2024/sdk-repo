package com.zhanchen.main.utils;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.os.Build;
import android.util.Size;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraInfoUtils {
    private static final Map<Integer, String> videoQualityMap = new HashMap<>();

    static {
        videoQualityMap.put(CamcorderProfile.QUALITY_HIGH, "高质量");
        videoQualityMap.put(CamcorderProfile.QUALITY_1080P, "1080P");
        videoQualityMap.put(CamcorderProfile.QUALITY_720P, "720P");
        videoQualityMap.put(CamcorderProfile.QUALITY_480P, "480P");
        videoQualityMap.put(CamcorderProfile.QUALITY_LOW, "低质量");
    }

//    public static String getCurrentVideoQuality(MediaRecorder mediaRecorder) {
//        if (mediaRecorder != null) {
//            int quality = mediaRecorder.get.quality;
//
//        }
//        return "未知画质";
//    }

    public static Size getPhotoResolution(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Size size = new Size(parameters.getPictureSize().width, parameters.getPictureSize().height);
        return size;
    }

    public static Size getVideoResolution(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreferredPreviewSizeForVideo();
        Size videoSize = new Size(size.width, size.height);
        return videoSize;
    }

    public static long getMaxVideoDuration(Camera camera, int quality) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            CamcorderProfile profile = CamcorderProfile.get(quality);
            return profile.duration / (1000 * 60);
        }
        return -1;
    }

    public static Size getPreviewAspectRatio(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();
//        float aspectRatio = (float) size.width / size.height;
//        return aspectRatio;
        Size previewSize = new Size(size.width, size.height);
        return previewSize;
    }


    public static List<Camera.Size> getSupportedPictureSizes(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        return parameters.getSupportedPictureSizes();
    }


//    public static void setIR(Context context, boolean flag){
//        InfraredManager infraredManager = (InfraredManager) context.getSystemService(Context.INFRARED_SERVICE);
//        if (infraredManager != null) {
//            infraredManager.enableIR();
//        }
//    }
}

