package com.hotniao.svideo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnMyExchangeActivity;
import com.hotniao.svideo.model.HnExchangeModel;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：我的账号之充值列表适配器
 * 创建人：mj
 * 创建时间：2017/9/8 18:15
 * 修改人：Administrator
 * 修改时间：2017/9/8 18:15
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyExchangeAdapter extends BaseQuickAdapter<HnExchangeModel.DBean.ItemsBean, BaseViewHolder> {

    private HnMyExchangeActivity context;

    public HnMyExchangeAdapter(HnMyExchangeActivity context) {
        this(R.layout.item_my_exchange_layout, null);
        this.context = context;

    }

    public HnMyExchangeAdapter(@LayoutRes int layoutResId, @Nullable List<HnExchangeModel.DBean.ItemsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HnExchangeModel.DBean.ItemsBean item) {

        helper.setText(R.id.tv_bi, item.getCoin() + HnApplication.getmConfig().getCoin());
        helper.setText(R.id.tv_money, item.getMoney() + "元兑");


        helper.addOnClickListener(R.id.mRlClick);

    }
}
