package com.hotniao.svideo.base;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.base.HnViewPagerBaseFragment;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.view.HnRecyclerGridDecoration;
import com.hotniao.svideo.R;
import com.hotniao.svideo.fragment.HnPlatformListFragment;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司
 * 项目名称：乐疯直播
 * 类描述：列表的公共类
 * 创建人：Mr.Xu
 * 创建时间：2017/3/3 0003
 */
public abstract class CommListFragment extends HnViewPagerBaseFragment {

    @BindView(R.id.mRecycler)
    protected RecyclerView mRecycler;
    @BindView(R.id.mRefresh)
    protected PtrClassicFrameLayout mSpring;
    @BindView(R.id.mHnLoadingLayout)
    protected HnLoadingLayout mLoadingLayout;

    private String TAG = "CommListFragment";
    private CommRecyclerAdapter mAdapter;

    private boolean isGrid = false;
    //页数   个数     是否刷新
    protected int page = 1;
    protected int pageSize = 20;

    @Override
    public int getContentViewId() {
        return R.layout.comm_list;
    }


    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //标题和标注
        TAG = setTAG();
        //适配器
        mAdapter = setAdapter();
        initRecycler();
        initEvent();
        getData(HnRefreshDirection.BOTH, page);


        //错误重新加载
        mLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                page = 1;
                mLoadingLayout.setStatus(HnLoadingLayout.Loading);
                getData(HnRefreshDirection.BOTH, page);
            }
        });
    }

    @Override
    public void fetchData() {

    }

    //TODO 待加网络请求

    protected abstract String setTAG();

    protected abstract CommRecyclerAdapter setAdapter();

    public void setGridManager(boolean isGrid) {
        this.isGrid = isGrid;
    }

    public void setLoadMore() {
        mSpring.setMode(PtrFrameLayout.Mode.REFRESH);
    }

    protected abstract RequestParams setRequestParam();

    protected abstract String setRequestUrl();

    protected abstract HnResponseHandler setResponseHandler(HnRefreshDirection state);

    /**
     * 创建人：Mr.Xu
     * 方法描述：Recycler列表初始化
     */
    private void initRecycler() {
        if (isGrid) {
            mRecycler.setLayoutManager(new GridLayoutManager(mActivity, 2));

            mRecycler.addItemDecoration(new HnRecyclerGridDecoration(6));
        } else {
            mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        }
        mRecycler.setHasFixedSize(true);

        mRecycler.setAdapter(mAdapter);
    }


    /**
     * 创建人：Mr.Xu
     * 方法描述：刷新处理
     */
    protected void initEvent() {
        mSpring.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                page = page + 1;
                getData(HnRefreshDirection.BOTH, page);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                page = 1;
                getData(HnRefreshDirection.TOP, page);
            }
        });

    }

    protected void getData(HnRefreshDirection state, int page) {
        this.page = page;
        RequestParams param = setRequestParam();
        param.put("page", page + "");
        param.put("pagesize", pageSize + "");
        HnHttpUtils.postRequest(setRequestUrl(), param, setRequestUrl(), setResponseHandler(state));

    }

    protected void setEmpty(String content, int res) {
        if (isAdded()) {
            if (mActivity == null) return;
            if (getArguments() != null && HnPlatformListFragment.FansContribute.equals(getArguments().getString("type")))
                content = "还没有收到礼物，赶快去直播吧~";
            if (mAdapter == null || mLoadingLayout == null) return;
            if (mAdapter.getItemCount() < 1) {
                mLoadingLayout.setEmptyText(content);
                mLoadingLayout.setEmptyImage(res);
                setLoadViewState(HnLoadingLayout.Empty, mLoadingLayout);
            } else {
                setLoadViewState(HnLoadingLayout.Success, mLoadingLayout);
            }
        }
    }

    protected void refreshFinish() {
        if (mSpring != null) mSpring.refreshComplete();
    }


}
