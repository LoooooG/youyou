package com.videolibrary.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.livelibrary.ui.beauty.BeautyDialogFragment;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCRecord;
import com.videolibrary.R;
import com.videolibrary.eventbus.HnDeleteVideoFileEvent;
import com.videolibrary.eventbus.HnSelectMusicEvent;
import com.videolibrary.util.FileUtils;
import com.videolibrary.util.TCConstants;
import com.videolibrary.util.TCUtils;
import com.videolibrary.view.ComposeRecordBtn;
import com.videolibrary.view.RecordProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

/**
 * UGC小视频录制界面
 */
public class TCVideoRecordActivity extends BaseActivity implements View.OnClickListener, TXRecordCommon.ITXVideoRecordListener,
        View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "TCVideoRecordActivity";
    private static final String OUTPUT_DIR_NAME = "TXUGC";
    private boolean mRecording = false;
    private boolean mStartPreview = false;
    private boolean mFront = true;
    private TXUGCRecord mTXCameraRecord;
    private TXRecordCommon.TXRecordResult mTXRecordResult;
    private long mDuration; // 视频总时长

    /* private BeautySettingPannel.BeautyParams mBeautyParams = new BeautySettingPannel.BeautyParams();*/
    private TXCloudVideoView mVideoView;
    private TextView mIvConfirm;
    private TextView mProgressTime;
    //    private ProgressDialog mCompleteProgressDialog;
//    private CustomProgressDialog mCustomProgressDialog;
    private ImageView mIvTorch;
    private ImageView mIvMusic;
    private ImageView mIvBeauty;
    private ImageView mIvChooseVIdeo;
    private ImageView mIvSwitchCamera;
    private ImageView mIvBack;
    private LinearLayout mLlFun;
    private ComposeRecordBtn mComposeRecordBtn;

    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener;
    private boolean mPause = false;
    private int mCurrentAspectRatio;
    private RelativeLayout mRecordRelativeLayout = null;
    private FrameLayout mMaskLayout;
    private RecordProgressView mRecordProgressView;
    private ImageView mIvDeleteLastPart;
    private boolean mIsTorchOpen = false; // 闪光灯的状态

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor;
    private float mLastScaleFactor;

    private int mRecommendQuality = TXRecordCommon.VIDEO_QUALITY_MEDIUM;
    private int mMinDuration;
    private int mMaxDuration;
    private int mAspectRatio; // 视频比例
    private int mRecordResolution; // 录制分辨率
    private int mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN; // 录制方向
    private int mRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT; // 渲染方向
    private int mBiteRate; // 码率
    private int mFps; // 帧率
    private int mGop; // 关键帧间隔
    private String mBGMPath;
    private String mBGMPlayingPath;
    private int mBGMDuration;
    private int mRecordSpeed = TXRecordCommon.RECORD_SPEED_NORMAL;
    private boolean mNeedEditer;
    private boolean mPortrait = true;

    private int mNowRecordLong = 0;

    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();


    public static void luncher(Activity activity) {
        activity.startActivity(new Intent(activity, TCVideoRecordActivity.class));
    }

    @Override
    public int getContentViewId() {
        Window window = getWindow();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
        return R.layout.activity_video_record;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        setShowTitleBar(false);
        setShowBack(false);


        mIvBack = (ImageView) findViewById(R.id.mIvBack);
        mIvBack.setOnClickListener(this);

        mBeautyDialogFragment = new BeautyDialogFragment();
        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, new BeautyDialogFragment.OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
                switch (key) {
                    case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
                    case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                    case BeautyDialogFragment.BEAUTYPARAM_RUDDY:
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.setBeautyDepth(1, params.mBeautyProgress, params.mWhiteProgress, params.mRuddyProgress);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT:
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.setFaceScaleLevel(params.mFaceLiftProgress);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_BIG_EYE:
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.setEyeScaleLevel(params.mBigEyeProgress);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.setFilter(TCUtils.getFilterBitmap(getResources(), params.mFilterIdx));
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL:
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.setMotionTmpl(params.mMotionTmplPath);
                        }
                        break;
                    case BeautyDialogFragment.BEAUTYPARAM_GREEN:
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.setGreenScreenFile(TCUtils.getGreenFileName(params.mGreenIdx), true);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void dismiss() {
                if (mIvBeauty != null)
                    mIvBeauty.setImageResource(R.drawable.meiyan);
            }
        });
        initViews();

        getData();
    }

    @Override
    public void getInitData() {

    }


    private void getData() {
        Intent intent = getIntent();
        if (intent == null) {
            TXCLog.e(TAG, "intent is null");
            return;
        }

        mMinDuration = 5 * 1000;//最少录制时间
        mMaxDuration = 60 * 1000;//最长录制时间
        mAspectRatio = TXRecordCommon.VIDEO_ASPECT_RATIO_9_16;//录制宽高比例
        mRecommendQuality = -1;//是否美颜
        mNeedEditer = true;//是否进入编辑页面

        mCurrentAspectRatio = mAspectRatio;

        mRecordProgressView.setMaxDuration(mMaxDuration);
        mRecordProgressView.setMinDuration(mMinDuration);

        // 自定义视频质量设置，用TXUGCCustomConfig
        mRecordResolution = TXRecordCommon.VIDEO_RESOLUTION_720_1280;//分辨率
        mBiteRate = 1800;//比特率
        mFps = 20;//帧率
        mGop = 3;//关键帧间隔

        TXCLog.d(TAG, "mMinDuration = " + mMinDuration + ", mMaxDuration = " + mMaxDuration + ", mAspectRatio = " + mAspectRatio +
                ", mRecommendQuality = " + mRecommendQuality + ", mRecordResolution = " + mRecordResolution + ", mBiteRate = " + mBiteRate + ", mFps = " + mFps + ", mGop = " + mGop);
    }

    private void startCameraPreview() {
        if (mStartPreview) return;
        mStartPreview = true;

        mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        mTXCameraRecord.setVideoRecordListener(this);
        mRecording = false;

        // activity竖屏模式，竖屏录制 :
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        // activity横屏模式，home在右横屏录制(activity随着重力感应旋转)：
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        // activity横屏模式，home在左横屏录制(activity随着重力感应旋转)：
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_LEFT);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        // 如果想保持activity为竖屏，并且要home在右横屏录制，那么可以用下面的方式：
        // activity竖屏模式，home在右横屏录制(锁定Activity不旋转，比如在manefest设置activity的 android:screenOrientation="portrait")：
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
        //
        // 推荐配置
        if (mRecommendQuality >= 0) {
            TXRecordCommon.TXUGCSimpleConfig simpleConfig = new TXRecordCommon.TXUGCSimpleConfig();
            simpleConfig.videoQuality = mRecommendQuality;
            simpleConfig.minDuration = mMinDuration;
            simpleConfig.maxDuration = mMaxDuration;
            simpleConfig.isFront = mFront;

            mTXCameraRecord.setRecordSpeed(mRecordSpeed);
            mTXCameraRecord.startCameraSimplePreview(simpleConfig, mVideoView);
            mTXCameraRecord.setAspectRatio(mCurrentAspectRatio);
        } else {
            // 自定义配置
            TXRecordCommon.TXUGCCustomConfig customConfig = new TXRecordCommon.TXUGCCustomConfig();
            customConfig.videoResolution = mRecordResolution;
            customConfig.minDuration = mMinDuration;
            customConfig.maxDuration = mMaxDuration;
            customConfig.videoBitrate = mBiteRate;
            customConfig.videoGop = mGop;
            customConfig.videoFps = mFps;
            customConfig.isFront = mFront;
            mTXCameraRecord.setRecordSpeed(mRecordSpeed);
            mTXCameraRecord.startCameraCustomPreview(customConfig, mVideoView);
            mTXCameraRecord.setAspectRatio(mCurrentAspectRatio);
        }

        mTXCameraRecord.setBeautyDepth(1, mBeautyParams.mBeautyProgress, mBeautyParams.mWhiteProgress, mBeautyParams.mRuddyProgress);
        mTXCameraRecord.setFaceScaleLevel(mBeautyParams.mFaceLiftProgress);
        mTXCameraRecord.setEyeScaleLevel(mBeautyParams.mBigEyeProgress);
        mTXCameraRecord.setFilter(TCUtils.getFilterBitmap(getResources(), mBeautyParams.mFilterIdx));
        mTXCameraRecord.setGreenScreenFile(TCUtils.getGreenFileName(mBeautyParams.mGreenIdx), true);
        mTXCameraRecord.setMotionTmpl(mBeautyParams.mMotionTmplPath);
    }


    private void initViews() {
        mMaskLayout = (FrameLayout) findViewById(R.id.mask);
        mMaskLayout.setOnTouchListener(this);

        mIvConfirm = (TextView) findViewById(R.id.btn_confirm);
        mIvConfirm.setOnClickListener(this);
        mIvConfirm.setEnabled(false);

        mIvChooseVIdeo = (ImageView) findViewById(R.id.mIvChooseVIdeo);
        mIvChooseVIdeo.setOnClickListener(this);
        mIvChooseVIdeo.setEnabled(true);

        mIvSwitchCamera = (ImageView) findViewById(R.id.btn_switch_camera);
        mLlFun = (LinearLayout) findViewById(R.id.mLlFun);


        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mVideoView.enableHardwareDecode(true);

        mProgressTime = (TextView) findViewById(R.id.progress_time);
        mIvDeleteLastPart = (ImageView) findViewById(R.id.btn_delete_last_part);
        mIvDeleteLastPart.setOnClickListener(this);


        mIvMusic = (ImageView) findViewById(R.id.btn_music_pannel);

        mIvBeauty = (ImageView) findViewById(R.id.btn_beauty);

        mRecordRelativeLayout = (RelativeLayout) findViewById(R.id.record_layout);
        mRecordProgressView = (RecordProgressView) findViewById(R.id.record_progress_view);

        mGestureDetector = new GestureDetector(this, this);
        mScaleGestureDetector = new ScaleGestureDetector(this, this);


        mIvTorch = (ImageView) findViewById(R.id.btn_torch);
        mIvTorch.setOnClickListener(this);

        if (mFront) {
            mIvTorch.setImageResource(R.drawable.ugc_torch_close);
            mIvTorch.setEnabled(false);
        } else {
            mIvTorch.setImageResource(R.drawable.selector_torch_close);
            mIvTorch.setEnabled(true);
        }

        mComposeRecordBtn = (ComposeRecordBtn) findViewById(R.id.compose_record_btn);

        setRecordTouchListener();
    }


    private void setRecordTouchListener() {
        mComposeRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // 按下按钮
                        if (mMaxDuration > mDuration) {
                            switchRecord();
                        } else {
                            stopRecord();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mMaxDuration > mDuration) {
                            switchRecord();
                        }
                        break;
                }
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        onActivityRotation();
        if (hasPermission()) {
            startCameraPreview();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTXCameraRecord != null) {
            mTXCameraRecord.setVideoProcessListener(null); // 这里要取消监听，否则在上面的回调中又会重新开启预览
            mTXCameraRecord.stopCameraPreview();
            mStartPreview = false;
            // 设置闪光灯的状态为关闭
            if (mIsTorchOpen) {
                mIsTorchOpen = false;
                if (mFront) {
                    mIvTorch.setImageResource(R.drawable.ugc_torch_close);
                    mIvTorch.setEnabled(false);
                } else {
                    mIvTorch.setImageResource(R.drawable.selector_torch_close);
                    mIvTorch.setEnabled(true);
                }
            }
        }
        if (mRecording && !mPause) {
            pauseRecord();
        }
        if (mTXCameraRecord != null) {
            mTXCameraRecord.pauseBGM();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        TXCLog.i(TAG, "onDestroy");
        if (mRecordProgressView != null) {
            mRecordProgressView.release();
        }

        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopBGM();
            mTXCameraRecord.stopCameraPreview();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.getPartsManager().deleteAllParts();
            mTXCameraRecord.release();
            mTXCameraRecord = null;
            mStartPreview = false;
        }
        abandonAudioFocus();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        onActivityRotation();
        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopCameraPreview();
        }

        if (mRecording && !mPause) {
            pauseRecord();
        }
        if (mTXCameraRecord != null) {
            mTXCameraRecord.pauseBGM();
        }
        mStartPreview = false;

        startCameraPreview();
    }

    /**
     * 用来在activity随着重力感应切换方向时，切换横竖屏录制
     * 注意：使用时，录制过程中或暂停后不允许切换横竖屏，如果开始录制时使用的是横屏录制，那么整段录制都要用横屏，否则录制失败。
     */
    protected void onActivityRotation() {
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变录制方向
        int mobileRotation = this.getWindowManager().getDefaultDisplay().getRotation();
        mRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT; // 渲染方向，因为activity也旋转了，本地渲染相对正方向的角度为0。
        mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectMusic(HnSelectMusicEvent event) {
        if (event != null) {
            mBGMPath = event.getPath();
        }

    }

    @Override
    public void onClick(View view) {
        if (R.id.mIvBack == view.getId()) {
            back();
        } else if (R.id.btn_beauty == view.getId()) {

            try {
                if (mBeautyDialogFragment.isAdded()) {
                    mBeautyDialogFragment.dismiss();
                } else {
                    mBeautyDialogFragment.show(getFragmentManager(), "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (R.id.btn_switch_camera == view.getId()) {
            mFront = !mFront;
            mIsTorchOpen = false;
            if (mFront) {
                mIvTorch.setImageResource(R.drawable.ugc_torch_close);
                mIvTorch.setEnabled(false);
            } else {
                mIvTorch.setImageResource(R.drawable.selector_torch_close);
                mIvTorch.setEnabled(true);
            }
            if (mTXCameraRecord != null) {
                TXCLog.i(TAG, "switchCamera = " + mFront);
                mTXCameraRecord.switchCamera(mFront);
            }
        } else if (R.id.btn_music_pannel == view.getId()) {

            ARouter.getInstance().build("/music/musicLoclActivity").navigation();
        } else if (R.id.btn_confirm == view.getId()) {
            if (mNowRecordLong < (mMinDuration / 1000)) {
                HnToastUtils.showToastShort(String.format(HnUiUtils.getString(R.string.need_to_record_at_least_seconds_to_save_video), mMinDuration / 1000 + ""));
            } else {
                stopRecord();
            }
        } else if (R.id.btn_delete_last_part == view.getId()) {
            deleteLastPart();
        } else if (R.id.btn_torch == view.getId()) {
            toggleTorch();
        } else if (R.id.mIvChooseVIdeo == view.getId()) {//选择视频
            HnChooseVideoActivity.luncher(this, HnChooseVideoActivity.SmallVideo);
        }

    }


    private void toggleTorch() {
        if (mIsTorchOpen) {
            mTXCameraRecord.toggleTorch(false);
            mIvTorch.setImageResource(R.drawable.selector_torch_close);
        } else {
            mTXCameraRecord.toggleTorch(true);
            mIvTorch.setImageResource(R.drawable.selector_torch_open);
        }
        mIsTorchOpen = !mIsTorchOpen;
    }

    private void deleteLastPart() {
        if (mRecording && !mPause || mTXCameraRecord.getPartsManager().getDuration() <= 0) {
            return;
        }
        mRecordProgressView.selectLast();
        CommDialog.newInstance(this).setClickListen(new CommDialog.TwoSelDialog() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                mRecordProgressView.deleteLast();
                mTXCameraRecord.getPartsManager().deleteLastPart();
                int timeSecond = mTXCameraRecord.getPartsManager().getDuration() / 1000;
                mProgressTime.setText(String.format(Locale.CHINA, "00:%02d", timeSecond));
                mDuration = mTXCameraRecord.getPartsManager().getDuration();
                mNowRecordLong = timeSecond;
                if (timeSecond < mMinDuration / 1000) {
                    mIvConfirm.setEnabled(true);
                } else {
                    mIvConfirm.setEnabled(true);
                }
            }
        }).setTitle("温馨提示").setContent("删除上一次拍摄内容").setCanceledOnOutside(true).setRightText("确定放弃").show();

    }

    /**
     * 设置录制时 按钮的显示状态
     *
     * @param isRecording
     */
    private void setRecordingViewVisibility(boolean isRecording) {
        if (isRecording) {
            mLlFun.setVisibility(View.GONE);
            mIvBack.setVisibility(View.GONE);
            mIvDeleteLastPart.setVisibility(View.GONE);
            mIvConfirm.setVisibility(View.GONE);
        } else {
            mLlFun.setVisibility(View.VISIBLE);
            mIvBack.setVisibility(View.VISIBLE);
            mIvDeleteLastPart.setVisibility(View.VISIBLE);
            mIvConfirm.setVisibility(View.VISIBLE);
        }
    }

    private void switchRecord() {
        if (mRecording) {
            if (mPause) {
                if (mTXCameraRecord.getPartsManager().getPartsPathList().size() == 0) {
                    startRecord();
                } else {
                    resumeRecord();
                }
            } else {
                pauseRecord();
            }
        } else {

            startRecord();
        }
    }


    private void resumeRecord() {
        setRecordingViewVisibility(true);

        if (mTXCameraRecord == null) {
            return;
        }

        mTXCameraRecord.setMicVolume(80 / (float) 100);

        mComposeRecordBtn.startRecord();
        int startResult = mTXCameraRecord.resumeRecord();

        if (!TextUtils.isEmpty(mBGMPath)) {
            if (mBGMPlayingPath == null || !mBGMPath.equals(mBGMPlayingPath)) {
                mBGMDuration = mTXCameraRecord.setBGM(mBGMPath);
                mTXCameraRecord.playBGMFromTime(0, mBGMDuration);
                mBGMPlayingPath = mBGMPath;
            } else {
                mTXCameraRecord.resumeBGM();
            }
        }


        mIvDeleteLastPart.setImageResource(R.drawable.fanhui);
        mIvDeleteLastPart.setEnabled(false);

        mPause = false;
        requestAudioFocus();

    }

    private void pauseRecord() {
        setRecordingViewVisibility(false);

        mComposeRecordBtn.pauseRecord();
        mPause = true;
        mIvDeleteLastPart.setImageResource(R.drawable.fanhui);
        mIvDeleteLastPart.setEnabled(true);

        if (mTXCameraRecord != null) {
            if (!TextUtils.isEmpty(mBGMPlayingPath)) {
                mTXCameraRecord.pauseBGM();
            }
            mTXCameraRecord.pauseRecord();
        }
        abandonAudioFocus();

    }

    private void stopRecord() {
        setRecordingViewVisibility(false);
        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopBGM();
            mTXCameraRecord.stopRecord();
        }
        mRecording = false;
        mPause = false;
        abandonAudioFocus();

    }

    private void startRecord() {

        setRecordingViewVisibility(true);
        mIvChooseVIdeo.setVisibility(GONE);


        // 在开始录制的时候，就不能再让activity旋转了，否则生成视频出错
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (mTXCameraRecord == null) {
            mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        }

        String customVideoPath = getCustomVideoOutputPath();
        String customCoverPath = customVideoPath.replace(".mp4", ".jpg");

        int result = mTXCameraRecord.startRecord(customVideoPath, customCoverPath);

        mComposeRecordBtn.startRecord();
        mIvDeleteLastPart.setImageResource(R.drawable.fanhui);
        mIvDeleteLastPart.setEnabled(false);

        mTXCameraRecord.setMicVolume(80 / (float) 100);
        if (!TextUtils.isEmpty(mBGMPath)) {
            mBGMDuration = mTXCameraRecord.setBGM(mBGMPath);
            mTXCameraRecord.playBGMFromTime(0, mBGMDuration);
            mBGMPlayingPath = mBGMPath;
        }

        mRecording = true;
        mPause = false;
        requestAudioFocus();

    }

    private String getCustomVideoOutputPath() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String time = sdf.format(new Date(currentTime));
        String outputDir = Environment.getExternalStorageDirectory() + File.separator + OUTPUT_DIR_NAME;
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        String tempOutputPath = outputDir + File.separator + "TXUGC_" + time + ".mp4";
        return tempOutputPath;
    }

    private void startPreview() {
        if (mTXRecordResult != null && (mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK
                || mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK_REACHED_MAXDURATION
                || mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK_LESS_THAN_MINDURATION)) {
            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_UGC_RECORD);
            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, mTXRecordResult.retCode);
            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, mTXRecordResult.descMsg);
            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mTXRecordResult.videoPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTXRecordResult.coverPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_DURATION, mDuration);
            if (mRecommendQuality == TXRecordCommon.VIDEO_QUALITY_LOW) {
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_360_640);
            } else if (mRecommendQuality == TXRecordCommon.VIDEO_QUALITY_MEDIUM) {
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_540_960);
            } else if (mRecommendQuality == TXRecordCommon.VIDEO_QUALITY_HIGH) {
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_720_1280);
            } else {
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, mRecordResolution);
            }
            startActivity(intent);
            finish();
        }
    }

    private void startEditVideo() {
        Intent intent = new Intent(this, TCVideoEditerActivity.class);
        intent.putExtra(TCConstants.VIDEO_EDITER_PATH, mTXRecordResult.videoPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTXRecordResult.coverPath);
        startActivity(intent);
    }

    @Override
    public void onRecordEvent(int event, Bundle param) {
        HnLogUtils.e("onRecordEvent event id = " + event);
        if (event == TXRecordCommon.EVT_ID_PAUSE) {
            mRecordProgressView.clipComplete();
        } else if (event == TXRecordCommon.EVT_CAMERA_CANNOT_USE) {
            Toast.makeText(this, "摄像头打开失败，请检查权限", Toast.LENGTH_SHORT).show();
        } else if (event == TXRecordCommon.EVT_MIC_CANNOT_USE) {
            Toast.makeText(this, "麦克风打开失败，请检查权限", Toast.LENGTH_SHORT).show();
        } else if (event == TXRecordCommon.EVT_ID_RESUME) {

        }
    }

    @Override
    public void onRecordProgress(long milliSecond) {
        HnLogUtils.e("onRecordProgress, mRecordProgressView = " + milliSecond);
        if (mRecordProgressView == null) {
            return;
        }
        mRecordProgressView.setProgress((int) milliSecond);
        float timeSecondFloat = milliSecond / 1000f;
        mNowRecordLong = Math.round(timeSecondFloat);
        mDuration = milliSecond;
        mProgressTime.setText(String.format(Locale.CHINA, "00:%02d", mNowRecordLong));
        if (timeSecondFloat < mMinDuration / 1000) {
            mIvConfirm.setEnabled(true);
        } else {
            mIvConfirm.setEnabled(true);
        }
    }


    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {

        mTXRecordResult = result;
        setRecordingViewVisibility(false);
        TXCLog.i(TAG, "onRecordComplete, result retCode = " + result.retCode + ", descMsg = " + result.descMsg + ", videoPath + " + result.videoPath + ", coverPath = " + result.coverPath);
        if (mTXRecordResult.retCode < -1) {
            mRecording = false;

            int timeSecond = mTXCameraRecord.getPartsManager().getDuration() / 1000;
            mProgressTime.setText(String.format(Locale.CHINA, "00:%02d", timeSecond));
            HnToastUtils.showToastShort("录制失败，原因：" + mTXRecordResult.descMsg);
        } else if (mTXRecordResult.retCode < 0) {

        } else {
            mDuration = mTXCameraRecord.getPartsManager().getDuration();
//            mProgressTime.setText(String.format(Locale.CHINA, "00:%02d", 0));
//            if (mTXCameraRecord != null) {
//                mRecordProgressView.deleteAll();
//                mTXCameraRecord.getPartsManager().deleteAllParts();
//            }
            if (mNeedEditer) {
                startEditVideo();
            } else {
                startPreview();
            }
        }
    }

    private void requestAudioFocus() {
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }

        if (null == mOnAudioFocusListener) {
            mOnAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

                @Override
                public void onAudioFocusChange(int focusChange) {
                    try {
                        TXCLog.i(TAG, "requestAudioFocus, onAudioFocusChange focusChange = " + focusChange);

                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            pauseRecord();
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            pauseRecord();
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                        } else {
                            pauseRecord();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        try {
            mAudioManager.requestAudioFocus(mOnAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abandonAudioFocus() {
        try {
            if (null != mAudioManager && null != mOnAudioFocusListener) {
                mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                startCameraPreview();
                break;
            default:
                break;
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == mMaskLayout) {
            if (motionEvent.getPointerCount() >= 2) {
                mScaleGestureDetector.onTouchEvent(motionEvent);
            } else if (motionEvent.getPointerCount() == 1) {
                mGestureDetector.onTouchEvent(motionEvent);
            }
        }
        return true;
    }

    // OnGestureListener回调start
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        try {
            if (mBeautyDialogFragment.isAdded()) {
                mBeautyDialogFragment.dismiss();
                mIvBeauty.setImageResource(R.drawable.meiyan);
                mRecordRelativeLayout.setVisibility(View.VISIBLE);
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    // OnGestureListener回调end

    // OnScaleGestureListener回调start
    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        int maxZoom = mTXCameraRecord.getMaxZoom();
        if (maxZoom == 0) {
            TXCLog.i(TAG, "camera not support zoom");
            return false;
        }

        float factorOffset = scaleGestureDetector.getScaleFactor() - mLastScaleFactor;

        mScaleFactor += factorOffset;
        mLastScaleFactor = scaleGestureDetector.getScaleFactor();
        if (mScaleFactor < 0) {
            mScaleFactor = 0;
        }
        if (mScaleFactor > 1) {
            mScaleFactor = 1;
        }

        int zoomValue = Math.round(mScaleFactor * maxZoom);
        mTXCameraRecord.setZoom(zoomValue);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        mLastScaleFactor = scaleGestureDetector.getScaleFactor();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }


    private void back() {
        if (!mRecording) {
            finishActivity();
        }
        if (mPause) {
            finishActivity();
        } else {
            pauseRecord();
        }
    }


    private void finishActivity() {
        if (mNowRecordLong == 0) {
            finish();
        } else {
            CommDialog.newInstance(this).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {
                }

                @Override
                public void rightClick() {
                    if (mPause) {
                        if (mTXCameraRecord != null) {
                            mTXCameraRecord.getPartsManager().deleteAllParts();
                        }
                    }
                    finish();
                }
            }).setTitle("温馨提示").setContent("是否放弃当前录制的视屏").setCanceledOnOutside(true).setRightText("确定放弃").show();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteVideoFile(HnDeleteVideoFileEvent event) {
        HnFileUtils.deleteFile(mTXRecordResult.videoPath);
        HnFileUtils.deleteFile(mTXRecordResult.coverPath);
    }
}
