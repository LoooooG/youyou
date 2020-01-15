package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.svideo.model.bean.HnFollowLiveBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：关注直播列表数据
 * 创建人：mj
 * 创建时间：2017/9/19 13:05
 * 修改人：Administrator
 * 修改时间：2017/9/19 13:05
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFollowLiveModel extends BaseResponseModel {

    private HnFollowLiveBean d;

    public HnFollowLiveBean getD() {
        return d;
    }

    public void setD(HnFollowLiveBean d) {
        this.d = d;
    }
}
