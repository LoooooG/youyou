package com.hn.library.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.hn.library.utils.HnDimenUtil.dp2px;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：DiWei
 * 类描述： 消息红点
 * 创建人：Kevin
 * 创建时间：2017/5/6 16:28
 * 修改人：Kevin
 * 修改时间：2017/5/6 16:28
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBadgeView extends TextView {


    public HnBadgeView(Context context) {
        this(context,null);
    }

    public HnBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HnBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBadgeNumber(int mBadgeNumber) {

        ViewGroup.LayoutParams params =  getLayoutParams();
        params.width = dp2px(getContext(), 8);
        params.height = dp2px(getContext(), 8);
        setLayoutParams(params);

        if(mBadgeNumber<=0)  {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
//        if (mBadgeNumber > 0 && mBadgeNumber <= 9) {
//            setText(String.valueOf(mBadgeNumber));
//        } else if (mBadgeNumber > 9 && mBadgeNumber <= 99) {
//            setText(String.valueOf(mBadgeNumber));
//        } else if (mBadgeNumber > 99) {
//            setText("99+");
//        }


    }
}
