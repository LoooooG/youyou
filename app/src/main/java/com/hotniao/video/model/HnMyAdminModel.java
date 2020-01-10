package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我的房管
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMyAdminModel extends BaseResponseModel {


    /**
     * d : {"anchor_admin":{"items":[{"anchor_level":"0","user_avatar":"","user_fans_total":"0","user_id":"12","user_level":"0","user_nickname":"17512052316"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}}
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
         * anchor_admin : {"items":[{"anchor_level":"0","user_avatar":"","user_fans_total":"0","user_id":"12","user_level":"0","user_nickname":"17512052316"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
         */

        private AnchorAdminBean anchor_admin;

        public AnchorAdminBean getAnchor_admin() {
            return anchor_admin;
        }

        public void setAnchor_admin(AnchorAdminBean anchor_admin) {
            this.anchor_admin = anchor_admin;
        }

        public static class AnchorAdminBean {
            /**
             * items : [{"anchor_level":"0","user_avatar":"","user_fans_total":"0","user_id":"12","user_level":"0","user_nickname":"17512052316"}]
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
                 * anchor_level : 0
                 * user_avatar :
                 * user_fans_total : 0
                 * user_id : 12
                 * user_level : 0
                 * user_nickname : 17512052316
                 */

                private String anchor_level;
                private String user_avatar;
                private String user_fans_total;
                private String user_id;
                private String user_level;
                private String user_nickname;
                private String is_anchor_admin;//是否房管
                private String user_intro;//简介

                public String getIs_anchor_admin() {
                    return is_anchor_admin;
                }

                public void setIs_anchor_admin(String is_anchor_admin) {
                    this.is_anchor_admin = is_anchor_admin;
                }

                public String getUser_intro() {
                    return user_intro;
                }

                public void setUser_intro(String user_intro) {
                    this.user_intro = user_intro;
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

                public String getUser_fans_total() {
                    return user_fans_total;
                }

                public void setUser_fans_total(String user_fans_total) {
                    this.user_fans_total = user_fans_total;
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
