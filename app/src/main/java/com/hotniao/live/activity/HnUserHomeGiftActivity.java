package com.hotniao.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.base.CommListActivity;
import com.hotniao.live.model.HnAnchorAcquireGiftModel;
import com.hotniao.live.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：用户获得的礼物
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserHomeGiftActivity extends CommListActivity {
    private List<HnAnchorAcquireGiftModel.DBean.ItemsBean >mData=new ArrayList<>();
    private CommRecyclerAdapter mAdapter;

    public  static void luncher(Activity activity ,String uid){
        activity.startActivity(new Intent(activity,HnUserHomeGiftActivity.class).putExtra("userId",uid));
    }

    @Override
    protected String setTitle() {
        return HnUiUtils.getString(R.string.gift_totle);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {

        mAdapter=new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                HnAnchorAcquireGiftModel.DBean.ItemsBean item = mData.get(position);
                ((FrescoImageView)holder.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(item.getLive_gift_logo()));
                ((TextView)holder.getView(R.id.mTvNum)).setText(item.getTotal()+"个");
                ((TextView)holder.getView(R.id.mTvName)).setText(item.getLive_gift_name());
                ((TextView)holder.getView(R.id.mTvMoney)).setText(HnUtils.setTwoPoint(item.getLive_gift_coin())+ HnApplication.getmConfig().getCoin());

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_user_home_gift;
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
        RequestParams params=new RequestParams();
        params.put("user_id",getIntent().getStringExtra("userId"));
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.USER_PROFILE_ANCHOR_GIFT;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnAnchorAcquireGiftModel>(HnAnchorAcquireGiftModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD()==null||model.getD().getItems() == null) {
                    setEmpty(HnUiUtils. getString(R.string.now_no_acquire_gift), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_acquire_gift), R.drawable.empty_com);
               HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_acquire_gift), R.drawable.empty_com);
            }
        };
    }
}
