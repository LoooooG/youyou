package com.hotniao.video.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hn.library.global.HnUrl;
import com.hotniao.video.model.bean.HnMyFocusBean;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述： 我的关注
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyFollowAdapter extends BaseQuickAdapter<HnMyFocusBean.FollowsBean.ItemsBean, BaseViewHolder> {

    public HnMyFollowAdapter() {
        super(R.layout.adapter_search_user);
    }


    @Override
    protected void convert(BaseViewHolder holder, HnMyFocusBean.FollowsBean.ItemsBean dataBean) {
        holder.addOnClickListener(R.id.mTvFocus);

        ((FrescoImageView) holder.getView(R.id.fiv_header)).setController(FrescoConfig.getController(dataBean.getUser_avatar()));
        ((TextView) holder.getView(R.id.tv_name)).setText(dataBean.getUser_nickname());
        ((TextView) holder.getView(R.id.tv_des)).setText(HnUiUtils.getString(R.string.fans_m) +dataBean.getUser_fans_total());
        //用户等级
        HnSkinTextView tvUserLevel=holder.getView(R.id.tv_user_level);
        HnLiveLevelUtil.setAudienceLevBg(tvUserLevel,dataBean.getUser_level(),true);
        final TextView mTvFocus = holder.getView(R.id.mTvFocus);
        if ("Y".equals(dataBean.getIs_follow())) {
            mTvFocus.setText(R.string.main_follow_on);
            mTvFocus.setSelected(false);
        } else {
            mTvFocus.setText(R.string.add_follow);
            mTvFocus.setSelected(true);
        }

    }
}