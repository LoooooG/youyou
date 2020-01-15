package com.hotniao.svideo.biz.chat;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.svideo.model.HnFastVideoListModel;
import com.hotniao.svideo.model.HnGetChatAnchorInfoModel;
import com.hotniao.svideo.model.HnMineChatStateInfoModel;
import com.loopj.android.http.RequestParams;
import com.videolibrary.videochat.model.HnInvateChatVideoModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于私信界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/12 16:33
 * 修改人：
 * 修改时间：2017/9/12 16:33
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFastChatBiz {
    private String TAG = "HnFastChatBiz";

    public static final String ChatVideoList = "ChatVideoList";
    public static final String ChatVideoMineState = "ChatVideoMineState";//私聊状态
    public static final String ChatVideoOperation = "ChatVideoOperation";//私聊 修改金额  开通  关闭
    public static final String ChatAnchorInfo = "ChatAnchorInfo";//获取主播信息
    public static final String InviteAnchorChat = "InviteAnchorChat";//邀请主播约聊

    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnFastChatBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 网络请求：获取私信详情列表数据
     */
    public void getChatVideoList(int page) {
        RequestParams param = new RequestParams();
        param.put("page", page + "");
        param.put("pagesize", 20 + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_GET_CHAT_DIALOG, param, TAG, new HnResponseHandler<HnFastVideoListModel>( HnFastVideoListModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ChatVideoList, errCode, msg);
                }
            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatVideoList, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatVideoList, model.getC(), model.getM());
                    }
                }

            }
        });
    }

    /**
     * 网络请求：获取开通状态和金额
     */
    public void getChatStateInfo() {
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_PRIVATE_CHAT_INFO, null, TAG, new HnResponseHandler<HnMineChatStateInfoModel>( HnMineChatStateInfoModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ChatVideoMineState, errCode, msg);
                }
            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatVideoMineState, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatVideoMineState, model.getC(), model.getM());
                    }
                }

            }
        });
    }

    /**
     * 开通一对一私聊
     *
     * @param chatPrice
     * @param type      1开通 2修改 3 关闭
     */
    public void openChatVideo(String chatPrice, int type,String chat_category_id) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty("chat_category_id")) {
            params.put("chat_category_id", chat_category_id);
        }
        params.put("chat_price", chatPrice);
        params.put("type", type + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_UPDATA_CHAT_STATUS, params, TAG, new HnResponseHandler<BaseResponseModel>( BaseResponseModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ChatVideoOperation, errCode, msg);
                }
            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatVideoOperation, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatVideoOperation, model.getC(), model.getM());
                    }
                }

            }
        });
    }

    public void openChatVideo_Chat(String chatPrice, int type,String chat_category_id) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty("chat_category_id")) {
            params.put("chat_category_id", chat_category_id);
        }
        params.put("chat_price", chatPrice);
        params.put("type", type + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_UPDATA_CHAT_STATUS, params, TAG, new HnResponseHandler<BaseResponseModel>( BaseResponseModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ChatVideoOperation, errCode, msg);
                }
            }

            @Override
            public void hnSuccess(String response) {

            }
        });
    }
    /**
     *获取主播信息
     *
     */
    public void getChatUserInfo(String userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_ANCHOR_INFO, params, TAG, new HnResponseHandler<HnGetChatAnchorInfoModel>( HnGetChatAnchorInfoModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(ChatAnchorInfo, errCode, msg);
                }
            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(ChatAnchorInfo, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(ChatAnchorInfo, model.getC(), model.getM());
                    }
                }

            }
        });
    }
    /**
     *邀请主播聊天
     */
    public void inviteAnchorChat(String userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_START_PRIVATE_CHAT, params, "LIVE_ANCHOR_START_PRIVATE_CHAT", new HnResponseHandler<HnInvateChatVideoModel>( HnInvateChatVideoModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(InviteAnchorChat, errCode, msg);
                }
            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(InviteAnchorChat, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(InviteAnchorChat, model.getC(), model.getM());
                    }
                }

            }
        });
    }


}
