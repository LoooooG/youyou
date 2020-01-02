package com.hotniao.livelibrary.biz.livebase;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.HnNoReadMessageModel;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：网络业务逻辑类：该类不做其他操作，只做用户播/主播端的相关网络操作，请勿将其他操作放入其中
 * 创建人：mj
 * 创建时间：2017/9/16 10:48
 * 修改人：Administrator
 * 修改时间：2017/9/16 10:48
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveBaseRequestBiz<T extends  HnLiveBaseListener>  {

   private String  TAG="HnLiveBaseRequestBiz";
    /**
     * 请求常量
     */
    /**请求礼物列表数据*/
    public static   final String REQUEST_TO_GIFT_LIST="REQUEST_TO_GIFT_LIST";
    /**请求未读消息*/
    public static   final String REQUEST_TO_NO_READ_MSG="REQUEST_TO_NO_READ_MSG";


    /**
     * 主播发送消息
     * @param listener                    监听回调
     * @param context                     上下文
     * @param messageContent              消息内容
     * @param mIsDanmu                    是否是弹幕
     * @param webscoketConnectSuccess      webscoket是否连接陈宫
     * @param uid                          用户id
     */
    public void requestToSendMessage(final T listener, BaseActivity context, String messageContent, final boolean mIsDanmu, boolean webscoketConnectSuccess, String uid) {
        if(context==null)  return;
        if(TextUtils.isEmpty(uid)||context==null)  return;
        if (TextUtils.isEmpty(messageContent)) {
            if (listener != null) {
                listener.requestFail("send_Messaqge", 0,context.getResources().getString(R.string.live_input_chat_content));
            }
            return;
        }
        if (!webscoketConnectSuccess) {
            if (listener != null) {
                listener.requestFail("send_Messaqge", 1,context.getResources().getString(R.string.live_again_connected));
            }
            return;
        }
        RequestParams param=new RequestParams();
        param.put("content",messageContent);
        param.put("anchor_user_id",uid);

        String url="";
        if(mIsDanmu){
            url= HnLiveUrl.Send_Barrage;
        }else{
            url= HnLiveUrl.Send_Msg;
        }
        HnHttpUtils.postRequest(url, param, TAG, new HnResponseHandler<BaseResponseModel>(context,BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if(model.getC()==0) {
                    if (listener != null) {
                        listener.requestSuccess("send_Messaqge", response,model);
                    }
                }else {
                    if (listener != null) {
                        listener.requestFail("send_Messaqge", model.getC(),model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("send_Messaqge", errCode,msg);
                }
            }
        });
    }

    /**
     * 网络请求：获取礼物列表
     */
    public  void  requestToGetGiftList(final T listener){
        RequestParams param=new RequestParams();
        param.put("time", HnPrefUtils.getString("giftTime", "0"));
        HnHttpUtils.postRequest(HnLiveUrl.Gift_List, param, TAG, new HnResponseHandler<HnGiftListModel>(HnGiftListModel.class) {
            @Override
            public void hnSuccess(String response) {
                if(model.getC()==0) {
                    if (listener != null) {
                        HnPrefUtils.setString("giftTime", model.getD().getTime());
                        listener.requestSuccess(REQUEST_TO_GIFT_LIST, response,model);
                    }
                }else {
                    if (listener != null) {
                        listener.requestFail(REQUEST_TO_GIFT_LIST, model.getC(),model.getM());
                    }
                }
            }
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(REQUEST_TO_GIFT_LIST, errCode,msg);
                }
            }
        });
    }

    public void clickZan(String anchorId){
        RequestParams params=new RequestParams();
        params.put("anchor_user_id",anchorId);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_ROOM_LIKE, params, TAG, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
            }
            @Override
            public void hnErr(int errCode, String msg) {
            }
        });
    }

    /**
     * 获取未读消息数
     */
    public void getNoReadMessage(final T listener) {
        HnHttpUtils.postRequest(HnUrl.USER_CHAT_UNREAD, null, HnUrl.USER_CHAT_UNREAD, new HnResponseHandler<HnNoReadMessageModel>(HnNoReadMessageModel.class) {
            @Override
            public void hnSuccess(String response) {
                try {
                    if (model.getC() == 0 && model.getD().getUnread() != null) {
                        HnNoReadMessageModel.DBean.UnreadBean bean = model.getD().getUnread();
                        HnPrefUtils.setString(NetConstant.User.Unread_Count, bean.getUser_chat());
                        if (listener != null) {
                            listener.requestSuccess(REQUEST_TO_NO_READ_MSG,response,bean.getUser_chat());
                        }

                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(REQUEST_TO_NO_READ_MSG, errCode,msg);
                }
            }
        });
    }

    /**
     * 获取主播房间信息
     *
     * @param uid 主播id
     */
    public void requestToGetRoomUser(final String uid) {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id", uid);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_ROOM_USERLIST, param, TAG, new HnResponseHandler<BaseResponseModel>( BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.e("aa");
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.e("aa");
            }
        });
    }
}
