package com.hotniao.livelibrary.model.event;

import com.hotniao.livelibrary.ui.beauty.BeautyDialogFragment;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：美颜
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLiveBeautyEvent {

   private int   type;
   private BeautyDialogFragment.BeautyParams params;

    public HnLiveBeautyEvent(int type, BeautyDialogFragment.BeautyParams params) {
        this.type = type;
        this.params = params;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BeautyDialogFragment.BeautyParams getParams() {
        return params;
    }

    public void setParams(BeautyDialogFragment.BeautyParams params) {
        this.params = params;
    }
}
