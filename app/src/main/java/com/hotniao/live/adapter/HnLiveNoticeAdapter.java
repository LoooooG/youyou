package com.hotniao.live.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.model.bean.HnLiveNoticeBean;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：开播提醒Adapter
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveNoticeAdapter extends BaseQuickAdapter<HnLiveNoticeBean.FollowsBean.ItemsBean, BaseViewHolder> {


    public HnLiveNoticeAdapter(List<HnLiveNoticeBean.FollowsBean.ItemsBean> data) {
        super(R.layout.adapter_live_notice,data);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final HnLiveNoticeBean.FollowsBean.ItemsBean item) {
        holder.addOnClickListener(R.id.mTvNotice);
        ((FrescoImageView) holder.getView(R.id.fiv_header)).setController(FrescoConfig.getController(item.getUser_avatar()));
        ((TextView) holder.getView(R.id.tv_name)).setText(item.getUser_nickname());
        ((TextView) holder.getView(R.id.tv_des)).setText(TextUtils.isEmpty(item.getUser_intro())?mContext.getString(R.string.no_something):item.getUser_intro());
        //用户等级
        HnSkinTextView tvUserLevel=holder.getView(R.id.tv_user_level);
        HnLiveLevelUtil.setAudienceLevBg(tvUserLevel,item.getUser_level(),true);

        final TextView mTvNotice = holder.getView(R.id.mTvNotice);
        if ("Y".equals(item.getIs_remind())) {
            mTvNotice.setSelected(true);
        } else if ("N".equals(item.getIs_remind())) {
            mTvNotice.setSelected(false);
        }






    }


}
