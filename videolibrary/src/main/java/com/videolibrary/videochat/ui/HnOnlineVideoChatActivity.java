package com.videolibrary.videochat.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDimenUtil;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.HnReceiveVideoChatBean;
import com.hotniao.livelibrary.ui.beauty.BeautyDialogFragment;
import com.hotniao.livelibrary.widget.HnLoadingAnimView;
import com.hotniao.livelibrary.widget.KeyboardLayout;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.videolibrary.R;
import com.videolibrary.util.TCUtils;
import com.videolibrary.videochat.IRTCRoomListener;
import com.videolibrary.videochat.RTCRoom;
import com.videolibrary.videochat.entity.FuzzyEvent;
import com.videolibrary.videochat.entity.PusherInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/video/HnOnlineVideoChatActivity", group = "video")
public class HnOnlineVideoChatActivity extends BaseActivity implements IRTCRoomListener, RTCRoom.PusherStreamCallback {
    private static final String TAG = "HnOnlineVideo";
    /**
     * 屏幕的宽高
     */
    int screenWidth, screenHight;
    RTCRoom mRtcRoom;
    private long lastTime;
    /**
     * 记录手指按下时在小悬浮窗的View上的坐标的值
     */
    private float xInView, yInView;
    /**
     * 记录当前手指位置在屏幕上的坐标值
     */
    private float xInScreen, yInScreen;
    /**
     * 记录手指按下时在屏幕上的坐标的值
     */
    private float xDownInScreen, yDownInScreen;
    /**
     * 记录当前leftMargin,topMargin
     */
    private float mLeftMargin, mTopMargin;
    /**
     * 美颜
     */
    private int mBeautyStyle = TXLiveConstants.BEAUTY_STYLE_SMOOTH;
    private int mBeautyLevel = 8;
    private int mWhiteningLevel = 7;
    private int mRuddyLevel = 4;
    /**
     * 小窗的大小和 距离上和右的距离
     */
    private int mSmallVideoWight = 90, mSmallVideoHeigh = 160;
    private int mMarginTop = 10, mMarginRight = 10;
    private HnOnlineVideoChatFragment mInfoFragment;
    /**
     * 等待动画
     */
    private LinearLayout mLlLoad;
    private TextView mTvLoad;
    private HnLoadingAnimView mLoadAnim;
    private ImageView mTvFuzzyMine, mIvButify, mIvFuzzy;
    private FrescoImageView mIvBg;
    private List<String> members = new ArrayList<>();
    private List<RoomVideoView> mVideoViewsVector = new ArrayList<>();
    private String userID;
    private HnReceiveVideoChatBean.DataBean roomInfo;
    private String mPushUrl;
    //是否已经接听
    private boolean isAgreeChatVideo = false;

    private FrameLayout view0, view1;
    private KeyboardLayout mVideoParent;
    //是否创建房间
    private boolean createRoom;
    private PusherInfo member;

    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();
    private boolean isView0Click = false, isView1Click = true;

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public synchronized RoomVideoView applyVideoView(String id) {

        Log.i(TAG, "applyVideoView() called with: userID = [" + id + "]");

        if (id == null || this.userID == null) {
            Log.w(TAG, "applyVideoView: member/id is null");
            return null;
        }

        for (RoomVideoView videoView : mVideoViewsVector) {
            if (!videoView.isUsed) {

                videoView.setUsed(true);
                videoView.userID = id;
                return videoView;
            } else {
                if (videoView.userID != null
                        && videoView.userID.equals(id)) {

                    videoView.setUsed(true);
                    return videoView;
                }
            }
        }
        return null;
    }

    public synchronized void recycleView(String id) {
        Log.i(TAG, "recycleView() called with: UserID = [" + id + "]");
        for (RoomVideoView V : mVideoViewsVector) {
            if (V.userID != null
                    && V.userID.equals(id)) {
                V.setUsed(false);
                V.userID = null;
            }
        }
    }

    public synchronized void recycleView() {
        for (RoomVideoView V : mVideoViewsVector) {
            Log.i(TAG, "recycleView() for remove member userID " + V.userID);
            V.setUsed(false);
            V.userID = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_online_video_chat;
    }

    @Subscribe
    public void onFuzzyEvent(FuzzyEvent event) {
        if (mIvFuzzy != null) {
            mIvFuzzy.setVisibility(event.isShow() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setShowTitleBar(false);
        mRtcRoom = new RTCRoom(this);
        mRtcRoom.setRTCRoomListener(this);
        Bundle bundle = getIntent().getExtras();
        this.roomInfo = bundle.getParcelable("roomInfo");
        this.userID = bundle.getString("userID");
        createRoom = bundle.getBoolean("createRoom");
        if (this.userID == null || roomInfo == null) {
            return;
        }
        initViews();

        RoomVideoView videoView = applyVideoView(this.userID);
        if (videoView == null) {
            return;
        }
        roomInfo.setCreate(createRoom);
        mInfoFragment = HnOnlineVideoChatFragment.newInstance(roomInfo);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mFrameInfo, mInfoFragment)
                .commitAllowingStateLoss();


        //在应用层调用startLocalPreview，启动本地预览
        mRtcRoom.startLocalPreview(videoView.view);
        try {
            mRtcRoom.setPauseImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_fast_chat_bg));
        } catch (OutOfMemoryError e) {
        }
        mRtcRoom.setBitrateRange(400, 800);
        mRtcRoom.setVideoRatio(RTCRoom.RTCROOM_VIDEO_RATIO_9_16);
        mRtcRoom.setHDAudio(true);
        mRtcRoom.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
        if (mIvButify != null) mIvButify.setVisibility(View.GONE);
        if (!createRoom) {

            mPushUrl = roomInfo.getPushUrl();
            mRtcRoom.createRoom(roomInfo.getF_user_nickname(), roomInfo.getF_user_id(), userID, roomInfo.getPushUrl(), new RTCRoom.CreateRoomCallback() {
                @Override
                public void onSuccess(String chatLog) {
                    if (isFinishing()) return;
                    if (mInfoFragment != null) mInfoFragment.setChatLog(roomInfo.getChat_log());
                }

                @Override
                public void onError(int errCode, String e) {
                    HnToastUtils.showToastShort(e);
                    if (mInfoFragment != null) mInfoFragment.setCancleChat();
                }
            });
        }
    }

    @Override
    public void getInitData() {

    }

    private void initViews() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHight = getScreenHeight(this) - getStatusHeight(this);

        mTvFuzzyMine = findViewById(R.id.mTvFuzzyMine);
        mIvFuzzy = findViewById(R.id.mIvFuzzy);
        mIvBg = findViewById(R.id.mIvBg);
        mIvBg.setController(FrescoConfig.getBlurController(roomInfo.getF_user_avatar()));
        if (createRoom) mIvBg.setVisibility(View.VISIBLE);
        else mIvBg.setVisibility(View.GONE);

        mLoadAnim = (HnLoadingAnimView) findViewById(com.hotniao.livelibrary.R.id.mLoadAnim);
        mLlLoad = (LinearLayout) findViewById(com.hotniao.livelibrary.R.id.mLlLoad);
        mTvLoad = (TextView) findViewById(com.hotniao.livelibrary.R.id.mTvLoad);

        mVideoParent = findViewById(R.id.mVideoParent);
        view0 = findViewById(R.id.video_view_0);
        view1 = findViewById(R.id.video_view_1);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view1
                .getLayoutParams();
        params1.leftMargin = (screenWidth - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight + mMarginRight));
        params1.topMargin = (HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mMarginTop));


        final TXCloudVideoView views[] = new TXCloudVideoView[6];
        views[0] = ((TXCloudVideoView) findViewById(R.id.rtmproom_video_0));
        views[1] = ((TXCloudVideoView) findViewById(R.id.rtmproom_video_1));

        for (int i = 0; i < 2; i++) {
            mVideoViewsVector.add(new RoomVideoView(views[i], null));
        }
//        setTouchListenser();
        setOnClick();
        mIvButify = findViewById(R.id.mIvButify);
        mIvButify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mBeautyDialogFragment.isAdded()) {
                        mBeautyDialogFragment.dismiss();
                    } else {
                        mBeautyDialogFragment.show(getFragmentManager(), "");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBeautyDialogFragment = new BeautyDialogFragment();
        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, new BeautyDialogFragment.OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
                switch (key) {
                    case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
                    case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                    case BeautyDialogFragment.BEAUTYPARAM_RUDDY:
                        if (mRtcRoom != null) {
                            mRtcRoom.setBeautyFilter(mBeautyStyle, params.mBeautyProgress, params.mWhiteProgress, params.mRuddyProgress);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT:
                        if (mRtcRoom != null) {
                            mRtcRoom.setFaceVLevel(params.mFaceLiftProgress);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_BIG_EYE:
                        if (mRtcRoom != null) {
                            mRtcRoom.setEyeScaleLevel(params.mBigEyeProgress);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                        if (mRtcRoom != null) {
                            mRtcRoom.setFilter(TCUtils.getFilterBitmap(getResources(), params.mFilterIdx));
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL:
                        if (mRtcRoom != null) {
                            mRtcRoom.setMotionTmpl(params.mMotionTmplPath);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_GREEN:
                        if (mRtcRoom != null) {
                            mRtcRoom.setGreenScreenFile(TCUtils.getGreenFileName(params.mGreenIdx));
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void dismiss() {
            }
        });
    }

    /**
     * 设置是否模糊请求者
     *
     * @param isFuzzy
     */
    private void setMineFuzzy(boolean isFuzzy) {
        if (mTvFuzzyMine == null) return;
        if (isFuzzy) mTvFuzzyMine.setVisibility(View.VISIBLE);
        else mTvFuzzyMine.setVisibility(View.GONE);
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

    /**
     * 自己的变成小框
     */
    private void setMyWindomSmall() {
        if (mVideoParent == null || view0 == null || view1 == null) return;
        isView0Click = true;
        isView1Click = false;
        mVideoParent.removeView(view1);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view1
                .getLayoutParams();
        params1.leftMargin = (screenWidth - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight)) / 2;
        params1.topMargin = (screenHight - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh)) / 2;

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view1, "scaleX", 1f,
                ((float) screenWidth / (float) HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight)),
                (float) screenWidth / (float) HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight));
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view1, "scaleY", 1f,
                (float) screenHight / (float) HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh),
                (float) screenHight / (float) HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh));


        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view1, "alpha", 0f, 1f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
        set.setDuration(10);
        set.start();


        view0.setLayoutParams(new RelativeLayout.LayoutParams(HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight),
                HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh)));
        RelativeLayout.LayoutParams params0 = (RelativeLayout.LayoutParams) view0
                .getLayoutParams();
        //设置固定位置
        params0.leftMargin = (screenWidth - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight + mMarginRight));
        params0.topMargin = (HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mMarginTop));

        //可拖动小窗口时设置的位置
//        params0.leftMargin = (int) mLeftMargin;
//        params0.topMargin = (int) mTopMargin;

        mVideoParent.addView(view1, 0);
    }

    /**
     * 自己的变成大框
     */
    private void setMyWindomBig() {
        if (mVideoParent == null || view0 == null || view1 == null) return;
        isView0Click = false;
        isView1Click = true;

        mVideoParent.removeView(view1);
        view0.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view1, "scaleX",
                (float) screenWidth / (float) HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight),
                1f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view1, "scaleY",
                (float) screenHight / (float) HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh),
                1f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view1, "alpha", 0f, 1f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
        set.setDuration(10);
        set.start();

        view1.setLayoutParams(new RelativeLayout.LayoutParams(HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight),
                HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh)));
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view1
                .getLayoutParams();
        params1.leftMargin = (screenWidth - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoWight + mMarginRight));
        params1.topMargin = (HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mMarginTop));

//        params1.leftMargin = (int) mLeftMargin;
//        params1.topMargin = (int) mTopMargin;
        mVideoParent.addView(view1, 1);
    }

    private void setOnClick() {
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HnLogUtils.d(TAG,"isAgreeChatVideo:"+isAgreeChatVideo+",isisView1Click:"+isView1Click);
                if (!isAgreeChatVideo) return;
                if (!isView1Click) return;
                if (System.currentTimeMillis() - lastTime < 800) return;
                lastTime = System.currentTimeMillis();
                setMyWindomSmall();

            }
        });
        view0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAgreeChatVideo) return;
                if (!isView0Click) return;
                if (System.currentTimeMillis() - lastTime < 800) return;
                lastTime = System.currentTimeMillis();
                setMyWindomBig();
            }
        });
    }

    /**
     * 移动小框  切换大小框
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListenser() {


        view1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (!isView1Click) return true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xInView = event.getX();
                        yInView = event.getY();
                        xDownInScreen = event.getRawX();
                        yDownInScreen = event.getRawY();
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();
                        // 手指移动的时候更新小悬浮窗的位置
                        //排除某些手机按下抬起会走move 的问题
                        if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop()
                                && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop())
                            return true;

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();

                        if ((int) (xInScreen - xInView) < 2) {
                            params.leftMargin = 2;
                        } else if ((int) (xInScreen - xInView) < (screenWidth - view.getWidth())) {
                            params.leftMargin = (int) (xInScreen - xInView);
                        } else {
                            params.leftMargin = (screenWidth - view.getWidth()) - 2;
                        }

                        if ((int) (yInScreen - yInView) < 2) {
                            params.topMargin = 2;
                        } else if ((int) (yInScreen - yInView) < (screenHight - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh + 1))) {
                            params.topMargin = (int) (yInScreen - yInView);
                        } else {
                            params.topMargin = (screenHight - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh + 1));
                        }
                        mLeftMargin = params.leftMargin;
                        mTopMargin = params.topMargin;
                        view.setLayoutParams(params);
                        break;
                    case MotionEvent.ACTION_UP:
                        //点击效果
                        if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop()
                                && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop()) {
                            if (System.currentTimeMillis() - lastTime < 800) {
                                return true;
                            }
                            lastTime = System.currentTimeMillis();


                            setMyWindomSmall();

                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        view0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (!isView0Click) return true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xInView = event.getX();
                        yInView = event.getY();
                        xDownInScreen = event.getRawX();
                        yDownInScreen = event.getRawY();
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();
                        // 手指移动的时候更新小悬浮窗的位置
                        //排除某些手机按下抬起会走move 的问题
                        if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop()
                                && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop())
                            return true;


                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();

                        if ((int) (xInScreen - xInView) < 2) {
                            params.leftMargin = 2;
                        } else if ((int) (xInScreen - xInView) < (screenWidth - view.getWidth())) {
                            params.leftMargin = (int) (xInScreen - xInView);
                        } else {
                            params.leftMargin = (screenWidth - view.getWidth()) - 2;
                        }

                        if ((int) (yInScreen - yInView) < 2) {
                            params.topMargin = 2;
                        } else if ((int) (yInScreen - yInView) < (screenHight - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh + 1))) {
                            params.topMargin = (int) (yInScreen - yInView);
                        } else {
                            params.topMargin = (screenHight - HnDimenUtil.dp2px(HnOnlineVideoChatActivity.this, mSmallVideoHeigh + 1));
                        }
                        mLeftMargin = params.leftMargin;
                        mTopMargin = params.topMargin;
                        view.setLayoutParams(params);
                        break;
                    case MotionEvent.ACTION_UP:
                        //点击效果
                        if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop()
                                && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(HnOnlineVideoChatActivity.this).getScaledTouchSlop()) {
                            if (System.currentTimeMillis() - lastTime < 800) {
                                return true;
                            }
                            lastTime = System.currentTimeMillis();


                            setMyWindomBig();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mRtcRoom.switchToForeground();
    }

    @Override
    public void onPause() {

        super.onPause();

        mRtcRoom.switchToBackground();
    }

    @Override
    public void onDestroy() {
        mRtcRoom.exitRoom(new RTCRoom.ExitRoomCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "exitRoom Success");
            }

            @Override
            public void onError(int errCode, String e) {
                Log.e(TAG, "exitRoom failed, errorCode = " + errCode + " errMessage = " + e);
            }
        });
        EventBus.getDefault().unregister(this);
        recycleView();
        setAnimStatue(false, "");
        mVideoViewsVector.clear();
        mRtcRoom.stopLocalPreview();
        super.onDestroy();
    }

    /**
     * call by activity
     */
    @Override
    public void onBackPressed() {
        mRtcRoom.exitRoom(new RTCRoom.ExitRoomCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "exitRoom Success");
            }

            @Override
            public void onError(int errCode, String e) {
                Log.e(TAG, "exitRoom failed, errorCode = " + errCode + " errMessage = " + e);
            }
        });

        recycleView();
        finish();
    }

    @Override
    public void onPusherJoin(PusherInfo member) {

        if (member == null || member.userID == null) {
            Log.w(TAG, "onPusherJoin: member or memeber id is null");
            return;
        }
        this.member = member;
        RoomVideoView V = applyVideoView(member.userID);
        if (V == null) return;
        mRtcRoom.addRemoteView(V.view, member); //开启远端视频渲染

    }

    @Override
    public void onPusherQuit(PusherInfo pusherInfo) {
//        mRtcRoom.delRemoteView(pusherInfo);//关闭远端视频渲染
//        recycleView(pusherInfo.userID);
        if (mInfoFragment != null) {
            mInfoFragment.setChatOverMine();
        }
    }

    @Override
    public void onRoomClosed(String roomId) {
        boolean createRoom = getIntent().getExtras().getBoolean("createRoom");
        if (createRoom == false) {
//            onBackPressed();
        }

    }

    @Override
    public void onRecvRoomTextMsg(String msg) {

    }

    @Override
    public void onVideoPlayEvent(int event) {
        if (isFinishing()) return;

        if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {//获取到首个
            setMyWindomSmall();
            setAnimStatue(false, "");
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {//视频播放开始，如果有转菊花什么的这个时候该停了
            setAnimStatue(false, "");
        } else if (event == TXLiveConstants.PLAY_WARNING_VIDEO_PLAY_LAG || event == TXLiveConstants.PLAY_WARNING_RECV_DATA_LAG) {
            //当前视频播放出现卡顿   网络来包不稳：可能是下行带宽不足，或由于主播端出流不均匀
            HnToastUtils.showToastShort("当前通话质量不佳");
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {//网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放
            setAnimStatue(true, "当前通话质量不佳");
            onPusherJoin(member);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
            setAnimStatue(true, "当前通话质量不佳");
        }

    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        if (errorCode == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {

            if (mRtcRoom != null && !TextUtils.isEmpty(mPushUrl))
                mRtcRoom.startPushStream(mPushUrl, null);
        } else
            HnToastUtils.showToastShort(errorMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatVideoStatueChange(HnReceiveVideoChatBean bean) {
        if (isFinishing()) return;
        if (bean == null) return;
        if (HnWebscoketConstants.Private_Chat_Cancel.equals(bean.getType())) {
            HnToastUtils.showToastShort(roomInfo.getF_user_nickname() + "已取消视频邀请");
            finish();
        } else if (HnWebscoketConstants.Private_Chat_Refuse.equals(bean.getType())) {
            HnToastUtils.showToastShort(roomInfo.getF_user_nickname() + "已拒绝您的视频邀请");
            finish();
        } else if (HnWebscoketConstants.Private_Chat_Accept.equals(bean.getType())) {
            isAgreeChatVideo = true;
            if (mIvButify != null) mIvButify.setVisibility(View.VISIBLE);
            if (mRtcRoom != null) {
                mRtcRoom.joinVideoChatRoom(roomInfo.getF_user_id(), bean, new RTCRoom.CreateRoomCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        if (mInfoFragment != null) {
                            mInfoFragment.setChatConnectSuccess(false);
                            mInfoFragment.setHangUp();
                        }
                        HnToastUtils.showToastShort(errInfo);
                    }

                    @Override
                    public void onSuccess(String name) {
//                        roomInfo. = roomId;
                        if (mInfoFragment != null) {
                            mInfoFragment.setChatConnectSuccess(true);
                            mInfoFragment.setChatUiStatue(false);
                        }
                    }
                });
            } else {
                if (mInfoFragment != null) {
                    mInfoFragment.setChatConnectSuccess(false);
                    mInfoFragment.setHangUp();
                }
                HnToastUtils.showToastShort("聊天室未连接");
            }
        } else if (HnWebscoketConstants.Private_Chat_HangUp.equals(bean.getType())) {
            if (mInfoFragment != null) mInfoFragment.setChatOver(bean);
            else {
                HnLogUtils.e("finish_chat-222推送111--" + bean.getData().toString());
                HnOverChatVideoActivity.luncher(this, roomInfo.getF_user_avatar(), bean.getData().getDuration(), bean.getData().getAmount()
                        , bean.getData().getCoin_amount(), roomInfo.isCreate());
                finish();
            }

        } else if (HnWebscoketConstants.Private_Chat_Vague.equals(bean.getType())) {
            //	1模糊 0 取消模糊
            if ("1".equals(bean.getData().getVague())) {
                setMineFuzzy(true);
            } else {
                setMineFuzzy(false);
            }
        } else if (HnWebscoketConstants.Push_Balance.equals(bean.getType())) {
            if (mInfoFragment != null) mInfoFragment.setBalance(bean.getData().getUser_coin());
        }
    }

    @Override
    public void onSuccess() {

    }

    public void setPushUrl(String push_url) {
        if (isFinishing()) {
            return;
        }
        if (mIvBg != null) mIvBg.setVisibility(View.GONE);
        if (mIvButify != null) mIvButify.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(push_url)) {
            if (mInfoFragment != null) {
                mInfoFragment.setHangUp();
            }
            return;
        }


        mPushUrl = push_url;
        HnLogUtils.d("roomInfo","roomInfo:"+roomInfo.toString());
        if (mRtcRoom != null) {
            HnLogUtils.d("roomInfo","mRtcRoom is not null");
            mRtcRoom.enterRoom(this.roomInfo, push_url, new RTCRoom.EnterRoomCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    HnLogUtils.d("roomInfo","errCode:"+errCode+",errInfo:"+errInfo);
                    HnToastUtils.showCenterToast(errInfo);
                    if (mInfoFragment != null) {
                        mInfoFragment.setHangUp();
                    }

                }

                @Override
                public void onSuccess() {
                    HnLogUtils.d("roomInfo","onSuccess");
                    HnOnlineVideoChatActivity.this.roomInfo = roomInfo;
                    HnOnlineVideoChatActivity.this.members.add(userID);
                    isAgreeChatVideo = true;
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //截获事件，不再处理
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class RoomVideoView {
        TXCloudVideoView view;
        boolean isUsed;
        String userID;

        public RoomVideoView(TXCloudVideoView view, String userID) {
            this.view = view;
            view.setVisibility(View.GONE);
            this.isUsed = false;
            this.userID = userID;
        }

        private void setUsed(boolean set) {
            view.setVisibility(set ? View.VISIBLE : View.GONE);
            this.isUsed = set;
        }

    }
}
