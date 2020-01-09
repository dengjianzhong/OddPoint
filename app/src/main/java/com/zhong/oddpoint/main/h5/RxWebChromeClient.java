package com.zhong.oddpoint.main.h5;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.PopupWindow;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.zhong.oddpoint.main.R;
import com.zhong.toollib.factory.PopupFactory;

import java.io.File;

public class RxWebChromeClient extends WebChromeClient implements View.OnClickListener {

    private Activity activity;
    private ValueCallback<Uri[]> mUploadMessageForAndroids;
    private ValueCallback<Uri> mUploadMessageForAndroid;
    private final View view;
    private PopupWindow popupWindow;
    private boolean close_tag = false;

    public RxWebChromeClient(Activity activity) {
        this.activity = activity;
        view = LayoutInflater.from(activity).inflate(R.layout.h5_item_file_type, null);
        view.findViewById(R.id.ll_open_image).setOnClickListener(this);
        view.findViewById(R.id.ll_open_camera).setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        request.grant(request.getResources());
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
    }

    //For Android 4.1+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessageForAndroid = uploadMsg;
        close_tag = false;
        String acceptTypes = acceptType;
        if (acceptTypes.contains("*/*") || acceptTypes.contains("file/*")) {
            openFileChoose();
        } else if (acceptTypes.contains("image/*")) {
            popupWindow = PopupFactory.showFileChoose(activity, new PopupFactory.IFormCloseListener() {
                @Override
                public void onClose() {
                    if (!close_tag) {
                        mUploadMessageForAndroids.onReceiveValue(new Uri[]{});
                    }
                }
            }, view, Gravity.BOTTOM);
        } else {
            openFileChoose();
        }
    }

    // For Android 5.0+
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        mUploadMessageForAndroids = filePathCallback;
        close_tag = false;
        String[] acceptTypes = fileChooserParams.getAcceptTypes();
        if (acceptTypes[0].contains("*/*") || acceptTypes[0].contains("file/*")) {
            openFileChoose();
        } else if (acceptTypes[0].contains("image/*")) {
            popupWindow = PopupFactory.showFileChoose(activity, new PopupFactory.IFormCloseListener() {
                @Override
                public void onClose() {
                    if (!close_tag) {
                        mUploadMessageForAndroids.onReceiveValue(new Uri[]{});
                    }
                }
            }, view, Gravity.BOTTOM);
        } else {
            openFileChoose();
        }
        return true;
    }

    // TODO: 打开相册选择页面
    private void openImageChoose() {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    // TODO: 打开文件选择页面
    private void openFileChoose() {
//        FilePickerManager.INSTANCE.from(activity)
//                .maxSelectable(1)
//                .enableSingleChoice()//单选模式
//                .forResult(FilePickerManager.REQUEST_CODE);
    }

    // TODO: 打开相机拍照
    private void openCamera() {
        String mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/temporary.jpg";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DATA, mFilePath);
            Uri mCameraTempUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (mCameraTempUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }
            activity.startActivityForResult(intent, 2020); // 参数常量为自定义的request code, 在取返回结果时有用
        }else {
            // 指定拍照意图
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 加载路径图片路径
            Uri mUri = Uri.fromFile(new File(mFilePath));
            // 指定存储路径，这样就可以保存原图了
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            activity.startActivityForResult(openCameraIntent, 2020);
        }
    }

    public ValueCallback<Uri[]> getUploadFileObjects() {
        return mUploadMessageForAndroids;
    }

    public ValueCallback<Uri> getUploadFileObject() {
        return mUploadMessageForAndroid;
    }

    @Override
    public void onClick(View v) {
        if (popupWindow != null) {
            close_tag = true;
            popupWindow.dismiss();
        }
        if (v.getId() == R.id.ll_open_camera) {
            openCamera();
        } else {
            openImageChoose();
        }
    }
}
