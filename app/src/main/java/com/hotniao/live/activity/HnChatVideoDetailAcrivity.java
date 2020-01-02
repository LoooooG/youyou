package com.hotniao.live.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.base.CommListActivity;
import com.hotniao.live.biz.chat.HnFastChatBiz;
import com.hotniao.live.model.HnFastChatDetailModel;
import com.hotniao.live.model.HnMyAdminModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.bean.PrivateChatBean;
import com.hotniao.livelibrary.util.DataTimeUtils;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：约聊详情
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnChatVideoDetailAcrivity extends CommListActivity {

    private CommRecyclerAdapter mAdapter;
    private List<HnFastChatDetailModel.DBean.ItemsBean> mData = new ArrayList<>();


    public static void luncher(Context activity, String name, String chatId) {
        activity.startActivity(new Intent(activity, HnChatVideoDetailAcrivity.class).putExtra("name", name).putExtra("chatId", chatId));
    }

    @Override
    protected String setTitle() {
        return getIntent().getStringExtra("name");
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        if (HnApplication.getmUserBean() == null) HnUserControl.getProfile(null);

        mRecycler.setBackgroundColor(0xfff0f0f6);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                HnFastChatDetailModel.DBean.ItemsBean item = mData.get(position);
                //设置时间
                TextView mTvTime = (TextView) holder.getView(R.id.mTvTime);
                if (mData.size() > 1) {
                    if (position == 0) {
                        mTvTime.setText(DataTimeUtils.getTimestampString(Long.valueOf(item.getCreate_time()) * 1000));
                        mTvTime.setVisibility(View.VISIBLE);
                    } else {
                        HnFastChatDetailModel.DBean.ItemsBean prevMessage = mData.get(position - 1);
                        if (prevMessage != null && DataTimeUtils.isCloseEnough(Long.valueOf(item.getCreate_time()) * 1000, Long.valueOf(prevMessage.getCreate_time()) * 1000)) {
                            mTvTime.setVisibility(View.GONE);
                        } else {
                            mTvTime.setText(DataTimeUtils.getTimestampString(Long.valueOf(item.getCreate_time()) * 1000));
                            mTvTime.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    //只有一条消息必须显示
                    mTvTime.setVisibility(View.VISIBLE);
                    mTvTime.setText(DataTimeUtils.getTimestampString(Long.valueOf(item.getCreate_time()) * 1000));
                }


                if (!HnApplication.getmUserBean().getUser_id().equals(item.getInviter_id())) {
                    holder.getView(R.id.mLlLeft).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mLlRight).setVisibility(View.GONE);
                    ((FrescoImageView) holder.getView(R.id.mIvLeftHead)).setController(FrescoConfig.getHeadController(item.getUser_avatar()));
                    setAdapterItemStatue((HnSkinTextView) holder.getView(R.id.mTvLeftContent), item, false);
                } else {
                    holder.getView(R.id.mLlLeft).setVisibility(View.GONE);
                    holder.getView(R.id.mLlRight).setVisibility(View.VISIBLE);
                    ((FrescoImageView) holder.getView(R.id.mIvRightHead)).setController(FrescoConfig.getHeadController(item.getUser_avatar()));
                    setAdapterItemStatue((HnSkinTextView) holder.getView(R.id.mTvRightContent), item, true);
                }
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_chat_video_detail;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        return mAdapter;
    }

    private void setAdapterItemStatue(HnSkinTextView mTvStatus, HnFastChatDetailModel.DBean.ItemsBean item, boolean isMe) {
        //0正在发送中 1已取消 2对方拒绝 3对方忙线中 4接通 5对方无应答
        String msg = "";
        if ("0".equals(item.getStatus())) {
        } else if ("1".equals(item.getStatus())) {
            if (isMe) {
                msg = "已取消";
            } else {
                msg = "对方已取消";
            }
            mTvStatus.setLeftDrawable(R.drawable.duihuaship);
        } else if ("2".equals(item.getStatus())) {
            if (isMe) {
                msg = "对方已拒绝";
            } else {
                msg = "已拒绝";
            }
            mTvStatus.setLeftDrawable(R.drawable.duihuaship);
        } else if ("3".equals(item.getStatus())) {
            if (isMe) {
                msg = "对方忙线中";
            } else {
                msg = "已取消";
            }
            mTvStatus.setLeftDrawable(R.drawable.duihuaship);
        } else if ("4".equals(item.getStatus())) {
            mTvStatus.setCompoundDrawables(null, null, null, null);
            msg = "通话时长   " + HnDateUtils.getMinute(item.getDuration());
        } else if ("5".equals(item.getStatus())) {
            if (isMe) {
                msg = "对方无应答";
            } else {
                msg = "已取消";
            }
            mTvStatus.setLeftDrawable(R.drawable.duihuaship);
        }
        mTvStatus.setText(msg);
    }

    @Override
    protected RequestParams setRequestParam() {
        RequestParams params = new RequestParams();
        params.put("dialog_id", getIntent().getStringExtra("chatId"));
        params.put("page", page + "");
        params.put("pagesize", 20 + "");
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.LIVE_ANCHOR_CHAT_DIALOG_DETAIL;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnFastChatDetailModel>(HnFastChatDetailModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (model.getD().getItems() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_chat_video), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_chat_video), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_chat_video), R.drawable.empty_com);
            }
        };
    }


}
