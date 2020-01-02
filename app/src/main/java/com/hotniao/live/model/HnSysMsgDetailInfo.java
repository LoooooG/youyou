package com.hotniao.live.model;

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

public class HnSysMsgDetailInfo  {


    /**
     * type : certification
     * data : {"content":"主播认证已提交,审批流程大概需要2-7个工作日，请耐心等待~"}
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
         * content : 主播认证已提交,审批流程大概需要2-7个工作日，请耐心等待~
         */

        private String content;
        private String withdraw_log_id;
        private String status;
        private String user_id;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWithdraw_log_id() {
            return withdraw_log_id;
        }

        public void setWithdraw_log_id(String withdraw_log_id) {
            this.withdraw_log_id = withdraw_log_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
