package com.hn.library.base;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户发送eventbus数据
 * 创建人：Administrator
 * 创建时间：2017/9/5 17:10
 * 修改人：Administrator
 * 修改时间：2017/9/5 17:10
 * 修改备注：
 * Version:  1.0.0
 */
public class EventBusBean {

    private  int  pos;

    private  String type;

    private  Object obj;
    private  Object otherObj;

    public EventBusBean(int pos, String type, Object obj) {
        this.pos = pos;
        this.type = type;
        this.obj = obj;
    }

    public EventBusBean(int pos, String type, Object obj, Object otherObj) {
        this.pos = pos;
        this.type = type;
        this.obj = obj;
        this.otherObj = otherObj;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getOtherObj() {
        return otherObj;
    }

    public void setOtherObj(Object otherObj) {
        this.otherObj = otherObj;
    }
}
