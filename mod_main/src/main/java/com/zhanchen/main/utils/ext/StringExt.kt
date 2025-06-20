package com.zhanchen.main.utils.ext

import java.io.File


fun StringBuffer.safetyAppend(txt: String?): StringBuffer {
    if (txt.isNullOrEmpty()) {
        return this
    } else {
        return this.append(txt).append("_")
    }
}


fun StringBuffer.safetyAppendDir(txt: String?): StringBuffer {
    if (txt.isNullOrEmpty()) {
        return this
    } else {
        return this.append(File.separator).append(txt)
    }
}