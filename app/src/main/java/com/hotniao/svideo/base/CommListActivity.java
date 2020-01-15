package com.hotniao.svideo.base;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
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
import com.loopj.android.http.RequestParams;

import butterknife.BindView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司
 * 项目名称：乐疯直播
 * 类描述：列表的公共类
 * 创建人：Mr.Xu
 * 创建时间：2017/3/3 0003
 */
public abstract class CommListActivity extends BaseActivity {

    @BindView(R.id.mRecycler)
    protected RecyclerView mRecycler;
    @BindView(R.id.mTvHead)
    protected TextView mTvHead;
    @BindView(R.id.mRefresh)
    protected PtrClassicFrameLayout mSpring;
    @BindView(R.id.mHnLoadingLayout)
    protected HnLoadingLayout mLoadingLayout;

    private String TAG = "";
    private CommRecyclerAdapter mAdapter;
    //页数   个数     是否刷新
    protected int page = 1;
    protected int pageSize = 20;

    private boolean isGrid = false;

    @Override
    public int getContentViewId() {
        return R.layout.comm_list;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        //标题和标注
        TAG = setTitle();
        setTitle(setTitle());
        //适配器
        mAdapter = setAdapter();
        initRecycler();
        initEvent();
        getData(HnRefreshDirection.TOP, page);

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
    public void getInitData() {

    }

    //TODO 待加网络请求

    protected abstract String setTitle();

    protected abstract CommRecyclerAdapter setAdapter();

    public void setGridManager(boolean isGrid) {
        this.isGrid = isGrid;

    }

    public void setTvHeadShow(boolean isShow) {
        if (isShow) mTvHead.setVisibility(View.VISIBLE);
        else mTvHead.setVisibility(View.GONE);

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
            mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
            mRecycler.addItemDecoration(new HnRecyclerGridDecoration(8));
        } else {
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
    }

    /**
     * 创建人：Mr.Xu
     * 方法描述：刷新处理
     */
    private void initEvent() {
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
        RequestParams param = setRequestParam();
        this.page = page;
        param.put("page", page + "");
        param.put("pagesize", pageSize + "");
        HnHttpUtils.postRequest(setRequestUrl(), param, setRequestUrl(), setResponseHandler(state));

    }

    protected void setEmpty(String content, int res) {
        if (mAdapter == null) return;
        if (mAdapter.getItemCount() < 1) {
            mLoadingLayout.setStatus(HnLoadingLayout.Empty);
            mLoadingLayout.setEmptyText(content).setEmptyImage(res);
        } else {
            mLoadingLayout.setStatus(HnLoadingLayout.Success);
        }
    }

    protected void refreshFinish() {
        if (mSpring != null) mSpring.refreshComplete();
    }


}
