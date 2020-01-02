package com.hotniao.livelibrary.ui.anchor.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hn.library.base.BaseFragment;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnNetUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnDialog;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.tencent.push.HnTXPushLiveManager;
import com.hotniao.livelibrary.biz.tencent.push.HnTXPushLiveObserver;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveBeautyEvent;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.widget.HnLoadingAnimView;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.hotniao.livelibrary.R.id.fiv_header;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：主播直播间  -- 视频层
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorLiveFragment extends BaseFragment implements HnTXPushLiveObserver {


    private String TAG = "HnAnchorLiveFragment";

    /**
     * 腾讯推流控件
     */
    private TXCloudVideoView mTXCloudVideoView;
    /**
     * 遮罩ui
     */
    private FrescoImageView mFrescoImageView;


    /**
     * 推流地址
     */
    private String mLiveUrl;
    /**
     * 用户头像 做遮罩层
     */
    private String avator;
    /**
     * true 前置摄像头  false 后置摄像头
     */
    private boolean mIsFront = true;
    /**
     * 是否打开美颜
     */
    private boolean isOpenGL = false;
    /**
     * 是否停止推流，让界面处于暂停状态
     */
    private boolean mStopPush = false;

    /**
     * 推流失败
     */
    private boolean mPushFail = false;
    /**
     * 推流管理器
     */
    private HnTXPushLiveManager mHnTXPushLiveManager;

    public static HnAnchorLiveFragment newInstance(String pushUrl, String avator) {
        HnAnchorLiveFragment dialog = new HnAnchorLiveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("push_url", pushUrl);
        bundle.putString("avator", avator);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getContentViewId() {
        return R.layout.live_fragment_anchorroom_live_layout1;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //初始化布局
        initViews();
    }

    /**
     * 初始化视图布局
     */
    private void initViews() {
        mTXCloudVideoView = (TXCloudVideoView) mRootView.findViewById(R.id.video_view);
        mFrescoImageView = (FrescoImageView) mRootView.findViewById(fiv_header);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLiveUrl = bundle.getString("push_url");
            avator = bundle.getString("avator");
            HnLogUtils.i(TAG, "推流地址：" + mLiveUrl);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //使用用户头像遮罩
            mFrescoImageView.setController(FrescoConfig.getController(avator));
            //添加推流观察者
            HnTXPushLiveManager.getInstance().attach(this);//todo
            //开启推流
            mHnTXPushLiveManager = HnTXPushLiveManager.getInstance().init(mActivity, mLiveUrl, "七牛", mTXCloudVideoView).startPush();
        }
    }

    /**
     * 正在进行推流
     */
    @Override
    public void onStartPushLiveIng() {
        mPushFail = false;
        HnLogUtils.i(TAG, "正在推流");
    }

    /**
     * 推流失败
     *
     * @param code   错误码
     * @param type   类型：标识符
     * @param object 携带的数据
     */
    @Override
    public void onPushLiveFail(int code, String type, Object object) {
        HnLogUtils.i(TAG, "推流失败" + code);
        if (mActivity == null) return;
        mPushFail = true;
        //网络状况不佳：上行带宽太小，上传数据受阻
        if (code == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Push_Stream_Statue,"busyNetwork"));//TODO
            //网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流
        } else if (code == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
            if (mHnTXPushLiveManager != null && mHnTXPushLiveManager.isRunningForeground(mActivity)) {
                mHnTXPushLiveManager.startPush();
            }
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Push_Stream_Statue,"poorNetwork"));
        }
    }

    /**
     * 推流成功
     *
     * @param code   错误码
     * @param type   类型：标识符
     * @param object 携带的数据
     */
    @Override
    public void onPushingSuccess(int code, String type, Object object) {
        if (mActivity == null) return;
        mPushFail = false;
        HnLogUtils.i(TAG, "推流成功" + code);
        if (code == TXLiveConstants.PUSH_EVT_PUSH_BEGIN/*PLAY_EVT_RCV_FIRST_I_FRAME  PUSH_EVT_PUSH_BEGIN*/) {//与服务器握手完毕,一切正常，准备开始推流
            mFrescoImageView.setVisibility(View.GONE);
            HnLogUtils.i(TAG, "摄像头已经打开，开始预览");
            EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Push_Stream_Statue,"okNetwork"));
        }
    }


    /**
     * 网络状态监听回调
     *
     * @param state
     */
    @Override
    public void onNetWorkState(int state) {
        if (mActivity == null) return;
        HnLogUtils.i(TAG, "网络状态:" + state);
        if (state == HnNetUtil.NETWORK_WIFI || state == HnNetUtil.NETWORK_MOBILE) {
            if (mPushFail) {
                if (mHnTXPushLiveManager != null && mHnTXPushLiveManager.isRunningForeground(mActivity))
                    mHnTXPushLiveManager.startPush();
            }
        } else if (state == HnNetUtil.NETWORK_NONE) {
            if (mActivity == null) return;
            if (mHnTXPushLiveManager != null && mHnTXPushLiveManager.isRunningForeground(mActivity)) {
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Push_Stream_Statue,"poorNetwork"));
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if(mHnTXPushLiveManager!=null){
//            mHnTXPushLiveManager.onResume(mTXCloudVideoView);
//        }
        HnLogUtils.i(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
//        if(mHnTXPushLiveManager!=null){
//            mHnTXPushLiveManager.onStop(mTXCloudVideoView);
//        }
        HnLogUtils.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHnTXPushLiveManager != null) {
            mHnTXPushLiveManager.destoryPublishRtmp(mTXCloudVideoView);
        }
        HnTXPushLiveManager.getInstance().detach(this);
        EventBus.getDefault().unregister(this);
        HnLogUtils.i(TAG, "主播直播间走到onDestory中");
    }

    /**
     * 专注于直播间的eventbus事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(HnLiveEvent event) {
        if (event != null) {
            if (HnLiveConstants.EventBus.Switch_Camera.equals(event.getType())) {//摄像头切换
                if (mHnTXPushLiveManager != null) {
                    mIsFront = mHnTXPushLiveManager.switchCamera(mIsFront);
                }
            } else if (HnLiveConstants.EventBus.Opne_GL.equals(event.getType())) {//美颜切换
                if (mHnTXPushLiveManager != null) {
                    isOpenGL = mHnTXPushLiveManager.setBeautyFilter(isOpenGL);
                    if (isOpenGL) {
//                        HnToastUtils.showToastShort(getResources().getString(R.string.live_beauty_open));
                    } else {
//                        HnToastUtils.showToastShort(getResources().getString(R.string.live_beauty_close));
                    }
                }
            } else if (HnLiveConstants.EventBus.Show_Time.equals(event.getType())) {//直播时间

            } else if (HnLiveConstants.EventBus.Live_Forbidden.equals(event.getType())) {//当用户被禁播/停播/禁用帐户时，停止直播间的推拉流
                if (mHnTXPushLiveManager != null) {
                    mHnTXPushLiveManager.onStop(mTXCloudVideoView);
                    mStopPush = true;
                }
            } else if (HnLiveConstants.EventBus.Leave_Live_Room.equals(event.getType())) {// 离开直播间
                if (mHnTXPushLiveManager != null) {
                    mHnTXPushLiveManager.onStop(mTXCloudVideoView);
                    mStopPush = true;
                }
            }
        }
    }

    @Subscribe
    public void beautyEvent(HnLiveBeautyEvent event) {
        if (event != null && event.getParams() != null)
            mHnTXPushLiveManager.setBeautifore(event);
    }

}
