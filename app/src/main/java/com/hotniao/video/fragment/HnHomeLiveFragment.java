package com.hotniao.video.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hn.library.base.BaseFragment;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.tab.SlidingTabLayout;
import com.hn.library.tab.listener.OnTabSelectListener;
import com.hn.library.utils.HnNetUtil;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.video.HnApplication;
import com.hotniao.video.HnMainActivity;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnAuthStateActivity;
import com.hotniao.video.activity.HnBeforeLiveSettingActivity;
import com.hotniao.video.activity.HnHomeSearchActivity;
import com.hotniao.video.activity.HnVideoAuthApplyActivity;
import com.hotniao.video.eventbus.HnSelectLiveCateEvent;
import com.hotniao.video.model.HnCanLiveModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.video.utils.HnUserUtil;
import com.hotniao.livelibrary.ui.anchor.activity.HnAnchorActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
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
public class HnHomeLiveFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.home_viewpager)
    ViewPager mHomeViewpager;

    @BindView(R.id.mIvLeft)
    ImageView mIvLeft;
    @BindView(R.id.mSlidTab)
    SlidingTabLayout mSlidTab;
    @BindView(R.id.mRlTop)
    RelativeLayout mRlTop;

    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private List<BaseFragment> mFragments;
    private List<String> mTitles = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.home_live_fragment;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),mRlTop);
        setTab();
    }


    private void setTab() {
        if (mActivity == null) return;
        mTitles.add("回放");
        mTitles.add("热门");
        //fragment集合
        mFragments = new ArrayList<>();
        mFragments.add(HnPlayBackFragment.getInstance());
        mFragments.add(HnHomeLiveChildFragment.getInstance());
        mHomeViewpager.setOffscreenPageLimit(mTitles.size());
        mHomeViewpager.setAdapter(new PagerAdapter(getChildFragmentManager(), mFragments, mTitles));
        refreshUI();

        mSlidTab.setViewPager(mHomeViewpager);
        mHomeViewpager.setCurrentItem(1);
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

    @OnClick({R.id.mIvLeft, R.id.mIvRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvLeft: //搜索界面
                mActivity.openActivity(HnHomeSearchActivity.class);

                break;
            case R.id.mIvRight:
                checkNetWork();
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
        private List<String> mTitles;

        public PagerAdapter(FragmentManager fm, List<BaseFragment> mFragments, List<String> mTitles) {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void checkNetWork() {
        int netWorkState = HnNetUtil.getNetWorkState(mActivity);
        if (netWorkState == HnNetUtil.NETWORK_WIFI) {//是否wifi
            checkPermission();
        } else if (netWorkState == HnNetUtil.NETWORK_MOBILE) {//是否流量
            CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {
                    checkPermission();
                }

                @Override
                public void rightClick() {
                    HnNetUtil.openWirelessSettings(mActivity);

                }
            }).setTitle(HnUiUtils.getString(R.string.prompt)).setContent(HnUiUtils.getString(R.string.no_wifi))
                    .setRightText(HnUiUtils.getString(R.string.to_set)).setLeftText(HnUiUtils.getString(R.string.live_continue_play)).show();

        } else if (netWorkState == HnNetUtil.NETWORK_NONE) {//没有网络
            CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {
                }

                @Override
                public void rightClick() {
                    HnNetUtil.openWirelessSettings(mActivity);
                }
            }).setTitle(HnUiUtils.getString(R.string.prompt)).setContent(HnUiUtils.getString(R.string.no_network)).setRightText(HnUiUtils.getString(R.string.to_set)).show();
        }
    }

    /**
     * 检查权限是否打开
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            api23permissionCheck();
        } else {
            boolean cameraCanUse = HnUserUtil.isCameraCanUse();
            if (cameraCanUse) {
                boolean hasPermission = HnUserUtil.isAudioPermission();
                if (hasPermission) {
                    requestCanLive();
                } else {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.main_audio_unable_to_live));
                }
            } else {
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.main_camera_unable_to_live));
            }
        }
    }

    private void api23permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (String permission : permissionManifest) {
            if (PermissionChecker.checkSelfPermission(mActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.main_camera_or_audio_unable_to_live));
        } else {
            requestCanLive();
        }
    }

    /**
     * 是否能够直播
     */
    private void requestCanLive() {
        HnHttpUtils.getRequest(HnUrl.LIVE_GET_LIVE_INFO, null, HnUrl.LIVE_GET_LIVE_INFO, new HnResponseHandler<HnCanLiveModel>(HnCanLiveModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0 || model.getD() == null) {
                    if (HnAnchorActivity.mIsLiveing) return;
                    startActivity(new Intent(mActivity, HnBeforeLiveSettingActivity.class).putExtra("bean", (Serializable) model.getD()));
                } else {
                    checkAnchorStatus();
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                //实名认证未通过
                if (HnServiceErrorUtil.USER_CERTIFICATION_FAIL == errCode ||
                        HnServiceErrorUtil.USER_CERTIFICATION_CHECK == errCode ||
                        HnServiceErrorUtil.USER_NOT_CERTIFICATION == errCode) {
                    checkAnchorStatus();
                } else {
                    HnToastUtils.showToastShort(msg);
                }

            }
        });
    }

    private void checkAnchorStatus() {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.NoApply);
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.Authing);
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.AuthNoPass);
        }
    }
}
