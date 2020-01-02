package com.hotniao.live.fragment.billRecord;

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
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.adapter.HnBillReceiveAdapter;
import com.hn.library.global.HnUrl;
import com.hotniao.live.model.HnReceiveGiftLogModel;
import com.hotniao.live.dialog.HnEarningTotalTypePopWindow;
import com.hotniao.live.utils.HnUiUtils;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.RESPONSE_CODE_BAD;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 - 收礼
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillReceiveFragment extends BaseFragment {


    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mLvBillRec;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;

    /**
     * 提现记录列表适配器
     */
    private HnBillReceiveAdapter mReceiveAdapter;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 时间类型
     */
    private String mDateType = HnEarningTotalTypePopWindow.DAY;

    private HnEarningTotalTypePopWindow mPopWindow;
    private TextView mTvTotal, mTvEmpty;
    private HnSkinTextView mTvType;

    private List<HnReceiveGiftLogModel.DBean.RecordListBean.ItemsBean> mData = new ArrayList<>();

    public static HnBillReceiveFragment newInstance() {

        Bundle args = new Bundle();
        HnBillReceiveFragment fragment = new HnBillReceiveFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.framgnet_bill_earning;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化适配器
        mReceiveAdapter = new HnBillReceiveAdapter(mData);
        mLvBillRec.setLayoutManager(new LinearLayoutManager(mActivity));
        mLvBillRec.setHasFixedSize(true);
        mLvBillRec.setAdapter(mReceiveAdapter);

        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
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
                    mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
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
        mReceiveAdapter.addHeaderView(view);

    }

    @Override
    protected void initData() {

        RequestParam param = new RequestParam();
        param.put("page", mPage + "");
        param.put("pagesize", 20 + "");
        param.put("date_type", mDateType + "");
        HnHttpUtils.getRequest(HnUrl.GET_GIFT_LOG, param, TAG, new HnResponseHandler<HnReceiveGiftLogModel>(mActivity, HnReceiveGiftLogModel.class) {

            @Override
            public void hnSuccess(String response) {

                try {
                    mSwipeRefresh.refreshComplete();
                    if (model.getC() == 0) {
                        HnReceiveGiftLogModel.DBean.RecordListBean d = model.getD().getRecord_list();
                        List<HnReceiveGiftLogModel.DBean.RecordListBean.ItemsBean> items = d.getItems();
                        mTvTotal.setText( HnUtils.setTwoPoints(model.getD().getAmount_total())+HnApplication.getmConfig().getDot());
                        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                        if (mPage == 1) {
                            mData.clear();
                        }
                        mData.addAll(items);
                        if (mReceiveAdapter != null)
                            mReceiveAdapter.notifyDataSetChanged();
                        setEmpty();
                        HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20,mReceiveAdapter.getItemCount());
                    }

                } catch (Exception e) {
                    hnErr(RESPONSE_CODE_BAD, "");
                    setEmpty();
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
