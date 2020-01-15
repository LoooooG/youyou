package com.hotniao.svideo.model;


import com.hn.library.http.BaseResponseModel;

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
public class HnGeneralizeModel extends BaseResponseModel {


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

        /**
         * total_money : 51456
         * user_invite_reward : 40106
         * user_invite_total : 44277
         */

        private double total_money;
        private double user_invite_reward;
        private int user_invite_total;

        public double getTotal_money() {
            return total_money;
        }

        public void setTotal_money(double total_money) {
            this.total_money = total_money;
        }

        public double getUser_invite_reward() {
            return user_invite_reward;
        }

        public void setUser_invite_reward(double user_invite_reward) {
            this.user_invite_reward = user_invite_reward;
        }

        public int getUser_invite_total() {
            return user_invite_total;
        }

        public void setUser_invite_total(int user_invite_total) {
            this.user_invite_total = user_invite_total;
        }
    }
}
