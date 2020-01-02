package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：收益详情  -邀请
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnBillInviteDetailModel extends BaseResponseModel{

    /**
     * d : {"amount_total":"10.0000","record_list":{"items":[{"invite":{"consume":"10.0000","reward_type":"recharge","time":"0"},"user":{"invite_level":"1","user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}}],"next":1,"page":1,"pagesize":1,"pagetotal":1,"prev":1,"total":1}}
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
         * amount_total : 10.0000
         * record_list : {"items":[{"invite":{"consume":"10.0000","reward_type":"recharge","time":"0"},"user":{"invite_level":"1","user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}}],"next":1,"page":1,"pagesize":1,"pagetotal":1,"prev":1,"total":1}
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
             * items : [{"invite":{"consume":"10.0000","reward_type":"recharge","time":"0"},"user":{"invite_level":"1","user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}}]
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
                 * invite : {"consume":"10.0000","reward_type":"recharge","time":"0"}
                 * user : {"invite_level":"1","user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}
                 */

                private InviteBean invite;
                private UserBean user;

                public InviteBean getInvite() {
                    return invite;
                }

                public void setInvite(InviteBean invite) {
                    this.invite = invite;
                }

                public UserBean getUser() {
                    return user;
                }

                public void setUser(UserBean user) {
                    this.user = user;
                }

                public static class InviteBean {
                    /**
                     * consume : 10.0000
                     * reward_type : recharge
                     * time : 0
                     */

                    private String consume;
                    private String reward_type;
                    private String time;

                    public String getConsume() {
                        return consume;
                    }

                    public void setConsume(String consume) {
                        this.consume = consume;
                    }

                    public String getReward_type() {
                        return reward_type;
                    }

                    public void setReward_type(String reward_type) {
                        this.reward_type = reward_type;
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
                     * invite_level : 1
                     * user_avatar : http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png
                     * user_id : 10
                     * user_nickname : 刘测试
                     */

                    private String invite_level;
                    private String user_avatar;
                    private String user_id;
                    private String user_nickname;

                    public String getInvite_level() {
                        return invite_level;
                    }

                    public void setInvite_level(String invite_level) {
                        this.invite_level = invite_level;
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
