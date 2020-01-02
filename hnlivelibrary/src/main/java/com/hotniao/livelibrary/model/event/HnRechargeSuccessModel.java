package com.hotniao.livelibrary.model.event;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户充值成功后推送的数据
 * 创建人：Administrator
 * 创建时间：2017/9/26 19:24
 * 修改人：Administrator
 * 修改时间：2017/9/26 19:24
 * 修改备注：
 * Version:  1.0.0
 */
public class HnRechargeSuccessModel {


    /**
     * type : pay_success
     * data : {"user_coin":"qqq"}
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
         * user_coin : qqq
         */

        private String user_coin;

        public String getUser_coin() {
            return user_coin;
        }

        public void setUser_coin(String user_coin) {
            this.user_coin = user_coin;
        }
    }
}
