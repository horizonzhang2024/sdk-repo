package com.zhanchen.main.ui.process;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.sum.glide.GlideAppKt;
import com.zhanchen.main.R;


public class PreviewActivity extends AppCompatActivity {

    private ImageView previewImageView;
    private VideoView previewVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置窗口全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_preview);

        previewImageView = findViewById(R.id.previewImageView);
        previewVideoView = findViewById(R.id.previewVideoView);

        // 获取传递过来的媒体文件的 URI
        Intent intent = getIntent();
        if (intent != null) {
            int uriType = intent.getIntExtra("uri_type", 0);
            Uri mediaUri = intent.getData();
            if (mediaUri != null) {
                if (uriType == 1) {
                    previewImageView.setVisibility(View.GONE);
                    previewVideoView.setVisibility(View.VISIBLE);
                    previewVideoView.setVideoURI(mediaUri);
                    previewVideoView.start();
                } else {
                    // 使用 Glide 加载和显示图像文件
                    previewImageView.setVisibility(View.VISIBLE);
                    previewVideoView.setVisibility(View.GONE);
                    GlideAppKt.setPreviewImage(previewImageView, mediaUri);
                }
            }
        }

        // 点击全屏，切换全屏或退出全屏
        previewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFullscreen();
            }
        });
    }

    // 切换全屏或退出全屏
    private void toggleFullscreen() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            // 退出全屏
            newUiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            newUiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
            newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else {
            // 进入全屏
            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    public void onBackPressed() {
        // 返回时关闭预览页面
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
