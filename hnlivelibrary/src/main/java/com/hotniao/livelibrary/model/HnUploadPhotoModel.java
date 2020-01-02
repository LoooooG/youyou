package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：图片上传签名   appid
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUploadPhotoModel extends BaseResponseModel {


    /**
     * d : {"config":{"app_id":"1252571077","bucket":"fireniao","region":"gz","sign":"b24ge4b030m8d1KjRSC0F+vqdqxhPTEyNTI1NzEwNzcmYj1maXJlbmlhbyZrPUFLSURLajA4NHVNNExYWEx5T3BzdzMxcld5SWd2eXJlOGMyMSZlPTE1MTMwNTQyMzImdD0xNTEyNDQ5NDMyJnI9MTE0NTcwMjYyMyZmPQ=="}}
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
         * config : {"app_id":"1252571077","bucket":"fireniao","region":"gz","sign":"b24ge4b030m8d1KjRSC0F+vqdqxhPTEyNTI1NzEwNzcmYj1maXJlbmlhbyZrPUFLSURLajA4NHVNNExYWEx5T3BzdzMxcld5SWd2eXJlOGMyMSZlPTE1MTMwNTQyMzImdD0xNTEyNDQ5NDMyJnI9MTE0NTcwMjYyMyZmPQ=="}
         */

        private ConfigBean config;

        public ConfigBean getConfig() {
            return config;
        }

        public void setConfig(ConfigBean config) {
            this.config = config;
        }

        public static class ConfigBean {
            @Override
            public String toString() {
                return "ConfigBean{" +
                        "appId='" + appId + '\'' +
                        ", bucket='" + bucket + '\'' +
                        ", region='" + region + '\'' +
                        ", sign='" + sign + '\'' +
                        ", image_cdn='" + image_cdn + '\'' +
                        '}';
            }

            /**
             * app_id : 1252571077
             * bucket : fireniao
             * region : gz
             * sign : b24ge4b030m8d1KjRSC0F+vqdqxhPTEyNTI1NzEwNzcmYj1maXJlbmlhbyZrPUFLSURLajA4NHVNNExYWEx5T3BzdzMxcld5SWd2eXJlOGMyMSZlPTE1MTMwNTQyMzImdD0xNTEyNDQ5NDMyJnI9MTE0NTcwMjYyMyZmPQ==
             */

            private String appId;
            private String bucket;
            private String region;
            private String sign;
            private String image_cdn;

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getImage_cdn() {
                return image_cdn;
            }

            public void setImage_cdn(String image_cdn) {
                this.image_cdn = image_cdn;
            }

            public String getBucket() {
                return bucket;
            }

            public void setBucket(String bucket) {
                this.bucket = bucket;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }
    }
}
