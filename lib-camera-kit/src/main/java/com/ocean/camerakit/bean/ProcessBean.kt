package com.ocean.camerakit.bean

import java.io.Serializable

/**
 * 工序宝 通用作业对象
 */
data class ProcessBean(
    val name: String,
    val empNo: String,
    val carType: String,
    val carNo: String,
    var level1: String,
    var level2: String,
    var level3: String,
    var level4: String?
) : Serializable

//名字_工号_车型_车号_作业1_作业2_作业3_时间戳.mp4