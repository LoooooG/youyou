package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取红人主播
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeHotAnchorModel extends BaseResponseModel{


    private List<DBean> d;

    public List<DBean> getD() {
        return d;
    }

    public void setD(List<DBean> d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * anchor_is_live : N
         * user_avatar :
         * user_birth :
         * user_fans_total : 0
         * user_id : 9
         * user_nickname : 乐播12261732132
         * user_sex : 1
         */

        private String anchor_is_live;
        private String user_avatar;
        private String user_birth;
        private String user_fans_total;
        private String user_id;
        private String user_nickname;
        private String user_sex;

        public String getAnchor_is_live() {
            return anchor_is_live;
        }

        public void setAnchor_is_live(String anchor_is_live) {
            this.anchor_is_live = anchor_is_live;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_birth() {
            return user_birth;
        }

        public void setUser_birth(String user_birth) {
            this.user_birth = user_birth;
        }

        public String getUser_fans_total() {
            return user_fans_total;
        }

        public void setUser_fans_total(String user_fans_total) {
            this.user_fans_total = user_fans_total;
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

        public String getUser_sex() {
            return user_sex;
        }

        public void setUser_sex(String user_sex) {
            this.user_sex = user_sex;
        }
    }
}
