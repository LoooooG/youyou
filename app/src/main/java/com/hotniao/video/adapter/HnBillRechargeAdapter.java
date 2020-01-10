package com.hotniao.video.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.model.PayLogModel;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 -- 充值
 * 创建人：Kevin
 * 创建时间：2017/3/6 16:16
 * 修改人：Kevin
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillRechargeAdapter extends BaseQuickAdapter<PayLogModel.DBean.RechargeListBean.ItemsBean, BaseViewHolder> {


    public HnBillRechargeAdapter() {
        super(R.layout.item_bill_recharge_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayLogModel.DBean.RechargeListBean.ItemsBean item) {
         /*时间*/
        TextView tvTime = helper.getView(R.id.tv_time);
        String time = item.getTime();
        if (!TextUtils.isEmpty(time)) {
            String showTime = HnDateUtils.getTime("yyyy-MM-dd HH:mm", time);
            tvTime.setText(showTime);
        }
        /*金额*/
        TextView tvMoney = helper.getView(R.id.tv_coin);
        String money = item.getCoin();
        tvMoney.setText(money + HnApplication.getmConfig().getCoin());

        /*状态 Y：成功，N：失败，C：充值中 */
        TextView tvState = helper.getView(R.id.tv_status);
        String state = item.getStatus();
        if ("C".equals(state)) {//充值中
            tvState.setText("充值中");
        } else if ("Y".equals(state)) {//充值成功
            tvState.setText("充值成功");
        } else {//充值失败
            tvState.setText("充值失败");
        }

    }
}
