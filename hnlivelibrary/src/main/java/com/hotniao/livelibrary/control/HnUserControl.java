package com.hotniao.livelibrary.control;

import android.text.TextUtils;

import com.hn.library.HnBaseApplication;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.model.HnLoginBean;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取用户信息  关注  取消关注
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserControl {


    public static void getProfile(final OnUserInfoListener loginListener) {
        HnHttpUtils.postRequest(HnUrl.PROFILE, null, HnUrl.PROFILE, new HnResponseHandler<HnLoginModel>(HnLoginModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model != null && model.getC() == 0) {
                    HnLoginBean result = model.getD();
                    if (result != null && result.getUser_id() != null) {
                        HnBaseApplication.setmUserBean(result);
                        HnPrefUtils.setString(NetConstant.User.UID, result.getUser_id());
                        HnPrefUtils.setString(NetConstant.User.USER_INFO, response);
                        HnPrefUtils.setString(NetConstant.User.ANCHOR_CHAT_CATEGORY, result.getAnchor_chat_category());
                        HnPrefUtils.setBoolean(NetConstant.User.IS_ANCHOR, TextUtils.equals(result.getUser_is_anchor(), "Y"));
                        HnPrefUtils.setString(NetConstant.User.TOKEN, result.getAccess_token());
                        HnPrefUtils.setString(NetConstant.User.Webscket_Url, result.getWs_url());
                        HnPrefUtils.setString(NetConstant.User.Unread_Count, "0");
                        HnPrefUtils.setBoolean(NetConstant.User.User_Forbidden, false);
                        if (loginListener != null) {
                            loginListener.onSuccess(result.getUser_id(), model, response);
                        }
                    } else {
                        if (loginListener != null) {
                            loginListener.onError(model.getC(), "账号信息不存在");
                        }
                    }

                } else {
                    if (loginListener != null) {
                        loginListener.onError(model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (loginListener != null) {
                    loginListener.onError(errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求取消关注
     *
     * @param uid 用户is
     */
    public static void cancelFollow(final String uid, final OnUserOperationListener listener) {
        RequestParams param = new RequestParams();
        param.put("user_id", uid);
        HnHttpUtils.postRequest(HnLiveUrl.DELETE_FOLLOW, param, "取消关注", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.onSuccess(uid, "", response);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(uid, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.onError(uid, errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求：添加关注
     *
     * @param uid 用户id
     */
    public static void addFollow(final String uid, String mAnchorId, final OnUserOperationListener listener) {
        RequestParams param = new RequestParams();
        param.put("user_id", uid);
        if (!TextUtils.isEmpty(mAnchorId))
            param.put("anchor_user_id", mAnchorId);
        HnHttpUtils.postRequest(HnLiveUrl.ADDFOLLOW, param, "添加关注", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.onSuccess(uid, "", response);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(uid, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.onError(uid, errCode, msg);
                }
            }
        });
    }


    /**
     * 用户信息
     */
    public interface OnUserInfoListener {
        void onSuccess(String uid, HnLoginModel model, String response);

        void onError(int errCode, String msg);
    }

    /**
     * 用户操作
     */
    public interface OnUserOperationListener {
        void onSuccess(String uid, Object obj, String response);

        void onError(String uid, int errCode, String msg);
    }

}
