package com.hotniao.live.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.model.HnBillInviteDetailModel;
import com.hotniao.live.model.HnLiveBackEarnModel;

import java.util.List;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：邀请收益
 * 创建人：Kevin
 * 创建时间：2017/3/6 16:16
 * 修改人：Kevin
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillVideoShowEarnAdapter extends BaseQuickAdapter<HnLiveBackEarnModel.DBean.ItemsBean, BaseViewHolder> {

    public HnBillVideoShowEarnAdapter(List<HnLiveBackEarnModel.DBean.ItemsBean> mData) {
        super(R.layout.adapter_bill_invite_earning, mData);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnLiveBackEarnModel.DBean.ItemsBean item) {
        helper.setText(R.id.mTvDay, item.getUser_nickname() + "观看" + HnUiUtils.getString(R.string.video_show));

        helper.setText(R.id.mTvPrice, item.getConsume() + HnApplication.getmConfig().getDot());
        helper.setText(R.id.mTvTime, HnDateUtils.dateFormat(item.getTime(), "yyyy-MM-dd HH:mm"));

    }

}

