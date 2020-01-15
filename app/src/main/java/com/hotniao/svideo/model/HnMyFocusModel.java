package com.hotniao.svideo.model;

import com.hotniao.svideo.model.bean.HnMyFocusBean;
import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：我的关注列表数据
 * 创建人：mj
 * 创建时间：2017/9/4 17:06
 * 修改人：Administrator
 * 修改时间：2017/9/4 17:06
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyFocusModel extends BaseResponseModel {

    public HnMyFocusBean d;

    public HnMyFocusBean getD() {
        return d;
    }

    public void setD(HnMyFocusBean d) {
        this.d = d;
    }
}
