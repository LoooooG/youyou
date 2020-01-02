package com.hotniao.livelibrary.model.event;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：eventbus事件   处理关注时发送
 * 创建人：mj
 * 创建时间：2017/9/13 11:10
 * 修改人：Administrator
 * 修改时间：2017/9/13 11:10
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFollowEvent {

    private  boolean  isFollow;
    private  String   uid;

    public HnFollowEvent(String uid, boolean isFollow) {
        this.uid = uid;
        this.isFollow = isFollow;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
