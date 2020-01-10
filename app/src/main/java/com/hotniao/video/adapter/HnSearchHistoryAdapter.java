package com.hotniao.video.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hotniao.video.R;
/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：搜索历史
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public HnSearchHistoryAdapter() {
        super(R.layout.item_searchhistory);
    }

    @Override
    protected void convert(BaseViewHolder helper, String dataBean) {

        helper.setText(R.id.tv_his,dataBean);
    }

}
