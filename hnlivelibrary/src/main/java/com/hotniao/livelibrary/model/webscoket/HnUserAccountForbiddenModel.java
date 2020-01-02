package com.hotniao.livelibrary.model.webscoket;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户账号禁用数据  用于装载webspcket推送的系统消息数据，任何模块都可以调用
 * 创建人：mj
 * 创建时间：2017/10/9 12:53
 * 修改人：Administrator
 * 修改时间：2017/10/9 12:53
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserAccountForbiddenModel {



    private String type;
    private DataBean data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String uid;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
