package com.hotniao.live.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.DateUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.model.HnLivePlayBackModel;
import com.hotniao.live.model.HnPlayBackModel;
import com.hotniao.live.utils.HnUiUtils;

import java.util.List;
import java.util.Locale;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserHomeBackAdapter extends BaseQuickAdapter<HnPlayBackModel.DBean.VideosBean.ItemsBean, BaseViewHolder> {
    public HnUserHomeBackAdapter(@Nullable List<HnPlayBackModel.DBean.VideosBean.ItemsBean> data) {
        super(R.layout.adapter_user_home_back, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnPlayBackModel.DBean.VideosBean.ItemsBean item) {
        ((FrescoImageView) helper.getView(R.id.mIvImg)).setController(FrescoConfig.getController(item.getImage_url()));
        if (TextUtils.isEmpty(item.getPlayback_price()) || 0 == Integer.parseInt(item.getPlayback_price())) {
            if (item.getCategory_name().size()>1&&"VIP".equals(item.getCategory_name().get(1))) {
                helper.getView(R.id.mTvPay).setVisibility(View.VISIBLE);
                helper.setText(R.id.mTvPay, "VIP");
            } else {
                helper.getView(R.id.mTvPay).setVisibility(View.VISIBLE);
                helper.setText(R.id.mTvPay, "免费");
            }

        } else {
            helper.getView(R.id.mTvPay).setVisibility(View.VISIBLE);
            helper.setText(R.id.mTvPay, item.getPlayback_price() + HnApplication.getmConfig().getCoin());
        }
        helper.setText(R.id.mTvTitle, item.getTitle());

        int progress = 0;
        try {
            progress = Integer.parseInt(item.getTime());
        } catch (Exception e) {
        }

        helper.setText(R.id.mTvCate, item.getCategory_name().get(0));
        helper.setText(R.id.mTvLong, String.format(Locale.CHINA, "%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, (progress % 3600) % 60));
        helper.setText(R.id.mTvLookNum, HnUtils.setNoPoint(item.getOnlines()) + HnUiUtils.getString(R.string.people_look));




    }
}
