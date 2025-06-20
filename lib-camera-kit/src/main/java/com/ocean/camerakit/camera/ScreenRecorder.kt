package com.ocean.camerakit.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.ToastUtils
import java.io.IOException

/**
 * 屏幕录制工具类
 */
class ScreenRecorder(
    val context: Context,
    private var outputFilePath: String, // 修改为 var
    val callback: ScreenRecorderCallback
) {
    companion object {
        private const val REQUEST_CODE_SCREEN_CAPTURE = 1001
    }

    private var mediaRecorder: MediaRecorder? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null

    private val displayMetrics: DisplayMetrics = DisplayMetrics()
    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private var isRecording: Boolean = false

    init {
        setupDisplayMetrics()
        setupMediaRecorder()
    }

    private fun setupDisplayMetrics() {
        val display = windowManager.defaultDisplay
        display.getRealMetrics(displayMetrics)
    }

    private fun setupMediaRecorder() {
        mediaRecorder = MediaRecorder().apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setVideoSource(MediaRecorder.VideoSource.SURFACE)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(outputFilePath)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setVideoFrameRate(30)
                setVideoEncodingBitRate(5 * 1024 * 1024)
                prepare()
            } catch (e: IOException) {
                e.printStackTrace() // Log error
                callback.onRecordingError("Failed to prepare MediaRecorder: ${e.message}")
            } catch (e: IllegalStateException) {
                e.printStackTrace() // Log error
                callback.onRecordingError("Failed to prepare MediaRecorder: ${e.message}")
            }
        }
    }


    fun start(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val projectionManager =
                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            val captureIntent = projectionManager.createScreenCaptureIntent()
            activity.startActivityForResult(captureIntent, REQUEST_CODE_SCREEN_CAPTURE)
        } else {
            callback.onRecordingError("Screen recording requires API level 21 or higher.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val projectionManager =
                    context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                mediaProjection = projectionManager.getMediaProjection(resultCode, data)
                startRecording()
            } else {
                callback.onRecordingError("Failed to get MediaProjection")
            }
        }
    }

    private fun startRecording() {
        if (mediaProjection == null) {
            callback.onRecordingError("MediaProjection is not initialized")
            return
        }

        try {
            virtualDisplay = mediaProjection?.createVirtualDisplay(
                "ScreenRecorder",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder?.surface,
                null,
                null
            )
            mediaRecorder?.start()
            isRecording = true
            callback.onRecordingStarted()
            ToastUtils.showShort("开始录音......")
        } catch (e: IllegalStateException) {
            callback.onRecordingError("Failed to start MediaRecorder: ${e.message}")
        }
    }


    fun stop() {
        if (isRecording) {
            mediaRecorder?.let {
                try {
                    it.stop()
                    callback.onRecordingStopped(outputFilePath)
                    ToastUtils.showShort("结束录音......")
                } catch (e: Exception) {
                    callback.onRecordingError("Failed to stop MediaRecorder: ${e.message}")
                } finally {
                    it.reset()
                    it.release()
                    mediaRecorder = null
                }
            }

            virtualDisplay?.release()
            virtualDisplay = null
            isRecording = false
            mediaProjection?.stop()
            mediaProjection = null
        }
    }


    fun isRecording(): Boolean {
        return isRecording
    }

    // 更新文件路径方法
    fun updateFilePath(newPath: String) {
        try {
            // Stop existing recording if any
            stop()

            // Setup new file path and reinitialize MediaRecorder
            outputFilePath = newPath
            setupMediaRecorder()

        } catch (ex: IllegalStateException) {
            ex.localizedMessage?.let { Log.e("ScreenRecorder", it) }
            callback.onRecordingError("Failed to update file path: ${ex.message}")
        } catch (e: IOException) {
            e.printStackTrace() // Log error
            callback.onRecordingError("Failed to update file path: ${e.message}")
        }
    }


    interface ScreenRecorderCallback {
        fun onRecordingStarted()
        fun onRecordingStopped(filePath: String)
        fun onRecordingError(error: String)
    }
}


//扩展函数
fun ScreenRecorder.startScreenRecording(newOutputFilePath: String) {
    if (!isRecording()) {
        updateFilePath(newOutputFilePath)
        start(context as Activity)
    } else {
        callback.onRecordingError("Already recording. Stop the current recording first.")
    }
}


