package com.hotniao.svideo.model.bean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：获取七牛token数据
 * 创建人：mj
 * 创建时间：2017/9/7 20:50
 * 修改人：Administrator
 * 修改时间：2017/9/7 20:50
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTokenBean {


    /**
     * service : qiniu
     * qiniu : {"token":"OMefIHOcACZGKthK48pmx1ElVQJDUILLSarlT7DH:rwHnCORsX1zWTYaJYeQkGojkhaM=:eyJzY29wZSI6ImxpdmUteW91Ym9zdG9yYWdlIiwiZGVhZGxpbmUiOjE1MDQ3NzkzNjZ9"}
     */

    private String service;
    private QiniuBean qiniu;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public QiniuBean getQiniu() {
        return qiniu;
    }

    public void setQiniu(QiniuBean qiniu) {
        this.qiniu = qiniu;
    }

    public static class QiniuBean {
        /**
         * token : OMefIHOcACZGKthK48pmx1ElVQJDUILLSarlT7DH:rwHnCORsX1zWTYaJYeQkGojkhaM=:eyJzY29wZSI6ImxpdmUteW91Ym9zdG9yYWdlIiwiZGVhZGxpbmUiOjE1MDQ3NzkzNjZ9
         */

        private String token;
        private String url;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
