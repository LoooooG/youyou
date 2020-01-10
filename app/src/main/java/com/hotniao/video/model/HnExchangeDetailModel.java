package com.hotniao.video.model;


import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：恋恋
 * 类描述：充值记录列表数据
 * 创建人：mj
 * 创建时间：2016/9/30 0030 16:30
 * 修改人：
 * 修改时间： 2016/9/30 0030 16:30
 * 修改备注：
 * Version:  1.0.0
 */
public class HnExchangeDetailModel extends BaseResponseModel {


    /**
     * d : {"recharge_list":{"items":[{"coin":"2.00","fee":"1.00","id":"24","status":"Y","time":"1510284515"}],"next":1,"page":1,"pagesize":1000,"pagetotal":1,"prev":1,"total":1}}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        /**
         * recharge_list : {"items":[{"coin":"2.00","fee":"1.00","id":"24","status":"Y","time":"1510284515"}],"next":1,"page":1,"pagesize":1000,"pagetotal":1,"prev":1,"total":1}
         */

        private List<ItemsBean> items;



        public static class ItemsBean {
            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }

            public int getExchange_coin() {
                return exchange_coin;
            }

            public void setExchange_coin(int exchange_coin) {
                this.exchange_coin = exchange_coin;
            }

            public int getExchange_money() {
                return exchange_money;
            }

            public void setExchange_money(int exchange_money) {
                this.exchange_money = exchange_money;
            }

            /**
             * items : [{"coin":"2.00","fee":"1.00","id":"24","status":"Y","time":"1510284515"}]
             * next : 1
             * page : 1
             * pagesize : 1000
             * pagetotal : 1
             * prev : 1
             * total : 1
             */

            private int add_time;
            private int exchange_coin;
            private int exchange_money;


        }
    }
}
