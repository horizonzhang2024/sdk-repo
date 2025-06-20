package com.ocean.camerakit.camera;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.util.Log;

/**
 * 硬件加速解码器类型
 */
public class HardwareDecoders {

    public static void listSupportedHardwareDecoders() {
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        MediaCodecInfo[] codecInfos = codecList.getCodecInfos();

        for (MediaCodecInfo codecInfo : codecInfos) {
            if (!codecInfo.isEncoder() && codecInfo.getName().startsWith("OMX.")) {
                Log.d("HardwareDecoders", "Decoder Name: " + codecInfo.getName());
                String[] supportedTypes = codecInfo.getSupportedTypes();
                for (String type : supportedTypes) {
                    Log.d("HardwareDecoders", "    Type: " + type);
                }
            }
        }
    }


    /**
     * @param decoderName
     * @return 例如传入"OMX.qcom.video.decoder.avc"
     * OMX.qcom.video.decoder.avc - video/avc
     * OMX.qcom.video.decoder.hevc - video/hevc
     * OMX.qcom.video.decoder.mpeg2 - video/mpeg2
     * OMX.qcom.video.decoder.vp9 - video/x-vnd.on2.vp9
     */
    public static boolean checkHardwareAccelerationSupported(String decoderName) {
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        MediaCodecInfo[] codecInfos = codecList.getCodecInfos();

        for (MediaCodecInfo codecInfo : codecInfos) {
            if (!codecInfo.isEncoder() && codecInfo.getName().equalsIgnoreCase(decoderName)) {
                return true;
            }
        }
        return false;
    }
}
