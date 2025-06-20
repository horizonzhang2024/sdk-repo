package com.ocean.camerakit

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ocean.camerakit.bean.ProcessBean
import com.ocean.camerakit.camera.CameraHelper
import com.ocean.camerakit.camera.DialogUtils
import com.ocean.camerakit.databinding.ActivityCameraBinding
import com.ocean.camerakit.ext.setOnClickDebounced
import com.ocean.camerakit.utils.DebounceHelper


/**
 * 带有实时预览的拍照工具
 */
class QuickCameraActivity : AppCompatActivity() {

    private val TAG = "QuickCameraActivity"
    private lateinit var cameraHelper: CameraHelper
    private lateinit var mBinding: ActivityCameraBinding
    private var firstTime: Long = 0
    private var keyCenterDebouncer: DebounceHelper? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //onkeydown的防抖间隔为3000ms
        keyCenterDebouncer = DebounceHelper(3000)
        // 解析传入的 ProcessBean
        intent?.let {
            if (it.hasExtra("process_bean")) {
                val processBean = it.getSerializableExtra("process_bean") as ProcessBean
                processBean.let { bean ->
                    // 使用 processBean 进行后续操作
                    mProcessBean = bean
                }
            }
        }
        // 禁止屏幕自动熄屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //==================================拍照sdk集成 start==================================
        cameraHelper = CameraHelper(this,
            mBinding.viewSurface,
            mBinding.recordingIndicator,
            mBinding.waterMarkView,
            mProcessBean?.level1 + "_" + mProcessBean?.level2,
            mBinding.recordTimer,
            object : CameraHelper.CameraResultCallback {
                override fun onPictureTaken(pictureFilePath: String?) {
                    Log.d(TAG, "Picture taken: $pictureFilePath")

                }

                override fun onVideoRecorded(videoFilePath: String?) {
                    Log.d(TAG, "Video recorded: $videoFilePath")
                }

                override fun onError(errorMessage: String?) {
                    Log.d(TAG, "camera error: $errorMessage")
                }

            })
        cameraHelper.initializeCamera()
        //==================================拍照sdk集成 end==================================


        mBinding.takePictureButton.setOnClickDebounced {
            cameraHelper.takePicture(mProcessBean?.level1 + "_" + mProcessBean?.level2)
        }


        mBinding.startRecordingButton.setOnClickDebounced {
            cameraHelper.startStopRecording()
            if (cameraHelper.isRecording) {
                //预览按钮容易导致拍摄视频退出后文件异常 隐藏掉入口
//                mBinding.pauseResumeRecordButton.visibility = View.VISIBLE
                mBinding.pauseResumeRecordButton.visibility = View.GONE
                mBinding.startRecordingButton.setImageResource(R.mipmap.icon_stop)
            } else {
                mBinding.pauseResumeRecordButton.visibility = View.GONE
                mBinding.startRecordingButton.setImageResource(R.mipmap.icon_start)
            }
        }

        mBinding.pauseResumeRecordButton.setOnClickDebounced(3000) {
            cameraHelper.pauseResumeRecording()
            if (cameraHelper.isPaused) {
                mBinding.pauseResumeRecordButton.setImageResource(R.mipmap.icon_resume)
            } else {
                mBinding.pauseResumeRecordButton.setImageResource(R.mipmap.icon_pause)
            }
        }


        mBinding.preViewButton.setOnClickDebounced {
            // 打开图库
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/* video/*"
            startActivityForResult(intent, 1001)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraHelper.onDestroy()
    }

    override fun onPause() {
        super.onPause()
//        cameraHelper.pauseResumeRecording()
    }

    override fun onResume() {
        super.onResume()
//        cameraHelper.pauseResumeRecording()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraHelper.handlePermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 接管实体按键
     */
    /**
     * @param keyCode 上19 下20 左4 右27 中 23
     * @param event   系统行为优先级别高 无法拦截
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // 打印所有的 KeyEvent
        Log.d("KeyEvent", "onKeyDown Key Code: " + keyCode + ", Action: " + event.action)
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (keyCenterDebouncer?.isDebounced() == true) {
                    // 中间键
                    // 单按开始录像，再按结束，不需要暂停操作
                    cameraHelper.run {
                        if (isRecording) {
                            stopRecording()
                        } else {
                            startRecording()
                        }
                    }
                }
                return true // 返回true表示事件已处理
            }

            KeyEvent.KEYCODE_DPAD_UP -> {
                // 拉近焦距放大 zoomIn
                cameraHelper.zoomIn()
                return true // 返回true表示事件已处理
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                // 拉远焦距缩小 zoomOut
                cameraHelper.zoomOut()
                return true // 返回true表示事件已处理
            }

            KeyEvent.FLAG_KEEP_TOUCH_MODE -> {
                val secondTime = System.currentTimeMillis()
                if (secondTime - firstTime > 1000) { // 如果两次按键时间间隔大于1秒，则不退出
                    Toast.makeText(this, "再按一次退出录像", Toast.LENGTH_SHORT).show()
                    firstTime = secondTime // 更新firstTime
                    return true // 返回true表示不再响应系统动作，返回false表示继续响应系统动作
                } else {
                    confirmFinish()
                    return true
//                    return super.onKeyDown(keyCode, event) // 其他按键事件交由父类处理
                }
            }

            KeyEvent.KEYCODE_CAMERA -> {
                //拍照
                cameraHelper.takePicture(mProcessBean?.level1 + "_" + mProcessBean?.level2)
                return true // 返回true表示事件已处理
            }

            else -> return super.onKeyDown(keyCode, event) // 其他按键事件交由父类处理
        }
    }


    override fun onBackPressed() {
        // 打印所有的 KeyEvent
        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime > 1000) { // 如果两次按键时间间隔大于1秒，则不退出
            Toast.makeText(this, "再按一次退出录像", Toast.LENGTH_SHORT).show()
            firstTime = secondTime // 更新firstTime
        } else { // 两次按键小于1秒时，退出录像
//            super.onBackPressed()
            confirmFinish()
        }
    }

    private fun confirmFinish() {
        DialogUtils.showTwoButtonDialog(
            this, "温馨提示", "是否确认退出录像？", "确认", { _, _ ->
                cameraHelper.stopRecording()
                finish()
            }, "取消", null
        )

    }


    companion object {
        var mProcessBean: ProcessBean? = null

        // 启动 QuickCameraActivity 的静态方法
        fun start(context: Context, processBean: ProcessBean) {
            val intent = Intent(context, QuickCameraActivity::class.java)
            intent.putExtra("process_bean", processBean)
            context.startActivity(intent)
        }
    }
}
