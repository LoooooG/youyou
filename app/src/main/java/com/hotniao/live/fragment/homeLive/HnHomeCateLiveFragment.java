package com.hotniao.live.fragment.homeLive;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.global.HnConstants;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.R;
import com.hotniao.live.adapter.HnHomeHotExtAdapter;
import com.hotniao.live.biz.home.HnHomeBiz;
import com.hotniao.live.eventbus.HnSelectLiveCateEvent;
import com.hotniao.live.model.HnHomeHotModel;
import com.hotniao.live.model.bean.HnHomeHotBean;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.model.HnLiveRoomInfoModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：首页 分类直播
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：mj
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeCateLiveFragment extends BaseFragment implements HnLoadingLayout.OnReloadListener, BaseRequestStateListener {
    static final String TAG = "RecyclerViewFragment";
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
    /**
     * 用于标识是切换大图模式还是侠拼图模式
     */
    private boolean mIsLittleMode = false;
    /**
     * 标识符  0 热门 1 最新
     */
    private String mCateId;

    private DayNightHelper mDayNightHelper;


    public static HnHomeCateLiveFragment getInstance() {
        HnHomeCateLiveFragment fragment = new HnHomeCateLiveFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCateId = bundle.getString("cateId");
        }
        //初始化适配器
        initAdapter();
        //事件监听
        initEvent();
        mDayNightHelper = new DayNightHelper();
    }

    @Override
    protected void initData() {
        mPage = 1;
        mHnHomeBiz.requestToHotList(mPage, mCateId);
        refreshUI();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            initData();
        }
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        /**空数据*/
        mEmptyLayout = LayoutInflater.from(mActivity).inflate(R.layout.empty_data_layout, (ViewGroup) mRecyclerView.getParent(), false);
        HnLoadingLayout mLoadLayout = mEmptyLayout.findViewById(R.id.mLoading);
        mLoadLayout.setEmptyImage(R.drawable.home_live).setEmptyText(getString(R.string.anchor_rest_now_come));
        mLoadLayout.setStatus(HnLoadingLayout.Empty);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new HnHomeHotExtAdapter(true);
//        mRecyclerView.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * 事件监听
     */
    protected void initEvent() {
        //刷新监听
//        mPtr.disableWhenHorizontalMove(true);
        mPtr.setMode(PtrFrameLayout.Mode.REFRESH);
        mPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage = mPage + 1;
                mHnHomeBiz.requestToHotList(mPage, mCateId);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHnHomeBiz.requestToHotList(mPage, mCateId);
            }
        });
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
        if ("hot_list".equals(type)) {
            HnHomeHotModel model = (HnHomeHotModel) obj;
            mActivity.closeRefresh(mPtr);
            if (model != null && model.getD() != null) {
                updateUI(model.getD());
            } else {
                if (mPage == 1 || mAdapter.getItemCount() < 1) {
                    mDatas.clear();
                    showEmptyView();
                }
            }
        } else if ("join_live_room".equals(type)) {//进入直播间
            HnLiveRoomInfoModel model = (HnLiveRoomInfoModel) obj;
            if (model != null && model.getD() != null) {//通过arouter框架进行跳转 进入用户直播间
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", model.getD());
                ARouter.getInstance().build("/live/HnAudienceActivity").with(bundle).navigation();
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mLoading == null) return;
        if ("hot_list".equals(type)) {
            mActivity.closeRefresh(mPtr);
            if (mPage == 1 || mAdapter.getItemCount() < 1) {
                mDatas.clear();
                showEmptyView();
            }
            HnToastUtils.showToastShort(msg);
        } else if ("join_live_room".equals(type)) {//进入直播间
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
            if (mPage == 1 && lives.size() > 0) {
                if (lives.size() > 1 && mAdapter.getItemCount() < 2) {
//                    mAdapter = new HnHomeHotLittleAdapter();
                    mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));

//                    mRecyclerView.setAdapter(mAdapter);
                } else if (lives.size() < 2 && mAdapter.getItemCount() > 1) {
//                    mAdapter = new HnHomeHotExtAdapter();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//                    mRecyclerView.setAdapter(mAdapter);
                }
            }
            if (lives.size() > 0) {
                showSuccessView();
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
                    mAdapter.setNewData(mDatas);
                    showEmptyView();
                }
            }
        }


        HnUiUtils.setRefreshMode(mPtr, mPage, 20, mDatas.size());
    }

    /**
     * 有直播列表数据时调用
     */
    public void showSuccessView() {
        mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
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

        //根布局背景色
        mRlPer.setBackgroundResource(background.resourceId);
        mLoading.setBackgroundResource(background.resourceId);
        Resources resources = getResources();

        boolean isDay = mDayNightHelper.isDay();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventSelectCate(HnSelectLiveCateEvent event) {
        if (HnSelectLiveCateEvent.REFRESH_UI_LIVE == event.getType()) {
            refreshUI();
        } else {
            if (event.getId().equals(mCateId)) {
                mPage = 1;
                mHnHomeBiz.requestToHotList(mPage, mCateId);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventRefresh(EventBusBean event) {
        if (HnConstants.EventBus.RefreshLiveList.equals(event.getType())) {
            mPage = 1;
            mHnHomeBiz.requestToHotList(mPage, mCateId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
