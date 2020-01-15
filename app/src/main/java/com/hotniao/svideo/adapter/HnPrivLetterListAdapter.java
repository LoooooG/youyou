package com.hotniao.svideo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.view.FrescoImageView;
import com.hotniao.svideo.R;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.hotniao.livelibrary.util.DataTimeUtils;


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
public class HnPrivLetterListAdapter extends BaseQuickAdapter<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean, BaseViewHolder> {


    private Context context;

    public HnPrivLetterListAdapter(Context context) {
        super(R.layout.live_item_private_list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean) {

        //头像
        FrescoImageView mIvHeader = (FrescoImageView) helper.getView(R.id.iv_header);
        mIvHeader.setController(FrescoConfig.getController(bean.getUser_avatar()));
        //昵称
        TextView mTvName = (TextView) helper.getView(R.id.tv_name);
        mTvName.setText(bean.getUser_nickname());
        //用户等级
        TextView mTvLevel = (TextView) helper.getView(R.id.tv_level);
        mTvLevel.setVisibility(View.GONE);
//        HnLiveLevelUtil.setAudienceLevBg(mTvLevel, bean.getLevel(), true);

        //内容
        TextView mTvContent = (TextView) helper.getView(R.id.tv_content);
        mTvContent.setText(bean.getContent());
        //时间
        TextView mTvTime = (TextView) helper.getView(R.id.tv_time);
        String update_time = bean.getTime();
        if (!TextUtils.isEmpty(update_time)) {
            long l = Long.parseLong(update_time)*1000;
            mTvTime.setText(DataTimeUtils.getTimestampString(l));
        }
//        String time = HnUtils.getDateToString_4(l);
//        DateUtils.setTimeShow(time, mTvTime, context);
        //消息数据
        TextView mTvNewMsg = (TextView) helper.getView(R.id.tv_new_msg);
        String unread = bean.getUnread();
        if ("0".equalsIgnoreCase(unread)) {
            mTvNewMsg.setVisibility(View.GONE);
        } else {
            mTvNewMsg.setVisibility(View.VISIBLE);
//            mTvNewMsg.setText(unread);
        }
        //性别
        ImageView mIvOffi = (ImageView) helper.getView(R.id.iv_offi);
        mIvOffi.setVisibility(View.GONE);
        //vip
        ImageView ivVip = helper.getView(R.id.iv_withdrawalsuccessful);
        ivVip.setVisibility(View.GONE);
    }
}
