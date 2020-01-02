package com.hotniao.live.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.model.HnVideoModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我的界面小视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMineVideoAdapter extends BaseQuickAdapter<HnVideoModel.DBean.ItemsBean,BaseViewHolder>{
    public HnMineVideoAdapter( @Nullable List<HnVideoModel.DBean.ItemsBean> data) {
        super(R.layout.adapter_mine_video, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnVideoModel.DBean.ItemsBean item) {
        helper.addOnClickListener(R.id.mIvMore).addOnClickListener(R.id.mRlClick);
        ((FrescoImageView)helper.getView(R.id.mIvImg)).setController(FrescoConfig.getController(item.getCover()));
        helper.setText(R.id.mTvTitle,item.getTitle());
        helper.setText(R.id.mTvCate,item.getCategory_name());
        helper.setText(R.id.mTvTime,  HnDateUtils.stampToDateMm(item.getCreate_time()));
        helper.setText(R.id.mTvLong,  HnDateUtils.getMinute(item.getDuration()));
        helper.setText(R.id.mTvCommNum, HnUtils.setNoPoint(item.getReply_num()));
        helper.setText(R.id.mTvZanNum,  HnUtils.setNoPoint(item.getLike_num()));



        if (TextUtils.isEmpty(item.getPrice())) {
            item.setNeedPay(false);
            helper.setText(R.id.mTvType, R.string.donot_pay_coin);
        } else {
            try {
                int price = Integer.parseInt(item.getPrice());
                if (0 < price) {
                    item.setNeedPay(true);
                    helper.setText(R.id.mTvType, price + HnApplication.getmConfig().getCoin());
                } else {
                    item.setNeedPay(false);
                    helper.setText(R.id.mTvType, R.string.donot_pay_coin);
                }

            } catch (Exception e) {
                item.setNeedPay(false);
                helper.setText(R.id.mTvType, R.string.donot_pay_coin);
            }
        }
    }
}
