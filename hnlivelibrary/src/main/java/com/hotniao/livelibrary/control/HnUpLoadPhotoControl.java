package com.hotniao.livelibrary.control;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.hn.library.HnBaseApplication;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.livelibrary.model.HnUploadPhotoModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：上传图片
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUpLoadPhotoControl {
    private static final String TAG = "HnUpLoadPhotoControl";
    private static COSClient cos;
    private static boolean isCanclerUpload = false;
    public static final int UploadImage = 1;
    public static final int UploadVideo = 2;
    //	public为私有写公有读 ；private为私有读写。默认public
    public static final String ReadPublic = "public";
    public static final String ReadPrivate = "private";

    /**
     * 普通上传
     *
     * @param file
     * @param mListener
     */
    public static void upLoadPhoto(final File file, final UpStutaListener mListener) {
        HnHttpUtils.uploadFile(file.getPath(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                HnFileUtils.deleteFile(file);
                try {
                    String toReturn = (responseBody == null) ? null : new String(responseBody, "UTF-8");
                    if (toReturn != null) {
                        JSONObject js = new JSONObject(toReturn);
                        JSONObject d = js.optJSONObject("d");
                        String url = d.optString("url");

                        if (0 == js.optInt("c")) {
                            if (mListener != null) {
                                mListener.uploadSuccess(url, toReturn, 1);
                            }
                        } else {
                            if (mListener != null) {
                                mListener.uploadError(1, "上传图片失败");
                            }
                        }
                    }
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.uploadError(1, "上传图片失败");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String toReturn = (responseBody == null) ? null : new String(responseBody, "UTF-8");

                    if (mListener != null) {
                        mListener.uploadError(1, "上传图片失败");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                mListener.uploadProgress((int) (bytesWritten * 100 / totalSize), 1);
            }
        });
    }

    /**
     * 普通上传
     *
     * @param file
     * @param mListener
     */
    public static void upLoadVideo(final File file, final UpStutaListener mListener) {
        HnHttpUtils.uploadVideo(file.getPath(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                HnLogUtils.e(Arrays.toString(responseBody));
                try {
                    String toReturn = (responseBody == null) ? null : new String(responseBody, "UTF-8");
                    if (toReturn != null) {
                        JSONObject js = new JSONObject(toReturn);
                        JSONObject d = js.optJSONObject("data");
                        String url = d.optString("url");

                        if (1 == js.optInt("code")) {
                            if (mListener != null) {
                                mListener.uploadSuccess(url, toReturn, 1);
                            }
                        } else {
                            if (mListener != null) {
                                mListener.uploadError(1, "上传视频失败：" + js.optString("msg"));
                            }
                        }
                    }
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.uploadError(1, "上传视频失败1");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String toReturn = (responseBody == null) ? null : new String(responseBody, "UTF-8");

                    if (mListener != null) {
                        mListener.uploadError(1, "上传视频失败2");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                mListener.uploadProgress((int) (bytesWritten * 100 / totalSize), 1);
            }
        });
    }

    /**
     * 初始化腾讯上传
     */
    public static void initTenceUpload(String appid, String point) {

        String peristenceId = null;
        //创建COSClientConfig对象，根据需要修改默认的配置参数
        COSClientConfig config = new COSClientConfig();
        //如设置园区
        if ("gz".equals(point)) {
            config.setEndPoint(COSEndPoint.COS_GZ);
        } else if ("tj".equals(point)) {
            config.setEndPoint(COSEndPoint.COS_TJ);
        } else if ("sh".equals(point)) {
            config.setEndPoint(COSEndPoint.COS_SH);
        } else if ("sgp".equals(point)) {
            config.setEndPoint(COSEndPoint.COS_SGP);
        } else {
            config.setEndPoint(COSEndPoint.COS_GZ);
        }

        cos = new COSClient(HnBaseApplication.getContext(), appid, config, peristenceId);

    }

    /**
     * 腾讯上传签名
     *
     * @param file
     */
    public static void getTenSign(final File file, final int type, String readType) {

        RequestParam param = new RequestParam();
        param.put("type", readType);
        param.put("action", "createSignature");//?action=createSignature
        HnHttpUtils.getRequest(HnUrl.FILE_UPLOAD_API_TEN, param, "FILE_UPLOAD_API_TEN", new HnResponseHandler<HnUploadPhotoModel>(null, HnUploadPhotoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getD() != null && model.getD().getConfig() != null)
                    HnPrefUtils.setString(HnConstants.IMAGE_CDN_PATH, model.getD().getConfig().getImage_cdn());
                if (-1 == type) {
                    return;
                }
                if (file == null) {
                    if (mListener != null) {
                        mListener.uploadError(1, "上传失败");
                    }
                    return;
                }
                if (model.getC() == 0) {
                    HnLogUtils.d(TAG,"file:"+file.getAbsolutePath()+",model:"+model.getD().getConfig().getImage_cdn()+",model:"+model.getD().getConfig());
                    upLoadPhotoTen(file, model.getD().getConfig(), type);
                } else {
                    if (mListener != null) {
                        mListener.uploadError(1, "上传失败");
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mListener != null) {
                    mListener.uploadError(1, "上传失败");
                }
            }
        });

    }


    /**
     * 腾讯上传
     *
     * @param file
     */
    public static void upLoadPhotoTen(final File file, final HnUploadPhotoModel.DBean.ConfigBean bean, final int type) {

        if (bean == null) {
            if (mListener != null) {
                mListener.uploadError(1, "上传失败");
            }
            return;
        }
        if (TextUtils.isEmpty(bean.getAppId())) {
            if (mListener != null) {
                mListener.uploadError(1, "上传失败(-1)");
            }
            return;
        }
        if (cos == null) initTenceUpload(bean.getAppId(), bean.getRegion());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                upload(bean, file, type);
            }
        });


    }

    public static void upload(final HnUploadPhotoModel.DBean.ConfigBean bean, final File file, final int type) {
        final String path = file.getAbsolutePath();
        String cosPath = null;
        if (UploadImage == type)
            cosPath = "/image/" + HnDateUtils.today() + "/" + System.currentTimeMillis() + "." + path.substring(path.lastIndexOf(".") + 1);
        else if (UploadVideo == type)
            cosPath = "/video/" + HnDateUtils.today() + "/" + System.currentTimeMillis() + "." + path.substring(path.lastIndexOf(".") + 1);


        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(bean.getBucket());
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(path);
        putObjectRequest.setSign(bean.getSign());
        int requestId = putObjectRequest.getRequestId();
        final String finalCosPath = cosPath;
        if (mListener != null) {
            mListener.uploadProgress((int) 0, requestId);
        }
        putObjectRequest.setListener(new IUploadTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                isCanclerUpload = false;
//                if (UploadImage == type)
//                    HnFileUtils.deleteFile(file);
                final PutObjectResult result = (PutObjectResult) cosResult;
                if (result != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                if (UploadVideo == type) {
                                    mListener.uploadSuccess(result.access_url, bean, type);
                                } else {
                                    String s = "";
                                    if (path.toLowerCase().endsWith("png") || path.toLowerCase().endsWith("jpg")) {
                                        s = bean.getImage_cdn() + finalCosPath;
                                    }
                                    mListener.uploadSuccess(s, bean, type);
                                }

                            }
                        }
                    });


                }
            }

            @Override
            public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                if (isCanclerUpload) {
                    isCanclerUpload = false;
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.uploadError(1, "上传失败");
                        }
                    }
                });
            }

            @Override
            public void onProgress(final COSRequest cosRequest, final long currentSize, final long totalSize) {
                float progress = (float) currentSize / totalSize;
                progress = progress * 100;
                final float finalProgress = progress;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.uploadProgress((int) finalProgress, cosRequest.getRequestId());
                        }
                    }
                });
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                if (isCanclerUpload) {
                    isCanclerUpload = false;
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.uploadError(1, "上传取消");
                        }
                    }
                });

            }
        });
        /** 发送请求：执行 */
        cos.putObjectAsyn(putObjectRequest);
    }

    public static void canclerUpload(int requestId) {
        if (cos != null) {
            isCanclerUpload = true;
            cos.cancelTask(requestId);
        }

    }

    private static UpStutaListener mListener;

    public static void setUpStutaListener(UpStutaListener listener) {
        mListener = listener;
    }

    public interface UpStutaListener {
        void uploadSuccess(String key, Object token, int type);

        void uploadProgress(int progress, int requestId);

        void uploadError(int code, String msg);
    }
}
