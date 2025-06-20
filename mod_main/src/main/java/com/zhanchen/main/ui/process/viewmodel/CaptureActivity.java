//package com.sum.main.ui.process.viewmodel;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.hardware.Camera;
//import android.media.CamcorderProfile;
//import android.media.CameraProfile;
//import android.media.MediaRecorder;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.SystemClock;
//import android.provider.DocumentsContract;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.util.Size;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.Chronometer;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.zhanchen.main.R;
//import com.zhanchen.main.ui.process.adapter.MyAdapter;
//import com.zhanchen.main.ui.process.entity.KeyValueBean;
//import com.zhanchen.main.ui.process.entity.ProcessBean;
//import com.zhanchen.main.utils.CameraInfoUtils;
//import com.zhanchen.main.utils.PreviewUtils;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback {
//
//    private static final int REQUEST_CAMERA_PERMISSION = 200;
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 201;
//
//    private Camera camera;
//    private SurfaceView surfaceView;
//    private SurfaceHolder surfaceHolder;
//    private MediaRecorder mediaRecorder;
//    private boolean isRecording = false;
//    private boolean isPaused = false;
//    private Button captureButton;
//    private Button recordButton;
//    private Button pauseButton;
//    private ImageView previewImg;
//    public static ProcessBean processBean;
//    // keys = {"照片大小", "录像周期", "视频画质", "存储位置", "存储空间不足", "预览大小", "红外激光定位"}
//    private Map<String, String> currentParamsMap = new HashMap<>();
//    private int currentVideoQuality = CameraProfile.QUALITY_HIGH;
//    private String currentStrorage = "内置存储";
//    private String currentStrorageDelete = "手动删除";
//    private File currentMediaFile;
//    private int maxDurationInSeconds = 240 * 60 * 1000; //ms
//    private Chronometer chronometer;
//    private long pauseOffset = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
////        XUI.initTheme(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_capture);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//
//        captureButton = findViewById(R.id.captureButton);
//        recordButton = findViewById(R.id.recordButton);
//        pauseButton = findViewById(R.id.pauseButton);
//        previewImg = findViewById(R.id.previewImg);
//        chronometer = findViewById(R.id.chronometer);
//
//        surfaceView = findViewById(R.id.surfaceView);
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
//
//        captureButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkCameraPermission()) {
//                    captureImage();
//                }
//            }
//        });
//
//        previewImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openMediaFileDir();
//            }
//        });
//
//        recordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkAudioPermission()) {
//                    if (isRecording) {
//                        recordButton.setText("开始");
//                        stopRecording();
//                        stopChronometer();
//                    } else {
//                        recordButton.setText("停止");
//                        startRecording();
//                        startChronometer();
//                    }
//                }
//            }
//        });
//
//
//        pauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pauseRecording();
//            }
//        });
//    }
//
//
//    private void openMediaFileDir() {
//        if (currentMediaFile == null) {
//            return;
//        }
//        String dir;
//        if (!currentMediaFile.exists()) {
//            dir = getOutputMediaFile("IMG", "JPEG").getParent();
//        } else {
//            dir = currentMediaFile.getParent();
//        }
//        Uri uri = Uri.parse(dir);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
//        // Check if there's a file manager app available to handle the intent
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//
//    private boolean checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//            return false;
//        }
//        return true;
//    }
//
//    private boolean checkAudioPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
//            return false;
//        }
//        return true;
//    }
//
//    private void captureImage() {
//        if (camera != null) {
//            camera.takePicture(null, null, new Camera.PictureCallback() {
//                @Override
//                public void onPictureTaken(byte[] data, Camera camera) {
//                    // Save the image
//                    File pictureFile = getOutputMediaFile("IMG_", ".jpg");
//                    if (pictureFile != null) {
//                        try {
//                            FileOutputStream fos = new FileOutputStream(pictureFile);
//                            fos.write(data);
//                            fos.close();
//                            Toast.makeText(CaptureActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
//                            previewImg.setImageBitmap(PreviewUtils.generateImagePreview(currentMediaFile.getAbsolutePath()));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    // Restart the preview
//                    camera.startPreview();
//                }
//            });
//        }
//    }
//
//    private void startRecording() {
//        if (prepareVideoRecorder()) {
//            mediaRecorder.start();
//            isRecording = true;
//            Toast.makeText(this, "录像开始...", Toast.LENGTH_SHORT).show();
//        } else {
//            releaseMediaRecorder();
//        }
//    }
//
//    private void pauseRecording() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (isRecording && !isPaused) {
//                pauseChronometer();
//                mediaRecorder.pause();
//                Toast.makeText(this, "录像暂停...", Toast.LENGTH_SHORT).show();
//                isPaused = true;
//                pauseButton.setText("继续");
//            } else if (isRecording && isPaused) {
//                resumeChronometer();
//                mediaRecorder.resume();
//                Toast.makeText(this, "录像继续...", Toast.LENGTH_SHORT).show();
//                isPaused = false;
//                pauseButton.setText("暂停");
//            }
//        }
//    }
//
//
//    private void stopRecording() {
//        if (isRecording) {
//            mediaRecorder.stop();
//            releaseMediaRecorder();
//            isRecording = false;
//            Toast.makeText(this, "录像结束...", Toast.LENGTH_SHORT).show();
//            previewImg.setImageBitmap(PreviewUtils.generateVideoPreview(currentMediaFile.getAbsolutePath()));
//        }
//    }
//
//    private boolean prepareVideoRecorder() {
//        camera.unlock();
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setCamera(camera);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
//        mediaRecorder.setVideoFrameRate(30);
////        mediaRecorder.setVideoEncodingBitRate();
//
//        mediaRecorder.setOutputFile(getOutputMediaFile("VID_", ".mp4").getPath().toString());
//
//        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
////        mediaRecorder.setOrientationHint(0);
//
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//            Log.e("CameraActivity", "Failed to prepare MediaRecorder: " + e.getMessage());
//            releaseMediaRecorder();
//            return false;
//        }
//
//        return true;
//    }
//
//
//    private void autoFocus() {
//        camera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                if (success) {
//                    // 对焦成功，执行拍照操作或其他操作
//                } else {
//                    // 对焦失败，可以尝试再次调用自动对焦方法
//                    autoFocus();
//                }
//            }
//        });
//    }
//
//    private void releaseMediaRecorder() {
//        if (mediaRecorder != null) {
//            mediaRecorder.reset();
//            mediaRecorder.release();
//            mediaRecorder = null;
//            camera.lock();
//        }
//    }
//
//    private File getOutputMediaFile(String prefix, String extension) {
//
//        // 获取当前日期和时间
//        Calendar calendar = Calendar.getInstance();
//        String yearMonth = new SimpleDateFormat("yyyyMM", Locale.getDefault()).format(calendar.getTime());
//        String monthDay = new SimpleDateFormat("MMdd", Locale.getDefault()).format(calendar.getTime());
//        String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
//        String namePrefix = "";
//        if (processBean != null) {
//            namePrefix = processBean.getName();
//        }
//
//        File mediaStorageDir;
//        File[] externalFilesDirs = getExternalFilesDirs(null);
//        if (externalFilesDirs.length > 1 && externalFilesDirs[1] != null) {
//            File externalSdCard = externalFilesDirs[1];
//            String sdCardPath = externalSdCard.getAbsolutePath();
//            // 外置 SD 卡的路径
//            mediaStorageDir = new File(new File(sdCardPath), "RecordVideo" + File.separator + yearMonth + File.separator + monthDay + File.separator + namePrefix);
//        } else {
//            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "RecordVideo" + File.separator + yearMonth + File.separator + monthDay + File.separator + namePrefix);
//        }
//
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.e("CameraActivity", "Failed to create directory");
//                return null;
//            }
//        }
////        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        StringBuffer sb = new StringBuffer();
//        if (processBean != null) {
//            sb.append(processBean.getName()).append("_").append(processBean.getEmpNo()).append("_").append(processBean.getCarType()).append("_").append(processBean.getCarNo()).append("_").append(processBean.getWorkRole()).append("_").append(processBean.getWorkProject()).append("_").append(processBean.getWorkTask()).append("_");
//        }
//        String workerPrefix = sb.toString();
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + prefix + workerPrefix + timeStamp + extension);
//        currentMediaFile = mediaFile;
//        return mediaFile;
//    }
//
//    @Override
//    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        try {
//            camera = Camera.open();
//            camera.setPreviewDisplay(holder);
//            camera.startPreview();
//            // 在预览Surface创建时触发自动对焦
//            autoFocus();
//        } catch (IOException e) {
//            Log.e("CameraActivity", "Failed to start camera preview: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//        if (surfaceHolder.getSurface() == null) {
//            return;
//        }
//
//        try {
//            camera.stopPreview();
//        } catch (Exception e) {
//            Log.e("CameraActivity", "Failed to stop camera preview: " + e.getMessage());
//        }
//
//        try {
//            camera.setDisplayOrientation(0);
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
//            // 在预览Surface变化时触发自动对焦
//            autoFocus();
//        } catch (Exception e) {
//            Log.e("CameraActivity", "Failed to start camera preview: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//        releaseCamera();
//    }
//
//    private void releaseCamera() {
//        if (camera != null) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                captureImage();
//            } else {
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (isRecording) {
//                    stopRecording();
//                } else {
//                    startRecording();
//                }
//            } else {
//                Toast.makeText(this, "Record audio permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_camera, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.aspect_ratio) {
//            showCameraOptionsPop();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void showCameraOptionsPop() {
//        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_items, null);
//        // 获取屏幕宽度
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenWidth = displayMetrics.widthPixels;
//
//        // 计算 PopupWindow 的宽度
//        int popupWidth = (int) (screenWidth * 0.7);
//        final PopupWindow popupWindow = new PopupWindow(contentView);
//
//        // 设置PopupWindow的属性
//        popupWindow.setWidth(popupWidth);
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(false);
////        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_background,null));
//
//        initCurrentParamsMap(currentParamsMap);
//        // 设置ListView的适配器
//        List<KeyValueBean> data = new ArrayList<>();
//        for (Map.Entry<String, String> entry : currentParamsMap.entrySet()) {
//            KeyValueBean bean = new KeyValueBean(entry.getKey(), entry.getValue());
//            data.add(bean);
//        }
//        final MyAdapter adapter = new MyAdapter(this, data);
//        ListView listView = contentView.findViewById(R.id.lv_items);
//        listView.setAdapter(adapter);
//
//        // 设置每个item的点击事件,并在text1上显示key, text2上显示value
//        // position 0 照片分辨率 1录像周期 2 视频画质  3 存储位置 4 存储空间不足  5 预览大小 6 红外激光定位
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                KeyValueBean bean = adapter.getItem(position);
//                String selectedLabel = ((TextView) view.findViewById(R.id.tv_key)).getText().toString();
//                TextView tvValue = ((TextView) view.findViewById(R.id.tv_value));
//                switch (selectedLabel) {
//                    case "照片大小":
//                        List<String> pictureSizes = new ArrayList<>();
//                        for (Camera.Size size : CameraInfoUtils.getSupportedPictureSizes(camera)) {
//                            pictureSizes.add(size.width + " x " + size.height);
//                        }
//                        showDialogWithList("请选择" + selectedLabel, pictureSizes, selectedText -> {
//                            String[] array = selectedText.split("x");
//                            camera.getParameters().setPictureSize(Integer.valueOf(array[0].trim()), Integer.valueOf(array[1].trim()));
//                            Toast.makeText(CaptureActivity.this, selectedText, Toast.LENGTH_SHORT).show();
//                            tvValue.setText(selectedText);
//                        });
//                        break;
//                    case "录像周期":
//                        showDialogWithList("请选择" + selectedLabel, new ArrayList<>(Arrays.asList("60分钟", "120分钟", "240分钟")), selectedText -> {
//                            String minutes = selectedText.substring(0, selectedText.indexOf("分钟"));
//                            maxDurationInSeconds = Integer.valueOf(minutes) * 60;
//                            Toast.makeText(CaptureActivity.this, selectedText, Toast.LENGTH_SHORT).show();
//                            tvValue.setText(selectedText);
//                        });
//                        break;
//                    case "视频画质":
//                        showDialogWithList("请选择" + selectedLabel, new ArrayList<>(Arrays.asList("1080P", "720P", "480P", "Low")), selectedText -> {
//                            Toast.makeText(CaptureActivity.this, selectedText, Toast.LENGTH_SHORT).show();
//                            tvValue.setText(selectedText);
//                        });
//                        break;
//                    case "存储位置":
//                        showDialogWithList("请选择" + selectedLabel, new ArrayList<>(Arrays.asList("内置存储", "外置SD卡")), selectedText -> {
//                            Toast.makeText(CaptureActivity.this, selectedText, Toast.LENGTH_SHORT).show();
//                            tvValue.setText(selectedText);
//                        });
//                        break;
//                    case "存储空间不足":
//                        showDialogWithList("请选择" + selectedLabel, new ArrayList<>(Arrays.asList("手动删除", "自动删除")), selectedText -> {
//                            Toast.makeText(CaptureActivity.this, selectedText, Toast.LENGTH_SHORT).show();
//                            tvValue.setText(selectedText);
//                        });
//                        break;
//                    case "预览大小":
//                        showDialogWithList("请选择" + selectedLabel, new ArrayList<>(Arrays.asList("16:9", "16:10", "4:3", "1:1")), selectedText -> {
//                            Toast.makeText(CaptureActivity.this, selectedText, Toast.LENGTH_SHORT).show();
//                            tvValue.setText(selectedText);
//                        });
//                        break;
//                    case "红外激光定位":
//                        showDialogWithList("请选择" + selectedLabel, new ArrayList<>(Arrays.asList("开", "关")), selectedText -> {
//                            Toast.makeText(CaptureActivity.this, camera.getParameters().toString(), Toast.LENGTH_LONG).show();
//                            tvValue.setText(selectedText);
//
//                        });
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//        });
//
//        // 设置显示位置
//        int[] location = new int[2];
//        // 相对于整个界面的坐标
//        contentView.getLocationOnScreen(location);
//        int x = location[0] + contentView.getWidth() / 2;
//        int y = location[1] + contentView.getHeight() / 2;
//
//        // 显示PopupWindow
//        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//    }
//
//    private void initCurrentParamsMap(Map<String, String> currentParamsMap) {
//        // keys = {"照片大小", "录像周期", "视频画质", "存储位置", "存储空间不足", "预览大小", "红外激光定位"}
//        currentParamsMap.clear();
//        Size size = CameraInfoUtils.getPhotoResolution(camera);
//        currentParamsMap.put("照片大小", size.getHeight() + " x " + size.getWidth());
//        long duration = CameraInfoUtils.getMaxVideoDuration(camera, currentVideoQuality);
//        currentParamsMap.put("录像周期", "240分钟");
//        Size size1 = CameraInfoUtils.getVideoResolution(camera);
//        currentParamsMap.put("视频画质", size1.getHeight() + " x " + size1.getWidth());
//        currentParamsMap.put("存储位置", currentStrorage);
//        currentParamsMap.put("存储空间不足", currentStrorageDelete);
//        Size size2 = CameraInfoUtils.getPreviewAspectRatio(camera);
//        currentParamsMap.put("预览大小", size2.getWidth() + " : " + size2.getHeight());
//        currentParamsMap.put("红外激光定位", "开");
//    }
//
//    /**
//     * 带单选项的Dialog
//     */
//
//    private void showDialogWithList(String title, List<String> options, RadioButtonClickListener listener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
//        builder.setView(dialogView);
//
//        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
//        dialogTitle.setText(title);
//        RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);
//        AlertDialog dialog = builder.create();
//        // 动态添加 RadioButton
//        for (int i = 0; i < options.size(); i++) {
//            RadioButton radioButton = new RadioButton(this);
//            radioButton.setText(options.get(i));
//            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            // 设置高度为 35dp
//            int heightInDp = 40;
//            float scale = getResources().getDisplayMetrics().density;
//            int heightInPixels = (int) (heightInDp * scale + 0.5f);
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightInPixels);
//            radioButton.setLayoutParams(layoutParams);
//            radioButton.setId(i);
//            radioButton.setChecked(false); // 设置为未选中状态
//            radioGroup.addView(radioButton);
//            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        // 当RadioButton被选中时的逻辑
//                        String selectedText = radioButton.getText().toString();
//                        // 执行回调方法，将选中的文本传递回去
//                        listener.onRadioButtonClicked(selectedText);
//                        dialog.dismiss();
//
//                    }
//                }
//            });
//        }
//        dialog.show();
//
//        // 设置dialog宽度为屏幕的60%
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        Window window = dialog.getWindow();
//        if (window != null) {
//            layoutParams.copyFrom(window.getAttributes());
//            int screenWidth = getResources().getDisplayMetrics().widthPixels;
//            int dialogWidth = (int) (screenWidth * 0.6); // 设置为屏幕宽度的60%
//            layoutParams.width = dialogWidth;
//            window.setAttributes(layoutParams);
//        }
//    }
//
//
//    public interface RadioButtonClickListener {
//        void onRadioButtonClicked(String selectedText);
//    }
//
//    private void startChronometer() {
//        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
//        chronometer.start();
//        isRecording = true;
//    }
//
//    private void stopChronometer() {
//        chronometer.stop();
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        pauseOffset = 0;
//        isRecording = false;
//        isPaused = false;
//    }
//
//    private void pauseChronometer() {
//        chronometer.stop();
//        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
//        isPaused = true;
//    }
//
//    private void resumeChronometer() {
//        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
//        chronometer.start();
//        isPaused = false;
//    }
//}
