package com.hotniao.svideo.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnExchangeDetailModel;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：lianlian
 * 类描述：
 * 创建人：zjw
 * 创建时间：2019/1/10 10:26
 * 修改人：zjw
 * 修改时间：2019/1/10 10:26
 * 修改备注：
 * Version:  1.0.0
 */
public class HnExchangeDetailAdapter extends BaseQuickAdapter<HnExchangeDetailModel.DBean.ItemsBean, BaseViewHolder> {


    public HnExchangeDetailAdapter() {
        super(R.layout.item_spread_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnExchangeDetailModel.DBean.ItemsBean item) {

        String time = item.getAdd_time()+"";
        if (!TextUtils.isEmpty(time)) {
            helper.setText(R.id.tv_user,HnDateUtils.dateFormat(time,"yyyy-MM-dd"));
            helper.setText(R.id.tv_time, HnDateUtils.dateFormat(time,"HH:mm:ss"));
        }

        TextView tvType = helper.getView(R.id.tv_type);
        tvType.setText(item.getExchange_coin()+ HnApplication.getmConfig().getCoin());

        TextView tvMoney = helper.getView(R.id.tv_money);
        tvMoney.setText(HnUtils.setTwoPoints(item.getExchange_money()+"") + "元");

    }
}
