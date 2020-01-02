package com.hotniao.live.model;

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

public class HnLiveBackEarnModel extends BaseResponseModel {

    /**
     * d : {"amount_total":"0.20","items":[{"consume":"0.20","time":"1517563499","user_nickname":"刘测试1"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
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
         * amount_total : 0.20
         * items : [{"consume":"0.20","time":"1517563499","user_nickname":"刘测试1"}]
         * next : 1
         * page : 1
         * pagesize : 20
         * pagetotal : 1
         * prev : 1
         * total : 1
         */

        private String amount_total;
        private int next;
        private int page;
        private int pagesize;
        private int pagetotal;
        private int prev;
        private int total;
        private List<ItemsBean> items;

        public String getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(String amount_total) {
            this.amount_total = amount_total;
        }

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
             * consume : 0.20
             * time : 1517563499
             * user_nickname : 刘测试1
             */

            private String consume;
            private String time;
            private String user_nickname;

            public String getConsume() {
                return consume;
            }

            public void setConsume(String consume) {
                this.consume = consume;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
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
