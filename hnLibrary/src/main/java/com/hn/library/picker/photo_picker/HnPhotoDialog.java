package com.hn.library.picker.photo_picker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hn.library.R;
import com.hn.library.utils.HnDimenUtil;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;

import java.io.FileNotFoundException;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：框架库
 * 类描述： 图片选择器对话框
 * 创建人：Kevin
 * 创建时间：2016/5/27 9:36
 * 修改人：Kevin
 * 修改时间：2016/5/27 9:36
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPhotoDialog extends DialogFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_CAMERA_PHOTO = 1001;
    private static final int REQUEST_CODE_PICKED_PHOTO = 1002;
    private static final int REQUEST_CODE_CROP_PHOTO   = 1003;

    private Uri mTempPhotoUri;
    private Uri mCroppedPhotoUri;

    private Activity mActivity;
    private float mWidth  = 160.0f;
    private float mHeight = 160.0f;

    public static HnPhotoDialog newInstance() {
        Bundle args = new Bundle();
        HnPhotoDialog fragment = new HnPhotoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mTempPhotoUri = HnPhotoUtils.generateTempImageUri(mActivity);
        mCroppedPhotoUri = HnPhotoUtils.generateTempCroppedImageUri(mActivity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.photo_dialog_layout, null);
        view.findViewById(R.id.bt_capture_photo).setOnClickListener(this);
        view.findViewById(R.id.bt_gallery_photo).setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogAlert);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        Window alertWindow = alertDialog.getWindow();
        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.80);
        alertWindow.setAttributes(params);
        return alertDialog;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        //拍照
        if (id == R.id.bt_capture_photo) {
            final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            HnPhotoUtils.addPhotoPickerExtras(intentCapture, mTempPhotoUri);
            startActivityForResult(intentCapture, REQUEST_CODE_CAMERA_PHOTO);
            //相册
        } else if (id == R.id.bt_gallery_photo) {
            final Intent intentPick = new Intent(Intent.ACTION_GET_CONTENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            HnPhotoUtils.addPhotoPickerExtras(intentPick, mTempPhotoUri);
            if(intentPick.resolveActivity(mActivity.getPackageManager())!=null){
                startActivityForResult(intentPick, REQUEST_CODE_PICKED_PHOTO);
            }else {
                HnToastUtils.showCenterToast("找不到应用");
            }
        }
    }

    /**
     * 设置需要采集的图片的宽度和高度
     *
     * @param width
     * @param height
     */
    public void setPhotoSize(float width, float height) {
        mWidth = width;
        mHeight = height;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                // Cropped photo was returned
                case REQUEST_CODE_CROP_PHOTO: {

                    if (getDialog().isShowing()) {
                        getDialog().dismiss();
                    }
                    final Uri uri;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    } else {
                        uri = mCroppedPhotoUri;
                    }
                    try {
                        // delete the original temporary photo if it exists
                        mActivity.getContentResolver().delete(mTempPhotoUri, null, null);
                        Bitmap bitmap = HnPhotoUtils.getBitmapFromUri(mActivity, uri);
                        if (onImageCallBack != null) {
                            onImageCallBack.onImage(bitmap, uri);
                        }

                    } catch (FileNotFoundException e) {
                        Toast.makeText(mActivity, "文件没有找到", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                // Photo was successfully taken or selected from gallery, now crop it.
                case REQUEST_CODE_PICKED_PHOTO:
                case REQUEST_CODE_CAMERA_PHOTO:
                    final Uri uri;
                    boolean isWritable = false;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                        HnLogUtils.i("<<", "uri=" + data.getData());
                    } else {
                        uri = mTempPhotoUri;
                        isWritable = true;
                    }
                    final Uri toCrop;
                    if (isWritable) {
                        // Since this uri belongs to our file provider, we know that it is writable by us.
                        //This means that we don't have to save it into another temporary location
                        //just to be able to crop it.
                        toCrop = uri;
                    } else {
                        toCrop = mTempPhotoUri;
                        try {
                            HnPhotoUtils.savePhotoFromUriToUri(mActivity, uri,
                                    toCrop, false);
                        } catch (SecurityException e) {
                            HnLogUtils.e("Did not have read-access to uri : " + uri);
                        }
                    }

                    doCropPhoto(toCrop, mCroppedPhotoUri);
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (getDialog().isShowing()) {
                getDialog().dismiss();
            }
        }
    }

    /**
     * 调用系统裁剪
     */
    private void doCropPhoto(Uri inputUri, Uri outputUri) {
        try {
            final Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(inputUri, "image/*");
            HnPhotoUtils.addPhotoPickerExtras(intent, outputUri);
            HnPhotoUtils.addCropExtras(intent, HnDimenUtil.dp2px(mActivity, mWidth), HnDimenUtil.dp2px(mActivity, mHeight));
            startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
        } catch (Exception e) {
            Toast.makeText(mActivity, "图片未找到", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 对外提供图片设置接口
     */
    public interface OnImageCallBack {
        void onImage(Bitmap bitmap, Uri uri);
    }

    private OnImageCallBack onImageCallBack;

    public void setOnImageCallBack(OnImageCallBack onImageCallBack) {
        this.onImageCallBack = onImageCallBack;
    }
}
