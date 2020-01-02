package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频消息
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoMessageModel extends BaseResponseModel {

    /**
     * d : {"items":[{"content":"","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515145049","id":"2","type":"1","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"},{"content":"测试评论","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515144902","id":"2","type":"2","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"},{"content":"测试评论","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515144856","id":"2","type":"2","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":3}
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
         * items : [{"content":"","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515145049","id":"2","type":"1","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"},{"content":"测试评论","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515144902","id":"2","type":"2","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"},{"content":"测试评论","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515144856","id":"2","type":"2","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432"}]
         * next : 1
         * page : 1
         * pagesize : 20
         * pagetotal : 1
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
             * content :
             * cover : https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg
             * create_time : 1515145049
             * id : 2
             * type : 1
             * user_avatar : https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png
             * user_nickname : 乐播01041439432
             */

            private String content;
            private String cover;
            private String create_time;
            private String id;
            private String type;
            private String user_avatar;
            private String user_nickname;
            private String  is_read;//	是否已读 0 否 1是

            public String getIs_read() {
                return is_read;
            }

            public void setIs_read(String is_read) {
                this.is_read = is_read;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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
