package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.livelibrary.model.bean.HnLiveRoomInfoBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：进入房间的相关信息
 * 创建人：mj
 * 创建时间：2017/9/19 19:38
 * 修改人：Administrator
 * 修改时间：2017/9/19 19:38
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveRoomInfoModel extends BaseResponseModel {

    private HnLiveRoomInfoBean d;

    public HnLiveRoomInfoBean getD() {
        return d;
    }

    public void setD(HnLiveRoomInfoBean d) {
        this.d = d;
    }
}
