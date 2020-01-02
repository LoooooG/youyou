package com.hotniao.livelibrary.adapter.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间消息   --- 主播红包
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorLuckyHolder {

    public  TextView           mLv;
    public  TextView           mNick;
    public  TextView           mUserNick;
    public  TextView           mTvDes;
    public  RelativeLayout     mInfo;
    public  RelativeLayout     mLucky;
    public  RelativeLayout     mContainer;
    private ReceivedSockedBean mData;

    public HnAnchorLuckyHolder(View convertView) {
        mLv = (TextView) convertView.findViewById(R.id.tv_level);
        mNick = (TextView) convertView.findViewById(R.id.userNick);
        mUserNick = (TextView) convertView.findViewById(R.id.tv_nick);
        mInfo = (RelativeLayout) convertView.findViewById(R.id.rl_level);
        mContainer = (RelativeLayout) convertView.findViewById(R.id.rl_msg_info);
        mLucky = (RelativeLayout) convertView.findViewById(R.id.luckymoney);
        mTvDes = (TextView) convertView.findViewById(R.id.tv_thanks);
    }

    public void setData(ReceivedSockedBean data) {
        mData = data;
    }

    public ReceivedSockedBean getData() {
        return mData;
    }
}
