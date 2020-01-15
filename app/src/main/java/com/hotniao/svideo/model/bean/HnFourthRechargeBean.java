package com.hotniao.svideo.model.bean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：第四方充值数据
 * 创建人：mj
 * 创建时间：2017/9/16 18:18
 * 修改人：Administrator
 * 修改时间：2017/9/16 18:18
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFourthRechargeBean {


    /**
     * pay_type : fourth
     * pay_info : {"pay_no":"20170906115406eedd67c9e1c4","pay_url":"http://192.168.60.245:8088/h5/index/testPay/MjAxNzA5MDYxMTU0MDZlZWRkNjdjOWUxYzQ="}
     */

    private String pay_type;
    private PayInfoBean pay_info;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public PayInfoBean getPay_info() {
        return pay_info;
    }

    public void setPay_info(PayInfoBean pay_info) {
        this.pay_info = pay_info;
    }

    public static class PayInfoBean {
        /**
         * pay_no : 20170906115406eedd67c9e1c4
         * pay_url : http://192.168.60.245:8088/h5/index/testPay/MjAxNzA5MDYxMTU0MDZlZWRkNjdjOWUxYzQ=
         */

        private String pay_no;
        private String pay_url;

        public String getPay_no() {
            return pay_no;
        }

        public void setPay_no(String pay_no) {
            this.pay_no = pay_no;
        }

        public String getPay_url() {
            return pay_url;
        }

        public void setPay_url(String pay_url) {
            this.pay_url = pay_url;
        }
    }
}
