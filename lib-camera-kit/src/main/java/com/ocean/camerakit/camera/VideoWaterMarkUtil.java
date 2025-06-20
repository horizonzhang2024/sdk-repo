package com.ocean.camerakit.camera;

import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;

public class VideoWaterMarkUtil {

    public interface FFmpegResponseHandler {
        void onSuccess(String outputPath);

        void onFailure(String message);

        void onProgress(String message);

        void onStart();

        void onFinish();
    }

    /**
     * 给视频添加文字水印
     *
     * @param inputVideoPath
     * @param watermarkText
     * @param responseHandler
     */
    public static void addTextWatermarkToVideo(String inputVideoPath, String watermarkText, FFmpegResponseHandler responseHandler) {
        // 临时输出文件路径
        String outputVideoPath = inputVideoPath.replace(".mp4", "_watermarked.mp4");
//        String fontFilePath = "/system/fonts/DroidSansFallback.ttf";
        String fontFilePath = "/system/fonts/NotoSansCJK-Regular.ttc";
        File fontFile = new File(fontFilePath);
        if (!fontFile.exists()) {
            Log.e("FFmpeg", "Font file does not exist at path: " + fontFilePath);
        }

        // 替换字符串中的特殊字符，生成 trimmedWatermarkText
        String trimmedWatermarkText = watermarkText.replace("\\", "\\\\") // 处理反斜杠
                .replace("\r", "")     // 去掉回车符
                .replace(":", "\\:")   // 处理冒号
                .replace("'", "\\'");  // 处理单引号


        // 打印检查替换后的字符串
        Log.e("FFmpeg", "Trimmed Watermark Text: " + trimmedWatermarkText);


        boolean hardwareAccelerationSupported = HardwareDecoders.checkHardwareAccelerationSupported("OMX.qcom.video.decoder.hevc");

//        String videoEncoder = hardwareAccelerationSupported ? "mediacodec" : "libx265"; // 使用 mediacodec 硬件编码器进行编码, 无需特意指定为hevc_mediacodec。
        String videoEncoder = "libx264"; //当前使用的ffmpeg不支持mediacodec硬件加速

        String[] command = {
                hardwareAccelerationSupported ? "-hwaccel" : "", hardwareAccelerationSupported ? "mediacodec" : "",
                "-i", inputVideoPath,
                "-vf", "scale=1280:720,drawtext=fontfile='" + fontFilePath + "':text='" + trimmedWatermarkText + "':fontcolor=white:fontsize=30:x=15:y=h-th-40",
                "-s", "1280x720", // 强制输出分辨率为720p
                "-b:v", "2M", // 设置视频比特率
                "-c:v", videoEncoder, // 使用硬件加速或软件编码器
                "-crf", "25", // 恒定质量模式
                "-preset", "faster", // 速度和质量的平衡
                "-profile:v", "baseline", // 使用高质量配置文件
                "-level", "5.1", // 设置级别，确保兼容性
                "-c:a", "aac", // 使用AAC音频编码器
                "-b:a", "128k", // 设置音频比特率
                "-threads", "8", // 多线程
                "-map", "0:v", // 保留原视频流
                "-map", "0:a?", // 保留原音频流（如果存在）
                outputVideoPath
        };


        /**
         * String[] command = {
         * "-i", inputVideoPath,
         * "-vf", "drawtext=fontfile='" + fontFilePath + "':text='" + watermarkText + "':fontcolor=white:fontsize=42:x=10:y=h-th-75",
         * "-c:v", "mpeg4",
         * "-b:v", "1000k", // 设置目标比特率为1000kbps 720p (1280x720) 视频：通常在 5 Mbps 到 10 Mbps 之间。1080p (1920x1080) 视频：通常在 10 Mbps 到 20 Mbps 之间。4K (3840x2160) 视频：通常在 40 Mbps 到 100 Mbps 之间。
         * "-q:v", "5", // 设置视频质量，范围是1-31，数值越低质量越高
         * "-threads", "4", // 使用多线程
         * "-preset", "fast", // 使用较快的预设值
         * "-c:a", "copy",
         * outputVideoPath
         * };
         */
        //优化后的h264编码 h264_mediacodec硬件加速 libx264 软件编码  mpeg4编码无法在chrome浏览器中播放
//        String[] command = {"-i", inputVideoPath, "-vf", "scale=1280:720,drawtext=fontfile='" + fontFilePath + "':text='" + trimmedWatermarkText + "':fontcolor=white:fontsize=30:x=15:y=h-th-40", "-s", "1280x720", // 强制输出分辨率为720p 相机支持的分辨率为960*720
//                "-b:v", "2M", // 设置视频比特率
//                "-c:v", "libx264", // 使用 libx264 编码器
//                "-crf", "25", // 恒定质量模式，数值越低质量越高，范围是0-51，推荐值是18-23
//                "-preset", "faster", // 将预设调整为 'fast'，在速度和质量之间取得平衡
////                "-tune", "zerolatency", // 调整为零延迟编码，适用于实时应用
//                "-profile:v", "baseline", // 使用高质量配置文件
//                "-level", "4.1", // 设置级别，确保兼容性
//                "-c:a", "aac", // 使用 AAC 音频编码器
//                "-b:a", "128k", // 设置音频比特率
//                "-threads", "8", // 多线程 太多会有上下文切换开销
//                "-map", "0:v", // 保留原视频流
//                "-map", "0:a?", // 保留原音频流（如果存在）
//                outputVideoPath};

        Config.enableLogCallback(message -> {
            Log.d("FFmpeg", message.getText());
            responseHandler.onProgress(message.getText());
            if (message.getText().toLowerCase().contains("error")) {
                Log.e("FFmpeg", "Error: " + message.getText());
            }
        });

        Config.enableStatisticsCallback(statistics -> {
            Log.d("FFmpeg", "frame: " + statistics.getVideoFrameNumber());
        });


        // 记录开始时间
        long startTime = System.currentTimeMillis();
        responseHandler.onStart();

        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            // 记录结束时间
            long endTime = System.currentTimeMillis();
            // 计算执行时间
            long executionTime = endTime - startTime;
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                Log.d("FFmpeg", "Command execution completed successfully.");
                // Check if the output file was created and has a non-zero size
                File outputFile = new File(outputVideoPath);
                if (outputFile.exists() && outputFile.length() > 0) {
//                    // 获取原视频文件大小
//                    File originalFile1 = new File(inputVideoPath);
//                    long originalFileSize = originalFile1.length();
//                    Log.d("FFmpeg", "Original video size: " + originalFileSize + " bytes");
//
//                    // 获取水印后视频文件大小
//                    long outputFileSize = outputFile.length();f
//                    Log.d("FFmpeg", "Watermarked video size: " + outputFileSize + " bytes");
                    // 替换原视频文件
                    File originalFile = new File(inputVideoPath);
                    if (originalFile.delete()) {
                        if (outputFile.renameTo(originalFile)) {
                            Log.d("FFmpeg", "Original video replaced with watermarked video.");
                            responseHandler.onSuccess(inputVideoPath); // 返回替换后的原视频路径
                        } else {
                            Log.e("FFmpeg", "Failed to rename watermarked video to original video name.");
                            responseHandler.onFailure("Failed to rename watermarked video to original video name.");
                        }
                    } else {
                        Log.e("FFmpeg", "Failed to delete original video.");
                        responseHandler.onFailure("Failed to delete original video.");
                    }
                } else {
                    responseHandler.onFailure("Output file is empty or not created.");
                }
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                Log.d("FFmpeg", "Command execution cancelled by user.");
                responseHandler.onFailure("Command execution cancelled by user.");
            } else {
                Log.d("FFmpeg", "Command execution failed with returnCode=" + returnCode);
                responseHandler.onFailure("Command execution failed with returnCode=" + returnCode);
            }
            responseHandler.onFinish();
            // 打印执行时间
            Log.d("FFmpeg", "Command execution time: " + executionTime + " milliseconds.");
        });
    }
}

