package com.hotniao.svideo.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnInviteWithdrawLogModel;
import com.hotniao.svideo.model.HnRewardLogModel;
import com.hotniao.livelibrary.util.DataTimeUtils;

import java.util.List;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：我的推广
 * 创建人：Kevin
 * 创建时间：2017/3/6 16:16
 * 修改人：Kevin
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnInviteWithdrawLogAdapter extends BaseQuickAdapter<HnInviteWithdrawLogModel.DBean.ItemsBean, BaseViewHolder> {

    public HnInviteWithdrawLogAdapter(List<HnInviteWithdrawLogModel.DBean.ItemsBean> data) {
        super(R.layout.item_invite_withdraw_log,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, HnInviteWithdrawLogModel.DBean.ItemsBean item) {
        //时间
        String time = String.valueOf(item.getTime());
        if(!TextUtils.isEmpty(item.getTime()+"")){
            if(DataTimeUtils.IsToday(Long.parseLong(time))){
                helper.setText(R.id.mTvDay,R.string.day);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(time,"HH:mm:ss"));
            }else if(DataTimeUtils.IsYesterday(Long.parseLong(time))){
                helper.setText(R.id.mTvDay,R.string.yesteday);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(time,"HH:mm:ss"));
            }else {
                helper.setText(R.id.mTvDay,HnDateUtils.dateFormat(time,"yyyy-MM-dd"));
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(time,"HH:mm:ss"));
            }
        }

        helper.setText(R.id.tv_price, HnUtils.setTwoPoints(item.getCash()+"")+"元");

        String state = item.getStatus();
        if("C".equals(state)){//申请中
            helper.setText(R.id.tv_status,"提现中");
        }else  if("Y".equals(state)){//充值成功
            helper.setText(R.id.tv_status,"已提现");
        }else if("N".equals(state)){
            helper.setText(R.id.tv_status,"提现失败");
        }
    }

}

