package com.hotniao.svideo.model.bean;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：关注直播列表数据
 * 创建人：mj
 * 创建时间：2017/9/19 13:06
 * 修改人：Administrator
 * 修改时间：2017/9/19 13:06
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFollowLiveBean {


    /**
     * live_list : {"items":[{"avatar":"http://ybimg.liveniao.com/20170911D6CA734D0DFC860EBE541BB4F8575418","is_vip":"1","like_num":"2","live_address":"长沙","live_level":"1","live_logo":"http://ybimg.liveniao.com/20170911D6CA734D0DFC860EBE541BB4F8575418","live_price":"0","live_title":"我要门票收费","live_type":"2","nick":"mj","online":"0","uid":"100006","down_url":"rtmp://pili-live-rtmp.youbo.liveniao.com/hn-live-youbo/100006"}],"page":1,"pagesize":50,"pagetotal":1,"total":1,"prev":1,"next":1}
     */

    private LiveListBean live_list;

    public LiveListBean getLive_list() {
        return live_list;
    }

    public void setLive_list(LiveListBean live_list) {
        this.live_list = live_list;
    }

    public static class LiveListBean {
        /**
         * items : [{"avatar":"http://ybimg.liveniao.com/20170911D6CA734D0DFC860EBE541BB4F8575418","is_vip":"1","like_num":"2","live_address":"长沙","live_level":"1","live_logo":"http://ybimg.liveniao.com/20170911D6CA734D0DFC860EBE541BB4F8575418","live_price":"0","live_title":"我要门票收费","live_type":"2","nick":"mj","online":"0","uid":"100006","down_url":"rtmp://pili-live-rtmp.youbo.liveniao.com/hn-live-youbo/100006"}]
         * page : 1
         * pagesize : 50
         * pagetotal : 1
         * total : 1
         * prev : 1
         * next : 1
         */

        private int page;
        private int pagesize;
        private int pagetotal;
        private int total;
        private int prev;
        private int next;
        private List<ItemsBean> items;

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

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPrev() {
            return prev;
        }

        public void setPrev(int prev) {
            this.prev = prev;
        }

        public int getNext() {
            return next;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * avatar : http://ybimg.liveniao.com/20170911D6CA734D0DFC860EBE541BB4F8575418
             * is_vip : 1
             * like_num : 2
             * live_address : 长沙
             * live_level : 1
             * live_logo : http://ybimg.liveniao.com/20170911D6CA734D0DFC860EBE541BB4F8575418
             * live_price : 0
             * live_title : 我要门票收费
             * live_type : 2
             * nick : mj
             * online : 0
             * uid : 100006
             * down_url : rtmp://pili-live-rtmp.youbo.liveniao.com/hn-live-youbo/100006
             */

            private String avatar;
            private String is_vip;
            private String like_num;
            private String live_address;
            private String live_level;
            private String live_logo;
            private String live_price;
            private String live_title;
            private String live_type;
            private String nick;
            private String online;
            private String uid;
            private String down_url;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(String is_vip) {
                this.is_vip = is_vip;
            }

            public String getLike_num() {
                return like_num;
            }

            public void setLike_num(String like_num) {
                this.like_num = like_num;
            }

            public String getLive_address() {
                return live_address;
            }

            public void setLive_address(String live_address) {
                this.live_address = live_address;
            }

            public String getLive_level() {
                return live_level;
            }

            public void setLive_level(String live_level) {
                this.live_level = live_level;
            }

            public String getLive_logo() {
                return live_logo;
            }

            public void setLive_logo(String live_logo) {
                this.live_logo = live_logo;
            }

            public String getLive_price() {
                return live_price;
            }

            public void setLive_price(String live_price) {
                this.live_price = live_price;
            }

            public String getLive_title() {
                return live_title;
            }

            public void setLive_title(String live_title) {
                this.live_title = live_title;
            }

            public String getLive_type() {
                return live_type;
            }

            public void setLive_type(String live_type) {
                this.live_type = live_type;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getOnline() {
                return online;
            }

            public void setOnline(String online) {
                this.online = online;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getDown_url() {
                return down_url;
            }

            public void setDown_url(String down_url) {
                this.down_url = down_url;
            }
        }
    }
}
