package com.hotniao.svideo.model;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnCityEntity  {
    private String city;//城市名字

    public HnCityEntity() {
    }

    public HnCityEntity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public HnCityEntity setCity(String city) {
        this.city = city;
        return this;
    }
}

