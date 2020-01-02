package com.hotniao.livelibrary.biz.anchor;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseBiz;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.util.HnLiveDateUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：主播端业务逻辑类，处理主播端的业务计算相关操作，网络操作，ui操作请尽量不要在该类处理
 * 创建人：mj
 * 创建时间：2017/9/14 20:03
 * 修改人：Administrator
 * 修改时间：2017/9/14 20:03
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorBiz extends HnLiveBaseBiz {

    private String TAG = "HnAnchorBiz";
    /**
     * 时间 定时器
     */
    private Disposable observable;
    private long liveTime = 0;
    /**
     * 心跳定时器
     */
    private Disposable heartBeatObserver;
    /**
     * 主播端信息回调接口
     */
    private HnAnchorInfoListener listener;

    private BaseActivity context;


    /**
     * 主播端网络请求类
     */
    private HnAnchorRequestBiz mHnAnchorRequestBiz;


    public HnAnchorBiz(HnAnchorInfoListener listener, BaseActivity context) {
        this.listener = listener;
        this.context = context;
        mHnAnchorRequestBiz = new HnAnchorRequestBiz(listener, context);
    }


    /**
     * 定时器：用于计算直播间的时间
     *
     * @param isOld
     * @param time
     */
    public void timeTask(String isOld, String time) {
        if ("0".equals(isOld)) {
            liveTime = 0;
        } else {
            if (TextUtils.isEmpty(time)) {
                liveTime = Long.valueOf(time);
            }
        }
        observable = Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        liveTime += 1;
                        String timeStr = HnLiveDateUtils.getLiveTime(liveTime);
                        HnLogUtils.i(TAG, "直播时间：" + timeStr + "---->" + aLong);
                        if (listener != null) {
                            listener.showTimeTask(liveTime, timeStr,"1");
                        }
                    }
                });


    }

    /**
     * 定时器 在时间段内发送请求与服务器保持连接
     */
    public void startHeardBeat() {
        mHnAnchorRequestBiz.requestToHeardBeat();
        heartBeatObserver = Observable.interval(20 * 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aVoid) throws Exception {
                        mHnAnchorRequestBiz.requestToHeardBeat();
                    }
                });

    }


    /**
     * 主播发送消息
     *
     * @param messageContent          消息内容
     * @param mIsDanmu                是否是弹幕
     * @param webscoketConnectSuccess webscoket是否连接陈宫
     * @param uid                     主播id
     */
    public void sendMessaqge(String messageContent, boolean mIsDanmu, boolean webscoketConnectSuccess, String uid) {
        if (mHnAnchorRequestBiz != null) {
            mHnAnchorRequestBiz.requestToSendMessage(listener, context, messageContent, mIsDanmu, webscoketConnectSuccess, uid);
        }
    }


    /**
     * 获取未读消息数
     */
    public void getNoReadMsg(){
        if (mHnAnchorRequestBiz != null) {
            mHnAnchorRequestBiz.getNoReadMessage(listener);
        }
    }
    /**
     * 获取在线用户
     */
    public void getRoomUser(String anchorid){
        if (mHnAnchorRequestBiz != null) {
            mHnAnchorRequestBiz.requestToGetRoomUser(anchorid);
        }
    }


    /**
     * 关闭定时器
     */
    public void closeDbservable() {
        if (observable != null) {
            observable.dispose();
            HnLogUtils.i(TAG, "关闭时间定时器");
        }
        if (heartBeatObserver != null) {
            heartBeatObserver.dispose();
            HnLogUtils.i(TAG, "关闭心跳定时器");
        }
    }


    /**
     * 当主播获取到用户发送的礼物时，需要更新该主播u票
     *
     * @param object 数据源
     * @param u_piao u票
     * @param uid    主播id
     * @return
     */
    public String getU_Piao(Object object, String u_piao, String uid) {
        if (TextUtils.isEmpty(uid)) return u_piao;
        ReceivedSockedBean bean = (ReceivedSockedBean) object;
        if (bean != null) {
            String id = bean.getData().getAnchor_uid();
            if (uid.equals(id)) {
                String dot = bean.getData().getTotal_dot();
                return dot;
            }
        }
        return u_piao;
    }


}
