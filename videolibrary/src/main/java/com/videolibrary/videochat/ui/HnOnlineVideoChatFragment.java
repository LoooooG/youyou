package com.videolibrary.videochat.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.adapter.HnLiveMessageAdapter;
import com.hotniao.livelibrary.biz.anchor.HnAnchorInfoListener;
import com.hotniao.livelibrary.biz.gift.HnGiftBiz;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseListener;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.giflist.HnDonwloadGiftStateListener;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.HnReceiveVideoChatBean;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.anchor.activity.HnAnchorActivity;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;
import com.hotniao.livelibrary.ui.gift.GiftDialog;
import com.hotniao.livelibrary.widget.gift.GiftManage;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.hotniao.livelibrary.widget.gift.LeftGiftsItemLayout;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftChannel;
import com.imlibrary.TCChatRoomMgr;
import com.lqr.emoji.EmotionLayout;
import com.tencent.TIMMessage;
import com.videolibrary.R;
import com.videolibrary.model.HnFeeDeductionModel;
import com.videolibrary.videochat.biz.HnVideoChatBiz;
import com.videolibrary.videochat.biz.HnVideoChatViewBiz;
import com.videolibrary.videochat.entity.FuzzyEvent;
import com.videolibrary.videochat.model.HnInvateChatVideoModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class HnOnlineVideoChatFragment extends BaseFragment implements View.OnClickListener, BaseRequestStateListener, HnDonwloadGiftStateListener, HnLiveBaseListener, View.OnLayoutChangeListener, View.OnTouchListener, HnAnchorInfoListener {

    public MediaPlayer mMediaPlayer;
    private RelativeLayout mRlParent;
    //顶部头像
    private RelativeLayout mRlHead;
    private TextView mTvBalance;
    private FrescoImageView mIvHead;
    private TextView mTvName;
    private TextView mTvHeadContent;
    //等待对方接听
    private RelativeLayout mRlWaitAns;
    private TextView mTvWaitAnsNo;
    private TextView mTvWaitAnsFuzzy;
    private TextView mTvWaitAnsCancle;
    //是否接听
    private RelativeLayout mRlAns;
    private TextView mTvAnsCancle;
    private ImageView mIvAnsBg;
    private TextView mTvAnswer;
    //正在聊天
    private LinearLayout mLlChat, mLLChatFuzzy;
    private RelativeLayout mRlBottomFun;
    private TextView mTvChatFuzzy;
    private TextView mTvChatMoney;
    private RelativeLayout mRlChatMsg, mRlContent;
    private ToggleButton mToBtn;
    private EditText mEtChat;
    private ImageView mIvEmoji;
    private TextView mTvSend;
    private LeftGiftControlLayout mGiftLay;
    private LeftGiftsItemLayout mGiftItem1, mGiftItem2;
    private ListView mLvChat;
    private TextView mTvChatMsg;
    private TextView mTvChatOver;
    private TextView mTvChatTime;
    private TextView mTvChatGift, mTvChatCancle, mTvChatRenew;
    private EmotionLayout mElEmotion;
    private BigGiftChannel mBigGift;
    /**
     * 礼物管理器
     */
    private BigGiftActionManager mBigGiftActionManager;
    private HnVideoChatBiz mVideoChatBiz;
    private HnVideoChatViewBiz mVideoChatViewBiz;
    /**
     * 房间Id
     */
    private String mRoomID;
    /**
     * 房间链接成功
     */
    private boolean mChatConnectSuccess = false;
    /**
     * 腾讯IM
     */
    private TCChatRoomMgr mTCChatRoomMgr;
    private Gson mGson;
    /**
     * 公聊消息
     */
    private ArrayList<HnReceiveSocketBean> mMessageData = new ArrayList<>();
    private HnLiveMessageAdapter mMessageAdapter;
    /**
     * 礼物列表
     */
    private ArrayList<GiftListBean> mGifts = new ArrayList<>();
    /**
     * 该类主要是获取礼物数据，从而进行礼物动画
     */
    private HnGiftBiz mHnGiftBiz;
    /**
     * 礼物对话框
     */
    private GiftDialog mGiftDialog;
    private HnReceiveVideoChatBean.DataBean mDbean;
    private int keyHeight;
    /**
     * 计费次数
     */
    private Double mTotalPay = Double.valueOf(0);

    /**
     * 是否模糊自己（请求者）
     */
    private boolean isFuzzyMe = false;

    /**
     * 小于五分钟第一次提示
     */
    private boolean isFirstToast = true;
    /**
     * handler处理线程
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (mActivity == null || msg.obj == null || mVideoChatViewBiz == null) return;
                if (HnWebscoketConstants.Handler_Join == msg.what) {
                    /** 用户进入 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData() != null && bean.getData().getFuser() != null) {
                        String id = bean.getData().getFuser().getUser().getUser_id();

                    }
                } else if (HnWebscoketConstants.Handler_Public_Msg == msg.what) {
                    /** 公聊 */
                    mMessageData = mVideoChatViewBiz.addMsgData(msg.obj, mMessageData);
                    initMessageAdapter();

                } else if (HnWebscoketConstants.Handler_Send_Gift == msg.what) {
                    /** 接收到发送礼物的数据 */
                    HnReceiveSocketBean bean = mVideoChatViewBiz.getGiftData(msg.obj, mGifts);
                    if (bean != null) {
                        //更新消息列表
                        mMessageData = mVideoChatViewBiz.addMsgData(msg.obj, mMessageData);
                        initMessageAdapter();
                        //触发礼物动画
                        boolean isHavaGift = mVideoChatViewBiz.loadGift(bean, mActivity, mBigGiftActionManager, mGiftLay, mGifts, mHnGiftBiz);
                        if (!isHavaGift) {
                            mHnGiftBiz.requestToGetGiftList();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    public static HnOnlineVideoChatFragment newInstance(HnReceiveVideoChatBean.DataBean roomInfo) {
        HnOnlineVideoChatFragment fragment = new HnOnlineVideoChatFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("roomInfo", roomInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initOther() {


        //礼物列表业务逻辑类
        mHnGiftBiz = new HnGiftBiz(mActivity, this, this);
        //获取礼物列表数据
        mHnGiftBiz.getGiftListData();
        //礼物管理器初始化
        GiftManage.init(mActivity);

        mBigGiftActionManager = new BigGiftActionManager();
        mBigGift.setDanAction(mBigGiftActionManager);
        mBigGiftActionManager.addChannel(mBigGift);

        mElEmotion.attachEditText(mEtChat);
        mElEmotion.setEmotionAddVisiable(false);
        mElEmotion.setEmotionSettingVisiable(false);
        //获取屏幕高度
        int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //表情 键盘和输入框之间的处理
        mVideoChatViewBiz.initEmotionKeyboard(mActivity, mRlContent, mIvEmoji, mEtChat, mElEmotion);


    }

    private void initView(View view) {

        //头部视图
        mRlParent = (RelativeLayout) mRootView.findViewById(R.id.mRlParent);
        mRlParent.addOnLayoutChangeListener(this);
        mRlParent.setOnTouchListener(this);

        //顶部头像
        mRlHead = view.findViewById(R.id.mRlHead);
        mIvHead = view.findViewById(R.id.mIvHead);
        mTvName = view.findViewById(R.id.mTvName);
        mTvHeadContent = view.findViewById(R.id.mTvHeadContent);
        mTvBalance = view.findViewById(R.id.mTvBalance);

        //等待对方接听
        mRlWaitAns = view.findViewById(R.id.mRlWaitAns);
        mTvWaitAnsNo = view.findViewById(R.id.mTvWaitAnsNo);
        mTvWaitAnsFuzzy = view.findViewById(R.id.mTvWaitAnsFuzzy);
        mTvWaitAnsCancle = view.findViewById(R.id.mTvWaitAnsCancle);

        //是否接听
        mRlAns = view.findViewById(R.id.mRlAns);
        mTvAnsCancle = view.findViewById(R.id.mTvAnsCancle);
        mTvAnswer = view.findViewById(R.id.mTvAnswer);
        mIvAnsBg = view.findViewById(R.id.mIvAnsBg);

        //正在聊天
        mLlChat = view.findViewById(R.id.mLlChat);
        mLLChatFuzzy = view.findViewById(R.id.mLLChatFuzzy);
        mRlBottomFun = view.findViewById(R.id.mRlBottomFun);
        mTvChatMoney = view.findViewById(R.id.mTvChatMoney);
        mTvChatFuzzy = view.findViewById(R.id.mTvChatFuzzy);
        mToBtn = view.findViewById(R.id.message_talk_tb);
        mToBtn.setVisibility(View.GONE);

        mRlContent = view.findViewById(R.id.mRlContent);
        mRlChatMsg = view.findViewById(R.id.rl_message);
        mEtChat = view.findViewById(R.id.message_input_et);
        mEtChat.setFocusable(false);
        mEtChat.setFocusableInTouchMode(false);

        mIvEmoji = view.findViewById(R.id.iv_emoji);
        mTvSend = view.findViewById(R.id.message_send_tv);

        mGiftLay = view.findViewById(R.id.giftLl);
        mGiftItem1 = view.findViewById(R.id.gift1);
        mGiftItem2 = view.findViewById(R.id.gift2);

        mLvChat = view.findViewById(R.id.mLvChat);
        mTvChatMsg = view.findViewById(R.id.mTvChatMsg);
        mTvChatTime = view.findViewById(R.id.mTvChatTime);
        mTvChatGift = view.findViewById(R.id.mTvChatGift);
        mTvChatCancle = view.findViewById(R.id.mTvChatCancle);
        mTvChatRenew = view.findViewById(R.id.mTvChatRenew);

        mElEmotion = view.findViewById(R.id.elEmotion);
        mBigGift = view.findViewById(R.id.mBigGift);
        mTvChatOver = view.findViewById(R.id.mTvChatOver);


        mTvWaitAnsFuzzy.setOnClickListener(this);
        mTvWaitAnsCancle.setOnClickListener(this);
        mTvAnsCancle.setOnClickListener(this);
        mTvAnswer.setOnClickListener(this);
        mIvEmoji.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        mTvChatMsg.setOnClickListener(this);
        mTvChatGift.setOnClickListener(this);
        mTvChatCancle.setOnClickListener(this);
        mTvChatRenew.setOnClickListener(this);
        mTvChatFuzzy.setOnClickListener(this);
        setSendMsg();
    }

    /**
     * 公聊EditText监听
     */
    private void setSendMsg() {
        mEtChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                              @Override
                                              public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                  if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                      ((InputMethodManager) mEtChat.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                              .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                                                                      InputMethodManager.HIDE_NOT_ALWAYS);

                                                      String messageContent = mEtChat.getText().toString().trim();
                                                      mVideoChatBiz.sendMessaqge(messageContent, mChatConnectSuccess, mRoomID);
                                                      return true;
                                                  }
                                                  return false;
                                              }
                                          }

        );
        mEtChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(mEtChat.getText().toString())) mTvSend.setSelected(false);
                else mTvSend.setSelected(true);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mTvWaitAnsFuzzy || view.getId() == R.id.mTvChatFuzzy) {//设置自己模糊
            setFuzzy();

        } else if (view.getId() == R.id.mTvWaitAnsCancle) {//请求者等待时取消视频

            setChatOverMine();
        } else if (view.getId() == R.id.mTvAnsCancle) {//接收者拒绝接听
            stopMusic();
            if (mDbean == null) return;
            mVideoChatBiz.refuseChatVideo(mDbean.getChat_log());
            mActivity.finish();

        } else if (view.getId() == R.id.mTvAnswer) {//接收者接听
            if (PermissionHelper.hasPermission(mActivity, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) /*&& PermissionHelper.isAudioRecordable()*/) {
                stopMusic();
                if (mDbean == null) return;
                mTvAnswer.setEnabled(false);
                mVideoChatBiz.acceptChatVideo(mDbean.getChat_log());
            } else {
                CommDialog.newInstance(mActivity).setClickListen(new CommDialog.OneSelDialog() {
                    @Override
                    public void sureClick() {
                        if (mActivity == null || mVideoChatBiz == null) return;
                        stopMusic();
                        if (mDbean == null) return;
                        mVideoChatBiz.refuseChatVideo(mDbean.getChat_log());
                        mActivity.finish();

                    }
                }).setTitle(HnUiUtils.getString(R.string.main_chat)).setContent("请在设置中，允许快点视频访问你的相机和麦克风权限")
                        .setCanceledOnOutside(false).setRightText(HnUiUtils.getString(R.string.i_know)).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mActivity == null || mVideoChatBiz == null) return;
                        stopMusic();
                        if (mDbean == null) return;
                        mVideoChatBiz.refuseChatVideo(mDbean.getChat_log());
                        mActivity.finish();
                    }
                }, 5000);
            }


        } else if (view.getId() == R.id.message_send_tv) {//视频时发送聊天按钮
            ((InputMethodManager) mEtChat.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

            String messageContent = mEtChat.getText().toString().trim();
            mVideoChatBiz.sendMessaqge(messageContent, mChatConnectSuccess, mRoomID);

        } else if (view.getId() == R.id.mTvChatMsg) {//视频时切换文字输入按钮
            mVideoChatViewBiz.showMessageSendLayout(mRlChatMsg, mRlBottomFun, mEtChat, mActivity);

        } else if (view.getId() == R.id.mTvChatGift) {//视频时请求者礼物按钮
            mGiftDialog = GiftDialog.newInstance(HnBaseApplication.getmUserBean().getUser_coin(), mRoomID,mDbean.getChat_log());
            mGiftDialog.show(mActivity.getSupportFragmentManager(), "gift");
            joinRoomAgain();

        } else if (view.getId() == R.id.mTvChatCancle) {//视频时挂断按钮
            if (mDbean == null) return;
            mVideoChatBiz.hangUpChatVideo(mDbean.getChat_log());

        } else if (view.getId() == R.id.mTvChatRenew) {//视频时请求者续费按钮
            ARouter.getInstance().build("/app/HnMyRechargeActivity").navigation();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_online_video_chat;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);


        mVideoChatViewBiz = new HnVideoChatViewBiz(mActivity);
        mVideoChatViewBiz.setListener(this);
        mVideoChatBiz = new HnVideoChatBiz(mActivity);
        mVideoChatBiz.setBaseRequestStateListener(this);


        mMediaPlayer = MediaPlayer.create(mActivity, R.raw.music_chat);//重新设置要播放的音频
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                startMusic();
            }
        });
        mDbean = getArguments().getParcelable("roomInfo");
        if (mDbean.isCreate()) {
            mRoomID = HnBaseApplication.getmUserBean().getUser_id();
        } else {
            mRoomID = mDbean.getF_user_id();
        }
        initView(view);
        initTcIm();
        initOther();

        setInitChatStatue();
        setHeadMessage();
        if (!mDbean.isCreate())
            mVideoChatViewBiz.startWaitTimeTast(mHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTvChatMoney != null && mTvChatMoney.getVisibility() == View.VISIBLE)
            mTvChatMoney.setText(HnUtils.setTwoPoint(HnBaseApplication.getmUserBean().getUser_coin()) + HnBaseApplication.getmConfig().getCoin());
    }

    /**
     * 设置初始化UI
     */
    private void setInitChatStatue() {
        if (mDbean.isCreate()) {
            mRlHead.setVisibility(View.VISIBLE);
            mRlWaitAns.setVisibility(View.GONE);
            mRlAns.setVisibility(View.VISIBLE);
            mLlChat.setVisibility(View.GONE);
        } else {
            mRlHead.setVisibility(View.VISIBLE);
            mRlWaitAns.setVisibility(View.VISIBLE);
            mRlAns.setVisibility(View.GONE);
            mLlChat.setVisibility(View.GONE);
            if (mVideoChatBiz != null) mVideoChatBiz.startHeardBeat(mDbean.getChat_log(), 2);
        }
        startMusic();

    }

    /**
     * 设置聊天log
     *
     * @param chatLog
     */
    public void setChatLog(String chatLog) {
        if (mDbean == null) mDbean = new HnReceiveVideoChatBean.DataBean();
        mDbean.setChat_log(chatLog);
    }

    /**
     * 设置聊天室链接成功失败
     *
     * @param chatConnectSuccess
     */
    public void setChatConnectSuccess(boolean chatConnectSuccess) {
        mChatConnectSuccess = chatConnectSuccess;
    }

    /**
     * 如果推流失败则取消
     */
    public void setCancleChat() {
        if (mDbean == null) return;
        mVideoChatBiz.cancleChatVideo(mDbean.getChat_log(), 1);
    }

    /**
     * 如果推流失败则挂断
     */
    public void setHangUp() {
        if (mDbean == null) return;
        mVideoChatBiz.hangUpChatVideo(mDbean.getChat_log());
    }

    /**
     * 推送聊天室结束
     *
     * @param bean
     */
    public void setChatOver(final HnReceiveVideoChatBean bean) {
        if (mTvChatOver == null || mActivity == null) return;
        mTvChatOver.setVisibility(View.VISIBLE);
        if (mVideoChatViewBiz != null) {
            mVideoChatViewBiz.closeWaitObservable();
            mVideoChatViewBiz.closePayObservable();
            mVideoChatViewBiz.closeChatObservable();
        }
        if (mVideoChatBiz != null) {
            mVideoChatBiz.closeDbservable();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null) return;
                if (mVideoChatViewBiz != null) {
                    mVideoChatViewBiz.closeWaitObservable();
                    mVideoChatViewBiz.closePayObservable();
                    mVideoChatViewBiz.closeChatObservable();
                }
                removeTcImListener();
                HnLogUtils.e("finish_chat-333推送2222--" + bean.getData().toString());
                HnOverChatVideoActivity.luncher(mActivity, mDbean.getF_user_avatar(), bean.getData().getDuration(), bean.getData().getAmount(),
                        bean.getData().getCoin_amount(), mDbean.isCreate());
                mActivity.finish();
            }
        }, 1500);

    }

    /**
     * 设置聊天室结束
     */
    public void setChatOverMine() {
        if (mChatConnectSuccess) {
            if (mDbean == null || mVideoChatBiz == null) return;
            mVideoChatBiz.closeDbservable();
            mVideoChatBiz.hangUpChatVideo(mDbean.getChat_log());
        } else {
            if (mVideoChatViewBiz != null) mVideoChatViewBiz.closeWaitObservable();
            if (mDbean != null && mVideoChatBiz != null) {
                mVideoChatBiz.closeDbservable();
                mVideoChatBiz.cancleChatVideo(mDbean.getChat_log(), 1);
            }
            mActivity.finish();
        }

    }

    public void setBalance(String money) {
        if (mDbean != null && mDbean.isCreate()) {
            if (mTvBalance != null) {
                mTvBalance.setVisibility(View.VISIBLE);
                mTvBalance.setText("余额：" + money + "金币");
            }
        }
    }

    /**
     * 接受者已接收  设置聊天时的UI
     */
    public void setChatUiStatue(boolean isCreate) {
        try {
            if (isCreate) {
                mRlHead.setVisibility(View.GONE);
                mRlWaitAns.setVisibility(View.GONE);
                mRlAns.setVisibility(View.GONE);
                mLlChat.setVisibility(View.VISIBLE);
                mLLChatFuzzy.setVisibility(View.GONE);
                mTvChatMoney.setVisibility(View.GONE);
                mTvChatGift.setVisibility(View.GONE);
                mTvChatRenew.setVisibility(View.GONE);

            } else {
                if (mVideoChatViewBiz != null) {
                    mVideoChatViewBiz.closeWaitObservable();
                    mVideoChatViewBiz.startChatTimeTask();
                    mVideoChatViewBiz.startPayChatTask();
                }
                mRlHead.setVisibility(View.GONE);
                mRlWaitAns.setVisibility(View.GONE);
                mRlAns.setVisibility(View.GONE);
                mLlChat.setVisibility(View.VISIBLE);
                mLLChatFuzzy.setVisibility(View.VISIBLE);
                mTvChatMoney.setVisibility(View.VISIBLE);
                mTvChatGift.setVisibility(View.VISIBLE);
                mTvChatRenew.setVisibility(View.VISIBLE);
                mTvChatMoney.setText(HnUtils.setTwoPoint(HnBaseApplication.getmUserBean().getUser_coin()) + HnBaseApplication.getmConfig().getCoin());
                if (mTvChatMoney.getVisibility() != View.VISIBLE)
                    mTvChatMoney.setVisibility(View.VISIBLE);
            }
            stopMusic();
        } catch (Exception e) {
        }

    }

    private void setHeadMessage() {
        if (mDbean == null) return;
        mIvHead.setController(FrescoConfig.getController(mDbean.getF_user_avatar()));
        mTvName.setText(mDbean.getF_user_nickname());
        if (mDbean.isCreate()) {
            mTvHeadContent.setText("邀请你视频聊天");
        } else {
            mTvHeadContent.setText("正在等待对方接受邀请....");
        }
    }

    /**
     * 设置模糊自己
     */
    private void setFuzzy() {
        if (TextUtils.isEmpty(mRoomID) || mRoomID.equals(HnBaseApplication.getmUserBean().getUser_id()))
            return;
        if (isFuzzyMe) {
            isFuzzyMe = false;
            mTvWaitAnsFuzzy.setSelected(false);
            mTvChatFuzzy.setSelected(false);
            //TODO 不模糊
            EventBus.getDefault().post(new FuzzyEvent(false));
        } else {
            isFuzzyMe = true;
            mTvWaitAnsFuzzy.setSelected(true);
            mTvChatFuzzy.setSelected(true);
            //TODO 模糊
            EventBus.getDefault().post(new FuzzyEvent(true));
        }
        if (mVideoChatBiz != null && mDbean != null) {
            mVideoChatBiz.fuzzyChatVideo(mDbean.getChat_log(), isFuzzyMe ? 1 : 0);
        }
    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化直播间消息适配器
     */
    public void initMessageAdapter() {
        if (mMessageAdapter == null) {
            mMessageAdapter = new HnLiveMessageAdapter(mActivity, mMessageData, mRoomID);
            mLvChat.setAdapter(mMessageAdapter);
        } else {
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 腾讯云
     */
    private void initTcIm() {
        //初始化消息回调
        mTCChatRoomMgr = TCChatRoomMgr.getInstance(mRoomID);
        mTCChatRoomMgr.setMessageListener(new TCChatRoomMgr.TCChatRoomListener() {
            @Override
            public void onJoinGroupCallback(int code, String msg) {
                if (0 == code) mChatConnectSuccess = true;
                else mChatConnectSuccess = false;
            }

            @Override
            public void onSendMsgCallback(int code, TIMMessage timMessage) {
            }

            @Override
            public void onReceiveMsg(int type, String content) {
                HnLogUtils.e("TcIm--" + content);
                matchLiveMsg(content);
            }

            @Override
            public void onGroupDelete(String roomId) {
                if (mActivity == null) return;
                if (!TextUtils.isEmpty(roomId) && roomId.equals(mRoomID)) {
                    //TODO  退出房间
                }
            }
        });
    }

    /**
     * 移除腾讯监听
     */
    private void removeTcImListener() {
        if (mTCChatRoomMgr != null)
            mTCChatRoomMgr.removeMsgListener();
    }

    /**
     * 加入房间未成功重新加入
     */
    private void joinRoomAgain() {
        if (!mChatConnectSuccess && mTCChatRoomMgr != null && !TextUtils.isEmpty(mRoomID))
            mTCChatRoomMgr.joinGroup(mRoomID);
    }

    /**
     * 分配IM推送来的消息
     *
     * @param data
     */
    private void matchLiveMsg(String data) {
        try {
            if (TextUtils.isEmpty(data)) return;
            JSONObject jsonObject = new JSONObject(data);
            String type = jsonObject.getString("type");
            if (mGson == null) mGson = new Gson();

            Message message = mHandler.obtainMessage();
            HnReceiveSocketBean result = mGson.fromJson(data, HnReceiveSocketBean.class);
            message.obj = result;

            if (HnWebscoketConstants.Public_Msg.equals(type)) {//公聊消息
                message.what = HnWebscoketConstants.Handler_Public_Msg;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Join.equals(type)) {//用户进入
                message.what = HnWebscoketConstants.Handler_Join;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Send_Gift.equals(type)) {//接收到发送礼物的数据
                message.what = HnWebscoketConstants.Handler_Send_Gift;
                mHandler.sendMessage(message);

            }

        } catch (Exception e) {
            HnLogUtils.e(TAG, "解析数据出现异常：" + e.getMessage());
        }

    }

    /**
     * 定时器
     *
     * @param dates 秒数
     * @param time  转化好的时间
     */
    @Override
    public void showTimeTask(long dates, String time, String type) {
        if (mActivity == null) return;
        if (HnVideoChatViewBiz.ChatTimeTask.equals(type)) {

            if (mTvChatTime != null) mTvChatTime.setText(time);

        } else if (HnVideoChatViewBiz.WaitTimeTask.equals(type)) {
            if (mTvWaitAnsNo != null)
                mTvWaitAnsNo.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mActivity != null && !mChatConnectSuccess) {

                        if (mVideoChatViewBiz != null) {
                            mVideoChatViewBiz.closeWaitObservable();
                            if (mDbean != null && mVideoChatBiz != null)
                                mVideoChatBiz.cancleChatVideo(mDbean.getChat_log(), 2);

                        }
                        mActivity.finish();
                    }

                }
            }, 30 * 1000);

        } else if (HnVideoChatViewBiz.PayTimeTask.equals(type)) {
//            if(HnBaseApplication.getmUserBean()==null||"1".equals(HnBaseApplication.getmUserBean().getApple_online())){
//                return;
//            }
            if (mActivity == null || mVideoChatBiz == null || mDbean == null) return;
            try {
                Double price = Double.parseDouble(mDbean.getPrice());
                if (Double.parseDouble(HnBaseApplication.getmUserBean().getUser_coin()) < price) {
                    HnToastUtils.showCenterLongToast("您的账号余额已不足，即将挂断通话");
                    if (mHandler != null) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mActivity != null && mVideoChatBiz != null) {
                                    mVideoChatBiz.hangUpChatVideo(mDbean.getChat_log());
                                }
                            }
                        }, 1000);
                    }
                } else {
                    mVideoChatBiz.feeDeductionChatVideo(mDbean.getChat_log());
                }

            } catch (Exception e) {
                mVideoChatBiz.feeDeductionChatVideo(mDbean.getChat_log());
            }


        }
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (HnVideoChatBiz.SendMessage.equals(type)) {
            //清除消息
            mEtChat.setText("");
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
            HnGiftListModel model = (HnGiftListModel) obj;
            if (model.getD() != null && model.getD().getGift() != null) {
                mHnGiftBiz.transformData(model.getD().getGift());
            }
            //从数据库中获取到数据
        } else if (HnGiftBiz.GET_GIFT_LIST_FROM_DB == type) {
            ArrayList<GiftListBean> giftList = (ArrayList<GiftListBean>) obj;
            if (giftList != null && giftList.size() > 0) {
                mGifts.clear();
                mGifts.addAll(giftList);
            }
        } else if (HnVideoChatBiz.AcceptChat.equals(type)) {//接受聊天

            HnAppManager.getInstance().finishActivity(HnAnchorActivity.class);
            HnAppManager.getInstance().finishActivity(HnAudienceActivity.class);
            if (mVideoChatViewBiz != null) {
                mVideoChatViewBiz.closeWaitObservable();
                mVideoChatViewBiz.startChatTimeTask();
            }
            if (mVideoChatBiz != null) mVideoChatBiz.startHeardBeat(mDbean.getChat_log(), 1);

            mChatConnectSuccess = true;
            mTvAnswer.setEnabled(true);
            HnInvateChatVideoModel model = (HnInvateChatVideoModel) obj;
            if (model != null && model.getD() != null) {
                ((HnOnlineVideoChatActivity) getActivity()).setPushUrl(model.getD().getPush_url());
                setChatUiStatue(true);
            }
        } else if (HnVideoChatBiz.HangUpChat.equals(type)) {//挂断聊天
            HnInvateChatVideoModel model = (HnInvateChatVideoModel) obj;
            if (model != null && model.getD() != null) {
                if (mVideoChatViewBiz != null) {
                    mVideoChatViewBiz.closeWaitObservable();
                    mVideoChatViewBiz.closePayObservable();
                    mVideoChatViewBiz.closeChatObservable();
                }
                if (mVideoChatBiz != null) {
                    mVideoChatBiz.closeDbservable();
                }
                HnLogUtils.e("finish_chat-111接口--" + model.getD().toString());
                removeTcImListener();
                HnOverChatVideoActivity.luncher(mActivity, mDbean.getF_user_avatar(), model.getD().getDuration(), model.getD().getAmount(),
                        model.getD().getCoin_amount(), mDbean.isCreate());
                mActivity.finish();
            }

        } else if (HnVideoChatBiz.FeeDeduction.equals(type)) {
            HnFeeDeductionModel model = (HnFeeDeductionModel) obj;
            if (model != null && model.getD() != null && model.getD().getUser() != null) {
                HnBaseApplication.getmUserBean().setUser_coin(model.getD().getUser().getUser_coin());
                if (mDbean != null && !TextUtils.isEmpty(mDbean.getPrice())) {
                    if (mDbean != null && !TextUtils.isEmpty(mDbean.getPrice())) {
                        updataCoin(model.getD().getUser().getUser_coin());
                    }

                }
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) return;
        if (HnVideoChatBiz.SendMessage.equals(type)) {
            //清除消息
            mEtChat.setText("");
            HnToastUtils.showToastLong(msg);
        } else if (HnVideoChatBiz.AcceptChat.equals(type) || HnVideoChatBiz.HangUpChat.equals(type)) {//接受聊天
            mTvAnswer.setEnabled(true);
            if(!msg.equals("请先支付私信费用")){
                HnToastUtils.showToastShort(msg);
            }
        } else if (HnVideoChatBiz.FeeDeduction.equals(type)) {//扣费
            if (HnServiceErrorUtil.USER_COIN_NOT_ENOUGH == code) {
                HnToastUtils.showCenterLongToast("您的账号余额已不足，即将挂断通话");
                if (mHandler != null) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity != null && mVideoChatBiz != null) {
                                mVideoChatBiz.hangUpChatVideo(mDbean.getChat_log());
                            }
                        }
                    }, 1000);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }

        }
    }

    private void updataCoin(String coin) {
        try {
            HnBaseApplication.getmUserBean().setUser_coin(coin);
            Double price = Double.parseDouble(mDbean.getPrice());
            mTotalPay = HnUtils.addDouble(mTotalPay, price);
            if (mTvChatMoney != null) {
                mTvChatMoney.setText(HnUtils.setTwoPoint(coin) + HnBaseApplication.getmConfig().getCoin());
                if (mTvChatMoney.getVisibility() != View.VISIBLE)
                    mTvChatMoney.setVisibility(View.VISIBLE);
            }
            if (isFirstToast && Double.parseDouble(coin) < HnUtils.mulDouble(5.00, price)) {
                isFirstToast = false;
                HnToastUtils.showCenterLongToast("您的账号余额已不足视频通话5分钟，请确保通话不被挂断立即前去续费");
            }
        } catch (Exception e) {
        }

    }

    @Subscribe()
    public void sendGiftUpdataCoinEvent(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Update_User_Coin.equals(event.getType())) {//更新用户虚拟币
            String coin = (String) event.getObj();
            if (!TextUtils.isEmpty(coin)) {
                updataCoin(coin);
            }
        }
    }


    @Override
    public void downloadGiftSuccess(boolean isShow, GiftListBean data, Object obj) {
        if (mActivity == null || mVideoChatViewBiz == null) {
            return;
        }
        mGifts = mVideoChatViewBiz.updateGiftListData(obj, isShow, data, mBigGiftActionManager, mGifts);
    }

    @Override
    public void downloadGiftFail(int code, String msg, GiftListBean data) {

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mVideoChatViewBiz != null) {
            mVideoChatViewBiz.onLayoutChnage(bottom, oldBottom, mElEmotion, mGiftLay, mRlChatMsg, mRlBottomFun, mActivity, keyHeight, mIvEmoji);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mVideoChatViewBiz != null) {
            mVideoChatViewBiz.onTouch(view, mElEmotion, mRlChatMsg, mRlBottomFun, mActivity);
        }
        return false;
    }


    /**
     * 播放音乐
     */
    private void startMusic() {
        try {
            if (mMediaPlayer == null)
                mMediaPlayer = MediaPlayer.create(mActivity, R.raw.music_chat);//重新设置要播放的音频
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止音乐
     */
    public void stopMusic() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoChatViewBiz != null) {
            mVideoChatViewBiz.closeChatObservable();
            mVideoChatViewBiz.closePayObservable();
            mVideoChatViewBiz.closeWaitObservable();
        }
        if (mVideoChatBiz != null) {
            mVideoChatBiz.closeDbservable();
        }
        mVideoChatBiz = null;
        mVideoChatViewBiz = null;
        removeTcImListener();
        stopMusic();
        EventBus.getDefault().unregister(this);
    }
}
