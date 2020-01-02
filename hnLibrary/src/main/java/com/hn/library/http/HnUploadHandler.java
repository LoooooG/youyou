package com.hn.library.http;

import com.google.gson.JsonParseException;
import com.hn.library.global.NetConstant;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.Header;

/**
 * 文件上传回调
 */
public abstract class HnUploadHandler<T extends UploadFileResponseModel> extends TextHttpResponseHandler {

    public  Class<T>                           cls;
    private WeakReference<OnHttpStateCallback> httpStateCallBack;
    public  T                                  model;

    protected HnUploadHandler(Class<T> cls) {
        this.cls = cls;
    }

    protected HnUploadHandler(Class<T> cls, OnHttpStateCallback httpStateCallBack) {
        this.cls = cls;
        this.httpStateCallBack = new WeakReference<OnHttpStateCallback>(httpStateCallBack);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (httpStateCallBack != null && httpStateCallBack.get() != null) {
            httpStateCallBack.get().requestStart();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (httpStateCallBack != null && httpStateCallBack.get() != null) {
            httpStateCallBack.get().requestFinish();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

        if (throwable instanceof UnknownHostException) {
            hnErr(NetConstant.RESPONSE_CODE_ERR, "无法连接服务器");
            return;
        }
        hnErr(NetConstant.RESPONSE_CODE_ERR, "上传失败:onFailure(" + statusCode + ")");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {

        HnLogUtils.json("<-- 请求成功 解析前数据-->", response);
        UploadFileResponseModel baseModel = null;
        try {
            baseModel = HnUtils.gson.fromJson(response, UploadFileResponseModel.class);
        } catch (JsonParseException e) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "获取数据异常(JsonParseException)");
            return;
        }

        if (baseModel == null || baseModel.getState() == null || baseModel.getState().equals("")) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "获取数据异常(some field is null)");
            return;
        }

        if (!baseModel.getState().equals("SUCCESS")) {
            hnErr(NetConstant.REQUEST_CODE_FAILURE, "上传失败");
            return;
        }

        try {
            model = HnUtils.gson.fromJson(response, cls);
        } catch (JsonParseException e) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "获取数据异常(JsonParseException)");
            return;
        }

        if (model == null) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "获取数据异常(some field is null)");
            return;
        }
        hnSuccess(response);
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        hnProgress(bytesWritten, totalSize);
    }

    /**
     * 错误处理
     */
    public abstract void hnErr(int errCode, String msg);

    public abstract void hnSuccess(String response);

    public abstract void hnProgress(long bytesWritten, long totalSize);
}
