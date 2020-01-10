package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：支付宝支付
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnAliPayModel extends BaseResponseModel {


    /**
     * d : {"data":"alipay_sdk=alipay-sdk-php-20161101&app_id=2018011701924286&biz_content=%7B%22body%22%3A%22%5Cu4e50%5Cu64ad%5Cu5145%5Cu503c%22%2C%22subject%22%3A%22%5Cu4e50%5Cu64ad%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22151667151727854718%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%221.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=%2Fv1%2Fnotify%2Fnotify%2FalipayNotify&sign_type=RSA2&timestamp=2018-01-23+09%3A38%3A37&version=1.0&sign=Ic4WosLQN4uVVhGyPvySYmisMFDOAwYw4QDFtulxTVY149KlKN2PPWVSgjahDG0Gf6X%2FH0b3S%2F5DrFJoIjmMBqtoD%2B242s%2FhWo7V0DaDOwuQ0Z80QLoTfWgCiSZwP3nAn0327IvKjJ6LhCYwkIEvTv00UKZjyShnYO3WhRpv3BMx7RU4FDCThwAbYjgccPK7XoxG9k0rOpZg%2BpsYQGQqPmV8V%2BIETfeA7nCftouL0%2BMgok1teY8zKcIH3OXxe%2FQlp9bg8ShAQ%2ByqosxUhLRIN5sOf4G8Wrc2BPFzLQaqPy8u5V3bHSCggrVLuiTXZxOLk0w5ubnkgyRANpD1tWnzag%3D%3D"}
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
         * data : alipay_sdk=alipay-sdk-php-20161101&app_id=2018011701924286&biz_content=%7B%22body%22%3A%22%5Cu4e50%5Cu64ad%5Cu5145%5Cu503c%22%2C%22subject%22%3A%22%5Cu4e50%5Cu64ad%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22151667151727854718%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%221.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=%2Fv1%2Fnotify%2Fnotify%2FalipayNotify&sign_type=RSA2&timestamp=2018-01-23+09%3A38%3A37&version=1.0&sign=Ic4WosLQN4uVVhGyPvySYmisMFDOAwYw4QDFtulxTVY149KlKN2PPWVSgjahDG0Gf6X%2FH0b3S%2F5DrFJoIjmMBqtoD%2B242s%2FhWo7V0DaDOwuQ0Z80QLoTfWgCiSZwP3nAn0327IvKjJ6LhCYwkIEvTv00UKZjyShnYO3WhRpv3BMx7RU4FDCThwAbYjgccPK7XoxG9k0rOpZg%2BpsYQGQqPmV8V%2BIETfeA7nCftouL0%2BMgok1teY8zKcIH3OXxe%2FQlp9bg8ShAQ%2ByqosxUhLRIN5sOf4G8Wrc2BPFzLQaqPy8u5V3bHSCggrVLuiTXZxOLk0w5ubnkgyRANpD1tWnzag%3D%3D
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
