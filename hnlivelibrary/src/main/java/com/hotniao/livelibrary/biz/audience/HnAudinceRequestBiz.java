package com.hotniao.livelibrary.biz.audience;

import com.hn.library.base.BaseActivity;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.HnLiveRoomInfoModel;
import com.hotniao.livelibrary.model.HnPayRoomPriceModel;
import com.hotniao.livelibrary.model.HnUserInfoDetailModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：网络业务逻辑类：该类不做其他操作，只做用户播端的相关网络操作，请勿将其他操作放入其中
 * 创建人：mj
 * 创建时间：2017/9/16 10:48
 * 修改人：Administrator
 * 修改时间：2017/9/16 10:48
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAudinceRequestBiz extends HnLiveBaseRequestBiz {

    private String TAG = "HnAnchorRequestBiz";

    /**
     * 主播端信息回调接口
     */
    private HnAudienceInfoListener listener;
    /**
     * 上下文
     */
    private BaseActivity context;


    public HnAudinceRequestBiz(HnAudienceInfoListener listener, BaseActivity context) {
        this.listener = listener;
        this.context = context;
    }


    /**
     * 网络请求:关注/取消关注
     */
    public void requestToFollow(boolean isFollow, String mUid, String anchorId) {
        if (isFollow) {
            HnUserControl.cancelFollow(mUid, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (listener != null) {
                        listener.requestSuccess("follow", response, "");
                    }
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (listener != null) {
                        listener.requestFail("follow", errCode, msg);
                    }
                }
            });
        } else {
            HnUserControl.addFollow(mUid, anchorId, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (listener != null) {
                        listener.requestSuccess("follow", response, "");
                    }
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (listener != null) {
                        listener.requestFail("follow", errCode, msg);
                    }
                }
            });

        }

    }

    /**
     * 网络请求:点赞
     *
     * @param uid
     */
    public void requestToLike(String uid) {
        RequestParams param = new RequestParams();
        param.put("uid", uid);
        HnHttpUtils.postRequest(HnLiveUrl.Like, param, TAG, new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("like", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("like", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("like", errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求:用户-支付直播费用
     *
     * @param mAnchor_id 主播id
     * @param type       3  计时收费   2 门票收费
     */
    public void requestToPay(final String mAnchor_id, String type) {
        if(context==null)return;
        RequestParams param = new RequestParams();
        param.put("anchor_user_id", mAnchor_id);
        String url = "";
        if ("2".equals(type)) {
            url = HnLiveUrl.Pay_Fare;
        } else if ("3".equals(type)) {
            url = HnLiveUrl.Pay_Minute;
        }
        HnHttpUtils.postRequest(url, param, "pay_live", new HnResponseHandler<HnPayRoomPriceModel>(context, HnPayRoomPriceModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("pay_room_price", mAnchor_id, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("pay_room_price", model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("pay_room_price", errCode, msg);
                }
            }
        });

    }

    /**
     * 获取主播房间信息
     *
     * @param uid 主播id
     */
    public void requestToGetRoomInfo(final String uid) {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id", uid);
        try {


            HnHttpUtils.postRequest(HnLiveUrl.Join_To_Room, param, uid, new HnResponseHandler<HnLiveRoomInfoModel>(context, HnLiveRoomInfoModel.class) {
                @Override
                public void hnSuccess(String response) {
                    if (model.getC() == 0) {
                        if (listener != null) {
                            listener.requestSuccess("join_live_room", uid, model);
                            HnLiveListModel.LiveListBean data = new HnLiveListModel.LiveListBean(model.getD().getLive().getAnchor_live_play_rtmp(),
                                    model.getD().getAnchor().getUser_id(), model.getD().getAnchor().getUser_avatar());
                            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Update_Room_Live, data));
                        }
                    } else {
                        if (listener != null) {
                            listener.requestFail("join_live_room", model.getC(), model.getM());
                        }
                    }
                }

                @Override
                public void hnErr(int errCode, String msg) {
                    if (listener != null) {
                        listener.requestFail("join_live_room", errCode, msg);
                    }
                }
            });
        } catch (Exception e) {
            HnLogUtils.e("aa");
        }
    }



    /**
     * 网络请求:离开房间的主播个人信息
     *
     * @param uid
     */
    public void requestToGetLeaverAnchorInfo(String uid) {
        RequestParams param = new RequestParams();
        param.put("user_id", uid);
        HnHttpUtils.postRequest(HnLiveUrl.Get_User_Info, param, TAG, new HnResponseHandler<HnUserInfoDetailModel>(context, HnUserInfoDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("user_info", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("user_info", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("join_live_room", errCode, msg);
                }

            }
        });
    }

    public void leaveRoom(String roomId) {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id", roomId);
        HnHttpUtils.postRequest(HnLiveUrl.Leave_To_Room, param, TAG, new HnResponseHandler<HnUserInfoDetailModel>(context, HnUserInfoDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
            }

            @Override
            public void hnErr(int errCode, String msg) {
            }
        });
    }
}
