package com.hotniao.svideo.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.activity.HnChatVideoDetailAcrivity;
import com.hotniao.svideo.activity.HnPrivateChatActivity;
import com.hotniao.svideo.model.HnChatRoomIdModel;
import com.hotniao.svideo.model.HnFastVideoListModel;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我的视频聊天adapter
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMineChatVideoAdapter extends BaseQuickAdapter<HnFastVideoListModel.DBean.ItemsBean, BaseViewHolder> {
    public HnMineChatVideoAdapter(@Nullable List<HnFastVideoListModel.DBean.ItemsBean> data) {
        super(R.layout.adapter_mine_chat_video, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HnFastVideoListModel.DBean.ItemsBean item) {
        boolean isMeInviter = false;
        if (HnApplication.getmUserBean().getUser_id().equals(item.getInviter_id()))
            isMeInviter = true;
        else isMeInviter = false;

        ((FrescoImageView) helper.getView(R.id.mIvImg)).setController(FrescoConfig.getHeadController(isMeInviter ? item.getInvitee_avatar() : item.getUser_avatar()));
        helper.setText(R.id.mTvName, isMeInviter ? item.getInvitee_nickname() : item.getUser_nickname());
        helper.setText(R.id.mTvTime, HnDateUtils.stampToDateMm(item.getUpdate_time()));

        final boolean finalIsMeInviter = isMeInviter;
        helper.getView(R.id.mRlClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HnChatVideoDetailAcrivity.luncher(mContext, finalIsMeInviter ? item.getInvitee_nickname() : item.getUser_nickname(),
                        item.getId());
            }
        });

        helper.getView(R.id.mIvImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("user_id", finalIsMeInviter ? item.getInvitee_id() : item.getInviter_id());
                HnHttpUtils.postRequest(HnUrl.GET_CHAT_ROOM_ID, params, HnUrl.GET_CHAT_ROOM_ID, new HnResponseHandler<HnChatRoomIdModel>(HnChatRoomIdModel.class) {
                    @Override
                    public void hnSuccess(String response) {
                        if (mContext == null || model.getD() == null || TextUtils.isEmpty(model.getD().getRoom_id()))
                            return;

                        Bundle bundle = new Bundle();
                        bundle.putString(HnConstants.Intent.DATA, finalIsMeInviter ? item.getInvitee_id() : item.getInviter_id());
                        bundle.putString(HnConstants.Intent.Name, finalIsMeInviter ? item.getInvitee_nickname() : item.getUser_nickname());
                        bundle.putString(HnConstants.Intent.ChatRoomId, model.getD().getRoom_id());
                        mContext.startActivity(new Intent(mContext, HnPrivateChatActivity.class).putExtras(bundle));
                    }

                    @Override
                    public void hnErr(int errCode, String msg) {
                        HnToastUtils.showToastShort(msg);
                    }
                });

            }
        });

    }
}
