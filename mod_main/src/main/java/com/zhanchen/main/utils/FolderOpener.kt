package com.zhanchen.main.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sum.framework.toast.TipsToast

object FolderOpener {
    fun openFolder(context: Context, folderPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = getUriForFolderPath(folderPath)
        intent.setDataAndType(uri, "resource/folder")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // 处理文件管理器未找到的情况
            TipsToast.showTips("找不到合适程序打开")
        }
    }

    private fun getUriForFolderPath(folderPath: String): Uri {
        val uri: Uri
        if (folderPath.startsWith("/sdcard") || folderPath.startsWith("/storage")) {
            uri = Uri.parse("file://$folderPath")
        } else {
            uri = Uri.parse("content://$folderPath")
        }
        return uri
    }
}
