package com.ocean.camerakit.utils

import android.app.Application

/**
 * @author zhanchen
 * @date   2023/3/2 16:10
 * @desc   提供应用环境
 */
object ModuleAppHelper {
    private lateinit var app: Application
    private var isDebug = false

    fun init(application: Application, isDebug: Boolean) {
        this.app = application
        this.isDebug = isDebug
    }

    /**
     * 获取全局应用
     */
    fun getApplication() = app

    /**
     * 是否为debug环境
     */
    fun isDebug() = isDebug
}