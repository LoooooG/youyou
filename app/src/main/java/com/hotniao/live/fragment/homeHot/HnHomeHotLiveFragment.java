package com.hotniao.live.fragment.homeHot;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.global.HnConstants;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.adapter.HnHomeHotExtAdapter;
import com.hotniao.live.base.BaseScollFragment;
import com.hotniao.live.biz.home.HnHomeBiz;
import com.hotniao.live.eventbus.HnSelectLiveCateEvent;
import com.hotniao.live.fragment.HnHomeChildFragment;
import com.hotniao.live.model.HnHomeHotModel;
import com.hotniao.live.model.bean.HnHomeHotBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：首页 热门
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：mj
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeHotLiveFragment extends BaseScollFragment implements HnLoadingLayout.OnReloadListener, BaseRequestStateListener {
    public static final String TAG = "HnHomeHotLiveFragment";
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mPtr;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;
    @BindView(R.id.mRlPer)
    RelativeLayout mRlPer;

    private View mEmptyLayout;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 业务逻辑类，处理首页业务
     */
    private HnHomeBiz mHnHomeBiz;

    /**
     * 热门直播列表适配器
     */
    private BaseQuickAdapter mAdapter;
    /**
     * 直播列表数据源
     */
    private List<HnHomeHotBean.ItemsBean> mDatas = new ArrayList<>();


    private DayNightHelper mDayNightHelper;


    public static HnHomeHotLiveFragment getInstance() {
        HnHomeHotLiveFragment fragment = new HnHomeHotLiveFragment();
        return fragment;
    }

    @Override
    public int getContentViewId() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return R.layout.common_loading_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLoading.setStatus(HnLoadingLayout.Loading);
        mLoading.setEmptyImage(R.drawable.home_live).setEmptyText(getString(R.string.anchor_rest_now_come));
        mLoading.setOnReloadListener(this);
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);

        //初始化适配器
        initAdapter();
        //事件监听
        initEvent();
        mDayNightHelper = new DayNightHelper();
    }

    @Override
    protected void initData() {
        mPage = 1;

        getData();
        refreshUI();
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mRecyclerView.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAdapter = new HnHomeHotExtAdapter(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


        /**空数据*/
        mEmptyLayout = LayoutInflater.from(mActivity).inflate(R.layout.empty_data_layout, (ViewGroup) mRecyclerView.getParent(), false);
        HnLoadingLayout mLoadLayout = mEmptyLayout.findViewById(R.id.mLoading);
        mLoadLayout.setEmptyImage(R.drawable.home_live).setEmptyText(getString(R.string.anchor_rest_now_come));
        mLoadLayout.setStatus(HnLoadingLayout.Empty);

    }

    /**
     * 事件监听
     */
    protected void initEvent() {
        //刷新监听
        mPtr.disableWhenHorizontalMove(true);
        mPtr.setMode(PtrFrameLayout.Mode.NONE);


    }


    /**
     * 重新加载
     *
     * @param v
     */
    @Override
    public void onReload(View v) {
        initData();
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null || mLoading == null) return;
        if (HnHomeBiz.HotLive.equals(type)) {
            HnHomeHotModel model = (HnHomeHotModel) obj;
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            mActivity.closeRefresh(mPtr);
            refreshComplete();

            if (model != null && model.getD() != null) {
                updateUI(model.getD());
            } else {
                showEmptyView();
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mLoading == null) return;
        if (HnHomeBiz.HotLive.equals(type)) {
            mActivity.closeRefresh(mPtr);
            refreshComplete();
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            showEmptyView();

            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 更新界面数据
     *
     * @param data 首页数据
     */
    private void updateUI(HnHomeHotBean data) {
        //直播列表
        List<HnHomeHotBean.ItemsBean> lives = data.getItems();
        if (lives != null && mAdapter != null) {
            if (lives.size() > 0) {
                if (mPage == 1) {
                    mDatas.clear();
                    mAdapter.setNewData(lives);
                } else {
                    mAdapter.addData(lives);
                }
                mDatas.addAll(lives);
            } else {
                if (mPage == 1 || mAdapter.getItemCount() < 1) {
                    mDatas.clear();
                }
            }
        }
        showEmptyView();

    }


    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }


    /**
     * 刷新UI界面
     */
    public void refreshUI() {
        //背景色
        TypedValue background = new TypedValue();
        //字体颜色#333333
        TypedValue textColor333 = new TypedValue();
        //条目背景颜色
        TypedValue item_color = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.pageBg_color, background, true);
        theme.resolveAttribute(R.attr.item_bg_color, item_color, true);
        theme.resolveAttribute(R.attr.text_color_333, textColor333, true);

        mRlPer.setBackgroundResource(background.resourceId);
        //根布局背景色
//        mLoading.setBackgroundResource(background.resourceId);
        Resources resources = getResources();

        boolean isDay = mDayNightHelper.isDay();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventSelectCate(HnSelectLiveCateEvent event) {
        if (HnSelectLiveCateEvent.REFRESH_UI_LIVE == event.getType()) {
            refreshUI();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventRefresh(EventBusBean event) {
        if (HnConstants.EventBus.RefreshLiveList == event.getType()) {
            mPage = 1;
            getData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 父fragment通知子fragment刷新
     */
    @Override
    public void pullToRefresh() {
        mPage = 1;
        getData();
    }

    private void getData() {
        if (HnMainActivity.mLocEntity == null)
            mHnHomeBiz.getOneHomeHotLive(mPage, null, null);
        else
            mHnHomeBiz.getOneHomeHotLive(mPage, HnMainActivity.mLocEntity.getmLng(), HnMainActivity.mLocEntity.getmLat());
    }

    /**
     * 通知父fragment刷新结束
     */
    @Override
    public void refreshComplete() {
        if (this.getParentFragment() instanceof HnHomeChildFragment) {
            ((HnHomeChildFragment) (this.getParentFragment())).refreshComplete();
        }
    }


    /**
     * 没有直播列表数据时调用
     */
    public void showEmptyView() {
        mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
        if (mDatas == null || mDatas.size() < 1) {
            if (mAdapter != null && mEmptyLayout != null) {
                mAdapter.setNewData(null);
                if ((ViewGroup) (mEmptyLayout.getParent()) != null)
                    ((ViewGroup) (mEmptyLayout.getParent())).removeView(mEmptyLayout);
                mAdapter.setEmptyView(mEmptyLayout);
            }
        }

    }

}
