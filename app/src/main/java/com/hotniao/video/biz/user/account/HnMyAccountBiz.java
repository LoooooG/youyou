package com.hotniao.video.biz.user.account;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.video.model.HnAliPayModel;
import com.hotniao.video.model.HnInviteWithdrawLogModel;
import com.hotniao.video.model.HnWxPayModel;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hn.library.model.HnLoginModel;
import com.hotniao.video.model.HnProfileMode;
import com.hn.library.global.HnUrl;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理我的账户的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/8 17:59
 * 修改人：Administrator
 * 修改时间：2017/9/8 17:59
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyAccountBiz {
    public static final String WxPay="WxPay";
    public static final String AliPay="AliPay";
    public static final String INVITE_WITHDRAW_RECORD="invite_withdraw_record";

    private String TAG = "HnMyAccountBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;


    public HnMyAccountBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 网路请求：获取我的账户界面数据
     */
    public void requestToMyAccount() {
        HnHttpUtils.getRequest(HnUrl.Get_Account, null, TAG, new HnResponseHandler<HnProfileMode>(context, HnProfileMode.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        if (model.getD() != null) {
                            listener.requestSuccess("my_account", response, model);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("my_account", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("my_account", errCode, msg);
                }
            }
        });


    }


    /**
     * 网络请求:支付宝充值
     *
     * @param id 套餐Id
     */
    public void rechargeAli(final String id) {
        RequestParams param = new RequestParams();
        param.put("recharge_combo_id", id);
        if (listener != null) {
            listener.requesting();
        }
        HnHttpUtils.postRequest(HnUrl.Pre_Pay_ZFB, param, TAG, new HnResponseHandler<HnAliPayModel>(context, HnAliPayModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(AliPay, id, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(AliPay, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(AliPay, errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求:微信充值
     */
    public void rechargeWxi(final String id) {
        RequestParams param = new RequestParams();
        param.put("recharge_combo_id", id);
        if (listener != null) {
            listener.requesting();
        }
        HnHttpUtils.postRequest(HnUrl.Pre_Pay_WX, param, TAG, new HnResponseHandler<HnWxPayModel>(context, HnWxPayModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(WxPay, id, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(WxPay, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(WxPay, errCode, msg);
                }
            }
        });
    }


    /**
     * 网络请求：用于获取用户信息
     */
    public void requestToUserInfo() {
        HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
            @Override
            public void onSuccess(String uid, HnLoginModel model, String response) {
                if (listener != null) {
                    listener.requestSuccess("user_info", response, model);
                }
            }

            @Override
            public void onError(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("user_info", errCode, msg);
                }
            }
        });

    }

    /**
     * 邀请提现记录
     */
    public void inviteWithdrawRecord(int page) {
        RequestParams param = new RequestParams();
        param.put("page", page+"");
        param.put("pageSize", "20");
        HnHttpUtils.postRequest(HnUrl.INVITE_WITHDRAW_RECORD, param, TAG, new HnResponseHandler<HnInviteWithdrawLogModel>(context, HnInviteWithdrawLogModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(INVITE_WITHDRAW_RECORD, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(INVITE_WITHDRAW_RECORD, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(INVITE_WITHDRAW_RECORD, errCode, msg);
                }
            }
        });
    }
}
