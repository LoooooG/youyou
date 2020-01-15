package com.hotniao.svideo.model.bean;


import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：我的账户界面数据
 * 创建人：mj
 * 创建时间：2017/9/8 15:27
 * 修改人：Administrator
 * 修改时间：2017/9/8 15:27
 * 修改备注：
 * Version:  1.0.0
 */
public class HnProfileBean {


    /**
     * recharge_combo : [{"recharge_combo_coin":"1","recharge_combo_fee":"1","recharge_combo_give_coin":"1","recharge_combo_id":"1"}]
     * user : {"user_avatar":"","user_coin":"99800","user_id":"8","user_nickname":"2"}
     */

    private UserBean user;
    private List<RechargeComboBean> recharge_combo;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<RechargeComboBean> getRecharge_combo() {
        return recharge_combo;
    }

    public void setRecharge_combo(List<RechargeComboBean> recharge_combo) {
        this.recharge_combo = recharge_combo;
    }

    public static class UserBean {
        /**
         * user_avatar :
         * user_coin : 99800
         * user_id : 8
         * user_nickname : 2
         */

        private String user_avatar;
        private String user_coin;
        private String user_id;
        private String user_nickname;

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_coin() {
            return user_coin;
        }

        public void setUser_coin(String user_coin) {
            this.user_coin = user_coin;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }
    }

    public static class RechargeComboBean {
        /**
         * recharge_combo_coin : 1
         * recharge_combo_fee : 1
         * recharge_combo_give_coin : 1
         * recharge_combo_id : 1
         */

        private String recharge_combo_coin;
        private String recharge_combo_fee;
        private String recharge_combo_give_coin;
        private String recharge_combo_id;
        private boolean isChoose;

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public String getRecharge_combo_coin() {
            return recharge_combo_coin;
        }

        public void setRecharge_combo_coin(String recharge_combo_coin) {
            this.recharge_combo_coin = recharge_combo_coin;
        }

        public String getRecharge_combo_fee() {
            return recharge_combo_fee;
        }

        public void setRecharge_combo_fee(String recharge_combo_fee) {
            this.recharge_combo_fee = recharge_combo_fee;
        }

        public String getRecharge_combo_give_coin() {
            return recharge_combo_give_coin;
        }

        public void setRecharge_combo_give_coin(String recharge_combo_give_coin) {
            this.recharge_combo_give_coin = recharge_combo_give_coin;
        }

        public String getRecharge_combo_id() {
            return recharge_combo_id;
        }

        public void setRecharge_combo_id(String recharge_combo_id) {
            this.recharge_combo_id = recharge_combo_id;
        }
    }
}
