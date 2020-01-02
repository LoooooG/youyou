package com.hotniao.live.fragment.billRecord;

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
import com.hotniao.live.R;
import com.hotniao.live.adapter.HnBillSendAdapter;
import com.hotniao.live.model.HnSendGiftLogModel;
import com.hotniao.live.utils.HnUiUtils;

import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;
import static com.hn.library.global.NetConstant.RESPONSE_CODE_BAD;
import static com.hotniao.live.R.string.code;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 - 送礼
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillSendFragment extends BaseFragment {

    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mLvBillRec;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;


    private HnBillSendAdapter mSendAdapter;

    private int mPage = 1;

    public static HnBillSendFragment newInstance() {
        Bundle args = new Bundle();
        HnBillSendFragment fragment = new HnBillSendFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.framgnet_bill_send_rec;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //初始化适配器
        mSendAdapter = new HnBillSendAdapter(mActivity);
        mLvBillRec.setLayoutManager(new LinearLayoutManager(mActivity));
        mLvBillRec.setHasFixedSize(true);
        mLvBillRec.setAdapter(mSendAdapter);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setEmptyImage(R.drawable.empty_com).setEmptyText(HnUiUtils.getString(R.string.now_no_record));
    }

    @Override
    protected void initData() {
        RequestParam param = new RequestParam();
        param.put("page", mPage + "");
        param.put("pagesize", 20 + "");
        HnHttpUtils.getRequest(HnUrl.SEND_GIFT_LOG, param, TAG, new HnResponseHandler<HnSendGiftLogModel>(mActivity, HnSendGiftLogModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.d("HnBillSendFragment","hnSuccess");
                try {
                    mSwipeRefresh.refreshComplete();
                    if (model.getC() == 0) {
                        HnSendGiftLogModel.DBean.RecordListBean d = model.getD().getRecord_list();
                        List<HnSendGiftLogModel.DBean.RecordListBean.ItemsBean> items = d.getItems();
                        if (mPage == 1) {
                            if (items.isEmpty()) {
                                mHnLoadingLayout.setStatus(HnLoadingLayout.Empty);
                                mHnLoadingLayout.setEmptyText(HnUiUtils.getString(R.string.not_send_gift));
                                return;
                            }
                            mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                            mSendAdapter.setNewData(items);
                        } else {

                            mSendAdapter.addData(items);
                            if (items.isEmpty()) {
                                mSwipeRefresh.setMode(PtrFrameLayout.Mode.NONE);
                            }
                        }
                        HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20,mSendAdapter.getItemCount());

                    }

                } catch (Exception e) {
                    hnErr(RESPONSE_CODE_BAD, "");
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.d("HnBillSendFragment","hnErr");
                if (mPage == 1) {
                    if (REQUEST_NET_ERROR == code) {
                        mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                    } else {
                        mActivity.setLoadViewState(HnLoadingLayout.Error, mHnLoadingLayout);
                    }
                } else {
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
