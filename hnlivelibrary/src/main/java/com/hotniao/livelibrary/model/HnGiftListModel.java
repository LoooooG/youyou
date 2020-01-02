package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.livelibrary.model.bean.HnGiftListBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物列表数据
 * 创建人：mj
 * 创建时间：2017/9/16 16:37
 * 修改人：Administrator
 * 修改时间：2017/9/16 16:37
 * 修改备注：
 * Version:  1.0.0
 */
public class HnGiftListModel   extends BaseResponseModel{

    private HnGiftListBean d;

    public HnGiftListBean getD() {
        return d;
    }

    public void setD(HnGiftListBean d) {
        this.d = d;
    }
}
