package com.hotniao.livelibrary.biz.tencent.pull;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnNetUtil;
import com.hotniao.livelibrary.biz.tencent.push.HnTXPushLiveManager;
import com.hotniao.livelibrary.util.HnLiveUtils;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：腾讯直播拉流控制类
 * 创建人：mj
 * 创建时间：2017/10/11 17:12
 * 修改人：Administrator
 * 修改时间：2017/10/11 17:12
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTxPullLiveManager implements HnTXPullLiveStateSubject, ITXLivePlayListener {


    private String TAG = "HnTxPullLiveManager";

    /**
     * 观察者接口集合
     */
    private List<HnTXPullLiveObserver> list = new ArrayList<>();
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 拉流地址
     */
    private String mPullUrl;
    /**
     * 拉流对象
     */
    private TXLivePlayer mLivePlayer;
    private TXLivePlayConfig mPlayConfig;
    /**
     * 定时器  一秒钟检测一侧网络状态
     */
    private Disposable mNetWorkStateObserver;
    /**
     * 网络状态
     */
    private int netWorkState = HnNetUtil.NETWORK_NONE;
    /**
     * 电话监听器
     */
    private TXPhoneStateListener mPhoneListener;

    private HnTxPullLiveManager() {

    }

    public static HnTxPullLiveManager getInstance() {
        return HnTxPullLiveManagerHolder.instance;
    }

    /**
     * 初始化
     *
     * @param context  上下文
     * @param mPullUrl 拉流地址
     */
    public HnTxPullLiveManager init(Context context, String mPullUrl, TXCloudVideoView mVideoView) {
        this.mContext = context;
        this.mPullUrl = mPullUrl;
        if (mContext != null) {
            //设置拉流相关属性
            setPullConfig();
            //网络监测定时器
            checkNetWorkStateTimeTask();
        }
        if (mLivePlayer != null && mVideoView != null) {
            //关键player对象与界面view
            mLivePlayer.setPlayerView(mVideoView);
        }
        return getInstance();
    }

    /**
     * 设置拉流相关属性
     */
    private void setPullConfig() {
        //创建player对象
        mLivePlayer = new TXLivePlayer(mContext);
        mPlayConfig = new TXLivePlayConfig();
        //极速模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);


        //设置播放器重连间隔. 秒
        mPlayConfig.setConnectRetryInterval(10);
        mPlayConfig.setConnectRetryCount(5);
        mLivePlayer.setConfig(mPlayConfig);
        mLivePlayer.setPlayListener(this);
        //电话监听
        mPhoneListener = new TXPhoneStateListener(mLivePlayer);
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 开始拉流
     */
    public HnTxPullLiveManager startPullLive() {
        if (mLivePlayer == null) {
            HnLogUtils.i(TAG, "拉流对象为空,无法进行拉流，请先初始化");
            notifyObserversToPullLiveFail(0, "拉流对象为空,无法进行拉流，请先初始化", null);
            return getInstance();
        }
        if (mLivePlayer != null) {
            if (TextUtils.isEmpty(mPullUrl)) {
                HnLogUtils.i(TAG, "拉流地址为空,无法进行拉流");
                notifyObserversToPullLiveFail(1, "拉流地址为空,无法进行拉流", null);
                return getInstance();
            }
            mLivePlayer.startPlay(mPullUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐FLV
        }
        return getInstance();
    }

    /**
     * 推流暂停
     */
    public void onPullPause() {
        if (mLivePlayer != null) {
            mLivePlayer.pause();
        }
    }

    /**
     * 拉流继续
     */
    public void onPullResume() {
        if (mLivePlayer != null) {
            mLivePlayer.resume();
        }
        HnLogUtils.i(TAG, "拉流继续");
    }

    /**
     * 拉流继续
     */
    public void onPullResume(TXCloudVideoView mVideoView) {
        if (mLivePlayer != null) {
            mLivePlayer.resume();

//            if(mVideoView!=null) {
//                mVideoView.onDestroy();
//            }
            if (mLivePlayer != null && mVideoView != null) {
                //关键player对象与界面view
//                mLivePlayer.setPlayerView(mVideoView);
//                startPullLive();
            }
        }
        HnLogUtils.i(TAG, "拉流继续");
    }

    /**
     * 停止拉流
     */
    private void stopPull() {
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(false);
        }
        HnLogUtils.i(TAG, "停止拉流");
    }

    /**
     * 结束界面，推流结束
     */
    public void onPullDestory(TXCloudVideoView mVideoView) {
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true); // true代表清除最后一帧画面
            mLivePlayer.setPlayListener(null);
        }
        if (mVideoView != null) {
            mVideoView.onDestroy();
        }
        HnLogUtils.i(TAG, "结束界面，推流结束");
    }

    /**
     * 检测网络的定时器
     */
    private void checkNetWorkStateTimeTask() {
        mNetWorkStateObserver = Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int currentNetWorkState = HnNetUtil.getNetWorkState(mContext);
                        if (netWorkState != currentNetWorkState) {
                            netWorkState = currentNetWorkState;
                            notifyObserversToNetWorkState(netWorkState);
                        }
                        if (netWorkState == HnNetUtil.NETWORK_WIFI) {
                            HnLogUtils.i(TAG, "网络状态：wifi网络");
                        } else if (netWorkState == HnNetUtil.NETWORK_MOBILE) {
                            HnLogUtils.i(TAG, "网络状态：移动网络");
                        } else if (netWorkState == HnNetUtil.NETWORK_NONE) {
                            HnLogUtils.i(TAG, "网络状态：无网络");
                        }

                    }
                });

    }

    /**
     * 关闭网络监测定时器
     */
    private void closeNetworkStateTimeTask() {
        if (mNetWorkStateObserver != null) {
            mNetWorkStateObserver.dispose();
        }
    }

    /**
     * 判定是否在前台工作
     */
    public boolean isRunningForeground(Activity mActivity) {
        ActivityManager mActivityivityManager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = mActivityivityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(mActivity.getApplicationInfo().processName)) {
                    HnLogUtils.i(TAG, "当前界面处于前台");
                    return true;
                }
            }
        }
        HnLogUtils.i(TAG, " 当前界面处于后台");
        return false;
    }

    @Override
    public void attach(HnTXPullLiveObserver observer) {
        if (observer == null) return;
        list.add(observer);
    }

    @Override
    public void detach(HnTXPullLiveObserver observer) {
        if (list.contains(observer)) {
            list.remove(observer);
        }
        //关闭网络监测定时器
        closeNetworkStateTimeTask();
    }

    @Override
    public void notifyObserversToPullLiveIng() {
        for (HnTXPullLiveObserver observerListener : list) {
            observerListener.onStartPullLiveIng();
        }
    }

    @Override
    public void notifyObserversToPullLiveFail(int code, String type, Object object) {
        for (HnTXPullLiveObserver observerListener : list) {
            observerListener.onPulliveFail(code, type, object);
        }
    }

    @Override
    public void notifyObserversToPullSuccess(int code, String type, Object object) {
        for (HnTXPullLiveObserver observerListener : list) {
            observerListener.onPullSuccess(code, type, object);
        }
    }

    @Override
    public void notifyObserversToNetWorkState(int state) {
        for (HnTXPullLiveObserver observerListener : list) {
            observerListener.onNetWorkState(state);
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle bundle) {
        HnLogUtils.e(TAG, "onPlayEvent :" + event);
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {//视频播放开始，如果有转菊花什么的这个时候该停了
            notifyObserversToPullSuccess(event, "onPlayEvent", bundle);
            HnLogUtils.i(TAG, "视频播放开始，如果有转菊花什么的这个时候该停了");

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {//视频播放进度，会通知当前进度和总体进度，仅在点播时有效

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {//视频播放loading，如果能够恢复，之后会有BEGIN事件
            notifyObserversToPullLiveFail(event, "onPlayEvent", bundle);
            HnLogUtils.i(TAG, "视视频播放loading，如果能够恢复，之后会有BEGIN事件");

            //网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放   视频播放结束(点播有效)
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPull();
            notifyObserversToPullLiveFail(event, "onPlayEvent", bundle);
            HnLogUtils.i(TAG, "网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放   视频播放结束(点播有效)");

        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {//网络接收到首个可渲染的视频数据包(IDR)
            HnLogUtils.i(TAG, "网络接收到首个可渲染的视频数据包(IDR)");
            notifyObserversToPullSuccess(event, "onPlayEvent", bundle);


        } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {

        } else if (event == TXLiveConstants.PLAY_WARNING_RECV_DATA_LAG) {//网络来包不稳：可能是下行带宽不足，或由于主播端出流不均匀
            notifyObserversToPullLiveFail(event, "onPlayEvent", bundle);
            HnLogUtils.i(TAG, "网络来包不稳：可能是下行带宽不足，或由于主播端出流不均匀");

        } else if (event == TXLiveConstants.PLAY_WARNING_VIDEO_PLAY_LAG) {//当前视频播放出现卡顿
            notifyObserversToPullLiveFail(event, "onPlayEvent", bundle);
            HnLogUtils.i(TAG, "当前视频播放出现卡顿");
        }

    }

    //***********************拉流回调监听  start***************************************************************************/

    @Override
    public void onNetStatus(Bundle bundle) {
        String msg = HnLiveUtils.getNetStatusString(bundle);
        HnLogUtils.i(TAG, "onNetStatus:" + msg);
    }

    public static class HnTxPullLiveManagerHolder {
        public static HnTxPullLiveManager instance = new HnTxPullLiveManager();
    }

    //***********************拉流回调监听  end***************************************************************************/

    static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePlayer> mPlayer;

        public TXPhoneStateListener(TXLivePlayer player) {
            mPlayer = new WeakReference<TXLivePlayer>(player);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePlayer player = mPlayer.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (player != null) player.setMute(true);
                    HnLogUtils.i("HnTxPullLiveManager", "电话等待接听  静音播放");
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (player != null) player.setMute(true);
                    HnLogUtils.i("HnTxPullLiveManager", "电话电话接听 静音播放");
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (player != null) player.setMute(false);
                    HnLogUtils.i("HnTxPullLiveManager", "电话挂机 非静音播放");
                    break;
            }
        }
    }
}
