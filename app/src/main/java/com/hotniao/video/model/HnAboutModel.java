package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：关于我们
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnAboutModel extends BaseResponseModel {


    /**
     * d : {"about_us":[{"id":"1","time":"0","title":"关于我们"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<AboutUsBean> about_us;

        public List<AboutUsBean> getAbout_us() {
            return about_us;
        }

        public void setAbout_us(List<AboutUsBean> about_us) {
            this.about_us = about_us;
        }

        public static class AboutUsBean {
            /**
             * id : 1
             * time : 0
             * title : 关于我们
             */

            private String id;
            private String time;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
        }
    }
}
