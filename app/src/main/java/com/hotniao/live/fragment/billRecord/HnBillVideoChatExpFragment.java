package com.hotniao.live.fragment.billRecord;

import android.text.TextUtils;
import android.widget.TextView;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.base.CommListFragment;
import com.hotniao.live.model.HnBuyVipRecordModel;
import com.hotniao.live.model.HnLiveBackEarnModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.util.DataTimeUtils;
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

public class HnBillVideoChatExpFragment extends CommListFragment {
    private CommRecyclerAdapter mAdapter;
    private List<HnLiveBackEarnModel.DBean.ItemsBean> mData = new ArrayList<>();


    public static HnBillVideoChatExpFragment newInstance() {
        HnBillVideoChatExpFragment fragment = new HnBillVideoChatExpFragment();
        return fragment;
    }

    @Override
    protected String setTAG() {
        return HnUiUtils.getString(R.string.video_chat);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        mLoadingLayout.setStatus(HnLoadingLayout.Success);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                //时间
                ((TextView) holder.getView(R.id.mTvContent)).setText("与" + mData.get(position).getUser_nickname() + HnUiUtils.getString(R.string.main_chat));
                ((TextView) holder.getView(R.id.mTvPrice)).setText(mData.get(position).getConsume() + HnApplication.getmConfig().getCoin());


                if(!TextUtils.isEmpty(mData.get(position). getTime())){
                    if(DataTimeUtils.IsToday(Long.parseLong(mData.get(position). getTime()))){
                        ((TextView)holder.getView(R.id.mTvDay)).setText(R.string.day);
                        ((TextView)holder.getView(R.id.mTvTime)).setText(HnDateUtils.dateFormat(mData.get(position). getTime(),"HH:mm:ss"));
                    }else if(DataTimeUtils.IsYesterday(Long.parseLong(mData.get(position). getTime()))){
                        ((TextView)holder.getView(R.id.mTvDay)).setText(R.string.yesteday);
                        ((TextView)holder.getView(R.id.mTvTime)).setText(HnDateUtils.dateFormat(mData.get(position). getTime(),"HH:mm:ss"));
                    }else {
                        ((TextView)holder.getView(R.id.mTvDay)).setText(HnDateUtils.dateFormat(mData.get(position). getTime(),"yyyy-MM-dd"));
                        ((TextView)holder.getView(R.id.mTvTime)).setText(HnDateUtils.dateFormat(mData.get(position). getTime(),"HH:mm:ss"));
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
        return HnUrl.CHAT_CONSUME_COIN;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnLiveBackEarnModel>(HnLiveBackEarnModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();
                if (model.getD().getItems() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_record), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
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
