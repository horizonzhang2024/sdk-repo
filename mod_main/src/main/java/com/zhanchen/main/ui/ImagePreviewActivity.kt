package com.zhanchen.main.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.sum.common.constant.KEY_URL
import com.sum.common.manager.FileManager
import com.sum.framework.base.BaseDataBindActivity
import com.sum.framework.ext.onClick
import com.sum.framework.toast.TipsToast
import com.sum.framework.utils.getStringFromResource
import com.sum.glide.setUrl
import com.zhanchen.main.databinding.ActivityImagePreviewBinding
import com.zhanchen.main.utils.ImageUtil
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author zhanchen
 * @date   2023/4/25 22:58
 * @desc   图片预览view
 */
class ImagePreviewActivity : BaseDataBindActivity<ActivityImagePreviewBinding>() {

    private val PERMISSION_REQUEST_CODE = 11

    companion object {
        fun start(context: Context, url: String?) {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            url?.let {
                intent.putExtra(KEY_URL, it)
            }
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val url = intent?.getStringExtra(KEY_URL)
        mBinding.ivPreview.setUrl(url)
        mBinding.titleBar.getRightTextView().onClick {
            //获取权限
            requestPermission()
        }
    }

    /**
     * 获取权限
     */
    private fun requestPermission() {
        // 检查是否已经授予了读写外部存储的权限
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_GRANTED &&
            readPermission == PackageManager.PERMISSION_GRANTED
        ) {
            // 权限已经授予，保存图片
            saveImage()
        } else {
            // 请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }
            if (allPermissionsGranted) {
                // 所有权限都被授予，保存图片
                saveImage()
            } else {
                // 权限被拒绝，显示提示信息
                TipsToast.showTips(com.sum.common.R.string.default_agree_permission)
            }
        }
    }

    /**
     * 保存图片
     */
    private fun saveImage() {
        lifecycleScope.launch {
            val bitmap = ImageUtil.viewToBitmap(mBinding.ivPreview)
            val path = FileManager.getImageDirectory(this@ImagePreviewActivity)
            val file = File(path, "${System.currentTimeMillis()}${FileManager.JPG_SUFFIX}")
            val success = ImageUtil.save(bitmap, file, Bitmap.CompressFormat.JPEG, false)
            val res =
                if (success) com.sum.common.R.string.default_save_success else com.sum.common.R.string.default_save_fail
            TipsToast.showTips(getStringFromResource(res))
        }
    }
}