package com.hotniao.svideo.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：单独的
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnPhotoAdapter extends BaseQuickAdapter<String ,BaseViewHolder> {
    public HnPhotoAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_photo,data);
    }

    public HnPhotoAdapter(List<String> data, String circle) {
        super(R.layout.adapter_photo_circle, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ((FrescoImageView)helper.getView(R.id.mIvImage)).setController(FrescoConfig.getHeadController(item));

    }
}
