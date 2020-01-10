package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：主播停止直播时返回的数据
 * 创建人：mj
 * 创建时间：2017/9/15 14:09
 * 修改人：Administrator
 * 修改时间：2017/9/15 14:09
 * 修改备注：
 * Version:  1.0.0
 */
public class HnStopLiveModel extends BaseResponseModel {


    /**
     * d : {"anchor_live_follow_total":"0","anchor_live_onlines":"0","anchor_ranking":"0","live_time":"0","user_avatar":"","user_dot":"0","user_id":"8","user_nickname":"2"}
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
         * anchor_live_follow_total : 0
         * anchor_live_onlines : 0
         * anchor_ranking : 0
         * live_time : 0
         * user_avatar :
         * user_dot : 0
         * user_id : 8
         * user_nickname : 2
         */

        private String anchor_live_follow_total;
        private String anchor_live_like_total;
        private String anchor_live_onlines;
        private String anchor_ranking;
        private String live_time;
        private String user_avatar;
        private String user_dot;
        private String user_id;
        private String user_nickname;
        private String is_create_record;
        private String anchor_live_log_id;

        public String getAnchor_live_log_id() {
            return anchor_live_log_id;
        }

        public void setAnchor_live_log_id(String anchor_live_log_id) {
            this.anchor_live_log_id = anchor_live_log_id;
        }

        public String getIs_create_record() {
            return is_create_record;
        }

        public void setIs_create_record(String is_create_record) {
            this.is_create_record = is_create_record;
        }

        public String getAnchor_live_like_total() {
            return anchor_live_like_total;
        }

        public void setAnchor_live_like_total(String anchor_live_like_total) {
            this.anchor_live_like_total = anchor_live_like_total;
        }

        public String getAnchor_live_follow_total() {
            return anchor_live_follow_total;
        }

        public void setAnchor_live_follow_total(String anchor_live_follow_total) {
            this.anchor_live_follow_total = anchor_live_follow_total;
        }

        public String getAnchor_live_onlines() {
            return anchor_live_onlines;
        }

        public void setAnchor_live_onlines(String anchor_live_onlines) {
            this.anchor_live_onlines = anchor_live_onlines;
        }

        public String getAnchor_ranking() {
            return anchor_ranking;
        }

        public void setAnchor_ranking(String anchor_ranking) {
            this.anchor_ranking = anchor_ranking;
        }

        public String getLive_time() {
            return live_time;
        }

        public void setLive_time(String live_time) {
            this.live_time = live_time;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_dot() {
            return user_dot;
        }

        public void setUser_dot(String user_dot) {
            this.user_dot = user_dot;
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
}
