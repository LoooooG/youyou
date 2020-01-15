package com.hotniao.svideo.fragment.billRecord;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hn.library.base.BaseFragment;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.adapter.HnBillInViteEarningAdapter;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.model.HnBillInviteDetailModel;
import com.hotniao.svideo.dialog.HnEarningTotalTypePopWindow;
import com.hotniao.svideo.utils.HnUiUtils;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.RESPONSE_CODE_BAD;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述： 邀请收益
 * 创建人： mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillInviteFragment extends BaseFragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;

    private TextView mTvTotal, mTvEmpty;
    private HnSkinTextView mTvType;
    private HnEarningTotalTypePopWindow mPopWindow;

    private int mPage = 1;
    private HnBillInViteEarningAdapter mAdapter;

    private List<HnBillInviteDetailModel.DBean.RecordListBean.ItemsBean> mData = new ArrayList<>();
    /**
     * 时间类型
     */
    private String mDateType = HnEarningTotalTypePopWindow.DAY;


    public static HnBillInviteFragment newInstance(int index) {
        HnBillInviteFragment dialog = new HnBillInviteFragment();
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
        mAdapter = new HnBillInViteEarningAdapter(mData);
        recyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(mAdapter);

        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
        addHead();
    }


    /**
     * 添加头部
     */
    private void addHead() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.head_earning_total, null);
        mTvTotal = (TextView) view.findViewById(R.id.mTvTotal);
        mTvEmpty = (TextView) view.findViewById(R.id.mTvEmpty);
        mTvType = (HnSkinTextView) view.findViewById(R.id.mTvType);

        if (mPopWindow == null) {
            mPopWindow = new HnEarningTotalTypePopWindow(mActivity);
            mPopWindow.setOnItemClickListener(new HnEarningTotalTypePopWindow.OnItemClickListener() {
                @Override
                public void itemClick(String name, String type) {
                    mTvType.setText(name);
                    mDateType = type;
                    mPage = 1;
                    initData();
                }

                @Override
                public void dismissLis() {
                    mTvType.setRightDrawable(R.drawable.account_lower);
                }
            });
        }
        mTvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPopWindow == null) {
                    mPopWindow = new HnEarningTotalTypePopWindow(mActivity);
                }
                mPopWindow.showUp(v);
                mTvType.setRightDrawable(R.drawable.account_upper);

            }
        });
        mAdapter.addHeaderView(view);

    }

    @Override
    protected void initData() {
        RequestParam param = new RequestParam();
        param.put("page", mPage + "");
        param.put("pagesize", 20 + "");
        param.put("date_type", mDateType + "");
        param.put("type", "dot");
        HnHttpUtils.getRequest(HnUrl.USER_AMOUNRECORD_INVITE_COIN, param, TAG, new HnResponseHandler<HnBillInviteDetailModel>(mActivity, HnBillInviteDetailModel.class) {

            @Override
            public void hnSuccess(String response) {
                try {
                    if (mActivity == null) return;
                    mSwipeRefresh.refreshComplete();
                    if (model.getC() == 0) {
                        HnBillInviteDetailModel.DBean.RecordListBean d = model.getD().getRecord_list();
                        List<HnBillInviteDetailModel.DBean.RecordListBean.ItemsBean> items = d.getItems();
                        mTvTotal.setText(HnUtils.setTwoPoints(model.getD().getAmount_total()) + HnApplication.getmConfig().getDot());
                        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                        if (mPage == 1) {
                            mData.clear();
                        }
                        mData.addAll(items);
                        if (mAdapter != null) mAdapter.notifyDataSetChanged();
                        setEmpty();
                        HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20, mData.size());
                    }

                } catch (Exception e) {
                    hnErr(RESPONSE_CODE_BAD, "");
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                mSwipeRefresh.refreshComplete();
                setEmpty();
            }
        });
    }

    private void setEmpty() {
        if (mTvEmpty == null) return;
        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
        mTvEmpty.setVisibility(mData.size() < 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        //刷新处理
        mSwipeRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                initData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                initData();
            }
        });

        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPage = 1;
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                initData();
            }
        });
    }

}
