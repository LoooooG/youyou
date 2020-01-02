package com.hotniao.livelibrary.model;

import com.google.gson.annotations.SerializedName;

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

public class HnJPushMessageInfo {


    /**
     * type : anchor_start_live
     * data : {"anchor_live_pay":"0","anchor_category_id":"1","anchor_user_id":"12"}
     * 0 : jiguang
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
         * anchor_live_pay : 0
         * anchor_category_id : 1
         * anchor_user_id : 12
         */

        private String anchor_live_pay;
        private String anchor_category_id;
        private String anchor_user_id;
        private String anchor_game_category_id;

        public String getAnchor_game_category_id() {
            return anchor_game_category_id;
        }

        public void setAnchor_game_category_id(String anchor_game_category_id) {
            this.anchor_game_category_id = anchor_game_category_id;
        }

        public String getAnchor_live_pay() {
            return anchor_live_pay;
        }

        public void setAnchor_live_pay(String anchor_live_pay) {
            this.anchor_live_pay = anchor_live_pay;
        }

        public String getAnchor_category_id() {
            return anchor_category_id;
        }

        public void setAnchor_category_id(String anchor_category_id) {
            this.anchor_category_id = anchor_category_id;
        }

        public String getAnchor_user_id() {
            return anchor_user_id;
        }

        public void setAnchor_user_id(String anchor_user_id) {
            this.anchor_user_id = anchor_user_id;
        }
    }
}
