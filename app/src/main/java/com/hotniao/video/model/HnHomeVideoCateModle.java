package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频分类
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeVideoCateModle extends BaseResponseModel {


    /**
     * d : {"video_category":[{"id":"1","name":"热门"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<VideoCategoryBean> video_category;

        public List<VideoCategoryBean> getVideo_category() {
            return video_category;
        }

        public void setVideo_category(List<VideoCategoryBean> video_category) {
            this.video_category = video_category;
        }

        public static class VideoCategoryBean {
            public VideoCategoryBean(String id, String name) {
                this.id = id;
                this.name = name;
            }

            /**
             * id : 1
             * name : 热门
             */

            private String id;
            private String logo;
            private String name;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
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
        }
    }
}
