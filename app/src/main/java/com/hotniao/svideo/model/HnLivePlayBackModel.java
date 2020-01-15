package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLivePlayBackModel extends BaseResponseModel {


    /**
     * d : {"videos":{"items":[{"category_name":["NBA","免费"],"end_time":"0","image_url":"","onlines":"0","playback_url":"","start_time":"1508749383","time":"0","title":""}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}}
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
         * videos : {"items":[{"category_name":["NBA","免费"],"end_time":"0","image_url":"","onlines":"0","playback_url":"","start_time":"1508749383","time":"0","title":""}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
         */

        private VideosBean videos;

        public VideosBean getVideos() {
            return videos;
        }

        public void setVideos(VideosBean videos) {
            this.videos = videos;
        }

        public static class VideosBean {
            /**
             * items : [{"category_name":["NBA","免费"],"end_time":"0","image_url":"","onlines":"0","playback_url":"","start_time":"1508749383","time":"0","title":""}]
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
                 * category_name : ["NBA","免费"]
                 * end_time : 0
                 * image_url :
                 * onlines : 0
                 * playback_url :
                 * start_time : 1508749383
                 * time : 0
                 * title :
                 */

                private String end_time;
                private String image_url;
                private String onlines;
                private String playback_url;
                private String start_time;
                private String time;
                private String title;
                private String anchor_live_log_id;
                private String playback_price;
                private List<String> category_name;

                public String getAnchor_live_log_id() {
                    return anchor_live_log_id;
                }

                public void setAnchor_live_log_id(String anchor_live_log_id) {
                    this.anchor_live_log_id = anchor_live_log_id;
                }

                public String getPlayback_price() {
                    return playback_price;
                }

                public void setPlayback_price(String playback_price) {
                    this.playback_price = playback_price;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public String getImage_url() {
                    return image_url;
                }

                public void setImage_url(String image_url) {
                    this.image_url = image_url;
                }

                public String getOnlines() {
                    return onlines;
                }

                public void setOnlines(String onlines) {
                    this.onlines = onlines;
                }

                public String getPlayback_url() {
                    return playback_url;
                }

                public void setPlayback_url(String playback_url) {
                    this.playback_url = playback_url;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public List<String> getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(List<String> category_name) {
                    this.category_name = category_name;
                }
            }
        }
    }
}
