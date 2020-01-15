package com.hotniao.svideo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnInviteChatBeforeActivity;
import com.hotniao.svideo.activity.HnInviteChatPreviewActivity;
import com.hotniao.svideo.activity.HnUserHomeActivity;
import com.hotniao.svideo.model.HnHomeFastChatModel;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.ui.audience.activity.HnAudienceActivity;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
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
public class HnHomeChatAdapter extends BaseQuickAdapter<HnHomeFastChatModel.DBean.ItemsBean, BaseViewHolder> {

    public HnHomeChatAdapter(List<HnHomeFastChatModel.DBean.ItemsBean> mData) {
        super(R.layout.adapter_home_chat, mData);
    }

    public HnHomeChatAdapter() {
        super(R.layout.adapter_home_chat);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HnHomeFastChatModel.DBean.ItemsBean item) {
        ((FrescoImageView) helper.getView(R.id.mIvImag)).setController(FrescoConfig.getController(item.getUser_avatar()));
        ((FrescoImageView) helper.getView(R.id.iv_avatar)).setController(FrescoConfig.getController(item.getUser_avatar()));
        helper.setText(R.id.tv_price,String.format(HnUiUtils.getString(R.string.one_to_one_chat_video_pay_min), TextUtils.isEmpty(item.getAnchor_chat_price()) ? "0" : item.getAnchor_chat_price(), HnApplication.getmConfig().getCoin()));
//        在线
//        勿扰修改为在聊
//        不在线修改为勿扰
        helper.setText(R.id.mTvType, "");
        if ("0".equals(item.getAnchor_chat_status())) {// 0不在线 1勿扰 2空闲 3空闲
            helper.setBackgroundRes(R.id.mTvType, R.drawable.lixian);
//            helper.setText(R.id.mTvType, "离线");
        } else if ( "1".equals(item.getAnchor_chat_status())) {
            helper.setBackgroundRes(R.id.mTvType, R.drawable.lixian);
//            helper.setText(R.id.mTvType, "勿扰");
        } else if("2".equals(item.getAnchor_chat_status())){
            helper.setBackgroundRes(R.id.mTvType, R.drawable.shipinzhong);
//            helper.setText(R.id.mTvType, "视频中");
        }else {
            helper.setBackgroundRes(R.id.mTvType, R.drawable.zaixian);
//            helper.setText(R.id.mTvType, "在线");
        }
        helper.setText(R.id.mTvName, item.getUser_nickname());
        helper.setText(R.id.mTvSign, TextUtils.isEmpty(item.getUser_intro()) ? mContext.getString(R.string.he_hava_no_intro) : item.getUser_intro());
        HnSkinTextView mTvLv = helper.getView(R.id.mTvLv);
        HnLiveLevelUtil.setAnchorLevBg(mTvLv, item.getAnchor_level(), true);

        helper.getView(R.id.mLlClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if ("1".equals(item.getAnchor_chat_status())) {
//                    HnLiveListModel model = new HnLiveListModel();
//                    List<HnLiveListModel.LiveListBean> list = new ArrayList<HnLiveListModel.LiveListBean>();
//                    model.setPos(0);
//                    HnLiveListModel.LiveListBean bean = new HnLiveListModel.LiveListBean("",
//                            item.getUser_id(), item.getUser_avatar());
//                    list.add(bean);
//
//                    model.setList(list);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("data", model);
//                    mContext.startActivity(new Intent(mContext, HnAudienceActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(bundle));
//
//                } else {
//                    HnUserHomeActivity.luncher((Activity) mContext, item.getUser_id());
//                    HnInviteChatBeforeActivity.luncher((Activity) mContext, item.getUser_id(), item.getUser_avatar(), item.getAnchor_chat_status());
                    HnInviteChatPreviewActivity.launcher((Activity) mContext, item.getUser_id(), item.getUser_avatar(), item.getAnchor_chat_status());
//                }
            }
        });
    }


}
