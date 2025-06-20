package com.ocean.camerakit.camera;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.util.Locale;

/**
 * 更新当前已经录制的时间
 */
public class RecordingTimeUtils {
    private TextView timeLabel;
    private Handler handler;
    private Runnable updateTimeRunnable;
    private long startTime;
    private long pausedTimeStartTime; // 用于记录暂停开始的时间
    private long totalPausedTime; // 用于记录总暂停时间
    private boolean isRunning = false;
    private boolean isPaused = false;

    public RecordingTimeUtils(TextView timeLabel) {
        if (timeLabel == null) {
            throw new IllegalArgumentException("TextView cannot be null");
        }
        this.timeLabel = timeLabel;
        this.handler = new Handler(Looper.getMainLooper());
        this.updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning && !isPaused) {
                    try {
                        updateTimer();
                    } catch (Exception e) {
                        // 处理可能的异常，避免崩溃
                        e.printStackTrace();
                    }
                    handler.postDelayed(this, 1000); // 每秒更新一次
                }
            }
        };
    }

    private void updateTimer() {
        long currentTime = System.currentTimeMillis();
        long recordTime = currentTime - startTime - totalPausedTime;
        int totalSeconds = (int) (recordTime / 1000);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        // 使用Locale.ROOT以避免地区问题
        timeLabel.setText(String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, seconds));
    }

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            totalPausedTime = 0;
            handler.post(updateTimeRunnable);
            isRunning = true;
            isPaused = false;
        }
    }

    public void pause() {
        if (isRunning && !isPaused) {
            isPaused = true;
            pausedTimeStartTime = System.currentTimeMillis();
            handler.removeCallbacks(updateTimeRunnable);
        }
    }


    public void resume() {
        if (isRunning && isPaused) {
            totalPausedTime += System.currentTimeMillis() - pausedTimeStartTime;
            isPaused = false;
            handler.post(updateTimeRunnable);
        }
    }

    public void stop() {
        if (isRunning) {
            handler.removeCallbacks(updateTimeRunnable);
            isRunning = false;
            isPaused = false;
        }
    }
}
