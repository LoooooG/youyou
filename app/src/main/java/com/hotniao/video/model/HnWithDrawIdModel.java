package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

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

public class HnWithDrawIdModel extends BaseResponseModel {

    /**
     * d : {"user":{"account":"","cash":"0.00","pay":"支付宝"}}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        public String getMin_withdraw_num() {
            return min_withdraw_num;
        }

        public void setMin_withdraw_num(String min_withdraw_num) {
            this.min_withdraw_num = min_withdraw_num;
        }

        /**
         * user : {"account":"","cash":"0.00","pay":"支付宝"}
         */

        private String min_withdraw_num;

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * account :
             * cash : 0.00
             * pay : 支付宝
             */

            private String account;
            private String cash;
            private String pay;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getCash() {
                return cash;
            }

            public void setCash(String cash) {
                this.cash = cash;
            }

            public String getPay() {
                return pay;
            }

            public void setPay(String pay) {
                this.pay = pay;
            }
        }
    }
}
