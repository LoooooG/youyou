package com.hotniao.svideo.model;


import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：充值记录列表数据
 * 创建人：mj
 * 创建时间：2016/9/30 0030 16:30
 * 修改人：
 * 修改时间： 2016/9/30 0030 16:30
 * 修改备注：
 * Version:  1.0.0
 */
public class PayLogModel extends BaseResponseModel {


    /**
     * d : {"recharge_list":{"items":[{"coin":"2.00","fee":"1.00","id":"24","status":"Y","time":"1510284515"}],"next":1,"page":1,"pagesize":1000,"pagetotal":1,"prev":1,"total":1}}
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
         * recharge_list : {"items":[{"coin":"2.00","fee":"1.00","id":"24","status":"Y","time":"1510284515"}],"next":1,"page":1,"pagesize":1000,"pagetotal":1,"prev":1,"total":1}
         */

        private RechargeListBean recharge_list;

        public RechargeListBean getRecharge_list() {
            return recharge_list;
        }

        public void setRecharge_list(RechargeListBean recharge_list) {
            this.recharge_list = recharge_list;
        }

        public static class RechargeListBean {
            /**
             * items : [{"coin":"2.00","fee":"1.00","id":"24","status":"Y","time":"1510284515"}]
             * next : 1
             * page : 1
             * pagesize : 1000
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
                 * coin : 2.00
                 * fee : 1.00
                 * id : 24
                 * status : Y
                 * time : 1510284515
                 */

                private String coin;
                private String fee;
                private String id;
                private String status;
                private String time;

                public String getCoin() {
                    return coin;
                }

                public void setCoin(String coin) {
                    this.coin = coin;
                }

                public String getFee() {
                    return fee;
                }

                public void setFee(String fee) {
                    this.fee = fee;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
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
