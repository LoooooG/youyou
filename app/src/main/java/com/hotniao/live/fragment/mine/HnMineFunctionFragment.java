package com.hotniao.live.fragment.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.model.HnLoginBean;
import com.hn.library.model.HnLoginModel;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnAnchorFunctionActivity;
import com.hotniao.live.activity.HnAnchorRelatedActivity;
import com.hotniao.live.activity.HnMyVipMemberActivity;
import com.hotniao.live.activity.HnPlatformListActivity;
import com.hotniao.live.activity.HnSettingActivity;
import com.hotniao.live.activity.HnShareGetMoneyActivity;
import com.hotniao.live.activity.HnUserClosedRankActivity;
import com.hotniao.live.activity.HnVideoAuthApplyActivity;
import com.hotniao.live.activity.HnWebActivity;
import com.hotniao.live.activity.account.HnMyAccountActivity;
import com.hotniao.live.base.BaseScollFragment;
import com.hotniao.live.eventbus.HnSignEvent;
import com.hotniao.live.fragment.HnMineFragment;
import com.hotniao.livelibrary.control.HnUserControl;
import com.reslibrarytwo.HnSkinTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

public class HnMineFunctionFragment extends BaseScollFragment {

    public static final String TAG = "HnMineFunctionFragment";
    @BindView(R.id.mTvAnchorRelated)
    TextView mTvAnchorRelated;
    @BindView(R.id.mTvPlatFormList)
    TextView mTvPlatFormList;
    @BindView(R.id.mTvMember)
    TextView mTvMember;
    @BindView(R.id.tv_my_account)
    HnSkinTextView mTvMyAccount;
    @BindView(R.id.rl_my_account)
    RelativeLayout mRlMyAccount;
    @BindView(R.id.mTvInvite)
    TextView mTvInvite;
    @BindView(R.id.mRlInVite)
    RelativeLayout mRlInVite;
    @BindView(R.id.mTvSign)
    TextView mTvSign;
    @BindView(R.id.mIvSign)
    ImageView mIvSign;
    @BindView(R.id.mTvSignState)
    TextView mTvSignState;
    @BindView(R.id.mRlSign)
    RelativeLayout mRlSign;
    @BindView(R.id.tv_help)
    HnSkinTextView mTvHelp;
    @BindView(R.id.rl_help)
    RelativeLayout mRlHelp;
    @BindView(R.id.rl_set)
    RelativeLayout rlSet;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R.id.tv_anchor)
    TextView tvAnchor;
    @BindView(R.id.llClose)
    LinearLayout llClose;
    Unbinder unbinder;

    private HnLoginBean mUserInfo;

    private Bundle bundle;

    public static HnMineFunctionFragment getInstance() {
        HnMineFunctionFragment fragment = new HnMineFunctionFragment();
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine_function;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        updateUi();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfo = HnApplication.getmUserBean();
        updateUi();
    }

    @Override
    public View getScrollableView() {
        return mScrollView;
    }

    @Override
    public void pullToRefresh() {
        HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
            @Override
            public void onSuccess(String uid, HnLoginModel model, String response) {
                if (mActivity == null) return;
                mUserInfo = HnApplication.getmUserBean();
                updateUi();
                refreshComplete();
            }

            @Override
            public void onError(int errCode, String msg) {
                if (mActivity == null) return;
                refreshComplete();
            }
        });

    }

    @Override
    public void refreshComplete() {
        if (this.getParentFragment() instanceof HnMineFragment) {
            ((HnMineFragment) (this.getParentFragment())).refreshComplete();
        }
    }


    @OnClick({R.id.mTvAnchorRelated, R.id.mTvPlatFormList, R.id.mTvMember, R.id.rl_my_account, R.id.mRlInVite, R.id.mRlSign, R.id.rl_help, R.id.rl_set, R.id.rl_anchor,R.id.llClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvAnchorRelated:
                mActivity.openActivity(HnAnchorRelatedActivity.class);
                break;
            case R.id.mTvPlatFormList:
                mActivity.openActivity(HnPlatformListActivity.class);
                break;
            case R.id.mTvMember:
                mActivity.openActivity(HnMyVipMemberActivity.class);
                break;
            case R.id.rl_my_account:
                if (mUserInfo == null) return;
                HnMyAccountActivity.luncher(mActivity, mUserInfo.getUser_coin(), TextUtils.equals("Y", mUserInfo.getUser_is_anchor()));
                break;
            case R.id.mRlInVite:
                mActivity.openActivity(HnShareGetMoneyActivity.class);
                break;
            case R.id.mRlSign:
                HnWebActivity.luncher(mActivity, getString(R.string.my_sign_in), HnUrl.USER_SIGNIN_DETAIL, HnWebActivity.Sign);
                break;
            case R.id.rl_help:
                HnWebActivity.luncher(mActivity, getString(R.string.help_and_feekback), HnUrl.USER_HELP_HOTQUESTION, HnWebActivity.Help);
                break;
            case R.id.rl_set:
                if (mUserInfo == null) return;
                bundle = new Bundle();
                bundle.putString(HnConstants.Intent.DATA, mUserInfo.getUser_id());
                mActivity.openActivity(HnSettingActivity.class, bundle);
                break;
            case R.id.rl_anchor:
                checkAnchorStatus();
                break;

            case R.id.llClose:
                HnUserClosedRankActivity.luncher(mActivity, mUserInfo.getUser_id(), mUserInfo.getUser_nickname());
                break;
        }
    }

    private void checkAnchorStatus() {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.NoApply);
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
//            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.Authing);
            mActivity.openActivity(HnAnchorFunctionActivity.class);
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
//            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.AuthNoPass);
            mActivity.openActivity(HnAnchorFunctionActivity.class);
        } else if ("3".equals(mVideoStatue) || "6".equals(mVideoStatue)) {
            mActivity.openActivity(HnAnchorFunctionActivity.class);
        }
    }

    /**
     * 更新界面数据
     */
    private void updateUi() {
        if (mActivity == null || mUserInfo == null) return;
        try {
            if ("Y".equals(mUserInfo.getUser_is_anchor())) {
                tvAnchor.setText("主播相关");
                llClose.setVisibility(View.GONE);
            } else {
                tvAnchor.setText("申请主播");
                llClose.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void signEvent(HnSignEvent event) {
        if (mTvSignState == null) return;
        if (event.isSign()) {
            mTvSignState.setText(R.string.signed);
            mIvSign.setVisibility(View.GONE);
        } else {
            mTvSignState.setText(R.string.no_sign);
            mIvSign.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
