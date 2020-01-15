package com.hotniao.svideo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnBillInviteDetailModel;
import com.hotniao.svideo.model.HnLiveBackEarnModel;

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
public class HnBillVideoChatEarnAdapter extends BaseQuickAdapter<HnLiveBackEarnModel.DBean.ItemsBean, BaseViewHolder> {

    public HnBillVideoChatEarnAdapter(List<HnLiveBackEarnModel.DBean.ItemsBean> mData) {
        super(R.layout.adapter_bill_invite_earning,mData);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnLiveBackEarnModel.DBean.ItemsBean item) {
        helper.setText(R.id.mTvDay, "与" + item.getUser_nickname() + HnUiUtils.getString(R.string.main_chat));

        helper.setText(R.id.mTvPrice, item.getConsume() + HnApplication.getmConfig().getDot());
        helper.setText(R.id.mTvTime, HnDateUtils.dateFormat(item.getTime(), "yyyy-MM-dd HH:mm"));

    }

}

