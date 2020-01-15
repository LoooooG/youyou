package com.hotniao.svideo.model.bean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：版本检测
 * 创建人：mj
 * 创建时间：2017/9/22 13:16
 * 修改人：Administrator
 * 修改时间：2017/9/22 13:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnCheckVersionBean {


    /**
     * app_version : {"number":"1","version":"0.0.1","download":"https://g37.gdl.netease.com/onmyoji_netease_10_1.0.23.apk","market_link":"http://app.xiaomi.com/details?id=com.ss.android.article.news&back=true&ref=mobileWeb","detail":"第一个版本"}
     * wallet_name : {"coin":"优币","dot":"优票"}
     */

    private AppVersionBean app_version;
    private WalletNameBean wallet_name;

    public AppVersionBean getApp_version() {
        return app_version;
    }

    public void setApp_version(AppVersionBean app_version) {
        this.app_version = app_version;
    }

    public WalletNameBean getWallet_name() {
        return wallet_name;
    }

    public void setWallet_name(WalletNameBean wallet_name) {
        this.wallet_name = wallet_name;
    }

    public static class AppVersionBean {
        /**
         * number : 1
         * version : 0.0.1
         * download : https://g37.gdl.netease.com/onmyoji_netease_10_1.0.23.apk
         * market_link : http://app.xiaomi.com/details?id=com.ss.android.article.news&back=true&ref=mobileWeb
         * detail : 第一个版本
         */

        private String number;
        private String version;
        private String download;
        private String market_link;
        private String detail;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getMarket_link() {
            return market_link;
        }

        public void setMarket_link(String market_link) {
            this.market_link = market_link;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    public static class WalletNameBean {
        /**
         * coin : 优币
         * dot : 优票
         */

        private String coin;
        private String dot;

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
    }
}
