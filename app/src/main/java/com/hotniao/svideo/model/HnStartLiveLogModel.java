package com.hotniao.svideo.model;


import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：充值记录列表数据  开播
 * 创建人：mj
 * 创建时间：2016/9/30 0030 16:30
 * 修改人：
 * 修改时间： 2016/9/30 0030 16:30
 * 修改备注：
 * Version:  1.0.0
 */
public class HnStartLiveLogModel extends BaseResponseModel {


    /**
     * d : {"amount_total":"30.00","record_list":{"items":[{"live":{"anchor_live_pay":"0","consume":"10.00","time":"1509343476"},"user":{"user_avatar":"","user_id":"12","user_nickname":"17512052316"}}],"next":2,"page":1,"pagesize":1,"pagetotal":3,"prev":1,"total":3}}
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
         * amount_total : 30.00
         * record_list : {"items":[{"live":{"anchor_live_pay":"0","consume":"10.00","time":"1509343476"},"user":{"user_avatar":"","user_id":"12","user_nickname":"17512052316"}}],"next":2,"page":1,"pagesize":1,"pagetotal":3,"prev":1,"total":3}
         */

        private String amount_total;
        private RecordListBean record_list;

        public String getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(String amount_total) {
            this.amount_total = amount_total;
        }

        public RecordListBean getRecord_list() {
            return record_list;
        }

        public void setRecord_list(RecordListBean record_list) {
            this.record_list = record_list;
        }

        public static class RecordListBean {
            /**
             * items : [{"live":{"anchor_live_pay":"0","consume":"10.00","time":"1509343476"},"user":{"user_avatar":"","user_id":"12","user_nickname":"17512052316"}}]
             * next : 2
             * page : 1
             * pagesize : 1
             * pagetotal : 3
             * prev : 1
             * total : 3
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
                 * live : {"anchor_live_pay":"0","consume":"10.00","time":"1509343476"}
                 * user : {"user_avatar":"","user_id":"12","user_nickname":"17512052316"}
                 */

                private LiveBean live;
                private UserBean user;

                public LiveBean getLive() {
                    return live;
                }

                public void setLive(LiveBean live) {
                    this.live = live;
                }

                public UserBean getUser() {
                    return user;
                }

                public void setUser(UserBean user) {
                    this.user = user;
                }

                public static class LiveBean {
                    /**
                     * anchor_live_pay : 0
                     * consume : 10.00
                     * time : 1509343476
                     */

                    private String anchor_live_pay;
                    private String consume;
                    private String time;

                    public String getAnchor_live_pay() {
                        return anchor_live_pay;
                    }

                    public void setAnchor_live_pay(String anchor_live_pay) {
                        this.anchor_live_pay = anchor_live_pay;
                    }

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
                }

                public static class UserBean {
                    /**
                     * user_avatar :
                     * user_id : 12
                     * user_nickname : 17512052316
                     */

                    private String user_avatar;
                    private String user_id;
                    private String user_nickname;

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

                    public String getUser_nickname() {
                        return user_nickname;
                    }

                    public void setUser_nickname(String user_nickname) {
                        this.user_nickname = user_nickname;
                    }
                }
            }
        }
    }
}