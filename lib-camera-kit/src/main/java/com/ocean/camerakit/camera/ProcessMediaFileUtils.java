package com.ocean.camerakit.camera;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

import androidx.annotation.NonNull;


import com.ocean.camerakit.BuildConfig;
import com.ocean.camerakit.bean.ProcessBean;
import com.ocean.camerakit.ext.StringExtKt;
import com.ocean.camerakit.utils.TipsToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 工序命名工具类 系统相机拍摄后自动重新命名
 * 处理后的文件都 Root/DCIM/RecordVideo/XXXXXX
 * SD存储的默认目录 Root/DCIM/JW7137/XXXXXX
 */
public class ProcessMediaFileUtils {
    private static final String DEVICE_MODEL_DIR_1 = "JW7136";
    private static final String DEVICE_MODEL_DIR_2 = "JW7137";
    private static final String TAG = "ProcessMediaFileUtils";

    private static String deviceModelDir = DEVICE_MODEL_DIR_2;

    public static void moveCameraFiles(Context context, long timeInMillis, String newDir, String newFileName, OperationCallback callback) {
        File mainDir = CameraFileUtils.getDCIM(context);
        //检查DCIM目录是否存在 并确保创建目录
        if (!CameraFileUtils.ensureFileExists(mainDir)) {
            Log.e(TAG, "Failed to create directory");
            return;
        }

        if (isDirExist(mainDir, deviceModelDir)) {
            //默认7137存在 deviceModelDir不用变
        } else if (isDirExist(mainDir, DEVICE_MODEL_DIR_1)) {
            //7136目录存在
            deviceModelDir = DEVICE_MODEL_DIR_1;
        } else {
            deviceModelDir = "";
        }

        //调试用的代码
        if (BuildConfig.DEBUG) {
            // 用华为手机debug时 JW7137目录不存在 打开该注释；海洋王手电运行时 恢复JW7137
            deviceModelDir = "";
        }

        File listDir = new File(mainDir, deviceModelDir);
        if (CameraFileUtils.ensureFileExists(listDir)) {
            processFiles(context, listDir, timeInMillis, newDir, newFileName, callback);
        }
    }


    //检查目录是否存在
    private static boolean isDirExist(File mainDir, String deviceDir) {
        File dir = new File(mainDir, deviceDir);
        return CameraFileUtils.exists(dir);
    }


    /**
     * @param context
     * @param listDir             mainDir+deviceModel 系统拍摄的默认存储目录
     * @param launchCameraMills   启动相机的时间
     * @param destinationDiret    新目录
     * @param destinationFileName 新文件名
     * @param callback
     */
    private static void processFiles(Context context, File listDir, long launchCameraMills, String destinationDiret, String destinationFileName, OperationCallback callback) {
        File[] files = listDir.listFiles();
        if (files == null) {
            if (callback != null) {
                callback.onResult(false, "Failed to list files in directory: " + listDir.getAbsolutePath());
            }
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                processFiles(context, file, launchCameraMills, destinationDiret, destinationFileName, callback);
            } else if (file.isFile() && file.lastModified() > launchCameraMills) {
                Log.e(TAG, "发现一个新文件::" + file.getName());
                String originalFilename = file.getName();

                if (originalFilename.endsWith(".mp4") || originalFilename.endsWith(".jpg")) {
                    String newFilename = appendFilePrefix(originalFilename, destinationFileName);
                    File destinationDir = new File(destinationDiret);

                    if (!CameraFileUtils.ensureFileExists(destinationDir)) {
                        if (callback != null) {
                            callback.onResult(false, "Failed to create destination directory: " + destinationDir);
                        }
                        return;
                    }

                    File destinationFile = new File(destinationDir, newFilename);
                    boolean renamed = file.renameTo(destinationFile);

                    if (!renamed) {
                        if (callback != null) {
                            callback.onResult(false, "Failed to rename file: " + file.getAbsolutePath());
                        }
                        return;
                    }

                    if (!destinationFile.exists()) {
                        if (callback != null) {
                            callback.onResult(false, "Destination file doesn't exist: " + destinationFile.getAbsolutePath());
                        }
                        return;
                    }

                    if (callback != null) {
                        callback.onResult(true, "Destination file exists");
                    }

                    MediaScannerConnection.scanFile(context, new String[]{destinationFile.getAbsolutePath()}, null, (path, uri) -> {
                        TipsToast.INSTANCE.showSuccessTips("拍摄文件已添加到图库");
                    });
                }
            } else {
                if (callback != null) {
                    callback.onResult(false, "file is neither a directory nor a newer file: " + file.getAbsolutePath());
                }
            }
        }
    }


    /**
     * 文件夹层级 命名
     *
     * @param context
     * @param processBean
     * @return
     */
    public static String getOutputMediaDir(Context context, ProcessBean processBean) {
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.getTime());
        String yearMonth = new SimpleDateFormat("yyyyMM", Locale.getDefault()).format(calendar.getTime());
        String month = new SimpleDateFormat("MM", Locale.getDefault()).format(calendar.getTime());
        String monthDay = new SimpleDateFormat("MMdd", Locale.getDefault()).format(calendar.getTime());
        String namePrefix = "";
        File mediaStorageDir;
        File DCIM_Dir = CameraFileUtils.getDCIM(context);

        //SD卡存储 或者内置存储
        if (processBean != null) {
            StringBuffer sb = getOutputDirSb(processBean, year, month, monthDay, namePrefix);
            mediaStorageDir = new File(DCIM_Dir, "RecordVideo" + sb);
        } else {
            mediaStorageDir = new File(DCIM_Dir, "RecordVideo" + File.separator + yearMonth + File.separator + monthDay + File.separator + namePrefix);
        }

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
                return null;
            }
        }
        return mediaStorageDir.getPath();
    }

    /**
     * 文件夹命名逻辑 层级
     *
     * @param processBean
     * @param year
     * @param month
     * @param monthDay
     * @param namePrefix
     * @return
     */
    @NonNull
    private static StringBuffer getOutputDirSb(ProcessBean processBean, String year, String month, String monthDay, String namePrefix) {
        StringBuffer sb = new StringBuffer();
//        sb = StringExtKt.safetyAppendDir(sb, processBean.getCarType() + "_" + processBean.getLevel2());
        sb = StringExtKt.safetyAppendDir(sb, year + "年");
        sb = StringExtKt.safetyAppendDir(sb, month + "月");
        sb = StringExtKt.safetyAppendDir(sb, monthDay + "日");
        sb = StringExtKt.safetyAppendDir(sb, processBean.getEmpNo());
//        sb = StringExtKt.safetyAppendDir(sb, processBean.getLevel1());
//        sb = StringExtKt.safetyAppendDir(sb, processBean.getLevel2());
//        sb = StringExtKt.safetyAppendDir(sb, processBean.getLevel3());
//        sb = StringExtKt.safetyAppendDir(sb, processBean.getLevel4());
//        sb = StringExtKt.safetyAppendDir(sb, processBean.getName());
        sb = StringExtKt.safetyAppendDir(sb, namePrefix);
        return sb;
    }


    /**
     * 文件名加上IMG VID前缀
     *
     * @param originalFilename
     * @param destinationFilename
     * @return
     */
    private static String appendFilePrefix(String originalFilename, String destinationFilename) {
        //保留xxx_012633.jpg
//        int lastUnderscoreIndex = originalFilename.lastIndexOf("_");
        //保留xxx.jpg
        int lastUnderscoreIndex = originalFilename.lastIndexOf(".");
        if (lastUnderscoreIndex >= 0) {
            //此处若想去掉_ 就lastUnderscoreIndex+1
            String suffix = originalFilename.substring(lastUnderscoreIndex);
            String prefix = "";
            if (suffix.endsWith(".jpg")) {
//                prefix = "IMG_";
                prefix = "";
            } else if (suffix.endsWith(".mp4")) {
//                prefix = "VID_";
                prefix = "";
            }
            return prefix + destinationFilename + suffix;
        }
        return destinationFilename;
    }

    /**
     * 文件命名逻辑
     *
     * @param processBean
     * @return
     */
    public static String getOutputMediaFileName(ProcessBean processBean) {
        StringBuffer sb = new StringBuffer();
        if (processBean != null) {
            Calendar calendar = Calendar.getInstance();
            String yearMonthDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            String hHmmss = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(calendar.getTime());
            //调整顺序
            sb = StringExtKt.safetyAppend(sb, processBean.getEmpNo());
            sb = StringExtKt.safetyAppend(sb, hHmmss);
            sb = StringExtKt.safetyAppend(sb, processBean.getCarNo());
//            sb = StringExtKt.safetyAppend(sb, processBean.getName());
            sb = StringExtKt.safetyAppend(sb, processBean.getLevel1());
            sb = StringExtKt.safetyAppend(sb, processBean.getLevel2());
//            sb = StringExtKt.safetyAppend(sb, processBean.getLevel3());
//            sb = StringExtKt.safetyAppend(sb, processBean.getCarType());
//            sb = StringExtKt.safetyAppend(sb, processBean.getLevel4());

        }
        String workerPrefix = "";
        if (sb.toString().endsWith("_")) {
            //去掉最末尾的_
            workerPrefix = sb.substring(0, sb.length() - 1);
        } else {
            workerPrefix = sb.toString();
        }
        return workerPrefix;
    }


    // 定义一个函数类型的回调
    public interface OperationCallback {
        void onResult(boolean isSuccess, String message);
    }
}
