package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：购买VIP记录
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnBuyVipRecordModel extends BaseResponseModel {


    /**
     * d : {"record_list":{"items":[{"vip":{"consume":"100.00","month":"1","time":"1508464338"}}],"next":2,"page":1,"pagesize":1,"pagetotal":14,"prev":1,"total":14}}
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
         * record_list : {"items":[{"vip":{"consume":"100.00","month":"1","time":"1508464338"}}],"next":2,"page":1,"pagesize":1,"pagetotal":14,"prev":1,"total":14}
         */

        private RecordListBean record_list;

        public RecordListBean getRecord_list() {
            return record_list;
        }

        public void setRecord_list(RecordListBean record_list) {
            this.record_list = record_list;
        }

        public static class RecordListBean {
            /**
             * items : [{"vip":{"consume":"100.00","month":"1","time":"1508464338"}}]
             * next : 2
             * page : 1
             * pagesize : 1
             * pagetotal : 14
             * prev : 1
             * total : 14
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
                 * vip : {"consume":"100.00","month":"1","time":"1508464338"}
                 */

                private VipBean vip;

                public VipBean getVip() {
                    return vip;
                }

                public void setVip(VipBean vip) {
                    this.vip = vip;
                }

                public static class VipBean {
                    /**
                     * consume : 100.00
                     * month : 1
                     * time : 1508464338
                     */

                    private String consume;
                    private String month;
                    private String time;

                    public String getConsume() {
                        return consume;
                    }

                    public void setConsume(String consume) {
                        this.consume = consume;
                    }

                    public String getMonth() {
                        return month;
                    }

                    public void setMonth(String month) {
                        this.month = month;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }
                }
            }
        }
    }
}
