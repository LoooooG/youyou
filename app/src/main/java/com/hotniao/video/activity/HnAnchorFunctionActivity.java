package com.hotniao.video.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.model.HnLoginModel;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnMineChatVideoAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.chat.HnFastChatBiz;
import com.hotniao.video.biz.home.HnHomeCate;
import com.hotniao.video.fragment.HnMineFragment;
import com.hotniao.video.model.HnChatTypeModel;
import com.hotniao.video.model.HnFastVideoListModel;
import com.hotniao.video.model.HnMineChatStateInfoModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUserControl;
import com.tencent.openqq.protocol.imsdk.msg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我的视频聊天
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnAnchorFunctionActivity extends BaseActivity implements BaseRequestStateListener {
    public static final String TAG = "HnAnchorFunctionActivity";

    @BindView(R.id.recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;
    private HnMineChatVideoAdapter mAdapter;
    private List<HnFastVideoListModel.DBean.ItemsBean> mData = new ArrayList<>();

    private TextView mTvOpen;
    private TextView mTvCharge;
    private TextView mTvType;
    private TextView mTvStatue;
    private RelativeLayout mRlEmpty;
    private RelativeLayout mRlMoney;
    private RelativeLayout mRlType;

    private HnFastChatBiz mFastChatBiz;

    private HnMineChatStateInfoModel.DBean mDBean;
    private int mPage = 1;

    private String mVideoStatue;

    public static HnAnchorFunctionActivity getInstance() {
        HnAnchorFunctionActivity fragment = new HnAnchorFunctionActivity();
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.common_loading_layout;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setTitle("主播相关");
        setShowBack(true);
        mFastChatBiz = new HnFastChatBiz(this);
        mFastChatBiz.setBaseRequestStateListener(this);
        initAdapter();
        setLisenter();
    }

    @Override
    public void getInitData() {
        mPage = 1;
        mFastChatBiz.getChatVideoList(mPage);
        mFastChatBiz.getChatStateInfo();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);

        mAdapter = new HnMineChatVideoAdapter(mData);
        mRecycler.setAdapter(mAdapter);

        View viewHead = LayoutInflater.from(this).inflate(R.layout.head_mine_chat_video, null);
        mTvOpen = viewHead.findViewById(R.id.mTvOpen);
        mTvCharge = viewHead.findViewById(R.id.mTvCharge);
        mTvType = viewHead.findViewById(R.id.mTvType);
        mTvStatue = viewHead.findViewById(R.id.mTvStatue);
        mRlEmpty = viewHead.findViewById(R.id.mRlEmpty);
        mRlMoney = viewHead.findViewById(R.id.mRlMoney);
        mRlType = viewHead.findViewById(R.id.mRlType);

        HnHomeCate.getChatTypeData();
        HnHomeCate.setOnCateListener(new HnHomeCate.OnCateListener() {
            @Override
            public void onSuccess() {
                if (mTvType != null)
                    HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
                        @Override
                        public void onSuccess(String uid, HnLoginModel model, String response) {
                            mTvType.setText(HnHomeCate.getChatTypeName(model.getD().getAnchor_chat_category()));

                        }

                        @Override
                        public void onError(int errCode, String msg) {

                        }
                    });

            }

            @Override
            public void onError(int errCode, String msg) {
                HnLogUtils.d(TAG,"error");

            }
        });

        mTvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDBean == null || !mDBean.isIs_anchor()) {
                    setVideoAuthNoPassDialog(true,"开启约聊需要实名认证及视频认证,两个认证通过后才能开启哦");
                } else if ("0".equals(mVideoStatue) || "1".equals(mVideoStatue) || "2".equals(mVideoStatue)) {
                    setVideoAuthNoPassClick();
                } else {
                    if (!"0".equals(mDBean.getChat_status())) {//关闭后将收不到视频聊天邀请
                        CommDialog.newInstance(HnAnchorFunctionActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                            @Override
                            public void leftClick() {
                            }

                            @Override
                            public void rightClick() {
                                mFastChatBiz.openChatVideo(mDBean.getPrivate_price(), "0".equals(mDBean.getChat_status()) ? 1 : 3, HnPrefUtils.getString(NetConstant.User.ANCHOR_CHAT_CATEGORY, ""));
                            }
                        }).setTitle(HnUiUtils.getString(R.string.fast_chat_add)).setContent("关闭后将收不到视频聊天邀请").show();
                    } else {
                        mFastChatBiz.openChatVideo(mDBean.getPrivate_price(), "0".equals(mDBean.getChat_status()) ? 1 : 3, HnPrefUtils.getString(NetConstant.User.ANCHOR_CHAT_CATEGORY, ""));
                    }
                }
            }
        });

        viewHead.findViewById(R.id.mRlStatue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户视频认证状态：0未认证 1认证中 2认证未通过 3认证通过 4审核中 5审核不通过 6审核通过
                if (mDBean == null || !mDBean.isIs_anchor()) {
                    setVideoAuthNoPassDialog(true,"开启约聊需要实名认证通过后才能开启哦");
                    mTvStatue.setText("未认证");
                } else {
                    setStatue(true);
                }
            }
        });
        mAdapter.addHeaderView(viewHead);

        setStatue(false);
    }

    @Subscribe
    public void onEventChatChange(HnChatTypeModel.DBean.ChatCategoryBean bean) {
        mTvType.setText(bean.getChat_category_name());
        mFastChatBiz.openChatVideo_Chat(mDBean.getPrivate_price(), "0".equals(mDBean.getChat_status()) ? 3 : 1, bean.getChat_category_id());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(EventBusBean event) {
        if (event != null) {
            if (HnConstants.EventBus.RefreshVideoAuthStatue.equals(event.getType())) {
                setStatue(false);
            }
        }
    }

    /**
     * 设置状态
     * 用户视频认证状态：0未认证 1认证中 2认证未通过 3认证通过 4审核中 5审核不通过 6审核通过
     */
    private void setStatue(final boolean isClick) {
        if (mTvStatue == null) return;
        if (HnApplication.getmUserBean() == null) {
            HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
                @Override
                public void onSuccess(String uid, HnLoginModel model, String response) {
                    checkAnchorStatus(isClick);
                    if(model.getD().getAnchor_chat_category().equals("11")){
                        mTvType.setText("推荐");
                    }else if(model.getD().getAnchor_chat_category().equals("14")){
                        mTvType.setText("新人");
                    }else if(model.getD().getAnchor_chat_category().equals("15")){
                        mTvType.setText("校园");
                    }

                }

                @Override
                public void onError(int errCode, String msg) {
                    HnApplication.getmUserBean().setVideo_authentication("0");
                    checkAnchorStatus(isClick);
                    mTvType.setText(HnHomeCate.getChatTypeName(HnPrefUtils.getString(NetConstant.User.ANCHOR_CHAT_CATEGORY, "")));

                }
            });
        } else {
            checkAnchorStatus(isClick);
        }
    }

    private void checkAnchorStatus(boolean isClick) {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.NoApply);
            }
            mTvStatue.setText("未认证");
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.Authing);
            }
            mTvStatue.setText("认证中");
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthNoPass);
            }
            mTvStatue.setText("认证不通过");
        } else if ("3".equals(mVideoStatue) || "6".equals(mVideoStatue)){
            mTvStatue.setText("已认证");
            if(isClick){
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthPass);
            }
        }
    }

    /**
     * 未通过认证的提示
     */
    private void setVideoAuthNoPassClick() {
        if ("0".equals(mVideoStatue)) {
            setVideoAuthNoPassDialog(false,"开启约聊需要实名认证及视频认证,两个认证通过后才能开启哦");
        } else if ("1".equals(mVideoStatue)) {
            HnToastUtils.showCenterToast("认证审核中，审核通过后才可开启");
        } else if ("2".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.AuthNoPass);
        }
    }

    private void setVideoAuthNoPassDialog(final boolean isRealName,String text) {
        CommDialog.newInstance(this).setClickListen(new CommDialog.TwoSelDialog() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
//                HnAuthStateActivity.luncher(mActivity);
                if (isRealName) {
                    HnAuthStateActivity.luncher(HnAnchorFunctionActivity.this);
                } else {
                    HnVideoAuthApplyActivity.lunchor(HnAnchorFunctionActivity.this, HnVideoAuthApplyActivity.NoApply);
                }

            }
        }).setTitle(HnUiUtils.getString(R.string.main_chat)).setContent(text)
                .setRightText("去认证").show();

    }


    private void setLisenter() {
        mRefresh.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                mFastChatBiz.getChatVideoList(mPage);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        mFastChatBiz.getChatStateInfo();
        setStatue(false);
    }

    private void setEmpty() {
        if (mRlEmpty == null) return;
        if (mData == null || mData.size() < 1) {
            mRlEmpty.setVisibility(View.VISIBLE);
        } else {
            mRlEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (HnFastChatBiz.ChatVideoList.equals(type)) {
            closeRefresh(mRefresh);
            HnFastVideoListModel model = (HnFastVideoListModel) obj;
            if (model == null || model.getD() == null || model.getD().getItems() == null) {
                setEmpty();
            } else {
                if (mPage == 1)
                    mData.clear();
                mData.addAll(model.getD().getItems());
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
                setEmpty();
            }
            HnUiUtils.setRefreshModeNone(mRefresh, mPage, 20, mData.size());

        } else if (HnFastChatBiz.ChatVideoMineState.equals(type)) {
            HnMineChatStateInfoModel model = (HnMineChatStateInfoModel) obj;
            if (model == null || model.getD() == null) return;
            setChatState(model.getD());
        } else if (HnFastChatBiz.ChatVideoOperation.equals(type)) {
            mFastChatBiz.getChatStateInfo();
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnFastChatBiz.ChatVideoList.equals(type)) {
            closeRefresh(mRefresh);
            setEmpty();
            HnToastUtils.showToastShort(msg);
        } else if (HnFastChatBiz.ChatVideoMineState.equals(type) || HnFastChatBiz.ChatVideoOperation.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    public void setChatState(HnMineChatStateInfoModel.DBean mDBean) {
        this.mDBean = mDBean;
        if (HnApplication.getmUserBean() != null) {
            mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        } else {
            mVideoStatue = "0";
        }

        if ("0".equals(mVideoStatue) || "1".equals(mVideoStatue) || "2".equals(mVideoStatue)) {
            mDBean.setChat_status("0");
        }

        mTvOpen.setSelected("0".equals(mDBean.getChat_status()) ? false : true);
        mTvCharge.setText(String.format(HnUiUtils.getString(R.string.one_to_one_chat_video_pay_min), TextUtils.isEmpty(mDBean.getPrivate_price()) ? "0" : mDBean.getPrivate_price(), mDBean.getCoin_name()));

        if (mDBean.isIs_anchor() && "0".equals(mDBean.getChat_status()) ? false : true) {
            mRlType.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
