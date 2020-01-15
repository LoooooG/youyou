package com.hotniao.svideo.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.PermissionHelper;
import com.hotniao.svideo.R;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.svideo.utils.ScreenUtils;

import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：上传头像
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnEditHeaderDialog extends AppCompatDialogFragment {

    private static HnEditHeaderDialog sDialog;

    private Activity mActivity;
    private Uri mTempPhotoUri;
    private Uri mCroppedPhotoUri;
    private float mWidth = 160.0f;
    private float mHeight = 240.0f;
    private static final int REQUEST_CODE_CAMERA_PHOTO = 1001;
    private static final int REQUEST_CODE_PICKED_PHOTO = 1002;
    private static final int REQUEST_CODE_CROP_PHOTO = 1003;
    private String TAG = "HnEditHeaderDialog";

    public static HnEditHeaderDialog newInstance() {


        Bundle args = new Bundle();
        sDialog = new HnEditHeaderDialog();
        sDialog.setArguments(args);
        return sDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        mTempPhotoUri = HnPhotoUtils.generateTempImageUri(mActivity);
        mCroppedPhotoUri = HnPhotoUtils.generateTempCroppedImageUri(mActivity);
        if (!PermissionHelper.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            HnToastUtils.showToastShort("请打开存储权限");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.dialog_edit_header, null);

        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(mActivity, R.style.PXDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        params.height = ScreenUtils.dp2px(mActivity, 140.0f);
        alertWindow.setAttributes(params);

        return dialog;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_take_photo, R.id.tv_galley})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_cancel:
                dialogDismiss();
                break;

            case R.id.tv_take_photo:  //拍照

                final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                HnPhotoUtils.addPhotoPickerExtras(intentCapture, mTempPhotoUri);
                startActivityForResult(intentCapture, REQUEST_CODE_CAMERA_PHOTO);
                break;

            case R.id.tv_galley:  //相册

                final Intent intentPick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                HnPhotoUtils.addPhotoPickerExtras(intentPick, mTempPhotoUri);
                if (intentPick.resolveActivity(mActivity.getPackageManager()) != null) {
                    startActivityForResult(intentPick, REQUEST_CODE_PICKED_PHOTO);
                } else {
                    HnToastUtils.showCenterToast("没有找到应用");
                }


                break;
        }
    }

    /**
     * dialog消失
     */
    private void dialogDismiss() {
        if (sDialog != null) {
            sDialog.dismiss();
            sDialog = null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                        Toast.makeText(mActivity, HnUiUtils.getString(R.string.not_find_file), Toast.LENGTH_SHORT).show();
                    }
                    if (getDialog().isShowing()) {
                        getDialog().dismiss();
                    }
                    break;
                }

                case REQUEST_CODE_PICKED_PHOTO:
                case REQUEST_CODE_CAMERA_PHOTO:
                    final Uri uri;
                    boolean isWritable = false;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                        HnLogUtils.d("<<", "uri=" + data.getData());
                    } else {
                        uri = mTempPhotoUri;
                        isWritable = true;
                    }
                    final Uri toCrop;
                    if (isWritable) {
                        toCrop = uri;
                    } else {
                        toCrop = mTempPhotoUri;
                        try {
                            HnPhotoUtils.savePhotoFromUriToUri(mActivity, uri,
                                    toCrop, false);
                        } catch (SecurityException e) {
                            HnLogUtils.d(TAG, "Did not have read-access to uri : " + uri);
                        }
                    }
                    if (requestCode == REQUEST_CODE_CAMERA_PHOTO) {
                        doCropPhoto(toCrop, mCroppedPhotoUri);
                    } else {
                        try {
                            Bitmap bitmap = HnPhotoUtils.getBitmapFromUri(mActivity, toCrop);
                            if (onImageCallBack != null) {
                                onImageCallBack.onImage(bitmap, uri);
                            }
                        } catch (FileNotFoundException e) {
                            Toast.makeText(mActivity, HnUiUtils.getString(R.string.not_find_file), Toast.LENGTH_SHORT).show();
                        }
                        if (getDialog().isShowing()) {
                            getDialog().dismiss();
                        }
                    }

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
            HnPhotoUtils.addCropExtras(intent, ScreenUtils.dp2px(mActivity, mWidth), ScreenUtils.dp2px(mActivity, mHeight));
            startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
        } catch (Exception e) {
            Toast.makeText(mActivity, HnUiUtils.getString(R.string.not_find_img), Toast.LENGTH_SHORT).show();
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
