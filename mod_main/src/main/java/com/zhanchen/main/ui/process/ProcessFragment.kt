package com.zhanchen.main.ui.process

import LicenseGenerator
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ocean.camerakit.QuickCameraActivity
import com.ocean.camerakit.bean.ProcessBean
import com.ocean.camerakit.camera.ProcessMediaFileUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.sum.framework.base.BaseMvvmFragment
import com.sum.framework.log.LogUtil
import com.sum.framework.toast.TipsToast
import com.zhanchen.main.databinding.FragmentMineHeadBinding
import com.zhanchen.main.databinding.FragmentProcessBinding
import com.zhanchen.main.ui.process.adapter.TaskAdapter
import com.zhanchen.main.ui.process.entity.CarListBean
import com.zhanchen.main.ui.process.entity.FirstBean
import com.zhanchen.main.ui.process.entity.FourthBean
import com.zhanchen.main.ui.process.entity.KeyValueBean
import com.zhanchen.main.ui.process.entity.PickerData
import com.zhanchen.main.ui.process.entity.SecondBean
import com.zhanchen.main.ui.process.entity.ThirdBean
import com.zhanchen.main.ui.process.viewmodel.ProcessViewModel
import com.zhanchen.main.ui.system.adapter.ArticleAdapter
import com.zhanchen.main.utils.DialogUtils
import com.zhanchen.main.utils.JsonArrayUtil
import com.zhanchen.main.utils.SharedPreferencesHelper


/**
 * @author zhanchen
 * @date   2023/3/3 8:22
 * @desc   我的
 */
class ProcessFragment : BaseMvvmFragment<FragmentProcessBinding, ProcessViewModel>(),
    OnRefreshListener, OnLoadMoreListener {

    private var spKey2: String = ""
    private var spKey3: String = ""
    private var spKey4: String = ""
    private var launchCameraTimeMills: Long = 0L;

    private var licenValidTimeMills: Long = 180 * 24 * 60 * 60 * 1000L;
    var mProcessBean: ProcessBean? = null
    private val CUSTOM_CARTYPE = "custom_cartype"
    private val CUSTOM_FIRST = "custom_first"
    private val LICENSE_VALID = "license_validated"
    private val LICENSE_EXPIRE = "license_expire_time"
    private val PICKER_INIT_TIP = "请选择作业信息"
    private val PICK_CARTYPE = 0
    private val PICK_WORKINFO = 1
    private var mPage = 0
    private lateinit var mHeadBinding: FragmentMineHeadBinding
    private lateinit var mAdapter: ArticleAdapter
    private var mCarDatas: MutableSet<String> = mutableSetOf()
    private var mPickerTaskData: PickerData = PickerData()
    private var mFirstDatas: MutableSet<String> = mutableSetOf()
    private var mSecondDatas: MutableMap<String, MutableList<String>> = mutableMapOf()
    private var mThirdDatas: MutableMap<String, MutableList<String>> = mutableMapOf()
    private var mFourthDatas: MutableMap<String, MutableList<String>> = mutableMapOf()
    private var mSelectCarType: String = ""
    private var mSelectLevel1: String = ""
    private var mSelectLevel2: String = ""
    private var mSelectLevel3: String = ""
    private var mSelectLevel4: String = ""
    private var mSavedDir: String = ""

    private val pendingTasks: MutableList<KeyValueBean> = mutableListOf()
    private var pendingTaskAdapter: TaskAdapter? = null
    private var mSelectedStep: String = ""  //当前步骤
    private lateinit var teaPickerView: TeaPickerView
    private var mSelectedLevel = 0 //1当前选择了firsttext 2 secondtext 3 thirdtext
    private var mStepFlag = false
    private val PERMISSION_REQUEST_CODE = 100

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initCarPickerView()
        initTaskPickerView()
        initCaptureBtn()
//        initLicenseKey()
    }


    /**
     * 校验必填信息
     */
    private fun initCaptureBtn() {
        mBinding?.btnCaptureMedia?.setOnClickListener {

            //工号 必填
            if (mBinding?.etvEmpNo?.text.toString().isNullOrEmpty()) {
                TipsToast.showTips("请先完善工号信息")
                return@setOnClickListener
            }

            //车号 必填
            if (mBinding?.etvCarNo?.text.toString().isNullOrEmpty()) {
                TipsToast.showTips("请先完善车组号信息")
                return@setOnClickListener
            }

            //车型 必填
            if (mSelectCarType.isNullOrEmpty()) {
                TipsToast.showTips("请先完善车型信息")
                return@setOnClickListener
            }


            //一级作业项 必填
            if (mSelectLevel1.isNullOrEmpty() || mSelectLevel1.equals(
                    PICKER_INIT_TIP, true
                )
            ) {
                TipsToast.showTips("请先完善一级作业信息")
                return@setOnClickListener
            }

            //二级作业项 必填
            if (mSelectLevel2.isNullOrEmpty()) {
                TipsToast.showTips("请先完善二级作业信息")
                return@setOnClickListener
            }


            //三级作业项 要么为空  要么不能是 "快速搜索”
            if (mSelectLevel3 == "快速搜索") {
                TipsToast.showTips("请先完善三级作业信息")
                return@setOnClickListener
            }


            mProcessBean = ProcessBean(
                mBinding?.etvName!!.text.toString().trim(),
                mBinding?.etvEmpNo!!.text.toString().trim(),
                mSelectCarType,
                mBinding?.etvCarNo!!.text.toString().trim(),
                mSelectLevel1,
                mSelectLevel2,
                mSelectLevel3,
                mSelectLevel4
            )
            requestCameraPermission()
        }


        mBinding?.btnGoToMedia?.setOnClickListener {
            // 打开图库
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/* video/*"
            startActivityForResult(intent, REQUEST_PICK_MEDIA)
        }
    }

    override fun onFragmentVisible(isVisibleToUser: Boolean) {
        LogUtil.e("isVisibleToUser:$isVisibleToUser")
    }

    /**
     * 车型选择器
     */
    private fun initCarPickerView() {
        var teaPickerView1 = TeaPickerView(
            requireContext() as Activity?, PickerData()
        )

        //点击选择器按钮
        mBinding?.btnPickCar?.setOnClickListener {
            //刷新选择器数据
            refreshPickData(teaPickerView1, PICK_CARTYPE)
            //显示选择器
            teaPickerView1.show(it)
        }
        //选择器点击事件
        teaPickerView1.setOnPickerClickListener { pickerData: PickerData ->
            val selectInfo = pickerData.firstText
            Toast.makeText(
                requireContext(), selectInfo, Toast.LENGTH_SHORT
            ).show()

            if (selectInfo.equals("自定义")) {
                DialogUtils.showDialogWithInput(requireContext(),
                    "请输入自定义车型",
                    object : DialogUtils.CustomInputListener {
                        override fun onCustomInputConfirmed(inputText: String) {
                            val customSet = SharedPreferencesHelper.getSet(
                                requireContext(), CUSTOM_CARTYPE
                            )
                            customSet.add(inputText)
                            SharedPreferencesHelper.saveSet(
                                requireContext(), CUSTOM_CARTYPE, customSet
                            )
                        }

                        override fun onCustomInputCanceled() {
                        }

                    })
            }
            teaPickerView1.dismiss() //关闭选择器
            mBinding?.btnPickCar?.text = "车型:$selectInfo"
            mSelectCarType = selectInfo
            PickerData.preText = selectInfo

        }
    }

    /**
     * 作业信息 选择器
     */
    private fun initTaskPickerView() {
        teaPickerView = TeaPickerView(
            requireContext() as Activity?, PickerData()
        )
        mBinding?.btnPickTask?.setOnClickListener {
            //显示选择器
            refreshPickData(teaPickerView, PICK_WORKINFO)
            teaPickerView.show(it)
        }
        //选择器点击事件
        teaPickerView.setOnPickerClickListener { pickerData: PickerData ->
            val selectInfo =
                pickerData.firstText + " " + pickerData.secondText + " " + pickerData.thirdText + " " + pickerData.fourthText
            Toast.makeText(
                requireContext(), selectInfo, Toast.LENGTH_SHORT
            ).show()

            //二级菜单 点击自定义
            clickCustomEvent(pickerData, 2)
            clickCustomEvent(pickerData, 3)
            clickCustomEvent(pickerData, 4)
            teaPickerView.dismiss() //关闭选择器
            //更新选择器按钮显示文本
            updatePickerTaskBtnText(pickerData)
        }
    }

    private fun updatePickerTaskBtnText(pickerData: PickerData) {
        mBinding?.btnPickTask?.text = if (!pickerData.fourthText.isNullOrEmpty()) {
            //有四级菜单
            pickerData.firstText + "\n" + pickerData.secondText + "\n" + pickerData.thirdText + "\n" + pickerData.fourthText
        } else if (!pickerData.thirdText.isNullOrEmpty()) {
            //有三级菜单
            pickerData.firstText + "\n" + pickerData.secondText + "\n" + pickerData.thirdText
        } else if (!pickerData.secondText.isNullOrEmpty()) {
            //有二级菜单
            pickerData.firstText + "\n" + pickerData.secondText
        } else {
            //只有一级菜单
            pickerData.firstText
        }

        //更新相机相册多媒体文件命名信息
        mSelectLevel1 = pickerData.firstText
        mSelectLevel2 = pickerData.secondText
        mSelectLevel3 = if (pickerData.thirdText.isNullOrEmpty()) {
            "通用"
        } else {
            pickerData.thirdText
        }
        mSelectLevel4 = pickerData.fourthText
    }


    /**
     * 自定义菜单 点击事件
     * 可处理： 自定义  快速搜索
     */
    private fun clickCustomEvent(pickerData: PickerData, selectLevel: Int) {
        when (selectLevel) {
            2 -> {//点击2级菜单 自定义
                if (pickerData.secondText.equals("自定义")) {
                    spKey2 = "自定义_" + pickerData.firstText
                    DialogUtils.showDialogWithInput(requireContext(),
                        "请输入自定义信息",
                        object : DialogUtils.CustomInputListener {
                            override fun onCustomInputConfirmed(inputText: String) {
                                val customSet = SharedPreferencesHelper.getSet(
                                    requireContext(), spKey2

                                )
                                customSet.add(inputText)
                                SharedPreferencesHelper.saveSet(
                                    requireContext(), spKey2, customSet
                                )

                                //自动选择刚刚添加的自定义菜单
                                pickerData.secondText = inputText
                                updatePickerTaskBtnText(pickerData)
                            }

                            override fun onCustomInputCanceled() {
                            }

                        })
                }
            }

            3 -> {//点击3级菜单 自定义
                if (pickerData.thirdText.equals("自定义")) {
                    spKey3 = "自定义_" + pickerData.firstText + "_" + pickerData.secondText
                    DialogUtils.showDialogWithInput(requireContext(),
                        "请输入自定义信息",
                        object : DialogUtils.CustomInputListener {
                            override fun onCustomInputConfirmed(inputText: String) {
                                val customSet = SharedPreferencesHelper.getSet(
                                    requireContext(), spKey3

                                )
                                customSet.add(inputText)
                                SharedPreferencesHelper.saveSet(
                                    requireContext(), spKey3, customSet
                                )

                                //自动选择刚刚添加的自定义菜单
                                pickerData.thirdText = inputText
                                updatePickerTaskBtnText(pickerData)
                            }

                            override fun onCustomInputCanceled() {
                            }
                        })
                } else if (pickerData.thirdText.equals("快速搜索")) {
                    mBinding?.btnPickTask?.postDelayed({
                        val data = mThirdDatas["$mSelectCarType&$mSelectLevel2"]
                        data?.run {
                            DialogUtils.showAutoCompleteDialog(requireContext(),
                                "快速搜索当前类别菜单",
                                data,
                                object : DialogUtils.CustomInputListener {
                                    override fun onCustomInputConfirmed(inputText: String) {
                                        //自动选择刚刚添加的自定义菜单
                                        pickerData.thirdText = inputText
                                        updatePickerTaskBtnText(pickerData)
                                    }

                                    override fun onCustomInputCanceled() {

                                    }

                                })
                        }
                    }, 200)
                }
            }

            4 -> {//点击4级菜单 自定义
                if (pickerData.fourthText.equals("自定义")) {
                    spKey4 =
                        "自定义_" + pickerData.firstText + "_" + pickerData.secondText + "_" + pickerData.thirdText
                    DialogUtils.showDialogWithInput(requireContext(),
                        "请输入自定义信息",
                        object : DialogUtils.CustomInputListener {
                            override fun onCustomInputConfirmed(inputText: String) {
                                val customSet = SharedPreferencesHelper.getSet(
                                    requireContext(), spKey4
                                )
                                customSet.add(inputText)
                                SharedPreferencesHelper.saveSet(
                                    requireContext(), spKey4, customSet
                                )

                                //自动选择刚刚添加的自定义菜单
                                pickerData.fourthText = inputText
                                updatePickerTaskBtnText(pickerData)
                            }

                            override fun onCustomInputCanceled() {
                            }

                        })
                }
            }
        }

    }


    /**
     * 刷新数据 初始化选择器数据
     */
    private fun refreshPickData(
        pickerView: TeaPickerView, pickType: Int
    ) {
        when (pickType) {
            PICK_WORKINFO -> { //作业信息
                //一级列表
                val firstBean = FirstBean()
                mFirstDatas.addAll(firstBean.repData.content)
                //二级列表
                val secondBean = SecondBean()
                mSecondDatas.putAll(secondBean.repData.content)
                //三级列表
                val thirdBean = ThirdBean()
                mThirdDatas.putAll(thirdBean.repData.content)
                //四级列表
                val fourthBean = FourthBean()
                mFourthDatas.putAll(fourthBean.repData.content)

                //添加自定义菜单到选项卡
                updateCustomOptionsToPicker()

                LogUtil.i(
                    JsonArrayUtil.toJson(
                        mFirstDatas
                    ), tag = "JsonData"
                )
                LogUtil.i(
                    JsonArrayUtil.toJson(
                        mSecondDatas
                    ), tag = "JsonData"
                )
                LogUtil.i(
                    JsonArrayUtil.toJson(
                        mThirdDatas
                    ), tag = "JsonData"
                )
                //设置数据有多少层级
                val data = mPickerTaskData
                data.firstDatas = mFirstDatas.toList() //json: ["广东","江西"]
                data.secondDatas =
                    mSecondDatas //json: {"江西":["南昌","赣州"],"广东":["广州","深圳","佛山","东莞"]}
                data.thirdDatas = mThirdDatas.toMap()
                data.fourthDatas = mFourthDatas.toMap()
                //默认提示信息 如果希望下次点开自动显示上一次选择的内容 则注释掉初始化
//                data.setInitSelectText(PICKER_INIT_TIP)
                pickerView.pickerData = data
                pickerView.setScreenH(2).setHeights(360).setDiscolourHook(true).setRadius(25)
                    .setContentLine(true).setRadius(25).build()
            }

            PICK_CARTYPE -> { //车型
                //一级列表
                val carListBean = CarListBean()
                mCarDatas.addAll(carListBean.repData.carTypes)
                //更新自定义输入的作业内容
                mCarDatas.remove("自定义")
                mCarDatas.addAll(
                    SharedPreferencesHelper.getSet(
                        requireContext(), CUSTOM_CARTYPE
                    )
                )
                mCarDatas.add("自定义")
                //设置数据有多少层级
                val data1 = PickerData()
                data1.firstDatas = mCarDatas.toList() //json: ["广东","江西"]
                data1.setInitSelectText("请选择车型")
                pickerView.pickerData = data1
                pickerView.setScreenH(2).setHeights(360).setDiscolourHook(true).setRadius(25)
                    .setContentLine(true).setRadius(25).build()
            }
        }

    }

    /**
     * 添加自定义菜单内容到筛选器
     * 包含二级 三级 四级自定义
     */
    private fun updateCustomOptionsToPicker() {
        //二级菜单自定义
        mSecondDatas[mSelectLevel1]?.run {
            if (this.contains("自定义")) {
                this.remove("自定义")
                this.addAll(
                    SharedPreferencesHelper.getSet(
                        requireContext(), spKey2
                    )
                )
                this.add("自定义")
            }
        }


        //三级菜单自定义
        mThirdDatas["$mSelectCarType&$mSelectLevel2"]?.run {
            if (this.contains("自定义")) {
                this.remove("自定义")
                this.addAll(
                    SharedPreferencesHelper.getSet(
                        requireContext(), spKey3
                    )
                )
                this.add("自定义")
            }
        }


        //四级菜单自定义
        mFourthDatas[mSelectLevel3]?.run {
            if (this.contains("自定义")) {
                this.remove("自定义")
                this.addAll(
                    SharedPreferencesHelper.getSet(
                        requireContext(), spKey4
                    )
                )
                this.add("自定义")
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 0
    }


    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mPage++
    }


    /**
     * 生成license
     */
    private fun generateLicenseKey(number: Int) {
        // 在 Android 项目中无法直接运行 main 函数，这里仅作为示例代码展示
        val licenseGenerator = LicenseGenerator(requireContext())
        licenseGenerator.generateLicenses(number)
    }


    /**
     * 获取权限
     */
    @SuppressLint("CheckResult")
    private fun requestCameraPermission() {
        // 定义需要请求的权限列表
        val permissionsToRequest = mutableListOf<String>()

        // 检查 WRITE_EXTERNAL_STORAGE 权限
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // 检查 READ_EXTERNAL_STORAGE 权限
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // 检查 RECORD_AUDIO 权限
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }

        // 检查 CAMERA 权限
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        // 如果有需要请求的权限，发起权限请求
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE
            )
        } else {
            // 所有权限都已授予，启动相机相关操作
            try {
//                    launchCamera()
                QuickCameraActivity.start(requireContext(), processBean = mProcessBean!!)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }



    //启动系统相机
    private fun launchCamera() {
        // 启动系统相机应用
        launchCameraTimeMills = System.currentTimeMillis()
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
//        val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
        startActivityForResult(intent, REQUEST_GO_MEDIA)
    }


    private val REQUEST_PICK_MEDIA = 100002
    private val REQUEST_GO_MEDIA = 100001

    //处理文件重命名 搬运逻辑
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GO_MEDIA && mProcessBean != null) {
            //文件夹层级
            val newDir = ProcessMediaFileUtils.getOutputMediaDir(
                requireContext(), mProcessBean
            )
            mSavedDir = newDir

            //文件前缀
            val fileName = ProcessMediaFileUtils.getOutputMediaFileName(
                mProcessBean
            )

            //拍摄完 搬运文件重命名文件 拍摄视频保存后太快返回会抓取不到 需要一点点时间 所以做个延时1s处理
            //如果进入相机后 切换了照相 视频，那么只会保存第一个拍摄文件 另外一个后缀是.thumbnail
            TipsToast.showTips("正在保存拍摄文件...")
            Handler().postDelayed({
                ProcessMediaFileUtils.moveCameraFiles(
                    requireContext(), launchCameraTimeMills, newDir, fileName, null
                )
            }, 1000)
        } else if (requestCode === REQUEST_PICK_MEDIA && resultCode === Activity.RESULT_OK && data != null) {
            val selectedMediaUri: Uri? = data.data
            selectedMediaUri?.run {
                val intent: Intent = Intent(
                    requireContext(), com.zhanchen.main.ui.process.PreviewActivity::class.java
                )
                val mimeType: String? = requireContext().contentResolver.getType(this)
                if (mimeType != null && mimeType.startsWith("image/")) {
                    // 从图库选择的是图像
                    // 处理图像预览逻辑
                    intent.putExtra("uri_type", 0)
                } else if (mimeType != null && mimeType.startsWith("video/")) {
                    // 从图库选择的是视频
                    // 处理视频预览逻辑
                    intent.putExtra("uri_type", 1)
                }
                intent.data = this
                startActivity(intent)
            }
        }
    }


    private fun initLicenseKey() {
        //检验是否要弹密钥框   本地没有标记 或者有标记但是过期了
        val isLicenseChecked = SharedPreferencesHelper.getBoolean(
            requireContext(), LICENSE_VALID, false
        )
        val licenseExpiredTime = SharedPreferencesHelper.getString(
            requireContext(), LICENSE_EXPIRE, System.currentTimeMillis().toString()
        )
        val currenMills = System.currentTimeMillis();
        val expireMills = licenseExpiredTime.toLong()
        val isLicenseExpired = currenMills > expireMills
        if ((!isLicenseChecked) || isLicenseExpired) {
            DialogUtils.showLicenseValidationDialog(requireContext(),
                object : DialogUtils.LicenseValidateListener {
                    override fun onLicenseSucceed(license: String) {
                        SharedPreferencesHelper.saveBoolean(
                            requireContext(), LICENSE_VALID, true
                        )
                        SharedPreferencesHelper.saveString(
                            requireContext(),
                            LICENSE_EXPIRE,
                            (System.currentTimeMillis() + licenValidTimeMills).toString()
                        )
                    }

                    override fun onLicenseFailed() {
                    }
                })
        }

        //安装证书license
        mBinding?.btnGenerate?.setOnClickListener {
//            generateLicenseKey(10000)
        }
    }
}