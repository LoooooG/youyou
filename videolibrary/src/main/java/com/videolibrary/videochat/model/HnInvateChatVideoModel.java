package com.videolibrary.videochat.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：邀请主播一对一私聊
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnInvateChatVideoModel extends BaseResponseModel {

    /**
     * d : {"chat_log":"测试内容21y4","push_url":""}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * chat_log : 测试内容21y4
         * push_url :
         */

        private String chat_log;
        private String push_url;
        private String amount;
        private String duration;
        private String coin_amount;

        public String getCoin_amount() {
            return coin_amount;
        }

        public void setCoin_amount(String coin_amount) {
            this.coin_amount = coin_amount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getChat_log() {
            return chat_log;
        }

        public void setChat_log(String chat_log) {
            this.chat_log = chat_log;
        }

        public String getPush_url() {
            return push_url;
        }

        public void setPush_url(String push_url) {
            this.push_url = push_url;
        }

        @Override
        public String toString() {
            return "DBean{" +
                    "chat_log='" + chat_log + '\'' +
                    ", push_url='" + push_url + '\'' +
                    ", amount='" + amount + '\'' +
                    ", duration='" + duration + '\'' +
                    ", coin_amount='" + coin_amount + '\'' +
                    '}';
        }
    }


}
