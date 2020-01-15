package com.hotniao.svideo.adapter;

import android.content.Context;

import com.alibaba.android.arouter.utils.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnStringUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnReceiveGiftLogModel;
import com.nostra13.universalimageloader.utils.L;

import java.util.List;


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
public class HnBillReceiveAdapter extends BaseQuickAdapter<HnReceiveGiftLogModel.DBean.RecordListBean.ItemsBean, BaseViewHolder> {

    public HnBillReceiveAdapter(List<HnReceiveGiftLogModel.DBean.RecordListBean.ItemsBean> data) {
        super(R.layout.adapter_bill_receive_gift,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, HnReceiveGiftLogModel.DBean.RecordListBean.ItemsBean item) {

        //头像
        //昵称
        helper.setText(R.id.mTvPrice, item.getGift().getConsume()+ HnApplication.getmConfig().getDot());
        //礼物名
        helper.setText(R.id.mTvContent, HnStringUtils.getString(R.string.receive) +item.getUser().getUser_nickname()+"的"+ item.getGift().getLive_gift_name()+"X" + item.getGift().getLive_gift_number());

        //时间
        String add_time = item.getGift().getTime();
        if(!TextUtils.isEmpty(add_time)){
            helper.setText(R.id.mTvTime, HnDateUtils.dateFormat(add_time,"yyyy-MM-dd HH:mm"));
        }else {
            helper.setText(R.id.mTvTime, "");
        }

    }

}

