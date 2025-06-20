package com.ocean.camerakit.camera

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

/**
 * 操作记录写入txt工具类
 */
data class ImageComparisonRecord(
    var index: Int,
    var taskName: String,
    var imageName: String = "未拍摄",
    var similarityResult: String = "未比对"
) {
    // 将数据类转为 JSONObject
    fun toJson(): JSONObject {
        return JSONObject(GsonBuilder().setPrettyPrinting().create().toJson(this))
    }
}

class ImageCompareFileUtil {
    private val records: MutableList<ImageComparisonRecord> = mutableListOf()
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val TAG = "ImageCompareFileUtil"

    // 从 JSONArray 加载记录
    fun loadRecordsFromJsonArray(jsonArray: JSONArray) {
        records.clear() // 清空当前记录

        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            if (isValidRecord(jsonObject)) {
                val record = gson.fromJson(jsonObject.toString(), ImageComparisonRecord::class.java)
                records.add(record)
            } else {
                Log.w(TAG, "无效的记录数据: ${jsonObject.toString()}")
            }
        }
        Log.i(TAG, "从 JSONArray 加载记录成功, 当前记录数: ${records.size}")
    }

    // 校验记录数据的有效性
    private fun isValidRecord(jsonObject: JSONObject): Boolean {
        return jsonObject.has("index") && jsonObject.has("taskName") &&
                jsonObject.has("imageName") && jsonObject.has("similarityResult")
    }

    // 添加记录
    fun addRecord(
        taskName: String,
        imageName: String = "未拍摄",
        similarityResult: String = "未比对"
    ) {
        synchronized(records) { // 确保线程安全
            // 查找是否已经存在相同 taskName 的记录
            val existingRecordIndex = records.indexOfFirst { it.taskName == taskName }

            if (existingRecordIndex != -1) {
                // 如果找到了记录，则更新该记录
                val existingRecord = records[existingRecordIndex]
                existingRecord.imageName = imageName
                existingRecord.similarityResult = similarityResult
                Log.i(
                    TAG,
                    "覆盖记录 - 任务名称: $taskName, 新图片名称: $imageName, 新相似度结果: $similarityResult"
                )
            } else {
                // 如果没有找到记录，则创建新记录并添加到列表
                val newIndex = (records.maxOfOrNull { it.index } ?: 0) + 1
                val record = ImageComparisonRecord(
                    index = newIndex,
                    taskName = taskName,
                    imageName = imageName,
                    similarityResult = similarityResult
                )
                records.add(record)
                Log.i(TAG, "添加记录 - $record")
            }
        }
    }

    // 通过 taskName 更新记录的 similarityResult 和 imageName
    fun updateRecord(taskName: String, similarityResult: String, imageName: String) {
        if (taskName.isEmpty()) {
            Log.w(TAG, "任务名称为空，无法更新记录")
            return
        }

        synchronized(records) { // 确保线程安全
            val recordsToUpdate = records.filter { it.taskName == taskName }
            if (recordsToUpdate.isNotEmpty()) {
                recordsToUpdate.forEach {
                    it.similarityResult = similarityResult
                    it.imageName = imageName
                }
                Log.i(
                    TAG,
                    "更新记录 - 任务名称: $taskName, 新相似度结果: $similarityResult, 新图片名称: $imageName"
                )
            } else {
                Log.w(TAG, "未找到任务名称为 $taskName 的记录")
            }
        }
    }

    // 将记录转换为 JSONArray
    fun getRecordsAsJsonArray(): JSONArray {
        return JSONArray(records.map { it.toJson() })
    }

    // 根据任务名称查找记录
    fun findRecordsByTask(taskName: String): List<ImageComparisonRecord> {
        synchronized(records) { // 确保线程安全
            return records.filter { it.taskName == taskName }
        }
    }

    // 根据相似度结果查找记录
    fun findRecordsBySimilarity(result: String): List<ImageComparisonRecord> {
        synchronized(records) { // 确保线程安全
            return records.filter { it.similarityResult == result }
        }
    }

    // 获取当前记录
    fun getRecords(): List<ImageComparisonRecord> {
        return records.toList() // 返回不可变列表
    }

    // 将记录写入指定目录的 txt 文件
    fun writeRecordsToFile(directoryPath: String, fileName: String) {
        Thread {
            val directory = File(directoryPath)
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Log.e(TAG, "无法创建目录: $directoryPath")
                    return@Thread
                }
            }

            val file = File(directory, fileName)
            try {
                file.writeText(getRecordsAsJsonArray().toString(2))
                Log.i(TAG, "记录成功写入文件: ${file.absolutePath}")
            } catch (e: IOException) {
                Log.e(TAG, "写入文件失败: ${e.message}", e)
            }
        }.start()
    }
}