package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取约聊主播
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeFastChatModel extends BaseResponseModel {


    /**
     * d : {"items":[{"anchor_chat_status":"3","anchor_level":1,"user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_id":"274","user_intro":"","user_nickname":"乐播01041439432","user_sex":1}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
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
         * items : [{"anchor_chat_status":"3","anchor_level":1,"user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_id":"274","user_intro":"","user_nickname":"乐播01041439432","user_sex":1}]
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
             * anchor_chat_status : 3
             * anchor_level : 1
             * user_avatar : https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png
             * user_id : 274
             * user_intro :
             * user_nickname : 乐播01041439432
             * user_sex : 1
             */

            private String anchor_chat_status;
            private String anchor_level;
            private String user_avatar;
            private String user_id;
            private String user_intro;
            private String user_nickname;
            private int user_sex;
            private String anchor_chat_price;

            public String getAnchor_chat_price() {
                return anchor_chat_price;
            }

            public void setAnchor_chat_price(String anchor_chat_price) {
                this.anchor_chat_price = anchor_chat_price;
            }

            public String getAnchor_chat_status() {
                return anchor_chat_status;
            }

            public void setAnchor_chat_status(String anchor_chat_status) {
                this.anchor_chat_status = anchor_chat_status;
            }

            public String getAnchor_level() {
                return anchor_level;
            }

            public void setAnchor_level(String anchor_level) {
                this.anchor_level = anchor_level;
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

            public String getUser_intro() {
                return user_intro;
            }

            public void setUser_intro(String user_intro) {
                this.user_intro = user_intro;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }

            public int getUser_sex() {
                return user_sex;
            }

            public void setUser_sex(int user_sex) {
                this.user_sex = user_sex;
            }
        }
    }
}
