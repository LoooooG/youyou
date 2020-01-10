package com.hotniao.video.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hn.library.base.BaseFragment;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hotniao.video.R;
import com.hotniao.video.biz.home.HnHomeCate;
import com.hotniao.video.dialog.HnAllLiveCateDialog;
import com.hotniao.video.eventbus.HnSelectLiveCateEvent;
import com.hotniao.video.fragment.homeLive.HnHomeCateLiveFragment;
import com.hotniao.video.fragment.homeLive.HnHomeLiveHotFragment;
import com.hotniao.video.model.HnHomeLiveCateModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：首页模块
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPlayBackFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.home_viewpager)
    ViewPager mHomeViewpager;
    @BindView(R.id.rl_title)
    LinearLayout mRlTitle;

    @BindView(R.id.mSlidTab)
    SlidingTabLayout mSlidTab;

    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private List<BaseFragment> mFragments;
    private List<HnHomeLiveCateModel.DBean.LiveCategoryBean> mTitles = new ArrayList<>();

    public static HnPlayBackFragment getInstance() {
        HnPlayBackFragment fragment = new HnPlayBackFragment();
        return fragment;
    }


    @Override
    public int getContentViewId() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return R.layout.home_live_child_fragment;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (HnHomeCate.mCateData.size() < 1) {
            HnHomeCate.getCateData();
            HnHomeCate.setOnCateListener(new HnHomeCate.OnCateListener() {
                @Override
                public void onSuccess() {
                    HnHomeCate.removeListener();
                    setTab();
                }

                @Override
                public void onError(int errCode, String msg) {
                    HnHomeCate.removeListener();
                    setTab();
                }
            });
        } else {
            setTab();
        }


    }


    private void setTab() {
        if (mActivity == null) return;
        //fragment集合
        mFragments = new ArrayList<>();

        HnHomeLiveCateModel.DBean.LiveCategoryBean hot = new HnHomeLiveCateModel.DBean.LiveCategoryBean();
        //添加热门
        hot.setAnchor_category_id("0");
        hot.setAnchor_category_name("热门");
        mTitles.add(hot);

        mTitles.addAll(HnHomeCate.mCateData);

        for (int i = 0; i < mTitles.size(); i++) {
            mFragments.add(HnPlayBackListFragment.getInstance(mTitles.get(i).getAnchor_category_id()));
        }


        mHomeViewpager.setOffscreenPageLimit(mTitles.size());
        mHomeViewpager.setAdapter(new PagerAdapter(getChildFragmentManager(), mFragments, mTitles));
        refreshUI();

        mSlidTab.setViewPager(mHomeViewpager);
    }

    @Override
    protected void initEvent() {
        mSlidTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mHomeViewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mHomeViewpager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void initData() {

    }

    @OnClick({R.id.mIvTab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvTab:
                HnAllLiveCateDialog.newInstance(mTitles).setClickListen(new HnAllLiveCateDialog.SelDialogListener() {
                    @Override
                    public void sureClick() {

                    }
                }).show(mActivity.getFragmentManager(), "cate");
                break;
        }
    }

    /**
     * 刷新UI界面
     */
    public void refreshUI() {
        EventBus.getDefault().post(new HnSelectLiveCateEvent(HnSelectLiveCateEvent.REFRESH_UI_LIVE, "", 0));
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private List<BaseFragment> mFragments;
        private List<HnHomeLiveCateModel.DBean.LiveCategoryBean> mTitles;

        public PagerAdapter(FragmentManager fm, List<BaseFragment> mFragments, List<HnHomeLiveCateModel.DBean.LiveCategoryBean> mTitles) {
            super(fm);
            this.mFragments = mFragments;
            this.mTitles = mTitles;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position).getAnchor_category_name();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragments.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", mTitles.get(position).getAnchor_category_id());
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    /**
     * 点击分类弹窗切换Tab
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(HnSelectLiveCateEvent event) {
        if (mActivity == null || HnSelectLiveCateEvent.SWITCH_CATE_LIVE != event.getType()) return;
        if (event.getPosition() < mTitles.size())
            mHomeViewpager.setCurrentItem(event.getPosition());

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
