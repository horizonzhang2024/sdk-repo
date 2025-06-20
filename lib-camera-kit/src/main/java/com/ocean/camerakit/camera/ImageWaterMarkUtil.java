package com.ocean.camerakit.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 照相水印
 */
public class ImageWaterMarkUtil {

    private static final String TAG = "WatermarkUtil";
    private static ImageWaterMarkUtil instance;
    private Handler handler;
    private String watermarkText;

    private Paint paint;

    private ImageWaterMarkUtil() {
        // 初始化 Paint 对象
        paint = new Paint();
        paint.setColor(Color.RED);
//        paint.setColor(0x80FFFFFF); // 水印颜色，白色，透明度为50%
        paint.setTextSize(72);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        handler = new Handler();
    }

    public static synchronized ImageWaterMarkUtil getInstance() {
        if (instance == null) {
            instance = new ImageWaterMarkUtil();
        }
        return instance;
    }


    /**
     * 绘制水印到图片
     *
     * @param bitmap
     * @param watermarkText
     * @return
     */
    public Bitmap addWatermarkToBitmap(Bitmap bitmap, String watermarkText) {
        if (bitmap == null) {
            Log.e(TAG, "Input bitmap is null");
            return null;
        }

        if (TextUtils.isEmpty(watermarkText)) {
            Log.e(TAG, "watermarktext is null");
            return bitmap;
        }

        Bitmap result = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(result);

        // 将水印文本按换行符分割成多行
        String[] lines = watermarkText.split("\n");

        // 初始绘制位置
        int x = 30; // 水平位置
        int y = result.getHeight() - 30 - (lines.length - 1) * (int) (paint.descent() - paint.ascent()); // 初始垂直位置

        // 每行文本的高度
        int lineHeight = (int) (paint.descent() - paint.ascent());

        // 逐行绘制文本
        for (String line : lines) {
            canvas.drawText(line, x, y, paint);
            y += lineHeight; // 移动到下一行的位置
        }

        return result;
    }


    /**
     * 得到水印信息
     */
    public void updateWatermark(String info, TextView watermarkView) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                if (TextUtils.isEmpty(info)) {
                    watermarkText = currentTime;
                } else {
                    watermarkText = info + "\n" + currentTime;
                }
                watermarkView.setText(watermarkText);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


}
