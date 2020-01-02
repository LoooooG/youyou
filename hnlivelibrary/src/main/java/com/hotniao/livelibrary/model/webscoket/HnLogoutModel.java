package com.hotniao.livelibrary.model.webscoket;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：被挤下线消息数据  用于装载webspcket推送的挤下线消息数据，任何模块都可以调用
 * 创建人：mj
 * 创建时间：2017年9月8日
 * 修改人：
 * 修改时间：2017年9月8日
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLogoutModel {
    /**
     * data : {"device_id":""}
     * msg :
     * type : user_kill_online
     */

    private DataBean data;
    private String msg;
    private String type;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class DataBean {
        /**
         * device_id :
         */

        private String device_id;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }
    }


}
