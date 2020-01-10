package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.io.Serializable;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：判断是否可以开播   和分享Url  上一次直播的分类
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnCanLiveModel extends BaseResponseModel {

    /**
     * d : {"anchor":{"anchor_category_id":"","anchor_category_name":""},"share_url":"http://www.baidu.com"}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean implements Serializable{
        /**
         * anchor : {"anchor_category_id":"","anchor_category_name":""}
         * share_url : http://www.baidu.com
         */

        private AnchorBean anchor;
        private String share_url;

        public AnchorBean getAnchor() {
            return anchor;
        }

        public void setAnchor(AnchorBean anchor) {
            this.anchor = anchor;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public static class AnchorBean implements Serializable {
            /**
             * anchor_category_id :
             * anchor_category_name :
             */

            private String anchor_category_id;
            private String anchor_category_name;
            private String game_category_code;
            private String game_category_id;
            private String game_category_name;
            private String live_free_time;

            public String getLive_free_time() {
                return live_free_time;
            }

            public void setLive_free_time(String live_free_time) {
                this.live_free_time = live_free_time;
            }

            public String getGame_category_code() {
                return game_category_code;
            }

            public void setGame_category_code(String game_category_code) {
                this.game_category_code = game_category_code;
            }

            public String getGame_category_id() {
                return game_category_id;
            }

            public void setGame_category_id(String game_category_id) {
                this.game_category_id = game_category_id;
            }

            public String getGame_category_name() {
                return game_category_name;
            }

            public void setGame_category_name(String game_category_name) {
                this.game_category_name = game_category_name;
            }

            public String getAnchor_category_id() {
                return anchor_category_id;
            }

            public void setAnchor_category_id(String anchor_category_id) {
                this.anchor_category_id = anchor_category_id;
            }

            public String getAnchor_category_name() {
                return anchor_category_name;
            }

            public void setAnchor_category_name(String anchor_category_name) {
                this.anchor_category_name = anchor_category_name;
            }
        }
    }
}
