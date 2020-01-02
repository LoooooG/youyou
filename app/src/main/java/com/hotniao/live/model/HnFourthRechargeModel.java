package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.live.model.bean.HnFourthRechargeBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：第四方充值数据
 * 创建人：mj
 * 创建时间：2017/9/16 18:17
 * 修改人：Administrator
 * 修改时间：2017/9/16 18:17
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFourthRechargeModel extends BaseResponseModel {

    private HnFourthRechargeBean d;

    public HnFourthRechargeBean getD() {
        return d;
    }

    public void setD(HnFourthRechargeBean d) {
        this.d = d;
    }
}
