package com.hotniao.svideo.biz.live.anchorAuth;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.EncryptUtils;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnAuthDetailModel;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.dialog.HnEditHeaderDialog;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.loopj.android.http.RequestParams;

import java.io.File;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理主播实名认证界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/7 10:03
 * 修改人：Administrator
 * 修改时间：2017/9/7 10:03
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorAuthenticationBiz {

    private String TAG = "HnAnchorAuthenticationBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnAnchorAuthenticationBiz(BaseActivity context) {
        this.context = context;
    }

    public void setLoginListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 网络请求：获取用户的主播状态信息
     */
    public void reuqestToAnchorAuthStatusInfo() {
        HnHttpUtils.postRequest(HnUrl.CERTIFICATION_CHECK, null, TAG, new HnResponseHandler<HnAuthDetailModel>(context, HnAuthDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("AnchorAuthStatusInfo", response, model.getD());
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("AnchorAuthStatusInfo", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("AnchorAuthStatusInfo", errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求：主播申请
     *
     * @param userName   用户名
     * @param phone      手机号
     * @param userIdCard 身份证号
     * @param onePath    身份证正面照
     * @param twoPath    身份证反面照
     * @param threePath  手持身份证照
     * @param isCheck    是否同意开播规则
     */
    public void requestToCommitAnchorApply(String userName, String phone, String userIdCard, String onePath, String twoPath, String threePath, boolean isCheck) {
//        if (!HnRegexUtils.isIDCard18(userIdCard) && !HnRegexUtils.isIDCard15(userIdCard)) {
//            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.incorr_idcard));
//            return;
//        }
        if (TextUtils.isEmpty(phone) || !HnRegexUtils.isMobileExact(phone)) {
            if (!HnRegexUtils.isMobileExact(phone)) {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.phone_account_true));
            } else {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.phone_account));
            }
            return;
        }
        RequestParams param = new RequestParams();
        param.put("realname", userName);
        if (!TextUtils.isEmpty(userIdCard)) {
            param.put("number", userIdCard);
        }
        param.put("phone", phone);
        if (!TextUtils.isEmpty(onePath)) param.put("front_img", onePath);
        if (!TextUtils.isEmpty(twoPath)) param.put("back_img", twoPath);
        if (!TextUtils.isEmpty(threePath)) param.put("user_img", threePath);
        if (isCheck) {
            param.put("is_check", "1");
        } else {
            param.put("is_check", "0");
        }

        HnHttpUtils.postRequest(HnUrl.CERTIFICATION_ADD, param, "主播认证", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {

                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("Commit_Anchor_Apply", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("Commit_Anchor_Apply", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("Commit_Anchor_Apply", errCode, msg);
                }
            }
        });

    }


    /**
     * 上传图片文件
     *
     * @param select 标识添加的第几张   身份证正面照/身份证正面照/手持身份证正面照
     */
    public void uploadPicFile(final String select) {
        HnEditHeaderDialog mHeaderDialog = HnEditHeaderDialog.newInstance();
        mHeaderDialog.show(context.getSupportFragmentManager(), "header");
        mHeaderDialog.setOnImageCallBack(new HnEditHeaderDialog.OnImageCallBack() {
            @Override
            public void onImage(Bitmap bitmap, Uri uri) {
                if (bitmap != null) {
                    //对文件名进行MD5加密   YYYYMMDD +md5
                    String fileName = HnDateUtils.getCurrentDate("yyyyMMdd").toUpperCase() + EncryptUtils.encryptMD5ToString(HnUtils.createRandom(false, 5)) + ".png";
                    File picFile = HnPhotoUtils.bitmapToFile(bitmap, fileName);
                    if (picFile.exists()) {
                        requestToGetToken(picFile, select);
                    }
                }
            }
        });

    }


    /**
     * 获取token
     *
     * @param
     * @param file   上传到文件
     * @param select
     */
    public void requestToGetToken(final File file, final String select) {
        if (listener != null) {
            listener.requesting();
        }

        HnUpLoadPhotoControl.getTenSign(file, HnUpLoadPhotoControl.UploadImage, HnUpLoadPhotoControl.ReadPublic);
        HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token, int type) {
                HnFileUtils.deleteFile(file);
                if (listener != null) {
                    listener.requestSuccess("upload_pic_file", key, select);
                }
            }

            @Override
            public void uploadProgress(int progress, int requestId) {

            }

            @Override
            public void uploadError(int code, String msg) {
                if (listener != null) {
                    listener.requestFail("upload_pic_file", code, "图片" + msg);
                }
            }
        });

    }


}
