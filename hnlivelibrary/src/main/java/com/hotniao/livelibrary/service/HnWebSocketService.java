package com.hotniao.livelibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.hn.library.HnBaseApplication;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.giflist.HnGiftListManager;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.HnLiveNoticeModel;
import com.hotniao.livelibrary.model.HnLiveRoomInfoModel;
import com.hotniao.livelibrary.model.HnReceiveVideoChatBean;
import com.hotniao.livelibrary.model.bean.HnGiftListBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.event.HnReceiverSysMsgEvent;
import com.hotniao.livelibrary.model.webscoket.HnLogoutModel;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.hotniao.livelibrary.model.webscoket.HnSysMsgModel;
import com.hotniao.livelibrary.model.webscoket.HnUserAccountForbiddenModel;
import com.hotniao.livelibrary.model.webscoket.HnUserLiveForbiddenModel;
import com.hotniao.livelibrary.ui.anchor.activity.HnAnchorActivity;
import com.hotniao.livelibrary.ui.anchor.fragment.HnAnchorInfoFragment;
import com.hotniao.livelibrary.util.HnNotificationUtil;
import com.imlibrary.TCChatRoomMgr;
import com.imlibrary.login.TCLoginMgr;
import com.koushikdutta.async.http.WebSocket;
import com.loopj.android.http.RequestParams;
import com.tencent.TIMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：直播
 * 类描述：全局推送服务   用于连接websocket服务器
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnWebSocketService extends Service {

    private String TAG = "HnWebSocketService";
    /**
     * 通知栏工具类
     */
    private HnNotificationUtil mHnNotificationUtil;


    /**
     * 腾讯IM
     */
    private TCChatRoomMgr mTCChatRoomMgr;
    /**
     * 腾讯云登录
     */
    private TCLoginMgr mTcLoginMgr;


    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        getRequestToGetGiftList();
        HnLogUtils.i(TAG, "webscoket 服务开启");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTcIm();
        return Service.START_REDELIVER_INTENT;
    }

    /**
     * 腾讯云
     */
    private void initTcIm() {
        if (HnBaseApplication.getmUserBean() == null) return;

        //初始化消息回调
        try {
            if (mTcLoginMgr == null) {
                mTcLoginMgr = TCLoginMgr.getInstance();
            }
            if (HnBaseApplication.getmUserBean().getTim() != null)
                mTcLoginMgr.imLogin(HnBaseApplication.getmUserBean().getTim().getAccount(), HnBaseApplication.getmUserBean().getTim().getSign(),
                        HnBaseApplication.getmUserBean().getTim().getApp_id(), HnBaseApplication.getmUserBean().getTim().getAccount_type());
            else HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
                @Override
                public void onSuccess(String uid, HnLoginModel model, String response) {
                    mTcLoginMgr.imLogin(HnBaseApplication.getmUserBean().getTim().getAccount(), HnBaseApplication.getmUserBean().getTim().getSign(),
                            HnBaseApplication.getmUserBean().getTim().getApp_id(), HnBaseApplication.getmUserBean().getTim().getAccount_type());
                }

                @Override
                public void onError(int errCode, String msg) {

                }
            });
        } catch (Exception e) {
        }

        //初始化消息回调
        if (mTCChatRoomMgr == null)
            mTCChatRoomMgr = TCChatRoomMgr.getInstance("");
        mTCChatRoomMgr.removeMsgListener();
        mTCChatRoomMgr.setMessageListener(new TCChatRoomMgr.TCChatRoomListener() {
            @Override
            public void onJoinGroupCallback(int code, String msg) {
            }

            @Override
            public void onSendMsgCallback(int code, TIMMessage timMessage) {
            }

            @Override
            public void onReceiveMsg(int type, String content) {
                try {
                    HnLogUtils.i(TAG, "webscoket 推送的数据：" + content);
                    if (TextUtils.isEmpty(content)) return;
                    JSONObject jsonObject = new JSONObject(content);
                    String msgType = jsonObject.getString("type");
                    matchImOrSocketMsg(msgType, content, content);
                } catch (Exception e) {
                }
            }

            @Override
            public void onGroupDelete(String roomId) {
            }
        });
    }


    /**
     * 分配消息
     *
     * @param type
     * @param data
     * @param object
     */
    private void matchImOrSocketMsg(String type, String data, Object object) {
        Gson gson = new Gson();
        if (HnWebscoketConstants.System_Msg.equals(type)) {//系统消息
            HnSysMsgModel msg = gson.fromJson(data, HnSysMsgModel.class);
            //通知栏
//            if (!"0".equals(msg.getData().getHas_voice())) {
//                mHnNotificationUtil.notify(HnUiUtils.getString(com.hn.library.R.string.app_name), msg.getData().getContent(), HnWebscoketConstants.System_Msg, msg.getData().getContent());
//            }
            EventBus.getDefault().post(new HnReceiverSysMsgEvent(type, msg));
        } else if (HnWebscoketConstants.Send_Pri_Msg.equals(type)) {//私聊消息
            HnPrivateMsgModel msg = gson.fromJson(data, HnPrivateMsgModel.class);
            EventBus.getDefault().post(new HnPrivateMsgEvent(type, msg));
        } else if (HnWebscoketConstants.logout.equals(type)) {//下线通知
            HnLogoutModel msg = gson.fromJson(data, HnLogoutModel.class);
            if (msg != null && msg.getData() != null) {
                String mAndroidId = HnUtils.getUniqueid(this);
                if (mAndroidId.equals(msg.getData().getDevice_id())) {
//                    closeWebscoketManager();
                    if (mTCChatRoomMgr != null)
                        mTCChatRoomMgr.removeMsgListener();

                    //退出时 将直播界面关掉
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room, 0));


                    //采用Arouter框架进行界面跳转
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isMulLogin", true);
                    bundle.putString("msg", msg.getMsg());
                    ARouter.getInstance().build("/main/HnLoginActivity", "app").with(bundle).navigation();
                    stopSelf();


                }
            }
        } else if (HnWebscoketConstants.Live_Notify.equals(type)) {//开播提醒
            HnLiveNoticeModel msg = gson.fromJson(data, HnLiveNoticeModel.class);
            mHnNotificationUtil.notify(HnUiUtils.getString(com.hn.library.R.string.app_name), "您关注的:" + msg.getData().getNick() + "开播了,赶紧搬上小板凳一起来!", HnWebscoketConstants.Live_Notify, msg.getData().getUid());
        } else if (HnWebscoketConstants.User_Forbidden.equals(type)) {//用户被禁用
            HnUserAccountForbiddenModel msg = gson.fromJson(data, HnUserAccountForbiddenModel.class);
            String mUid = HnPrefUtils.getString(NetConstant.User.UID, "");
            if (msg.getData().getUid().equals(mUid)) {
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room, null));
                HnPrefUtils.setBoolean(NetConstant.User.User_Forbidden, true);
                //采用Arouter框架进行界面跳转
                Bundle bundle = new Bundle();
                bundle.putBoolean("User_Forbidden", true);
                ARouter.getInstance().build("/app/HnMainActivity").with(bundle).navigation();
            }
        } else if (HnWebscoketConstants.Live_Forbidden.equals(type)) {//用户被禁播
            HnUserLiveForbiddenModel msg = gson.fromJson(data, HnUserLiveForbiddenModel.class);
            String mUid = HnPrefUtils.getString(NetConstant.User.UID, "");
            if (msg.getData().getUid().equals(mUid)) {
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Live_Forbidden, msg.getData().getTill()));
            }
        } else if (HnWebscoketConstants.Private_Chat_Video.equals(type)) {//视频邀请消息
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            //采用Arouter框架进行界面跳转
            Bundle bundle = new Bundle();

            bundle.putParcelable("roomInfo", bean.getData());
            bundle.putString("userID", HnBaseApplication.getmUserBean().getUser_id());
            bundle.putBoolean("createRoom", true);
            ARouter.getInstance().build("/video/HnOnlineVideoChatActivity", "video").withFlags(Intent.FLAG_ACTIVITY_NEW_TASK).with(bundle).navigation();

        } else if (HnWebscoketConstants.Private_Chat_Cancel.equals(type)) {//取消视频邀请
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            EventBus.getDefault().post(bean);
        } else if (HnWebscoketConstants.Private_Chat_Refuse.equals(type)) {//拒绝视频邀请
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            EventBus.getDefault().post(bean);
        } else if (HnWebscoketConstants.Private_Chat_Accept.equals(type)) {//接受视频邀请
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            EventBus.getDefault().post(bean);
        } else if (HnWebscoketConstants.Private_Chat_HangUp.equals(type)) {//挂断视频邀请
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            EventBus.getDefault().post(bean);
        } else if (HnWebscoketConstants.Private_Chat_Vague.equals(type)) {//模糊视频
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            EventBus.getDefault().post(bean);
        } else if (HnWebscoketConstants.Push_Balance.equals(type)) {//余额
            HnReceiveVideoChatBean bean = gson.fromJson(data, HnReceiveVideoChatBean.class);
            EventBus.getDefault().post(bean);
        }
    }

    @Subscribe
    public void onEvntbusCallback(EventBusBean event) {
        if (event != null) {
            if ("stop_websocket_service".equals(event.getType())) {//停止webscoket服务
                HnLogUtils.i(TAG, "用户退出 断开webscoket服务");
//                mHnNotificationUtil.clearNotifications();
                if (mTCChatRoomMgr != null)
                    mTCChatRoomMgr.removeMsgListener();
                stopSelf();
            } else if (HnLiveConstants.EventBus.Join_To_Room.equals(event.getType())) {//进入直播间
                String data = (String) event.getObj();
                if (!TextUtils.isEmpty(data)) {
                    requestToGetLiveInfo(data);
                }
            }
        }
    }

    /**
     * 网络请求：获取直播信息
     *
     * @param data
     */
    private void requestToGetLiveInfo(String data) {
        RequestParams param = new RequestParams();
        param.put("anchor_user_id", data);
        HnHttpUtils.postRequest(HnLiveUrl.Join_To_Room, param, TAG, new HnResponseHandler<HnLiveRoomInfoModel>(HnLiveRoomInfoModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (model != null && model.getD() != null) {//通过arouter框架进行跳转 进入用户直播间
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", model.getD());
                        ARouter.getInstance().build("/live/HnAudienceActivity").with(bundle).navigation();
                        HnLiveListModel.LiveListBean data = new HnLiveListModel.LiveListBean(model.getD().getLive().getAnchor_live_play_flv(),
                                model.getD().getAnchor().getUser_id(), model.getD().getAnchor().getUser_avatar());
                        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Update_Room_Live, data));
                    }
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }


    /**
     * 网络请求：获取礼物列表数据
     *
     * @param
     */
    public void getRequestToGetGiftList() {
        RequestParams params = new RequestParams();
        params.put("time", HnPrefUtils.getString("giftTime", "0"));
        HnHttpUtils.postRequest(HnLiveUrl.Gift_List, null, TAG, new HnResponseHandler<HnGiftListModel>(HnGiftListModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (model.getD() != null && model.getD().getGift() != null) {
                        transformData(model.getD().getGift());
                        HnPrefUtils.setString("giftTime", model.getD().getTime());
                    }
                } else {
                    HnLogUtils.i(TAG, "获取礼物列表数据失败:" + model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.i(TAG, "获取礼物列表数据失败" + msg);
            }
        });
    }

    /**
     * 数据转换
     *
     * @param gift_list
     * @param
     */
    private void transformData(List<HnGiftListBean.GiftBean> gift_list) {
        if (gift_list == null || gift_list.size() == 0) {
            return;
        }
        ArrayList<GiftListBean> list = new ArrayList<>();
        for (int i = 0; i < gift_list.size(); i++) {
            HnGiftListBean.GiftBean giftBean = gift_list.get(i);
            for (int j = 0; j < giftBean.getItems().size(); j++) {
                HnGiftListBean.GiftBean.ItemsBean result = giftBean.getItems().get(j);
                GiftListBean bean = new GiftListBean();
                bean.setGift_id(result.getId());
                bean.setGiftName(result.getName());
                bean.setDetail(result.getDetail());
                bean.setState(result.getStatus());
                bean.setStaticGiftUrl(result.getIcon());
                bean.setDynamicGiftUrl(result.getIcon_gif());
                bean.setZipDownUrl(result.getAnimation());
                bean.setGiftCoin(result.getCoin());

                bean.setSort(result.getSort());
                bean.setmTabId(giftBean.getId() + "");
                bean.setmTabName(giftBean.getName());
                list.add(bean);
            }
        }
        HnGiftListManager mHnGiftListManager = HnGiftListManager.getInstance();
        mHnGiftListManager.dealGiftList(this, list);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mTCChatRoomMgr != null)
            mTCChatRoomMgr.removeMsgListener();
//        closeWebscoketManager();
        HnLogUtils.i(TAG, "webscoket 服务销毁");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
