package com.hotniao.video.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.global.HnUrl;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.HnMainActivity;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnAnchorFunctionActivity;
import com.hotniao.video.activity.HnHomeSearchActivity;
import com.hotniao.video.activity.HnInviteFriendActivity;
import com.hotniao.video.activity.HnNearActivity;
import com.hotniao.video.activity.HnVideoAuthApplyActivity;
import com.hotniao.video.activity.HnVideoShowActivity;
import com.hotniao.video.activity.HnWebActivity;
import com.hotniao.video.adapter.HnScrollViewPagerAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.home.HnHomeBiz;
import com.hotniao.video.fragment.homeHot.HnHomeHotChatFragment;
import com.hotniao.video.fragment.homeHot.HnHomeHotVideoFragment;
import com.hotniao.video.model.HnBannerModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.video.widget.scollorlayout.ScrollableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeChildFragment extends BaseFragment implements BaseRequestStateListener {
    @BindView(R.id.convenientBanner)
    ConvenientBanner mBanner;
    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.mViewHead)
    View mViewHead;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.mRlTop)
    RelativeLayout mRlTop;

    private HnHomeBiz mHnHomeBiz;

    private String[] mTitles = {HnUiUtils.getString(R.string.main_chat),  HnUiUtils.getString(R.string.home_hot_video)};
    private List<BaseScollFragment> mFragments = new ArrayList<>();

    private DayNightHelper mDayNightHelper;
    /**
     * 广告数据源
     */
    private List<String> mImgUrl = new ArrayList<>();


    public static HnHomeChildFragment getInstance() {
        HnHomeChildFragment fragment = new HnHomeChildFragment();
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home_child;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),mRlTop);

        mFragments.addAll(getFragments());
        HnScrollViewPagerAdapter adapter = new HnScrollViewPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.setAdapter(adapter);
        mTab.setViewPager(mViewPager);
        mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(0));
        mViewPager.setCurrentItem(0);
        mDayNightHelper = new DayNightHelper();
        refreshUI();
        setListener();
        com.hn.library.utils.HnUiUtils.setBannerHeight(mActivity, mBanner);
    }

    @Override
    protected void initData() {
        mHnHomeBiz.getBanner(1);
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
                mHnHomeBiz.getBanner(1);
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
        FragmentManager manager = getChildFragmentManager();
        List<BaseScollFragment> list = new ArrayList<>();

//        HnHomeHotChatFragment chatFragment
//                = (HnHomeHotChatFragment) manager.findFragmentByTag(HnHomeHotChatFragment.TAG);
//        if (chatFragment == null) {
//            chatFragment = HnHomeHotChatFragment.getInstance();
//        }
//        HnHomeHotLiveFragment liveFragment
//                = (HnHomeHotLiveFragment) manager.findFragmentByTag(HnHomeHotLiveFragment.TAG);
//        if (liveFragment == null) {
//            liveFragment = HnHomeHotLiveFragment.getInstance();
//        }

        HnHomeHotVideoFragment videoFragment
                = (HnHomeHotVideoFragment) manager.findFragmentByTag(HnHomeHotVideoFragment.TAG);
        if (videoFragment == null) {
            videoFragment = HnHomeHotVideoFragment.getInstance();
        }
        list.add(videoFragment);
//        Collections.addAll(list, chatFragment,  videoFragment);

        return list;
    }


    @OnClick({R.id.mTvSignIn, R.id.mTvNear, R.id.mTvVideoShow, R.id.mTvInvite, R.id.iv_search,R.id.iv_shoot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvSignIn:
                HnWebActivity.luncher(mActivity, getString(R.string.my_sign_in), HnUrl.USER_SIGNIN_DETAIL, HnWebActivity.Sign);
                break;
            case R.id.mTvNear:
                mActivity.openActivity(HnNearActivity.class);
                break;
            case R.id.mTvVideoShow:
                mActivity.openActivity(HnVideoShowActivity.class);
                break;
            case R.id.mTvInvite:
                mActivity.openActivity(HnInviteFriendActivity.class);
                break;

            case R.id.iv_search:
                mActivity.openActivity(HnHomeSearchActivity.class);
                break;
            case R.id.iv_shoot:
                checkAnchorStatus();
                break;
        }
    }

    private void checkAnchorStatus() {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.NoApply);
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.Authing);
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.AuthNoPass);
        } else if ("3".equals(mVideoStatue) || "6".equals(mVideoStatue)) {
            ((HnMainActivity)getActivity()).openVideo();
        }
    }

    @Override
    public void requesting() {

    }


    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null) return;
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


    /**
     * 刷新UI界面
     */
    public void refreshUI() {
        //条目背景颜色
//        TypedValue item_color = new TypedValue();
//        mActivity.getTheme().resolveAttribute(R.attr.item_bg_color, item_color, true);
        //标题背景色
//        mRlTop.setBackgroundResource(item_color.resourceId);
        //更换图标
//        boolean isDay = mDayNightHelper.isDay();
//        rbHot.setBackgroundResource(isDay?R.drawable.select_line_indicator:R.drawable.select_line_indicator_dark);
//        rbNew.setBackgroundResource(isDay?R.drawable.select_line_indicator:R.drawable.select_line_indicator_dark);
//        mTab.setBackgroundResource(isDay ? R.drawable.select_line_indicator : R.drawable.select_line_indicator_dark);

//        Resources resources = getResources();
//        rbHot.setTextColor(isDay?resources.getColorStateList(R.color.select_text_color):resources.getColorStateList(R.color.select_text_color_dark));
//        rbNew.setTextColor(isDay?resources.getColorStateList(R.color.select_text_color):resources.getColorStateList(R.color.select_text_color_dark));

//        mIvSearch.setImageResource(isDay ? R.drawable.home_search : R.drawable.home_search_dark);

    }


}
