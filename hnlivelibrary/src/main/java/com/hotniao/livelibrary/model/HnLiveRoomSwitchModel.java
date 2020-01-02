package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

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

public class HnLiveRoomSwitchModel extends BaseResponseModel {

    /**
     * d : {"anchors":[{"anchor_category_id":"0","anchor_level":"0","anchor_live_img":"123","anchor_live_pay":"","user_avatar":"","user_id":"8","user_level":"0","user_nickname":"2"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<AnchorsBean> anchors;

        public List<AnchorsBean> getAnchors() {
            return anchors;
        }

        public void setAnchors(List<AnchorsBean> anchors) {
            this.anchors = anchors;
        }

        public static class AnchorsBean {
            /**
             * anchor_category_id : 0
             * anchor_level : 0
             * anchor_live_img : 123
             * anchor_live_pay :
             * user_avatar :
             * user_id : 8
             * user_level : 0
             * user_nickname : 2
             */

            private String anchor_category_id;
            private String anchor_level;
            private String anchor_live_img;
            private String anchor_live_pay;
            private String user_avatar;
            private String user_id;
            private String user_level;
            private String user_nickname;
            private String anchor_game_category_code;
            private String anchor_game_category_id;

            public String getAnchor_game_category_code() {
                return anchor_game_category_code;
            }

            public void setAnchor_game_category_code(String anchor_game_category_code) {
                this.anchor_game_category_code = anchor_game_category_code;
            }

            public String getAnchor_game_category_id() {
                return anchor_game_category_id;
            }

            public void setAnchor_game_category_id(String anchor_game_category_id) {
                this.anchor_game_category_id = anchor_game_category_id;
            }

            public String getAnchor_category_id() {
                return anchor_category_id;
            }

            public void setAnchor_category_id(String anchor_category_id) {
                this.anchor_category_id = anchor_category_id;
            }

            public String getAnchor_level() {
                return anchor_level;
            }

            public void setAnchor_level(String anchor_level) {
                this.anchor_level = anchor_level;
            }

            public String getAnchor_live_img() {
                return anchor_live_img;
            }

            public void setAnchor_live_img(String anchor_live_img) {
                this.anchor_live_img = anchor_live_img;
            }

            public String getAnchor_live_pay() {
                return anchor_live_pay;
            }

            public void setAnchor_live_pay(String anchor_live_pay) {
                this.anchor_live_pay = anchor_live_pay;
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

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }
        }
    }
}
