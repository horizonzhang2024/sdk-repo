package com.ocean.camerakit.utils;

/**
 * 用来给实体按键增加防抖逻辑 例如onkeydown
 */
public class DebounceHelper {

    private long lastTime;
    private long debounceInterval;

    public DebounceHelper(long debounceInterval) {
        this.debounceInterval = debounceInterval;
        this.lastTime = 0;
    }

    public boolean isDebounced() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < debounceInterval) {
            return false;
        }
        lastTime = currentTime;
        return true;
    }
}