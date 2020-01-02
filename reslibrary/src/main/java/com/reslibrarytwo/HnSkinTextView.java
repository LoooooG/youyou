package com.reslibrarytwo;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：uLive_v1.0
 * 类描述： 可以设置左侧图标的文字
 * 创建人：Kevin
 * 创建时间：2017/9/29 13:30
 * 修改人：Kevin
 * 修改时间：2017/9/29 13:30
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSkinTextView extends TextView {
    public HnSkinTextView(Context context) {
        super(context);
    }

    public HnSkinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HnSkinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLeftDrawable(@DrawableRes int leftRes){
        setCompoundDrawablesWithIntrinsicBounds(leftRes, 0,0,0);
    }
    public void setRightDrawable(@DrawableRes int rightRes){
        setCompoundDrawablesWithIntrinsicBounds(0,0,rightRes,0);
    }
    public void setTopDrawable(@DrawableRes int topRes){
        setCompoundDrawablesWithIntrinsicBounds(0,topRes,0,0);
    }
    public void setBottomDrawable(@DrawableRes int bottomRes){
        setCompoundDrawablesWithIntrinsicBounds(0,0,0,bottomRes);
    }
}

