package com.hotniao.livelibrary.ui.gift;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司
 * 项目名称：乐疯直播
 * 类描述：
 * 创建人：Mr.Xu
 * 创建时间：2017/3/29 0029
 */
public class HnGiftEven {

    public String id ;
    public String name;
    public String url;
    public String money;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public HnGiftEven(String name, String url, String money, String id) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.url = url;
    }


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
