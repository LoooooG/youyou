package com.hotniao.svideo.fragment.video;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.base.HnViewPagerBaseFragment;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnMyRechargeActivity;
import com.hotniao.svideo.activity.HnUserHomeActivity;
import com.hotniao.svideo.biz.video.HnVideoBiz;
import com.hotniao.svideo.dialog.HnInputTextMsgDialog;
import com.hotniao.svideo.dialog.HnShareDialog;
import com.hotniao.svideo.dialog.HnVideoCommDialog;
import com.hotniao.svideo.model.HnVideoCommModel;
import com.hotniao.svideo.model.HnVideoDetailModel;
import com.hotniao.svideo.model.HnVideoRoomSwitchModel;
import com.hotniao.svideo.model.HnVideoUrlModel;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.event.HnFollowEvent;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;
import com.reslibrarytwo.HnSkinTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

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

public class HnVideoDetailQnFragment extends HnViewPagerBaseFragment implements BaseRequestStateListener {
    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.mVideoViewQn)
    PLVideoTextureView mVideoView;
    @BindView(R.id.mViewClick)
    View mViewClick;
    @BindView(R.id.mIvImg)
    FrescoImageView mIvImg;
    @BindView(R.id.mIvBg)
    FrescoImageView mIvBg;
    @BindView(R.id.mTvFouse)
    TextView mTvFouse;
    @BindView(R.id.mIvPlay)
    ImageView mIvPlay;
    @BindView(R.id.mIvZan)
    HnSkinTextView mTvZan;
    @BindView(R.id.mIvComm)
    TextView mTvComm;
    @BindView(R.id.mIvShare)
    TextView mTvShare;
    @BindView(R.id.mTvTitle)
    TextView mTvTitle;
    @BindView(R.id.mTvName)
    TextView mTvName;


    @BindView(R.id.mTvDialogDetail)
    TextView mTvDialogDetail;
    @BindView(R.id.mRlPayDialog)
    RelativeLayout mRlPayDialog;

    private String mPlayUrl;
    private boolean mVideoPause = true;
    private boolean mPlaying = false;
    private AVOptions mOptions;


    private HnInputTextMsgDialog mInputTextMsgDialog;

    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;

    private HnVideoDetailModel.DBean mDbean;
    private HnVideoRoomSwitchModel.DBean mSwitchData;
    private String mVideoId;

    //回复评论人的Id
    private String mFUserId;

    private HnVideoBiz mHnVideoBiz;
    //付费之前不能点击
    private boolean mCanClick = true;

    public static HnVideoDetailQnFragment newInstance(HnVideoRoomSwitchModel.DBean bean) {
        Bundle args = new Bundle();
        args.putSerializable("data", bean);
        HnVideoDetailQnFragment fragment = new HnVideoDetailQnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_video_detail;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mHnVideoBiz = new HnVideoBiz(mActivity);
        mHnVideoBiz.setBaseRequestStateListener(this);


        mShareAPI = UMShareAPI.get(mActivity);
        mShareAction = new ShareAction(mActivity);

        initPlayer();

        mInputTextMsgDialog = new HnInputTextMsgDialog(mActivity, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(new HnInputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg, boolean isReplayUser) {
                if (TextUtils.isEmpty(mVideoId)) return;
                if (isReplayUser) {
                    mHnVideoBiz.commVideo(mVideoId, mFUserId , msg);
                    mFUserId = null;
                } else {
                    mHnVideoBiz.commVideo(mVideoId, null, msg);
                }
            }
        });
        mSwitchData = (HnVideoRoomSwitchModel.DBean) getArguments().getSerializable("data");

        mIvBg.setController(FrescoConfig.getHeadController(mSwitchData.getCover()));
        mVideoId = mSwitchData.getId();
        mHnVideoBiz.getVideoDetail(mSwitchData.getId());


    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //设置显示模式
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mOptions = new AVOptions();
        mOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        mOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);//采用软解码
        mOptions.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        // 设置偏好的视频格式，设置后会加快对应格式视频流的打开速度，但播放其他格式会出错
        // m3u8 = 1, mp4 = 2, flv = 3
        // mOptions.setInteger(AVOptions.KEY_PREFER_FORMAT, 1);

        mVideoView.setAVOptions(mOptions);
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setLooping(true);

    }

    @Override
    public void fetchData() {

    }

    private int STATUS_ERROR = -1;
    private int STATUS_PLAYING = 2;


    private void statusChange(int newStatus) {
        if (newStatus == STATUS_ERROR) {
            if (mIvPlay != null) mIvPlay.setVisibility(View.VISIBLE);
            if (mIvBg != null) mIvBg.setVisibility(View.VISIBLE);
        } else if (newStatus == STATUS_PLAYING) {
            if (mIvPlay != null) mIvPlay.setVisibility(View.GONE);
            if (mIvBg != null) mIvBg.setVisibility(View.GONE);

        }

    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    statusChange(STATUS_PLAYING);
                    if (mVideoView != null && PLVideoView.ASPECT_RATIO_FIT_PARENT != mVideoView.getDisplayAspectRatio())
                        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
//                    if ((3 * plMediaPlayer.getVideoWidth()) > (2 * plMediaPlayer.getVideoHeight())) {
//                        if (mVideoView != null && PLVideoView.ASPECT_RATIO_FIT_PARENT != mVideoView.getDisplayAspectRatio())
//                            mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
//                    } else {
//                        if (mVideoView != null && PLVideoView.ASPECT_RATIO_PAVED_PARENT != mVideoView.getDisplayAspectRatio())
//                            mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
//                    }
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    break;
                default:
                    break;
            }
            statusChange(STATUS_ERROR);
            return true;
        }
    };


    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mInputTextMsgDialog.show();


    }


    @OnClick({R.id.mIvBack, R.id.mIvImg, R.id.mTvFouse, R.id.mIvZan, R.id.mIvComm, R.id.mIvShare, R.id.mTvComm,
            R.id.mTvFinish, R.id.mTvGoTo, R.id.mIvPlay, R.id.mViewClick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvGoTo: // 去充值
                // if (mHnVideoBiz != null) mHnVideoBiz.getVideoUrl(mSwitchData.getId());
                openActivity(HnMyRechargeActivity.class);
                break;
            case R.id.mTvFinish:
            case R.id.mIvBack:
                mActivity.finish();
                break;
            case R.id.mIvImg:
                if (mDbean == null) return;
                if (TextUtils.isEmpty(mDbean.getUser_id())) {
                    HnToastUtils.showToastShort("用户不存在");
                    return;
                }
                HnUserHomeActivity.luncher(mActivity, mDbean.getUser_id());
                break;
            case R.id.mTvFouse:
                if (mDbean == null) return;
                clickFocus();
                break;
            case R.id.mIvZan:
                if (!mCanClick) return;
                if (mDbean == null || mHnVideoBiz == null || TextUtils.isEmpty(mVideoId)) return;
                if (!mDbean.isIs_like()) mHnVideoBiz.clickZanVideo(mVideoId);
                else HnToastUtils.showToastShort(getString(R.string.you_have_zan_end));
                break;
            case R.id.mIvComm:
                if (!mCanClick) return;
                if (TextUtils.isEmpty(mVideoId) || mDbean == null) return;
                HnVideoCommDialog.newInstance(mVideoId, mDbean.getReply_num()).setClickListen(new HnVideoCommDialog.SelDialogListener() {

                    @Override
                    public void replyClick(String videoId, HnVideoCommModel.DBean.ItemsBean item) {
                        if (item == null) {
                            if (mInputTextMsgDialog != null)
                                mInputTextMsgDialog.setHintText(getString(R.string.input_comm_content), false);
                        } else {
                            mFUserId = item.getUser_id();
                            if (mInputTextMsgDialog != null)
                                mInputTextMsgDialog.setHintText("回复  " + item.getUser_nickname(), true);
                        }
                        showInputMsgDialog();
                    }
                }).show(mActivity.getFragmentManager(), "comm");
                break;
            case R.id.mIvShare:
                if (!mCanClick) return;
                if (mDbean == null || mSwitchData == null) return;
                HnShareDialog.newInstance(mShareAPI, mShareAction, HnUiUtils.getString(R.string.here_have_a_small_video_have_open), HnUrl.setImageUrl(mSwitchData.getCover()),
                        mDbean.getShare_url(), HnUiUtils.getString(R.string.is_heris_here_most_voluptuous_little_video)).setItemShareListener(new HnShareDialog.onShareDialogListener() {
                    @Override
                    public void sureClick() {
                        if (mHnVideoBiz != null) {
                            mHnVideoBiz.shareSuccess(mVideoId);
                        }
                    }
                }).show(mActivity.getFragmentManager(), "share");
                break;
            case R.id.mTvComm:
                if (!mCanClick) return;
                if (mDbean == null) return;
                if (mInputTextMsgDialog != null)
                    mInputTextMsgDialog.setHintText(getString(R.string.input_comm_content), false);
                showInputMsgDialog();
                break;
            case R.id.mIvPlay:
            case R.id.mViewClick:
                if (!mCanClick) return;
                if (mPlayUrl.contains(".m3u8")) return;
                if (mPlaying) {
                    if (mVideoPause) {
                        mVideoView.start();
                        if (mIvPlay != null) {
                            mIvPlay.setVisibility(View.GONE);
                        }
                    } else {
                        mVideoView.pause();
                        if (mIvPlay != null) {
                            mIvPlay.setVisibility(View.VISIBLE);
                        }

                    }
                    mVideoPause = !mVideoPause;
                } else {
                    if (mIvPlay != null) {
                        mIvPlay.setVisibility(View.GONE);
                    }
                    mVideoView.seekTo(1);
                    startPlay();
                }
                break;
        }
    }

    private void startPlay() {
        if (mActivity == null || TextUtils.isEmpty(mPlayUrl) || mVideoView == null) return;
        if (mPlayUrl.contains(".m3u8")) {
            mWebView.loadUrl(mPlayUrl);
            mWebView.setVisibility(View.VISIBLE);
            mIvBg.setVisibility(View.GONE);
            mViewClick.setVisibility(View.GONE);
            mIvPlay.setVisibility(View.GONE);
        } else {
            mWebView.setVisibility(View.GONE);
            try {
                mVideoView.setVideoPath(mPlayUrl);
                HnLogUtils.i("mPlayUrl:" + mPlayUrl);
                mVideoView.start();
                mPlaying = true;
                mVideoPause = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mVideoView != null) {
            mVideoView.pause();
            if (clearLastFrame) {
                mVideoView.setOnErrorListener(null);
                mVideoView.setOnInfoListener(null);
                mVideoView.stopPlayback();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mVideoPause && mVideoView != null && !TextUtils.isEmpty(mPlayUrl)) {
            mVideoView.start();
        } else {
            startPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null)
            mVideoView.pause();
        hintMsg();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlay(true);
        if (mVideoView != null) {
            mVideoView.setOnErrorListener(null);
            mVideoView.setOnInfoListener(null);
            mVideoView.stopPlayback();
            mVideoView = null;
        }
        EventBus.getDefault().unregister(this);
    }


    public void hintMsg() {
        //打开软键盘
        if (mActivity == null) return;
        if (mInputTextMsgDialog != null && mInputTextMsgDialog.isShowing()) {
            mInputTextMsgDialog.dismiss();
        }
    }


    /**
     * 滑动切换视频
     *
     * @param bean
     */
    public void switchVideo(HnVideoRoomSwitchModel.DBean bean) {
        HnHttpUtils.cancelRequest(HnUrl.VIDEO_APP_VIDEO_DETAIL + mVideoId);
        HnHttpUtils.cancelRequest(HnUrl.VIDEO_APP_VIDEO_URL + mVideoId);
        clearData();

        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnInfoListener(mOnInfoListener);
        mSwitchData = bean;
        mIvBg.setController(FrescoConfig.getHeadController(bean.getCover()));
        mIvBg.setVisibility(View.VISIBLE);
        mVideoId = bean.getId();
        mHnVideoBiz.getVideoDetail(bean.getId());
    }

    /**
     * 清理数据
     */
    private void clearData() {
        if (mActivity == null || mIvImg == null || mTvTitle == null) return;
        stopPlay(true);
        if (mRlPayDialog != null) mRlPayDialog.setVisibility(View.GONE);
        mVideoId = null;
        mFUserId = null;
        mDbean = null;
        mSwitchData = null;
        mIvImg.setController(FrescoConfig.getHeadController("1111"));
        mTvZan.setTopDrawable(R.drawable.video_detail_dianzan);
        mTvComm.setText("0");
        mTvShare.setText("0");
        mTvZan.setText("0");
        mTvTitle.setText("");
        mTvName.setText("");
        mCanClick = true;
    }

    /**
     * 设置数据
     */
    private void setMessage() {
        if (mDbean == null && mTvZan != null) return;
        mTvZan.setText(HnUtils.setNoPoint(mDbean.getLike_num()));
        mTvShare.setText(HnUtils.setNoPoint(mDbean.getShare_num()));
        mTvComm.setText(HnUtils.setNoPoint(mDbean.getReply_num()));
        mTvTitle.setText(mDbean.getTitle());
        mTvName.setText(mDbean.getUser_nickname());//缺少name
        mIvImg.setController(FrescoConfig.getHeadController(mDbean.getUser_avatar()));

        setFocusState();
        setZanState();
    }

    private void setZanState() {
        if (mDbean == null || mTvZan == null) return;
        if (mDbean.isIs_like()) {
            mTvZan.setTopDrawable(R.drawable.btn_like_sel);
        } else {
            mTvZan.setTopDrawable(R.drawable.video_detail_dianzan);
        }
    }

    /**
     * 设置关注状态
     */
    private void setFocusState() {
        if (mDbean == null || mTvFouse == null) return;
        if (HnApplication.getmUserBean() == null) {
            if (HnPrefUtils.getString(NetConstant.User.UID, "").equals(mDbean.getUser_id()))
                mTvFouse.setVisibility(View.GONE);
        } else if (HnApplication.getmUserBean().getUser_id().equals(mDbean.getUser_id())) {
            mTvFouse.setVisibility(View.GONE);
        }
        if (mDbean.isIs_follow()) {
            mTvFouse.setText(R.string.main_follow_on);
        } else {
            mTvFouse.setText(R.string.add_fouces_chat);
        }
    }

    /**
     * 点赞
     */
    private void clickFocus() {
        if (mDbean.isIs_follow()) {
            HnUserControl.cancelFollow(mDbean.getUser_id(), new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (mActivity == null) return;
                    mDbean.setIs_follow(false);
                    EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFocusState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (mActivity == null) return;
                    HnToastUtils.showToastShort(msg);
                }
            });
        } else {
            HnUserControl.addFollow(mDbean.getUser_id(), null, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (mActivity == null) return;
                    mDbean.setIs_follow(true);
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFocusState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (mActivity == null) return;
                    HnToastUtils.showToastShort(msg);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(HnFollowEvent event) {
        if (event != null) {
            String uid = event.getUid();
            boolean isFollow = event.isFollow();
            if (!TextUtils.isEmpty(uid) && mDbean != null) {
                if (uid.equals(mDbean.getUser_id())) {
                    if (isFollow) {
                        mDbean.setIs_follow(true);
                    } else {
                        mDbean.setIs_follow(false);
                    }
                    setFocusState();
                }
            }
        }

    }

    private void getVideoUrlAndPay() {
        if (mDbean == null) return;
        mCanClick = true;
        if (!TextUtils.isEmpty(mDbean.getPrice())) {
            try {
                long price = Long.parseLong(mDbean.getPrice());
                if (0 < price) {
                    mCanClick = false;
                    mTvDialogDetail.setText(String.format(HnUiUtils.getString(R.string.look_video_need_pay_shold_look), mDbean.getPrice(), HnApplication.getmConfig().getCoin()));
                    mRlPayDialog.setVisibility(View.VISIBLE);
                } else {
                    mRlPayDialog.setVisibility(View.GONE);
                    mHnVideoBiz.getVideoUrl(mSwitchData.getId());
                }
            } catch (Exception e) {
                mHnVideoBiz.getVideoUrl(mSwitchData.getId());
            }
        } else {
            mRlPayDialog.setVisibility(View.GONE);
            mHnVideoBiz.getVideoUrl(mSwitchData.getId());
        }

    }

    /**
     * 显示去充值提示框
     */
    private void showGoRechargeDialog() {
        mCanClick = false;
        mTvDialogDetail.setText("余额不足哦，充值后才可观看私密视频");
        mRlPayDialog.setVisibility(View.VISIBLE);
    }


    @Override
    public void requesting() {

    }


    @Override
    public void requestSuccess(String type, String rspOrId, Object obj) {
        if (mActivity == null) return;
        if (!mVideoId.equals(rspOrId)) return;
        if (HnVideoBiz.VideoDetail.equals(type)) {
            HnVideoDetailModel model = (HnVideoDetailModel) obj;
            mVideoId = rspOrId;
            if (model != null && model.getD() != null) {
                mDbean = model.getD();
                setMessage();
                // getVideoUrlAndPay();
                mHnVideoBiz.getVideoUrl(mSwitchData.getId()); // 直接播放
            }
        } else if (HnVideoBiz.VideoUrl.equals(type)) {
            HnVideoUrlModel model = (HnVideoUrlModel) obj;
            if (model.getD() != null && !TextUtils.isEmpty(model.getD().getUrl())) {
                if (mRlPayDialog != null) mRlPayDialog.setVisibility(View.GONE);
                mPlayUrl = model.getD().getUrl();
//                mPlayUrl = "http://67vod.814876.com/2019/4d8fda27/m3u8.m3u8";
//                mPlayUrl = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
                mCanClick = true;
                startPlay();
            }
        } else if (HnVideoBiz.VideoZan.equals(type)) {
            if (mDbean == null || TextUtils.isEmpty(mDbean.getLike_num())) return;
            try {
                mDbean.setIs_like(true);
                setZanState();
                int zanNum = Integer.parseInt(mDbean.getLike_num());
                zanNum += 1;
                mDbean.setLike_num(zanNum + "");
                mTvZan.setText(zanNum + "");
            } catch (Exception e) {
            }
        } else if (HnVideoBiz.VideoComm.equals(type)) {
            if (mDbean == null || TextUtils.isEmpty(mDbean.getReply_num())) return;
            try {
                int commNum = Integer.parseInt(mDbean.getReply_num());
                commNum += 1;
                mDbean.setReply_num(commNum + "");
                mTvComm.setText(commNum + "");
                EventBus.getDefault().post(new EventBusBean(1, HnConstants.EventBus.RefreshVideoCommList, commNum));
            } catch (Exception e) {
            }
        } else if (HnVideoBiz.VideoShare.equals(type)) {
            try {
                int shareNum = Integer.parseInt(mDbean.getShare_num());
                shareNum += 1;
                mDbean.setShare_num(shareNum + "");
                mTvShare.setText(shareNum + "");
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) return;
        if (code == 10000) {
            showGoRechargeDialog();
        } else if (HnVideoBiz.VideoDetail.equals(type) || HnVideoBiz.VideoZan.equals(type) || HnVideoBiz.VideoComm.equals(type)
                || HnVideoBiz.VideoShare.equals(type)) {
            HnToastUtils.showToastShort(msg);
        } else if (HnVideoBiz.VideoUrl.equals(type)) {
            if (HnServiceErrorUtil.USER_COIN_NOT_ENOUGH == code) {
                CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {

                    }

                    @Override
                    public void rightClick() {
                        openActivity(HnMyRechargeActivity.class);
                    }
                }).setTitle(HnUiUtils.getString(R.string.live_letter_bal_not_enough))
                        .setContent(String.format(HnUiUtils.getString(R.string.live_balance_not_enough_1), HnApplication.getmConfig().getCoin()))
                        .setRightText(HnUiUtils.getString(R.string.live_immediately_immediatel)).show();
            } else {
                HnToastUtils.showToastShort(msg);
            }

        }
    }


}
