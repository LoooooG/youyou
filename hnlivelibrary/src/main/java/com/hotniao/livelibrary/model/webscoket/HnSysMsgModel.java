package com.hotniao.livelibrary.model.webscoket;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：系统消息数据  用于装载webspcket推送的系统消息数据，任何模块都可以调用
 * 创建人：mj
 * 创建时间：2017年9月8日
 * 修改人：
 * 修改时间：2017年9月8日
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSysMsgModel {


    /**
     * type : system_msg
     * data : {"content":"主播认证已提交！审批流程大概需要2-7个工作日，请耐心等待。","add_time":"1504755626"}
     */

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
        /**
         * content : 主播认证已提交！审批流程大概需要2-7个工作日，请耐心等待。
         * add_time : 1504755626
         */

        private String content;
        private String add_time;
        private String has_voice;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHas_voice() {
            return has_voice;
        }

        public void setHas_voice(String has_voice) {
            this.has_voice = has_voice;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
