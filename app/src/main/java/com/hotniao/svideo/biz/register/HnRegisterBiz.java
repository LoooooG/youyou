package com.hotniao.svideo.biz.register;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.model.HnLoginModel;
import com.hotniao.svideo.model.HnRegisterCodeModel;
import com.hn.library.model.HnLoginBean;
import com.hotniao.svideo.utils.HnUserUtil;
import com.loopj.android.http.RequestParams;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理注册界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/4 10:56
 * 修改人：Administrator
 * 修改时间：2017/9/4 10:56
 * 修改备注：
 * Version:  1.0.0
 */
public class HnRegisterBiz {

    private String TAG = "HnRegisterBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;

    private boolean isCanSendSms = true;//是否可以发送验证码

    public HnRegisterBiz(BaseActivity context) {
        this.context = context;
    }

    public void setRegisterListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    public void requestToRegisterFirstStep(String phone, String mYzm, String mPwd, String mYqm) {
        if (TextUtils.isEmpty(phone)) {
            if (listener != null) {
                listener.requestFail("register", 0, context.getResources().getString(R.string.phone_account));
            }
            return;
        }
        if (!HnRegexUtils.isMobileExact(phone)) {
            if (listener != null) {
                listener.requestFail("bind_change_phone", 0, context.getResources().getString(R.string.phone_account_true));
            }
            return;
        }
        if (TextUtils.isEmpty(mYzm)) {
            if (listener != null) {
                listener.requestFail("register", 0, context.getResources().getString(R.string.log_input_yzm));
            }
            return;
        }
        if (TextUtils.isEmpty(mPwd)) {
            if (listener != null) {
                listener.requestFail("register", 0, context.getResources().getString(R.string.please_input_pwd));
            }
            return;
        }
        if (mPwd.length() < 6 || mPwd.length() > 16) {
            if (listener != null) {
                listener.requestFail("register", 0, context.getResources().getString(R.string.please_input_pwd));
            }
            return;
        }
        //设备id
        String mAndroidId = HnUserUtil.getUniqueid();
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("phone", phone);
        param.put("code", mYzm);
        param.put("password", mPwd);
        if (!TextUtils.isEmpty(mYqm)) {
            param.put("invite_code", mYqm);
        }
        HnHttpUtils.postRequest(HnUrl.REGISTER_PHONE, param, TAG, new HnResponseHandler<HnLoginModel>(HnLoginModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {

                    HnLoginBean result = model.getD();
                    HnApplication.setmUserBean(result);
                    if (result != null && result.getUser_id() != null) {
                        HnPrefUtils.setString(HnConstants.LogInfo.UID, result.getUser_id());
                        HnPrefUtils.setString(NetConstant.User.USER_INFO, response);
                        HnPrefUtils.setString(NetConstant.User.ANCHOR_CHAT_CATEGORY, result.getAnchor_chat_category());
                        HnPrefUtils.setBoolean(NetConstant.User.IS_ANCHOR, TextUtils.equals(result.getUser_is_anchor(), "Y"));
                        HnPrefUtils.setString(NetConstant.User.TOKEN, result.getAccess_token());
                        HnPrefUtils.setString(NetConstant.User.Webscket_Url, result.getWs_url());
                        HnPrefUtils.setString(NetConstant.User.Unread_Count, "0");
                        HnPrefUtils.setBoolean(NetConstant.User.User_Forbidden, false);
                    }
                    if (listener != null) {
                        listener.requestSuccess("register_second_step", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("register_second_step", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("register_second_step", errCode, msg);
                }
            }
        });


    }
}
