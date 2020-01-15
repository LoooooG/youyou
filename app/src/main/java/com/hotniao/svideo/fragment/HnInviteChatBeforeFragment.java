package com.hotniao.svideo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnFileUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnInviteChatPreviewActivity;
import com.hotniao.svideo.activity.HnMyRechargeActivity;
import com.hotniao.svideo.activity.HnPrivateChatActivity;
import com.hotniao.svideo.activity.HnUserHomeActivity;
import com.hotniao.svideo.biz.chat.HnFastChatBiz;
import com.hotniao.svideo.model.HnGetChatAnchorInfoModel;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.HnReceiveVideoChatBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.videolibrary.videochat.model.HnInvateChatVideoModel;
import com.videolibrary.videochat.ui.HnOnlineVideoChatActivity;

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

public class HnInviteChatBeforeFragment extends BaseFragment implements ITXLivePlayListener, BaseRequestStateListener {
    private static final String TAG = "HnInviteChatBeforeActivity";
    @BindView(R.id.mVideoView)
    TXCloudVideoView mVideoView;
    @BindView(R.id.mIvHead)
    FrescoImageView mIvHead;
    @BindView(R.id.mIvImg)
    FrescoImageView mIvBg;
    @BindView(R.id.mTvName)
    TextView mTvName;
    @BindView(R.id.mTvId)
    TextView mTvId;
    @BindView(R.id.mIvPlay)
    ImageView mIvPlay;
//    @BindView(R.id.mTvMoney)
//    TextView mTvMoney;
    @BindView(R.id.mTvStar)
    TextView mTvStar;
    @BindView(R.id.mTvTitle)
    TextView mTvTitle;
    @BindView(R.id.mTvFouse)
    TextView mTvFouse;


    private String mUid = "";
    //在线离线状态
    private String type;

    private String mPlayUrl;
    private boolean mVideoPause = false;
    private boolean mPlaying = false;
    private int mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;

    private TXVodPlayer mTXLivePlayer;
    private TXVodPlayConfig mPlayConfig = new TXVodPlayConfig();

    private HnFastChatBiz mFastChatBiz;

    private HnGetChatAnchorInfoModel.DBean mDbean;

    private String errMsg = "";
    private boolean isShowDialog = false;

    public static HnInviteChatBeforeFragment newInstance(String userId,String type,String avatar){
        HnInviteChatBeforeFragment fragment = new HnInviteChatBeforeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        bundle.putString("type",type);
        bundle.putString("avatar",avatar);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_invite_chat_before;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFastChatBiz = new HnFastChatBiz(mActivity);
        mFastChatBiz.setBaseRequestStateListener(this);
        Bundle arguments = getArguments();
        if(arguments != null){
            mUid = arguments.getString("userId");
            type = arguments.getString("type");
            mIvBg.setController(FrescoConfig.getController(arguments.getString("avatar")));
            mIvHead.setController(FrescoConfig.getController(arguments.getString("avatar")));
            mTvId.setText("ID：" + mUid);
        }
        setConfig();
    }

    @Override
    protected void initData() {
        mFastChatBiz.getChatUserInfo(mUid);
    }

    @OnClick({R.id.mIvHead, R.id.mIvClose, R.id.mVideoView, R.id.mTvPrivateMessage, R.id.mTvDetail, R.id.mTvChatVideo, R.id.mTvFouse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvHead:
                if(getActivity() instanceof HnInviteChatPreviewActivity){
                    ((HnInviteChatPreviewActivity)getActivity()).setCurrentPage(1);
                }
                break;
            case R.id.mIvClose:
                mActivity.finish();
                break;
            case R.id.mVideoView:
                if (mDbean == null) return;
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
                    //
//                    HnUserHomeActivity.luncher(this, mUid);
//                    overridePendingTransition(R.anim.user_home_activity_enter, R.anim.invite_chat_activity_exit);

                } else {
                    if (mIvPlay != null) {
                        mIvPlay.setVisibility(View.GONE);
                    }
                    startPlay();
                }
                break;
            case R.id.mTvPrivateMessage:
                if (mUid == null || mDbean == null) return;
                if (HnApplication.getmUserBean().getUser_id().equals(mUid))
                    return;
                Bundle bundle = new Bundle();
                bundle.putString(HnLiveConstants.Intent.DATA, mUid);
                bundle.putString(HnLiveConstants.Intent.Name, mDbean.getUser_nickname());
                bundle.putString(HnLiveConstants.Intent.ChatRoomId, mDbean.getChat_room_id());
                startActivity(new Intent(mActivity, HnPrivateChatActivity.class).putExtras(bundle));
                break;
            case R.id.mTvDetail:
                if (!TextUtils.isEmpty(mUid))
                    if(getActivity() instanceof HnInviteChatPreviewActivity){
                        ((HnInviteChatPreviewActivity)getActivity()).setCurrentPage(1);
                    }
                break;
            case R.id.mTvChatVideo:
                if (mDbean == null) return;
                if ("0".equals(type)) {
                    CommDialog.newInstance(mActivity).setClickListen(new CommDialog.OneSelDialog() {
                        @Override
                        public void sureClick() {

                        }
                    }).setTitle(HnUiUtils.getString(R.string.fast_chat_add)).setContent("主播当前不接受视频聊天噢~").setRightText(HnUiUtils.getString(R.string.i_know)).show();
                    return;
                }
                if (HnApplication.getmUserBean() == null) {
                    HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
                        @Override
                        public void onSuccess(String uid, HnLoginModel model, String response) {
                            invite();
                        }

                        @Override
                        public void onError(int errCode, String msg) {
                            HnToastUtils.showCenterToast(msg);
                        }
                    });
                } else {
                    invite();
                }

                break;
            case R.id.mTvFouse:
                if (mDbean == null) return;
                clickFouces();
                break;
        }
    }

    private void invite() {
        if (Double.parseDouble(HnApplication.getmUserBean().getUser_coin()) < Double.parseDouble(mDbean.getPrice())) {
            CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {

                }

                @Override
                public void rightClick() {
                    mActivity.openActivity(HnMyRechargeActivity.class);
                }
            }).setTitle(HnUiUtils.getString(R.string.live_letter_bal_not_enough)).setContent(getString(R.string.not_enough_recharge_can_chat_fast)).setLeftText(getString(R.string.cruel_refused_to)).setRightText(HnUiUtils.getString(R.string.live_go_excharge)).show();
            return;
        }
        if (PermissionHelper.isCameraUseable() && PermissionHelper.isAudioRecordable()) {
            mFastChatBiz.inviteAnchorChat(mDbean.getUser_id());
        } else {
            CommDialog.newInstance(mActivity).setClickListen(new CommDialog.OneSelDialog() {
                @Override
                public void sureClick() {
                }
            }).setTitle(HnUiUtils.getString(com.videolibrary.R.string.main_chat)).setContent("请在设置中，允许多多视频访问你的相机和麦克风权限")
                    .setRightText(HnUiUtils.getString(com.videolibrary.R.string.i_know)).show();
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
                    if (mActivity.isFinishing()) return;
                    mDbean.setIs_follow(false);
                    EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFoucesState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (mActivity.isFinishing()) return;
                    HnToastUtils.showToastShort(msg);
                }
            });
        } else {
            HnUserControl.addFollow(mDbean.getUser_id(), null, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    if (mActivity.isFinishing()) return;
                    mDbean.setIs_follow(true);
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFoucesState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    if (mActivity.isFinishing()) return;
                    HnToastUtils.showToastShort(msg);
                }
            });
        }
    }

    private void setConfig() {
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXVodPlayer(mActivity);
        }
        mTXLivePlayer.setPlayerView(mVideoView);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mTXLivePlayer.setPlayListener(this);

        //极速模式
//        mPlayConfig.setAutoAdjustCacheTime(true);
//        mPlayConfig.setMinAutoAdjustCacheTime(1);
//        mPlayConfig.setMaxAutoAdjustCacheTime(1);
        //设置播放器重连间隔. 秒
        mPlayConfig.setConnectRetryInterval(10);
        mTXLivePlayer.setConfig(mPlayConfig);
    }

    private void startPlay() {

        if (!TextUtils.isEmpty(mPlayUrl)) {
            if (mTXLivePlayer == null) return;
            mTXLivePlayer.startPlay(mPlayUrl);
            mPlaying = true;
        } else {
            if (mIvBg != null) mIvBg.setVisibility(View.VISIBLE);
            if (mIvPlay != null) mIvPlay.setVisibility(View.GONE);
        }

    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            if (clearLastFrame)
                mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);

            mPlaying = false;
        }
    }

    private void checkPlayUrl() {
        if (mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) {
            if (mPlayUrl.contains(".flv")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
            } else if (mPlayUrl.contains(".m3u8")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
            } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mVideoView.onResume();
        if (!mVideoPause && mTXLivePlayer != null) {
            mTXLivePlayer.resume();
        } else {
            startPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mVideoView.onPause();
        if (mTXLivePlayer != null)
            mTXLivePlayer.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.onDestroy();
            mVideoView = null;
        }

        stopPlay(true);
        mTXLivePlayer = null;

    }

    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {


        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            if (mIvBg != null) mIvBg.setVisibility(View.GONE);
            if (mIvPlay != null) mIvPlay.setVisibility(View.GONE);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            /*if (mTXLivePlayer != null) mTXLivePlayer.seek(0);
            if (mIvBg != null) mIvBg.setVisibility(View.VISIBLE);
            if (mIvPlay != null) mIvPlay.setVisibility(View.VISIBLE);
            mVideoPause = false;
            stopPlay(false);*/
            startPlay();
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    private void setMessage() {
        if (mDbean == null) return;
        mIvHead.setController(FrescoConfig.getController(mDbean.getUser_avatar()));
        mTvName.setText(mDbean.getUser_nickname());
        mTvTitle.setText(mDbean.getUser_intro());
        if (TextUtils.isEmpty(mDbean.getUser_constellation())) {
            mTvStar.setVisibility(View.INVISIBLE);
        } else {
            mTvStar.setVisibility(View.VISIBLE);
            mTvStar.setText(mDbean.getUser_constellation());
        }


//        mTvMoney.setText(String.format(HnUiUtils.getString(R.string.fast_chat_need_money_more), mDbean.getPrice(), mDbean.getCoin_name()));
        if (!TextUtils.isEmpty(mDbean.getUser_video_cover())) {
            mIvBg.setController(FrescoConfig.getController(mDbean.getUser_video_cover()));
        } else if (!TextUtils.isEmpty(mDbean.getUser_video())) {
            mIvBg.setImageBitmap(HnFileUtils.createVideoThumbnail(mDbean.getUser_video(), 200, 200));
        } else {
            mIvBg.setController(FrescoConfig.getController(mDbean.getUser_avatar()));
        }

        setFoucesState();


    }

    /**
     * 设置关注状态
     */
    private void setFoucesState() {
        if (mDbean == null || mTvFouse == null) return;

        if (mDbean.isIs_follow()) {
            mTvFouse.setText(R.string.main_follow_on);
        } else {
            mTvFouse.setText(R.string.add_fouces_chat);
        }
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity.isFinishing()) return;
        if (HnFastChatBiz.ChatAnchorInfo.equals(type)) {
            HnGetChatAnchorInfoModel model = (HnGetChatAnchorInfoModel) obj;
            if (model != null && model.getD() != null) {
                mDbean = model.getD();
                mPlayUrl = model.getD().getUser_video();
                checkPlayUrl();
                startPlay();
                setMessage();
            }
        } else if (HnFastChatBiz.InviteAnchorChat.equals(type)) {
            HnInvateChatVideoModel model = (HnInvateChatVideoModel) obj;
            if (model.getD() == null) return;
            final HnReceiveVideoChatBean.DataBean roomInfo = new HnReceiveVideoChatBean.DataBean();
            HnLogUtils.d(TAG,"mDbean:"+mDbean.toString());
            roomInfo.setF_user_avatar(mDbean.getUser_avatar());
            roomInfo.setF_user_id(mDbean.getUser_id());
            roomInfo.setF_user_nickname(mDbean.getUser_nickname());
            roomInfo.setPrice(mDbean.getPrice());
            roomInfo.setChat_log(model.getD().getChat_log());
            roomInfo.setPushUrl(model.getD().getPush_url());
            HnLogUtils.d(TAG,"roomInfo:"+roomInfo.toString());
            Bundle bundle = new Bundle();
            bundle.putParcelable("roomInfo", roomInfo);
            bundle.putString("userID", HnApplication.getmUserBean().getUser_id());
            bundle.putBoolean("createRoom", false);
            startActivity(new Intent(mActivity, HnOnlineVideoChatActivity.class).putExtras(bundle));
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnFastChatBiz.ChatAnchorInfo.equals(type)) {
            HnToastUtils.showToastShort(msg);
        } else if (HnFastChatBiz.InviteAnchorChat.equals(type)) {
            if (HnServiceErrorUtil.NOT_ONLINE == code) {
                errMsg = "主播当前不接受视频聊天噢~";
                isShowDialog = true;
            } else if (HnServiceErrorUtil.DO_NOT_DISTURB == code || HnServiceErrorUtil.IS_BUSYING == code) {
                errMsg = "主播当前较忙，试试与其他主播视频聊天吧~";
                isShowDialog = true;
            } else if (HnServiceErrorUtil.IN_BLACK == code) {
                errMsg = "要先将Ta从黑名单中解除哦~";
                isShowDialog = false;
            } else if (HnServiceErrorUtil.HAS_BEEN_BLACKED == code) {
                errMsg = "对方已将你拉黑了~";
                isShowDialog = false;
            } else {
                isShowDialog = false;
                errMsg = msg;
            }
            if (isShowDialog)
                CommDialog.newInstance(mActivity).setClickListen(new CommDialog.OneSelDialog() {
                    @Override
                    public void sureClick() {

                    }
                }).setTitle(HnUiUtils.getString(R.string.fast_chat_add)).setContent(errMsg)
                        .setRightText(HnUiUtils.getString(R.string.i_know)).show();

            else HnToastUtils.showCenterToast(msg);
        }
    }
}
