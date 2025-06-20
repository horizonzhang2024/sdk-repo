package com.zhanchen.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sum.common.provider.MainServiceProvider
import com.sum.framework.base.BaseDataBindActivity
import com.sum.framework.ext.countDownCoroutines
import com.sum.framework.ext.onClick
import com.sum.framework.utils.StatusBarSettingHelper
import com.zhanchen.main.R
import com.zhanchen.main.databinding.ActivitySplashBinding

/**
 * @author zhanchen
 * @date   2023/3/29 14:25
 * @desc   启动页
 */
class SplashActivity : BaseDataBindActivity<ActivitySplashBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarSettingHelper.setStatusBarTranslucent(this)

        if (intent.action != null && intent.action == Intent.ACTION_MAIN) {
            val intent = Intent(applicationContext, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        mBinding.tvSkip.onClick {
            MainServiceProvider.toMain(this)
        }
        //倒计时
        countDownCoroutines(0, lifecycleScope, onTick = {
            mBinding.tvSkip.text = getString(R.string.splash_time, it.plus(1).toString())
        }) {
            MainServiceProvider.toMain(this)
            finish()
        }


    }
}