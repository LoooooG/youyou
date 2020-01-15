package com.hotniao.svideo.fragment.video;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.svideo.HnMainActivity;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnVideoShowActivity;
import com.hotniao.svideo.adapter.HnHomeVideoAdapter;
import com.hotniao.svideo.base.BaseScollFragment;
import com.hotniao.svideo.biz.home.HnHomeBiz;
import com.hotniao.svideo.model.HnVideoModel;
import com.hotniao.svideo.utils.HnUiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：个人秀
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：mj
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPersonalVideoFragment extends BaseScollFragment implements HnLoadingLayout.OnReloadListener, BaseRequestStateListener {
    public static final String TAG = "HnPersonalVideoFragment";
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mPtr;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;
    @BindView(R.id.mRlPer)
    RelativeLayout mRlPer;

    /**
     * 头部布局  广告位
     */
    private View mHeaderView;
    private RelativeLayout mRlEmpty;
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
    private HnHomeVideoAdapter mAdapter;
    /**
     * 直播列表数据源
     */
    private List<HnVideoModel.DBean.ItemsBean> mDatas = new ArrayList<>();



    public static HnPersonalVideoFragment getInstance() {
        HnPersonalVideoFragment fragment = new HnPersonalVideoFragment();
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.common_loading_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLoading.setStatus(HnLoadingLayout.Loading);
        mLoading.setEmptyImage(R.drawable.home_live).setEmptyText(getString(R.string.now_no_data));
        mLoading.setOnReloadListener(this);
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);
        //初始化适配器
        initAdapter();
        //事件监听
        initEvent();
    }

    @Override
    protected void initData() {
        mPage = 1;
        getData();
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mRecyclerView.addItemDecoration(new HnSpacesItemDecoration(6, true));
        mAdapter = new HnHomeVideoAdapter(mDatas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mHeaderView = mActivity.getLayoutInflater().inflate(R.layout.bannerview, null);
        mRlEmpty = (RelativeLayout) mHeaderView.findViewById(R.id.rl_empty);
        mHeaderView.findViewById(R.id.convenientBanner).setVisibility(View.GONE);
        ((TextView) mHeaderView.findViewById(R.id.mTvEmpty)).setText(R.string.now_no_data);

        mAdapter.addHeaderView(mHeaderView);

    }

    /**
     * 事件监听
     */
    protected void initEvent() {
        //刷新监听
        mPtr.disableWhenHorizontalMove(true);
        mPtr.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage = mPage + 1;
                getData();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

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
        if (HnHomeBiz.HotVideo.equals(type)) {
            HnVideoModel model = (HnVideoModel) obj;
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            mActivity.closeRefresh(mPtr);
            refreshComplete();

            if (model != null && model.getD() != null) {
                updateUI(model.getD());
            } else {
                if (mAdapter != null && mAdapter.getItemCount() < 1)
                    mRlEmpty.setVisibility(View.VISIBLE);
                mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mLoading == null) return;
        if (HnHomeBiz.HotVideo.equals(type)) {
            mActivity.closeRefresh(mPtr);
            refreshComplete();
            mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
            if (mAdapter != null && mAdapter.getItemCount() < 1)
                mRlEmpty.setVisibility(View.VISIBLE);

            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 更新界面数据
     *
     * @param data 首页数据
     */
    private void updateUI(HnVideoModel.DBean data) {
        //直播列表
        List<HnVideoModel.DBean.ItemsBean> lives = data.getItems();

        if (lives != null && mAdapter != null) {
            if (lives.size() > 0) {
                showSuccessView();
                if (mPage == 1) {
                    mDatas.clear();
                }
                mDatas.addAll(lives);
                mAdapter.notifyDataSetChanged();
                lives.clear();
            } else {
                if (mPage == 1 || mAdapter.getItemCount() < 1) {
                    mDatas.clear();
                    mAdapter.setNewData(mDatas);
                    showEmptyView();
                }
            }
        }
        HnUiUtils.setRefreshModeNone(mPtr, mPage, 10, mDatas.size());

    }


    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }


    /**
     * 有直播列表数据时调用
     */
    public void showSuccessView() {
        mRlEmpty.setVisibility(View.GONE);
    }

    /**
     * 没有直播列表数据时调用
     */
    public void showEmptyView() {
        mRlEmpty.setVisibility(View.VISIBLE);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
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
            mHnHomeBiz.getOneHomeHotVideo(mPage, 0, null, null, null, null, null,"1");
    }

    /**
     * 通知父fragment刷新结束
     */
    @Override
    public void refreshComplete() {
        if (this.getActivity() instanceof HnVideoShowActivity) {
            ((HnVideoShowActivity) (this.getActivity())).refreshComplete();
        }
    }


}
