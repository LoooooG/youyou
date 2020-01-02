package com.hotniao.livelibrary.model.event;

import com.hotniao.livelibrary.model.webscoket.HnSysMsgModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：eventbus事件   接收到系统消息时发送
 * 创建人：Administrator
 * 创建时间：2017/9/12 12:44
 * 修改人：Administrator
 * 修改时间：2017/9/12 12:44
 * 修改备注：
 * Version:  1.0.0
 */
public class HnReceiverSysMsgEvent {

    private String          type;
    private HnSysMsgModel data;

    public HnReceiverSysMsgEvent(String type, HnSysMsgModel data) {
        this.type = type;
        this.data = data;
    }

    public HnReceiverSysMsgEvent(HnSysMsgModel data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HnSysMsgModel getData() {
        return data;
    }

    public void setData(HnSysMsgModel data) {
        this.data = data;
    }
}
