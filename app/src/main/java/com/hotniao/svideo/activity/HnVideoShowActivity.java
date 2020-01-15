package com.hotniao.svideo.activity;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.NetConstant;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.PermissionHelper;
import com.hotniao.svideo.R;
import com.hotniao.svideo.adapter.HnScrollViewPagerAdapter;
import com.hotniao.svideo.base.BaseScollFragment;
import com.hotniao.svideo.biz.home.HnHomeBiz;
import com.hotniao.svideo.fragment.video.HnPersonalVideoFragment;
import com.hotniao.svideo.fragment.video.HnPrivateVideoFragment;
import com.hotniao.svideo.model.HnBannerModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.svideo.widget.scollorlayout.ScrollableLayout;
import com.videolibrary.activity.HnChooseVideoActivity;
import com.videolibrary.activity.TCVideoRecordActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视屏秀
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoShowActivity extends BaseActivity implements BaseRequestStateListener {
    @BindView(R.id.mBanner)
    ConvenientBanner mBanner;
    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;

    private HnHomeBiz mHnHomeBiz;


    /**
     * 广告数据源
     */
    private List<String> mImgUrl = new ArrayList<>();


    private String[] mTitles = {HnUiUtils.getString(R.string.personal_show), HnUiUtils.getString(R.string.private_show)};
    private List<BaseScollFragment> mFragments = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_show;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.video_show);
        mSubtitle.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(R.drawable.xiaoship_drak);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mSubtitle.setCompoundDrawables(null, null, drawable, null);
        mSubtitle.setCompoundDrawablePadding(4);
        mSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR, false)) {
                    HnAuthStateActivity.luncher(HnVideoShowActivity.this);
                }else {
                    HnChooseVideoActivity.luncher(HnVideoShowActivity.this, HnChooseVideoActivity.PublishVideo);
                }
            }
        });


        mHnHomeBiz = new HnHomeBiz(this);
        mHnHomeBiz.setBaseRequestStateListener(this);

        setListener();

        mFragments.addAll(getFragments());
        HnScrollViewPagerAdapter adapter = new HnScrollViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.setAdapter(adapter);
        mTab.setViewPager(mViewPager);
        mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(0));
        mViewPager.setCurrentItem(0);


        com.hn.library.utils.HnUiUtils.setBannerHeight(this, mBanner);
    }

    @Override
    public void getInitData() {
        mHnHomeBiz.getBanner(2);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        //刷新监听
        mRefresh.setEnabledNextPtrAtOnce(true);
        mRefresh.setKeepHeaderWhenRefresh(true);
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mHnHomeBiz.getBanner(2);
                if (mFragments.size() > mViewPager.getCurrentItem()) {
                    mFragments.get(mViewPager.getCurrentItem()).pullToRefresh();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mScrollableLayout.isCanPullToRefresh()) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }
                return false;
            }
        });
        mScrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {

            }
        });

        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 添加fragment集合
     *
     * @return
     */
    private List<BaseScollFragment> getFragments() {
        FragmentManager manager = getSupportFragmentManager();
        List<BaseScollFragment> list = new ArrayList<>();

        HnPersonalVideoFragment chatFragment
                = (HnPersonalVideoFragment) manager.findFragmentByTag(HnPersonalVideoFragment.TAG);
        if (chatFragment == null) {
            chatFragment = HnPersonalVideoFragment.getInstance();
        }
        HnPrivateVideoFragment liveFragment
                = (HnPrivateVideoFragment) manager.findFragmentByTag(HnPrivateVideoFragment.TAG);
        if (liveFragment == null) {
            liveFragment = HnPrivateVideoFragment.getInstance();
        }


        Collections.addAll(list, chatFragment, liveFragment);

        return list;
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (isFinishing()) return;
        if (HnHomeBiz.Banner.equals(type)) {
            HnBannerModel model = (HnBannerModel) obj;
            if (model == null || model.getD().getCarousel() == null || mBanner == null || mHnHomeBiz == null)
                return;
            mHnHomeBiz.initViewpager(mBanner, mImgUrl, model.getD().getCarousel());

        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnHomeBiz.Banner.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    public void refreshComplete() {
        if (mRefresh != null) {
            mRefresh.refreshComplete();
        }
    }


}
