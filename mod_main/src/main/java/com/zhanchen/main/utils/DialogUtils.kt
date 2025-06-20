package com.zhanchen.main.utils

import LicenseGenerator
import android.R.attr.data
import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Filter
import android.widget.Toast
import com.sum.framework.ext.layoutInflater
import com.sum.framework.toast.TipsToast
import com.zhanchen.main.R
import com.zhanchen.main.ui.adapter.CustomAutoCompleteAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date


object DialogUtils {
    fun showLicenseValidationDialog(context: Context, listener: LicenseValidateListener) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.license_validation_dialog, null)
        val etLicense = dialogView.findViewById<EditText>(R.id.etLicense)
        etLicense.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
        val alertDialog =
            AlertDialog.Builder(context).setView(dialogView).setTitle("请验证许可证密钥")
                .setMessage("密钥未授权或者已过期，请提供有效的生产许可证以获取完整的使用权限。")
                .setCancelable(false).setPositiveButton("验证", null) // 设置按钮监听器为null，稍后自定义处理
                .create()
        alertDialog.show()
        // 获取对话框的确定按钮
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            // 在此处处理许可证验证逻辑
            val enteredLicense = etLicense.text.toString()
            // 进行许可证验证
            val jsonAry = LicenseGenerator(context).loadJsonArrayFromFile()
            if (jsonAry.toString()
                    .contains("\"license\":\"$enteredLicense\"", ignoreCase = false)
            ) {
                // 许可证有效，显示有效对话框
                // 获取当前日期并转换为可操作的日期对象
                val calendar: Calendar = Calendar.getInstance()
                val currentDate: Date = calendar.time
                // 将当前日期增加 180 天
                calendar.add(Calendar.DAY_OF_YEAR, 180)
                val futureDate: Date = calendar.time
                // 将增加后的日期转换为 YYYYMMDD 格式
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate: String = dateFormat.format(futureDate)
                // 创建 Toast 消息
                TipsToast.showTips("验证通过. 密钥有效期延长至$formattedDate")
                // 关闭对话框
                alertDialog.dismiss()
                listener.onLicenseSucceed(enteredLicense)
            } else {
                // 许可证无效，显示无效对话框
                TipsToast.showTips("验证失败. 请联系运维人员以获取完整许可！")
                listener.onLicenseFailed()
            }
        }


        // 设置对话框宽度为屏幕宽度的0.6倍
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val dialogWidth = (screenWidth * 0.8).toInt()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.attributes)
        layoutParams.width = dialogWidth
        alertDialog.window?.attributes = layoutParams
    }


    //带输入框的弹框
    fun showDialogWithInput(
        context: Context, dialogTitle: String, inputListener: CustomInputListener
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_input, null)
        builder.setView(dialogView)

        builder.setTitle(dialogTitle).setPositiveButton("确认") { dialog, which ->
            val editTextInput = dialogView.findViewById<EditText>(R.id.editTextInput)
            val userInput: String = editTextInput.text.toString()
            // 在这里处理用户输入的文本
            Toast.makeText(context, "$userInput", Toast.LENGTH_SHORT).show()
            inputListener.onCustomInputConfirmed(userInput)
        }.setNegativeButton(
            "取消"
        ) { dialog, which -> // 用户点击取消按钮时的操作
            dialog.dismiss()
            inputListener.onCustomInputCanceled()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    //带模糊搜索列表输入框的弹框
    fun showAutoCompleteDialog(
        context: Context,
        dialogTitle: String,
        data: List<String>,
        inputListener: CustomInputListener
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_auto_complete, null)
        builder.setView(dialogView)

        val autoCompleteTextView =
            dialogView.findViewById<AutoCompleteTextView>(R.id.autoCompleteTV)

        // 自定义适配器
        val adapter = CustomAutoCompleteAdapter(context, ArrayList(data))

        // 设置 AutoCompleteTextView 的适配器和阈值
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 0

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            autoCompleteTextView.setText(selectedItem)
        }
        builder.setTitle(dialogTitle).setPositiveButton("确认") { dialog, which ->
            val editTextInput = dialogView.findViewById<AutoCompleteTextView>(R.id.autoCompleteTV)
            val userInput: String = editTextInput.text.toString()
            // 在这里处理用户输入的文本
            Toast.makeText(context, "$userInput", Toast.LENGTH_SHORT).show()
            inputListener.onCustomInputConfirmed(userInput)
        }.setNegativeButton(
            "取消"
        ) { dialog, which -> // 用户点击取消按钮时的操作
            dialog.dismiss()
            inputListener.onCustomInputCanceled()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    interface LicenseValidateListener {
        fun onLicenseSucceed(license: String)
        fun onLicenseFailed()
    }


    fun showSingleButtonDialog(
        context: Context?, title: String?, message: String?, buttonText: String?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title).setMessage(message).setPositiveButton(
            buttonText
        ) { dialog, id -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }


    interface CustomInputListener {
        fun onCustomInputConfirmed(inputText: String)
        fun onCustomInputCanceled()
    }
}

