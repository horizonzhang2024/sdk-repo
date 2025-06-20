package com.ocean.camerakit.camera;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/**
 * File: PermissionsHelper.java
 * Author: zhanchen
 * Date: 2024/8/28
 * Description: 权限请求封装
 */
public class PermissionsHelper {

    private static final int REQUEST_CODE = 1001; // 默认请求代码
    private static String[] permissions; // 需要申请的权限
    private static PermissionsCallback callback;

    public interface PermissionsCallback {
        void onPermissionsGranted();

        void onPermissionsDenied();
    }

    // 初始化需要的权限，并返回当前实例以支持链式调用
    public static PermissionsHelper init(String[] requiredPermissions) {
        permissions = requiredPermissions;
        return new PermissionsHelper();
    }

    // 检查并请求权限，并返回当前实例以支持链式调用
    public PermissionsHelper checkAndRequestPermissions(Activity activity, PermissionsCallback callback) {
        PermissionsHelper.callback = callback;

        if (permissions == null || permissions.length == 0) {
            throw new IllegalStateException("Permissions not initialized. Call init() first.");
        }

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
        } else {
            callback.onPermissionsGranted();
        }

        return this;
    }

    // 处理权限请求结果
    public static void handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE == requestCode) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                if (callback != null) {
                    callback.onPermissionsGranted();
                }
            } else {
                if (callback != null) {
                    callback.onPermissionsDenied();
                }
            }
        }
    }
}