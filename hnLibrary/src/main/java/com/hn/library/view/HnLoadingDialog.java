package com.hn.library.view;

import android.app.Dialog;
import android.content.Context;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：自定义加载或执行状态对话框
 * 创建人：Kevin
 * 创建时间：2016/5/6 15:09
 * 修改人：Kevin
 * 修改时间：2016/5/6 15:09
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLoadingDialog extends Dialog {
    private String tag;

    public HnLoadingDialog(Context context) {
        super(context);
    }

    public HnLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public HnLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
