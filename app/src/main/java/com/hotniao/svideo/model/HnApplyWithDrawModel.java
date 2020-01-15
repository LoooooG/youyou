package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：申请提现
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnApplyWithDrawModel extends BaseResponseModel {


    /**
     * d : {"user":{"user_dot":"测试内容23fq"},"withdraw_log":{"id":"0"}}
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
         * user : {"user_dot":"测试内容23fq"}
         * withdraw_log : {"id":"0"}
         */

        private UserBean user;
        private WithdrawLogBean withdraw_log;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public WithdrawLogBean getWithdraw_log() {
            return withdraw_log;
        }

        public void setWithdraw_log(WithdrawLogBean withdraw_log) {
            this.withdraw_log = withdraw_log;
        }

        public static class UserBean {
            /**
             * user_dot : 测试内容23fq
             */

            private String user_dot;

            public String getUser_dot() {
                return user_dot;
            }

            public void setUser_dot(String user_dot) {
                this.user_dot = user_dot;
            }
        }

        public static class WithdrawLogBean {
            /**
             * id : 0
             */

            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
