package com.hotniao.livelibrary.model.bean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：支付房间费用后返回
 * 创建人：mj
 * 创建时间：2017/9/21 11:32
 * 修改人：Administrator
 * 修改时间：2017/9/21 11:32
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPayRoomPriceBean {


    /**
     * user : {"user_coin":"0"}
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
         * user_coin : 0
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
