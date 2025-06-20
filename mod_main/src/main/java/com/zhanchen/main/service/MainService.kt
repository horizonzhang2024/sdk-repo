package com.zhanchen.main.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.sum.common.constant.MAIN_SERVICE_HOME
import com.sum.common.service.IMainService
import com.zhanchen.main.MainActivity
import com.zhanchen.main.ui.ArticleDetailActivity

/**
 * @author zhanchen
 * @date   2023/3/26 18:23
 * @desc   主页服务
 * 提供对IMainService接口的具体实现
 */
@Route(path = MAIN_SERVICE_HOME)
class MainService : IMainService {
    /**
     * 跳转主页
     * @param context
     * @param index tab位置
     */
    override fun toMain(context: Context, index: Int) {
        com.zhanchen.main.MainActivity.start(context, index)
    }

    /**
     * 跳转主页
     * @param context
     * @param url
     * @param title 标题
     */
    override fun toArticleDetail(context: Context, url: String, title: String) {
        ArticleDetailActivity.start(context, url, title)
    }

    override fun init(context: Context?) {
    }
}