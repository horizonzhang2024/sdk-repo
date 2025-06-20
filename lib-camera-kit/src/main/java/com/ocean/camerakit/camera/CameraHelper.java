package com.ocean.camerakit.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ocean.camerakit.QuickCameraActivity;
import com.ocean.camerakit.utils.SPUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * File: CameraHelper.java
 * Author: zhanchen
 * Date: 2024/8/28
 * Description: 使用系统相机API封装拍照录像工具类
 */
public class CameraHelper implements SurfaceHolder.Callback {
    private static final String TAG = "CameraHelper";
    private Context mContext;
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private CameraResultCallback callback;
    private boolean isRecording = false;
    private boolean isPaused = false;
    private String videoFilePath;
    private BlinkingIndicatorUtil blinkUtil;
    private RecordingTimeUtils recordTimeUtil;
    private TextView waterMarkView;
    private TextView recordTimerView;
    private String waterMarkText;


    public CameraHelper(Activity activity, SurfaceView surfaceView, View recordingIndicator, TextView waterMarkView, String waterMarkText, TextView recordTimerView, CameraResultCallback callback) {
        this.mContext = activity;
        this.surfaceView = surfaceView;
        this.waterMarkView = waterMarkView;
        this.waterMarkText = waterMarkText;
        this.recordTimerView = recordTimerView;
        this.surfaceHolder = surfaceView.getHolder();
        this.blinkUtil = new BlinkingIndicatorUtil(recordingIndicator);
        this.recordTimeUtil = new RecordingTimeUtils(recordTimerView);
        this.callback = callback;

        PermissionsHelper.init(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}).checkAndRequestPermissions(activity, new PermissionsHelper.PermissionsCallback() {
            @Override
            public void onPermissionsGranted() {
                initializeCamera();
            }

            @Override
            public void onPermissionsDenied() {
                Toast.makeText(mContext, "权限被拒绝，请授予权限后再次操作", Toast.LENGTH_SHORT).show();
                if (callback != null) {
                    callback.onError("Permissions not granted");
                }
            }
        });
    }


    //接管UI中的handlePermissionsResult
    public void handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsHelper.handlePermissionsResult(requestCode, permissions, grantResults);
    }


    public void initializeCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                surfaceHolder.addCallback(this);
                camera.setPreviewDisplay(surfaceHolder);

                // Set the correct display orientation
                setCameraDisplayOrientation();

                // Set camera parameters
                Camera.Parameters params = camera.getParameters();
                // 应用照片大小设置
                String photoSize = SPUtil.getSetting(mContext, "photo_size", null);
                if (photoSize != null) {
                    String[] dimensions = photoSize.split("x");
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);

                    // Validate and set picture size
                    Camera.Size bestSize = getBestPictureSize(params.getSupportedPictureSizes(), width, height);
                    if (bestSize != null) {
                        params.setPictureSize(bestSize.width, bestSize.height);
                    }
                }

                // 检查是否支持定焦模式
                if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                camera.setParameters(params);
                camera.startPreview();
                // Update watermark
                ImageWaterMarkUtil.getInstance().updateWatermark(waterMarkText, waterMarkView);
            } catch (IOException e) {
                handleError("Error setting camera preview: " + e.getMessage());
            } catch (RuntimeException e) {
                handleError("Failed to open camera: " + e.getMessage());
            } catch (Exception e) {
                handleError("Unexpected error: " + e.getMessage());
            }
        }
    }

    //设置显示方向
    private void setCameraDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    //旋转图片
    private byte[] rotateImageIfNecessary(byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        int rotation = getCameraRotation();

        // 旋转Bitmap
        Bitmap rotatedBitmap = rotateBitmap(bitmap, rotation);

        // 把旋转后的Bitmap转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //获取正确方向
    private int getCameraRotation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    private void handleError(String errorMsg) {
        Log.e(TAG, errorMsg);
        if (callback != null) {
            callback.onError(errorMsg);
        }
    }

    /**
     * 拍照
     *
     * @param imgWatermarkText
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void takePicture(String imgWatermarkText) {
        if (camera == null) {
            initializeCamera();
        }

        if (camera != null) {
            try {
                camera.autoFocus((success, camera) -> {
                    camera.takePicture(null, null, (data, cameras) -> {
                        //拍照
//                        File picFile = CameraFileUtils.getOutputMediaFile(mContext, 0, "MyPictures", "");


                        //文件夹命名
                        String fileDir = ProcessMediaFileUtils.getOutputMediaDir(mContext, QuickCameraActivity.Companion.getMProcessBean());
                        //文件名
                        // 获取当前时间
                        LocalTime currentTime = LocalTime.now();
                        // 格式化时间为 24 小时制，不带冒号
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
                        String formattedTime = currentTime.format(formatter);
                        //文件命名
                        String fileName = ProcessMediaFileUtils.getOutputMediaFileName(QuickCameraActivity.Companion.getMProcessBean()) + ".jpg";

                        File picFile = new File(fileDir + File.separator + fileName);
                        if (!CameraFileUtils.ensureFileExists(picFile)) {
                            handleError("Failed to create file:" + picFile.getPath());
                            return;
                        }

                        String pictureFilePath = picFile.getPath();
                        // 旋转图片数据 校准镜头角度
                        byte[] rotatedData = rotateImageIfNecessary(data);
                        new SavePictureTask(mContext, pictureFilePath, imgWatermarkText, callback).execute(rotatedData);

                        // 保存成功后恢复预览，不要重新初始化相机
                        try {
                            camera.startPreview();
                        } catch (Exception e) {
                            handleError("Failed to restart preview: " + e.getMessage());
                        }
                    });
                });
            } catch (Exception e) {
                handleError("Failed to take picture: " + e.getMessage());
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initializeCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            handleError("Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private Camera.Size getBestPictureSize(List<Camera.Size> supportedSizes, int width, int height) {
        Camera.Size bestSize = null;
        for (Camera.Size size : supportedSizes) {
            if (size.width >= width && size.height >= height) {
                if (bestSize == null || size.width < bestSize.width) {
                    bestSize = size;
                }
            }
        }
        return bestSize;
    }


    /**
     * 销毁相机实例
     */
    public void releaseCamera() {
        if (camera != null) {
            try {
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                Log.e(TAG, "Failed to release camera: " + e.getMessage());
            }
        }
    }

    /**
     * 创建 MediaRecorder 实例。
     * 设置音频和视频源。
     * 设置输出格式。
     * 设置视频编码器。
     * 设置视频尺寸、帧率等参数。
     * 设置输出文件路径。
     * 准备 MediaRecorder。
     * 视频质量包含了编码设置
     * 如果用户同时设置了视频质量（高中低）和编码格式，则以视频质量的高中低设置为准。
     * 若用户只设置了其中之一，则以用户设置的值为准。
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean prepareVideoRecorder() {
        if (camera == null) {
            return false;
        }

        mediaRecorder = new MediaRecorder();
        try {
            // Step 1: Unlock and set camera to MediaRecorder
            camera.unlock();
            mediaRecorder.setCamera(camera);

            // Step 2: Set sources
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

            // Step 3: Apply CamcorderProfile settings
            applyVideoSettings(mediaRecorder, mContext);

            // Step 4: Apply user settings for video resolution and frame rate
            applyUserSettings();

            // Step 5: Set output file
//            File vidFile = CameraFileUtils.getOutputMediaFile(mContext, 1, "MyVideos", "");

            //文件夹命名
            String fileDir = ProcessMediaFileUtils.getOutputMediaDir(mContext, QuickCameraActivity.Companion.getMProcessBean());
            //文件名
            // 获取当前时间
            LocalTime currentTime = LocalTime.now();
            // 格式化时间为 24 小时制，不带冒号
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
            String formattedTime = currentTime.format(formatter);
            //文件命名
            String fileName = ProcessMediaFileUtils.getOutputMediaFileName(QuickCameraActivity.Companion.getMProcessBean()) + ".mp4";

            File vidFile = new File(fileDir + File.separator + fileName);
            if (!CameraFileUtils.ensureFileExists(vidFile)) {
                handleError("Failed to create file:" + vidFile.getPath());
                return false;
            }


            videoFilePath = vidFile.getPath();
            mediaRecorder.setOutputFile(videoFilePath);

            // Step 6: Set preview display
            mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

            // 设置录制时的方向
            mediaRecorder.setOrientationHint(getCameraRotation());

            mediaRecorder.prepare();
        } catch (IllegalStateException | IOException e) {
            Log.d(TAG, "Error preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            if (callback != null) {
                callback.onError("Error preparing MediaRecorder: " + e.getMessage());
            }
            return false;
        }
        return true;
    }


    // 设置视频质量+编码
    private void applyVideoSettings(MediaRecorder mediaRecorder, Context mContext) {
        // 获取视频质量设置
        String videoQuality = SPUtil.getSetting(mContext, "video_quality", "高");
        // 设置视频质量
        CamcorderProfile profile = getCamcorderProfile(videoQuality);
        if (profile != null) {
            // 调整比特率，降低文件大小但保持清晰度
            profile.videoBitRate = adjustBitRate(profile.videoBitRate);
            mediaRecorder.setProfile(profile);
        }
    }


    // 调整比特率功能
    private int adjustBitRate(int currentBitRate) {
        // 可以根据需要调整目标比特率，例如降低为当前比特率的75%
        return (int) (currentBitRate * 0.85);
    }

    // 设置视频质量获取CamcorderProfile 默认HIGH
    private CamcorderProfile getCamcorderProfile(String videoQuality) {
        switch (videoQuality.toLowerCase()) {
            case "中":
                return CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
            case "低":
                return CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
            default:
                CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
                // 调整比特率
                profile.videoBitRate = adjustBitRate(profile.videoBitRate);
                return profile;
        }
    }


    // 设置编码格式获取视频编码器
    private int getVideoEncoder(String encodingFormat) {
        switch (encodingFormat.toLowerCase()) {
            case "video/h264":
                return MediaRecorder.VideoEncoder.H264;
            case "video/hevc":
                return MediaRecorder.VideoEncoder.HEVC;
            default:
                return MediaRecorder.VideoEncoder.HEVC; // 使用默认编码器
        }
    }


    //设置视频分辨率 帧率
    private void applyUserSettings() {
        // Apply video resolution setting
        String videoResolution = SPUtil.getSetting(mContext, "video_resolution", null);
        if (videoResolution != null) {
            String[] dimensions = videoResolution.split("x");
            if (dimensions.length == 2) {
                try {
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);
                    if (width > 0 && height > 0) {
                        mediaRecorder.setVideoSize(width, height);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Invalid video resolution format");
                }
            }
        }

        // Apply video frame rate setting
        String videoFrame = SPUtil.getSetting(mContext, "video_frame", null);
        if (videoFrame != null) {
            try {
                int frameRate = Integer.parseInt(videoFrame.replace("fps", ""));
                if (frameRate > 0) {
                    mediaRecorder.setVideoFrameRate(frameRate);
                }
            } catch (Exception e) {
                Log.d(TAG, "Invalid video frame rate format");
            }
        }
    }


    /**
     * 录像
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startRecording() {
        if (isRecording) {
            Toast.makeText(mContext, "已在录像中...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (camera == null) {
            initializeCamera();
        }
        if (prepareVideoRecorder()) {


            try {
                mediaRecorder.start();
            } catch (IllegalStateException ise) {
                Toast.makeText(mContext, "录像启动失败: " + ise.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "IllegalStateException when trying to start MediaRecorder: " + ise.getMessage());
                releaseMediaRecorder();
            } catch (RuntimeException re) {
                Toast.makeText(mContext, "录像启动失败: " + re.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "RuntimeException when trying to start MediaRecorder: " + re.getMessage());
                releaseMediaRecorder();
            }
            isRecording = true;
            isPaused = false;
            Toast.makeText(mContext, "录像开始...", Toast.LENGTH_SHORT).show();

            // 开始闪烁小红点
            blinkUtil.startBlinking();
            // 开始显示录制时间
            recordTimeUtil.start();
        } else {
            releaseMediaRecorder();
        }
    }


    public void pauseRecording() {
        if (isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.pause();
            }
            isPaused = true;
            Toast.makeText(mContext, "录像暂停", Toast.LENGTH_SHORT).show();
            // 停止闪烁小红点
            blinkUtil.stopBlinking();
            // 暂停录制时间
            recordTimeUtil.pause();
        }
    }


    //开始结束录像
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startStopRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }


    public void resumeRecording() {
        if (isRecording && isPaused) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.resume();
            }
            isPaused = false;
            Toast.makeText(mContext, "录像继续...", Toast.LENGTH_SHORT).show();
            // 开始闪烁小红点
            blinkUtil.startBlinking();
            // 开始显示录制时间
            recordTimeUtil.resume();
        }
    }

    public void pauseResumeRecording() {
        if (isRecording) {
            if (isPaused) {
                // 当前是暂停状态，恢复录制
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mediaRecorder.resume();
                }
                isPaused = false;
                Toast.makeText(mContext, "录像继续...", Toast.LENGTH_SHORT).show();
                // 开始闪烁小红点
                blinkUtil.startBlinking();
                // 开始显示录制时间
                recordTimeUtil.resume();
            } else {
                // 当前是录制状态，暂停录制
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mediaRecorder.pause();
                }
                isPaused = true;
                Toast.makeText(mContext, "录像暂停", Toast.LENGTH_SHORT).show();
                // 停止闪烁小红点
                blinkUtil.stopBlinking();
                // 暂停录制时间
                recordTimeUtil.pause();
            }
        } else {
//            Toast.makeText(mContext, "录制尚未开始", Toast.LENGTH_SHORT).show();
        }
    }

    //水印暂时不处理
    public void stopRecording() {
        if (isRecording) {
            Toast.makeText(mContext, "录像结束", Toast.LENGTH_SHORT).show();
            if (mediaRecorder != null) {
                try {
                    mediaRecorder.stop();
                } catch (RuntimeException e) {
                    Log.e(TAG, "Error stopping MediaRecorder: " + e.getMessage());
                    // 处理异常，例如释放资源或通知用户
                } finally {
                    releaseMediaRecorder();
                }
            }
            isRecording = false;
            isPaused = false;

            // 停止并隐藏小红点
            blinkUtil.stopBlinking();
            // 停止录制时间
            recordTimeUtil.stop();

            Log.i(TAG, "录像完成: " + videoFilePath);
            // 显示 Toast
            Toast.makeText(mContext, "录像完成: " + videoFilePath, Toast.LENGTH_SHORT).show();
            // 通知媒体扫描器扫描新文件
            SavePictureTask.notifyMediaScanner(mContext, videoFilePath);
            if (callback == null) {
                return;
            } else {
                callback.onVideoRecorded(videoFilePath);
            }

            //如果不需要加水印 直接callback 如果需要加水印 则走如下逻辑
//            String currentTime;
//            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//            String finalWaterMark = waterMarkText + "\n" + currentTime;
//            VideoWaterMarkUtil.addTextWatermarkToVideo(videoFilePath, finalWaterMark, new VideoWaterMarkUtil.FFmpegResponseHandler() {
//                @Override
//                public void onSuccess(String markedVideoFilePath) {
//                    //录制路径返回
//                    Log.e(TAG, "水印处理完成 文件保存为: " + markedVideoFilePath);
//                    Toast.makeText(mContext, "录像完成: " + markedVideoFilePath, Toast.LENGTH_SHORT).show();
//                    // 通知媒体扫描器扫描新文件
//                    SavePictureTask.notifyMediaScanner(mContext, markedVideoFilePath);
//                    if (callback == null) {
//                        return;
//                    } else {
//                        callback.onVideoRecorded(markedVideoFilePath);
//                    }
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    // Handle failure
//                    Log.e("FFmpeg", "Failed to add watermark: " + message);
//                    if (callback != null) {
//                        callback.onVideoRecorded(videoFilePath);
//                    }
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    // Handle progress
//                    Log.d("FFmpeg", "Progress: " + message);
//                }
//
//                @Override
//                public void onStart() {
//                    // Handle start
//                    Log.d("FFmpeg", "Watermarking started.");
//                }
//
//                @Override
//                public void onFinish() {
//                    // Handle finish
//                    Log.d("FFmpeg", "Watermarking finished.");
//                }
//            });
        }
    }

    /**
     * 销毁录像实例
     */
    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            if (camera != null) {
                // 锁定相机以便后续相机操作
                camera.lock();
            }
        }
    }


    //生命周期处理
    public void onDestroy() {
        if (isRecording) {
            stopRecording();
        }
        releaseMediaRecorder();
        releaseCamera();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isPaused() {
        return isPaused;
    }


    public void updateWaterMarkView(String msg) {
        ImageWaterMarkUtil.getInstance().updateWatermark(msg, waterMarkView);
    }

    public void zoomIn() {
        Toast.makeText(mContext, "拉近焦距", Toast.LENGTH_SHORT).show();
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            if (params.isZoomSupported()) {
                int currentZoom = params.getZoom();
                if (currentZoom < params.getMaxZoom()) {
                    params.setZoom(currentZoom + 1);
                    camera.setParameters(params);
                }
            } else {
                Toast.makeText(mContext, "Zoom not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void zoomOut() {
        Toast.makeText(mContext, "拉远焦距", Toast.LENGTH_SHORT).show();
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            if (params.isZoomSupported()) {
                int currentZoom = params.getZoom();
                if (currentZoom > 0) {
                    params.setZoom(currentZoom - 1);
                    camera.setParameters(params);
                }
            } else {
                Toast.makeText(mContext, "Zoom not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public interface CameraResultCallback {
        void onPictureTaken(String pictureFilePath);

        void onVideoRecorded(String videoFilePath);

        void onError(String errorMessage);
    }
}



