package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取约聊对话列表
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnFastVideoListModel extends BaseResponseModel {


    /**
     * d : {"items":[{"id":"测试内容021n","invitee_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","invitee_id":"测试内容s93k","invitee_nickname":"乐播01041439432","inviter_id":"测试内容3jpb","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
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
         * items : [{"id":"测试内容021n","invitee_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","invitee_id":"测试内容s93k","invitee_nickname":"乐播01041439432","inviter_id":"测试内容3jpb","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"}]
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
             * id : 测试内容021n
             * invitee_avatar : https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png
             * invitee_id : 测试内容s93k
             * invitee_nickname : 乐播01041439432
             * inviter_id : 测试内容3jpb
             * user_avatar : https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png
             * user_nickname : 乐播01041439432
             */

            private String id;
            private String invitee_avatar;
            private String invitee_id;
            private String invitee_nickname;
            private String inviter_id;
            private String user_avatar;
            private String user_nickname;
            private String update_time;

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getInvitee_avatar() {
                return invitee_avatar;
            }

            public void setInvitee_avatar(String invitee_avatar) {
                this.invitee_avatar = invitee_avatar;
            }

            public String getInvitee_id() {
                return invitee_id;
            }

            public void setInvitee_id(String invitee_id) {
                this.invitee_id = invitee_id;
            }

            public String getInvitee_nickname() {
                return invitee_nickname;
            }

            public void setInvitee_nickname(String invitee_nickname) {
                this.invitee_nickname = invitee_nickname;
            }

            public String getInviter_id() {
                return inviter_id;
            }

            public void setInviter_id(String inviter_id) {
                this.inviter_id = inviter_id;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
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
