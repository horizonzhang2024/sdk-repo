package com.zhanchen.process

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import com.sum.framework.log.LogUtil
import com.sum.framework.manager.ActivityManager
import com.sum.framework.manager.AppFrontBack
import com.sum.framework.manager.AppFrontBackListener
import com.sum.framework.toast.TipsToast
import com.sum.stater.dispatcher.TaskDispatcher
import com.zhanchen.process.task.InitAppManagerTask
import com.zhanchen.process.task.InitArouterTask
import com.zhanchen.process.task.InitMmkvTask
import com.zhanchen.process.task.InitRefreshLayoutTask
import com.zhanchen.process.task.InitSumHelperTask
import org.opencv.android.OpenCVLoader

/**
 * @author zhanchen
 * @date   2023/2/9 23:19
 * @desc   应用类
 */
class SumApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "OpenCV initialization failed")
        } else {
            Log.d("OpenCV", "OpenCV initialization succeeded")
        }
        //注册APP前后台切换监听
        appFrontBackRegister()
        // App启动立即注册监听
        registerActivityLifecycle()
        TipsToast.init(this)
        //1.启动器：TaskDispatcher初始化
        TaskDispatcher.init(this)
        //2.创建dispatcher实例
        val dispatcher: TaskDispatcher = TaskDispatcher.createInstance()
        //3.添加任务并且启动任务
        dispatcher.addTask(InitSumHelperTask(this))
            .addTask(InitMmkvTask())
            .addTask(InitAppManagerTask())
            .addTask(InitRefreshLayoutTask())
            .addTask(InitArouterTask())
            .start()

        //4.等待，需要等待的方法执行完才可以往下执行
        dispatcher.await()
    }

    /**
     * 注册APP前后台切换监听
     */
    private fun appFrontBackRegister() {
        AppFrontBack.register(this, object : AppFrontBackListener {
            override fun onBack(activity: Activity?) {
                LogUtil.d("onBack")
            }

            override fun onFront(activity: Activity?) {
                LogUtil.d("onFront")
            }
        })
    }

    /**
     * 注册Activity生命周期监听
     */
    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.pop(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                ActivityManager.push(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }
        })
    }
}