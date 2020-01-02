package com.hotniao.livelibrary.biz;

import android.app.Activity;
import android.text.TextUtils;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.HnUserInfoDetailModel;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于用户详情界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/13 10:11
 * 修改人：Administrator
 * 修改时间：2017/9/13 10:11
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserDetailBiz {

    public static final String UserInfoDetail="user_info_detail";
    public static final String Follow="follow";

    public static final  String ADD_BLACK="ADD_BLACK";
    public static final  String REPORT="REPORT";

    private String TAG = "HnPrivateLetterBiz";
    private Activity context;

    private BaseRequestStateListener listener;

    public HnUserDetailBiz(Activity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 网络请求:获取用户信息
     *
     * @param uid
     * @param mRoomId 房间id
     */
    public void requestToUserDetail(String uid, String mRoomId) {
        if (TextUtils.isEmpty(uid)) return;
        RequestParams param = new RequestParams();
        param.put("user_id", uid);
        param.put("anchor_user_id", mRoomId);
        HnHttpUtils.postRequest(HnLiveUrl.Get_User_Info, param, TAG, new HnResponseHandler<HnUserInfoDetailModel>(HnUserInfoDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(UserInfoDetail, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(UserInfoDetail, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(UserInfoDetail, errCode, msg);
                }

            }
        });

    }

    /**
     * 网络请求:是否关注
     *
     * @param isCared 是否已关注
     * @param uid     用户id
     */
    public void requestToFollow(boolean isCared, String uid, String anchorId) {

        if (isCared) {
            HnUserControl.cancelFollow(uid, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (listener != null) {
                        listener.requestSuccess(Follow, response, "");
                    }
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (listener != null) {
                        listener.requestFail(Follow, errCode, msg);
                    }
                }
            });
        } else {

            HnUserControl.addFollow(uid, anchorId, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (listener != null) {
                        listener.requestSuccess(Follow, response, "");
                    }
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (listener != null) {
                        listener.requestFail(Follow, errCode, msg);
                    }
                }
            });
        }

    }


    /**
     * 网络请求：拉黑
     * @paramtype 1加入黑名单 2解除拉黑
     */
    public void blackOpro(final String userId ,String type) {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id",userId);
        param.put("type",type);
        HnHttpUtils.postRequest(HnUrl.USER_PROFILR_ADD_BLACK, param, "USER_PROFILR_ADD_BLACK", new HnResponseHandler<BaseResponseModel>( BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ADD_BLACK, response, userId);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ADD_BLACK, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ADD_BLACK, errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求：举报
     * @param
     */
    public void report(final String userId,  String content) {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id",userId);
        param.put("content",content);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_REPORT_ROOM, param, "USER_PROFILR_ADD_BLACK", new HnResponseHandler<BaseResponseModel>( BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(REPORT, response, userId);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(REPORT, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(REPORT, errCode, msg);
                }
            }
        });
    }
}
