package com.hotniao.livelibrary.model.bean;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物列表数据
 * 创建人：mj
 * 创建时间：2017/9/16 16:38
 * 修改人：Administrator
 * 修改时间：2017/9/16 16:38
 * 修改备注：
 * Version:  1.0.0
 */
public class HnGiftListBean {

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private List<GiftBean> gift;

    public List<GiftBean> getGift() {
        return gift;
    }

    public void setGift(List<GiftBean> gift) {
        this.gift = gift;
    }

    public static class GiftBean {
        /**
         * items : [{"animation":"","coin":"1000000000","detail":"","icon":"","icon_gif":"","id":"1","name":"大宝剑","status":"1"}]
         * id : 1
         * name : 大礼物
         */

        private int id;
        private String name;
        private List<ItemsBean> items;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * animation :
             * coin : 1000000000
             * detail :
             * icon :
             * icon_gif :
             * id : 1
             * name : 大宝剑
             * status : 1
             */

            private String animation;
            private String coin;
            private String detail;
            private String icon;
            private String icon_gif;
            private String id;
            private String name;
            private String status;
            private String sort;
            private String localGifPath;//本地gif图
            private boolean isCheck;

            public String getLocalGifPath() {
                return localGifPath;
            }

            public void setLocalGifPath(String localGifPath) {
                this.localGifPath = localGifPath;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public String getAnimation() {
                return animation;
            }

            public void setAnimation(String animation) {
                this.animation = animation;
            }

            public String getCoin() {
                return coin;
            }

            public void setCoin(String coin) {
                this.coin = coin;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIcon_gif() {
                return icon_gif;
            }

            public void setIcon_gif(String icon_gif) {
                this.icon_gif = icon_gif;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
