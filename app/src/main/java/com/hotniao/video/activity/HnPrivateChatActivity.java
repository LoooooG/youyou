package com.hotniao.video.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnFastClickUtil;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.biz.chat.HnPrivateLetterBiz;
import com.hn.library.global.HnConstants;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.util.SystemUtils;
import com.hotniao.livelibrary.adapter.HnPrivateChatListAdapter;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnSendPriMsgModel;
import com.hotniao.livelibrary.model.PrivateLetterDetailModel;
import com.hotniao.livelibrary.model.bean.Emoji;
import com.hotniao.livelibrary.model.bean.PrivateChatBean;
import com.hotniao.livelibrary.model.bean.PrivateLetterDetail;
import com.hotniao.livelibrary.model.bean.UserDialogBean;
import com.hotniao.livelibrary.model.event.EmojiClickEvent;
import com.hotniao.livelibrary.model.event.EmojiDeleteEvent;
import com.hotniao.livelibrary.model.event.HnFollowEvent;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.hotniao.livelibrary.ui.fragment.HnEmojiFragment;
import com.hotniao.livelibrary.util.EmojiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：私信详情
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnPrivateChatActivity") // 必须标明注解
public class HnPrivateChatActivity extends BaseActivity implements AbsListView.OnScrollListener, HnLoadingLayout.OnReloadListener, BaseRequestStateListener {

    private final LayoutTransition transitioner = new LayoutTransition();//键盘和表情切换
    @BindView(R.id.iv_add_follow)
    TextView ivAddFollow;
    @BindView(R.id.rl_add_follow)
    RelativeLayout mRlFollow;
    @BindView(R.id.private_chat_listview)
    ListView mPrivateChatListview;
    @BindView(R.id.swiperefresh)
    PtrClassicFrameLayout mSwiperefresh;
    @BindView(R.id.outcontainer)
    LinearLayout mOutcontainer;
    @BindView(R.id.et_pri)
    EditText mPrivateChatInput;
    @BindView(R.id.private_chat_emoj)
    ImageView privateChatEmoj;
    @BindView(R.id.iv_gift)
    ImageView mIvGift;
    @BindView(R.id.private_chat_send)
    TextView mPrivateChatSend;
    @BindView(R.id.emoji_container)
    LinearLayout mContainer;
    @BindView(R.id.gift_container)
    RelativeLayout mGiftContainer;
    @BindView(R.id.bottom_con)
    RelativeLayout mBottomCon;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.MHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.mRlPay)
    RelativeLayout mRlPay;
    @BindView(R.id.mLLInput)
    LinearLayout mLLInput;
    @BindView(R.id.mBoxTips)
    CheckBox mBoxTips;
    @BindView(R.id.mTvPayCoin)
    TextView mTvPayCoin;
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
     * 对方的id
     */
    private String mUid;
    /**
     * 对方昵称
     */
    private String nick;
    /**
     * 聊id
     */
    private String mChatRoomId;

    /**
     * 业务逻辑类，用于处理业务逻辑
     */
    private HnPrivateLetterBiz mHnPrivateLetterBiz;

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
    //是否显示表情
    private boolean isShowEmoj = false;

    @OnClick({R.id.private_chat_emoj, R.id.mTv2bVip, R.id.mTvPay, R.id.iv_gift, R.id.private_chat_send, R.id.iv_add_follow, R.id.et_pri})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTv2bVip:
                startActivity(new Intent(this, HnMyVipMemberActivity.class));
                break;
            case R.id.mTvPay:
                if (mBoxTips.isChecked()) {
                    HnPrefUtils.setBoolean("tips", true);
                }
                mLLInput.setVisibility(View.VISIBLE);
                mRlPay.setVisibility(View.GONE);
                break;
            case R.id.iv_gift:  //出现礼物布局
                mHnPrivateLetterBiz.showGiftView(SystemUtils.isKeyBoardShow(this), transitioner, mGiftContainer, mBottomCon, mContainer, mPrivateChatInput, mOutcontainer);
                break;
            case R.id.private_chat_emoj: //emojo表情
                if (isShowEmoj) {
                    isShowEmoj = false;
                    privateChatEmoj.setImageResource(R.mipmap.smile);
                    EmojiUtil.hideEmotionView(this, SystemUtils.isKeyBoardShow(this), mGiftContainer, mOutcontainer, mBottomCon, mContainer, mPrivateChatInput);
                    SystemUtils.showKeyBoard(mPrivateChatInput);
                } else {
                    isShowEmoj = true;
                    privateChatEmoj.setImageResource(R.mipmap.keyboard);
                    mHnPrivateLetterBiz.showEmotionView(SystemUtils.isKeyBoardShow(this), transitioner, mContainer, mBottomCon, mGiftContainer, mOutcontainer, mPrivateChatInput);
                }
                break;
            case R.id.private_chat_send:
                if (HnFastClickUtil.isFastClick()) {
                    return;
                }
                String content = mPrivateChatInput.getText().toString().trim();
                mHnPrivateLetterBiz.requestToSendPrivateLetter(content, mUid, mChatRoomId);
                break;
            case R.id.iv_add_follow:  //关注
                mHnPrivateLetterBiz.requestToFollow(mUid);
                break;
            case R.id.et_pri:
                isShowEmoj = false;
                privateChatEmoj.setImageResource(R.mipmap.smile);
                if (mContainer.isShown()) {
                    EmojiUtil.hideEmotionView(this, SystemUtils.isKeyBoardShow(this), mGiftContainer, mOutcontainer, mBottomCon, mContainer, mPrivateChatInput);
                } else if (mGiftContainer.isShown()) {
                    EmojiUtil.hideGiftView(this, SystemUtils.isKeyBoardShow(this), mGiftContainer, mOutcontainer, mBottomCon, mContainer, mPrivateChatInput);
                }
                break;
            default:
                break;
        }
    }

    private void setSendMsg() {
        mPrivateChatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                        @Override
                                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                            if(HnFastClickUtil.isFastClick()){
                                                                return true;
                                                            }
                                                            if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                                String content = mPrivateChatInput.getText().toString().trim();
                                                                mHnPrivateLetterBiz.requestToSendPrivateLetter(content, mUid, mChatRoomId);
                                                                return true;
                                                            }
                                                            return false;
                                                        }
                                                    }

        );

        mPrivateChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(mPrivateChatInput.getText().toString()))
                    mPrivateChatSend.setSelected(false);
                else mPrivateChatSend.setSelected(true);
            }
        });
    }


    @Override
    public int getContentViewId() {
        return R.layout.dialog_privatechat;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        //初始化数据
        initData();
        //初始化视图
        initView();
        //初始化适配器
        initAdapter();
        //设置监听
        setListener();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUid = bundle.getString(HnConstants.Intent.DATA);
            nick = bundle.getString(HnConstants.Intent.Name);
            mChatRoomId = bundle.getString(HnConstants.Intent.ChatRoomId);
        }
        ownUid = HnPrefUtils.getString(NetConstant.User.UID, "");
        mHnPrivateLetterBiz = new HnPrivateLetterBiz(this);
        mHnPrivateLetterBiz.setBaseRequestStateListener(this);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setOnReloadListener(this);
        EventBus.getDefault().register(this);
        setShowBack(true);
        setTitle(nick);
        if (mHnEmojiFragment == null) {
            mHnEmojiFragment = HnEmojiFragment.Instance();
            getSupportFragmentManager().beginTransaction().add(R.id.emoji_container, mHnEmojiFragment).commit();
        }

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new HnPrivateChatListAdapter(this, dataList, true);
            mPrivateChatListview.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        setSendMsg();
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
        mPrivateChatListview.setOnScrollListener(this);
        //输入监听
        mPrivateChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    mIvGift.setVisibility(View.VISIBLE);
//                    mPrivateChatSend.setVisibility(View.GONE);
//                } else {
//                    mPrivateChatSend.setVisibility(View.VISIBLE);
//                    mIvGift.setVisibility(View.GONE);
//                }
            }
        });
    }


    @Override
    public void getInitData() {
        mPage = 1;
        mHnPrivateLetterBiz.requestToPrivateLetterDetail(mChatRoomId, mDialogId);
    }

    @Override
    public void onReload(View v) {
        getInitData();
    }

    @Override
    public void requesting() {
        showDoing(getResources().getString(R.string.loading), null);
    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null) return;
        done();
        if ("Private_Msg_Detail_List".equals(type)) {//私信详情列表
            setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
            closeRefresh(mSwiperefresh);
            PrivateLetterDetailModel model = (PrivateLetterDetailModel) obj;
            //TODO
            if (model != null && model.getD() != null) {
                updateUI(model.getD());
            }
        } else if ("add_follow".equals(type)) {//点击关注
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_follow_succeed));
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Follow, mUid));
            EventBus.getDefault().post(new HnFollowEvent(mUid, true));
            if (mRlFollow != null && mRlFollow.getVisibility() == View.VISIBLE) {
                mRlFollow.setVisibility(View.GONE);
            }
        } else if ("send_msg".equals(type)) {//发送消息
            HnSendPriMsgModel model = (HnSendPriMsgModel) obj;
            if (model != null) {
                mDialogId = model.getD().getDialog().getDialog_id();
                PrivateChatBean bean = new PrivateChatBean();
                bean.setUid(model.getD().getDialog().getFrom_user().getUser_id());
                bean.setAvator(model.getD().getDialog().getFrom_user().getUser_avatar());
                bean.setNick(model.getD().getDialog().getFrom_user().getUser_nickname());
                bean.setShowMsgTyoe("ownermsg");
                bean.setMsgContent(response);
                if (!TextUtils.isEmpty(model.getD().getDialog().getTime())) {
                    bean.setAdd_time(Long.valueOf(model.getD().getDialog().getTime()) * 1000 + "");
                }
                dataList.add(bean);
                mAdapter.notifyDataSetChanged();
                mPrivateChatListview.setSelection(dataList.size() - 1);
            }
            mPrivateChatInput.setText("");
        }

    }


    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null) return;
        done();
        if ("Private_Msg_Detail_List".equals(type)) {//私信详情列表
            closeRefresh(mSwiperefresh);
            if (mPage == 1) {
                if (REQUEST_NET_ERROR == code) {
                    setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                } else {
                    setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }

        } else if ("add_follow".equals(type)) {//点击关注
            HnToastUtils.showToastShort(msg);
        } else if ("send_msg".equals(type)) {//发送消息
            if (code == 10024) {
                CommDialog.newInstance(this)
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
            } else {
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
                mRlFollow.setVisibility(View.VISIBLE);
            } else {
                mRlFollow.setVisibility(View.GONE);
            }

            if (TextUtils.equals(result.getUser().isIs_free(), "N") && !HnPrefUtils.getBoolean("tips", false)) {
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
        ownUid = HnPrefUtils.getString(NetConstant.User.UID, "0");

        //自己的昵称
        myNick = HnApplication.getmUserBean().getUser_nickname();
        //自己的头像
        myAvator = HnApplication.getmUserBean().getUser_avatar();

        List<UserDialogBean> msgList = result.getUser_dialog();
        if (msgList != null && msgList.size() > 0) {
            mDialogId = msgList.get(msgList.size() - 1).getDialog_id();
            List<PrivateChatBean> list = new ArrayList<>();
            for (int i = 0; i < msgList.size(); i++) {
                PrivateChatBean data = new PrivateChatBean();
                UserDialogBean bean = msgList.get(i);
                UserDialogBean.FromUserBean from_user = bean.getFrom_user();
                data.setNick(from_user.getUser_nickname());
                data.setUid(from_user.getUser_id());
                data.setAvator(from_user.getUser_avatar());
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
                mPrivateChatListview.setSelection(dataList.size() - 1);
            } else {
                dataList.addAll(0, list);
                mAdapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    mPrivateChatListview.setSelection(dataList.size() - list.size() - 1);
                }
            }
        }


    }

    @Subscribe
    public void onVipEvent(HnLiveEvent event) {
        if (TextUtils.equals(event.getType(), HnLiveConstants.EventBus.Buy_VIP_Success)) {
            mLLInput.setVisibility(View.VISIBLE);
            mRlPay.setVisibility(View.GONE);
        }
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
                        mPrivateChatListview.setSelection(dataList.size() - 1);
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
                    mRlFollow.setVisibility(View.GONE);
                    isFolllow = "1";
                } else {
                    mRlFollow.setVisibility(View.VISIBLE);
                    isFolllow = "0";
                }
            }
        }
    }

    /**
     * 接收到关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveClickHeaderIconvent(EventBusBean event) {
        if (event != null) {
            if (HnLiveConstants.EventBus.User_Info.equals(event.getType())) {
                PrivateChatBean bean = (PrivateChatBean) event.getObj();
//                HnUserDetailDialog dialog = HnUserDetailDialog.newInstance(0, bean.getUid(), ownUid, 0);
//                dialog.setActvity(this);
//                dialog.show(getSupportFragmentManager(), "HnUserDetailDialog");
                HnUserHomeActivity.luncher(HnPrivateChatActivity.this, bean.getUid());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHnPrivateLetterBiz.requestToExitMsgDetail(mChatRoomId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Clean_Unread, mUid));
        EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Update_Msg, mUid));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);


    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean enable = false;
        if (mPrivateChatListview != null && mPrivateChatListview.getChildCount() > 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = mPrivateChatListview.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = mPrivateChatListview.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        mSwiperefresh.setEnabled(enable);
    }

    @Subscribe
    public void emojiDelete(EmojiDeleteEvent deleteEvent) {
        String text = mPrivateChatInput.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                mPrivateChatInput.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            mPrivateChatInput.getText().delete(index, text.length());
            displayTextView();
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        mPrivateChatInput.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        displayTextView();
    }

    @Subscribe
    public void emojiClick(EmojiClickEvent event) {

        Emoji emoji = event.getEmoji();
        if (emoji != null) {
            int index = mPrivateChatInput.getSelectionEnd();
            Editable editable = mPrivateChatInput.getEditableText();
            editable.append(emoji.getContent());

        }
        displayTextView();
    }

    private void displayTextView() {
        try {
            EmojiUtil.handlerEmojiText(mPrivateChatInput, mPrivateChatInput.getText().toString(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (mContainer.getVisibility() == View.VISIBLE && mGiftContainer.getVisibility() == View.GONE) {
            EmojiUtil.hideEmotionView(this, false, mGiftContainer, mOutcontainer, mBottomCon, mContainer, mPrivateChatInput);
        } else if (mGiftContainer.getVisibility() == View.VISIBLE && mContainer.getVisibility() == View.GONE) {
            EmojiUtil.hideGiftView(this, false, mGiftContainer, mOutcontainer, mBottomCon, mContainer, mPrivateChatInput);
        } else {
            super.onBackPressed();
        }
    }

}
