package com.hotniao.live.model.bean;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：开播设置用户列表数据
 * 创建人：mj
 * 创建时间：2017/9/7 18:04
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveNoticeBean {


    /**
     * follows : {"items":[{"anchor_level":"0","is_remind":"Y","user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_intro":"卡拉卡拉吧","user_level":"0","user_nickname":"天行哈哈"}],"next":2,"page":1,"pagesize":1,"pagetotal":2,"prev":1,"total":2}
     * remind_total : 2
     */

    private FollowsBean follows;
    private String remind_total;
    private String is_remind;//	全部提醒设置，Y：开启，N：关闭

    public String getIs_remind() {
        return is_remind;
    }

    public void setIs_remind(String is_remind) {
        this.is_remind = is_remind;
    }

    public FollowsBean getFollows() {
        return follows;
    }

    public void setFollows(FollowsBean follows) {
        this.follows = follows;
    }

    public String getRemind_total() {
        return remind_total;
    }

    public void setRemind_total(String remind_total) {
        this.remind_total = remind_total;
    }

    public static class FollowsBean {
        /**
         * items : [{"anchor_level":"0","is_remind":"Y","user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_intro":"卡拉卡拉吧","user_level":"0","user_nickname":"天行哈哈"}]
         * next : 2
         * page : 1
         * pagesize : 1
         * pagetotal : 2
         * prev : 1
         * total : 2
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
             * anchor_level : 0
             * is_remind : Y
             * user_avatar : http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png
             * user_id : 9
             * user_intro : 卡拉卡拉吧
             * user_level : 0
             * user_nickname : 天行哈哈
             */

            private String anchor_level;
            private String is_remind;
            private String user_avatar;
            private String user_id;
            private String user_intro;
            private String user_level;
            private String user_nickname;

            public String getAnchor_level() {
                return anchor_level;
            }

            public void setAnchor_level(String anchor_level) {
                this.anchor_level = anchor_level;
            }

            public String getIs_remind() {
                return is_remind;
            }

            public void setIs_remind(String is_remind) {
                this.is_remind = is_remind;
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
