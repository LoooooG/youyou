package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：首页直播分类
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeLiveCateModel extends BaseResponseModel {


    /**
     * d : {"game_category":[{"game_category_code":"","game_category_id":"1","game_category_logo":"","game_category_name":""}],"live_category":[{"anchor_category_logo":"","anchor_category_id":"1","anchor_category_name":"明星"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<LiveCategoryBean> live_category;


        public List<LiveCategoryBean> getLive_category() {
            return live_category;
        }

        public void setLive_category(List<LiveCategoryBean> live_category) {
            this.live_category = live_category;
        }


        public static class LiveCategoryBean {
            /**
             * anchor_category_logo :
             * anchor_category_id : 1
             * anchor_category_name : 明星
             */

            private String anchor_category_logo;
            private String anchor_category_id;
            private String anchor_category_name;

            public String getAnchor_category_logo() {
                return anchor_category_logo;
            }

            public void setAnchor_category_logo(String anchor_category_logo) {
                this.anchor_category_logo = anchor_category_logo;
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
