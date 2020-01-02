package com.hotniao.live.biz.login;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.model.HnLoginBean;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理登录界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/1 16:55
 * 修改人：Administrator
 * 修改时间：2017/9/1 16:55
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLoginBiz {

    public final static String QQ = "qq";
    public final static String WX = "weixin";
    public final static String SINA = "sina";

    private String TAG = "HnLoginBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnLoginBiz(BaseActivity context) {
        this.context = context;
    }

    public void setLoginListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 执行登陆网络请求   适合手机号，第三方登录
     *
     * @param authtoken  openId  手机号
     * @param authsecret 密码
     * @param authtype   登录类型
     */
    public void executeLogin(final String authtoken, final String authsecret, final String authtype, String open_id,String city) {
        if ("phone".equals(authtype)) {
            if (TextUtils.isEmpty(authtoken) || !HnRegexUtils.isMobileExact(authtoken)) {
                if (listener != null) {
                    if (!HnRegexUtils.isMobileExact(authtoken)) {
                        listener.requestFail("login", 0, context.getResources().getString(R.string.phone_account_true));
                    } else {
                        listener.requestFail("login", 0, context.getResources().getString(R.string.phone_account));
                    }

                }
                return;
            }
            if (TextUtils.isEmpty(authsecret)) {
                if (listener != null) {
                    listener.requestFail("login", 0, context.getResources().getString(R.string.please_input_code));
                }
                return;
            }
        }
        RequestParams param = new RequestParams();
        if (listener != null) {
            listener.requesting();
        }

        String url = HnUrl.LOGIN;
        if (QQ.equals(authtype)) {
            url = HnUrl.LOGIN_QQ;
            param.put("auth_access_token", authtoken);
            param.put("open_id", open_id);
        } else if (WX.equals(authtype)) {
            url = HnUrl.LOGIN_WX;
            param.put("auth_access_token", authtoken);
            param.put("open_id", open_id);
        } else if (SINA.equals(authtype)) {
            url = HnUrl.LOGIN_SINA;
            param.put("auth_access_token", authtoken);
            param.put("open_id", open_id);
        } else {
            url = HnUrl.REGISTER_LOGIN;
            param.put("anchor_local",city);
            param.put("phone", authtoken);
            param.put("code", authsecret);

        }


        HnHttpUtils.postRequest(url, param, "HnLoginActivity", new HnResponseHandler<HnLoginModel>(context, HnLoginModel.class) {

            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d("HnLoginActivity","result.getAnchor_chat_category():");
                if (model.getC() == 0) {
                    HnLoginBean result = model.getD();

                    HnApplication.setmUserBean(result);
                    if (result != null && result.getUser_id() != null) {
                        HnPrefUtils.setString(NetConstant.User.UID, result.getUser_id());
                        HnPrefUtils.setString(NetConstant.User.USER_INFO, response);
                        HnPrefUtils.setBoolean(NetConstant.User.IS_ANCHOR, TextUtils.equals(result.getUser_is_anchor(), "Y"));
                        HnPrefUtils.setString(NetConstant.User.ANCHOR_CHAT_CATEGORY, result.getAnchor_chat_category());
                        HnPrefUtils.setString(NetConstant.User.TOKEN, result.getAccess_token());
                        HnPrefUtils.setString(NetConstant.User.Webscket_Url, result.getWs_url());
                        HnPrefUtils.setString(NetConstant.User.Unread_Count, "0");
                        HnPrefUtils.setBoolean(NetConstant.User.User_Forbidden, false);
                        HnLogUtils.d("HnLoginActivity","result.getAnchor_chat_category():"+result.getAnchor_chat_category());
                    }

                    if (listener != null) {
                        listener.requestSuccess("login", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("login", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.d("HnLoginActivity","result.getAnchor_chat_category():error");
                if (listener != null) {
                    listener.requestFail("login", errCode, msg);
                }
            }
        });
    }


    /**
     * 退出清数据
     */
    public void executeExit(String msg) {
        HnPrefUtils.setString(NetConstant.User.UID, "");
        HnPrefUtils.setString(NetConstant.User.Webscket_Url, "");
        HnPrefUtils.setString(NetConstant.User.TOKEN, "");

        long currentTimeMillis = System.currentTimeMillis();
        String curTime = HnUtils.getDateToString_3(currentTimeMillis);
        String[] split = curTime.split(" ");
        String time = split[1];

        CommDialog.newInstance(context).setClickListen(new CommDialog.OneSelDialog() {
            @Override
            public void sureClick() {
            }
        }).setTitle(HnUiUtils.getString(R.string.log_out_nitify)).setContent(msg).setCanceledOnOutside(false).show();
//                .setContent(String.format(HnUiUtils.getString(R.string.account_repeat_login), time)).show();
    }

}
