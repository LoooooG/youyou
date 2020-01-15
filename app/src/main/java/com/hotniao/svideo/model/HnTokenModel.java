package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.svideo.model.bean.HnTokenBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：获取七牛token数据
 * 创建人：mj
 * 创建时间：2017/9/7 20:49
 * 修改人：
 * 修改时间：2
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTokenModel extends BaseResponseModel {

    private HnTokenBean d;

    public HnTokenBean getD() {
        return d;
    }

    public void setD(HnTokenBean d) {
        this.d = d;
    }
}
