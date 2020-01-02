package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：存储用户信息
 * 创建人：mj
 * 创建时间：2017/9/13 10:19
 * 修改人：Administrator
 * 修改时间：2017/9/13 10:19
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserInfoDetailModel  extends BaseResponseModel {


    private HnUserInfoDetailBean d;

    public HnUserInfoDetailBean getD() {
        return d;
    }

    public void setD(HnUserInfoDetailBean d) {
        this.d = d;
    }
}
