package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.live.model.bean.HnHomeHotBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：首页热门数据
 * 创建人：mj
 * 创建时间：2017/9/13 14:12
 * 修改人：Administrator
 * 修改时间：2017/9/13 14:12
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeHotModel extends BaseResponseModel {
    private HnHomeHotBean d;

    public HnHomeHotBean getD() {
        return d;
    }

    public void setD(HnHomeHotBean d) {
        this.d = d;
    }
}
