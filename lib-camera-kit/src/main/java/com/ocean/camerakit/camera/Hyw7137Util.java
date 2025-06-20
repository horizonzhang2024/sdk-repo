package com.ocean.camerakit.camera;

//�<   FileUtil.java Z:\FileUtil.java    2   C:\Users\Fuhaifa\AppData\Local\Temp\FileUtil.java �;  package com.mediatek.camera.location.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Hyw7137Util {
    private static final boolean DEBUG = true;
    private static final String TAG = "FileUtil";
    public static final String BATTERY_VOLTAGE_INTERNAL_FILE = "/sys/class/power_supply/battery/batt_vol";
    public static final String BATTERY_VOLTAGE_EXTERNAL_FILE = "/sys/class/power_supply/battery/bat_vol_smb";
    public static final String LOCATION_GPS_LATLON_FILE = "/sys/devices/platform/bdhl_misc/gnssinfo";
    public static final String LOCATION_IMEIIFO_FILE = "/sys/devices/platform/mt-battery/imeiinfo";
    //激光定位节点 0灭灯，1闪灯，2常亮
    public static final String NODE_FILE_LASER_POSITION = "/sys/devices/platform/leds-mt65xx/ir_status";

    //提示灯：蓝灯：亮度
    private static final String BLUE_LED_FILE = "/sys/class/leds/blue/brightness";
    //提示灯：蓝灯：点亮时间
    private static final String BLUE_LED_TON_FILE = "/sys/class/leds/blue/ton";
    //提示灯：蓝灯：熄灭时间
    private static final String BLUE_LED_TOFF_FILE = "/sys/class/leds/blue/toff";

    //提示灯：蓝灯：触发器
    private static final String BLUE_LED_TRIGGER_FILE = "/sys/class/leds/blue/trigger";
    //提示灯：蓝灯：触发器：正常模式
    private static final String LED_TRIGGER_CC_MODE = "cc_mode";
    //提示灯：蓝灯：触发器：呼吸灯模式
    private static final String LED_TRIGGER_BREATH_MODE = "breath_mode";

    public static final int VIDEOREC_RATIO_4_3 = 1;
    public static final int VIDEOREC_RATIO_16_9 = 2;
    private static final String SYS_PROP_MEDIA_SETTINGS_XML = "persist.media.settings.xml";//modify fuhaifa181023
    private static final String SYS_PROP_MEDIA_SETTINGS_43 = "/vendor/etc/media_profiles.xml";
    private static final String SYS_PROP_MEDIA_SETTINGS_169 = "/vendor/etc/media_profiles_169.xml";
    public static final Long VIDEO_SIZE_1G = 1 * 1024 * 1024 * 1024L;
    public static final Long VIDEO_SIZE_200M = 200 * 1024 * 1024L;
    private final static int DELETE_FILE_STATUS_FAILURE = 0;
    private final static int DELETE_FILE_STATUS_SUCCESS = 1;
    public final static int DELETE_FILE_STATUS_BUSY = 2;
    //startadd fuhaifa180822 savepath
    private static final int FILENAME_MAXBYTES = 200;
    private static final int FILENAME_MAXBYTES_GBK = 60;
    //endadd fuhaifa180822 savepath
    private static Boolean mIsMonitoring = false;
    private static Boolean mIsMonitorSave = false;

    //红灯：0灭灯，1是亮灯
    private static final String SYS_PROP_SETTING_RED_STATE = "/sys/devices/platform/bdhl_misc/red_led_state";


    //绿或黄灯：0灭灯，1是亮灯
    private static final String SYS_PROP_SETTING_GREEN_STATE = "/sys/devices/platform/bdhl_misc/gy_led_state";


    /**
     * 红外激光灯：0灭灯，1闪灯，2常亮
     * /sys/devices/platform/leds-mt65xx/ir_status
     *
     * @param true 开红外激光灯 false 关红外激光灯
     */
    public static void switchIRLed(boolean status) {
        if (status) {
            WriteToNodeFile(NODE_FILE_LASER_POSITION, "2");
        } else {
            WriteToNodeFile(NODE_FILE_LASER_POSITION, "0");
        }
    }


    /**
     * 红灯：0灭灯，1是亮灯
     * /sys/devices/platform/bdhl_misc/red_led_state
     */
    public static void switchRedSignal(boolean status) {
        if (status) {
            WriteToNodeFile(SYS_PROP_SETTING_RED_STATE, "1");
        } else {
            WriteToNodeFile(SYS_PROP_SETTING_RED_STATE, "0");
        }
    }

    /**
     * 绿或黄灯：0灭灯，1是亮灯
     * /sys/devices/platform/bdhl_misc/gy_led_state
     */
    public static void switchGreenSignal(boolean status) {
        if (status) {
            WriteToNodeFile(SYS_PROP_SETTING_GREEN_STATE, "1");
        } else {
            WriteToNodeFile(SYS_PROP_SETTING_GREEN_STATE, "0");
        }
    }

    /*
     * echo "breath_mode" > /sys/class/leds/blue/trigger
     * 延迟10毫秒
     * echo "2" > "/sys/class/leds/blue/ton"
     * echo "2" >  "/sys/class/leds/blue/toff"
     * echo "3" >  /sys/class/leds/blue/brightness
     */
    public static void switchBlueLed(boolean status) {
        if (status) {
            WriteToNodeFile(BLUE_LED_TRIGGER_FILE, LED_TRIGGER_BREATH_MODE);
            WriteToNodeFile(BLUE_LED_TON_FILE, "1");
            WriteToNodeFile(BLUE_LED_TOFF_FILE, "4");
            WriteToNodeFile(BLUE_LED_FILE, "1");
            WriteToNodeFile(NODE_FILE_LASER_POSITION, "1");
        } else {
            WriteToNodeFile(BLUE_LED_FILE, LED_TRIGGER_CC_MODE);
            WriteToNodeFile(BLUE_LED_FILE, "0");
        }
    }

    public static Boolean isMonitoring() {
        return mIsMonitoring;
    }

    public static void setMonitoring(Boolean isMonitoring) {
        mIsMonitoring = isMonitoring;
        if (mIsMonitoring) {
            mIsMonitorSave = true;
        }
    }

    public static Boolean isMonitorSave() {
        return mIsMonitorSave;
    }

    public static void setMonitorSave(Boolean isMonitorSave) {
        mIsMonitorSave = isMonitorSave;
    }

//    public static void setSysPropMediaSetting(int value) {
//        if (value == VIDEOREC_RATIO_16_9) {
//            SystemProperties.set(SYS_PROP_MEDIA_SETTINGS_XML, SYS_PROP_MEDIA_SETTINGS_169);
//        } else {
//            SystemProperties.set(SYS_PROP_MEDIA_SETTINGS_XML, SYS_PROP_MEDIA_SETTINGS_43);
//        }
//    }

//    public static int getSysPropMediaSetting() {
//        int value = VIDEOREC_RATIO_4_3;
//        if (SystemProperties.get(SYS_PROP_MEDIA_SETTINGS_XML, SYS_PROP_MEDIA_SETTINGS_43).equals(SYS_PROP_MEDIA_SETTINGS_169)) {
//            value = VIDEOREC_RATIO_16_9;
//        } else {
//            value = VIDEOREC_RATIO_4_3;
//        }
//        return value;
//    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     **/
    public static void writeToFile(String filepath, String text) { // 新建或打开日志文件
        try {
            File file = new File(filepath);
            FileWriter fileWriter = new FileWriter(file, true); // 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            bufWriter.write(text);
            //bufWriter.newLine();
            bufWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidNmeaMsg(String nmeaStr) {
        byte[] bytes = nmeaStr.getBytes();
        byte checksumCalcValue = 0;
        int checksumIndex = nmeaStr.indexOf("*");
        int checksumValue;

        if ((nmeaStr.charAt(0) != '$') || (checksumIndex == -1)) {
            return false;
        }

        String result = nmeaStr.substring(checksumIndex + 1, nmeaStr.length()).trim();
        checksumValue = Integer.parseInt(result, 16);

        for (int i = 1; i < checksumIndex; i++) {
            checksumCalcValue = (byte) (checksumCalcValue ^ bytes[i]);
        }

        if (checksumValue == checksumCalcValue) {
            return true;
        } else {
            return false;
        }
    }

    public static void WriteToNodeFile(String nodefile, String content) {
        FileWriter filewriter = null;
        try {
            File nodeFile = new File(nodefile);
            filewriter = new FileWriter(nodeFile);
            filewriter.write(content);
            filewriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (filewriter != null) filewriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String ReadFromNodeFile(String nodefile) {
        String value = "";
        int read = 0;
        FileReader filereader = null;
        try {
            File nodeFile = new File(nodefile);
            filereader = new FileReader(nodeFile);
            while ((read = filereader.read()) != -1) {
                if (read != 10) {//\n
                    value = value + (char) read;
                }
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (filereader != null) filereader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
	
	/*public String getCurrentBatteryValue(String filename) {
        String batteryValue = null;
        String mFileName = filename;
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            try {
                File currentFilePath = new File(mFileName);
                if (currentFilePath.exists()) {
                    fileInputStream = new FileInputStream(currentFilePath);
                    inputStreamReader = new InputStreamReader(fileInputStream);
                    br = new BufferedReader(inputStreamReader);
                    String data = null;
                    while ((data = br.readLine()) != null) {
                    	batteryValue = data;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (br != null) {
                    br.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batteryValue;
    }*/

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void RecursionDeleteFile(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

//    public static int deleteOldFileFromFloder(Context context, String path) {
//        int deleteStatus = DELETE_FILE_STATUS_FAILURE;
//        try {
//            ArrayList<File> fileList = new ArrayList<File>();
//            getFiles(fileList, path);
//            if ((fileList != null) && (fileList.size() > 0)) {
//                File oldFile = fileList.get(0);
//                for (int i = 1; i < fileList.size(); i++) {
//                    File nextFile = fileList.get(i);
//                    if (nextFile.exists()) {
//                        if (nextFile.lastModified() < oldFile.lastModified()) {
//                            oldFile = nextFile;
//                        }
//                    } else {
//                        scanCameraPath(context, nextFile.getAbsolutePath());
//                    }
//                }
//                //LogUtil.writeLog(TAG, "=1=FromFloder=date:"+oldFile.lastModified()+"==size:"+oldFile.length()+"==path:"+oldFile.getAbsolutePath());
//                try {
//                    if (oldFile.exists()) {
//                        if ((oldFile.getName().equals(Storage.VideoGpsFileName))
//                                || (oldFile.getName().equals(Storage.VideoTempFileName))) {
//                            //LogUtil.writeLog(TAG, "=1=FromFloder===oldFileName():"+oldFile.getName());
//                            deleteStatus = DELETE_FILE_STATUS_BUSY;
//                        } else {
//                            //LogUtil.writeLog(TAG, "=2=FromFloder===oldFileName():"+oldFile.getName());
//                            deleteStatus = oldFile.delete() ? DELETE_FILE_STATUS_SUCCESS : DELETE_FILE_STATUS_FAILURE;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                fileList.remove(oldFile);
//                scanCameraPath(context, oldFile.getAbsolutePath());
//                //LogUtil.writeLog(TAG, "=2=FromFloder=success:"+success+"==leftSpace:"+Storage.getAvailableSpace()+"==1G:"+VIDEO_SIZE_1G);
//                if (Storage.getAvailableSpace() < VIDEO_SIZE_1G) {
//                    if (fileList.size() > 0) {
//                        deleteStatus = deleteOldFileFromFilelist(context, fileList);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "=3=FromFloder===e:" + e);
//            e.printStackTrace();
//        }
//        //LogUtil.writeLog(TAG, "=4=FromFloder===deleteStatus:"+deleteStatus);
//        return deleteStatus;
//    }

//    private static int deleteOldFileFromFilelist(Context context, ArrayList<File> fileList) {
//        int deleteStatus = DELETE_FILE_STATUS_FAILURE;
//        try {
//            if ((fileList != null) && (fileList.size() > 0)) {
//                File oldFile = fileList.get(0);
//                for (int i = 1; i < fileList.size(); i++) {
//                    File nextFile = fileList.get(i);
//                    if (nextFile.exists()) {
//                        if (nextFile.lastModified() < oldFile.lastModified()) {
//                            oldFile = nextFile;
//                        }
//                    } else {
//                        scanCameraPath(context, nextFile.getAbsolutePath());
//                    }
//                }
//                //LogUtil.writeLog(TAG, "=1=FromFilelist=date:"+oldFile.lastModified()+"==size:"+oldFile.length()+"==path:"+oldFile.getAbsolutePath());
//                try {
//                    if (oldFile.exists()) {
//                        if ((oldFile.getName().equals(Storage.VideoGpsFileName))
//                                || (oldFile.getName().equals(Storage.VideoTempFileName))) {
//                            //LogUtil.writeLog(TAG, "=1=FromFilelist===oldFileName():"+oldFile.getName());
//                            deleteStatus = DELETE_FILE_STATUS_BUSY;
//                        } else {
//                            //LogUtil.writeLog(TAG, "=2=FromFilelist===oldFileName():"+oldFile.getName());
//                            deleteStatus = oldFile.delete() ? DELETE_FILE_STATUS_SUCCESS : DELETE_FILE_STATUS_FAILURE;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                fileList.remove(oldFile);
//                scanCameraPath(context, oldFile.getAbsolutePath());
//                //LogUtil.writeLog(TAG, "=2=FromFilelist=success:"+success+"==leftSpace:"+Storage.getAvailableSpace()+"==1G:"+VIDEO_SIZE_1G);
//                if (Storage.getAvailableSpace() < VIDEO_SIZE_1G) {
//                    if (fileList.size() > 0) {
//                        deleteStatus = deleteOldFileFromFilelist(context, fileList);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "=3=FromFilelist===e:" + e);
//            e.printStackTrace();
//        }
//        //LogUtil.writeLog(TAG, "=4=FromFilelist===deleteStatus:"+deleteStatus);
//        return deleteStatus;
//    }

    private static void getFiles(ArrayList<File> fileList, String path) {
        File[] allFiles = new File(path).listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isFile()) {
                fileList.add(file);
            } else if (file.isDirectory()) {  //!file.getAbsolutePath().contains(".thumnail")
                getFiles(fileList, file.getAbsolutePath());
            }
        }
        //LogUtil.writeLog(TAG, "=1=getFiles===fileList:"+fileList);
    }

    public static void scanCameraPath(Context context, String path) {
        //LogUtil.writeLog(TAG, "=xxx=scanCameraPath===context:"+context+"===path:"+path);
        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        if (context != null) context.sendBroadcast(intent);
    }

    //startadd fuhaifa180822 savepath
    public static String getFileNamePrefix(String savePath) {
        if ((savePath == null) || (savePath.isEmpty())) {
            return "";
        }

        String resStr = "";
        try {
            String[] _pathArr = savePath.split("/");
            int arrLength = _pathArr.length;

            if (arrLength >= 3) {
                resStr = _pathArr[arrLength - 3] + "_" + _pathArr[arrLength - 2] + "_" + _pathArr[arrLength - 1];
            } else if (arrLength == 2) {
                resStr = _pathArr[0] + "_" + _pathArr[1];
            } else if (arrLength == 1) {
                resStr = _pathArr[0];
            }

            if (arrLength > 0) {
                if ((resStr == null) || (resStr.isEmpty())) {
                    return "";
                }
            } else {
                resStr = savePath;
            }

            if (resStr.length() > FILENAME_MAXBYTES_GBK) {
                byte[] raw = resStr.getBytes(StandardCharsets.UTF_8);
                if (raw.length > FILENAME_MAXBYTES) {
                    resStr = trimFilename(resStr, FILENAME_MAXBYTES);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resStr;
    }

    private static String trimFilename(String str, int maxBytes) {
        final StringBuilder res = new StringBuilder(str);
        trimFilename(res, maxBytes);
        return res.toString();
    }

    private static void trimFilename(StringBuilder res, int maxBytes) {
        byte[] raw = res.toString().getBytes(StandardCharsets.UTF_8);
        if (raw.length > maxBytes) {
            maxBytes -= 3;
            while (raw.length > maxBytes) {
                res.deleteCharAt(res.length() / 2);
                raw = res.toString().getBytes(StandardCharsets.UTF_8);
            }
        }
    }
}
      