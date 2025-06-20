package com.zhanchen.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhanchen.main.ui.SplashActivity;


public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                // 开机广播 在这里启动你的应用程序
                Intent launchIntent = new Intent(context, SplashActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        }
    }
}
