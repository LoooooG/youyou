package com.hotniao.livelibrary.ui.anchor.fragment;


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
import android.util.Log;
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
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnFastClickUtil;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.adapter.HnLiveMessageAdapter;
import com.hotniao.livelibrary.adapter.HnOnlineRecAdapter;
import com.hotniao.livelibrary.biz.anchor.HnAnchorBiz;
import com.hotniao.livelibrary.biz.anchor.HnAnchorInfoListener;
import com.hotniao.livelibrary.biz.anchor.HnAnchorViewBiz;
import com.hotniao.livelibrary.biz.gift.HnGiftBiz;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.giflist.HnDonwloadGiftStateListener;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.HnGiftListModel;
import com.hotniao.livelibrary.model.HnJPushMessageInfo;
import com.hotniao.livelibrary.model.HnOnlineModel;
import com.hotniao.livelibrary.model.HnStartLiveInfoModel;
import com.hotniao.livelibrary.model.bean.HnGiftListBean;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.OnlineBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.model.event.HnLiveBeautyEvent;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.event.HnReceiverSysMsgEvent;
import com.hotniao.livelibrary.ui.anchor.activity.HnAnchorActivity;
import com.hotniao.livelibrary.ui.beauty.BeautyDialogFragment;
import com.hotniao.livelibrary.util.HnLiveSwitchDataUitl;
import com.hotniao.livelibrary.widget.danmu.DanmakuActionManager;
import com.hotniao.livelibrary.widget.danmu.DanmakuChannel;
import com.hotniao.livelibrary.widget.dialog.HnInputTextLiveDialog;
import com.hotniao.livelibrary.widget.dialog.HnPlatfromListDialog;
import com.hotniao.livelibrary.widget.dialog.HnShareLiveDialog;
import com.hotniao.livelibrary.widget.gift.GiftManage;
import com.hotniao.livelibrary.widget.gift.GiftModel;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.hotniao.livelibrary.widget.gift.LeftGiftsItemLayout;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftChannel;
import com.hotniao.livelibrary.widget.periscope.PeriscopeLayout;
import com.imlibrary.TCChatRoomMgr;
import com.lqr.emoji.EmotionLayout;
import com.tencent.TIMMessage;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：主播直播间  -- 互动层
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorInfoFragment extends BaseFragment implements View.OnLayoutChangeListener, View.OnClickListener, HnAnchorInfoListener, View.OnTouchListener, HnDonwloadGiftStateListener {

    /**
     * 布局文件组件
     */
    //头部布局
    private RelativeLayout mRoomTopContainer;
    private RelativeLayout llRoot;
    private ImageView ivVip;
    private FrescoImageView ivHeader;
    private TextView mTvName;
    private TextView tvPeopleNumber;
    private TextView tv_u_piao;
    private TextView tvId;
    private TextView mTvNetSpeed;//实时网速
    private TextView mTvPlatfromRank;//排名
    private RecyclerView mRecyclerview;
    private TextView tvFolllow;
    private LinearLayout llContain;
    private LinearLayout mLlContent;
    //底部布局
    private ImageButton ibMessage;
    private ImageButton ibPrivateLetter;
    private ImageButton ibMeiYan;
    private ImageButton ibCamera;
    private ImageButton ibClose, mIvShare;
    private TextView tvUnreadCount;
    private LinearLayout llBottomContainer;
    private FrameLayout frlBottom;

    //中间布局
    private LinearLayout llContainerVG;
    private DanmakuChannel mDanmakuChannelA;
    private DanmakuChannel mDanmakuChannelB;
    //进场特效
    private LinearLayout mLlEnterRoom;
    private TextView mTvEntLv;
    private TextView mTvEntName;
    private View mViewEnt;

    private PeriscopeLayout plReceivestar;
    private LeftGiftControlLayout leftFiftLl;
    private LeftGiftsItemLayout giftLl;
    private LeftGiftsItemLayout gift2;
    private BigGiftChannel bigGift;

    private PeriscopeLayout mPlGreat;
    //聊天布局
    private RelativeLayout rlMessage;
    private ToggleButton mToggleButton;
    private EditText etSendData;
    private ImageView ivEmoj;
    private TextView tvSend;
    private EmotionLayout mElEmotion;

    private ListView messageListView;
    /**
     * 推流状态显示
     */
    private TextView mTvPushStatue;

    /**
     * 礼物列表数据
     */
    private List<HnGiftListBean.GiftBean> giftList = new ArrayList<>();
    /**
     * 弹幕管理器
     */
    private DanmakuActionManager danmakuActionManager;
    /**
     * 礼物管理器
     */
    private BigGiftActionManager mBigGiftActionManager;

    /**
     * 开播之前服务器返回的数据
     */
    private HnStartLiveInfoModel.DBean result;
    /**
     * 未读消息数
     */
    private long unread_Count = 0;
    /**
     * 直播类型标识符   0 免费 1 计时收费  2 门票收费 3 vip
     */
    private int live_type = 0;
    /**
     * 在线人数统计=在线实际人数+在线机器人人数
     */
    private long mOnLineNumber = 0;


    /**
     * 主播id
     */
    private String mUid;
    /**
     * 当前用户的u票
     */
    private String u_piao = "0";
    /**
     * 弹幕价格  默认10
     */
    private String barrage_price = "10";
    /**
     * 业务逻辑类 处理直播间的相关业务，ui相关操作不要在类中操作
     */
    private HnAnchorBiz mHnAnchorBiz;
    /**
     * 该类主要是做一些ui的操作  如ui的显示和隐藏等，不做业务逻辑计算
     */
    private HnAnchorViewBiz mHnAnchorViewBiz;

    /**
     * 该类主要是获取礼物数据，从而进行礼物动画
     */
    private HnGiftBiz mHnGiftBiz;
    /**
     * 礼物列表
     */
    private ArrayList<GiftListBean> gifts = new ArrayList<>();

    /**
     * 直播间左下角小时适配器
     */
    private HnLiveMessageAdapter mMessageAdapter;
    /**
     * 左下角消息列表数据源
     */
    private ArrayList<HnReceiveSocketBean> messageList = new ArrayList<>();

    /**
     * 用于展示的列表数据  小于等于30  在线实际在线人在前，机器在后，若实际在线人数》30 只显示实际的在线30人
     */
    private ArrayList<OnlineBean> allList = new ArrayList<>();
    /**
     * 主播间右上角在线人数显示列表适配器
     */
    private HnOnlineRecAdapter mHnOnlineRecAdapter;
    /**
     * receiver的管理器
     */
    private LinearLayoutManager mLinearLayoutManager;


    /**
     * 阀值设置为屏幕高度的1/3
     */
    private int keyHeight;
    /**
     * websocket是否连接成功
     * 如果是腾讯IM 主播默认链接成功
     */
    private boolean webscoketConnectSuccess = true;
    /**
     * 是否正在显示私信dialog
     */
    private boolean isShowPrivateLetterDialog = false;
    /**
     * 正在和你聊天的人的uid 用户用户点击头像后进入私信后,直播界面不对该用户的私信信息进行加减操作
     */
    private String Chatting_Uid = "-1";
    /**
     * 是否被禁播 当没禁播时，只弹出被禁播的对话框，其他的不弹出
     */
    private boolean UserLiveForbidden = false;

    /**
     * 美颜调节面板弹窗
     */
    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();


    /**
     * 进场动画
     */
    private Animation highLevelExitAnima;
    private Animation highLevelEntenerAnima;
    private List<HnReceiveSocketBean.DataBean.FuserBean> mEnterData = new ArrayList<>();


    private UMShareAPI mShareAPI = null;
    private ShareAction mShareAction;

    private HnInputTextLiveDialog mInputTextDialog;

    /**
     * 腾讯IM
     */
    private TCChatRoomMgr mTCChatRoomMgr;
    private Gson mGson;

    public static HnAnchorInfoFragment newInstance(HnStartLiveInfoModel.DBean result, int live_type) {
        HnAnchorInfoFragment fragment = new HnAnchorInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", result);
        bundle.putInt("live_type", live_type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fiv_header) {//头像  主播点击自己的头像  暂时屏蔽此功能
            mHnAnchorViewBiz.showUserInfoDialog(mUid, mUid, mActivity, 2, result.getUser_id(), "Y");
        } else if (v.getId() == R.id.ib_message) {//消息按钮
//            mHnAnchorViewBiz.showMessageSendLayout(rlMessage, llBottomContainer, etSendData, mActivity);
            showInputMsgDialog();
        } else if (v.getId() == R.id.iv_privateletter) {//私信
            if (result == null) return;
            isShowPrivateLetterDialog = true;
            mHnAnchorViewBiz.showPriveteLetterListDialog(mActivity, true, null, null, null, null, null);

        } else if (v.getId() == R.id.camera) {//相机
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Switch_Camera, null));
        } else if (v.getId() == R.id.opengl) {//美颜
//            EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Opne_GL, null));
            if (mBeautyDialogFragment.isAdded()) mBeautyDialogFragment.dismiss();
            else mBeautyDialogFragment.show(mActivity.getFragmentManager(), "");

        } else if (v.getId() == R.id.tv_close) {//关闭直播间
            mHnAnchorViewBiz.onBack(rlMessage, llBottomContainer, mElEmotion, mOnLineNumber, 0, live_type + "", mActivity);
        } else if (v.getId() == R.id.message_send_tv) {//发动消息
            ((InputMethodManager) etSendData.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            if (HnFastClickUtil.isFastClick()) {
                return;
            }
            String messageContent = etSendData.getText().toString().trim();
            boolean mIsDanmu = mToggleButton.isChecked();
            mHnAnchorBiz.sendMessaqge(messageContent, mIsDanmu, webscoketConnectSuccess, mUid);
        } else if (v.getId() == R.id.mTvPlatfromRank) {//悠悠直播排名
            if (result == null) return;
            HnPlatfromListDialog.newInstance(result.getUser_id()).show(mActivity.getFragmentManager(), "");
        } else if (v.getId() == R.id.tv_coin) {//收益 粉丝贡献榜
            if (result == null) return;
            ARouter.getInstance().build("/app/HnFansContributeListActivity").withString("userId", result.getUser_id()).navigation();
        } else if (v.getId() == R.id.mIvShare) {//分享
            if (result == null) return;
            HnShareLiveDialog.newInstance(mShareAPI, mShareAction, result.getShare_url(),
                    result.getUser_avatar(), result.getUser_nickname(), result.getUser_id(), true).show(mActivity.getFragmentManager(), "share");
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
                                                         mHnAnchorBiz.sendMessaqge(messageContent, mIsDanmu, webscoketConnectSuccess, mUid);
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
                if (TextUtils.isEmpty(etSendData.getText().toString())) tvSend.setSelected(false);
                else tvSend.setSelected(true);
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.live_fragment_live_anchor;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mShareAPI = UMShareAPI.get(mActivity);
        mShareAction = new ShareAction(mActivity);
        //初始化布局
        initViews();

        mInputTextDialog = new HnInputTextLiveDialog(mActivity, R.style.InputDialog);
        mInputTextDialog.setmOnTextSendListener(new HnInputTextLiveDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg, boolean isDanmu) {
                mHnAnchorBiz.sendMessaqge(msg, isDanmu, webscoketConnectSuccess, mUid);
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

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            result = (HnStartLiveInfoModel.DBean) bundle.getSerializable("data");
            live_type = bundle.getInt("live_type");
            updateUi();
        }


    }

    /**
     * 腾讯云
     */
    private void initTcIm() {
        //初始化消息回调
        mTCChatRoomMgr = TCChatRoomMgr.getInstance(result.getUser_id());

        mTCChatRoomMgr.setMessageListener(new TCChatRoomMgr.TCChatRoomListener() {
            @Override
            public void onJoinGroupCallback(int code, String msg) {
              /*  if (0 == code) webscoketConnectSuccess = true;
                else webscoketConnectSuccess = false;*/
                Log.e("#####################","#############");
            }

            @Override
            public void onSendMsgCallback(int code, TIMMessage timMessage) {
                Log.e("#####################","#############");
            }

            @Override
            public void onReceiveMsg(int type, String content) {
                HnLogUtils.e("TcIm--" + content);
                matchLiveMsg(content);
            }

            @Override
            public void onGroupDelete(String roomId) {
                if (mActivity == null || mHnAnchorBiz == null || TextUtils.isEmpty(mUid)) return;
                if (!TextUtils.isEmpty(roomId) && roomId.equals(mUid)) {
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Leave_Live_Room, null));
                    Bundle bundle = new Bundle();
                    bundle.putString("payType", live_type + "");
                    ARouter.getInstance().build("/app/HnAnchorStopLiveActivity").with(bundle).navigation();
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
                /*** 禁播*/
                HnReceiveSocketBean result = mGson.fromJson(data, HnReceiveSocketBean.class);
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Live_Forbidden, result.getMsg()));
            } else if (HnWebscoketConstants.System.equals(type)) {//升级等系统消息
                message.what = HnWebscoketConstants.Handler_System;
                mHandler.sendMessage(message);
            }

        } catch (Exception e) {
            HnLogUtils.e(TAG, "解析数据出现异常：" + e.getMessage());
        }

    }


    /**
     * 更新界面的ui
     */
    private void updateUi() {
        if (result == null) return;

        initTcIm();
        //默认打开美颜
        EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Opne_GL, null));
        //主播信息
        HnStartLiveInfoModel.DBean anchor = result;
        if (anchor != null) {
            //头像
            String avator = anchor.getUser_avatar();
            ivHeader.setController(FrescoConfig.getController(avator));
            mTvName.setText(anchor.getUser_nickname());
            //优号
            mUid = anchor.getUser_id();
            tvId.setText(getResources().getString(R.string.live_id) + mUid);

            //排名
            setRank(anchor.getAnchor_ranking());


            //优票
            u_piao = anchor.getUser_dot();
            tv_u_piao.setText(HnUtils.setTwoPoint(u_piao));
            //是否是vip
            String isVip = "0";
            if (TextUtils.isEmpty(isVip) || "0".equals(isVip)) {
                ivVip.setVisibility(View.GONE);
            } else {
                ivVip.setVisibility(View.VISIBLE);
            }
        }
        //系统公告
        List<String> notice = result.getNotices();
        messageList = mHnAnchorBiz.addSystemNotice(notice);
        //初始化消息适配器
        initMessageAdapter();
        //初始化在线人数适配器
        initUserHeaderAdapter();

        //弹幕价格
        barrage_price = result.getBarrage_fee();
        if (mInputTextDialog != null)
            mInputTextDialog.setPrice(result.getBarrage_fee());
        //心跳  主播心跳
        mHnAnchorBiz.startHeardBeat();
        //消息未读数
        String count = HnPrefUtils.getString(NetConstant.User.Unread_Count, "");
        if (!TextUtils.isEmpty(count) && !"0".equals(count)) {
            tvUnreadCount.setVisibility(View.VISIBLE);
            unread_Count = 0;
        } else {
            tvUnreadCount.setVisibility(View.GONE);
            unread_Count = Long.valueOf(0);
        }


        if (mHnAnchorBiz != null) mHnAnchorBiz.getNoReadMsg();
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


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mHnAnchorViewBiz != null) {
            mHnAnchorViewBiz.onLayoutChnage(bottom, oldBottom, mRoomTopContainer, mElEmotion, leftFiftLl, rlMessage, llBottomContainer, mActivity, keyHeight, ivEmoj);
        }
    }


    /**
     * 时间显示计时器
     *
     * @param date 秒数
     * @param time 转化好的时间
     */
    @Override
    public void showTimeTask(long date, String time, String type) {
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Show_Time, date));
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

    /**
     * 网络请求成功
     *
     * @param type     类型  标识符
     * @param response 返回的响应数据
     * @param obj      实体等数据   根据实际需求而定
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if ("send_Messaqge".equals(type)) {
            //清除消息
            etSendData.setText("");
            mToggleButton.setChecked(false);
        } else if ("gift_list".equals(type)) {//获取礼物列表
            HnGiftListModel model = (HnGiftListModel) obj;
            if (model.getD() != null && model.getD().getGift() != null) {
                giftList.addAll(model.getD().getGift());
            }
            //网络请求获取礼物列表
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
            HnGiftListModel model = (HnGiftListModel) obj;
            if (model.getD() != null && model.getD().getGift() != null) {
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
     * 网络请求失败
     *
     * @param type 类型 标识符
     * @param code 错误码
     * @param msg  错误信息
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if ("send_Messaqge".equals(type)) {//发送公聊消息
            HnToastUtils.showToastShort(msg);
        } else if ("gift_list".equals(type)) {//获取礼物列表

            //网络请求获取礼物列表
        } else if (HnLiveBaseRequestBiz.REQUEST_TO_GIFT_LIST == type) {
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
        if (mActivity == null) {
            return;
        }
        gifts = mHnAnchorBiz.updateGiftListData(obj, isShow, data, mBigGiftActionManager, gifts);

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
                    //更新消息列表
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData() != null && bean.getData().getFuser() != null) {
                        String id = bean.getData().getFuser().getUser().getUser_id();
                        if (mUid.equals(id)) return;//主播进场不加入消息列表
                        messageList = mHnAnchorBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                        //进场动画
                        if ("Y".equals(bean.getData().getFuser().getUser().getIs_level_effect())
                                || "Y".equals(bean.getData().getFuser().getUser().getUser_is_member())) {
                            if (mLlEnterRoom.getVisibility() == View.INVISIBLE || mLlEnterRoom.getVisibility() == View.GONE) {
                                setEnterAnim(bean.getData().getFuser());
                            } else {
                                mEnterData.add(bean.getData().getFuser());
                            }
                        }

//                        if (bean.getData().getFuser().getUser() != null) {
//                            HnReceiveSocketBean.DataBean.FuserBean.UserBean user = bean.getData().getFuser().getUser();
//                            OnlineBean onlineBean = new OnlineBean(user.getUser_id(), user.getUser_avatar(), "0", user.getUser_is_member());
//                            //人数计算
//                            mOnLineNumber = mOnLineNumber + 1;
//                            tvPeopleNumber.setText(HnUtils.setNoPoint(mOnLineNumber + "") + "人");
//                            allList.add(onlineBean);
//                            initUserHeaderAdapter();
//                        }

                    }
                } else if (HnWebscoketConstants.Handler_Public_Msg == msg.what) {
                    /** 公聊 */
                    messageList = mHnAnchorBiz.addMsgData(msg.obj, messageList);
                    initMessageAdapter();

                } else if (HnWebscoketConstants.Handler_Send_Anchor == msg.what) {
                    /** 更新主播信息 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean.getData().getAnchor() != null) {
                        setRank(bean.getData().getAnchor().getAnchor_ranking());
                        tv_u_piao.setText(HnUtils.setTwoPoint(bean.getData().getAnchor().getUser_dot()));
                    }

                } else if (HnWebscoketConstants.Handler_Send_Gift == msg.what) {
                    /** 接收到发送礼物的数据 */
                    HnReceiveSocketBean bean = mHnAnchorBiz.getGiftData(msg.obj, gifts);
                    if (bean != null) {
                        //更新消息列表
                        messageList = mHnAnchorBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                        //触发礼物动画
                        boolean isHavaGift = mHnAnchorViewBiz.loadGift(bean, mActivity, mBigGiftActionManager, leftFiftLl, gifts, mHnGiftBiz);
                        if (!isHavaGift) {
                            mHnGiftBiz.requestToGetGiftList();
                        }
                        //当主播获取礼物后，更新主播u票数
                        u_piao = bean.getData().getAnchor().getUser_dot();
                        tv_u_piao.setText(HnUtils.setTwoPoint(u_piao));
                        if (bean.getData().getAnchor() != null) {
                            setRank(bean.getData().getAnchor().getAnchor_ranking());
                        }
                    }

                } else if (HnWebscoketConstants.Handler_Barrage == msg.what) {
                    /** 弹幕 */
                    //触发弹幕动画
                    mHnAnchorViewBiz.setDanmu(danmakuActionManager, msg.obj);
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean.getData().getAnchor() != null) {
                        setRank(bean.getData().getAnchor().getAnchor_ranking());
                        tv_u_piao.setText(HnUtils.setTwoPoint(bean.getData().getAnchor().getUser_dot()));
                    }
                } else if (HnWebscoketConstants.Handler_Stop_Live == msg.what) {
                    /** 主播停止直播 */
                    if (!UserLiveForbidden) {
                        mHnAnchorViewBiz.showLiveFinishDialog(msg.obj, mUid, mActivity);
                    }

                } else if (HnWebscoketConstants.Handler_Onlines == msg.what) {
                    /** 直播间真实在线人推送 */
                    HnOnlineModel model = (HnOnlineModel) msg.obj;
                    ArrayList<OnlineBean> items = mHnAnchorBiz.getOnlienList(msg.obj, mUid);
                    //人数计算
                    mOnLineNumber = TextUtils.isEmpty(model.getData().getOnlines()) ? items.size() : Integer.parseInt(model.getData().getOnlines());
                    tvPeopleNumber.setText(HnUtils.setNoPoint(mOnLineNumber + "") + "人");
                    //在线实际人列表
                    ArrayList<OnlineBean> newAllList = mHnAnchorBiz.getShowOnlineList(items);
                    allList.clear();
                    allList.addAll(newAllList);
                    initUserHeaderAdapter();
                } else if (HnWebscoketConstants.Handler_Prohibit_Talk == msg.what) {
                    /** 禁言 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData().getUser() != null) {
                        messageList = mHnAnchorBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                    }

                } else if (HnWebscoketConstants.Handler_Kick == msg.what) {
                    /** 踢出房间 */
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if (bean != null && bean.getData().getUser() != null) {
                        //更新消息列表
                        messageList = mHnAnchorBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                    }
                    if (mHnAnchorBiz != null && !TextUtils.isEmpty(mUid))
                        mHnAnchorBiz.getRoomUser(mUid);

                } else if (HnWebscoketConstants.Handler_Attitude == msg.what) {
                    /** 点赞 */
                    //更新消息列表
                    final HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    if ("Y".equals(bean.getData().getUser().getIs_first())) {
                        messageList = mHnAnchorBiz.addMsgData(msg.obj, messageList);
                        initMessageAdapter();
                    }
                    if (mPlGreat != null) mPlGreat.addHeart();

                } else if (HnWebscoketConstants.Handler_Room_Admin == msg.what) {
                    /** 房管 */
                } else if (HnWebscoketConstants.Handler_System == msg.what) {
                    /**升级等系统消息*/
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) msg.obj;
                    bean.setType(HnWebscoketConstants.System);
                    bean.setNotice(bean.getMsg());
                    messageList = mHnAnchorBiz.addMsgData(bean, messageList);
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
        if (event != null) {
            if (HnLiveConstants.EventBus.Push_Stream_Statue.equals(event.getType())) {//推流状态显示
                if (0 == event.getPos()) {//推流受损
                    if (mTvPushStatue != null && View.VISIBLE != mTvPushStatue.getVisibility()) {
                        mTvPushStatue.setVisibility(View.VISIBLE);
                        if ("busyNetwork".equals(event.getObj()) && mHandler != null) {//如果是网络上行受阻，提示一下延迟三秒消失
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mActivity != null && mTvPushStatue != null) ;
                                    mTvPushStatue.setVisibility(View.GONE);
                                }
                            }, 3000);
                        }
                    }
                } else if (1 == event.getPos()) {//推流OK
                    if (mTvPushStatue != null && View.VISIBLE == mTvPushStatue.getVisibility()) {
                        mTvPushStatue.setVisibility(View.GONE);
                    }
                }

            } else if (HnLiveConstants.EventBus.Live_Back.equals(event.getType())) {//点击返回按钮
                mHnAnchorViewBiz.onBack(rlMessage, llBottomContainer, mElEmotion, mOnLineNumber, 0, live_type + "", mActivity);
            } else if (HnLiveConstants.EventBus.Click_User_Logo.equals(event.getType())) {//点击用户头像
                OnlineBean onlineBean = (OnlineBean) event.getObj();
                if (onlineBean != null) {
                    String uid = onlineBean.getId();
                    mHnAnchorViewBiz.showUserInfoDialog(uid, mUid, mActivity, 2, result.getUser_id(), "Y");
                }
            } else if (HnLiveConstants.EventBus.Click_Public_Message.equals(event.getType())) {//点击公聊
                String userId = (String) event.getObj();
                if (!TextUtils.isEmpty(userId)) {
                    mHnAnchorViewBiz.showUserInfoDialog(userId, mUid, mActivity, 2, result.getUser_id(), "Y");
                }

            } else if (HnLiveConstants.EventBus.Clear_Unread_Count.equals(event.getType())) {//忽略未读
                unread_Count = 0;
                setNewMsgStatue(unread_Count);
            } else if (HnLiveConstants.EventBus.Receiver_New_Msg.equals(event.getType())) {//收到新的私信/系统消息
                unread_Count += 1;
                setNewMsgStatue(unread_Count);
            } else if (HnLiveConstants.EventBus.Clear_Unread.equals(event.getType())) {//进入私信详情后，需要清除对应的未读消息数
                String count = (String) event.getObj();
                if (!TextUtils.isEmpty(count)) {
                    Long num = Long.valueOf(count);
                    unread_Count = unread_Count - num;
                    setNewMsgStatue(unread_Count);
                    if (mHnAnchorBiz != null) mHnAnchorBiz.getNoReadMsg();
                }
            } else if (HnLiveConstants.EventBus.Close_Private_Letter_Dialog.equals(event.getType())) {//是否显示私信相关对话框
                isShowPrivateLetterDialog = false;
                Chatting_Uid = "-1";
            } else if (HnLiveConstants.EventBus.Chatting_Uid.equals(event.getType())) {//正在和你聊天的人的uid 用户用户点击头像后进入私信后,直播界面不对该用户的私信信息进行加减操作
                Chatting_Uid = (String) event.getObj();
            } else if (HnLiveConstants.EventBus.Click_Dan_Mu.equals(event.getType())) {//点击弹幕触发事件
                ReceivedSockedBean bean = (ReceivedSockedBean) event.getObj();
                if (bean != null) {
                    ReceivedSockedBean.DataBean.UserInfoBean fuser = bean.getData().getUser_info();
                    String uid = fuser.getUid();
                    if (mUid.equals(uid)) return;//自己不可点击查看
                    mHnAnchorViewBiz.showUserInfoDialog(uid, mUid, mActivity, 2, result.getUser_id(), "Y");
                }
            } else if (HnLiveConstants.EventBus.Click_Small_Gift.equals(event.getType())) {//点击小礼物触发事件
                GiftModel bean = (GiftModel) event.getObj();
                if (bean != null) {
                    String uid = bean.getSendUserId();
                    if (mUid.equals(uid)) return;//自己不可点击查看
                    mHnAnchorViewBiz.showUserInfoDialog(uid, mUid, mActivity, 2, result.getUser_id(), "Y");
                }
            } else if (HnLiveConstants.EventBus.Live_Forbidden.equals(event.getType())) {//当用户被禁播/停播/禁用帐户时，停止直播间的推拉流,网络请求
                if (mHnAnchorBiz != null) {
                    mHnAnchorBiz.closeDbservable();
                }
                removeTcImListener();
                String time = (String) event.getObj();
                if (mHnAnchorViewBiz != null) {
                    UserLiveForbidden = true;
                    mHnAnchorViewBiz.showLiveForbidenDialog(mActivity, time);
                }
            } else if (HnLiveConstants.EventBus.Leave_Live_Room.equals(event.getType())) {//用户离开直播间/被挤下线

                if (mHnAnchorBiz != null) {
                    mHnAnchorBiz.closeDbservable();
                }
                removeTcImListener();
                HnLogUtils.i(TAG, "释放直播间资源");
                if (mActivity != null) {
                    mActivity.finish();
                }
            } else if (HnLiveConstants.EventBus.Update_Live_KBS.equals(event.getType())) {
                mTvNetSpeed.setText(event.getObj() + "kb/s");
            } else if (HnLiveConstants.EventBus.Liveing_Click_Notify.equals(event.getType())) {//极光点击发送
                final HnJPushMessageInfo info = (HnJPushMessageInfo) event.getObj();
                CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                        if (!info.getData().getAnchor_user_id().equals(mUid)) {
                            stopLive(mActivity, info);
                        }
                    }
                }).setTitle(HnUiUtils.getString(R.string.look_liveing)).setContent(HnUiUtils.getString(R.string.look_liveing_now_live)).show();
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


    @Override
    public void onDestroy() {
        if (mHnAnchorBiz != null) {
            mHnAnchorBiz.closeDbservable();
        }
        removeTcImListener();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 初始化布局
     */
    private void initViews() {
        EventBus.getDefault().register(this);
        //初始化ui相关操作类
        mHnAnchorViewBiz = new HnAnchorViewBiz();
        //初始化业务逻辑类
        mHnAnchorBiz = new HnAnchorBiz(this, mActivity);
        //礼物列表业务逻辑类
        mHnGiftBiz = new HnGiftBiz(mActivity, this, this);

        //美颜弹窗
        mBeautyDialogFragment = new BeautyDialogFragment();
        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, new BeautyDialogFragment.OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
                EventBus.getDefault().post(new HnLiveBeautyEvent(key, params));
            }

            @Override
            public void dismiss() {

            }
        });

        //头部视图
        llRoot = (RelativeLayout) mRootView.findViewById(R.id.root_view);
        llRoot.addOnLayoutChangeListener(this);
        llRoot.setOnTouchListener(this);
        //推流状态显示
        mTvPushStatue = (TextView) mRootView.findViewById(R.id.mTvPushStatue);
        //顶部控制容器
        mRoomTopContainer = (RelativeLayout) mRootView.findViewById(R.id.rl_top_con);
        //头像
        ivHeader = (FrescoImageView) mRootView.findViewById(R.id.fiv_header);
        ivHeader.setOnClickListener(this);
        //vip
        ivVip = (ImageView) mRootView.findViewById(R.id.iv_vip);
        //时间
        mTvName = (TextView) mRootView.findViewById(R.id.tv_time);
        //人数
        tvPeopleNumber = (TextView) mRootView.findViewById(R.id.tv_online);
        //关注
        tvFolllow = (TextView) mRootView.findViewById(R.id.tv_follow);
        tvFolllow.setVisibility(View.GONE);
        //u 票
        tv_u_piao = (TextView) mRootView.findViewById(R.id.tv_coin);
        tv_u_piao.setOnClickListener(this);
        //id号
        tvId = (TextView) mRootView.findViewById(R.id.tv_id);
        //实时网速
        mTvNetSpeed = (TextView) mRootView.findViewById(R.id.mTvNetSpeed);
        mTvNetSpeed.setVisibility(View.VISIBLE);
        //排名
        mTvPlatfromRank = (TextView) mRootView.findViewById(R.id.mTvPlatfromRank);
        mTvPlatfromRank.setOnClickListener(this);
        //顶部头像列表
        mRecyclerview = (RecyclerView) mRootView.findViewById(R.id.recy_online);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);

        mLlContent = (LinearLayout) mRootView.findViewById(R.id.ll_content);
        //底部布局
        llBottomContainer = (LinearLayout) mRootView.findViewById(R.id.bottom_container);
        frlBottom = (FrameLayout) mRootView.findViewById(R.id.bottom_fra);
        //消息
        ibMessage = (ImageButton) mRootView.findViewById(R.id.ib_message);
        ibMessage.setOnClickListener(this);
        //私信
        ibPrivateLetter = (ImageButton) mRootView.findViewById(R.id.iv_privateletter);
        ibPrivateLetter.setOnClickListener(this);
        //消息未读数
        tvUnreadCount = (TextView) mRootView.findViewById(R.id.tv_unread_pri);

        //camrea
        ibCamera = (ImageButton) mRootView.findViewById(R.id.camera);
        ibCamera.setOnClickListener(this);
        //分享
        mIvShare = (ImageButton) mRootView.findViewById(R.id.mIvShare);
        mIvShare.setOnClickListener(this);
        //美颜
        ibMeiYan = (ImageButton) mRootView.findViewById(R.id.opengl);
        ibMeiYan.setOnClickListener(this);
        //关闭
        ibClose = (ImageButton) mRootView.findViewById(R.id.tv_close);
        ibClose.setOnClickListener(this);
        //左下角消息显示列表
        messageListView = (ListView) mRootView.findViewById(R.id.message_listview);

        //中间布局
        llContainerVG = (LinearLayout) mRootView.findViewById(R.id.containerVG);
        mDanmakuChannelA = (DanmakuChannel) mRootView.findViewById(R.id.danA);
        mDanmakuChannelB = (DanmakuChannel) mRootView.findViewById(R.id.danB);
        mElEmotion = (EmotionLayout) mRootView.findViewById(R.id.elEmotion);
        //点赞
        mPlGreat = (PeriscopeLayout) mRootView.findViewById(R.id.mPlGreat);
        //进场特效
        mLlEnterRoom = (LinearLayout) mRootView.findViewById(R.id.rl_enter_effect);
        mTvEntLv = (TextView) mRootView.findViewById(R.id.tv_level);
        mTvEntName = (TextView) mRootView.findViewById(R.id.high_level);
        mViewEnt = mRootView.findViewById(R.id.mViewEnt);

        plReceivestar = (PeriscopeLayout) mRootView.findViewById(R.id.receive_star);
        leftFiftLl = (LeftGiftControlLayout) mRootView.findViewById(R.id.giftLl);
        giftLl = (LeftGiftsItemLayout) mRootView.findViewById(R.id.gift1);
        gift2 = (LeftGiftsItemLayout) mRootView.findViewById(R.id.gift2);
        //大礼物
        bigGift = (BigGiftChannel) mRootView.findViewById(R.id.ani_view);
        mBigGiftActionManager = new BigGiftActionManager();
        bigGift.setDanAction(mBigGiftActionManager);
        mBigGiftActionManager.addChannel(bigGift);

        danmakuActionManager = new DanmakuActionManager();
        mDanmakuChannelA.setDanAction(danmakuActionManager);
        mDanmakuChannelB.setDanAction(danmakuActionManager);
        danmakuActionManager.addChannel(mDanmakuChannelA);
        danmakuActionManager.addChannel(mDanmakuChannelB);

        //聊天容器
        rlMessage = (RelativeLayout) mRootView.findViewById(R.id.rl_message);
        etSendData = (EditText) mRootView.findViewById(R.id.message_input_et);
        etSendData.setFocusable(false);
        etSendData.setFocusableInTouchMode(false);

        tvSend = (TextView) mRootView.findViewById(R.id.message_send_tv);
        tvSend.setOnClickListener(this);

//        setSendMsg();

        mToggleButton = (ToggleButton) mRootView.findViewById(R.id.message_talk_tb);
        //公聊与弹幕切换
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etSendData.setHint(R.string.live_10_bi_content);
                    etSendData.setHint(barrage_price + HnBaseApplication.getmConfig().getCoin() + "/条");
                } else {
                    etSendData.setHint(R.string.live_input_chat_content);
                }
            }
        });

        ivEmoj = (ImageView) mRootView.findViewById(R.id.iv_emoji);


        mElEmotion.attachEditText(etSendData);
        mElEmotion.setEmotionAddVisiable(false);
        mElEmotion.setEmotionSettingVisiable(false);
        //获取屏幕高度
        int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //获取礼物列表数据
        mHnGiftBiz.getGiftListData();
        //礼物管理器初始化
        GiftManage.init(mActivity);
        //表情 键盘和输入框之间的处理
        mHnAnchorViewBiz.initEmotionKeyboard(mActivity, mLlContent, ivEmoj, etSendData, mElEmotion);

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


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mHnAnchorViewBiz != null) {
            mHnAnchorViewBiz.onTouch(view, mElEmotion, rlMessage, llBottomContainer, mRoomTopContainer, mActivity);
        }
        return false;
    }


    /**
     * 初始化直播间消息适配器
     */
    public void initMessageAdapter() {
        if (mMessageAdapter == null) {
            mMessageAdapter = new HnLiveMessageAdapter(mActivity, messageList, mUid);
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
            mHnOnlineRecAdapter = new HnOnlineRecAdapter(mActivity, allList);
            mRecyclerview.setAdapter(mHnOnlineRecAdapter);
        } else {
            mHnOnlineRecAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 主播结束直播
     *
     * @param context
     * @param info
     */
    private void stopLive(final Context context, final HnJPushMessageInfo info) {
        HnHttpUtils.getRequest(HnUrl.Stop_Live, null, HnUrl.Stop_Live, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    HnAppManager.getInstance().finishActivity(HnAnchorActivity.class);
                    HnLiveSwitchDataUitl.joinRoom(context, info.getData().getAnchor_category_id(), info.getData().getAnchor_live_pay(), info.getData().getAnchor_user_id(), info.getData().getAnchor_game_category_id());
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
}
