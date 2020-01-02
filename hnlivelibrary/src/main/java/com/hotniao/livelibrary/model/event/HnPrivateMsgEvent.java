package com.hotniao.livelibrary.model.event;

import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：eventbus事件   接收到私聊消息时发送
 * 创建人：Administrator
 * 创建时间：2017/9/12 12:44
 * 修改人：Administrator
 * 修改时间：2017/9/12 12:44
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateMsgEvent {

    private String          type;
    private HnPrivateMsgModel data;

    public HnPrivateMsgEvent(String type, HnPrivateMsgModel data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HnPrivateMsgModel getData() {
        return data;
    }

    public void setData(HnPrivateMsgModel data) {
        this.data = data;
    }
}
