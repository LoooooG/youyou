package com.hotniao.live.biz.forgetPwd;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnRegexUtils;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理忘记密码界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/4 18:06
 * 修改人：Administrator
 * 修改时间：2017/9/4 18:06
 * 修改备注：
 * Version:  1.0.0
 */
public class HnForgetPwdBiz {

    private String TAG = "HnForgetPwdBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;


    public HnForgetPwdBiz(BaseActivity context) {
        this.context = context;
    }

    public void setRegisterListener(BaseRequestStateListener listener) {
        this.listener = listener;

    }


    /**
     * 网络请求:获取忘记密码时的验证码
     *
     * @param phomeStr
     */
    public void requestToSendSms(String phomeStr) {
        if (TextUtils.isEmpty(phomeStr)) {
            if (listener != null) {
                listener.requestFail("forget_pwd_sms", 0, context.getResources().getString(R.string.phone_account));
            }
            return;
        }
        if (!HnRegexUtils.isMobileExact(phomeStr)) {
            if (listener != null) {
                listener.requestFail("forget_pwd_sms", 0, context.getResources().getString(R.string.log_input_okphone));
            }
            return;
        }
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("phone", phomeStr);
        HnHttpUtils.postRequest(HnUrl.VERIFY_CODE_FORGETPWD, param, TAG, new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("forget_pwd_sms", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("forget_pwd_sms", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("forget_pwd_sms", errCode, msg);
                }
            }
        });

    }

    /**
     * 网络请求：忘记密码
     *
     * @param phomeStr
     * @param regCodeStr
     * @param regPasswordStr
     */
    public void requestToForgetPwd(String phomeStr, String regCodeStr, String regPasswordStr) {
        if (TextUtils.isEmpty(phomeStr) ) {
            if (listener != null) {
                listener.requestFail("forget_pwd", 0, context.getResources().getString(R.string.phone_account));
            }
            return;
        }
        if( !HnRegexUtils.isMobileExact(phomeStr)){
            if (listener != null) {
                listener.requestFail("forget_pwd", 0, context.getResources().getString(R.string.phone_account_true));
            }
            return;
        }
        if (TextUtils.isEmpty(regPasswordStr)) {
            if (listener != null) {
                listener.requestFail("forget_pwd", 0, context.getResources().getString(R.string.pwd));
            }
            return;
        }
        if (TextUtils.isEmpty(regCodeStr)) {
            if (listener != null) {
                listener.requestFail("forget_pwd", 0, context.getResources().getString(R.string.log_input_ver));
            }
            return;
        }
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("phone", phomeStr);
        param.put("code", regCodeStr);
        param.put("password", regPasswordStr);
        HnHttpUtils.postRequest(HnUrl.FORGETPWD, param, "forget_pwd", new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("forget_pwd", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("forget_pwd", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("forget_pwd", errCode, msg);
                }
            }
        });


    }
}
