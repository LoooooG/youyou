package com.hn.library.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：配置信息
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnConfigModel extends BaseResponseModel {


    /**
     * d : {"coin":"","dot":"","version":{"code":"0","content":"开发版本v1.0.0","create_time":"0","download_url":"","is_force":"N","name":"v1.0.0"}}
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
         * coin :
         * dot :
         * version : {"code":"0","content":"开发版本v1.0.0","create_time":"0","download_url":"","is_force":"N","name":"v1.0.0"}
         */

        private String coin;
        private String dot;
        private VersionBean version;

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getDot() {
            return dot;
        }

        public void setDot(String dot) {
            this.dot = dot;
        }

        public VersionBean getVersion() {
            return version;
        }

        public void setVersion(VersionBean version) {
            this.version = version;
        }

        public static class VersionBean {
            /**
             * code : 0
             * content : 开发版本v1.0.0
             * create_time : 0
             * download_url :
             * is_force : N
             * name : v1.0.0
             */

            private String code;
            private String content;
            private String create_time;
            private String download_url;
            private String is_force;
            private String name;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getDownload_url() {
                return download_url;
            }

            public void setDownload_url(String download_url) {
                this.download_url = download_url;
            }

            public String getIs_force() {
                return is_force;
            }

            public void setIs_force(String is_force) {
                this.is_force = is_force;
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
