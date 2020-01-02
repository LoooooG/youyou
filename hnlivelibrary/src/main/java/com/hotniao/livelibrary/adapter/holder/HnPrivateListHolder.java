package com.hotniao.livelibrary.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.bean.PrivateLetterList;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：私信
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateListHolder {

    public  FrescoImageView             mUserHeader;
    public  TextView                    mUserNick;
    public  TextView                    mUserLevel;
    public  TextView                    mUserLastContent;
    public  TextView                    mUserTime;
    public  TextView                    mNewMessageNotify;
    private PrivateLetterList.ItemsBean mBean;


    public HnPrivateListHolder(View view) {

        mUserHeader = (FrescoImageView) view.findViewById(R.id.user_header);
        mUserNick = (TextView) view.findViewById(R.id.user_nick);
        mUserLevel = (TextView) view.findViewById(R.id.user_level);
        mUserLastContent = (TextView) view.findViewById(R.id.user_lastcontent);
        mUserTime = (TextView) view.findViewById(R.id.user_time);
        mNewMessageNotify = (TextView) view.findViewById(R.id.tv_new_msg);

    }

    public void setBean(PrivateLetterList.ItemsBean bean) {
        mBean = bean;
    }

    public PrivateLetterList.ItemsBean getBean() {
        return mBean;
    }
}

