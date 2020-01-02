package com.videolibrary.videochat.biz;

import android.content.Context;
import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.loopj.android.http.RequestParams;
import com.videolibrary.model.HnFeeDeductionModel;
import com.videolibrary.videochat.model.HnInvateChatVideoModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，该类用于处理直播间 客户端和主播端 公共的view交互，不做业务处理以及网络请求
 * 创建人：Administrator
 * 创建时间：2017/9/20 10:41
 * 修改人：Administrator
 * 修改时间：2017/9/20 10:41
 * 修改备注：
 * Version:  1.0.0
 */
public class HnVideoChatBiz {

    public static final String SendMessage = "SendMessage";
    public static final String AcceptChat = "AcceptChat";//接受
    public static final String HangUpChat = "HangUpChat";//挂断
    public static final String FeeDeduction = "FeeDeduction";//扣费

    private String TAG = "HnLiveBaseViewBiz";
    private BaseRequestStateListener listener;
    /**
     * 心跳定时器
     */
    private Disposable heartBeatObserver;

    private Context mContext;

    public HnVideoChatBiz(BaseActivity context) {
        this.mContext = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }


    /**
     * 主播发送消息
     *
     * @param messageContent          消息内容
     * @param webscoketConnectSuccess webscoket是否连接陈宫
     * @param uid                     主播id
     */
    public void sendMessaqge(String messageContent, boolean webscoketConnectSuccess, String uid) {
        if (TextUtils.isEmpty(uid) || mContext == null) return;
        if (TextUtils.isEmpty(messageContent)) {
            if (listener != null) {
                listener.requestFail(SendMessage, 0, HnUiUtils.getResources().getString(R.string.live_input_chat_content));
            }
            return;
        }
        if (!webscoketConnectSuccess) {
            if (listener != null) {
                listener.requestFail(SendMessage, 1, HnUiUtils.getResources().getString(R.string.live_again_connected));
            }
            return;
        }
        RequestParams param = new RequestParams();
        param.put("content", messageContent);
        param.put("anchor_user_id", uid);
        HnHttpUtils.postRequest(HnLiveUrl.Send_Msg, param, HnLiveUrl.Send_Msg, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(SendMessage, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(SendMessage, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(SendMessage, errCode, msg);
                }
            }
        });
    }

    /**
     * 取消一对一
     * 1 主动取消 2时间超时
     */
    public void cancleChatVideo(String chatLog, int type) {
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        params.put("type", type + "");

        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_CANCEL_PRIVATE_CHAT, params, "LIVE_ANCHOR_CANCEL_PRIVATE_CHAT", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
            }

            @Override
            public void hnErr(int errCode, String msg) {
            }
        });
    }

    /**
     * 拒绝一对一聊天
     */
    public void refuseChatVideo(String chatLog) {
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_REFUSE_PRIVATE_CHAT, params, "LIVE_ANCHOR_REFUSE_PRIVATE_CHAT", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
            }

            @Override
            public void hnErr(int errCode, String msg) {
            }
        });
    }

    /**
     * 接受一对一聊天
     */
    public void acceptChatVideo(String chatLog) {
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_ACCEPT_PRIVATE_CHAT, params, "LIVE_ANCHOR_ACCEPT_PRIVATE_CHAT", new HnResponseHandler<HnInvateChatVideoModel>(HnInvateChatVideoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(AcceptChat, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(AcceptChat, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(AcceptChat, errCode, msg);
                }
            }
        });
    }

    /**
     * 挂断一对一聊天
     */
    public void hangUpChatVideo(String chatLog) {
        HnLogUtils.e("finish_chat---hangUpChatVideo");
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_HANGUP_PRIVATE_CHAT, params, "LIVE_ANCHOR_HANGUP_PRIVATE_CHAT", new HnResponseHandler<HnInvateChatVideoModel>(HnInvateChatVideoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(HangUpChat, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(HangUpChat, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(HangUpChat, errCode, msg);
                }
            }
        });
    }

    /**
     * 扣费一对一聊天
     */
    public void feeDeductionChatVideo(String chatLog) {
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_PRIVATE_CHAT_MINUTE, params, "LIVE_ANCHOR_PRIVATE_CHAT_MINUTE", new HnResponseHandler<HnFeeDeductionModel>(HnFeeDeductionModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(FeeDeduction, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(FeeDeduction, model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(FeeDeduction, errCode, msg);
                }
            }
        });
    }

    /**
     * 模糊一对一聊天
     * <p>
     * 0 取消模糊 1模糊
     */
    public void fuzzyChatVideo(String chatLog, int type) {
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        params.put("type", type + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_VAGUE_CHAT, params, "LIVE_ANCHOR_VAGUE_CHAT", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {

            }

            @Override
            public void hnErr(int errCode, String msg) {

            }
        });
    }


    /**
     * 心跳
     * <p>
     * 1表示主播端 2表示用户端
     */
    public void heardBeat(String chatLog, int type) {
        if (mContext == null) return;
        RequestParams params = new RequestParams();
        params.put("chat_log", chatLog);
        params.put("type", type + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_ANCHOR_CHAT_HEARTBEAT, params, "LIVE_ANCHOR_CHAT_HEARTBEAT", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {

            }

            @Override
            public void hnErr(int errCode, String msg) {

            }
        });
    }

    /**
     * 定时器 在时间段内发送请求与服务器保持连接
     * 1表示主播端 2表示用户端
     */
    public void startHeardBeat(final String chatLog, final int type) {
        heardBeat(chatLog, type);
        if (heartBeatObserver != null) heartBeatObserver.dispose();
        heartBeatObserver = Observable.interval(10 * 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aVoid) throws Exception {
//                        HnToastUtils.showToastShort("startHeardBeat__startHeardBeat");
                        heardBeat(chatLog, type);
                    }
                });

    }

    /**
     * 关闭定时器
     */
    public void closeDbservable() {
        if (heartBeatObserver != null) {
            heartBeatObserver.dispose();
//            HnToastUtils.showToastShort("startHeardBeat__关闭心跳定时器");
            HnLogUtils.i("startHeardBeat", "关闭心跳定时器");
        }
    }
}
