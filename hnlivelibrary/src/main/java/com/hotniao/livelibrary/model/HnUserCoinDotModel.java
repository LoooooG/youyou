package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述 用户余额
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserCoinDotModel extends BaseResponseModel {
    /**
     * d : {"user":{"user_coin":"测试内容jk05","user_dot":"测试内容8239"}}
     */

    private DiamondModel.DBean d;

    public DiamondModel.DBean getD() {
        return d;
    }

    public void setD(DiamondModel.DBean d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * user : {"user_coin":"测试内容jk05","user_dot":"测试内容8239"}
         */

        private DiamondModel.DBean.UserBean user;

        public DiamondModel.DBean.UserBean getUser() {
            return user;
        }

        public void setUser(DiamondModel.DBean.UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * user_coin : 测试内容jk05
             * user_dot : 测试内容8239
             */

            private String user_coin;
            private String user_dot;

            public String getUser_coin() {
                return user_coin;
            }

            public void setUser_coin(String user_coin) {
                this.user_coin = user_coin;
            }

            public String getUser_dot() {
                return user_dot;
            }

            public void setUser_dot(String user_dot) {
                this.user_dot = user_dot;
            }
        }
    }
}
