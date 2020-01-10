package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取主播一对一私聊状态
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMineChatStateInfoModel extends BaseResponseModel {

    /**
     * d : {"chat_status":"测试内容3yu8","coin_name":"测试内容1y68","is_anchor":true,"private_price":"测试内容64f3"}
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
         * chat_status : 测试内容3yu8
         * coin_name : 测试内容1y68
         * is_anchor : true
         * private_price : 测试内容64f3
         */

        private String chat_status;
        private String coin_name;
        private boolean is_anchor;
        private String private_price;

        public String getChat_status() {
            return chat_status;
        }

        public void setChat_status(String chat_status) {
            this.chat_status = chat_status;
        }

        public String getCoin_name() {
            return coin_name;
        }

        public void setCoin_name(String coin_name) {
            this.coin_name = coin_name;
        }

        public boolean isIs_anchor() {
            return is_anchor;
        }

        public void setIs_anchor(boolean is_anchor) {
            this.is_anchor = is_anchor;
        }

        public String getPrivate_price() {
            return private_price;
        }

        public void setPrivate_price(String private_price) {
            this.private_price = private_price;
        }
    }
}
