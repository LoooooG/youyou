package com.hotniao.live.activity.account;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.tab.CommonTabLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.entity.TabEntity;
import com.hn.library.tab.listener.CustomTabEntity;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.bindPhone.HnFirstBindPhoneActivity;
import com.hotniao.live.activity.withdraw.HnWithDrawWriteActivity;
import com.hotniao.live.adapter.MyTabPagerAdapter;
import com.hn.library.global.HnConstants;
import com.hn.library.view.CommDialog;
import com.hotniao.live.fragment.billRecord.HnBillInviteFragment;
import com.hotniao.live.fragment.billRecord.HnBillLiveBackEarnFragment;
import com.hotniao.live.fragment.billRecord.HnBillReceiveFragment;
import com.hotniao.live.fragment.billRecord.HnBillStartLiveFragment;
import com.hotniao.live.fragment.billRecord.HnBillVideoChatEarnFragment;
import com.hotniao.live.fragment.billRecord.HnBillVideoShowEarnFragment;
import com.hotniao.live.model.CheckStatueModel;
import com.hotniao.live.utils.HnUiUtils;
import com.tencent.openqq.protocol.imsdk.msg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：收益详情
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserBillEarningActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnTabSelectListener {


    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_bill)
    ViewPager mVpBill;
    @BindView(R.id.mTvCion)
    TextView mTvCion;
    @BindView(R.id.mTvBalance)
    TextView mTvBalance;

    //标识符  用于默认显示第几个tab
    private int pos = 0;
    //tab  标题
    private List<String> tabTitles;
    //集合    存放fargment
    private List<Fragment> mFragments;
    private int canWithdraw = 2;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_bill_earnings;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(false);
        setShowTitleBar(false);
        setTitle(R.string.account_detail);
        mTvBalance.setText(HnUiUtils.getString(R.string.balance) + "(" + HnApplication.getmConfig().getDot() + ")");
        //初始化数据
        initData();
        //初始化视图
        initView();


        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/I770-Sans.ttf");
        mTvCion.setTypeface(typeFace);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvCion.setText(HnUtils.setTwoPoint(HnApplication.getmUserBean().getUser_dot()));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt(HnConstants.Intent.DATA, 0);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();

        tabTitles.add(HnUiUtils.getString(R.string.tab_gift_recevier));
        tabTitles.add(HnUiUtils.getString(R.string.tab_start_live));
//        tabTitles.add(HnUiUtils.getString(R.string.tab_invite_people));
        tabTitles.add(HnUiUtils.getString(R.string.video_chat_net));
        tabTitles.add(HnUiUtils.getString(R.string.live_play_back));
//        tabTitles.add(HnUiUtils.getString(R.string.small_video));
        //收礼
        mFragments.add(HnBillReceiveFragment.newInstance());
        //开播
        mFragments.add(HnBillStartLiveFragment.newInstance(0));
        //邀请
//        mFragments.add(HnBillInviteFragment.newInstance(0));
        //视屏聊天
        mFragments.add(HnBillVideoChatEarnFragment.newInstance(0));
        //回放
        mFragments.add(HnBillLiveBackEarnFragment.newInstance(0));
//        mFragments.add(HnBillVideoShowEarnFragment.newInstance(0));

        mVpBill.setOffscreenPageLimit(tabTitles.size());
        mVpBill.setAdapter(new MyTabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles));
        //设置默认选择tab
        mTab.setViewPager(mVpBill);
        mTab.setCurrentTab(pos);
        mVpBill.setCurrentItem(pos);
        //事件监听
        mVpBill.addOnPageChangeListener(this);
        mTab.setOnTabSelectListener(this);
    }

    @Override
    public void getInitData() {
        check();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTab.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelect(int position) {
        mVpBill.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @OnClick({R.id.mIvBack, R.id.mTvWithdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                finish();
                break;
            case R.id.mTvWithdraw:
                if (TextUtils.isEmpty(HnApplication.getmUserBean().getUser_phone())) {
                    CommDialog.newInstance(HnUserBillEarningActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                        @Override
                        public void leftClick() {

                        }

                        @Override
                        public void rightClick() {
                            openActivity(HnFirstBindPhoneActivity.class);
                        }
                    }).setTitle(getString(R.string.bind_phone)).setContent(getString(R.string.now_goto_bind_phone)).show();
                    return;
                }
                if (0 == canWithdraw) {
                    HnToastUtils.showToastShort("每次只可提现一次");
                    return;
                }
                if (1 == canWithdraw) {
                    HnToastUtils.showToastShort("正在审核中");
                    return;
                }
                openActivity(HnWithDrawWriteActivity.class);
                break;
        }
    }

    private void check() {
        HnHttpUtils.postRequest(HnUrl.CHECK_STATUS, null, HnUrl.CHECK_STATUS, new HnResponseHandler<CheckStatueModel>(CheckStatueModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {
                    canWithdraw = model.getD().getStatus();
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }
}
