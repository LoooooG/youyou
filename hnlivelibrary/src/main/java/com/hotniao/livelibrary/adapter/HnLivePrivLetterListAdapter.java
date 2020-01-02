package com.hotniao.livelibrary.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.hotniao.livelibrary.util.HnLiveDateUtils;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：私信模块Adapter
 * 创建人：阳石柏
 * 创建时间：2017/7/5 9:40
 * 修改人：阳石柏
 * 修改时间：2017/7/5 9:40
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLivePrivLetterListAdapter extends BaseQuickAdapter<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean, BaseViewHolder> {


    private Context context;

    public HnLivePrivLetterListAdapter(Context context) {
        super(R.layout.live_item_private_list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean) {

        //头像
        FrescoImageView mIvHeader = (FrescoImageView) helper.getView(R.id.iv_header);
        TextView mTvName = (TextView) helper.getView(R.id.tv_name);
        TextView mTvLevel = (TextView) helper.getView(R.id.tv_level);
        TextView mTvContent = (TextView) helper.getView(R.id.tv_content);
        TextView mTvTime = (TextView) helper.getView(R.id.tv_time);
        //性别
        ImageView mIvOffi = (ImageView) helper.getView(R.id.iv_offi);
        ImageView mIsVip = (ImageView) helper.getView(R.id.iv_withdrawalsuccessful);
        //消息数据
        TextView mTvNewMsg = (TextView) helper.getView(R.id.tv_new_msg);
        TextView mTvTag = (TextView) helper.getView(R.id.tv_show_tag);

        String uid = bean.getUser_id();
        if ("0".equals(uid)) {//系统消息
            mIvHeader.setImageURI("res:///" + R.mipmap.sys_msg_icon);
            //昵称
            mTvName.setText(R.string.live_system_msg);
            //等级
            mTvLevel.setVisibility(View.GONE);
            //性别
            mIvOffi.setVisibility(View.GONE);

            mIsVip.setVisibility(View.GONE);
        } else {//私聊列表
            //头像
            String avator = bean.getUser_avatar();
            mIvHeader.setController(FrescoConfig.getController(avator));
            //昵称
            mTvName.setText(bean.getUser_nickname());
            //用户等级
            mTvLevel.setVisibility(View.GONE);
            //性别
            mIvOffi.setVisibility(View.GONE);
            //vip
            mIsVip.setVisibility(View.GONE);
        }

        //内容
        mTvContent.setText(bean.getContent());
        //时间
        String update_time = bean.getTime();
        if (!TextUtils.isEmpty(update_time)) {
            long l = Long.parseLong(update_time);
            String time = HnUtils.getDateToString_4(l);
            String data = HnLiveDateUtils.setTimeShow(time, context);
            mTvTime.setVisibility(View.VISIBLE);
            mTvTime.setText(data);
        } else {
            mTvTime.setVisibility(View.GONE);
        }
        //未读消息
        String unread = bean.getUnread();
        if ("0".equalsIgnoreCase(unread)) {
            mTvNewMsg.setVisibility(View.GONE);
        } else {
            mTvNewMsg.setVisibility(View.VISIBLE);
//            mTvNewMsg.setText(unread);
        }


        //
        boolean isShow = bean.isShowPriLetterStr();
        if (isShow) {
            mTvTag.setVisibility(View.VISIBLE);
            mTvTime.setVisibility(View.GONE);
        } else {
            mTvTag.setVisibility(View.GONE);
            mTvTime.setVisibility(View.VISIBLE);
        }
    }
}
