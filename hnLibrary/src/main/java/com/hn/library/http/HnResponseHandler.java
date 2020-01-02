package com.hn.library.http;

import com.google.gson.JsonParseException;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.NetConstant;
import com.hn.library.utils.HnConstUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.Header;

import static com.hn.library.global.NetConstant.REQUEST_CODE_FAILURE;
import static com.hn.library.global.NetConstant.REQUEST_CODE_LOGIN_ERR;

/**
 * 请求回调
 */
public abstract class HnResponseHandler<T extends BaseResponseModel> extends TextHttpResponseHandler {

    public    Class<T>                           cls;
    private   WeakReference<OnHttpStateCallback> httpStateCallBack;
    public    T                                  model;
    protected OnRequestErrCallBack               errCallBack;

    protected HnResponseHandler(Class<T> cls) {
        this.cls = cls;
    }

    protected HnResponseHandler(OnRequestErrCallBack callBack, Class<T> cls, OnHttpStateCallback httpStateCallBack) {
        this.cls = cls;
        this.errCallBack = callBack;
        if (httpStateCallBack != null) {
            this.httpStateCallBack = new WeakReference<OnHttpStateCallback>(httpStateCallBack);
        }
    }

    protected HnResponseHandler(OnRequestErrCallBack callBack, Class<T> cls) {
        this.cls = cls;
        this.errCallBack = callBack;
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
            hnErr(NetConstant.RESPONSE_CODE_ERR, "网络请求失败，请稍后重试");
            return;
        }
        hnErr(NetConstant.RESPONSE_CODE_ERR, "网络请求失败，请稍后重试");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {
        HnLogUtils.json("<--请求成功 解析前数据-->", response);

        BaseResponseModel baseModel = null;
        try {
            baseModel = HnUtils.gson.fromJson(response, BaseResponseModel.class);
        } catch (JsonParseException e) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "服务器数据异常");
            return;
        }
        if (baseModel == null || String.valueOf(baseModel.getC()).equals("")) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "服务器数据异常");
            return;
        }

        int responseCode = baseModel.getC();

        //需要登录
        if (responseCode==REQUEST_CODE_LOGIN_ERR) {
            if (errCallBack != null) {
                HnLogUtils.i("errCallBack", "没有登录！");
                errCallBack.loginErr(REQUEST_CODE_LOGIN_ERR, baseModel.getM());
            }
            return;
        }

        if (responseCode ==REQUEST_CODE_FAILURE) {//业务请求失败
            hnErr(REQUEST_CODE_FAILURE, baseModel.getM());
            return;
        }else {

        }

        try {
            model = HnUtils.gson.fromJson(response, cls);
        } catch (JsonParseException e) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "服务器数据异常");
            return;
        }

        if (model == null || String.valueOf(baseModel.getC()).equals("")) {
            hnErr(NetConstant.RESPONSE_CODE_BAD, "服务器数据异常");
            return;
        }
        if(HnServiceErrorUtil.ACCESS_TOKEN_INVALID==model.getC()){
            EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.LoginFailure, model));
        }
        if(model.getC()!=0){
            hnErr(model.getC(), model.getM());
            return;
        }

        hnSuccess(response);
    }


    public abstract void hnSuccess(String response);

    public abstract void hnErr(int errCode, String msg);
}
