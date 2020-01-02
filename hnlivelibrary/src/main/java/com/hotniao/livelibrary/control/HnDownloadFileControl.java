package com.hotniao.livelibrary.control;


import android.text.TextUtils;

import com.hn.library.HnBaseApplication;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.task.listener.IDownloadTaskListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;


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

public class HnDownloadFileControl {
    private static COSClient cos;
    /**
     * 初始化腾讯上传
     */
    public static void initTenceUpload(String appid, String point) {

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

        cos = new COSClient(HnBaseApplication.getContext(), appid, config, "");

    }


    public static void downloadFile(final String downloadURl, final String savePath, final DownStutaListener mListener) {
        if (cos == null) initTenceUpload("1256015004", "gz");

        GetObjectRequest getObjectRequest = new GetObjectRequest(downloadURl, savePath);
        getObjectRequest.setSign(null);

        getObjectRequest.setListener(new IDownloadTaskListener() {

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                if (mListener != null) mListener.downloadSuccess(downloadURl, savePath);
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                if (mListener != null) mListener.downloadError(cosResult.code, cosResult.msg);
            }

            @Override
            public void onProgress(COSRequest cosRequest, long l, long l1) {

            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                if (mListener != null) mListener.downloadError(1, "下载取消");
            }
        });
        /** 发送请求：执行 */
        cos.getObjectAsyn(getObjectRequest);
    }


    public interface DownStutaListener {
        void downloadSuccess(String url, String path);

        void downloadError(int code, String msg);
    }


    public static void down(final String downloadURl, final String savePath, final DownStutaListener mListener) {
        String mBaseUrl = "", mEndUrl = "";
        if (!TextUtils.isEmpty(downloadURl)) {
            String[] split = downloadURl.split(".com");
            if (split.length > 1) {
                mBaseUrl = split[0] + ".com";
                mEndUrl = split[1];
            }
        }
        if (TextUtils.isEmpty(mBaseUrl)) {
            if (mListener != null) mListener.downloadError(1001, "无效地址");
            return;
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        DownloadApi retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);

        Call<ResponseBody> call = retrofit.retrofitDownload(mEndUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(savePath);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                    if (mListener != null) mListener.downloadSuccess(downloadURl, savePath);
                } catch (IOException e) {
                    if (mListener != null) mListener.downloadError(1001, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mListener != null) mListener.downloadError(1001, t.getMessage());
            }
        });

    }

    public interface DownloadApi {
        @GET
        Call<ResponseBody> retrofitDownload(@Url String url);
    }
}
