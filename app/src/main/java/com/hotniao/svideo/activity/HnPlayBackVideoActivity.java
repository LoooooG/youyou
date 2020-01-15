package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.svideo.biz.chat.HnFastChatBiz;
import com.hotniao.svideo.biz.share.HnShareBiz;
import com.hotniao.svideo.biz.user.userinfo.HnMineBiz;
import com.hotniao.svideo.dialog.HnShareDialog;
import com.hotniao.svideo.model.HnGetChatAnchorInfoModel;
import com.hotniao.svideo.model.HnPlayBackNoticeModel;
import com.hotniao.svideo.model.HnPlayBackUrlModel;
import com.hotniao.svideo.model.HnShareRuleModel;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.loopj.android.http.RequestParams;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：播放回放视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnPlayBackVideoActivity extends BaseActivity implements ITXLivePlayListener, BaseRequestStateListener {
    @BindView(R.id.mVideoView)
    TXCloudVideoView mVideoView;
    @BindView(R.id.mIvPlay)
    ImageView mIvPlay;
    @BindView(R.id.mSeekbar)
    SeekBar mSeekBar;
    @BindView(R.id.mIvImg)
    FrescoImageView mIvImg;
    @BindView(R.id.mIvClose)
    ImageView mIvClose;
    @BindView(R.id.mIvHead)
    FrescoImageView mIvHead;
    @BindView(R.id.mTvName)
    TextView mTvName;
    @BindView(R.id.mTvFouse)
    TextView mTvFouse;
    @BindView(R.id.mTvId)
    TextView mTvId;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_notice)
    TextView tvNotice;

    //点播相关
    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private boolean mVideoPause = false;
    private boolean mPlaying = false;

    private String mPlayUrl;
    private String userId;
    private int mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;

    private TXLivePlayer mTXLivePlayer;
    private TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();

    private HnFastChatBiz mFastChatBiz;
    private HnGetChatAnchorInfoModel.DBean mDbean;
    private HnShareBiz shareBiz;
    private HnShareRuleModel.DBean ruleModel;

    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;

    /**
     * 跳转
     *
     * @param activity
     * @param uid       用户id
     * @param liveIdUrl 直播记录id 或者链接
     * @param type      类型  1  直播  2   其他
     */
    public static void luncher(Activity activity, String uid, String liveIdUrl, int type, String avater) {
        activity.startActivity(new Intent(activity, HnPlayBackVideoActivity.class).putExtra("uid", uid).putExtra("id", liveIdUrl)
                .putExtra("type", type).putExtra("avater", avater));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_play_back_video;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        mPlayUrl = getIntent().getStringExtra("id");
        userId = getIntent().getStringExtra("uid");
        mFastChatBiz = new HnFastChatBiz(this);
        mFastChatBiz.setBaseRequestStateListener(this);
        shareBiz = new HnShareBiz(this);
        shareBiz.setBaseRequestStateListener(this);

        mShareAPI = UMShareAPI.get(this);
        mShareAction = new ShareAction(this);

        mIvImg.setController(FrescoConfig.getController(getIntent().getStringExtra("avater")));
        if (1 == getIntent().getIntExtra("type", 2)) {
            getPlayVideoUrl(mPlayUrl);
        } else {
            checkPlayUrl();
            startPlay();
        }

        mTvId.setText("ID：" + userId);
        getNotice();
    }

    private void getPlayVideoUrl(String liveId) {
        RequestParams params = new RequestParams();
        params.put("anchor_live_log_id", liveId);
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_GET_PALY_BACK_URL, params, "VIDEO_APP_GET_PALY_BACK_URL", new HnResponseHandler<HnPlayBackUrlModel>(HnPlayBackUrlModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (0 == model.getC()) {
                    mPlayUrl = model.getD().getUrl();
                    checkPlayUrl();
                    startPlay();
                } else {
                    HnToastUtils.showToastShort(model.getM());
                    finish();
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                HnToastUtils.showToastShort(msg);
                finish();
            }
        });
    }

    private void getNotice() {
        HnHttpUtils.getRequest(HnUrl.PLAY_BACK_NOTICE, null, "PLAY_BACK_NOTICE", new HnResponseHandler<HnPlayBackNoticeModel>(HnPlayBackNoticeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (0 == model.getC()) {
                    String notice = "公告：" + model.getD().getNotice();
                    SpannableString text = new SpannableString(notice);
                    text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_color)),0,3,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvNotice.setText(text);
                } else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void getInitData() {
        mFastChatBiz.getChatUserInfo(userId);
        shareBiz.shareRule();
        setLisener();
    }

    private void setLisener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                tvStartTime.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, (progress % 3600) % 60));
                tvEndTime.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", seekBar.getMax() / 3600, (seekBar.getMax() % 3600) / 60, (seekBar.getMax() % 3600) % 60));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTXLivePlayer.seek(seekBar.getProgress());
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });

    }


    @OnClick({R.id.mIvPlay, R.id.mIvClose, R.id.mIvHead, R.id.mTvFouse,R.id.iv_share})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mIvPlay:
                if (mPlaying) {
                    if (mVideoPause) {
                        mTXLivePlayer.resume();
                        if (mIvPlay != null) {
                            mIvPlay.setBackgroundResource(R.mipmap.playback_begin_nor);
                        }
                    } else {
                        mTXLivePlayer.pause();
                        if (mIvPlay != null) {
                            mIvPlay.setBackgroundResource(R.mipmap.playback_stop_nor);
                        }
                    }
                    mVideoPause = !mVideoPause;
                } else {
                    if (mIvPlay != null) {
                        mIvPlay.setBackgroundResource(R.mipmap.playback_begin_nor);
                    }
                    startPlay();
                }
                break;
            case R.id.mIvClose:
                finish();
                break;
            case R.id.mIvHead:
                HnUserHomeActivity.luncher(this, userId);
                break;
            case R.id.mTvFouse:
                if (mDbean == null) return;
                clickFouces();
                break;
            case R.id.iv_share:
                if(ruleModel != null){
                    HnShareRuleModel.DBean.ShareBean shareBean = ruleModel.getShare();
                    HnShareDialog.newInstance(mShareAPI, mShareAction, shareBean.getContent(), shareBean.getLogo(), shareBean.getUrl(), shareBean.getTitle()).show(getFragmentManager(), "share");
                }
                break;
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
                    mDbean.setIs_follow(false);
                    EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFoucesState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    HnToastUtils.showToastShort(msg);
                }
            });
        } else {
            HnUserControl.addFollow(mDbean.getUser_id(), null, new HnUserControl.OnUserOperationListener() {
                @Override
                public void onSuccess(String uid, Object obj, String response) {
                    mDbean.setIs_follow(true);
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Follow, mDbean.getUser_id()));
                    setFoucesState();
                }

                @Override
                public void onError(String uid, int errCode, String msg) {
                    HnToastUtils.showToastShort(msg);
                }
            });
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            if (mIvPlay != null) {
                mIvPlay.setBackgroundResource(R.mipmap.playback_begin_nor);
            }
            if (mIvImg != null) mIvImg.setVisibility(View.GONE);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (mSeekBar != null) {
                mSeekBar.setProgress(progress);
            }
            tvStartTime.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, (progress % 3600) % 60));
            tvEndTime.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 3600) % 60));

            if (mSeekBar != null) {
                mSeekBar.setMax(duration);
            }
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_WARNING_RECONNECT) {
            if (mIvImg != null) mIvImg.setVisibility(View.VISIBLE);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlay(false);
            mVideoPause = false;
            tvStartTime.setText(String.format(Locale.CHINA, "%s", "00:00:00"));
            tvEndTime.setText(String.format(Locale.CHINA, "%s", "00:00:00"));
            if (mSeekBar != null) {
                mSeekBar.setProgress(0);
            }
            if (mIvPlay != null) {
                mIvPlay.setBackgroundResource(R.mipmap.playback_stop_nor);
            }
        }
    }


    @Override
    public void onNetStatus(Bundle status) {
//        if (mVideoView != null) {
//            mVideoView.setLogText(status, null, 0);
//        }
//        if (status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
//            if (mTXLivePlayer != null)
//                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//        } else if (mTXLivePlayer != null)
//            mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    private void startPlay() {
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(this);
        }
        mTXLivePlayer.setPlayerView(mVideoView);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mTXLivePlayer.setPlayListener(this);

        //极速模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
//        设置播放器重连间隔. 秒
        mPlayConfig.setConnectRetryInterval(10);
        mTXLivePlayer.setConfig(mPlayConfig);
        mTXLivePlayer.startPlay(mPlayUrl, mUrlPlayType);

        mPlaying = true;

    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);

            mPlaying = false;
        }
    }

    private void checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "播放地址不合法", Toast.LENGTH_SHORT).show();
        }

        if (mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) {
            if (mPlayUrl.contains(".flv")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
            } else if (mPlayUrl.contains(".m3u8")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
            } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            } else {
                Toast.makeText(getApplicationContext(), "播放地址不合法", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "播放地址不合法", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
        if (!mVideoPause && mTXLivePlayer != null) {
            mTXLivePlayer.resume();
        } else {
            startPlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null)
            mVideoView.onPause();
        if (mTXLivePlayer != null)
            mTXLivePlayer.pause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay(true);
        if (mVideoView != null) {
            mVideoView.onDestroy();
            mVideoView = null;
        }


        if (mTXLivePlayer != null)
            mTXLivePlayer = null;

    }

    @Override
    public void requesting() {

    }

    private void setMessage() {
        if (mDbean == null) return;
        mIvHead.setController(FrescoConfig.getController(mDbean.getUser_avatar()));
        mTvName.setText(mDbean.getUser_nickname());
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
    public void requestSuccess(String type, String response, Object obj) {
        if (HnFastChatBiz.ChatAnchorInfo.equals(type)) {
            HnGetChatAnchorInfoModel model = (HnGetChatAnchorInfoModel) obj;
            if (model != null && model.getD() != null) {
                mDbean = model.getD();
                setMessage();
            }
        }else if(HnShareBiz.SHARE_RULE.equals(type)){
            ruleModel = (HnShareRuleModel.DBean) obj;
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        HnToastUtils.showToastShort(msg);
    }

}
