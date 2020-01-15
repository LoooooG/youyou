package com.hotniao.svideo.fragment.billRecord;

import android.text.TextUtils;
import android.widget.TextView;

import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hotniao.svideo.base.CommListFragment;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.model.HnBuyVipRecordModel;
import com.hotniao.livelibrary.util.DataTimeUtils;
import com.hotniao.svideo.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：购买Vip
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnBillBuyVipFragment extends CommListFragment {
    private CommRecyclerAdapter mAdapter;
    private List<HnBuyVipRecordModel.DBean.RecordListBean.ItemsBean> mData = new ArrayList<>();


    public static HnBillBuyVipFragment newInstance() {
        HnBillBuyVipFragment fragment = new HnBillBuyVipFragment();
        return fragment;
    }

    @Override
    protected String setTAG() {
        return "购买Vip";
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        mLoadingLayout.setStatus(HnLoadingLayout.Success);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                //时间
                ((TextView)holder.getView(R.id.mTvContent)).setText(HnUiUtils.getString(R.string.buy)+mData.get(position).getVip().getMonth()+HnUiUtils.getString(R.string.month_vip));
                ((TextView)holder.getView(R.id.mTvPrice)).setText("¥"+mData.get(position).getVip().getConsume());

                if(!TextUtils.isEmpty(mData.get(position).getVip().getTime())){
                    if(DataTimeUtils.IsToday(Long.parseLong(mData.get(position).getVip().getTime()))){
                        ((TextView)holder.getView(R.id.mTvDay)).setText(R.string.day);
                        ((TextView)holder.getView(R.id.mTvTime)).setText(HnDateUtils.dateFormat(mData.get(position).getVip().getTime(),"HH:mm:ss"));
                    }else if(DataTimeUtils.IsYesterday(Long.parseLong(mData.get(position).getVip().getTime()))){
                        ((TextView)holder.getView(R.id.mTvDay)).setText(R.string.yesteday);
                        ((TextView)holder.getView(R.id.mTvTime)).setText(HnDateUtils.dateFormat(mData.get(position).getVip().getTime(),"HH:mm:ss"));
                    }else {
                        ((TextView)holder.getView(R.id.mTvDay)).setText(HnDateUtils.dateFormat(mData.get(position).getVip().getTime(),"yyyy-MM-dd"));
                        ((TextView)holder.getView(R.id.mTvTime)).setText(HnDateUtils.dateFormat(mData.get(position).getVip().getTime(),"HH:mm:ss"));
                    }
                }
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_bill_buy_vip;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };

        return mAdapter;
    }

    @Override
    protected RequestParams setRequestParam() {
        RequestParams params = new RequestParams();
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.BUY_VIP_RECORD;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnBuyVipRecordModel>(HnBuyVipRecordModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();
                if (model.getD().getRecord_list() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_record), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getRecord_list().getItems());
                if(mAdapter!=null)
                mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_record), R.drawable.empty_com);

                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_record), R.drawable.empty_com);
            }
        };
    }
}
