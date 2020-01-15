package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：会员数据
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVipDataModel extends BaseResponseModel {


    /**
     * d : {"user":{"user_avatar":"","user_id":"1","user_is_member":"N","user_level":"测试内容6d5s","user_member_expire_time":"0","user_nickname":"2"},"vip_combo":[{"combo_fee":"100","combo_id":"1","combo_month":"1"}],"vip_privilege":[{"logo":"","name":"会员进场特效"}]}
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
         * user : {"user_avatar":"","user_id":"1","user_is_member":"N","user_level":"测试内容6d5s","user_member_expire_time":"0","user_nickname":"2"}
         * vip_combo : [{"combo_fee":"100","combo_id":"1","combo_month":"1"}]
         * vip_privilege : [{"logo":"","name":"会员进场特效"}]
         */
        private String order_id;
        private UserBean user;
        private List<VipComboBean> vip_combo;
        private List<VipPrivilegeBean> vip_privilege;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<VipComboBean> getVip_combo() {
            return vip_combo;
        }

        public void setVip_combo(List<VipComboBean> vip_combo) {
            this.vip_combo = vip_combo;
        }

        public List<VipPrivilegeBean> getVip_privilege() {
            return vip_privilege;
        }

        public void setVip_privilege(List<VipPrivilegeBean> vip_privilege) {
            this.vip_privilege = vip_privilege;
        }

        public static class UserBean {
            /**
             * user_avatar :
             * user_id : 1
             * user_is_member : N
             * user_level : 测试内容6d5s
             * user_member_expire_time : 0
             * user_nickname : 2
             */

            private String user_avatar;
            private String user_id;
            private String user_coin;
            private String user_is_member;
            private String user_level;
            private String user_member_expire_time;
            private String user_nickname;

            public String getUser_coin() {
                return user_coin;
            }

            public void setUser_coin(String user_coin) {
                this.user_coin = user_coin;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_is_member() {
                return user_is_member;
            }

            public void setUser_is_member(String user_is_member) {
                this.user_is_member = user_is_member;
            }

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }

            public String getUser_member_expire_time() {
                return user_member_expire_time;
            }

            public void setUser_member_expire_time(String user_member_expire_time) {
                this.user_member_expire_time = user_member_expire_time;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }
        }

        public static class VipComboBean {
            /**
             * combo_fee : 100
             * combo_id : 1
             * combo_month : 1
             */

            private String combo_fee;
            private String combo_id;
            private String combo_month;

            public String getCombo_fee() {
                return combo_fee;
            }

            public void setCombo_fee(String combo_fee) {
                this.combo_fee = combo_fee;
            }

            public String getCombo_id() {
                return combo_id;
            }

            public void setCombo_id(String combo_id) {
                this.combo_id = combo_id;
            }

            public String getCombo_month() {
                return combo_month;
            }

            public void setCombo_month(String combo_month) {
                this.combo_month = combo_month;
            }
        }

        public static class VipPrivilegeBean {
            /**
             * logo :
             * name : 会员进场特效
             */

            private String logo;
            private String name;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
