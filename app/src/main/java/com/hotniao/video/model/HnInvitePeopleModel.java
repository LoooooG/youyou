package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：邀请的用户
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnInvitePeopleModel extends BaseResponseModel {

    /**
     * d : {"invite_user":{"items":[{"user_avatar":"测试内容gl5x","user_id":"0","user_invite_total":"测试内容56k8","user_nicknname":""}],"next":0,"page":1,"pagesize":20,"pagetotal":0,"prev":1,"total":0}}
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
         * invite_user : {"items":[{"user_avatar":"测试内容gl5x","user_id":"0","user_invite_total":"测试内容56k8","user_nicknname":""}],"next":0,"page":1,"pagesize":20,"pagetotal":0,"prev":1,"total":0}
         */

        private InviteUserBean invite_user;

        public InviteUserBean getInvite_user() {
            return invite_user;
        }

        public void setInvite_user(InviteUserBean invite_user) {
            this.invite_user = invite_user;
        }

        public static class InviteUserBean {
            /**
             * items : [{"user_avatar":"测试内容gl5x","user_id":"0","user_invite_total":"测试内容56k8","user_nicknname":""}]
             * next : 0
             * page : 1
             * pagesize : 20
             * pagetotal : 0
             * prev : 1
             * total : 0
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
                 * user_avatar : 测试内容gl5x
                 * user_id : 0
                 * user_invite_total : 测试内容56k8
                 * user_nicknname :
                 */

                private String user_avatar;
                private String user_id;
                private String user_invite_total;
                private String user_nickname;
                private String user_level;

                public String getUser_level() {
                    return user_level;
                }

                public void setUser_level(String user_level) {
                    this.user_level = user_level;
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

                public String getUser_invite_total() {
                    return user_invite_total;
                }

                public void setUser_invite_total(String user_invite_total) {
                    this.user_invite_total = user_invite_total;
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
