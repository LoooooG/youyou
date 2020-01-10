package com.hotniao.video.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnNetUtil;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnAuthStateActivity;
import com.hotniao.video.activity.HnBeforeLiveSettingActivity;
import com.hotniao.video.model.HnCanLiveModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.video.utils.HnUserUtil;
import com.hn.library.utils.PermissionHelper;
import com.hotniao.livelibrary.ui.anchor.activity.HnAnchorActivity;
import com.videolibrary.activity.TCVideoRecordActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：直播或者小视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLiveOrVideoDialog extends DialogFragment implements View.OnClickListener {


    private Activity mActivity;
    private static HnLiveOrVideoDialog dialog;

    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    public static HnLiveOrVideoDialog newInstance() {
        dialog = new HnLiveOrVideoDialog();
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_live_or_video, null);
        view.findViewById(R.id.ll_live).setOnClickListener(this);
        view.findViewById(R.id.ll_record).setOnClickListener(this);


        Dialog dialog = new Dialog(mActivity, R.style.PXDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (mActivity.getWindowManager().getDefaultDisplay().getWidth());
//        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        window.setAttributes(params);
        return dialog;
    }


    public HnLiveOrVideoDialog setItemClickListener(OnClickDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private OnClickDialogListener listener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_live:
                if (listener != null) listener.clickLive();
                checkNetWork();
                break;
            case R.id.ll_record:
                if (listener != null) listener.clickRecord();
                if (PermissionHelper.isCameraUseable() && PermissionHelper.isAudioRecordable()) {
                    if (PermissionHelper.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // TODO 执行拥有权限时的下一步。
                        TCVideoRecordActivity.luncher(mActivity);
                    } else {
                        HnToastUtils.showToastShort("请开启存储权限");
                    }
                } else {
                    HnToastUtils.showToastShort("请开启相机或录音权限");
                }
                break;
        }
        dismiss();
    }

    public interface OnClickDialogListener {
        void clickLive();

        void clickRecord();
    }


    private void checkNetWork() {
        int netWorkState = HnNetUtil.getNetWorkState(mActivity);
        if (netWorkState == HnNetUtil.NETWORK_WIFI) {//是否wifi
            checkPermission();
        } else if (netWorkState == HnNetUtil.NETWORK_MOBILE) {//是否流量
            CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {
                    checkPermission();
                }

                @Override
                public void rightClick() {
                    HnNetUtil.openWirelessSettings(mActivity);

                }
            }).setTitle(HnUiUtils.getString(R.string.prompt)).setContent(HnUiUtils.getString(R.string.no_wifi))
                    .setRightText(HnUiUtils.getString(R.string.to_set)).setLeftText(HnUiUtils.getString(R.string.live_continue_play)).show();

        } else if (netWorkState == HnNetUtil.NETWORK_NONE) {//没有网络
            CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {
                }

                @Override
                public void rightClick() {
                    HnNetUtil.openWirelessSettings(mActivity);
                }
            }).setTitle(HnUiUtils.getString(R.string.prompt)).setContent(HnUiUtils.getString(R.string.no_network)).setRightText(HnUiUtils.getString(R.string.to_set)).show();
        }
    }

    /**
     * 检查权限是否打开
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            api23permissionCheck();
        } else {
            boolean cameraCanUse = HnUserUtil.isCameraCanUse();
            if (cameraCanUse) {
                boolean hasPermission = HnUserUtil.isAudioPermission();
                if (hasPermission) {
                    requestCanLive();
                } else {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.main_audio_unable_to_live));
                }
            } else {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.main_camera_unable_to_live));
            }
        }
    }

    private void api23permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (String permission : permissionManifest) {
            if (PermissionChecker.checkSelfPermission(mActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.main_camera_or_audio_unable_to_live));
        } else {
            requestCanLive();
        }
    }

    /**
     * 是否能够直播
     */
    private void requestCanLive() {
        HnHttpUtils.getRequest(HnUrl.LIVE_GET_LIVE_INFO, null, HnUrl.LIVE_GET_LIVE_INFO, new HnResponseHandler<HnCanLiveModel>(HnCanLiveModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0 || model.getD() == null) {
                    if (HnAnchorActivity.mIsLiveing) return;
                    mActivity.startActivity(new Intent(mActivity, HnBeforeLiveSettingActivity.class).putExtra("bean", (Serializable) model.getD()));
                } else {
                    HnAuthStateActivity.luncher(mActivity);
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                //实名认证未通过
                if (HnServiceErrorUtil.USER_CERTIFICATION_FAIL == errCode ||
                        HnServiceErrorUtil.USER_CERTIFICATION_CHECK == errCode ||
                        HnServiceErrorUtil.USER_NOT_CERTIFICATION == errCode) {
                    HnAuthStateActivity.luncher(mActivity);
                } else {
                    HnToastUtils.showToastShort(msg);
                }

            }
        });
    }


}
