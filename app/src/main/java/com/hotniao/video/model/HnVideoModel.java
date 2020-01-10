package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频列表
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoModel extends BaseResponseModel {


    /**
     * d : {"items":[{"category_id":"测试内容hus8","category_name":"热门","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515123949","id":"2","like_num":"0","reply_num":"0","title":"我们不一样","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432","watch_num":"0"},{"category_id":"测试内容hus8","category_name":"热门","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515133151","id":"3","like_num":"0","reply_num":"0","title":"我们不一样","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432","watch_num":"0"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":2}
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
         * items : [{"category_id":"测试内容hus8","category_name":"热门","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515123949","id":"2","like_num":"0","reply_num":"0","title":"我们不一样","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432","watch_num":"0"},{"category_id":"测试内容hus8","category_name":"热门","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","create_time":"1515133151","id":"3","like_num":"0","reply_num":"0","title":"我们不一样","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_nickname":"乐播01041439432","watch_num":"0"}]
         * next : 1
         * page : 1
         * pagesize : 20
         * pagetotal : 1
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
             * category_id : 测试内容hus8
             * category_name : 热门
             * cover : https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg
             * create_time : 1515123949
             * id : 2
             * like_num : 0
             * reply_num : 0
             * title : 我们不一样
             * user_avatar : https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png
             * user_nickname : 乐播01041439432
             * watch_num : 0
             */

            private String category_id;
            private String category_name;
            private String cover;
            private String create_time;
            private String id;
            private String like_num;
            private String reply_num;
            private String title;
            private String user_avatar;
            private String user_nickname;
            private String watch_num;
            private String duration;
            private String price;
            private boolean isNeedPay=false;

            public boolean isNeedPay() {
                return isNeedPay;
            }

            public void setNeedPay(boolean needPay) {
                isNeedPay = needPay;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
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

            public String getLike_num() {
                return like_num;
            }

            public void setLike_num(String like_num) {
                this.like_num = like_num;
            }

            public String getReply_num() {
                return reply_num;
            }

            public void setReply_num(String reply_num) {
                this.reply_num = reply_num;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public String getWatch_num() {
                return watch_num;
            }

            public void setWatch_num(String watch_num) {
                this.watch_num = watch_num;
            }
        }
    }
}
