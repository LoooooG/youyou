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
public class HnRewardLogModel extends BaseResponseModel {


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
             * consume : 57050
             * create_time : 66872
             * invitee_user_nickname : 测试内容40oc
             * user_invite_reward_type : 测试内容8gj9
             */

            private double consume;
            private long create_time;
            private String invitee_user_nickname;
            private String user_invite_reward_type;

            public double getConsume() {
                return consume;
            }

            public void setConsume(double consume) {
                this.consume = consume;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getInvitee_user_nickname() {
                return invitee_user_nickname;
            }

            public void setInvitee_user_nickname(String invitee_user_nickname) {
                this.invitee_user_nickname = invitee_user_nickname;
            }

            public String getUser_invite_reward_type() {
                return user_invite_reward_type;
            }

            public void setUser_invite_reward_type(String user_invite_reward_type) {
                this.user_invite_reward_type = user_invite_reward_type;
            }
        }
    }
}
