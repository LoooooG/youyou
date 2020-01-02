package com.hotniao.live.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnStringUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.model.HnStartLiveLogModel;
import com.hotniao.live.utils.HnUiUtils;

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
public class HnBillStartLiveAdapter extends BaseQuickAdapter<HnStartLiveLogModel.DBean.RecordListBean.ItemsBean, BaseViewHolder> {

    public HnBillStartLiveAdapter( List<HnStartLiveLogModel.DBean.RecordListBean.ItemsBean> mData) {
        super(R.layout.adapter_bill_receive_gift,mData);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnStartLiveLogModel.DBean.RecordListBean.ItemsBean item) {

        //头像
        //昵称
        // 	直播类型，1：门票，2：计时
        if("2".equals(item.getLive().getAnchor_live_pay())){
            helper.setText(R.id.mTvContent, item.getUser().getUser_nickname()+"-"+HnStringUtils.getString(R.string.minute_pay));
        }else {
            helper.setText(R.id.mTvContent,item.getUser().getUser_nickname()+"-"+ HnStringUtils.getString(R.string.tickets_pay));
        }
        String name= HnUtils.getDot();
        helper.setText(R.id.mTvPrice, item.getLive().getConsume()+  HnApplication.getmConfig().getDot());
        //时间
        helper.setText(R.id.mTvTime, HnDateUtils.dateFormat(item.getLive().getTime(),"yyyy-MM-dd HH:mm"));


    }

}

