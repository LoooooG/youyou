package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.livelibrary.model.bean.HnPayRoomPriceBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：支付房间费用后返回
 * 创建人：mj
 * 创建时间：2017/9/21 11:31
 * 修改人：Administrator
 * 修改时间：2017/9/21 11:31
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPayRoomPriceModel extends BaseResponseModel {

    private HnPayRoomPriceBean d;

    public HnPayRoomPriceBean getD() {
        return d;
    }

    public void setD(HnPayRoomPriceBean d) {
        this.d = d;
    }
}
