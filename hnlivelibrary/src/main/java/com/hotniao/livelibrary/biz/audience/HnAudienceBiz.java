package com.hotniao.livelibrary.biz.audience;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.adapter.HnLiveMessageAdapter;
import com.hotniao.livelibrary.adapter.HnOnlineRecAdapter;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseBiz;
import com.hotniao.livelibrary.model.HnOnlineModel;
import com.hotniao.livelibrary.model.bean.HnLiveRoomInfoBean;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.OnlineBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.ui.audience.fragment.HnAudienceInfoFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户端端业务逻辑类，处理用户端的业务计算相关操作，网络操作，ui操作请尽量不要在该类处理
 * 创建人：mj
 * 创建时间：2017/9/20 10:23
 * 修改人：Administrator
 * 修改时间：2017/9/20 10:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAudienceBiz extends HnLiveBaseBiz {

    private String TAG = "HnAudienceBiz";

    /**
     * 用户端信息回调接口
     */
    private HnAudienceInfoListener listener;
    /**
     * 用户端网络请求类
     */
    private HnAudinceRequestBiz mHnAudinceRequestBiz;
    /**
     * 上下文
     */
    private BaseActivity context;
    /**
     * 免费查看定时器
     */
    private Disposable mFreeLookObserverable;
    /**
     * 计时收费定时器
     */
    private Disposable payObservable;
    /**
     * vip时间倒计器
     */
    private Disposable vipObservable;


    public HnAudienceBiz(HnAudienceInfoListener listener, BaseActivity context) {
        this.listener = listener;
        this.context = context;
        mHnAudinceRequestBiz = new HnAudinceRequestBiz(listener, context);
    }

    /**
     * 用户发送消息
     *
     * @param messageContent          消息内容
     * @param mIsDanmu                是否是弹幕
     * @param webscoketConnectSuccess webscoket是否连接陈宫
     * @param uid                     id
     */
    public void sendMessaqge(String messageContent, boolean mIsDanmu, boolean webscoketConnectSuccess, String uid) {
        if (mHnAudinceRequestBiz != null) {
            mHnAudinceRequestBiz.requestToSendMessage(listener, context, messageContent, mIsDanmu, webscoketConnectSuccess, uid);
        }
    }

    /**
     * 点赞
     *
     * @param anchorId 主播id
     */
    public void clickZan(String anchorId) {
        if (mHnAudinceRequestBiz != null) {
            mHnAudinceRequestBiz.clickZan(anchorId);
        }
    }

    /**
     * 获取未读消息数
     */
    public void getNoReadMsg() {
        if (mHnAudinceRequestBiz != null) {
            mHnAudinceRequestBiz.getNoReadMessage(listener);
        }
    }

    /**
     * 设置关注
     *
     * @param uid
     * @param isFollow
     * @param anchorId 主播id
     */
    public void setFollow(String uid, String isFollow, String anchorId) {
        if (TextUtils.isEmpty(uid)) return;
        if (TextUtils.isEmpty(isFollow) || "N".equals(isFollow)) {
            mHnAudinceRequestBiz.requestToFollow(false, uid, anchorId);
        } else {
            mHnAudinceRequestBiz.requestToFollow(true, uid, anchorId);
        }
    }


    /**
     * 定时付费
     *
     * @param mAnchor_id        主播id
     * @param type              支付类型   3  计时收费   2 门票收费
     * @param hasPayMenPiaoLive 针对门票支付  true：已支付  false：未支付
     */
    public void timePay(final String mAnchor_id, final String type, boolean hasPayMenPiaoLive) {
        if (TextUtils.isEmpty(mAnchor_id)) return;
        if ("2".equals(type)) {
            if (hasPayMenPiaoLive) return;
            mHnAudinceRequestBiz.requestToPay(mAnchor_id, type);
        } else if ("3".equals(type)) {
            if (payObservable != null) {
                payObservable.dispose();
                HnLogUtils.e("payObservable---close");
            }
            payObservable = Observable.interval(0, 60 * 1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mHnAudinceRequestBiz.requestToPay(mAnchor_id, type);
                            HnLogUtils.e("payObservable---" + aLong);
                        }
                    });
        }

    }

    /**
     * 开启vip计时器
     *
     * @param mOwnIsVip 时间
     */
    public void startVIPTimeTask(String mOwnIsVip) {
        if ("Y".equals(mOwnIsVip)) return;
        final long time = Long.valueOf(30) * 1000;
        Date date = new Date();
        if (time - date.getTime() > 6 * 60 * 60 * 1000) {
            HnLogUtils.i(TAG, "vip到时时间过长，无需进行定时操作");
            return;
        }
        vipObservable = Observable.interval(60 * 1000, TimeUnit.MICROSECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Date date = new Date();
                        long nowTime = date.getTime();
                        if (nowTime >= time) {
                            if (listener != null) {
                                listener.vipComeDue();
                            }
                        }
                    }
                });

    }

    /**
     * 点赞
     *
     * @param uid 主播id
     */
    public void setStart(String uid) {
        if (TextUtils.isEmpty(uid)) return;
        mHnAudinceRequestBiz.requestToLike(uid);
    }

    /**
     * 获取主播房间信息
     *
     * @param mAnchor_id 主播id
     */
    public void requestToGetRoomInfo(String mAnchor_id) {
        if (TextUtils.isEmpty(mAnchor_id)) return;
        mHnAudinceRequestBiz.requestToGetRoomInfo(mAnchor_id);

    }


    /**
     * 请求房间用户
     *
     * @param mAnchor_id 主播id
     */
    public void requestToGetRoomUser(String mAnchor_id) {
        if (TextUtils.isEmpty(mAnchor_id)) return;
        mHnAudinceRequestBiz.requestToGetRoomUser(mAnchor_id);

    }

    /**
     * 获取已离开房间的主播信息
     *
     * @param uid 主播id
     */
    public void requestToGetLeaverAnchorInfo(String uid) {
        if (TextUtils.isEmpty(uid)) return;
        mHnAudinceRequestBiz.requestToGetLeaverAnchorInfo(uid);
    }

    /**
     * 离开房间
     *
     * @param anchorId
     */
    public void leaveRoom(String anchorId) {
        if (TextUtils.isEmpty(anchorId)) return;
        mHnAudinceRequestBiz.leaveRoom(anchorId);
    }


    /**
     * 开启免费观看倒计时
     *
     * @param freeTime 时间 秒  0——30s
     */
    public void startFreeLookCountdown(String freeTime) {
        final long count = Long.valueOf(freeTime);
        mFreeLookObserverable = Observable.interval(1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return count - aLong;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        HnLogUtils.i(TAG, "免费观看倒计时：" + aLong);
                        if (aLong == 0) {//倒计时结束
                            if (listener != null) {
                                listener.freeLookFinish();
                            }
                        }

                    }
                });

    }


    /**
     * 主播停止直播，用户离开直播间
     *
     * @param object    数据源
     * @param anchor_id 主播id
     */
    public void leaveAnchorLiveRomm(Object object, String anchor_id, BaseActivity mActvity) {
        Bundle bundle = new Bundle();
        bundle.putString("data", anchor_id);
        ARouter.getInstance().build("/app/HnAudiStopLiveActivity").with(bundle).navigation();
        if (mActvity != null) {
            mActvity.finish();
        }
    }

    /**
     * 用户端接到Online类型后的数据后，获取里面的在线人员列表  不能包括主播
     *
     * @param object 数据源
     * @return anchor_id     主播id
     */
    public ArrayList<OnlineBean> getOnlienList(Object object, String anchor_id) {

        ArrayList<OnlineBean> list = new ArrayList<>();
        if (context == null || object == null) return list;
        HnOnlineModel model = (HnOnlineModel) object;
        if (model == null || model.getData() == null) return list;
        List<HnOnlineModel.DataBean.UsersBean> items = model.getData().getUsers();
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (!TextUtils.isEmpty(anchor_id) && !anchor_id.equals(items.get(i).getUser_id())) {
                    OnlineBean bean = new OnlineBean(items.get(i).getUser_id(), items.get(i).getUser_avatar(), "0", items.get(i).getUser_is_member());
                    list.add(bean);
                }
            }
        }
        return list;
    }


    /**
     * 获取主播的u票
     *
     * @param object
     * @param mAnchor_u_piao
     * @return
     */
    public String getUPiao(Object object, String mAnchor_u_piao) {
        HnReceiveSocketBean bean = (HnReceiveSocketBean) object;
        if (bean != null && bean.getData() != null) {
            return bean.getData().getAnchor().getUser_dot();
        }
        return mAnchor_u_piao;
    }

    /**
     * 从推送的礼物数据中获取主播的u票
     *
     * @param object         数据源
     * @param mAnchor_u_piao 主播当前u票
     * @return
     */
    public String getUPiaoFromGift(Object object, String mAnchor_u_piao) {
        HnReceiveSocketBean bean = (HnReceiveSocketBean) object;
        if (bean != null && bean.getData() != null) {
            return bean.getData().getAnchor().getUser_dot();
        }
        return mAnchor_u_piao;
    }


    /**
     * 关闭订阅
     */
    public void closeObservable() {
        if (mFreeLookObserverable != null) {
            mFreeLookObserverable.dispose();
            HnLogUtils.i(TAG, "关闭免费观看定时器");
        }
    }

    /**
     * 关闭支付订阅
     */
    public void closePayObservable() {
        if (payObservable != null) {
            payObservable.dispose();
            HnLogUtils.e("payObservable---关闭支付定时器");
        }
    }

    /**
     * 关闭vip计时订阅
     */
    public void closeVIPObservable() {
        if (vipObservable != null) {
            vipObservable.dispose();
            HnLogUtils.i(TAG, "关闭支付vip");
        }
    }


    /**
     * 清除websocket和定时器以及显示列表数据  当切换直播间时需要将直播间的数据清空
     *
     * @param mActivity
     * @param mAllList
     * @param messageList
     * @param mOwnIsVip
     * @param mHnOnlineRecAdapter
     * @param mMessageAdapter
     */
    public void clearWebsocketAndListData(HnAudienceInfoFragment mActivity, ArrayList<OnlineBean> mAllList, ArrayList<HnReceiveSocketBean> messageList, String mOwnIsVip, HnOnlineRecAdapter mHnOnlineRecAdapter, HnLiveMessageAdapter mMessageAdapter) {
        closePayObservable();
        closeObservable();
        closeVIPObservable();

        /**
         * 列表数据重置
         */
        mAllList.clear();
        messageList.clear();
        if (mHnOnlineRecAdapter != null) {
            mHnOnlineRecAdapter.notifyDataSetChanged();
        }
        if (mMessageAdapter != null) {
            mMessageAdapter.notifyDataSetChanged();
        }

    }
}
