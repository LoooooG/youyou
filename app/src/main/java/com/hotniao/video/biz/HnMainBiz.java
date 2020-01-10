package com.hotniao.video.biz;

import android.text.TextUtils;
import android.view.View;

import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.model.HnConfigModel;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.HnMainActivity;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnWebActivity;
import com.hotniao.video.dialog.HnSignStatePopWindow;
import com.hotniao.video.dialog.HnUpGradeDialog;
import com.hotniao.video.eventbus.HnSignEvent;
import com.hotniao.video.model.HnBannerModel;
import com.hotniao.video.model.HnHomeHotModel;
import com.hotniao.video.model.HnSignStateModel;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.HnNoReadMessageModel;
import com.loopj.android.http.RequestParams;
import com.tencent.openqq.protocol.imsdk.msg;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于首页界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/13 13:54
 * 修改人：Administrator
 * 修改时间：2017/9/13 13:54
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMainBiz {

    public static final String NoReadMsg = "NoReadMsg";//为阅读消息
    public static final String SignStatue = "SignStatue";//签名状态
    public static final String CheckVersion = "CheckVersion";//检测版本

    private String TAG = "HnMainBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnMainBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 网络请求：版本检测
     */
    public void requestToCheckVersion() {
        HnHttpUtils.postRequest(HnUrl.USER_APP_CONFIG, null, TAG, new HnResponseHandler<HnConfigModel>(HnConfigModel.class) {
                    @Override
                    public void hnSuccess(String response) {
                        if (model.getC() == 0) {
                            if (listener != null) {
                                listener.requestSuccess(CheckVersion, response, model);
                            }
                        } else {
                            if (listener != null) {
                                listener.requestFail(CheckVersion, model.getC(), model.getM());
                            }
                        }
                    }

                    @Override
                    public void hnErr(int errCode, String msg) {
                        if (listener != null) {
                            listener.requestFail(CheckVersion, errCode, msg);
                        }
                    }
                }
        );
    }


    /**
     * 获取未读消息数
     */
    public void getNoReadMessage() {
        HnHttpUtils.postRequest(HnUrl.USER_CHAT_UNREAD, null, HnUrl.USER_CHAT_UNREAD, new HnResponseHandler<HnNoReadMessageModel>(HnNoReadMessageModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(NoReadMsg, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(NoReadMsg, model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(NoReadMsg, errCode, msg);
                }
            }
        });
    }

    /**
     * 获取是否签到   1 第一次
     */
    public void getSignState(final String type) {
        HnHttpUtils.postRequest(HnUrl.USER_SIGN_JUDGE, null, HnUrl.USER_SIGN_JUDGE, new HnResponseHandler<HnSignStateModel>(HnSignStateModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(SignStatue, type, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(SignStatue, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(SignStatue, errCode, msg);
                }
            }
        });
    }

    /***
     * 如果个人信息缺失 在获取一次
     */
    public void getUserInfo(final int type) {
        HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
            @Override
            public void onSuccess(String uid, HnLoginModel model, String response) {
                if (type == 1) {
                    HnApplication.login(uid);
                }
            }

            @Override
            public void onError(int errCode, String msg) {
            }
        });
    }


    public void online() {
        HnHttpUtils.postRequest(HnUrl.ONLNINE, null, HnUrl.ONLNINE, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {


            }

            @Override
            public void hnErr(int errCode, String msg) {

            }
        });
    }
}
