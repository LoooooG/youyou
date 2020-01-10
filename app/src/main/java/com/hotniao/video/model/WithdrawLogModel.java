package com.hotniao.video.model;


import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：提现记录
 * 创建人：tcz
 * 创建时间：2016/9/30 0030 17:13
 * 修改人：tcz
 * 修改时间： 2016/9/30 0030 17:13
 * 修改备注：
 * Version:  1.0.0
 */
public class WithdrawLogModel extends BaseResponseModel {


    /**
     * d : {"withdraw_log":{"items":[{"account":"18122711280","cash":"1234.00","id":"2","pay":"支付宝","status":"C","time":"1509438556"}],"next":1,"page":1,"pagesize":1,"pagetotal":1,"prev":1,"total":1}}
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
         * withdraw_log : {"items":[{"account":"18122711280","cash":"1234.00","id":"2","pay":"支付宝","status":"C","time":"1509438556"}],"next":1,"page":1,"pagesize":1,"pagetotal":1,"prev":1,"total":1}
         */
        private String withdraw_total;

        public String getWithdraw_total() {
            return withdraw_total;
        }

        public void setWithdraw_total(String withdraw_total) {
            this.withdraw_total = withdraw_total;
        }

        private WithdrawLogBean withdraw_log;

        public WithdrawLogBean getWithdraw_log() {
            return withdraw_log;
        }

        public void setWithdraw_log(WithdrawLogBean withdraw_log) {
            this.withdraw_log = withdraw_log;
        }

        public static class WithdrawLogBean {
            /**
             * items : [{"account":"18122711280","cash":"1234.00","id":"2","pay":"支付宝","status":"C","time":"1509438556"}]
             * next : 1
             * page : 1
             * pagesize : 1
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
                 * account : 18122711280
                 * cash : 1234.00
                 * id : 2
                 * pay : 支付宝
                 * status : C
                 * time : 1509438556
                 */

                private String account;
                private String cash;
                private String id;
                private String pay;
                private String status;
                private String time;

                public String getAccount() {
                    return account;
                }

                public void setAccount(String account) {
                    this.account = account;
                }

                public String getCash() {
                    return cash;
                }

                public void setCash(String cash) {
                    this.cash = cash;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPay() {
                    return pay;
                }

                public void setPay(String pay) {
                    this.pay = pay;
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
