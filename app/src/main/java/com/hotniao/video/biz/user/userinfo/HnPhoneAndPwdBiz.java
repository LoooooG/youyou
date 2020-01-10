package com.hotniao.video.biz.user.userinfo;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hn.library.global.HnUrl;
import com.hotniao.video.model.HnRegisterCodeModel;
import com.hotniao.video.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于用户的手机号和密码的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/7 16:11
 * 修改人：Administrator
 * 修改时间：2017/9/7 16:11
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPhoneAndPwdBiz {

    private String TAG = "HnPhoneAndPwdBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;


    public HnPhoneAndPwdBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;

    }

    /**
     * 网络请求:获取绑定新手机时的验证码
     *
     * @param phomeStr
     */
    public void requestToSendSms(String phomeStr) {
        if (TextUtils.isEmpty(phomeStr) || !HnRegexUtils.isMobileExact(phomeStr)) {
            if (listener != null) {
                listener.requestFail("forget_pwd_sms", 0, context.getResources().getString(R.string.phone_account));
            }
            return;
        }
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("phone", phomeStr);
        param.put("type", "change_mobile");
        HnHttpUtils.postRequest(HnUrl.SENDSMS, param, TAG, new HnResponseHandler<HnRegisterCodeModel>(context, HnRegisterCodeModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("change_mobile_sms", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("change_mobile_sms", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("change_mobile_sms", errCode, msg);
                }
            }
        });

    }

    /**
     * 网络请求：绑定信息的手机
     *
     * @param phomeStr   手机号
     * @param regCodeStr 验证码
     */
    public void requestToBindNewPhone(String phomeStr, String regCodeStr, String pwd) {
        if (TextUtils.isEmpty(phomeStr)) {
            if (listener != null) {
                listener.requestFail("bind_new_phone", 0, context.getResources().getString(R.string.phone_account));
            }
            return;
        }
        if (!HnRegexUtils.isMobileExact(phomeStr)) {
            if (listener != null) {
                listener.requestFail("bind_change_phone", 0, context.getResources().getString(R.string.phone_account_true));
            }
            return;
        }
        if (TextUtils.isEmpty(regCodeStr)) {
            if (listener != null) {
                listener.requestFail("bind_new_phone", 0, context.getResources().getString(R.string.log_input_ver));
            }
            return;
        }
       /* if (TextUtils.isEmpty(pwd)) {
            if (listener != null) {
                listener.requestFail("bind_new_phone", 0, context.getResources().getString(R.string.please_input_pwd));
            }
            return;
        }
        if (6 > pwd.length() || 16 < pwd.length()) {
            if (listener != null) {
                listener.requestFail("bind_new_phone", 0, HnUiUtils.getString(R.string.please_input_pwd));
            }
            return;
        }*/
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("phone", phomeStr);
        param.put("code", regCodeStr);
//        param.put("password", pwd);
        HnHttpUtils.postRequest(HnUrl.BIND_PHONE, param, "BIND_PHONE", new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("bind_new_phone", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("bind_new_phone", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("bind_new_phone", errCode, msg);
                }
            }
        });


    }

    /**
     * 网络请求：更换绑定手机
     *
     * @param phomeStr   手机号
     * @param regCodeStr 验证码
     * @param oldCode    更换之前的验证码
     */
    public void requestToChangeBindPhone(String phomeStr, String regCodeStr, String oldCode) {
        if (TextUtils.isEmpty(phomeStr)) {
            if (listener != null) {
                listener.requestFail("bind_change_phone", 0, context.getResources().getString(R.string.phone_account));
            }
            return;
        }
        if (!HnRegexUtils.isMobileExact(phomeStr)) {
            if (listener != null) {
                listener.requestFail("bind_change_phone", 0, context.getResources().getString(R.string.phone_account_true));
            }
            return;
        }
        if (TextUtils.isEmpty(regCodeStr)) {
            if (listener != null) {
                listener.requestFail("bind_change_phone", 0, context.getResources().getString(R.string.log_input_ver));
            }
            return;
        }
//        if (TextUtils.isEmpty(oldCode)) {
//            if(listener!=null){
//                listener.requestFail("bind_new_phone",0,context.getResources().getString(R.string.please_input_pwd));
//            }
//            return;
//        }
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("new_phone", phomeStr);
        param.put("code", oldCode);
        param.put("new_phone_code", regCodeStr);
        HnHttpUtils.postRequest(HnUrl.BIND_CHANGE_PHONE, param, "BIND_CHANGE_PHONE", new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("bind_change_phone", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("bind_change_phone", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("bind_change_phone", errCode, msg);
                }
            }
        });


    }

    /**
     * 网络请求:修改密码
     *
     * @param curPwd   旧密码
     * @param newPwd   新密码
     * @param mConfirm 确认密码
     */
    public void editUserPwd(String curPwd, String newPwd, String mConfirm) {
        if (TextUtils.isEmpty(curPwd)) {
            if (listener != null) {
                listener.requestFail("chanage_pwd", 0, context.getString(R.string.please_input_old_password));
            }
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            if (listener != null) {
                listener.requestFail("chanage_pwd", 0, context.getString(R.string.please_input_new_password));
            }
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            if (listener != null) {
                listener.requestFail("chanage_pwd", 0, context.getString(R.string.please_input_new_password_again));
            }
            return;
        }

        if (curPwd.length() > 16 || curPwd.length() < 6 || newPwd.length() > 16 || newPwd.length() < 6 || mConfirm.length() > 16 || mConfirm.length() < 6) {
            if (listener != null) {
                listener.requestFail("chanage_pwd", 0, context.getString(R.string.password_length_6_16));
            }
            return;
        }
        if (!mConfirm.equalsIgnoreCase(newPwd)) {
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.pwd_not_match));
            return;
        }
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("password", curPwd);
        param.put("new_password", newPwd);
        HnHttpUtils.postRequest(HnUrl.MODIFYPWD, param, "修改密码", new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("chanage_pwd", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("chanage_pwd", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("chanage_pwd", errCode, msg);
                }
            }
        });

    }
}
