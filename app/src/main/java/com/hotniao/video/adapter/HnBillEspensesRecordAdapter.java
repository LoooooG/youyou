package com.hotniao.video.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnStringUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.model.HnEspensesRecordModel;
import com.hotniao.livelibrary.util.DataTimeUtils;

/**
 * Copyright (C) 2018,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：众赢
 * 类描述：
 * 创建人：李柯
 * 创建时间：2018/10/23 11:14
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBillEspensesRecordAdapter extends BaseQuickAdapter<HnEspensesRecordModel.DBean.ItemsBean,BaseViewHolder> {
    private static final String TAG = "HnBillEspensesRecordAdapter";
    private Context context;

    public HnBillEspensesRecordAdapter(Context context) {
        super(R.layout.item_expense_record);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HnEspensesRecordModel.DBean.ItemsBean item) {
        //昵称
        helper.setText(R.id.tv_nick_name, HnStringUtils.getString(R.string.send_ta)+item.getUser_nickname());
        //优币
        HnLogUtils.d(TAG,"item.getConsume():"+item.getConsume());
        String consume="";
        if (item.getConsume() != null) {
            consume=item.getConsume();
            if(consume.endsWith(".00")){
                consume = consume.substring(0, consume.indexOf("."));
            }
        }
        helper.setText(R.id.tv_coin_num, "-"+consume+HnApplication.getmConfig().getCoin());
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
    }
}
