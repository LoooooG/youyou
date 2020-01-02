package com.hotniao.live.model;


import com.hn.library.http.BaseResponseModel;

import java.util.List;

public class HnExchangeModel extends BaseResponseModel {


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


        private List<ItemsBean> items;


        public static class ItemsBean {

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            private int coin;
            private int id;
            private int money;

        }
    }
}
