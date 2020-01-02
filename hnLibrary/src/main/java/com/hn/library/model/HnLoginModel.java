package com.hn.library.model;

import com.hn.library.http.BaseResponseModel;
import com.hn.library.model.HnLoginBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：HJN_v1.2.1
 * 类描述：注册第二步返回的数据
 * 创建人：mj
 * 创建时间：2017/9/4 12:50
 * 修改人：Administrator
 * 修改时间：2017/9/4 12:50
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLoginModel extends BaseResponseModel {

    private HnLoginBean d;

    public HnLoginBean getD() {
        return d;
    }

    public void setD(HnLoginBean d) {
        this.d = d;
    }
}
