package com.hotniao.video.fragment.modify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.HnMainActivity;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnHomeSearchActivity;
import com.hotniao.video.activity.HnVideoAuthApplyActivity;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.home.HnHomeBiz;
import com.hotniao.video.biz.home.HnHomeCate;
import com.hotniao.video.dialog.HnChatTypeDialog;
import com.hotniao.video.eventbus.HnSelectLiveCateEvent;
import com.hotniao.video.fragment.HnHomeChatFragment;
import com.hotniao.video.model.HnChatTypeModel;
import com.hotniao.video.widget.scollorlayout.ScrollableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: LooooG
 * created on: 2020/1/8 12:00
 * description: 活动整合页面
 */
public class HnActivityGroupFragment extends BaseFragment implements ViewPager.OnPageChangeListener,BaseRequestStateListener {

    @BindView(R.id.home_viewpager)
    ViewPager mHomeViewpager;
    @BindView(R.id.mSlidTab)
    SlidingTabLayout mSlidTab;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private List<BaseScollFragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    private HnHomeBiz mHnHomeBiz;

    @Override
    public int getContentViewId() {
        return R.layout.activity_group;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),rlTitle);
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);
        if (HnHomeCate.mChatData.size() < 1) {
            HnHomeCate.getChatTypeData();
            HnHomeCate.setOnCateListener(new HnHomeCate.OnCateListener() {
                @Override
                public void onSuccess() {
                    HnHomeCate.removeListener();
                    setTab();
                    mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(getCurrentPage()));
                }

                @Override
                public void onError(int errCode, String msg) {
                    HnHomeCate.removeListener();
                    setTab();
                    mScrollableLayout.getHelper().setCurrentScrollableContainer(mFragments.get(getCurrentPage()));
                }
            });
        } else {
            setTab();
        }
//        com.hn.library.utils.HnUiUtils.setBannerHeight(mActivity, mBanner);
        setListener();
    }

    private void setListener(){
        //刷新监听
        mRefresh.setEnabledNextPtrAtOnce(true);
        mRefresh.setKeepHeaderWhenRefresh(true);
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mHnHomeBiz.getBanner(1);
                if (mFragments.size() > mHomeViewpager.getCurrentItem()) {
                    mFragments.get(mHomeViewpager.getCurrentItem()).pullToRefresh();
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
        mHomeViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void refreshComplete() {
        if (mRefresh != null) {
            mRefresh.refreshComplete();
        }
    }

    private void setTab() {
        if (mActivity == null) return;
        // tab集合
        mTitles.add("签到");
        mTitles.add("分享");
        // fragment集合
        mFragments = new ArrayList<>();
        mFragments.add(new HnSignFragment());
        mFragments.add(new HnShareGetMoneyFragment());
        // viewpager
        mHomeViewpager.setOffscreenPageLimit(mTitles.size());
        mHomeViewpager.setAdapter(new PagerAdapter(getChildFragmentManager(), mFragments, mTitles));
        mSlidTab.setViewPager(mHomeViewpager);
        mHomeViewpager.setCurrentItem(getCurrentPage());
    }

    //进入首页默认显示推荐页
    private int getCurrentPage(){
        return 0;
    }

    @Override
    protected void initData() {
        mHnHomeBiz.getBanner(1);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(HnSelectLiveCateEvent event) {
        if (mActivity == null || HnSelectLiveCateEvent.SWITCH_CHAT_TYPE != event.getType()) return;
        if (event.getPosition() < mTitles.size())
            mHomeViewpager.setCurrentItem(event.getPosition()+1);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {

    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnHomeBiz.Banner.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragments != null && mFragments.size() > 0) {
            for (int i = 0; i < mFragments.size(); i++) {
                if (mFragments.get(i) instanceof HnShareGetMoneyFragment) {
                    mFragments.get(i).onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private List<BaseScollFragment> mFragments;

        private List<String> mTitles;

        public PagerAdapter(FragmentManager fm, List<BaseScollFragment> mFragments, List<String> mTitles) {
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
            return mTitles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragments.get(position);
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
