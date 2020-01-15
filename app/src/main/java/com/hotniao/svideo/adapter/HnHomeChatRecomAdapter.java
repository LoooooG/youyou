package com.hotniao.svideo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnHomeHotAnchorChatModel;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：首页 约聊
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeChatRecomAdapter extends BaseQuickAdapter<HnHomeHotAnchorChatModel.DBean.AnchorBean, BaseViewHolder> {

    public HnHomeChatRecomAdapter(List<HnHomeHotAnchorChatModel.DBean.AnchorBean> mData) {
        super(R.layout.adapter_home_chat_recom,mData);
    }

    @Override
    protected void convert(BaseViewHolder helper,HnHomeHotAnchorChatModel.DBean.AnchorBean item) {
        ((FrescoImageView)helper.getView(R.id.mIvImg)).setController(FrescoConfig.getController(item.getUser_avatar()));

    }


}
