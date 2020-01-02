package com.hotniao.live.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hotniao.live.model.HnHomeSearchModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述： 搜索用户适配器
 * 创建人：刘龙龙
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class  HnSearchMatchAdapter extends BaseQuickAdapter<HnHomeSearchModel.DBean.ItemsBean, BaseViewHolder> {
    private String keyword = "";

    public HnSearchMatchAdapter() {
        super(R.layout.item_searchhistory);
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected void convert(BaseViewHolder helper, HnHomeSearchModel.DBean.ItemsBean dataBean) {

        helper.setText(R.id.tv_his, dataBean.getContent());
        TextView mTv = helper.getView(R.id.tv_his);
        mTv.setText(dataBean.getContent());
        if (!TextUtils.isEmpty(dataBean.getContent())) {
            int bstart = dataBean.getContent().indexOf(keyword);
            int bend = bstart + keyword.length();
            if (bstart > -1 && bend > -1) {
                SpannableString spannableString = new SpannableString(dataBean.getContent());
                spannableString.setSpan(new ForegroundColorSpan(HnUiUtils.getResources().getColor(R.color.main_color)), bstart, bend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                mTv.setText(spannableString);
            }
        }

    }
}