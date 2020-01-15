package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取主播礼物
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnAnchorAcquireGiftModel extends BaseResponseModel {

    /**
     * d : {"items":[{"live_gift_coin":"100.0000","live_gift_logo":"http://youbo-1252571077.coscd.myqcloud.com/gift/perfume.png","live_gift_name":"香水","total":"2"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
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
         * items : [{"live_gift_coin":"100.0000","live_gift_logo":"http://youbo-1252571077.coscd.myqcloud.com/gift/perfume.png","live_gift_name":"香水","total":"2"}]
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
             * live_gift_coin : 100.0000
             * live_gift_logo : http://youbo-1252571077.coscd.myqcloud.com/gift/perfume.png
             * live_gift_name : 香水
             * total : 2
             */

            private String live_gift_coin;
            private String live_gift_logo;
            private String live_gift_name;
            private String total;

            public String getLive_gift_coin() {
                return live_gift_coin;
            }

            public void setLive_gift_coin(String live_gift_coin) {
                this.live_gift_coin = live_gift_coin;
            }

            public String getLive_gift_logo() {
                return live_gift_logo;
            }

            public void setLive_gift_logo(String live_gift_logo) {
                this.live_gift_logo = live_gift_logo;
            }

            public String getLive_gift_name() {
                return live_gift_name;
            }

            public void setLive_gift_name(String live_gift_name) {
                this.live_gift_name = live_gift_name;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
        }
    }
}
