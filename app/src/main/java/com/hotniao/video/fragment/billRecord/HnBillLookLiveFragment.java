package com.hotniao.video.fragment.billRecord;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.base.BaseFragment;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnBillLookLiveAdapter;
import com.hn.library.global.HnUrl;
import com.hotniao.video.model.HnLookLiveLogModel;
import com.hotniao.video.utils.HnUiUtils;

import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;
import static com.hn.library.global.NetConstant.RESPONSE_CODE_BAD;
import static com.hotniao.video.R.string.code;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述： 账单明细之看播
 * 创建人： mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillLookLiveFragment extends BaseFragment  {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;

    private int mPage=1;
    private HnBillLookLiveAdapter mBillStartLiveAdapter;

    public static HnBillLookLiveFragment newInstance(int index) {
        HnBillLookLiveFragment dialog = new HnBillLookLiveFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        dialog.setArguments(bundle);
        return dialog;

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_hn_open_or_look;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //初始化适配器
        mBillStartLiveAdapter = new HnBillLookLiveAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(mBillStartLiveAdapter);

        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyImage(R.drawable.empty_com).setEmptyText(getString(R.string.now_no_record));
    }

    @Override
    protected void initData() {
        RequestParam param=new RequestParam();
        param.put("page",mPage+"");
        param.put("pagesize",20+"");
        HnHttpUtils.getRequest(HnUrl.LIVE_PAY_LOG, param, TAG, new HnResponseHandler<HnLookLiveLogModel>(mActivity,HnLookLiveLogModel.class) {

            @Override
            public void hnSuccess(String response) {
                try {
                    mSwipeRefresh.refreshComplete();
                    if (model.getC() == 0) {
                        HnLookLiveLogModel.DBean.RecordListBean d = model.getD().getRecord_list();
                        List<HnLookLiveLogModel.DBean.RecordListBean.ItemsBean> items = d.getItems();
                        if(mPage==1) {
                            if(items.isEmpty()){
                                mHnLoadingLayout.setStatus(HnLoadingLayout.Empty);
                                mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.not_look_live_record));
                                return;
                            }
                            mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                            mBillStartLiveAdapter.setNewData(items);
                        }else {

                            mBillStartLiveAdapter.addData(items);
                            if(items.isEmpty()){
                                mSwipeRefresh.setMode(PtrFrameLayout.Mode.NONE);
                            }
                        }
                        HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20,mBillStartLiveAdapter.getItemCount());

                    }

                }catch (Exception e){
                    hnErr(RESPONSE_CODE_BAD,"");
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(mPage==1) {
                    if (REQUEST_NET_ERROR == code) {
                        mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                    } else {
                        mActivity.setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                    }
                }else {
                    mSwipeRefresh.refreshComplete();
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        //刷新处理
        mSwipeRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage+=1;
                initData();
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage=1;
                initData();
            }
        });

        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPage=1;
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                initData();
            }
        });
    }
}
