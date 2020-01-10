package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.video.model.bean.HnCheckVersionBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：版本检测
 * 创建人：mj
 * 创建时间：2017/9/22 13:16
 * 修改人：Administrator
 * 修改时间：2017/9/22 13:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnCheckVersionModel extends BaseResponseModel {

    private HnCheckVersionBean d;

    public HnCheckVersionBean getD() {
        return d;
    }

    public void setD(HnCheckVersionBean d) {
        this.d = d;
    }
}
