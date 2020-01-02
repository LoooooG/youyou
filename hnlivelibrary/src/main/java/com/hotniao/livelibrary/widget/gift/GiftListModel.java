package com.hotniao.livelibrary.widget.gift;


import com.hn.library.http.BaseResponseModel;

import java.util.ArrayList;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：礼物模型
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class GiftListModel extends BaseResponseModel {

    private ArrayList<Gift> d;

    public ArrayList<Gift> getD() {
        return d;
    }

    public void setD(ArrayList<Gift> d) {
        this.d = d;
    }
}
