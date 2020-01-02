package com.hotniao.livelibrary.model;


import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司
 * 类描述：
 * 创建人：Mr.Xu
 * 创建时间：2017/3/20 0020
 */
public class DiamondModel extends BaseResponseModel {


    /**
     * d : {"user":{"user_coin":"测试内容jk05","user_dot":"测试内容8239"}}
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
         * user : {"user_coin":"测试内容jk05","user_dot":"测试内容8239"}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
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
