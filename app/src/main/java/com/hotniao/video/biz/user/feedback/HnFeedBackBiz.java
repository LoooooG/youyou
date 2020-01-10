package com.hotniao.video.biz.user.feedback;

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
import com.hn.library.utils.HnUtils;
import com.hotniao.video.R;
import com.hotniao.video.biz.set.HnCacheDealUtils;
import com.hn.library.global.HnUrl;
import com.hotniao.video.dialog.HnEditHeaderDialog;
import com.hotniao.livelibrary.control.HnUpLoadPhotoControl;
import com.loopj.android.http.RequestParams;

import java.io.File;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理意见反馈界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/7 20:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFeedBackBiz {

    private String TAG = "HnFeedBackBiz";
    private BaseActivity context;

    private HnCacheDealUtils mHnCacheDealUtils;

    private BaseRequestStateListener listener;

    public HnFeedBackBiz(BaseActivity context) {
        this.context = context;

    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 添加图片
     *
     * @param
     */
    public void addUploadPic() {
        final HnEditHeaderDialog mHeaderDialog = HnEditHeaderDialog.newInstance();
        mHeaderDialog.show(context.getSupportFragmentManager(), "header");
        mHeaderDialog.setOnImageCallBack(new HnEditHeaderDialog.OnImageCallBack() {
            @Override
            public void onImage(Bitmap bitmap, Uri uri) {
                if (bitmap != null) {
                    //对文件名进行MD5加密
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

        HnUpLoadPhotoControl.getTenSign(file,HnUpLoadPhotoControl.UploadImage,HnUpLoadPhotoControl.ReadPublic);
        HnUpLoadPhotoControl.setUpStutaListener(new HnUpLoadPhotoControl.UpStutaListener() {
            @Override
            public void uploadSuccess(final String key, Object token,int type) {
                HnFileUtils.deleteFile(file);
                if (listener != null) {
                    listener.requestSuccess("upload_file", key, token);
                }
            }

            @Override
            public void uploadProgress(int progress,int requestId) {

            }

            @Override
            public void uploadError(int code, String msg) {
                if (listener != null) {
                    listener.requestFail("upload_file", 1, "图片"+msg);
                }
            }
        });

    }

    /**
     * 上传图片
     * @param file    上传到文件
     * @param token   七牛token
     * @param url     文件地址前缀  服务器返回 用于拼接

     */

    /**
     * 网络请求：提交反馈意见
     *
     * @param feedback 意见数据
     * @param way      联系方式
     * @param key1     第一张图片的key
     * @param key2     第二张图片的key
     * @param key3     第三张图片的key
     */
    public void requestToCommitData(String feedback, String way, String key1, String key2, String key3) {
        if (TextUtils.isEmpty(feedback)) {
            if (listener != null) {
                listener.requestFail("commit_feed_back", 0, context.getResources().getString(R.string.please_input_feedback));
            }
            return;
        }
        if (TextUtils.isEmpty(way)) {
            if (listener != null) {
                listener.requestFail("commit_feed_back", 0, context.getResources().getString(R.string.please_input_way));
            }
            return;
        }

        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("content", feedback);
        param.put("user_link", way);
        param.put("os", "Android");
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(key1)) {
            sb.append(key1);
        }
        if (!TextUtils.isEmpty(key2)) {
            sb.append("," + key2);
        }
        if (!TextUtils.isEmpty(key3)) {
            sb.append("," + key3);
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            param.put("images", sb.toString());
        }
        HnHttpUtils.postRequest(HnUrl.FEED_BACK, param, TAG, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("commit_feed_back", response, model);
                    }

                } else {
                    if (listener != null) {
                        listener.requestFail("commit_feed_back", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("commit_feed_back", errCode, msg);
                }
            }
        });

    }
}
