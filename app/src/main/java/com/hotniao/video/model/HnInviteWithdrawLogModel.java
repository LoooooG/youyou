package com.hotniao.video.model;


import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：充值记录列表数据
 * 创建人：mj
 * 创建时间：2016/9/30 0030 16:30
 * 修改人：
 * 修改时间： 2016/9/30 0030 16:30
 * 修改备注：
 * Version:  1.0.0
 */
public class HnInviteWithdrawLogModel extends BaseResponseModel {


    /**
     * d : {"amount_total":"592.00","record_list":{"items":[{"gift":{"consume":"10.00","live_gift_logo":"","live_gift_name":"小宝剑","live_gift_number":"1","time":"1509351631"},"user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171030/1509357007525991.png","user_id":"13","user_nickname":"18576677537"}}],"next":2,"page":1,"pagesize":1,"pagetotal":61,"prev":1,"total":61}}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {


        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * account : 测试内容xf8o
             * cash : 86660
             * id : 46470
             * pay : 测试内容s22x
             * status : 测试内容cd2j
             * time : 43384
             */

            private String account;
            private int cash;
            private int id;
            private String pay;
            private String status;
            private int time;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public int getCash() {
                return cash;
            }

            public void setCash(int cash) {
                this.cash = cash;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPay() {
                return pay;
            }

            public void setPay(String pay) {
                this.pay = pay;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }
        }
    }
}
