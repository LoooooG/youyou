package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：粉丝贡献榜   主播榜   用户贡献榜
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnFansContributeModel extends BaseResponseModel {


    /**
     * d : {"anchor":{"anchor_ranking":"暂无排名","is_member":"N","live_gift_coin":"1.0000","user_avatar":"","user_id":"8","user_level":"0","user_nickname":"2"},"deadline":"1509292799","rank":{"items":[{"is_member":"Y","live_gift_coin":"1.0000","user_avatar":"","user_id":"8","user_level":"0","user_nickname":"2"}],"next":1,"page":1,"pagesize":10,"pagetotal":1,"prev":1,"total":1}}
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
         * anchor : {"anchor_ranking":"暂无排名","is_member":"N","live_gift_coin":"1.0000","user_avatar":"","user_id":"8","user_level":"0","user_nickname":"2"}
         * deadline : 1509292799
         * rank : {"items":[{"is_member":"Y","live_gift_coin":"1.0000","user_avatar":"","user_id":"8","user_level":"0","user_nickname":"2"}],"next":1,"page":1,"pagesize":10,"pagetotal":1,"prev":1,"total":1}
         */

        private AnchorBean anchor;
        private String deadline;
        private RankBean rank;

        public AnchorBean getAnchor() {
            return anchor;
        }

        public void setAnchor(AnchorBean anchor) {
            this.anchor = anchor;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public RankBean getRank() {
            return rank;
        }

        public void setRank(RankBean rank) {
            this.rank = rank;
        }

        public static class AnchorBean {
            /**
             * anchor_ranking : 暂无排名
             * is_member : N
             * live_gift_coin : 1.0000
             * user_avatar :
             * user_id : 8
             * user_level : 0
             * user_nickname : 2
             */

            private String anchor_ranking;
            private String is_member;
            private String live_gift_coin;
            private String user_avatar;
            private String user_id;
            private String user_level;
            private String user_nickname;

            public String getAnchor_ranking() {
                return anchor_ranking;
            }

            public void setAnchor_ranking(String anchor_ranking) {
                this.anchor_ranking = anchor_ranking;
            }

            public String getIs_member() {
                return is_member;
            }

            public void setIs_member(String is_member) {
                this.is_member = is_member;
            }

            public String getLive_gift_coin() {
                return live_gift_coin;
            }

            public void setLive_gift_coin(String live_gift_coin) {
                this.live_gift_coin = live_gift_coin;
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

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }
        }

        public static class RankBean {
            /**
             * items : [{"is_member":"Y","live_gift_coin":"1.0000","user_avatar":"","user_id":"8","user_level":"0","user_nickname":"2"}]
             * next : 1
             * page : 1
             * pagesize : 10
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
                 * is_member : Y
                 * live_gift_coin : 1.0000
                 * user_avatar :
                 * user_id : 8
                 * user_level : 0
                 * user_nickname : 2
                 */

                private String is_member;
                private String live_gift_coin;
                private String user_avatar;
                private String user_id;
                private String user_level;
                private String user_nickname;
                private String is_follow;
                private String live_gift_dot;
                public String getLive_gift_dot() {
                    return live_gift_dot;
                }

                public void setLive_gift_dot(String live_gift_dot) {
                    this.live_gift_dot = live_gift_dot;
                }
                public String getIs_follow() {
                    return is_follow;
                }

                public void setIs_follow(String is_follow) {
                    this.is_follow = is_follow;
                }

                public String getIs_member() {
                    return is_member;
                }

                public void setIs_member(String is_member) {
                    this.is_member = is_member;
                }

                public String getLive_gift_coin() {
                    return live_gift_coin;
                }

                public void setLive_gift_coin(String live_gift_coin) {
                    this.live_gift_coin = live_gift_coin;
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

                public String getUser_level() {
                    return user_level;
                }

                public void setUser_level(String user_level) {
                    this.user_level = user_level;
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
