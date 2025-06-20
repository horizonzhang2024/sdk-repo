package com.ocean.camerakit.camera;

import android.os.Handler;
import android.view.View;

/**
 * 录像小红点 闪烁控制工具类
 */
public class BlinkingIndicatorUtil {
    private Handler indicatorHandler = new Handler();
    private Runnable blinkRunnable;
    private View indicator;    // Add this line for a reference to the view

    public BlinkingIndicatorUtil(final View indicator) {
        this.indicator = indicator;    // Save the indicator View reference
        blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (indicator != null) {
                    if (indicator.getVisibility() == View.VISIBLE) {
                        indicator.setVisibility(View.INVISIBLE);
                    } else {
                        indicator.setVisibility(View.VISIBLE);
                    }
                    indicatorHandler.postDelayed(this, 500); // 每500毫秒切换一次可见性
                }
            }
        };
    }

    public void startBlinking() {
        if (indicatorHandler != null) {
            indicatorHandler.post(blinkRunnable);
        }
        if (indicator != null) {
            indicator.setVisibility(View.VISIBLE);
        }
    }

    public void pauseBlinking() {
        stopBlinking();
        if (indicator != null) {
            indicator.setVisibility(View.GONE);
        }
    }

    public void stopBlinking() {
        if (indicatorHandler != null) {
            indicatorHandler.removeCallbacks(blinkRunnable);
        }
        if (indicator != null) {
            indicator.setVisibility(View.GONE); // 保持可见
        }
    }
}

