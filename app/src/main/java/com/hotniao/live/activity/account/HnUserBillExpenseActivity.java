package com.hotniao.live.activity.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnConstants;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hotniao.live.R;
import com.hotniao.live.adapter.MyTabPagerAdapter;
import com.hotniao.live.fragment.billRecord.HnBillBuyVipFragment;
import com.hotniao.live.fragment.billRecord.HnBillExpensesRecordFragment;
import com.hotniao.live.fragment.billRecord.HnBillLiveBackExpFragment;
import com.hotniao.live.fragment.billRecord.HnBillLookLiveFragment;
import com.hotniao.live.fragment.billRecord.HnBillSendFragment;
import com.hotniao.live.fragment.billRecord.HnBillVideoChatExpFragment;
import com.hotniao.live.fragment.billRecord.HnBillVideoShowExpFragment;
import com.hotniao.live.utils.HnUiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：消费记录
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserBillExpenseActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnTabSelectListener {


    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_bill)
    ViewPager mVpBill;

    //标识符  用于默认显示第几个tab
    private int pos = 0;
    //tab  标题
    private List<String> tabTitles;
    //集合    存放fargment
    private List<Fragment> mFragments;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_bill;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        //初始化数据
        initData();
        //初始化视图
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt(HnConstants.Intent.DATA);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setShowBack(true);
        setTitle(R.string.expense_record);
        tabTitles = new ArrayList<>();
        mFragments = new ArrayList<>();

        tabTitles.add(HnUiUtils.getString(R.string.tab_send_gift));
        tabTitles.add(HnUiUtils.getString(R.string.tab_look_live));
        tabTitles.add(HnUiUtils.getString(R.string.tab_buy_vip));
        tabTitles.add(HnUiUtils.getString(R.string.video_chat_net));
        tabTitles.add(HnUiUtils.getString(R.string.live_play_back));
//        tabTitles.add(HnUiUtils.getString(R.string.small_video));
        tabTitles.add(HnUiUtils.getString(R.string.chat_record));

        //送礼
        mFragments.add(HnBillSendFragment.newInstance());
        //看播
        mFragments.add(HnBillLookLiveFragment.newInstance(1));
        //购买VIP
        mFragments.add(HnBillBuyVipFragment.newInstance());
        mFragments.add(HnBillVideoChatExpFragment.newInstance());
        mFragments.add(HnBillLiveBackExpFragment.newInstance());
//        mFragments.add(HnBillVideoShowExpFragment.newInstance());
        mFragments.add(HnBillExpensesRecordFragment.newInstance());



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
}
