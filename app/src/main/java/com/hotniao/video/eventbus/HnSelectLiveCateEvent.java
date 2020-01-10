package com.hotniao.video.eventbus;

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

public class HnSelectLiveCateEvent {

    public static final int REFRESH_UI_LIVE=0;//刷新LiveUI
    public static final int REFRESH_UI_VIDEO=1;//刷新VideoUI
    public static final int SWITCH_CATE_LIVE=2;//切换Live分类
    public static final int SWITCH_CATE_VIDEO=3;//切换Video分类
    public static final int SWITCH_CHAT_TYPE=4;//切换Chat分类


    private String id;
    private int position;
    private int type;

    public HnSelectLiveCateEvent(int type,String id, int position) {
        this.id = id;
        this.position = position;
        this.type=type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
