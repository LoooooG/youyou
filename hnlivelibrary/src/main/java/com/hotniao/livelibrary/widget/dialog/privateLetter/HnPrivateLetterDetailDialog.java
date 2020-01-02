package com.hotniao.livelibrary.widget.dialog.privateLetter;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.adapter.HnPrivateChatListAdapter;
import com.hotniao.livelibrary.biz.privateLetter.HnPrivateLetterDetailBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.HnSendPriMsgModel;
import com.hotniao.livelibrary.model.PrivateLetterDetailModel;
import com.hotniao.livelibrary.model.bean.Emoji;
import com.hotniao.livelibrary.model.bean.PrivateChatBean;
import com.hotniao.livelibrary.model.bean.PrivateLetterDetail;
import com.hotniao.livelibrary.model.bean.UserDialogBean;
import com.hotniao.livelibrary.model.event.EmojiClickEvent;
import com.hotniao.livelibrary.model.event.EmojiDeleteEvent;
import com.hotniao.livelibrary.model.event.HnFollowEvent;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.hotniao.livelibrary.ui.fragment.HnEmojiFragment;
import com.hotniao.livelibrary.util.EmojiUtil;
import com.hotniao.livelibrary.util.HnLiveScreentUtils;
import com.hotniao.livelibrary.util.HnLiveSystemUtils;
import com.hotniao.livelibrary.util.HnLiveUtils;
import com.hotniao.livelibrary.util.SystemUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：私聊聊天详情对话框
 * 创建人：mj
 * 创建时间：2017/9/18 18:02
 * 修改人：Administrator
 * 修改时间：2017/9/18 18:02
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateLetterDetailDialog extends AppCompatDialogFragment implements AbsListView.OnScrollListener, View.OnClickListener, BaseRequestStateListener, HnLoadingLayout.OnReloadListener {


    private final LayoutTransition transitioner = new LayoutTransition();//键盘和表情切换
    private String TAG = "HnPrivateLetterDetailDialog";
    private TextView tvNick;
    private ImageView ivBack;
    private ImageView ivClose;
    /**
     * 消息列表
     */
    private HnLoadingLayout mHnLoadingLayout;
    private ListView mRecyclerView;
    private PtrClassicFrameLayout mSwiperefresh;
    /**
     * 关注
     */
    private RelativeLayout rlAddFollow;
    private TextView tvFollow;
    /**
     * 发送消息
     */
    private CheckBox mBoxTips;
    private TextView tvSend;
    private TextView mTvPayCoin;
    private EditText etData;
    private ImageView ivSmile;
    private LinearLayout mContainer;
    private RelativeLayout mBottomCon;
    private RelativeLayout mRlPay;
    private LinearLayout mLLInput;
    private LinearLayout mOutcontainer;
    private BaseActivity mActivity;
    private HnEmojiFragment mHnEmojiFragment;


    /**
     * 自己的id
     */
    private String ownUid;
    //自己头像
    private String myAvator;
    /*自己的昵称*/
    private String myNick;
    /**
     * 对方用户id
     */
    private String mUid;
    /**
     * 对方昵称
     */
    private String nick;
    /**
     * 聊天ID
     */
    private String mChatRoomId;
    /**
     * 未读消息数
     */
    private String unread_msg = "0";
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 数据源
     */
    private List<PrivateChatBean> dataList = new ArrayList<>();
    /**
     * 适配器
     */
    private HnPrivateChatListAdapter mAdapter;
    /**
     * 是否关注 N 未关注  Y 关注了
     */
    private String isFolllow = "N";

    private String mDialogId = "0";


    /**
     * 业务逻辑类，用于处理业务逻辑
     */
    private HnPrivateLetterDetailBiz mHnPrivateLetterBiz;
    private View mView;
    private Dialog mDialog;
    private int mWidthPx;

    /**
     * 显示表情
     */
    private boolean isShowEmoj = false;

    public static HnPrivateLetterDetailDialog getInstance(String uid, String nick, String unread_msg, String mChatRoomId) {
        HnPrivateLetterDetailDialog sDialog = new HnPrivateLetterDetailDialog();
        Bundle b = new Bundle();
        b.putString("uid", uid);
        b.putString("nick", nick);
        b.putString("unread_msg", unread_msg);
        b.putString("mChatRoomId", mChatRoomId);
        sDialog.setArguments(b);
        return sDialog;
    }


    @Subscribe
    public void onVipEvent(HnLiveEvent event) {
        if (TextUtils.equals(event.getType(), HnLiveConstants.EventBus.Buy_VIP_Success)) {
            mLLInput.setVisibility(View.VISIBLE);
            mRlPay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.private_chat_back) {//返回
            dismiss();
        } else if (v.getId() == R.id.mTvPay) {
            if (mBoxTips.isChecked()) {
                HnPrefUtils.setBoolean("tips", true);
            }
            mLLInput.setVisibility(View.VISIBLE);
            mRlPay.setVisibility(View.GONE);
        } else if (v.getId() == R.id.mTv2bVip) {
            ARouter.getInstance().build("/app/HnMyVipMemberActivity").navigation();
        } else if (v.getId() == R.id.private_chat_close) {//关闭
            dismiss();
        } else if (v.getId() == R.id.private_chat_send) {//发送
            String content = etData.getText().toString().trim();
            mHnPrivateLetterBiz.requestToSendPrivateLetter(content, mUid, mChatRoomId);

        } else if (v.getId() == R.id.iv_add_follow) {//关注
            mHnPrivateLetterBiz.requestToFollow(mUid);

        } else if (v.getId() == R.id.private_chat_emoj) {//表情

            int emojihei = HnLiveSystemUtils.getKeyboardHeight(mActivity);

            if (isShowEmoj) {
                isShowEmoj = false;
                ivSmile.setImageResource(R.mipmap.smile);

                Window win = mDialog.getWindow();
                win.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams par = win.getAttributes();
                par.width = mWidthPx;
                par.height = (mWidthPx / 2) + HnLiveScreentUtils.dp2px(mActivity, 80.2f);
                win.setAttributes(par);
                if (mBottomCon.getVisibility() == View.VISIBLE) {
                    EmojiUtil.hideEmotionView(mActivity, HnLiveSystemUtils.isKeyBoardShow(mActivity), null, mOutcontainer, mBottomCon, mContainer, etData);
                }
                SystemUtils.showKeyBoard(etData);
            } else {
                isShowEmoj = true;
                ivSmile.setImageResource(R.mipmap.keyboard);

                Window window = mDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = mWidthPx;

                params.height = (mWidthPx / 2) + HnLiveScreentUtils.dp2px(mActivity, 80.2f) + HnLiveScreentUtils.dp2px(mActivity, 224);
                window.setAttributes(params);
                HnLogUtils.d(TAG, "emojiH:" + emojihei);
                HnLogUtils.d(TAG, "chatdialogHei:" + params.height);

                showEmotionView(false);
            }

        } else if (v.getId() == R.id.et_pri) {

            Window win = mDialog.getWindow();
            win.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams par = win.getAttributes();
            par.width = mWidthPx;
            par.height = (mWidthPx / 2) + HnLiveScreentUtils.dp2px(mActivity, 80.2f);
            win.setAttributes(par);
            if (mBottomCon.getVisibility() == View.VISIBLE) {
                ivSmile.setImageResource(R.mipmap.smile);
                EmojiUtil.hideEmotionView(mActivity, HnLiveSystemUtils.isKeyBoardShow(mActivity), null, mOutcontainer, mBottomCon, mContainer, etData);
            }

        }


    }

    private void setSendMsg() {
        etData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                             @Override
                                             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                 if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                     String content = etData.getText().toString().trim();
                                                     mHnPrivateLetterBiz.requestToSendPrivateLetter(content, mUid, mChatRoomId);
                                                     return true;
                                                 }
                                                 return false;
                                             }
                                         }

        );
        etData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etData.getText().toString())) tvSend.setSelected(false);
                else tvSend.setSelected(true);
            }
        });
    }

    /**
     * 显示emoji
     *
     * @param showAnimation
     */
    private void showEmotionView(boolean showAnimation) {
        if (showAnimation) {
            transitioner.setDuration(500);
        } else {
            transitioner.setDuration(0);
        }


        HnLiveSystemUtils.hideSoftInput(mActivity, etData);
        mContainer.getLayoutParams().height = HnLiveScreentUtils.dp2px(mActivity, 240);
        mContainer.setVisibility(View.VISIBLE);
        mBottomCon.setVisibility(View.VISIBLE);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //在5.0有navigationbar的手机，高度高了一个statusBar
        int lockHeight = HnLiveSystemUtils.getAppContentHeight(mActivity);
        int i = (mWidthPx / 2) /*+ ScreenUtils.dp2px(mActivity, 40.2f)*/;
        lockContainerHeight(i);
    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) mOutcontainer.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUid = bundle.getString("uid");
            nick = bundle.getString("nick");
            unread_msg = bundle.getString("unread_msg");
            mChatRoomId = bundle.getString("mChatRoomId");
            HnLogUtils.i("mj", "清除未读消息:" + unread_msg);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = (BaseActivity) getActivity();
        mWidthPx = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        mHnPrivateLetterBiz = new HnPrivateLetterDetailBiz(mActivity);
        mHnPrivateLetterBiz.setBaseRequestStateListener(this);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        HnLogUtils.d(TAG, "oncreateView");

        mView = View.inflate(mActivity, R.layout.live_dialog_private_letter_detail_layout, null);
        initView(mView);
        mDialog = getDialog();
        mDialog.setCanceledOnTouchOutside(true);

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                HnLiveUtils.hideSoftKeyBoard(etData, mActivity);
            }
        });
        //表情
        if (mHnEmojiFragment == null) {
            mHnEmojiFragment = HnEmojiFragment.Instance();
            getChildFragmentManager().beginTransaction().replace(R.id.dialog_emoji_containers, mHnEmojiFragment).commit();
        }

        //初始化数据
        initData();
        return mView;
    }

    @Override
    public void onStart() {

        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.getWindow().getAttributes().width = mWidthPx;
        mDialog.getWindow().getAttributes().height = (mWidthPx / 2) + HnLiveScreentUtils.dp2px(mActivity, 80.2f);
        super.onStart();
    }


    /**
     * 初始化布局
     *
     * @param view
     */
    private void initView(View view) {
        tvNick = (TextView) view.findViewById(R.id.private_chat_nick);
        tvNick.setText(nick);
        ivBack = (ImageView) view.findViewById(R.id.private_chat_back);
        ivBack.setOnClickListener(this);
        ivClose = (ImageView) view.findViewById(R.id.private_chat_close);
        ivClose.setOnClickListener(this);
         view.findViewById(R.id.mTvPay).setOnClickListener(this);

        mBoxTips = view.findViewById(R.id.mBoxTips);
        mRlPay = view.findViewById(R.id.mRlPay);
        mTvPayCoin = view.findViewById(R.id.mTvPayCoin);
        mLLInput = view.findViewById(R.id.mLLInput);
        view.findViewById(R.id.mTv2bVip).setOnClickListener(this);
        mHnLoadingLayout = (HnLoadingLayout) view.findViewById(R.id.mHnLoadingLayout);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setOnReloadListener(this);
        mRecyclerView = (ListView) view.findViewById(R.id.private_chat_listview);
        mSwiperefresh = (PtrClassicFrameLayout) view.findViewById(R.id.swiperefresh);

        mContainer = (LinearLayout) view.findViewById(R.id.dialog_emoji_containers);
        mBottomCon = (RelativeLayout) view.findViewById(R.id.bottom_con);
        mOutcontainer = (LinearLayout) view.findViewById(R.id.outcontainer);


        rlAddFollow = (RelativeLayout) view.findViewById(R.id.rl_add_follow);
        tvFollow = (TextView) view.findViewById(R.id.iv_add_follow);
        tvFollow.setOnClickListener(this);
        tvSend = (TextView) view.findViewById(R.id.private_chat_send);
        tvSend.setOnClickListener(this);
        etData = (EditText) view.findViewById(R.id.et_pri);
        etData.setOnClickListener(this);
        setSendMsg();
        ivSmile = (ImageView) view.findViewById(R.id.private_chat_emoj);
        ivSmile.setOnClickListener(this);
        //初始化适配器
        initAdapter();
        //设置监听
        setListener();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mPage = 1;
        mHnPrivateLetterBiz.requestToPrivateLetterDetail(mChatRoomId, mDialogId);
    }


    @Override
    public void requesting() {

    }


    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null) return;
        if ("Private_Msg_Detail_List".equals(type)) {//私信详情列表
            mActivity.setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
            mActivity.closeRefresh(mSwiperefresh);
            PrivateLetterDetailModel model = (PrivateLetterDetailModel) obj;
            if (model != null && model.getD() != null) {
                updateUI(model.getD());
            }
        } else if ("add_follow".equals(type)) {//点击关注
            HnToastUtils.showToastShort(mActivity.getResources().getString(R.string.live_follow_succeed));
            if (rlAddFollow != null && rlAddFollow.getVisibility() == View.VISIBLE) {
                rlAddFollow.setVisibility(View.GONE);
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Follow, mUid));
            }
        } else if ("send_msg".equals(type)) {//发送消息
            HnSendPriMsgModel model = (HnSendPriMsgModel) obj;
            if (model != null) {
                mDialogId = model.getD().getDialog().getDialog_id();
                PrivateChatBean bean = new PrivateChatBean();
                bean.setUid(ownUid);
                bean.setAvator(myAvator);
                bean.setNick(myNick);
                bean.setShowMsgTyoe("ownermsg");
                bean.setMsgContent(model.getD().getDialog().getMsg());
                if (!TextUtils.isEmpty(model.getD().getDialog().getTime())) {
                    bean.setAdd_time(Long.valueOf(model.getD().getDialog().getTime()) * 1000 + "");
                }
                dataList.add(bean);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setSelection(dataList.size() - 1);
            }
            etData.setText("");
        }

    }


    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null) return;

        if ("Private_Msg_Detail_List".equals(type)) {//私信详情列表
            mActivity.closeRefresh(mSwiperefresh);
            if (mPage == 1) {
                if (REQUEST_NET_ERROR == code) {
                    mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                } else {
                    mActivity.setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }

        } else if ("add_follow".equals(type)) {//点击关注
            HnToastUtils.showToastShort(msg);
        } else if ("send_msg".equals(type)) {//发送消息
            if(code==10024){
                CommDialog.newInstance(getContext())
                        .setContent(msg)
                        .setRightText("去充值")
                        .setClickListen(new CommDialog.TwoSelDialog() {
                            @Override
                            public void leftClick() {

                            }

                            @Override
                            public void rightClick() {
                                ARouter.getInstance().build("/app/HnMyRechargeActivity").navigation();
                            }
                        })
                        .show();
            }else {
                HnToastUtils.showToastShort(msg);
            }
        }
    }

    /**
     * 更新界面ui
     *
     * @param result 私信详情数据
     */
    private void updateUI(PrivateLetterDetail result) {
        if (mPage == 1) {
            isFolllow = result.getUser().getIs_follow();
            if ("N".equals(isFolllow)) {
                rlAddFollow.setVisibility(View.VISIBLE);
            } else {
                rlAddFollow.setVisibility(View.GONE);
            }
            if (TextUtils.equals(result.getUser().isIs_free(),"N") && !HnPrefUtils.getBoolean("tips", false)) {
                mTvPayCoin.setText("与主播聊天，需要支付" + result.getMsg_price() + "金币/条");
                mRlPay.setVisibility(View.VISIBLE);
                mLLInput.setVisibility(View.GONE);
            } else {
                mRlPay.setVisibility(View.GONE);
                mLLInput.setVisibility(View.VISIBLE);
            }
        }


        //获取自己的数据
        //自己id
        ownUid = HnBaseApplication.getmUserBean().getUser_id();
        //自己的昵称
        myNick = HnBaseApplication.getmUserBean().getUser_nickname();
        //自己的头像
        myAvator = HnBaseApplication.getmUserBean().getUser_avatar();
        //获取对方的数据
        List<UserDialogBean> msgList = result.getUser_dialog();
        if (msgList != null && msgList.size() > 0) {
            mDialogId = msgList.get(msgList.size() - 1).getDialog_id();
            List<PrivateChatBean> list = new ArrayList<>();
            for (int i = 0; i < msgList.size(); i++) {
                PrivateChatBean data = new PrivateChatBean();
                UserDialogBean bean = msgList.get(i);
                data.setNick(bean.getFrom_user().getUser_nickname());
                data.setUid(bean.getFrom_user().getUser_id());
                data.setAvator(bean.getFrom_user().getUser_avatar());
                if (bean.getFrom_user().getUser_id().equals(ownUid)) {
                    data.setShowMsgTyoe("ownermsg");
                } else {
                    data.setShowMsgTyoe("usermsg");
                }


                if (!TextUtils.isEmpty(bean.getTime())) {
                    data.setAdd_time(Long.valueOf(bean.getTime()) * 1000 + "");
                }
                data.setMsgContent(bean.getMsg());
                list.add(data);
            }
            //排序
            Collections.sort(list, new Comparator<PrivateChatBean>() {
                @Override
                public int compare(PrivateChatBean a, PrivateChatBean b) {
                    String a_time = a.getAdd_time();
                    String b_time = b.getAdd_time();
                    if (!TextUtils.isEmpty(a_time) && !TextUtils.isEmpty(b_time)) {
                        if (Long.valueOf(a_time) - Long.valueOf(b_time) > 0) {
                            return 1;
                        } else if (Long.valueOf(a_time) - Long.valueOf(b_time) < 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                    return 0;
                }
            });


            if (mPage == 1) {
                dataList.addAll(list);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setSelection(dataList.size() - 1);
            } else {
                dataList.addAll(0, list);
                mAdapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    mRecyclerView.setSelection(dataList.size() - list.size() - 1);
                }
            }
        }


    }

    @Override
    public void onReload(View v) {
        initData();
    }


    /**
     * 初始化适配器
     */
    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new HnPrivateChatListAdapter(mActivity, dataList, true);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        //刷新监听
        mSwiperefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage += 1;
                mHnPrivateLetterBiz.requestToPrivateLetterDetail(mChatRoomId, mDialogId);
            }
        });
        //listview滑动监听
        mRecyclerView.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean enable = false;
        if (mRecyclerView != null && mRecyclerView.getChildCount() > 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = mRecyclerView.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = mRecyclerView.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        mSwiperefresh.setEnabled(enable);
    }


    /**
     * 接收到webscoket推送过来的私信消息数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePrivateEvent(HnPrivateMsgEvent event) {
        if (event != null) {
            if (HnWebscoketConstants.Send_Pri_Msg.equals(event.getType())) {
                HnPrivateMsgModel.DataBean model = event.getData().getData();
                if (model != null) {
                    String id = model.getDialog().getFrom_user().getUser_id();
                    if (!TextUtils.isEmpty(id) && mUid.equals(id)) {
                        PrivateChatBean bean = new PrivateChatBean();
                        bean.setUid(mUid);
                        bean.setAvator(model.getDialog().getFrom_user().getUser_avatar());
                        bean.setNick(model.getDialog().getFrom_user().getUser_nickname());
                        bean.setShowMsgTyoe("usermsg");
                        bean.setMsgContent(model.getDialog().getMsg());
                        bean.setAdd_time(Long.valueOf(model.getDialog().getTime()) * 1000 + "");
                        dataList.add(bean);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setSelection(dataList.size() - 1);
                    }

                }
            }
        }
    }


    /**
     * 接收到关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveFollowEvent(HnFollowEvent event) {
        if (event != null) {
            boolean isFolow = event.isFollow();
            String id = event.getUid();
            if (mUid.equals(id)) {
                if (isFolow) {
                    rlAddFollow.setVisibility(View.GONE);
                    isFolllow = "1";
                } else {
                    rlAddFollow.setVisibility(View.VISIBLE);
                    isFolllow = "0";
                }
            }
        }
    }


    @Subscribe
    public void emojiDelete(EmojiDeleteEvent deleteEvent) {

        String text = etData.getText().toString();

        HnLogUtils.d(TAG, "走到表情删除方法中");

        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                etData.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            etData.getText().delete(index, text.length());
            displayTextView();
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        etData.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        displayTextView();
    }

    @Subscribe
    public void emojiClick(EmojiClickEvent event) {

        Emoji emoji = event.getEmoji();

        HnLogUtils.d(TAG, "走到表情点击方法中");

        if (emoji != null) {
            int index = etData.getSelectionEnd();
            Editable editable = etData.getEditableText();
            editable.append(emoji.getContent());

            HnLogUtils.d(TAG, "表情点击");
        }
        displayTextView();
    }

    private void displayTextView() {
        try {
            EmojiUtil.handlerEmojiText(etData, etData.getText().toString(), mActivity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mHnPrivateLetterBiz.requestToExitMsgDetail(mChatRoomId);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Reset_Data, 0));
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Clear_Unread, unread_msg));
        HnLogUtils.i("mj", "清除未读消息:" + unread_msg);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDialogShowMark(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Close_Dialog_Show_Mark.equals(event.getType()) && isAdded())
            dismiss();

    }

}
