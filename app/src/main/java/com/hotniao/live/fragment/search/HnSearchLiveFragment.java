package com.hotniao.live.fragment.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hotniao.live.base.CommListFragment;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hotniao.live.eventbus.HnSearchEvent;
import com.hotniao.live.model.HnHomeHotModel;
import com.hotniao.live.model.bean.HnHomeHotBean;
import com.hn.library.utils.HnRefreshDirection;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveSwitchDataUitl;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：搜索直播
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSearchLiveFragment extends CommListFragment {

    private CommRecyclerAdapter mAdapter;
    private List<HnHomeHotBean.ItemsBean> mData = new ArrayList<>();
    private String mKeyWords;

    public static HnSearchLiveFragment getInstance() {
        HnSearchLiveFragment f = new HnSearchLiveFragment();
        return f;
    }

    @Override
    protected String setTAG() {
        return HnUiUtils.getString(R.string.live);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        EventBus.getDefault().register(this);
        setGridManager(true);
        mKeyWords = getArguments().getString("keyWord");

        mLoadingLayout.setStatus(HnLoadingLayout.Success);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getAnchor_live_img()));
                //直播模式  vip/计时/门票
                ImageView mIvType = holder.getView(R.id.mIvType);
                String live_tyoe = mData.get(position).getAnchor_live_pay();
                if ("0".equals(live_tyoe)) {
                    mIvType.setImageResource(R.drawable.icon_free);
                } else if ("1".equals(live_tyoe)) {
                    mIvType.setImageResource(R.drawable.icon_vip);
                } else if ("2".equals(live_tyoe) || "3".equals(live_tyoe)) {
                    mIvType.setImageResource(R.drawable.icon_pay);
                } else {
                    mIvType.setImageResource(R.drawable.icon_free);
                }
                //标题
                ((TextView) holder.getView(R.id.mTvTitle)).setText(mData.get(position).getUser_nickname());
                ((TextView) holder.getView(R.id.mTvNum)).setText(mData.get(position).getAnchor_live_onlines());
                if (!"0".equals(mData.get(position).getAnchor_game_category_id())) {
                    ((FrescoImageView) holder.getView(R.id.mIvGameLogo)).setController(FrescoConfig.getController(mData.get(position).getAnchor_game_category_logo()));
                    holder.getView(R.id.mIvGameLogo).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.mIvGameLogo).setVisibility(View.GONE);
                }

                holder.getView(R.id.mIvImg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HnLiveSwitchDataUitl.joinRoom(mActivity, mData.get(position).getAnchor_category_id(), mData.get(position).getAnchor_live_pay(), mData.get(position).getUser_id(), mData.get(position).getAnchor_game_category_id());
                    }
                });
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_search_live;
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
        mKeyWords = TextUtils.isEmpty(mKeyWords) ? getArguments().getString("keyWord") : mKeyWords;
        params.put("kw", mKeyWords);
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.SEARCH_LIVE;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnHomeHotModel>(HnHomeHotModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();
                if (model.getD() == null) return;
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_search_data), R.drawable.home_no_search);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_search_data), R.drawable.home_no_search);
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(HnSearchEvent event) {
        if (!TextUtils.isEmpty(event.getKey())) {
            mKeyWords = event.getKey();
            getData(HnRefreshDirection.TOP, 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
