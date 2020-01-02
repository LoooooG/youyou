package com.hotniao.livelibrary.ui.audience.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseFragment;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnNetUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnDialog;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.tencent.pull.HnTXPullLiveObserver;
import com.hotniao.livelibrary.biz.tencent.pull.HnTxPullLiveManager;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.widget.HnLoadingAnimView;
import com.hotniao.livelibrary.widget.blur.BitmapBlur;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用户直播间  -- 视频层
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAudienceLiveFragment extends BaseFragment implements HnTXPullLiveObserver {

    public final static String TAG = "HnAudienceLiveFragment";

    /**
     * 布局
     */
    TXCloudVideoView mVideoView;
    RelativeLayout mLiveFrame;
    FrescoImageView mFivHeader;

    /**
     * 等待动画
     */
    private LinearLayout mLlLoad;
    private TextView mTvLoad;
    private HnLoadingAnimView mLoadAnim;


    /**
     * 主播头像
     */
    private String avator;
    /**
     * 直播拉流地址
     */
    private String mVideoPath; //文件路径

    /**
     * 拉流业务处理
     */
    private HnTxPullLiveManager mHnTxPullLiveManager;

    private boolean isNetBusy = false;

    public static HnAudienceLiveFragment newInstance(HnLiveListModel.LiveListBean bean) {
        HnAudienceLiveFragment fragment = new HnAudienceLiveFragment();
        Bundle b = new Bundle();
        b.putParcelable("data", bean);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.live_audience_live_fragment1;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化view
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        EventBus.getDefault().register(this);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLiveFrame = (RelativeLayout) mRootView.findViewById(R.id.live_frame_bg);
        mVideoView = (TXCloudVideoView) mRootView.findViewById(R.id.video_view);
        mFivHeader = (FrescoImageView) mRootView.findViewById(R.id.fiv_header);

        mLlLoad = (LinearLayout) mRootView.findViewById(R.id.mLlLoad);
        mTvLoad = (TextView) mRootView.findViewById(R.id.mTvLoad);
        mLoadAnim = (HnLoadingAnimView) mRootView.findViewById(R.id.mLoadAnim);


    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            HnLiveListModel.LiveListBean bean = bundle.getParcelable("data");
            if (bean != null) {
                avator = bean.getAvator();
                mVideoPath = bean.getPullUrl();
                rsetUpdateUi();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mHnTxPullLiveManager != null) {
            mHnTxPullLiveManager.onPullResume();
        }
        HnLogUtils.i(TAG, "拉流走入onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mHnTxPullLiveManager != null) {
            mHnTxPullLiveManager.onPullPause();
        }
        HnLogUtils.i(TAG, "用户主播间走入onStop");
    }

    private BroadcastReceiver mHomeReceiver = new BroadcastReceiver() {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (!TextUtils.isEmpty(reason) && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
//                    if(mHnTxPullLiveManager!=null)mHnTxPullLiveManager.onPullPause();
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        setAnimStatue(false, "");
        if (mHnTxPullLiveManager != null) {
            mHnTxPullLiveManager.onPullDestory(mVideoView);
        }
        HnTxPullLiveManager.getInstance().detach(this);
        EventBus.getDefault().unregister(this);
        HnLogUtils.i(TAG, "用户主播间走入onDestroy");
    }


    /**
     * 正在拉流
     */
    @Override
    public void onStartPullLiveIng() {
        mFivHeader.setVisibility(View.VISIBLE);
    }

    /**
     * 拉流失败
     *
     * @param code   错误码
     * @param type   类型：标识符
     * @param object 携带的数据
     */
    @Override
    public void onPulliveFail(int code, String type, Object object) {
        HnLogUtils.i(TAG, "拉流失败:" + code);

        //当前视频播放出现卡顿   网络来包不稳：可能是下行带宽不足，或由于主播端出流不均匀
        if (code == TXLiveConstants.PLAY_WARNING_VIDEO_PLAY_LAG || code == TXLiveConstants.PLAY_WARNING_RECV_DATA_LAG) {
            if (isNetBusy) {
                isNetBusy = true;
                HnToastUtils.showToastShort(getString(R.string.live_anchor_network_bed));
            }
        } else if (code == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {//网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放
            setAnimStatue(true, HnUiUtils.getString(R.string.live_anchor_network_bed));
            if (mHnTxPullLiveManager != null) {
                mHnTxPullLiveManager.onPullResume(mVideoView);
            }
        } else if (code == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
            if (mFivHeader != null && mFivHeader.getVisibility() != View.VISIBLE) {
                setAnimStatue(true, HnUiUtils.getString(R.string.live_anchor_network_bed));
            }
        } else {
            mFivHeader.setVisibility(View.VISIBLE);

        }

    }

    /**
     * 拉流成功
     *
     * @param code   错误码
     * @param type   类型：标识符
     * @param object 携带的数据
     */
    @Override
    public void onPullSuccess(int code, String type, Object object) {
        HnLogUtils.i(TAG, "拉流成功:" + code);
        if (code == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {//视频播放开始，如果有转菊花什么的这个时候该停了
            mFivHeader.setVisibility(View.GONE);
            setAnimStatue(false, "");
        } else if (code == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            setAnimStatue(false, "");
        }
    }

    /**
     * 网络状态
     *
     * @param state
     */
    @Override
    public void onNetWorkState(int state) {
        HnLogUtils.i(TAG, "网络状态:" + state);
        if (mActivity == null) return;
        if (state == HnNetUtil.NETWORK_WIFI || state == HnNetUtil.NETWORK_MOBILE) {
            if (mFivHeader != null && mLoadAnim == null) {
                if (mHnTxPullLiveManager != null && (View.VISIBLE == mFivHeader.getVisibility() || mLoadAnim.getAnimatorStatue())) {
                    mHnTxPullLiveManager.onPullResume(mVideoView);
                }
            }
        } else if (state == HnNetUtil.NETWORK_NONE) {
            if (mHnTxPullLiveManager != null && mHnTxPullLiveManager.isRunningForeground(mActivity)) {
                setAnimStatue(true, HnUiUtils.getString(R.string.live_audience_network_bed));
            }

        }
    }


    /**
     * 直播间eventbus回调
     *
     * @param event
     */
    @Subscribe
    public void onLiveEventBusCallBack(HnLiveEvent event) {
        if (event != null) {
            if (HnLiveConstants.EventBus.Leave_Live_Room.equals(event.getType())) {//被挤下线/用户离开
                if (mHnTxPullLiveManager != null) {
                    mHnTxPullLiveManager.onPullDestory(mVideoView);
                }

            } else if (HnLiveConstants.EventBus.Update_Room_Live.equals(event.getType())) {//房间信息
                HnLogUtils.i(TAG, "更新房间信息");
                if (mHnTxPullLiveManager != null) {
                    HnTxPullLiveManager.getInstance().detach(this);
                    mHnTxPullLiveManager.onPullDestory(mVideoView);
                }
                HnLiveListModel.LiveListBean data = (HnLiveListModel.LiveListBean) event.getObj();
                if (data != null) {
                    avator = data.getAvator();
                    mVideoPath = data.getPullUrl();
                    rsetUpdateUi();
                }
            } else if (HnLiveConstants.EventBus.Update_Room_Info.equals(event.getType())) {
                HnLiveListModel.LiveListBean data = (HnLiveListModel.LiveListBean) event.getObj();
                if (data != null) {
                    avator = data.getAvator();
                    mVideoPath = data.getPullUrl();
                    if (mActivity == null) return;
                    //设置遮罩层
                    mFivHeader.setImageURI(avator);
                    mFivHeader.setVisibility(View.VISIBLE);
                    setAnimStatue(false, "");
                }
            } else if (HnLiveConstants.EventBus.Scroll_viewPager.equals(event.getType())) {//当前主播已离开时，不在进行拉流
                boolean flag = (boolean) event.getObj();
                if (!flag && 1001 != event.getPos()) {
                    if (mHnTxPullLiveManager != null) {
                        HnTxPullLiveManager.getInstance().detach(this);
                        mHnTxPullLiveManager.onPullPause();
                    }
                }
            }

        }
    }

    /**
     * 设置等待动画状态
     *
     * @param isStart true 开启
     * @param hint    提示
     */
    private void setAnimStatue(boolean isStart, String hint) {
        if (mLlLoad == null || mTvLoad == null || mLoadAnim == null) return;
        if (isStart) {
            mTvLoad.setText(hint);
            mLoadAnim.startAnimator();
            mLlLoad.setVisibility(View.VISIBLE);
        } else {
            mTvLoad.setText("");
            mLlLoad.setVisibility(View.GONE);
            mLoadAnim.stopAnimator();
        }
    }


    public void rsetUpdateUi() {
        if (mActivity == null) return;
        //设置遮罩层
        mFivHeader.setImageURI(avator);
        mFivHeader.setVisibility(View.VISIBLE);
        setAnimStatue(false, "");
        //添加拉流观察者
        HnTxPullLiveManager.getInstance().attach(this);
        //开始拉流
        mHnTxPullLiveManager = HnTxPullLiveManager.getInstance().init(mActivity, mVideoPath, mVideoView).startPullLive();
        HnLogUtils.i(TAG, "直播间拉流地址:" + mVideoPath + "--->主播头像" + avator);
    }

}
