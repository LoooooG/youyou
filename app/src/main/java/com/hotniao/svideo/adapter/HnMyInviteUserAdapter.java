package com.hotniao.svideo.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.model.HnExchangeDetailModel;
import com.hotniao.svideo.model.HnMyInviteUserModel;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：lianlian
 * 类描述：
 * 创建人：zjw
 * 创建时间：2019/1/10 10:26
 * 修改人：zjw
 * 修改时间：2019/1/10 10:26
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyInviteUserAdapter extends BaseQuickAdapter<HnMyInviteUserModel.DBean.ItemsBean, BaseViewHolder> {


    public HnMyInviteUserAdapter() {
        super(R.layout.item_my_invite_user);
    }

    @Override
    protected void convert(BaseViewHolder helper, HnMyInviteUserModel.DBean.ItemsBean item) {

        helper.setText(R.id.tv_name,item.getUser_nickname());
        FrescoImageView ivAvatar = helper.getView(R.id.fiv_header);
        ivAvatar.setController(FrescoConfig.getController(item.getUser_avatar()));
    }
}
