package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：轮播图
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnBannerModel extends BaseResponseModel {


    /**
     * d : {"carousel":[{"carousel_href":"http://www.baidu.com","carousel_id":"12","carousel_url":"http://www.baidu.com"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<CarouselBean> carousel;

        public List<CarouselBean> getCarousel() {
            return carousel;
        }

        public void setCarousel(List<CarouselBean> carousel) {
            this.carousel = carousel;
        }

        public static class CarouselBean {
            /**
             * carousel_href : http://www.baidu.com
             * carousel_id : 12
             * carousel_url : http://www.baidu.com
             */

            private String carousel_href;
            private String carousel_id;
            private String carousel_url;

            public String getCarousel_href() {
                return carousel_href;
            }

            public void setCarousel_href(String carousel_href) {
                this.carousel_href = carousel_href;
            }

            public String getCarousel_id() {
                return carousel_id;
            }

            public void setCarousel_id(String carousel_id) {
                this.carousel_id = carousel_id;
            }

            public String getCarousel_url() {
                return carousel_url;
            }

            public void setCarousel_url(String carousel_url) {
                this.carousel_url = carousel_url;
            }
        }
    }
}
