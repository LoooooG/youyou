package com.hotniao.livelibrary.ui.audience.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hn.library.base.BaseFragment;
import com.hn.library.base.EventBusBean;
import com.hn.library.ultraviewpager.HnSwipeAnimationController;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用户直播间  -- 空
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAudienceTopFragment extends BaseFragment {

    /**
     * 布局控件
     */
    RelativeLayout mRootExit;


    /**
     * 直播所需要的数据
     */
    private HnLiveListModel.LiveListBean bean;
    HnSwipeAnimationController mAnimationController;

    private RelativeLayout mRlRootView;
    private RelativeLayout mFrameInfo;

    public static HnAudienceTopFragment newInstance(HnLiveListModel.LiveListBean bean) {
        HnAudienceTopFragment fragment = new HnAudienceTopFragment();
        Bundle b = new Bundle();
        b.putParcelable("data", bean);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.live_layout_info_fragment;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化视图
        mRootExit = (RelativeLayout) mRootView.findViewById(R.id.root_view);
        mRlRootView = mRootView.findViewById(R.id.mRlRootView);
        mFrameInfo = mRootView.findViewById(R.id.mFrameInfo);

        EventBus.getDefault().register(this);
    }


    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            bean = bundle.getParcelable("data");
        }

        mAnimationController = new HnSwipeAnimationController(mActivity);
        mAnimationController.setAnimationView(mFrameInfo);

        try {
            mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mFrameTop, new HnTopEmptyFragment())
                    .commitAllowingStateLoss();
            mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mFrameInfo, HnAudienceInfoFragment.newInstance(bean))
                    .commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }


    @Override
    protected void initEvent() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCallBack(HnLiveEvent event) {
        if (event != null) {
            if (HnLiveConstants.EventBus.Scroll_viewPager.equals(event.getType())) {//主播在线，可以左右滑动；主播不在线，禁止左右滑动
                boolean isCanScroll = (boolean) event.getObj();
                if (mAnimationController != null) {
                    mAnimationController.setIsCanScroll(isCanScroll);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTouchEvenet(EventBusBean event) {
        if (event != null && "onTouch".equals(event.getType())) {
            boolean isCanScroll = (boolean) event.getObj();
            if (mAnimationController != null) {
                mAnimationController.setTouchScroll(isCanScroll);
            }
        }
    }
}
