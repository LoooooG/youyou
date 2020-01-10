package com.hotniao.video.model;

import android.graphics.Bitmap;

import java.io.Serializable;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：本地图片类
 * 创建人：刘勇
 * 创建时间：2017/3/13 14：00
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLocalImageModel implements Serializable {
    private String url;
    private String type;

    public HnLocalImageModel() {
    }

    public HnLocalImageModel(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
