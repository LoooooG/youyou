package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.video.model.bean.HnProfileBean;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：我的账户界面数据
 * 创建人：mj
 * 创建时间：2017/9/8 15:27
 * 修改人：Administrator
 * 修改时间：2017/9/8 15:27
 * 修改备注：
 * Version:  1.0.0
 */
public class HnProfileMode extends BaseResponseModel {

    public HnProfileBean d;

    public HnProfileBean getD() {
        return d;
    }

    public void setD(HnProfileBean d) {
        this.d = d;
    }
}
