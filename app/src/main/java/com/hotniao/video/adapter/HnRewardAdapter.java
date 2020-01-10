package com.hotniao.video.adapter;

import com.alibaba.android.arouter.utils.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnStringUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.model.HnReceiveGiftLogModel;
import com.hotniao.video.model.HnRewardLogModel;

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
public class HnRewardAdapter extends BaseQuickAdapter<HnRewardLogModel.DBean.ItemsBean, BaseViewHolder> {

    public HnRewardAdapter(List<HnRewardLogModel.DBean.ItemsBean> data) {
        super(R.layout.item_reward_log,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, HnRewardLogModel.DBean.ItemsBean item) {
        helper.setText(R.id.tv_name,item.getInvitee_user_nickname());
        helper.setText(R.id.tv_price, item.getConsume()+ HnApplication.getmConfig().getDot());
        String type = item.getUser_invite_reward_type();
        if("register".equals(type)){
            helper.setText(R.id.tv_type,"注册");
        }else if("recharge".equals(type)){
            helper.setText(R.id.tv_type,"充值");
        }else if("vip".equals(type)){
            helper.setText(R.id.tv_type,"充值");
        }else if("withdraw".equals(type)){
            helper.setText(R.id.tv_type,"提现");
        }
    }

}

