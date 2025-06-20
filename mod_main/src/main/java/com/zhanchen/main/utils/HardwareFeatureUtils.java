package com.zhanchen.main.utils;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class HardwareFeatureUtils {
    public static void showHardwareFeatures(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] features = packageManager.getSystemAvailableFeatures();

        StringBuilder featureNames = new StringBuilder();
        for (FeatureInfo feature : features) {
            String featureName = feature.name;
            featureNames.append(featureName).append("\n");
        }

        Toast.makeText(context, featureNames.toString(), Toast.LENGTH_LONG).show();
    }
}
