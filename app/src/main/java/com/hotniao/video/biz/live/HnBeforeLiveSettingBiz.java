package com.hotniao.video.biz.live;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.picker.photo_picker.HnPhotoUtils;
import com.hn.library.utils.EncryptUtils;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.hotniao.livelibrary.model.HnStartLiveInfoModel;
import com.hn.library.global.HnUrl;
import com.hotniao.video.dialog.HnEditHeaderDialog;
import com.loopj.android.http.RequestParams;

import java.io.File;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理开播之前的设置界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/14 9:05
 * 修改人：Administrator
 * 修改时间：2017/9/14 9:05
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBeforeLiveSettingBiz {

    private String TAG = "HnBeforeLiveSettingBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnBeforeLiveSettingBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 上传图片文件
     */
    public void uploadPicFile() {
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
                        requestToGetToken(picFile);
                    }
                }
            }
        });

    }


    /**
     * 获取token
     *
     * @param
     * @param file 上传到文件
     */
    public void requestToGetToken(final File file) {
        if (listener != null) {
            listener.requesting();
        }

        HnUpLoadPhotoControl.getTenSign(file, HnUpLoadPhotoControl.UploadImage, HnUpLoadPhotoControl.ReadPublic);
        HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token, int type) {
                HnFileUtils.deleteFile(file);
                if (listener != null) {
                    listener.requestSuccess("upload_pic_file", key, "");
                }
            }

            @Override
            public void uploadProgress(int progress,int requestId) {

            }

            @Override
            public void uploadError(int code, String msg) {
                if (listener != null) {
                    listener.requestFail("upload_pic_file", 3, "图片"+msg);
                }
            }
        });

    }

    /**
     * 上传图片
     *
     * @param file  上传到文件
     * @param token 七牛token
     * @param url   文件地址前缀  服务器返回 用于拼接
     */


    /**
     * 网络请求:
     *
     * @param imgUrl      封面地址
     * @param title       标题
     * @param choose_type 直播类型    0：免费，1：VIP，2：门票，3：计时
     * @param money1      计时收费使用
     * @param money1      门票收费使用
     * @param cityAddress 地址
     * @param mLiveTypeId 直播分类id
     */
    public void requestToStartLive(String imgUrl, String title, int choose_type, final String money1, String money2,
                                   String cityAddress, String lat, String lng, String mLiveTypeId) {

        if (TextUtils.isEmpty(mLiveTypeId) || "-1".equals(mLiveTypeId) || "0".equals(mLiveTypeId)) {
            if (listener != null) {
                listener.requestFail("start_live", 0, "请选择直播频道");
                return;
            }
        }

        RequestParams param = new RequestParams();
        param.put("anchor_category_id", mLiveTypeId);

        if (!TextUtils.isEmpty(lat)) param.put("anchor_lat", lat);
        if (!TextUtils.isEmpty(lng)) param.put("anchor_lng", lng);

        if (!TextUtils.isEmpty(title)) {
            param.put("anchor_live_title", title);
        }
        if (!TextUtils.isEmpty(cityAddress)) {
            param.put("anchor_local", cityAddress);
        }
        if (!TextUtils.isEmpty(imgUrl)) {
            param.put("anchor_live_img", imgUrl);
        }
        param.put("anchor_live_pay", choose_type + "");
        if (choose_type == 2) {
            if (TextUtils.isEmpty(money2)) {
                if (listener != null) {
                    listener.requestFail("start_live", 0, "请输入支付金额");
                    return;
                }
            } else {
                Long moneyNumber = Long.valueOf(money2);
                if (moneyNumber <= 0) {
                    if (listener != null) {
                        listener.requestFail("start_live", 0, "支付金额不能小于0");
                        return;
                    }
                }
                param.put("anchor_live_fee", money2);
            }
        }
        if (choose_type == 3) {
            if (TextUtils.isEmpty(money1)) {
                if (listener != null) {
                    listener.requestFail("start_live", 0, "请输入支付金额");
                    return;
                }
            } else {
                Long moneyNumber = Long.valueOf(money1);
                if (moneyNumber <= 0) {
                    if (listener != null) {
                        listener.requestFail("start_live", 0, "支付金额不能小于0");
                        return;
                    }
                }
                param.put("anchor_live_fee", money1);
            }

        }

        if (listener != null) {
            listener.requesting();
        }
        HnHttpUtils.postRequest(HnUrl.PUSH_STARTLIVE, param, TAG, new HnResponseHandler<HnStartLiveInfoModel>(context, HnStartLiveInfoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() != 0) {
                    if (listener != null) {
                        listener.requestFail("start_live", model.getC(), model.getM());
                    }
                } else {
                    if (listener != null) {
                        listener.requestSuccess("start_live", response, model);

                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("start_live", errCode, msg);
                }
            }
        });

    }

}
