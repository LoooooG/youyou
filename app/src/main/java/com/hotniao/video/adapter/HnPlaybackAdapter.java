package com.hotniao.video.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnAnchorRelatedActivity;
import com.hotniao.video.activity.HnInviteChatPreviewActivity;
import com.hotniao.video.activity.HnPlayBackVideoActivity;
import com.hotniao.video.model.HnHomeFastChatModel;
import com.hotniao.video.model.HnPlayBackModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.Date;
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
public class HnPlaybackAdapter extends BaseQuickAdapter<HnPlayBackModel.DBean.VideosBean.ItemsBean, BaseViewHolder> {

    public HnPlaybackAdapter(List<HnPlayBackModel.DBean.VideosBean.ItemsBean> mData) {
        super(R.layout.adapter_playback, mData);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HnPlayBackModel.DBean.VideosBean.ItemsBean item) {
        ((FrescoImageView) helper.getView(R.id.mIvImag)).setController(FrescoConfig.getController(item.getImage_url()));
        helper.setText(R.id.tv_num,HnUtils.setNoPoint(item.getOnlines()));
        int type = item.getType();
        helper.setBackgroundRes(R.id.mTvType, R.drawable.fufeibg);
        if(type == 0){
            helper.setText(R.id.mTvType,"免费");
        }else if(type == 1){
            helper.setText(R.id.mTvType,"VIP");
        }else {
            helper.setText(R.id.mTvType,item.getPlayback_price() + "金币");
        }


        helper.setText(R.id.mTvName, item.getNickname());

        String releaseTime = item.getPublic_time();
        if(!TextUtils.isEmpty(releaseTime)){
            if(HnApplication.getmUserBean().getUser_id().equals(item.getUser_id())){
                helper.setGone(R.id.tv_date,true);
                helper.setText(R.id.tv_date,HnUtils.dateFormatYMD(new Date(Long.parseLong(item.getPublic_time()) * 1000)));
            }else{
                helper.setGone(R.id.tv_date,false);
            }
        }

    }


}
