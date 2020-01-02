package com.hotniao.live.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnStringUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.model.HnSendGiftLogModel;
import com.hotniao.livelibrary.util.DataTimeUtils;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 -- 送礼
 * 创建人：Kevin
 * 创建时间：2017/3/6 16:16
 * 修改人：Kevin
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillSendAdapter extends BaseQuickAdapter<HnSendGiftLogModel.DBean.RecordListBean.ItemsBean, BaseViewHolder> {

    private Context  context;

    public HnBillSendAdapter(Context  context) {
        super(R.layout.item_bill_receive_gift);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HnSendGiftLogModel.DBean.RecordListBean.ItemsBean item) {

        //头像
        ((FrescoImageView) helper.getView(R.id.mIvGift)).setController(FrescoConfig.getController(item.getGift().getLive_gift_logo()));
        //昵称
        helper.setText(R.id.tv_fname, HnStringUtils.getString(R.string.send_ta)+item.getAnchor().getUser_nickname());
        //礼物名
        helper.setText(R.id.tv_gift_name, item.getGift().getLive_gift_name()+"X" + item.getGift().getLive_gift_number());
        //优币
        helper.setText(R.id.tv_coin_num,HnUtils.setTwoPoints(item.getGift().getConsume())+ HnApplication.getmConfig().getCoin());
        //时间
        if(!TextUtils.isEmpty(item.getGift().getTime())){
            if(DataTimeUtils.IsToday(Long.parseLong(item.getGift().getTime()))){
                helper.setText(R.id.mTvDay,R.string.day);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getGift().getTime(),"HH:mm:ss"));
            }else if(DataTimeUtils.IsYesterday(Long.parseLong(item.getGift().getTime()))){
                helper.setText(R.id.mTvDay,R.string.yesteday);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getGift().getTime(),"HH:mm:ss"));
            }else {
                helper.setText(R.id.mTvDay,HnDateUtils.dateFormat(item.getGift().getTime(),"yyyy-MM-dd"));
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getGift().getTime(),"HH:mm:ss"));
            }
        }


    }

}
