package com.hotniao.live.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnStringUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.model.HnLookLiveLogModel;
import com.hotniao.livelibrary.util.DataTimeUtils;
import com.hotniao.live.utils.HnUiUtils;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 -- 收礼
 * 创建人：Kevin
 * 创建时间：2017/3/6 16:16
 * 修改人：Kevin
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillLookLiveAdapter extends BaseQuickAdapter<HnLookLiveLogModel.DBean.RecordListBean.ItemsBean, BaseViewHolder> {

    public HnBillLookLiveAdapter() {
        super(R.layout.item_bill_start_live_gift);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnLookLiveLogModel.DBean.RecordListBean.ItemsBean item) {

        //头像
//        ((FrescoImageView) helper.getView(R.id.fiv_avatar)).setImageURI(item.avatar);
        //昵称
        helper.setText(R.id.tv_fname, item.getAnchor().getUser_nickname());
        // 付费名称	直播类型，1：门票，2：计时
        if("2".equals(item.getLive().getAnchor_live_pay())){
            helper.setText(R.id.tv_pay_type, HnStringUtils.getString(R.string.minute_payed));
        }else if("1".equals(item.getLive().getAnchor_live_pay())) {
            helper.setText(R.id.tv_pay_type, HnStringUtils.getString(R.string.tickets_payed));
        }

        helper.setText(R.id.tv_num, item.getLive().getConsume()+ HnApplication.getmConfig().getCoin());
        //时间
        if(!TextUtils.isEmpty(item.getLive().getTime())){
            if(DataTimeUtils.IsToday(Long.parseLong(item.getLive().getTime()))){
                helper.setText(R.id.mTvDay,R.string.day);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getLive().getTime(),"HH:mm:ss"));
            }else if(DataTimeUtils.IsYesterday(Long.parseLong(item.getLive().getTime()))){
                helper.setText(R.id.mTvDay,R.string.yesteday);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getLive().getTime(),"HH:mm:ss"));
            }else {
                helper.setText(R.id.mTvDay,HnDateUtils.dateFormat(item.getLive().getTime(),"yyyy-MM-dd"));
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getLive().getTime(),"HH:mm:ss"));
            }
        }

    }

}

