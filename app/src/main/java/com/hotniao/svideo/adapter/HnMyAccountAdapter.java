package com.hotniao.svideo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnMyRechargeActivity;
import com.hotniao.svideo.model.bean.HnProfileBean;
import com.hotniao.svideo.utils.HnUiUtils;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：我的账号之充值列表适配器
 * 创建人：mj
 * 创建时间：2017/9/8 18:15
 * 修改人：Administrator
 * 修改时间：2017/9/8 18:15
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyAccountAdapter extends BaseQuickAdapter<HnProfileBean.RechargeComboBean,BaseViewHolder> {

    private HnMyRechargeActivity context;

    public HnMyAccountAdapter(HnMyRechargeActivity context) {
        this(R.layout.item_my_account_layout, null);
        this.context=context;

    }    public HnMyAccountAdapter(@LayoutRes int layoutResId, @Nullable List<HnProfileBean.RechargeComboBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HnProfileBean.RechargeComboBean item) {

        //优币
        String u_bi=item.getRecharge_combo_coin();
        TextView tvUBi=helper.getView(R.id.tv_bi);
        tvUBi.setText(u_bi+ HnApplication.getmConfig().getCoin() );



        //金额
        String money=item.getRecharge_combo_fee();
        TextView tvMoney=helper.getView(R.id.tv_money);
        tvMoney.setText("¥ "+money);

        if (!TextUtils.isEmpty(money)) {
            //为TextView设置不同的字体大小和颜色
            String s = "¥";
            String[] split =money.split("\\.");
            SpannableString styledText = new SpannableString("¥" + money);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.textSize12), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.textSize16), s.length(), split[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.textSize12), s.length() + split[0].length(), s.length() +money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMoney.setText(styledText, TextView.BufferType.SPANNABLE);
        } else {
            tvMoney.setText("¥0");
        }
        //是否被选中
        boolean choose=item.isChoose();
        if(choose){
            if(context.mIsDay){
                tvMoney.setBackgroundResource(R.drawable.shape_violet_bg_violet_stroke_recentage);
            }else {
                tvMoney.setBackgroundResource(R.drawable.shape_violet_bg_violet_stroke_recentage);
            }
            tvMoney.setTextColor(context.getResources().getColor(R.color.comm_text_color_main));

        }else{
            if(context.mIsDay){
                tvMoney.setBackgroundResource(R.drawable.shape_white_bg_black_stroke_recentage);
                tvMoney.setTextColor(context.getResources().getColor(R.color.comm_text_color_black_hs));
            }else {
                tvMoney.setBackgroundResource(R.drawable.shape_violet_bg_violet_stroke_recentage_dark);
                tvMoney.setTextColor(context.getResources().getColor(R.color.white));
            }

        }



        helper.addOnClickListener(R.id.mRlClick);

    }
}
