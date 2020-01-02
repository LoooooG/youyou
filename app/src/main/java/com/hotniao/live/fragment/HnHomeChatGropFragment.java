package com.hotniao.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnHomeSearchActivity;
import com.hotniao.live.activity.HnVideoAuthApplyActivity;
import com.hotniao.live.base.BaseScollFragment;
import com.hotniao.live.biz.home.HnHomeBiz;
import com.hotniao.live.biz.home.HnHomeCate;
import com.hotniao.live.dialog.HnChatTypeDialog;
import com.hotniao.live.eventbus.HnSelectLiveCateEvent;
import com.hotniao.live.model.HnBannerModel;
import com.hotniao.live.model.HnChatTypeModel;
import com.hotniao.live.widget.scollorlayout.ScrollableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * create by Mr.x
 * on 9/8/2018
 */
public class HnHomeChatGropFragment extends BaseFragment implements ViewPager.OnPageChangeListener,BaseRequestStateListener {

    @BindView(R.id.home_viewpager)
    ViewPager mHomeViewpager;
    @BindView(R.id.mSlidTab)
    SlidingTabLayout mSlidTab;
//    @BindView(R.id.convenientBanner)
//    ConvenientBanner mBanner;
    @BindView(R.id.mRefresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private List<BaseScollFragment> mFragments = new ArrayList<>();
    private List<HnChatTypeModel.DBean.ChatCategoryBean> mTitles = new ArrayList<>();

    private HnHomeBiz mHnHomeBiz;

    /**
     * 广告数据源
     */
    private List<String> mImgUrl = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.home_chat_group;
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
        //fragment集合
        mFragments = new ArrayList<>();
        HnChatTypeModel.DBean.ChatCategoryBean chatBean = new HnChatTypeModel.DBean.ChatCategoryBean();
        chatBean.setChat_category_id("-2");
        chatBean.setChat_category_name("关注");
        //添加热门
        mTitles.add(chatBean);
        mTitles.addAll(HnHomeCate.mChatData);
        for (int i = 0; i < mTitles.size(); i++) {
//            if ("-1".equals(mTitles.get(i).getChat_category_id())) {
//                mFragments.add(HnHomeLiveHotFragment.getInstance());
//            } else {
//                mFragments.add(HnHomeCateLiveFragment.getInstance());
//            }
            if ("-2".equals((mTitles.get(i).getChat_category_id()))) {
                mFragments.add(new HnHomeChatFragment(""));
            } else {
                mFragments.add(new HnHomeChatFragment(mTitles.get(i).getChat_category_id()));
            }
        }
        mHomeViewpager.setOffscreenPageLimit(mTitles.size());
        mHomeViewpager.setAdapter(new PagerAdapter(getChildFragmentManager(), mFragments, mTitles));
        mSlidTab.setViewPager(mHomeViewpager);
        mHomeViewpager.setCurrentItem(getCurrentPage());
    }

    //进入首页默认显示推荐页
    private int getCurrentPage(){
        int currentItem = 0;
        for(int x=0;x < mTitles.size();x++){
            String title = mTitles.get(x).getChat_category_name();
            if("推荐".equals(title)){
                currentItem = x;
            }
        }
        return currentItem;
    }

    @OnClick({R.id.mIvTab_group,R.id.iv_search,R.id.iv_shoot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvTab_group:
                HnChatTypeDialog.newInstance(mTitles).setClickListen(new HnChatTypeDialog.SelDialogListener() {
                    @Override
                    public void sureClick() {

                    }
                }).show(mActivity.getFragmentManager(), "cate");
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
        if (mActivity == null) return;
//        if (HnHomeBiz.Banner.equals(type)) {
//            HnBannerModel model = (HnBannerModel) obj;
//            if (model == null || model.getD().getCarousel() == null || mBanner == null || mHnHomeBiz == null)
//                return;
//            mHnHomeBiz.initViewpager(mBanner, mImgUrl, model.getD().getCarousel());
//
//        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnHomeBiz.Banner.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private List<BaseScollFragment> mFragments;

        private List<HnChatTypeModel.DBean.ChatCategoryBean> mTitles;

        public PagerAdapter(FragmentManager fm, List<BaseScollFragment> mFragments, List<HnChatTypeModel.DBean.ChatCategoryBean> mTitles) {
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
            return mTitles.get(position).getChat_category_name();
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
