package com.hotniao.livelibrary.ui.audience.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnFastClickUtil;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.adapter.HnLiveMessageAdapter;
import com.hotniao.livelibrary.adapter.HnOnlineRecAdapter;
import com.hotniao.livelibrary.biz.audience.HnAudienceBiz;
import com.hotniao.livelibrary.biz.audience.HnAudienceInfoListener;
import com.hotniao.livelibrary.biz.audience.HnAudienceViewBiz;
import com.hotniao.livelibrary.biz.gift.HnGiftBiz;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.giflist.HnDonwloadGiftStateListener;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.HnJPushMessageInfo;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.HnLiveRoomInfoModel;
import com.hotniao.livelibrary.model.HnOnlineModel;
import com.hotniao.livelibrary.model.HnPayRoomPriceModel;
import com.hotniao.livelibrary.model.HnUserInfoDetailModel;
import com.hotniao.livelibrary.model.bean.HnLiveRoomInfoBean;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;
import com.hotniao.livelibrary.model.bean.OnlineBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.event.HnReceiverSysMsgEvent;
import com.hotniao.livelibrary.model.event.HnRechargeSuccessModel;
import com.hotniao.livelibrary.model.event.UserCoinReduceEvent;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;
import com.hotniao.livelibrary.ui.gift.GiftDialog;
import com.hotniao.livelibrary.util.HnLiveSwitchDataUitl;
import com.hotniao.livelibrary.widget.danmu.DanmakuActionManager;
import com.hotniao.livelibrary.widget.danmu.DanmakuChannel;
import com.hotniao.livelibrary.widget.dialog.HnInputTextLiveDialog;
import com.hotniao.livelibrary.widget.dialog.HnPlatfromListDialog;
import com.hotniao.livelibrary.widget.dialog.HnShareLiveDialog;
import com.hotniao.livelibrary.widget.gift.GiftModel;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.hotniao.livelibrary.widget.gift.LeftGiftsItemLayout;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftChannel;
import com.hotniao.livelibrary.widget.periscope.PeriscopeLayout;
import com.imlibrary.TCChatRoomMgr;
import com.jakewharton.rxbinding2.view.RxView;
import com.lqr.emoji.EmotionLayout;
import com.reslibrarytwo.HnSkinTextView;
import com.tencent.TIMMessage;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.hotniao.livelibrary.R.id.ll_info;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用户直播间  -- 互动层
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 *
 * @author mj
 */
public class HnAudienceInfoFragment extends BaseFragment implements HnAudienceInfoListener, View.OnClickListener, View.OnLayoutChangeListener, View.OnTouchListener, HnDonwloadGiftStateListener {

    /**
     * 布局控件
     */
    private LinearLayout roomBottomRela;
    private LinearLayout mLLContent;
    private BigGiftChannel bigGift;
    private RelativeLayout mRoomTopContainer;
    private RelativeLayout rlTop;
    private RelativeLayout rlInfo;
    private ImageView ivVip;
    private FrescoImageView fivHeader;
    private LinearLayout llAncInfo;
    private ImageView ivLivetime;
    private TextView tvName;
    private TextView tvPeopleNumber;
    private TextView tvFollow;
    private RecyclerView mRecyclerView;
    private TextView tvCoin;
    private TextView mTvPlatfromRank;
    private TextView tvLuckyDisplay;
    private TextView tvRank;
    private TextView tvId;
    private LinearLayout llTask;
    private RelativeLayout rlMessage;
    private ToggleButton mToggleButton;
    private EditText etSendData;
    private ImageView ivEmoji;
    private TextView messageSendTv;
    private PeriscopeLayout periscopeStar;
    private LeftGiftControlLayout leftFiftLl;
    private LeftGiftsItemLayout gift1;
    private LeftGiftsItemLayout gift2;
    private LinearLayout containerVG;
    private DanmakuChannel mDanmakuChannelA;
    private DanmakuChannel mDanmakuChannelB;
    //进场特效
    private LinearLayout mLlEnterRoom;
    private TextView mTvEntLv;
    private TextView mTvEntName;
    private View mViewEnt;

    private FrameLayout bottomFra;
    private ListView messageListView;
    private LinearLayout llBottomContainer;
    private ImageView roomNotifiImg;
    private ImageButton ivPrivateletter;
    private TextView tvUnreadCount;
    private ImageButton ibGift;
    private ImageButton tvClose;
    private ImageButton mIvShare;

    private EmotionLayout mElEmotion;
    private PeriscopeLayout mPlGreat;
    /**
     * 蒙层
     */
    private ImageView mIvMask;
    private FrameLayout mFramePay;

    /**
     * 主播离开层
     */
    private View viewBg;
    private FrescoImageView mHeaderIcon;
    private TextView tvNick;
    private ImageView ivSex;
    private HnSkinTextView tvUserLeaveLevel;
    private TextView tvLiveLevel;
    private LinearLayout llInfo;
    private TextView tvStart;
    private TextView tvGoHome;

    /**
     * 进场动画
     */
    private Animation highLevelExitAnima;
    private Animation highLevelEntenerAnima;
    private List<HnReceiveSocketBean.DataBean.FuserBean> mEnterData = new ArrayList<>();


    /**
     * 直播间信息
     */
    private HnLiveRoomInfoBean mRoomInfo;
    /**
     * 直播类型  0：免费，1：VIP，2：门票，3：计时
     **/
    private String mLiveType = "0";
    /**
     * 直播价格
     */
    private String mLivePrice = "0";
    /**
     * 主播id
     */
    private String mAnchor_id;
    /**
     * 主播u票数
     */
    private String mAnchor_U_Piao;
    /**
     * 自己的id
     */
    private String mOwn_id;
    /**
     * 自己是否是vip
     */
    private String mOwnIsVip = "N";
    /**
     * 自己的u币
     */
    private String mOwn_Coin;
    /**
     * 是否房管  N否 Y是
     */

    private String mIsRoomAdmin = "N";
    /**
     * 是否已关注此主播
     */
    private String isFollow = "N";
    /**
     * 未读消息数
     */
    private long unread_Count = 0;
    /**
     * 弹幕价格  默认10
     */
    private String barrage_price = "10";

    /**
     * 在线人数统计=在线实际人数+在线机器人人数
     */
    private long mOnLineNumber = 0;
    /**
     * 用于展示的列表数据  小于等于30  在线实际在线人在前，机器在后，若实际在线人数》30 只显示实际的在线30人
     */
    private ArrayList<OnlineBean> mAllList = new ArrayList<>();
    /**
     * 主播间右上角在线人数显示列表适配器
     */
    private HnOnlineRecAdapter mHnOnlineRecAdapter;
    /**
     * receiver的管理器
     */
    private LinearLayoutManager mLinearLayoutManager;

    /**
     * 直播间左下角小时适配器
     */
    private HnLiveMessageAdapter mMessageAdapter;
    /**
     * 左下角消息列表数据源
     */
    private ArrayList<HnReceiveSocketBean> messageList = new ArrayList<>();


    /**
     * websocket是否连接成功
     */
    private boolean webscoketConnectSuccess = false;
    /**
     * 是否正在显示私信dialog
     */
    private boolean isShowPrivateLetterDialog = false;
    /**
     * 正在和你聊天的人的uid 用户用户点击头像后进入私信后,直播界面不对该用户的私信信息进行加减操作
     */
    private String Chatting_Uid = "-1";

    /**
     * 阀值设置为屏幕高度的1/3
     */
    private int keyHeight;

    /**
     * 业务逻辑类 处理用户直播间的相关业务，ui相关操作不要在类中操作
     */
    private HnAudienceBiz mHnAudienceBiz;
    /**
     * 该类主要是做一些ui的操作  如ui的显示和隐藏等，不做业务逻辑计算
     */
    private HnAudienceViewBiz mHnAudienceViewBiz;
    /**
     * 该类主要是获取礼物数据，从而进行礼物动画
     */
    private HnGiftBiz mHnGiftBiz;
    private ArrayList<GiftListBean> gifts = new ArrayList<>();

    /**
     * 弹幕管理器
     */
    private DanmakuActionManager danmakuActionManager;
    /**
     * 大礼物管理器
     */
    private BigGiftActionManager mBigGiftActionManager;
    /**
     * 礼物对话框
     */
    private GiftDialog mGiftDialog;
    /**
     * 充值不足
     */
    private boolean isBananceNotEnough = false;
    /**
     * 是否支付了门票
     */
    private boolean hasPayMenPiaoLive = false;


    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;
    private HnInputTextLiveDialog mInputTextDialog;

    /**
     * 腾讯IM
     */
    private TCChatRoomMgr mTCChatRoomMgr;
    private Gson mGson;

    private HnLiveListModel.LiveListBean bean;

    private ArrayList<HnReceiveSocketBean> noticeList = new ArrayList<>();
    private boolean isAddNotice = false;

    public static HnAudienceInfoFragment newInstance(HnLiveListModel.LiveListBean bean) {
        HnAudienceInfoFragment fragment = new HnAudienceInfoFragment();
        Bundle b = new Bundle();
        b.putParcelable("data", bean);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fiv_header) {//头像   用户点击主播头像
            if (mRoomInfo == null) return;
            mHnAudienceViewBiz.showUserInfoDialog(mAnchor_id, mOwn_id, mActivity, 2, mRoomInfo.getAnchor().getUser_id(), mIsRoomAdmin, true);

        } else if (v.getId() == R.id.room_notifi_img) {//消息按钮
            if (mRoomInfo == null) return;
//            mHnAudienceViewBiz.showMessageSendLayout(rlMessage, llBottomContainer, etSendData, mActivity);
            showInputMsgDialog();
        } else if (v.getId() == R.id.iv_privateletter) {//私信
            if (mRoomInfo == null) return;
            isShowPrivateLetterDialog = true;
            Chatting_Uid = "0";
            mHnAudienceViewBiz.showPriveteLetterListDialog(mActivity, false, mAnchor_id, mRoomInfo.getAnchor().getUser_nickname(),
                    mRoomInfo.getAnchor().getUser_avatar(), "100", "", mRoomInfo.getAnchor().getChat_room_id());

        } else if (v.getId() == R.id.tv_close) {//关闭直播间
            if (mRoomInfo == null) return;
            mHnAudienceViewBiz.onBack(rlMessage, llBottomContainer, mElEmotion, mOnLineNumber, 1, mLiveType, mActivity);

        } else if (v.getId() == R.id.message_send_tv) {//发动消息
            if (mRoomInfo == null) return;
            ((InputMethodManager) etSendData.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            if (HnFastClickUtil.isFastClick()) {
                return;
            }
            String messageContent = etSendData.getText().toString().trim();
            boolean mIsDanmu = mToggleButton.isChecked();
            joinRoomAgain();
            mHnAudienceBiz.sendMessaqge(messageContent, mIsDanmu, webscoketConnectSuccess, mAnchor_id);

        } else if (v.getId() == R.id.tv_follow) {//关注
            if (mRoomInfo == null) return;
            mHnAudienceBiz.setFollow(mAnchor_id, isFollow, mAnchor_id);

        } else if (v.getId() == R.id.ib_gift) {//礼物
            if (mRoomInfo == null) return;
            mGiftDialog = GiftDialog.newInstance(mOwn_Coin, mAnchor_id);
            mGiftDialog.show(mActivity.getSupportFragmentManager(), "gift");
            joinRoomAgain();

        } else if (v.getId() == R.id.tv_start) {//主播已离开直播间view中的关注
            if (TextUtils.isEmpty(mAnchor_id)) return;
            mHnAudienceBiz.setFollow(mAnchor_id, isFollow, mAnchor_id);

        } else if (v.getId() == R.id.tv_go_home) {//主播已离开直播间view中的返回首页
            if (mActivity != null) {
                mActivity.finish();
            }
        } else if (v.getId() == R.id.tv_coin) {//收益
            if (mRoomInfo == null) return;
            ARouter.getInstance().build("/app/HnFansContributeListActivity").withString("userId", mRoomInfo.getAnchor().getUser_id()).navigation();

        } else if (v.getId() == R.id.mTvPlatfromRank) {//悠悠直播排名
            if (mRoomInfo == null) return;
            HnPlatfromListDialog.newInstance(mRoomInfo.getAnchor().getUser_id()).show(mActivity.getFragmentManager(), "");

        } else if (v.getId() == R.id.mIvShare) {//分享
            if (mRoomInfo == null) return;
            HnShareLiveDialog.newInstance(mShareAPI, mShareAction, mRoomInfo.getLive().getShare_url(),
                    mRoomInfo.getAnchor().getUser_avatar(), mRoomInfo.getAnchor().getUser_nickname(), mRoomInfo.getAnchor().getUser_id(), false).show(mActivity.getFragmentManager(), "share");

        } else if (v.getId() == R.id.mIvMask || v.getId() == R.id.not_live_bg) {
            //点击蒙层取消下层事件和列表某个直播结束下层事件
        }
    }

    /**
     * 公聊EditText监听
     */
    private void setSendMsg() {
        etSendData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                 @Override
                                                 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                     if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                         ((InputMethodManager) etSendData.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                                                 .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                                                                         InputMethodManager.HIDE_NOT_ALWAYS);
                                                         String messageContent = etSendData.getText().toString().trim();
                                                         boolean mIsDanmu = mToggleButton.isChecked();
                                                         if (TextUtils.isEmpty(mAnchor_id)) return true;
                                                         joinRoomAgain();
                                                         mHnAudienceBiz.sendMessaqge(messageContent, mIsDanmu, webscoketConnectSuccess, mAnchor_id);
                                                         return true;
                                                     }
                                                     return false;
                                                 }
                                             }

        );
        etSendData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etSendData.getText().toString()))
                    messageSendTv.setSelected(false);
                else messageSendTv.setSelected(true);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (HnBaseApplication.getmUserBean() != null)
            mOwn_Coin = HnBaseApplication.getmUserBean().getUser_coin();
    }

    @Override
    public int getContentViewId() {
        return R.layout.live_fragment_live_audience;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * 初始化腾讯云
         */
        initTcIm();
        mShareAPI = UMShareAPI.get(mActivity);
        mShareAction = new ShareAction(mActivity);
        //初始化视图
        initViews();

        mInputTextDialog = new HnInputTextLiveDialog(mActivity, R.style.InputDialog);
        mInputTextDialog.setmOnTextSendListener(new HnInputTextLiveDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg, boolean isDanmu) {
                if (TextUtils.isEmpty(mAnchor_id)) return;
                joinRoomAgain();
                mHnAudienceBiz.sendMessaqge(msg, isDanmu, webscoketConnectSuccess, mAnchor_id);
            }
        });
    }

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        if (mInputTextDialog == null)
            mInputTextDialog = new HnInputTextLiveDialog(mActivity, R.style.InputDialog);
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextDialog.getWindow().setAttributes(lp);
        mInputTextDialog.setCancelable(true);
        mInputTextDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mInputTextDialog.show();


    }

    /**
     * 加入房间未成功重新加入
     */
    private void joinRoomAgain() {
        if (!webscoketConnectSuccess && mTCChatRoomMgr != null && !TextUtils.isEmpty(mAnchor_id))
            mTCChatRoomMgr.joinGroup(mAnchor_id);
    }

    /**
     * 退出群组或者移除监听
     *
     * @param isRemove 是否移除监听
     */
    private void quitGroupOrRemove(boolean isRemove) {
        if (mTCChatRoomMgr != null) {
            mTCChatRoomMgr.quitGroup(mAnchor_id);
            if (isRemove) mTCChatRoomMgr.removeMsgListener();
        }
    }

    /**
     * 腾讯云
     */
    private void initTcIm() {
        //初始化消息回调
        mTCChatRoomMgr = TCChatRoomMgr.getInstance("");
        mTCChatRoomMgr.setMessageListener(new TCChatRoomMgr.TCChatRoomListener() {
            @Override
            public void onJoinGroupCallback(int code, String msg) {
                if (0 == code) {
                    webscoketConnectSuccess = true;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mHnAudienceBiz != null && mRoomInfo != null && mRoomInfo.getAnchor() != null)
                                mHnAudienceBiz.requestToGetRoomUser(mRoomInfo.getAnchor().getUser_id());
                        }
                    }, 1);
                } else {
                    webscoketConnectSuccess = false;
                }
            }

            @Override
            public void onSendMsgCallback(int code, TIMMessage timMessage) {
            }

            @Override
            public void onReceiveMsg(int type, String content) {
                HnLogUtils.e("TcIm--" + content);
                webscoketConnectSuccess = true;
                matchLiveMsg(content);
            }

            @Override
            public void onGroupDelete(String roomId) {
                if (mActivity == null || mHnAudienceBiz == null || TextUtils.isEmpty(mAnchor_id))
                    return;
                if (!TextUtils.isEmpty(roomId) && roomId.equals(mAnchor_id)) {
                    quitGroupOrRemove(true);
                    mHnAudienceBiz.leaveAnchorLiveRomm("", mAnchor_id, mActivity);
                }
            }
        });
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
            if (HnWebscoketConstants.Onlines.equals(type)) {//在线人数
                HnOnlineModel result = mGson.fromJson(data, HnOnlineModel.class);
                message.obj = result;
            } else {//其他消息
                HnReceiveSocketBean result = mGson.fromJson(data, HnReceiveSocketBean.class);
                message.obj = result;
            }

            if (HnWebscoketConstants.Public_Msg.equals(type)) {//公聊消息
                message.what = HnWebscoketConstants.Handler_Public_Msg;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Send_Anchor.equals(type)) {//更新主播信息
                message.what = HnWebscoketConstants.Handler_Send_Anchor;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Join.equals(type)) {//用户进入
                message.what = HnWebscoketConstants.Handler_Join;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Send_Gift.equals(type)) {//接收到发送礼物的数据
                message.what = HnWebscoketConstants.Handler_Send_Gift;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Barrage.equals(type)) {//弹幕
                message.what = HnWebscoketConstants.Handler_Barrage;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Prohibit_Talk.equals(type)) {//禁言
                message.what = HnWebscoketConstants.Handler_Prohibit_Talk;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Kick.equals(type)) {//踢出房间
                message.what = HnWebscoketConstants.Handler_Kick;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Onlines.equals(type)) {//推送直播间真实在线人数
                message.what = HnWebscoketConstants.Handler_Onlines;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Attitude.equals(type)) {//点赞
                message.what = HnWebscoketConstants.Handler_Attitude;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Stop_Live.equals(type)) {//主播停止直播
                message.what = HnWebscoketConstants.Handler_Stop_Live;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.Kill_Live.equals(type)) {//禁播
                message.what = HnWebscoketConstants.Handler_Kill_Live;
                mHandler.sendMessage(message);


            } else if (HnWebscoketConstants.Room_Admin.equals(type)) {//房管
                message.what = HnWebscoketConstants.Handler_Room_Admin;
                mHandler.sendMessage(message);

            } else if (HnWebscoketConstants.System.equals(type)) {//升级等系统消息
                message.what = HnWebscoketConstants.Handler_System;
                mHandler.sendMessage(message);
            }

        } catch (Exception e) {
            HnLogUtils.e(TAG, "解析数据出现异常：" + e.getMessage());
        }

    }


    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            bean = bundle.getParcelable("data");
            mAnchor_id = bean.getUid();
            if (!TextUtils.isEmpty(mAnchor_id)) {
                if (mTCChatRoomMgr != null) mTCChatRoomMgr.joinGroup(mAnchor_id);
                mHnAudienceBiz.requestToGetRoomInfo(mAnchor_id);
            }
        }
    }

    /**
     * 更新ui
     */
    private void updateUI() {
        if (mActivity == null || !isAdded()) return;
        if (tvId == null) return;
        if (mFramePay != null) mFramePay.setVisibility(View.GONE);
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Scroll_viewPager, true));
        //自己的id
        mOwn_id = HnPrefUtils.getString(NetConstant.User.UID, "");
        //主播相关信息
        HnLiveRoomInfoBean.AnchorBean anchor = mRoomInfo.getAnchor();
        if (anchor != null) {
            //主播头像
            String avator = anchor.getUser_avatar();
            fivHeader.setController(FrescoConfig.getController(avator));
            //主播name
            String name = anchor.getUser_nickname();
            tvName.setText(name);
            //主播id  优号
            mAnchor_id = anchor.getUser_id();
            tvId.setText(getString(R.string.live_id) + mAnchor_id);
            //主播的收益 u票数
            mAnchor_U_Piao = anchor.getUser_dot();
            tvCoin.setText(HnUtils.setTwoPoint(mAnchor_U_Piao));
            //排名
            setRank(anchor.getAnchor_ranking());

            //是否已关注
            isFollow = anchor.getIs_follow();
            if (TextUtils.isEmpty(isFollow) || "N".equals(isFollow)) {//未关注
                tvFollow.setVisibility(View.VISIBLE);
            } else {
                tvFollow.setVisibility(View.GONE);
            }
            if (mAnchor_id.equals(HnBaseApplication.getmUserBean().getUser_id())) {
                tvFollow.setVisibility(View.GONE);
            }
            //是否是vip
            ivVip.setVisibility(View.GONE);
        }
        //用户信息
        HnLiveRoomInfoBean.UserBean userBean = mRoomInfo.getUser();
        if (userBean != null) {
            mOwn_Coin = userBean.getUser_coin();
            mOwnIsVip = userBean.getUser_is_member();

        }
        mIsRoomAdmin = mRoomInfo.getUser().getIs_anchor_admin();
        //系统公告
        List<String> notice = mRoomInfo.getLive().getNotices();


        noticeList.clear();
        noticeList.addAll(mHnAudienceBiz.addSystemNotice(notice));
        if (!isAddNotice) {
            if (messageList == null) messageList = mHnAudienceBiz.addSystemNotice(notice);
            else messageList.addAll(mHnAudienceBiz.addSystemNotice(notice));
            isAddNotice = true;
        }
        //初始化消息适配器
        initMessageAdapter();
        //初始化在线人数适配器
        initUserHeaderAdapter();
        //消息未读数
        String count = HnPrefUtils.getString(NetConstant.User.Unread_Count, "");
        if (!TextUtils.isEmpty(count) && !"0".equals(count)) {
            tvUnreadCount.setVisibility(View.VISIBLE);
            unread_Count = 0;
        } else {
            tvUnreadCount.setVisibility(View.GONE);
            unread_Count = Long.valueOf(0);
        }

        if (mHnAudienceBiz != null) mHnAudienceBiz.getNoReadMsg();


        //直播类型
        HnLiveRoomInfoBean.LiveBean liveBean = mRoomInfo.getLive();
        if (liveBean != null) {
            //弹幕价格
            barrage_price = liveBean.getBarrage_fee();
            if (mInputTextDialog != null)
                mInputTextDialog.setPrice(mRoomInfo.getLive().getBarrage_fee());
            mLiveType = liveBean.getAnchor_live_pay();
            mLivePrice = liveBean.getAnchor_live_fee();
            //查看自己的免费时间
            String freeTime = mRoomInfo.getUser().getFree_time();
            String hasPay = mRoomInfo.getUser().getIs_pay();
            if ("0".equals(mLiveType)) return;
//            0：免费，1：VIP，2：门票，3：计时


            if (TextUtils.isEmpty(freeTime) || "0".equals(freeTime)) {
                if ("1".equals(mLiveType)) {
                    if ("N".equals(mOwnIsVip) || TextUtils.isEmpty(mOwnIsVip)) {//不是vip会员
                        isBananceNotEnough = false;
                        mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, true, mFramePay);
                    } else {//vip计时
                        mHnAudienceBiz.startVIPTimeTask(mOwnIsVip);
                    }
                } else if ("2".equals(mLiveType)) {//门票付费
                    if ("N".equals(hasPay) || TextUtils.isEmpty(hasPay)) {//没有付费
                        hasPayMenPiaoLive = false;
                        isBananceNotEnough = false;
                        mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, true, mFramePay);
                    } else {
                        hasPayMenPiaoLive = true;
                    }
                }
                if ("3".equals(mLiveType)) {//计时收费
                    if ("N".equals(hasPay) || TextUtils.isEmpty(hasPay)) {
                        if (TextUtils.isEmpty(freeTime) || 0 > Integer.parseInt(freeTime))
                            freeTime = "30";
                        mHnAudienceBiz.startFreeLookCountdown(freeTime);
                    } else {
                        isBananceNotEnough = false;
                        mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, true, mFramePay);
                    }
                }
            } else {
                if (TextUtils.isEmpty(freeTime) || 0 > Integer.parseInt(freeTime)) freeTime = "30";
                mHnAudienceBiz.startFreeLookCountdown(freeTime);
            }
        }


    }

    private void addNoticeFirst() {
        if (noticeList == null || noticeList.size() < 1) return;
        if (!isAddNotice && messageList.size() < 1) {
            if (messageList == null) messageList = noticeList;
            else messageList.addAll(noticeList);
            isAddNotice = true;
        }
    }

    /**
     * 设置排名
     *
     * @param rank
     */
    private void setRank(String rank) {
        if (mTvPlatfromRank == null || mActivity == null) return;
        //排名
        if (!TextUtils.isEmpty(rank)) {
            try {
                if (1 > Integer.parseInt(rank) || 200 < Integer.parseInt(rank)) {
                    mTvPlatfromRank.setText(HnUiUtils.getString(R.string.live_firebird_rank) + "200+");
                } else {
                    mTvPlatfromRank.setText(HnUiUtils.getString(R.string.live_firebird_rank) + rank);
                }

            } catch (Exception e) {
                mTvPlatfromRank.setText(HnUiUtils.getString(R.string.live_firebird_rank) + "200+");
            }
        } else {
            mTvPlatfromRank.setText(HnUiUtils.getString(R.string.live_firebird_rank) + "200+");
        }

    }

    /**
     * 初始化视图
     */
    private void initViews() {
        EventBus.getDefault().register(this);
        /**初始化ui处理业务逻辑类*/
        mHnAudienceViewBiz = new HnAudienceViewBiz();
        /**初始化业务逻辑类*/
        mHnAudienceBiz = new HnAudienceBiz(this, mActivity);
        /**礼物列表业务逻辑类*/
        mHnGiftBiz = new HnGiftBiz(mActivity, this, this);


        //主播已结束直播显示界面
        viewBg = (View) mRootView.findViewById(R.id.not_live_bg);
        viewBg.setOnClickListener(this);
        viewBg.setVisibility(View.GONE);
        mHeaderIcon = (FrescoImageView) mRootView.findViewById(R.id.iv_icon);
        tvNick = (TextView) mRootView.findViewById(R.id.tv_nick);
        ivSex = (ImageView) mRootView.findViewById(R.id.iv_sex);
        tvUserLeaveLevel = (HnSkinTextView) mRootView.findViewById(R.id.tv_user_level);
        tvLiveLevel = (TextView) mRootView.findViewById(R.id.tv_live_level);
        llInfo = (LinearLayout) mRootView.findViewById(ll_info);
        tvStart = (TextView) mRootView.findViewById(R.id.tv_start);
        tvStart.setOnClickListener(this);
        tvGoHome = (TextView) mRootView.findViewById(R.id.tv_go_home);
        tvGoHome.setOnClickListener(this);


        //主播交互界面
        roomBottomRela = (LinearLayout) mRootView.findViewById(R.id.room_bottom_rela);
        roomBottomRela.addOnLayoutChangeListener(this);
        roomBottomRela.setOnTouchListener(this);
        //游戏
        mLLContent = (LinearLayout) mRootView.findViewById(R.id.ll_content);

        //大礼物
        bigGift = (BigGiftChannel) mRootView.findViewById(R.id.ani_view);
        mBigGiftActionManager = new BigGiftActionManager();
        bigGift.setDanAction(mBigGiftActionManager);
        mBigGiftActionManager.addChannel(bigGift);

        //顶部控制容器
        mRoomTopContainer = (RelativeLayout) mRootView.findViewById(R.id.rl_top_con);
        rlTop = (RelativeLayout) mRootView.findViewById(R.id.rl_top);
        rlInfo = (RelativeLayout) mRootView.findViewById(R.id.rl_info);
        //主播头像
        fivHeader = (FrescoImageView) mRootView.findViewById(R.id.fiv_header);
        fivHeader.setOnClickListener(this);
        //vip
        ivVip = (ImageView) mRootView.findViewById(R.id.iv_vip);

        llAncInfo = (LinearLayout) mRootView.findViewById(R.id.ll_anc_info);
        ivLivetime = (ImageView) mRootView.findViewById(R.id.iv_livetime);

        tvName = (TextView) mRootView.findViewById(R.id.tv_time);
        //蒙层
        mIvMask = (ImageView) mRootView.findViewById(R.id.mIvMask);
        mIvMask.setOnClickListener(this);
        mFramePay = (FrameLayout) mRootView.findViewById(R.id.mFramePay);


        //在线人数
        tvPeopleNumber = (TextView) mRootView.findViewById(R.id.tv_online);
        tvFollow = (TextView) mRootView.findViewById(R.id.tv_follow);
        tvFollow.setOnClickListener(this);

        //右上角头像列表
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recy_online);

        //排名
        mTvPlatfromRank = (TextView) mRootView.findViewById(R.id.mTvPlatfromRank);
        mTvPlatfromRank.setOnClickListener(this);
        tvCoin = (TextView) mRootView.findViewById(R.id.tv_coin);
        tvCoin.setOnClickListener(this);
        tvLuckyDisplay = (TextView) mRootView.findViewById(R.id.tv_lucky_display);
        tvRank = (TextView) mRootView.findViewById(R.id.tv_rank);
        tvId = (TextView) mRootView.findViewById(R.id.tv_id);
        llTask = (LinearLayout) mRootView.findViewById(R.id.ll_task);
        rlMessage = (RelativeLayout) mRootView.findViewById(R.id.rl_message);
        mToggleButton = (ToggleButton) mRootView.findViewById(R.id.message_talk_tb);
        //输入框
        etSendData = (EditText) mRootView.findViewById(R.id.message_input_et);
        etSendData.setFocusable(false);
        etSendData.setFocusableInTouchMode(false);
        ivEmoji = (ImageView) mRootView.findViewById(R.id.iv_emoji);
        //发送消息
        messageSendTv = (TextView) mRootView.findViewById(R.id.message_send_tv);
        messageSendTv.setOnClickListener(this);
//        setSendMsg();
        periscopeStar = (PeriscopeLayout) mRootView.findViewById(R.id.periscope_star);
        leftFiftLl = (LeftGiftControlLayout) mRootView.findViewById(R.id.giftLl);
        gift1 = (LeftGiftsItemLayout) mRootView.findViewById(R.id.gift1);

        gift2 = (LeftGiftsItemLayout) mRootView.findViewById(R.id.gift2);
        containerVG = (LinearLayout) mRootView.findViewById(R.id.containerVG);
        mDanmakuChannelA = (DanmakuChannel) mRootView.findViewById(R.id.danA);
        mDanmakuChannelB = (DanmakuChannel) mRootView.findViewById(R.id.danB);
        //进场特效
        mLlEnterRoom = (LinearLayout) mRootView.findViewById(R.id.rl_enter_effect);
        mTvEntLv = (TextView) mRootView.findViewById(R.id.tv_level);
        mTvEntName = (TextView) mRootView.findViewById(R.id.high_level);
        mViewEnt = mRootView.findViewById(R.id.mViewEnt);
        bottomFra = (FrameLayout) mRootView.findViewById(R.id.bottom_fra);
        //左下角公聊列表
        messageListView = (ListView) mRootView.findViewById(R.id.lv_message);
        //底部布局父容器
        llBottomContainer = (LinearLayout) mRootView.findViewById(R.id.bottom_con);
        //消息按钮
        roomNotifiImg = (ImageView) mRootView.findViewById(R.id.room_notifi_img);
        roomNotifiImg.setOnClickListener(this);
        //私信信息
        ivPrivateletter = (ImageButton) mRootView.findViewById(R.id.iv_privateletter);
        ivPrivateletter.setOnClickListener(this);
        tvUnreadCount = (TextView) mRootView.findViewById(R.id.tv_unread_pri);
        ibGift = (ImageButton) mRootView.findViewById(R.id.ib_gift);
        ibGift.setOnClickListener(this);
        //点赞
        mPlGreat = (PeriscopeLayout) mRootView.findViewById(R.id.mPlGreat);
//        mPlGreat.setOnClickListener(this);
        //退出
        tvClose = (ImageButton) mRootView.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(this);
        mIvShare = (ImageButton) mRootView.findViewById(R.id.mIvShare);
        mIvShare.setOnClickListener(this);
        //获取屏幕高度
        int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //表情同期
        mElEmotion = (EmotionLayout) mRootView.findViewById(R.id.elEmotion);
        //公聊与弹幕切换
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etSendData.setHint(barrage_price + HnBaseApplication.getmConfig().getCoin() + "/条");
                } else {
                    etSendData.setHint(R.string.live_input_chat_content);
                }
            }
        });
        //获取礼物列表数据
        mHnGiftBiz.getGiftListData();
        //弹幕
        danmakuActionManager = new DanmakuActionManager();
        mDanmakuChannelA.setDanAction(danmakuActionManager);
        mDanmakuChannelB.setDanAction(danmakuActionManager);
        danmakuActionManager.addChannel(mDanmakuChannelA);
        danmakuActionManager.addChannel(mDanmakuChannelB);
        //表情 键盘和输入框之间的处理
        mHnAudienceViewBiz.initEmotionKeyboard(mActivity, mLLContent, ivEmoji, etSendData, mElEmotion);
        //表情
        mElEmotion.attachEditText(etSendData);
        mElEmotion.setEmotionAddVisiable(false);
        mElEmotion.setEmotionSettingVisiable(false);


        /**进场动画*/
        highLevelEntenerAnima = AnimationUtils.loadAnimation(mActivity, R.anim.enter_to_left);
        highLevelEntenerAnima.setFillAfter(true);

        highLevelExitAnima = AnimationUtils.loadAnimation(mActivity, R.anim.hide_to_left);
        highLevelExitAnima.setFillAfter(true);
        highLevelExitAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mActivity == null || mLlEnterRoom == null) return;
                mLlEnterRoom.setVisibility(View.GONE);
                if (mLlEnterRoom.getVisibility() == View.INVISIBLE || mLlEnterRoom.getVisibility() == View.GONE) {
                    if (mEnterData != null && mEnterData.size() > 0) {
                        HnReceiveSocketBean.DataBean.FuserBean chatBean = mEnterData.get(0);
                        mEnterData.remove(0);
                        setEnterAnim(chatBean);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        /**点赞  */
        RxView.clicks(mPlGreat)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        if (TextUtils.isEmpty(mAnchor_id)) return;
                        mHnAudienceBiz.clickZan(mAnchor_id);
                        if (mHnAudienceViewBiz != null) {
                            mHnAudienceViewBiz.onTouch(mLLContent, mElEmotion, rlMessage, llBottomContainer, mRoomTopContainer, mActivity);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 进场动画
     *
     * @param jionBean
     */
    private void setEnterAnim(HnReceiveSocketBean.DataBean.FuserBean jionBean) {
        try {
            if ("Y".equals(jionBean.getUser().getUser_is_member())) {
                mLlEnterRoom.setBackgroundResource(R.mipmap.join_room_special_vip);
                mViewEnt.setVisibility(View.VISIBLE);
            } else if ("Y".equals(jionBean.getUser().getIs_level_effect())) {
                mLlEnterRoom.setBackgroundResource(R.drawable.senior);
                mViewEnt.setVisibility(View.GONE);
            }
            mLlEnterRoom.setVisibility(View.VISIBLE);
            mTvEntName.setText(jionBean.getUser().getUser_nickname());
        } catch (Exception e) {
        }
        mLlEnterRoom.startAnimation(highLevelEntenerAnima);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null) return;
                mLlEnterRoom.startAnimation(highLevelExitAnima);
            }
        }, 1500);
    }

    /**
     * 网络请求:请求成功
     *
     * @param type 类型  标识符
     * @param obj  实体等数据   根据实际需求而定
     */
    @Override
    public void requestSuccess(String type, String uid, Object obj) {
        if (roomBottomRela == null || mActivity == null) {
            return;
        }
        if ("send_Messaqge".equals(type)) {
            //清除消息
            etSendData.setText("");
            mToggleButton.setChecked(false);
        } else if ("follow".equals(type)) {//关注
            if (TextUtils.isEmpty(isFollow) || "N".equals(isFollow)) {
                isFollow = mAnchor_id;
                tvFollow.setVisibility(View.GONE);
                tvStart.setText(getResources().getString(R.string.live_search_on_follow));
                HnToastUtils.showToastShort(getString(R.string.live_follow_success));
            } else {
                isFollow = "N";
                tvFollow.setVisibility(View.VISIBLE);
                tvStart.setText(getResources().getString(R.string.live_follow_anchor));
                HnToastUtils.showToastShort(getString(R.string.live_cancle_follow_success));
            }
        } else if ("pay_room_price".equals(type)) {//支付房间价格
            if (TextUtils.isEmpty(mAnchor_id) || !mAnchor_id.equals(uid)) return;
            HnPayRoomPriceModel model = (HnPayRoomPriceModel) obj;
            if (model != null && model.getD() != null) {
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Hide_Mask, 0));
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Close_Dialog, 0));
                if (model.getD().getUser() != null)
                    mOwn_Coin = model.getD().getUser().getUser_coin();
                if ("2".equals(mLiveType)) {
                    hasPayMenPiaoLive = true;
                }
            }
        } else if ("join_live_room".equals(type)) {//获取房间信息
            if (TextUtils.isEmpty(mAnchor_id) || !mAnchor_id.equals(uid)) return;
            HnLiveRoomInfoModel model = (HnLiveRoomInfoModel) obj;
            if (model != null && model.getD() != null) {
                mRoomInfo = model.getD();
                updateUI();
                if (mFramePay != null && mFramePay.getVisibility() != View.VISIBLE)
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Scroll_viewPager, true));
            }
        } else if ("user_info".equals(type)) {//获取主播信息
            HnUserInfoDetailModel model = (HnUserInfoDetailModel) obj;
            if (model != null && model.getD() != null) {
                updateUiToAnchorLeave(model.getD());
            }
            //网络请求获取礼物列表
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
            HnGiftListModel model = (HnGiftListModel) obj;
            if (model.getD() != null && model.getD().getGift() != null && mHnGiftBiz != null) {
                mHnGiftBiz.transformData(model.getD().getGift());
            }
            //从数据库中获取到数据
        } else if (HnGiftBiz.GET_GIFT_LIST_FROM_DB == type) {
            ArrayList<GiftListBean> giftList = (ArrayList<GiftListBean>) obj;
            if (giftList != null && giftList.size() > 0) {
                gifts.clear();
                gifts.addAll(giftList);
            }
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_NO_READ_MSG.equals(type)) {
            try {
                Long num = Long.valueOf(TextUtils.isEmpty(obj + "") ? "0" : obj + "");
                setNewMsgStatue(num);
            } catch (Exception e) {
            }
        }
    }


    /**
     * 网络请求:请求失败
     *
     * @param type 类型 标识符
     * @param code 错误码
     * @param msg  错误信息
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if (roomBottomRela == null || mActivity == null || mHnAudienceBiz == null || mHnAudienceViewBiz == null) {
            return;
        }
        if ("send_Messaqge".equals(type)) {//发送公聊消息
            HnToastUtils.showToastShort(msg);
        } else if ("follow".equals(type)) {//关注
            HnToastUtils.showToastShort(msg);
        } else if ("pay_room_price".equals(type)) {//支付房间价格

            if (code == HnServiceErrorUtil.USER_COIN_NOT_ENOUGH) {//用户余额不足
                isBananceNotEnough = true;

                mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, false, mFramePay);
                mHnAudienceBiz.closePayObservable();
            }
        } else if ("join_live_room".equals(type)) {//获取房间信息
            if (code == HnServiceErrorUtil.LIVE_END_ERROR) {//主播不在家
                if (mHnAudienceBiz != null) {
                    mHnAudienceBiz.requestToGetLeaverAnchorInfo(bean.getUid());
                }
            }
            HnToastUtils.showToastShort(msg);
            //viewpagerbuy
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Scroll_viewPager, false));
        } else if ("user_info".equals(type)) {//获取主播信息
            HnToastUtils.showToastShort(msg);
            //网络请求获取礼物列表
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
            HnToastUtils.showToastShort(msg);
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_NO_READ_MSG.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 大礼物下载成功
     *
     * @param isShow 下载完成后是否显示
     * @param data   礼物数据
     */
    @Override
    public void downloadGiftSuccess(boolean isShow, GiftListBean data, Object obj) {
        if (roomBottomRela == null || mActivity == null || mHnAudienceBiz == null || mBigGiftActionManager == null) {
            return;
        }
        gifts = mHnAudienceBiz.updateGiftListData(obj, isShow, data, mBigGiftActionManager, gifts);
    }

    /**
     * 大礼物下载失败
     *
     * @param code 错误码
     * @param msg  原因
     * @param data 礼物数据
     */
    @Override
    public void downloadGiftFail(int code, String msg, GiftListBean data) {

    }

    /**
     * 当用户进入直播间有免费倒计时查看，倒计时结束触发
     */
    @Override
    public void freeLookFinish() {
        if (mHnAudienceViewBiz == null) return;
        if ("1".equals(mLiveType)) {
            if (TextUtils.isEmpty(mOwnIsVip) || "N".equals(mOwnIsVip)) {//不是vip
                isBananceNotEnough = false;
                mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, false, mFramePay);
            }
        } else {
            isBananceNotEnough = false;
            mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, false, mFramePay);
        }
    }

    /**
     * vip 到期  针对vip
     */
    @Override
    public void vipComeDue() {
        mHnAudienceBiz.closeVIPObservable();
        mHnAudienceViewBiz.showVIPOoutTimeDialog(mActivity);
    }


    /**
     * handler处理线程
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (mActivity == null || msg.obj == null) return;
                if (HnWebscoketConstants.Handler_Join == msg.what) {
                    /** 用户进入 */
                    addNoticeFirst();

                    //进场动画 用户等级需在15级以上
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    //更新消息列表
                    messageList = mHnAudienceBiz.addMsgData(msg.obj, messageList);
                    initMessageAdapter();

                    if (bean != null && bean.getData() != null && bean.getData().getFuser() != null) {
                        if ("Y".equals(bean.getData().getFuser().getUser().getIs_level_effect())
                                || "Y".equals(bean.getData().getFuser().getUser().getUser_is_member())) {
                            if (mLlEnterRoom.getVisibility() == View.INVISIBLE || mLlEnterRoom.getVisibility() == View.GONE) {
                                setEnterAnim(bean.getData().getFuser());
                            } else {
                                mEnterData.add(bean.getData().getFuser());
                            }
                        }
                        if (HnBaseApplication.getmUserBean().getUser_id().equals(bean.getData().getFuser().getUser().getUser_id())) {
                            if (mHnAudienceBiz != null && mRoomInfo != null && mRoomInfo.getAnchor() != null)
                                mHnAudienceBiz.requestToGetRoomUser(mRoomInfo.getAnchor().getUser_id());
                        }

//                        if (bean.getData().getFuser().getUser() != null) {
//                            HnReceiveSocketBean.DataBean.FuserBean.UserBean user = bean.getData().getFuser().getUser();
//                            OnlineBean onlineBean = new OnlineBean(user.getUser_id(), user.getUser_avatar(), "0", user.getUser_is_member());
//                            //人数计算
//                            mOnLineNumber = mOnLineNumber + 1;
//                            tvPeopleNumber.setText(HnUtils.setNoPoint(mOnLineNumber + "") + "人");
//                            mAllList.add(onlineBean);
//                            initUserHeaderAdapter();
//                        }
                    }

                } else if (HnWebscoketConstants.Handler_Public_Msg == msg.what) {
                    addNoticeFirst();
                    /** 公聊 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    messageList = mHnAudienceBiz.addMsgData(msg.obj, messageList);
                    initMessageAdapter();
                } else if (HnWebscoketConstants.Handler_Send_Anchor == msg.what) {
                    /** 更新主播信息 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean.getData().getAnchor() != null) {
                        setRank(bean.getData().getAnchor().getAnchor_ranking());
                        tvCoin.setText(HnUtils.setTwoPoint(bean.getData().getAnchor().getUser_dot()));
                    }
                } else if (HnWebscoketConstants.Handler_Send_Gift == msg.what) {
                    /** 接收到发送礼物的数据 */
                    HnReceiveSocketBean bean = mHnAudienceBiz.getGiftData(msg.obj, gifts);
                    if (bean != null) {
                        addNoticeFirst();
                        //更新消息列表
                        messageList = mHnAudienceBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                        //触发礼物动画
                        boolean isHavaGift = mHnAudienceViewBiz.loadGift(bean, mActivity, mBigGiftActionManager, leftFiftLl, gifts, mHnGiftBiz);
                        if (!isHavaGift) {
                            mHnGiftBiz.requestToGetGiftList();
                        }
                        //获取主播最新u票数据
                        mAnchor_U_Piao = mHnAudienceBiz.getUPiaoFromGift(msg.obj, mAnchor_U_Piao);
                        tvCoin.setText(HnUtils.setTwoPoint(mAnchor_U_Piao));
                        if (bean.getData().getAnchor() != null) {
                            setRank(bean.getData().getAnchor().getAnchor_ranking());
                        }
                    }


                } else if (HnWebscoketConstants.Handler_Barrage == msg.what) {
                    /** 弹幕 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    mHnAudienceViewBiz.setDanmu(danmakuActionManager, msg.obj);

                    if (bean.getData().getAnchor() != null) {
                        setRank(bean.getData().getAnchor().getAnchor_ranking());
                        tvCoin.setText(HnUtils.setTwoPoint(bean.getData().getAnchor().getUser_dot()));
                    }
                } else if (HnWebscoketConstants.Handler_Stop_Live == msg.what) {
                    /** 主播停止直播 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData() != null && bean.getData().getAnchor_user_id().equals(mRoomInfo.getAnchor().getUser_id())) {
                        quitGroupOrRemove(true);

                        mHnAudienceBiz.leaveAnchorLiveRomm(msg.obj, mAnchor_id, mActivity);
                    }

                } else if (HnWebscoketConstants.Handler_Onlines == msg.what) {
                    /** 直播间真实在线人推送 */
                    HnOnlineModel model = (HnOnlineModel) msg.obj;

                    if (mActivity == null || mHnAudienceBiz == null) return;
                    ArrayList<OnlineBean> items = mHnAudienceBiz.getOnlienList(msg.obj, mAnchor_id);
                    //人数计算
                    mOnLineNumber = TextUtils.isEmpty(model.getData().getOnlines()) ? items.size() : Integer.parseInt(model.getData().getOnlines());
                    tvPeopleNumber.setText(HnUtils.setNoPoint(mOnLineNumber + "") + "人");
                    //在线实际人列表
                    ArrayList<OnlineBean> newAllList = mHnAudienceBiz.getShowOnlineList(items);
                    mAllList.clear();
                    mAllList.addAll(newAllList);
                    initUserHeaderAdapter();

                } else if (HnWebscoketConstants.Handler_Prohibit_Talk == msg.what) {
                    /** 禁言 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData().getUser() != null) {
                        addNoticeFirst();
                        messageList = mHnAudienceBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                    }

                } else if (HnWebscoketConstants.Handler_Kick == msg.what) {
                    /** 踢出房间 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData().getUser() != null) {
                        addNoticeFirst();
                        //更新消息列表
                        messageList = mHnAudienceBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                        if (TextUtils.isEmpty(mOwn_id))
                            mOwn_id = HnBaseApplication.getmUserBean().getUser_id();
                        if (mOwn_id.equals(bean.getData().getUser().getUser_id())) {
                            HnToastUtils.showToastShort(getString(R.string.live_kick_room));
                            quitGroupOrRemove(true);
                            mActivity.finish();
                        }

                        if (mHnAudienceBiz != null && mRoomInfo != null && mRoomInfo.getAnchor() != null)
                            mHnAudienceBiz.requestToGetRoomUser(mRoomInfo.getAnchor().getUser_id());
                    }
                } else if (HnWebscoketConstants.Handler_Attitude == msg.what) {
                    /** 点赞 */
                    //更新消息列表
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if ("Y".equals(bean.getData().getUser().getIs_first())) {
                        addNoticeFirst();
                        messageList = mHnAudienceBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                    }
                    if (mPlGreat != null) mPlGreat.addHeart();
                } else if (HnWebscoketConstants.Handler_Room_Admin == msg.what) {
                    /** 房管 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData().getUser() != null) {
                        mIsRoomAdmin = bean.getData().getUser().getIs_anchor_admin();
                        HnToastUtils.showToastShort("您已被主播" + ("Y".equals(mIsRoomAdmin) ? "设为" : "取消") + "管理员");
                    }
                } else if (HnWebscoketConstants.Handler_Kill_Live == msg.what) {
                    /** 禁播*/
                    final HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    HnToastUtils.showToastShort(bean.getMsg());
                    quitGroupOrRemove(true);
                    mHnAudienceBiz.leaveAnchorLiveRomm(msg.obj, mAnchor_id, mActivity);

                } else if (HnWebscoketConstants.Handler_System == msg.what) {

                    addNoticeFirst();
                    /**升级等系统消息*/
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    bean.setType(HnWebscoketConstants.System);
                    bean.setNotice(bean.getMsg());
                    messageList = mHnAudienceBiz.addMsgData(bean, messageList);
                    initMessageAdapter();
                }
            } catch (Exception e) {
            }
        }
    };

    /**
     * eventbus事件处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(HnLiveEvent event) {
        if (mActivity == null) return;
        if (event != null) {
            if (HnLiveConstants.EventBus.Close_Dialog.equals(event.getType())) {//关闭所有的dialog
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Scroll_viewPager, true));
                mFramePay.setVisibility(View.GONE);
            }
            if (HnLiveConstants.EventBus.Update_Room_Info.equals(event.getType())) {//切换房间相关信息
                //清除数据
                clearUiData();
                //重新加载数据
                HnLogUtils.i(TAG, "更新房间信息");
                HnLiveListModel.LiveListBean data = (HnLiveListModel.LiveListBean) event.getObj();

                quitGroupOrRemove(false);
                mHnAudienceBiz.leaveRoom(mAnchor_id);
                if (data != null) {
                    bean = data;
                    mAnchor_id = data.getUid();
                    mHnAudienceBiz.requestToGetRoomInfo(data.getUid());
                    if (mTCChatRoomMgr != null) {
                        mTCChatRoomMgr.joinGroup(data.getUid());
                    }
                }
            }
            if (HnLiveConstants.EventBus.Live_Back.equals(event.getType())) {//点击返回按钮
                mHnAudienceViewBiz.onBack(rlMessage, llBottomContainer, mElEmotion, mOnLineNumber, 1, mLiveType, mActivity);
            } else if (HnLiveConstants.EventBus.Click_User_Logo.equals(event.getType())) {//点击用户头像
                OnlineBean onlineBean = (OnlineBean) event.getObj();
                if (onlineBean != null) {
                    String uid = onlineBean.getId();
//                    if (mOwn_id.equals(uid)) return;//自己不可点击查看
                    mHnAudienceViewBiz.showUserInfoDialog(uid, mOwn_id, mActivity, 2, mRoomInfo.getAnchor().getUser_id(), mIsRoomAdmin);
                }
            } else if (HnLiveConstants.EventBus.Click_Public_Message.equals(event.getType())) {//点击公聊
                String userId = (String) event.getObj();
                if (!TextUtils.isEmpty(userId)) {
                    mHnAudienceViewBiz.showUserInfoDialog(userId, mOwn_id, mActivity, 2, mRoomInfo.getAnchor().getUser_id(), mIsRoomAdmin);
                }
            } else if (HnLiveConstants.EventBus.Clear_Unread_Count.equals(event.getType())) {//忽略未读
                unread_Count = 0;
                setNewMsgStatue(unread_Count);
            } else if (HnLiveConstants.EventBus.Receiver_New_Msg.equals(event.getType())) {//收到新的私信/系统消息
                unread_Count += 1;
                setNewMsgStatue(unread_Count);
            } else if (HnLiveConstants.EventBus.Clear_Unread.equals(event.getType())) {//进入私信详情后，需要清除对应的未读消息数

                String count = (String) event.getObj();
                HnLogUtils.i(TAG, "清除未读消息:" + count);
                if (!TextUtils.isEmpty(count)) {
                    Long num = Long.valueOf(count);
                    unread_Count = unread_Count - num;
                    setNewMsgStatue(unread_Count);
                    if (mHnAudienceBiz != null) mHnAudienceBiz.getNoReadMsg();
                }
            } else if (HnLiveConstants.EventBus.Close_Private_Letter_Dialog.equals(event.getType())) {//是否显示私信相关对话框
                isShowPrivateLetterDialog = false;
                Chatting_Uid = "-1";
            } else if (HnLiveConstants.EventBus.Chatting_Uid.equals(event.getType())) {//正在和你聊天的人的uid 用户用户点击头像后进入私信后,直播界面不对该用户的私信信息进行加减操作
                Chatting_Uid = (String) event.getObj();
            } else if (HnLiveConstants.EventBus.Follow.equals(event.getType())) {//关注
                int pos = event.getPos();
                String uid = (String) event.getObj();
                if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(uid) && mAnchor_id.equals(uid)) {
                    if (pos == 0) {//添加关注
                        isFollow = uid;
                        tvFollow.setVisibility(View.GONE);
                    } else if (pos == 1) {//取消关注
                        isFollow = "N";
                        tvFollow.setVisibility(View.VISIBLE);
                    }
                    mRoomInfo.getAnchor().setIs_follow(isFollow);
                }
            } else if (HnLiveConstants.EventBus.Update_User_Coin.equals(event.getType())) {//更新用户虚拟币
                String coin = (String) event.getObj();
                if (!TextUtils.isEmpty(coin)) {
                    mOwn_Coin = coin;
                }
            } else if (HnLiveConstants.EventBus.Show_Buy.equals(event.getType())) {//显示购买按钮
                if (!"rechargeCancle".equals(event.getObj() + ""))
                    mActivity.finish();
            }
            if (HnLiveConstants.EventBus.Countiune_Look.equals(event.getType())) {//继续观看
                //对于计时收费和门票收费，查看当前用户的金额够不够，不够则进行充值；对于vip直播 判断用户跳入vip购买界面
                //0：免费，1：VIP，2：门票，3：计时
                if ("3".equals(mLiveType) || "2".equals(mLiveType)) {
                    if (TextUtils.isEmpty(mOwn_Coin) || "0".equals(mOwn_Coin)) {
                        isBananceNotEnough = true;
                        mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, false, mFramePay);
                        mHnAudienceBiz.closePayObservable();
                    } else {
                        if (!TextUtils.isEmpty(mLivePrice)) {
                            double ownMoney = Double.valueOf(mOwn_Coin);
                            double livePrice = Double.valueOf(mLivePrice);
                            if (livePrice > ownMoney) {
                                isBananceNotEnough = true;
                                mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, false, mFramePay);
                                mHnAudienceBiz.closePayObservable();
                            } else {
                                mHnAudienceBiz.timePay(mAnchor_id, mLiveType, hasPayMenPiaoLive);
                            }
                        }
                    }
                } else if ("1".equals(mLiveType)) {//vip
                    if ("N".equals(mOwnIsVip) || TextUtils.isEmpty(mOwnIsVip)) {//不是vip会员
                        ARouter.getInstance().build("/app/HnMyVipMemberActivity").navigation();
                    } else {//vip计时
                        mHnAudienceBiz.startVIPTimeTask(mOwnIsVip);
                    }
                }
            } else if (HnLiveConstants.EventBus.Buy_VIP_Success.equals(event.getType())) {//购买vip成功
                mOwnIsVip = "Y";
                mHnAudienceBiz.startVIPTimeTask(mOwnIsVip);
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Hide_Mask, 0));
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Close_Dialog, 0));
            } else if (HnLiveConstants.EventBus.Leave_Live_Room.equals(event.getType())) {//用户离开/被挤下线
                quitGroupOrRemove(true);
//                mHnAudienceBiz.leaveRoom(mAnchor_id);
                if (mHnAudienceBiz != null) {
                    mHnAudienceBiz.closePayObservable();
                    mHnAudienceBiz.closeObservable();
                    mHnAudienceBiz.closeVIPObservable();
                }
                if (mActivity != null) {
                    mActivity.finish();
                }
            } else if (HnLiveConstants.EventBus.Click_Dan_Mu.equals(event.getType())) {//点击弹幕触发事件
                ReceivedSockedBean bean = (ReceivedSockedBean) event.getObj();
                if (bean != null) {
                    ReceivedSockedBean.DataBean.UserInfoBean fuser = bean.getData().getUser_info();
                    String uid = fuser.getUid();
                    //自己不可点击查看
                    if (mOwn_id.equals(uid)) {
                        return;
                    }
                    mHnAudienceViewBiz.showUserInfoDialog(uid, mOwn_id, mActivity, 2, mRoomInfo.getAnchor().getUser_id(), mIsRoomAdmin);
                }
            } else if (HnLiveConstants.EventBus.Click_Small_Gift.equals(event.getType())) {//点击小礼物触发事件
                GiftModel bean = (GiftModel) event.getObj();
                if (bean != null) {
                    String uid = bean.getSendUserId();
                    if (mOwn_id.equals(uid)) return;//自己不可点击查看
                    mHnAudienceViewBiz.showUserInfoDialog(uid, mOwn_id, mActivity, 2, mRoomInfo.getAnchor().getUser_id(), mIsRoomAdmin);
                }

            } else if (HnLiveConstants.EventBus.Pay_Success.equals(event.getType())) {//用户充值金额
                HnRechargeSuccessModel model = (HnRechargeSuccessModel) event.getObj();
                if (model != null && model.getData() != null) {
                    String coin = model.getData().getUser_coin();
                    if (!TextUtils.isEmpty(coin)) {
                        mOwn_Coin = coin;
                        HnLogUtils.i(TAG, "直播间接到用户充值信息:" + mOwn_Coin);
                    }
                }

            } else if (HnLiveConstants.EventBus.Close_Soft.equals(event.getType())) {//隐藏输入框
                if (mHnAudienceViewBiz != null) {
                    mHnAudienceViewBiz.onTouch(mLLContent, mElEmotion, rlMessage, llBottomContainer, mRoomTopContainer, mActivity);
                }
            } else if (HnLiveConstants.EventBus.Update_Gift_List.equals(event.getType())) {//更新礼物列表
                ArrayList<GiftListBean> giftList = (ArrayList<GiftListBean>) event.getObj();
                gifts.clear();
                gifts.addAll(giftList);
            } else if (HnLiveConstants.EventBus.Show_Mask.equals(event.getType())) {//显示遮罩层
                bigGift.setShow(HnLiveConstants.EventBus.Show_Mask);
                mIvMask.setVisibility(View.VISIBLE);

            } else if (HnLiveConstants.EventBus.Hide_Mask.equals(event.getType())) {//隐藏遮罩层
                bigGift.setShow(HnLiveConstants.EventBus.Hide_Mask);
                mIvMask.setVisibility(View.GONE);
            } else if (HnLiveConstants.EventBus.Look_Live_Click_Notify.equals(event.getType())) {//极光推送  如果是在看直播
                final HnJPushMessageInfo info = (HnJPushMessageInfo) event.getObj();
                if (info.getData().getAnchor_user_id().equals(mRoomInfo.getAnchor().getUser_id()))
                    return;
                CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                        if (info.getData().getAnchor_user_id().equals(mRoomInfo.getAnchor().getUser_id()))
                            return;
                        if (!TextUtils.isEmpty(mAnchor_id)) mHnAudienceBiz.leaveRoom(mAnchor_id);
                        HnAppManager.getInstance().finishActivity(HnAudienceActivity.class);
                        HnLiveSwitchDataUitl.joinRoom(mActivity, info.getData().getAnchor_category_id(), info.getData().getAnchor_live_pay(), info.getData().getAnchor_user_id(), info.getData().getAnchor_game_category_id());

                    }
                }).setTitle(HnUiUtils.getString(R.string.look_liveing)).setContent(HnUiUtils.getString(R.string.look_liveing_now_look_live)).show();
            }
        }
    }

    @Subscribe
    public void userCoinReduce(UserCoinReduceEvent event) {
        if (TextUtils.isEmpty(event.getCoin()) || mActivity == null) return;
        mOwn_Coin = event.getCoin();
        if ("3".equals(mLiveType) && View.VISIBLE == mIvMask.getVisibility() && View.VISIBLE != mFramePay.getVisibility()) {
            if (!TextUtils.isEmpty(mLivePrice)) {
                double ownMoney = Double.valueOf(mOwn_Coin);
                double livePrice = Double.valueOf(mLivePrice);
                if (livePrice > ownMoney) {
                    isBananceNotEnough = true;
                    mHnAudienceViewBiz.showHintDialog(mActivity, mLiveType, isBananceNotEnough, mLivePrice, false, mFramePay);
                    mHnAudienceBiz.closePayObservable();
                } else {
                    mHnAudienceBiz.timePay(mAnchor_id, mLiveType, hasPayMenPiaoLive);
                }
            }
        }

    }


    /**
     * 接收到webscoket推送过来的私信消息数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePrivateEvent(HnPrivateMsgEvent event) {
        if (event != null) {
            if (HnWebscoketConstants.Send_Pri_Msg.equals(event.getType())) {
                if (!isShowPrivateLetterDialog || "-1".equals(Chatting_Uid)) {
                    unread_Count += 1;
                    setNewMsgStatue(unread_Count);
                }
            }
        }
    }


    /**
     * 接收到webscoket推送过来的系统消息数据
     *
     * @param event
     */
    @Subscribe
    public void receiverSystemMsgEvent(HnReceiverSysMsgEvent event) {
        if (event != null) {
            if ("system_msg".equals(event.getType())) {
                if (!isShowPrivateLetterDialog) {
                    unread_Count += 1;
                    setNewMsgStatue(unread_Count);
                }
            }
        }
    }

    /**
     * 设置消息红点状态
     *
     * @param num
     */
    private void setNewMsgStatue(long num) {
        unread_Count = num;
        if (unread_Count <= 0) {
            unread_Count = 0;
        }
        if (unread_Count > 0) {
            tvUnreadCount.setVisibility(View.VISIBLE);
        } else {
            tvUnreadCount.setVisibility(View.GONE);
        }
        HnPrefUtils.setString(NetConstant.User.Unread_Count, unread_Count + "");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(mAnchor_id)) mHnAudienceBiz.leaveRoom(mAnchor_id);

        if (mHnAudienceBiz != null) {
            mHnAudienceBiz.closeObservable();
            mHnAudienceBiz.closeVIPObservable();
            mHnAudienceBiz.closePayObservable();
            mHnAudienceBiz = null;
        }
        HnHttpUtils.cancelRequest("pay_live");
        if (mHnGiftBiz != null) {
            mHnGiftBiz.closeDB();
            mHnGiftBiz = null;
        }
        if (mHnAudienceViewBiz != null) {
            mHnAudienceViewBiz = null;
        }
        quitGroupOrRemove(true);


        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mHnAudienceViewBiz != null) {
            mHnAudienceViewBiz.onLayoutChnage(bottom, oldBottom, mRoomTopContainer, mElEmotion, leftFiftLl, rlMessage, llBottomContainer, mActivity, keyHeight, ivEmoji);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mHnAudienceViewBiz != null) {
            mHnAudienceViewBiz.onTouch(view, mElEmotion, rlMessage, llBottomContainer, mRoomTopContainer, mActivity);
        }

        return false;
    }


    /**
     * 初始化直播间消息适配器
     */
    public void initMessageAdapter() {
        if (mMessageAdapter == null) {
            mMessageAdapter = new HnLiveMessageAdapter(mActivity, messageList, mOwn_id);
            messageListView.setAdapter(mMessageAdapter);
        } else {
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化直播间右上角头像列表适配器
     */
    public void initUserHeaderAdapter() {
        if (mHnOnlineRecAdapter == null) {
            mHnOnlineRecAdapter = new HnOnlineRecAdapter(mActivity, mAllList);
            mLinearLayoutManager = new LinearLayoutManager(mActivity);
            mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mHnOnlineRecAdapter);
        } else {
            mHnOnlineRecAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新主播离开房间后的ui
     *
     * @param anchor
     */
    private void updateUiToAnchorLeave(HnUserInfoDetailBean anchor) {
        //主播相关信息
        if (anchor != null) {
            isFollow = mHnAudienceViewBiz.setAnchorLeaveRoomViewData(mActivity, anchor, viewBg, mHeaderIcon, tvNick, tvStart, ivSex, tvUserLeaveLevel, tvLiveLevel);
            //主播id  优号
            mAnchor_id = anchor.getUser_id();
        }
    }

    /**
     * 清除ui上的显示
     */
    private void clearUiData() {
        isAddNotice = false;
        HnHttpUtils.cancelRequest("pay_live");
        HnHttpUtils.cancelRequest(mAnchor_id);
        /**
         * 清除进场动画
         */
        mHnAudienceViewBiz.clearEnterRoomAnim(mLlEnterRoom, mEnterData, highLevelEntenerAnima, highLevelExitAnima);
        /**
         * 清除websocket和定时器以及显示列表数据
         */
        mHnAudienceBiz.clearWebsocketAndListData(this, mAllList, messageList, mOwnIsVip, mHnOnlineRecAdapter, mMessageAdapter);
        mHnOnlineRecAdapter = null;
        mMessageAdapter = null;
        mOwnIsVip = null;


        /**
         * 清除左上角数据
         */
        mHnAudienceViewBiz.clearLeftTopViewData(mActivity, fivHeader, tvName, tvPeopleNumber, tvFollow, tvId);
        /**
         * 清除动画视图
         */
        mHnAudienceViewBiz.clearAnimtionView(leftFiftLl, mBigGiftActionManager, danmakuActionManager);
        /**
         * 清除输入视图数据
         */
        mHnAudienceViewBiz.clearInputView(mActivity, etSendData, rlMessage, llBottomContainer, mElEmotion, mToggleButton, mRoomTopContainer);
        /**
         * 主播离开直播间的数据重置
         */
        mHnAudienceViewBiz.clearAnchorLeaveRoomView(mActivity, viewBg, mHeaderIcon, tvNick, tvStart, ivSex, tvUserLeaveLevel, tvLiveLevel);
    }

}
