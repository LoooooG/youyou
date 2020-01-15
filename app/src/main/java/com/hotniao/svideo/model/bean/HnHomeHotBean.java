package com.hotniao.svideo.model.bean;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：首页热门数据
 * 创建人：mj
 * 创建时间：2017/9/13 14:13
 * 修改人：Administrator
 * 修改时间：2017/9/13 14:13
 * 修改备注：
 * Version:  1.0.0
 */
public class HnHomeHotBean {


    /**
     * items : [{"anchor_exp":"0","anchor_lat":"0.000000","anchor_level":"0","anchor_live_img":"","anchor_live_onlines":"1","anchor_live_pay":"0","anchor_live_title":"","anchor_lng":"0.000000","anchor_local":"","user_id":"8"}]
     * next : 1
     * page : 1
     * pagesize : 20
     * pagetotal : 1
     * prev : 1
     * total : 1
     */

    private int next;
    private int page;
    private int pagesize;
    private int pagetotal;
    private int prev;
    private int total;
    private List<ItemsBean> items;

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * anchor_exp : 0
         * anchor_lat : 0.000000
         * anchor_level : 0
         * anchor_live_img :
         * anchor_live_onlines : 1
         * anchor_live_pay : 0
         * anchor_live_title :
         * anchor_lng : 0.000000
         * anchor_local :
         * user_id : 8
         */

        private String anchor_category_id;
        private String anchor_category_name;
        private String anchor_exp;
        private String anchor_lat;
        private String anchor_level;
        private String anchor_live_img;
        private String anchor_live_onlines;
        private String anchor_live_pay;//	主播直播收费类型，0：免费，1：VIP，2：门票，3：计时
        private String anchor_live_title;
        private String anchor_lng;
        private String anchor_local;
        private String user_id;
        private String user_level;
        private String user_nickname;
        private String anchor_game_category_code;
        private String anchor_game_category_id;
        private String anchor_game_category_name;
        private String anchor_game_category_logo;

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getAnchor_game_category_logo() {
            return anchor_game_category_logo;
        }

        public void setAnchor_game_category_logo(String anchor_game_category_logo) {
            this.anchor_game_category_logo = anchor_game_category_logo;
        }

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

        public String getAnchor_game_category_name() {
            return anchor_game_category_name;
        }

        public void setAnchor_game_category_name(String anchor_game_category_name) {
            this.anchor_game_category_name = anchor_game_category_name;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
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

        public String getAnchor_exp() {
            return anchor_exp;
        }

        public void setAnchor_exp(String anchor_exp) {
            this.anchor_exp = anchor_exp;
        }

        public String getAnchor_lat() {
            return anchor_lat;
        }

        public void setAnchor_lat(String anchor_lat) {
            this.anchor_lat = anchor_lat;
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

        public String getAnchor_live_onlines() {
            return anchor_live_onlines;
        }

        public void setAnchor_live_onlines(String anchor_live_onlines) {
            this.anchor_live_onlines = anchor_live_onlines;
        }

        public String getAnchor_live_pay() {
            return anchor_live_pay;
        }

        public void setAnchor_live_pay(String anchor_live_pay) {
            this.anchor_live_pay = anchor_live_pay;
        }

        public String getAnchor_live_title() {
            return anchor_live_title;
        }

        public void setAnchor_live_title(String anchor_live_title) {
            this.anchor_live_title = anchor_live_title;
        }

        public String getAnchor_lng() {
            return anchor_lng;
        }

        public void setAnchor_lng(String anchor_lng) {
            this.anchor_lng = anchor_lng;
        }

        public String getAnchor_local() {
            return anchor_local;
        }

        public void setAnchor_local(String anchor_local) {
            this.anchor_local = anchor_local;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
