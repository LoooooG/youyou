package com.hotniao.livelibrary.widget.gift;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：点击送礼头像
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class GiftHeaderClickEvent {


    private final GiftModel gift;

    public GiftHeaderClickEvent(GiftModel gift) {
        this.gift = gift;
    }


    public GiftModel getGift() {
        return gift;
    }
}
