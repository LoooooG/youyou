package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：搜索音乐
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMusicSearchModel extends BaseResponseModel {

    /**
     * d : {"items":[{"author":"大壮","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","duration":"27","id":"2","name":"我们不一样","url":"https://lebolive-1255651273.image.myqcloud.com/music/20180105/1515114915983599.mp3"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
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
         * items : [{"author":"大壮","cover":"https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg","duration":"27","id":"2","name":"我们不一样","url":"https://lebolive-1255651273.image.myqcloud.com/music/20180105/1515114915983599.mp3"}]
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
             * author : 大壮
             * cover : https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg
             * duration : 27
             * id : 2
             * name : 我们不一样
             * url : https://lebolive-1255651273.image.myqcloud.com/music/20180105/1515114915983599.mp3
             */

            private String author;
            private String cover;
            private String duration;
            private String id;
            private String name;
            private String url;
            private String localPath;

            public String getLocalPath() {
                return localPath;
            }

            public void setLocalPath(String localPath) {
                this.localPath = localPath;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
