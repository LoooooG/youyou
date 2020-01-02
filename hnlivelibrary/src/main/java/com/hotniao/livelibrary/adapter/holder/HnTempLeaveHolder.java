package com.hotniao.livelibrary.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.model.bean.SocketFuserBean;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间消息   --- 暂时离开
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTempLeaveHolder {

    public  TextView        mMsg;
    private SocketFuserBean mData;

    public HnTempLeaveHolder(View convertView) {
        mMsg = (TextView) convertView.findViewById(R.id.userMsg);
    }

    public void setData(SocketFuserBean data) {
        mData = data;
    }

    public SocketFuserBean getData() {
        return mData;
    }
}
