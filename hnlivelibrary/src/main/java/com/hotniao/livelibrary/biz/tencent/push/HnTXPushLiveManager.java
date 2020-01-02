package com.hotniao.livelibrary.biz.tencent.push;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnNetUtil;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.tencent.pull.HnTXPushLiveBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveBeautyEvent;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.beauty.BeautyDialogFragment;
import com.hotniao.livelibrary.ui.beauty.utils.HnBeautyUtils;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.tencent.rtmp.TXLiveConstants.PUSH_EVT_CONNECT_SUCC;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：腾讯直播推流控制类
 * 创建人：mj
 * 创建时间：2017/10/11 10:26
 * 修改人：Administrator
 * 修改时间：2017/10/11 10:26
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTXPushLiveManager implements ITXLivePushListener, HnTXPushLiveStateSubject {


    private String TAG = "HnTXPushLiveManager";
    /**
     * 观察者接口集合
     */
    private List<HnTXPushLiveObserver> list = new ArrayList<>();
    /**
     * 上下文
     */
    private Context context;
    /**
     * 推流地址
     */
    private String pushUrl;
    /**
     * 推流云平台
     */
    private String platform;

    /**
     * 推流对象
     */
    private TXLivePusher mLivePusher;
    /**
     * 推流参数配置
     */
    private TXLivePushConfig mLivePushConfig;
    /**
     * 直播推流相关操作
     */
    private HnTXPushLiveBiz mHnTXPushLiveBiz;
    /**
     * 定时器  一秒钟检测一侧网络状态
     */
    private Disposable mNetWorkStateObserver;
    /**
     * 网络状态
     */
    private int netWorkState = HnNetUtil.NETWORK_NONE;
    /**电话监听器*/
    private  TXPhoneStateListener mPhoneListener;


    private HnTXPushLiveManager() {

    }

    public static class HnTXPushLiveManagerHolder {
        private static HnTXPushLiveManager instance = new HnTXPushLiveManager();
    }

    public static HnTXPushLiveManager getInstance() {
        return HnTXPushLiveManagerHolder.instance;
    }

    /**
     * 初始化
     *
     * @param context  上下文
     * @param pushUrl  推流地址
     * @param platform 推流云平台
     */
    public HnTXPushLiveManager init(Context context, String pushUrl, String platform, TXCloudVideoView view) {
        this.context = context;
        this.pushUrl = pushUrl;
        this.platform = platform;
        //网络监测定时器
        checkNetWorkStateTimeTask();
        //初始化腾讯推流api处理对象
        mHnTXPushLiveBiz = HnTXPushLiveBiz.getInstance();
        //初始化推流对象
        mLivePusher = new TXLivePusher(context);
        mLivePusher.setPushListener(this);
        //设置推流默认配置，当然你也可以自定义配置
        //开启预览

        // 300 为后台播放暂停图片的最长持续时间,单位是秒
        // 10 为后台播放暂停图片的帧率,最小值为5,最大值为20
//        Bitmap bitmap = mHnTXPushLiveBiz.decodeResource(context.getResources(), R.drawable.default_live);
        TXLivePushConfig config = new TXLivePushConfig();
//        config.setVideoResolution(640 * 360);
        config.setVideoBitrate(700);
        config.setAutoAdjustBitrate(false);
        mLivePusher.setVideoQuality(VIDEO_QUALITY_STANDARD_DEFINITION,false,false);
        mLivePushConfig = mHnTXPushLiveBiz.setTXLivePushConfig(mLivePusher, config,platform,null);
        //开启预览
        startCameraPreview(view);
        //电话监听
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        return getInstance();
    }


    /**
     * 电话监听
     */
    static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;
        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<TXLivePusher>(pusher);
        }
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch(state){
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (pusher != null) pusher.pausePusher();
                    HnLogUtils.i("HnTXPushLiveManager","电话等待接听 暂停推流");
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null) pusher.pausePusher();
                    HnLogUtils.i("HnTXPushLiveManager","电话电话接听 暂停推流");
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null) pusher.resumePusher();
                    HnLogUtils.i("HnTXPushLiveManager","电话挂机 重新推流");
                    break;
            }
        }
    };

    /**
     * 设置推流配置  若想使用自定义配置，请传入config对象。
     *
     * @param config 视频参数配置
     */
    public HnTXPushLiveManager setTXLivePushConfig(TXLivePushConfig config) {
        if (mHnTXPushLiveBiz != null) {
//            mLivePushConfig = mHnTXPushLiveBiz.setTXLivePushConfig(mLivePusher, config, platform);
        }
        return getInstance();
    }

    /**
     * 开启预览
     *
     * @param view
     */
    public void startCameraPreview(TXCloudVideoView view) {
        //将界面元素和Pusher对象关联起来，从而能够将手机摄像头采集到的画面渲染到屏幕上。
        mLivePusher.startCameraPreview(view);
    }

    /**
     * 开始推流
     */
    public HnTXPushLiveManager startPush() {
        if (mHnTXPushLiveBiz != null) {
            notifyObserversToPushLiveIng();
            mHnTXPushLiveBiz.startPush(mLivePusher, pushUrl, mLivePushConfig);
            HnLogUtils.i(TAG, "开始直播推流");
        }
        return getInstance();
    }

    /**
     * 自定义设定清晰度
     *
     * @param qulity           SDK 提供了六种基础档位，根据我们服务大多数客户的经验进行积累和配置。
     * @param adjustBitrate    是否开启 Qos 流量控制，
     * @param adjustResolution 是否允许动态分辨率，
     */
    public void setVideoQuality(int qulity, boolean adjustBitrate, boolean adjustResolution) {
        if (mHnTXPushLiveBiz != null) {
            mHnTXPushLiveBiz.setVideoQuality(mLivePusher, qulity, adjustBitrate, adjustResolution);
        }
    }

    /**
     * 切换摄像头 默认是前置
     *
     * @param isFont 是否是前置
     * @return
     */
    public boolean switchCamera(boolean isFont) {
        if (mHnTXPushLiveBiz != null) {
            isFont = mHnTXPushLiveBiz.switchCamera(mLivePusher, isFont);
        }
        return isFont;
    }

    /**
     * 设置美颜
     *
     * @param isOpen true:打开美颜   false:关闭美颜
     */
    public boolean setBeautyFilter(boolean isOpen) {
        if (mHnTXPushLiveBiz != null) {
            boolean flag = isOpen;
            if (isOpen) {//关闭美颜
                flag = mHnTXPushLiveBiz.setBeautyFilter(mLivePusher, 0, 0, 0, 0);
            } else {//打开美颜
                flag = mHnTXPushLiveBiz.setBeautyFilter(mLivePusher, 1, 8, 7, 4);
            }
            if (flag) {
                isOpen = !isOpen;
            }
        }
        return isOpen;
    }

    public void setBeautifore(HnLiveBeautyEvent event){
        if(BeautyDialogFragment.BEAUTYPARAM_BEAUTY==event.getType()||BeautyDialogFragment.BEAUTYPARAM_WHITE==event.getType()
                ||BeautyDialogFragment.BEAUTYPARAM_RUDDY==event.getType()){
            if (mLivePusher != null) {
                mLivePusher.setBeautyFilter(1,event.getParams().mBeautyProgress, event.getParams().mWhiteProgress,event.getParams().mRuddyProgress);
            }
        }else if(BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT==event.getType()){
            if (mLivePusher != null) {
                mLivePusher.setFaceSlimLevel(event.getParams().mFaceLiftProgress);
            }
        }else if(BeautyDialogFragment.BEAUTYPARAM_BIG_EYE==event.getType()){
            if (mLivePusher != null) {
                mLivePusher.setEyeScaleLevel(event.getParams().mBigEyeProgress);
            }
        }else if(BeautyDialogFragment.BEAUTYPARAM_FILTER==event.getType()){
            if (mLivePusher != null) {
                mLivePusher.setFilter(HnBeautyUtils.getFilterBitmap(context.getResources(), event.getParams().mFilterIdx));
            }
        }   else if( BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL==event.getType()) {

            if (mLivePusher != null) {
                mLivePusher.setMotionTmpl(event.getParams().mMotionTmplPath);
            }
        }else if(BeautyDialogFragment.BEAUTYPARAM_GREEN==event.getType()){
            if (mLivePusher != null){
                mLivePusher.setGreenScreenFile(HnBeautyUtils.getGreenFileName(event.getParams().mGreenIdx));
            }
        }

    }


    /**
     * 停止rtmp推流
     */
    public void stopPublishRtmp() {
        if (mHnTXPushLiveBiz != null) {
            mHnTXPushLiveBiz.stopPublishRtmp(mLivePusher);
            HnLogUtils.i(TAG, "停止推流");
        }

    }

    /**
     * 当界面销毁时，结束rtmp推流
     */
    public void destoryPublishRtmp(TXCloudVideoView view) {
        if (mHnTXPushLiveBiz != null) {
            mHnTXPushLiveBiz.destoryPublishRtmp(mLivePusher, mLivePushConfig, view);
            HnLogUtils.i(TAG, "结束推流");
        }

    }

    // activity 的 onStop 生命周期函数
    public void onStop(TXCloudVideoView mCaptureView) {
        if (mHnTXPushLiveBiz != null) {
            mHnTXPushLiveBiz.onStop(mLivePusher, mCaptureView);
            HnLogUtils.i(TAG, "停止直播推流");
        }
    }

    // activity 的 onStop 生命周期函数
    public void onResume(TXCloudVideoView mCaptureView) {
        if (mHnTXPushLiveBiz != null) {
            mHnTXPushLiveBiz.onResume(mLivePusher, mCaptureView);
        }
    }


    @Override
    public void onPushEvent(int event, Bundle param) {
        HnLogUtils.e(TAG, "LivePublisherActivity :" + event);
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        mHnTXPushLiveBiz.appendEventLog(event, msg);
        //打开摄像头失败    打开麦克风失败
        if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            stopPublishRtmp();
            notifyObserversToPushLiveFail(event, "onPushEvent", null);
            HnLogUtils.i(TAG, "开摄像头失败    打开麦克风失败");
            //网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流
        } else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
            stopPublishRtmp();
            notifyObserversToPushLiveFail(event, "onPushEvent", null);
            HnLogUtils.i(TAG, "网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流");
        } else if (event == TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_UNSURPORT) {
            notifyObserversToPushLiveFail(event, "onPushEvent", null);
            stopPublishRtmp();

        } else if (event == TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_START_FAILED) {
            notifyObserversToPushLiveFail(event, "onPushEvent", null);
            stopPublishRtmp();

        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_RESOLUTION) {

        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_BITRATE) {


            //网络状况不佳：上行带宽太小，上传数据受阻
        } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            HnLogUtils.i(TAG, "网络状况不佳：上行带宽太小，上传数据受阻");
            notifyObserversToPushLiveFail(event, "onPushEvent", null);
            //网络断连, 已启动自动重连 (自动重连连续失败超过三次会放弃)
        } else if (event == TXLiveConstants.PUSH_WARNING_RECONNECT) {
            HnLogUtils.i(TAG, "网络断连, 已启动自动重连 (自动重连连续失败超过三次会放弃)");//TODO 1
            notifyObserversToPushLiveFail(event, "onPushEvent", null);


            //硬编码启动失败，采用软编码
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mLivePusher.setConfig(mLivePushConfig);
            notifyObserversToPushLiveFail(event, "onPushEvent", null);
            HnLogUtils.i(TAG, "硬编码启动失败，采用软编码");


        } else if (event == TXLiveConstants.PUSH_EVT_START_VIDEO_ENCODER) {
            int encType = param.getInt(TXLiveConstants.EVT_PARAM1);

            //已经成功连接到腾讯云推流服务器
        } else if (event == PUSH_EVT_CONNECT_SUCC) {
            HnLogUtils.i(TAG, "已经成功连接到腾讯云推流服务器");
            //与服务器握手完毕,一切正常，准备开始推流
        } else if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            notifyObserversToPushSuccess(event, "onPushEvent", null);
            HnLogUtils.i(TAG, "与服务器握手完毕,一切正常，准备开始推流");
            //推流器已成功打开摄像头（Android部分手机这个过程需要1-2秒）
        } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
            HnLogUtils.i(TAG, "推流器已成功打开摄像头（Android部分手机这个过程需要1-2秒）");
            //直播推流成功
        } else if (event == TXLiveConstants.PUSH_EVT_CONNECT_SUCC) {
            notifyObserversToPushSuccess(event, "onPushEvent", null);
            HnLogUtils.i(TAG, "直播推流成功");
        }
    }

    @Override
    public void onNetStatus(Bundle status) {
        String str = mHnTXPushLiveBiz.getNetStatusString(status);
        HnLogUtils.i(TAG, "onNetStatus:" + str);
        int anInt = status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED)/8;
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Update_Live_KBS, anInt));
//        Log.d(TAG, "Current status, CPU:"+status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE)+
//                ", RES:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH)+"*"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)+
//                ", SPD:"+status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED)+"Kbps"+
//                ", FPS:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS)+
//                ", ARA:"+status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE)+"Kbps"+
//                ", VRA:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE)+"Kbps");
    }

    /**
     * 检测网络的定时器
     */
    private void checkNetWorkStateTimeTask() {

        mNetWorkStateObserver = Observable.interval(5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int currentNetWorkState = HnNetUtil.getNetWorkState(context);
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
        try {


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
        } catch (Exception e) {
        }
        return false;
    }


    @Override
    public void attach(HnTXPushLiveObserver observer) {
        if (observer == null) return;
        list.add(observer);
    }

    @Override
    public void detach(HnTXPushLiveObserver observer) {
        if (list.contains(observer)) {
            list.remove(observer);
        }
        //关闭网络监测定时器
        closeNetworkStateTimeTask();
    }

    @Override
    public void notifyObserversToPushLiveIng() {
        for (HnTXPushLiveObserver observerListener : list) {
            observerListener.onStartPushLiveIng();
        }
    }

    @Override
    public void notifyObserversToPushLiveFail(int code, String type, Object object) {
        for (HnTXPushLiveObserver observerListener : list) {
            observerListener.onPushLiveFail(code, type, object);
        }
    }

    @Override
    public void notifyObserversToPushSuccess(int code, String type, Object object) {
        for (HnTXPushLiveObserver observerListener : list) {
            observerListener.onPushingSuccess(code, type, object);
        }
    }

    @Override
    public void notifyObserversToNetWorkState(int state) {
        for (HnTXPushLiveObserver observerListener : list) {
            observerListener.onNetWorkState(state);
        }
    }


}
