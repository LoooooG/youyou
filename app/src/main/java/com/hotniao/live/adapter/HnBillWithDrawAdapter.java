package com.hotniao.live.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hotniao.live.R;
import com.hotniao.live.model.WithdrawLogModel;
import com.hotniao.livelibrary.util.DataTimeUtils;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：账单明细 -- 提现
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillWithDrawAdapter extends BaseQuickAdapter<WithdrawLogModel.DBean.WithdrawLogBean.ItemsBean, BaseViewHolder> {

    public HnBillWithDrawAdapter() {
        super(R.layout.item_bill_withdraw_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithdrawLogModel.DBean.WithdrawLogBean.ItemsBean item) {

        //时间
        if(!TextUtils.isEmpty(item.getTime())){
            if(DataTimeUtils.IsToday(Long.parseLong(item.getTime()))){
                helper.setText(R.id.mTvDay,R.string.day);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getTime(),"HH:mm:ss"));
            }else if(DataTimeUtils.IsYesterday(Long.parseLong(item.getTime()))){
                helper.setText(R.id.mTvDay,R.string.yesteday);
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getTime(),"HH:mm:ss"));
            }else {
                helper.setText(R.id.mTvDay,HnDateUtils.dateFormat(item.getTime(),"yyyy-MM-dd"));
                helper.setText(R.id.tv_ctime, HnDateUtils.dateFormat(item.getTime(),"HH:mm:ss"));
            }
        }

        /*金额*/
        TextView  mTvMoney=helper.getView(R.id.mTvMoney);
        String  money=item.getCash();
        mTvMoney.setText(money);

        helper.setText(R.id.mTvType,item.getPay()+":"+item.getAccount());

        /*状态 提现状态，Y：已提现，C：提现中，N：提现失败 */
        TextView  mTvState=helper.getView(R.id.mTvState);
        String  state=item.getStatus();
        if("C".equals(state)){//申请中
            mTvState.setText("提现中");
        }else  if("Y".equals(state)){//充值成功
            mTvState.setText("已提现");
        }else if("N".equals(state)){
            mTvState.setText("提现失败");//拒绝
        }

    }
}
