package com.hotniao.live.fragment.video;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.base.HnViewPagerBaseFragment;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnMyRechargeActivity;
import com.hotniao.live.activity.HnUserHomeActivity;
import com.hotniao.live.biz.video.HnVideoBiz;
import com.hotniao.live.dialog.HnInputTextMsgDialog;
import com.hotniao.live.dialog.HnShareDialog;
import com.hotniao.live.dialog.HnVideoCommDialog;
import com.hotniao.live.model.HnVideoCommModel;
import com.hotniao.live.model.HnVideoDetailModel;
import com.hotniao.live.model.HnVideoRoomSwitchModel;
import com.hotniao.live.model.HnVideoUrlModel;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

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

public class HnVideoDetailFragment extends HnViewPagerBaseFragment implements ITXLivePlayListener, BaseRequestStateListener {
    @BindView(R.id.mVideoView)
    TXCloudVideoView mVideoView;
    @BindView(R.id.mIvImg)
    FrescoImageView mIvImg;
    @BindView(R.id.mIvBg)
    FrescoImageView mIvBg;
    @BindView(R.id.mTvFouse)
    TextView mTvFouse;
    @BindView(R.id.mIvPlay)
    ImageView mIvPlay;
    @BindView(R.id.mIvZan)
    TextView mTvZan;
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
    private int mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;

    private TXLivePlayer mTXLivePlayer;
    private TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();


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

    public static HnVideoDetailFragment newInstance(HnVideoRoomSwitchModel.DBean bean) {
        Bundle args = new Bundle();
        args.putSerializable("data", bean);
        HnVideoDetailFragment fragment = new HnVideoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_video_detail;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHnVideoBiz = new HnVideoBiz(mActivity);
        mHnVideoBiz.setBaseRequestStateListener(this);


        mShareAPI = UMShareAPI.get(mActivity);
        mShareAction = new ShareAction(mActivity);

        initPalyer();

        mInputTextMsgDialog = new HnInputTextMsgDialog(mActivity, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(new HnInputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg, boolean isRepleyUser) {
                if (TextUtils.isEmpty(mVideoId)) return;
                if (isRepleyUser) {
                    mHnVideoBiz.commVideo(mVideoId, mFUserId, msg);
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
    private void initPalyer() {
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(mActivity);
        }
        mTXLivePlayer.setPlayerView(mVideoView);
//        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);//RENDER_MODE_FULL_FILL_SCREEN//RENDER_MODE_ADJUST_RESOLUTION
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);//RENDER_MODE_FULL_FILL_SCREEN//RENDER_MODE_ADJUST_RESOLUTION
        mTXLivePlayer.enableHardwareDecode(false);
        mTXLivePlayer.setMute(true);

        //极速模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setCacheTime(1);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
        //设置播放器重连间隔. 秒
        mPlayConfig.setConnectRetryInterval(10);
        mTXLivePlayer.setConfig(mPlayConfig);
    }

    @Override
    public void fetchData() {

    }

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


    @OnClick({R.id.mIvBack, R.id.mIvImg, R.id.mTvFouse, R.id.mIvZan, R.id.mIvComm, R.id.mIvShare, R.id.mTvComm, R.id.mVideoView, R.id.mTvFinish, R.id.mTvGoTo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvGoTo://付费观看按钮
                if (mHnVideoBiz != null) mHnVideoBiz.getVideoUrl(mSwitchData.getId());
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
                clickFouces();
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
            case R.id.mVideoView:
                if (!mCanClick) return;
                if (mPlaying) {
                    if (mVideoPause) {
                        mTXLivePlayer.resume();
                        if (mIvPlay != null) {
                            mIvPlay.setVisibility(View.GONE);
                        }
                    } else {
                        mTXLivePlayer.pause();
                        if (mIvPlay != null) {
                            mIvPlay.setVisibility(View.VISIBLE);
                        }

                    }
                    mVideoPause = !mVideoPause;
                } else {
                    if (mIvPlay != null) {
                        mIvPlay.setVisibility(View.GONE);
                    }
                    startPlay();
                }
                break;
        }
    }

    private void startPlay() {
        if (!TextUtils.isEmpty(mPlayUrl) && mTXLivePlayer != null) {
            checkPlayUrl();
            mTXLivePlayer.setPlayListener(this);

            mTXLivePlayer.startPlay(mPlayUrl, mUrlPlayType);
            mPlaying = true;
            mVideoPause = false;
        }

    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);

            mPlaying = false;
        }
    }

    private int checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            HnToastUtils.showToastShort("播放地址不合法!");
        }

        if (mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) {
            if (mPlayUrl.contains(".flv")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
            } else if (mPlayUrl.contains(".m3u8")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
            } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            } else {
                HnToastUtils.showToastShort("播放地址不合法!");
            }
        } else {
            HnToastUtils.showToastShort("播放地址不合法!");
        }
        return mUrlPlayType;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!mVideoPause && mTXLivePlayer != null) {

            mTXLivePlayer.resume();
        } else {
            startPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null)
            mVideoView.onPause();
        if (mTXLivePlayer != null)
            mTXLivePlayer.pause();
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
            mVideoView.onDestroy();
            mVideoView = null;
        }
    }


    public void hintMsg() {
        //打开软键盘
        if (mActivity == null) return;
        if (mInputTextMsgDialog != null && mInputTextMsgDialog.isShowing()) {
            mInputTextMsgDialog.dismiss();
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (mActivity == null) return;
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            if (mIvPlay != null) {
                mIvPlay.setVisibility(View.GONE);
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {


        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            if (mIvPlay != null) {
                mIvPlay.setVisibility(View.VISIBLE);
            }
            if (mIvBg != null) {
                mIvBg.setVisibility(View.VISIBLE);
            }
            stopPlay(false);

//            mTXLivePlayer.seek(0);
//            startPlay();
            mVideoPause = false;

        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            if (mIvPlay != null) mIvPlay.setVisibility(View.GONE);
            if (mIvBg != null) mIvBg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    /**
     * 滑动切换视频
     *
     * @param bean
     */
    public void swicthVideo(HnVideoRoomSwitchModel.DBean bean) {
        HnHttpUtils.cancelRequest(HnUrl.VIDEO_APP_VIDEO_DETAIL + mVideoId);
        HnHttpUtils.cancelRequest(HnUrl.VIDEO_APP_VIDEO_URL + mVideoId);
        clearData();
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
    private void setMessgae() {
        if (mDbean == null && mTvZan != null) return;
        mTvZan.setText(HnUtils.setNoPoint(mDbean.getLike_num()));
        mTvShare.setText(HnUtils.setNoPoint(mDbean.getShare_num()));
        mTvComm.setText(HnUtils.setNoPoint(mDbean.getReply_num()));
        mTvTitle.setText(mDbean.getTitle());
        mTvName.setText(mDbean.getUser_nickname());//缺少name
        mIvImg.setController(FrescoConfig.getHeadController(mDbean.getUser_avatar()));

        setFoucesState();
    }

    /**
     * 设置关注状态
     */
    private void setFoucesState() {
        if (mDbean == null || mTvFouse == null) return;
        if (HnApplication.getmUserBean().getUser_id().equals(mDbean.getUser_id()))
            mTvFouse.setVisibility(View.GONE);
        if (mDbean.isIs_follow()) {
            mTvFouse.setText(R.string.main_follow_on);
        } else {
            mTvFouse.setText(R.string.add_fouces_chat);
        }
    }

    /**
     * 点赞
     */
    private void clickFouces() {
        if (mDbean.isIs_follow()) {
            HnUserControl.cancelFollow(mDbean.getUser_id(), new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (mActivity == null) return;
                    mDbean.setIs_follow(false);
                    EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFoucesState();
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
                    setFoucesState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (mActivity == null) return;
                    HnToastUtils.showToastShort(msg);
                }
            });
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
                setMessgae();
                getVideoUrlAndPay();
            }
        } else if (HnVideoBiz.VideoUrl.equals(type)) {
            HnVideoUrlModel model = (HnVideoUrlModel) obj;
            if (model.getD() != null && !TextUtils.isEmpty(model.getD().getUrl())) {
                if (mRlPayDialog != null) mRlPayDialog.setVisibility(View.GONE);
                mPlayUrl = model.getD().getUrl();
                mCanClick = true;
                startPlay();
            }
        } else if (HnVideoBiz.VideoZan.equals(type)) {
            if (mDbean == null || TextUtils.isEmpty(mDbean.getLike_num())) return;
            try {
                mDbean.setIs_like(true);
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
        if (HnVideoBiz.VideoDetail.equals(type) || HnVideoBiz.VideoZan.equals(type) || HnVideoBiz.VideoComm.equals(type)
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
