package com.ocean.camerakit.camera

import android.app.AlertDialog
import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import com.ocean.camerakit.R
import com.ocean.camerakit.ext.layoutInflater
import com.ocean.camerakit.widget.CustomAutoCompleteAdapter

object DialogUtils {
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
        val adapter = CustomAutoCompleteAdapter(
            context,
            ArrayList(data)
        )

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


    /**
     * 显示一个双按钮弹框
     *
     * @param context 上下文
     * @param title 弹框标题
     * @param message 弹框内容
     * @param positiveButtonText 确认按钮文本
     * @param positiveButtonListener 确认按钮点击事件监听器
     * @param negativeButtonText 取消按钮文本
     * @param negativeButtonListener 取消按钮点击事件监听器
     */
    fun showTwoButtonDialog(
        context: Context?,
        title: String?,
        message: String?,
        positiveButtonText: String?,
        positiveButtonListener: ((dialog: AlertDialog, id: Int) -> Unit)? = null,
        negativeButtonText: String?,
        negativeButtonListener: ((dialog: AlertDialog, id: Int) -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, id ->
                dialog.dismiss()
                positiveButtonListener?.invoke(dialog as AlertDialog, id)
            }
            .setNegativeButton(negativeButtonText) { dialog, id ->
                dialog.dismiss()
                negativeButtonListener?.invoke(dialog as AlertDialog, id)
            }

        val dialog = builder.create()
        dialog.show()
    }


    interface CustomInputListener {
        fun onCustomInputConfirmed(inputText: String)
        fun onCustomInputCanceled()
    }
}

