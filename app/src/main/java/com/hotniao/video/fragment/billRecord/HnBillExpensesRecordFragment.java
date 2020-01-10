package com.hotniao.video.fragment.billRecord;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.base.BaseFragment;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnBillEspensesRecordAdapter;
import com.hotniao.video.model.HnEspensesRecordModel;
import com.hotniao.video.utils.HnUiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;
import static com.hn.library.global.NetConstant.RESPONSE_CODE_BAD;
import static com.hotniao.video.R.string.code;

/**
 * Copyright (C) 2018,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：众赢
 * 类描述：
 * 创建人：李柯
 * 创建时间：2018/10/23 10:33
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillExpensesRecordFragment extends BaseFragment {
    private static final String TAG = "HnBillExpensesRecordFra";

    @BindView(R.id.record_recyclerview)
    RecyclerView recordRecyclerview;
    @BindView(R.id.record_ptr_refresh)
    PtrClassicFrameLayout recordPtrRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    Unbinder unbinder;

    HnBillEspensesRecordAdapter recordAdapter;
    int page=1;

    public static HnBillExpensesRecordFragment newInstance() {
        Bundle args = new Bundle();
        HnBillExpensesRecordFragment fragment = new HnBillExpensesRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_expenses_fragment;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recordAdapter = new HnBillEspensesRecordAdapter(mActivity);
        recordRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        recordRecyclerview.setAdapter(recordAdapter);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyImage(R.drawable.empty_com).setEmptyText(HnUiUtils.getString(R.string.now_no_record));
    }

    @Override
    protected void initData() {
        final RequestParam param = new RequestParam();
        param.put("page", page + "");
        param.put("pagesize", 20 + "");
        HnHttpUtils.getRequest(HnUrl.CHAT_CONSUME, param, TAG, new HnResponseHandler<HnEspensesRecordModel>(mActivity, HnEspensesRecordModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d("wuzhengxing","hnSuccess");
                try {
                    recordPtrRefresh.refreshComplete();
                    if (model.getC() == 0) {
                        List<HnEspensesRecordModel.DBean.ItemsBean> itemsList = model.getdBean().getItems();
                        if (page == 1) {
                            if (itemsList.size()==0) {
                                HnLogUtils.d(TAG,"itemsList.isEmpty()");
                                mHnLoadingLayout.setStatus(HnLoadingLayout.Empty);
                                mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.now_no_record));
                                return;
                            }
                            mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                            recordAdapter.setNewData(itemsList);
                        } else {
                            recordAdapter.addData(itemsList);
                            if (itemsList.isEmpty()) {
                                recordPtrRefresh.setMode(PtrFrameLayout.Mode.NONE);
                            }
                        }
                        HnUiUtils.setRefreshMode(recordPtrRefresh, page, 20, recordAdapter.getItemCount());
                    }
                } catch (Exception e) {
                    hnErr(RESPONSE_CODE_BAD, "");
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.d(TAG,"hnErr");
                HnLogUtils.d("wuzhengxing","hnErr");
                if (page == 1) {
                    if (REQUEST_NET_ERROR == code) {
                        mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                    } else {
                        mActivity.setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                    }
                } else {
                    recordPtrRefresh.refreshComplete();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initEvent() {
        //刷新处理
        recordPtrRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page += 1;
                initData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
            }
        });

        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                page = 1;
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                initData();
            }
        });

    }
}
