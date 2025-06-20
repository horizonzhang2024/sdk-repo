package com.sum.main;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.zhanchen.main.ui.SplashActivity;


public class BootCompletedJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        // 启动目标应用程序的主活动
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.d("zcg", "自动启动啦");
        return false; // 任务已完成
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
