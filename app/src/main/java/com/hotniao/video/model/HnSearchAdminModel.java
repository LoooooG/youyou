package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：搜索房管
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSearchAdminModel extends BaseResponseModel {

    /**
     * d : {"users":{"items":[{"is_anchor_admin":"Y","user_avatar":"","user_id":"12","user_intro":"","user_level":"0","user_nickname":"17512052316"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}}
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
         * users : {"items":[{"is_anchor_admin":"Y","user_avatar":"","user_id":"12","user_intro":"","user_level":"0","user_nickname":"17512052316"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
         */

        private UsersBean users;

        public UsersBean getUsers() {
            return users;
        }

        public void setUsers(UsersBean users) {
            this.users = users;
        }

        public static class UsersBean {
            /**
             * items : [{"is_anchor_admin":"Y","user_avatar":"","user_id":"12","user_intro":"","user_level":"0","user_nickname":"17512052316"}]
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
                 * is_anchor_admin : Y
                 * user_avatar :
                 * user_id : 12
                 * user_intro :
                 * user_level : 0
                 * user_nickname : 17512052316
                 */

                private String is_anchor_admin;
                private String user_avatar;
                private String user_id;
                private String user_intro;
                private String user_level;
                private String user_nickname;
                private String user_fans_total;

                public String getUser_fans_total() {
                    return user_fans_total;
                }

                public void setUser_fans_total(String user_fans_total) {
                    this.user_fans_total = user_fans_total;
                }

                public String getIs_anchor_admin() {
                    return is_anchor_admin;
                }

                public void setIs_anchor_admin(String is_anchor_admin) {
                    this.is_anchor_admin = is_anchor_admin;
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
}
