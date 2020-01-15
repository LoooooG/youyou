package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：游戏下注收益
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnGameEarningModel extends BaseResponseModel {


    /**
     * d : {"amount_total":"40.00","record_list":{"items":[{"game":{"consume":"20.00","consume_category":"下注成功赢钱","game_name":"开心牛仔","time":"1512120283"}}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":2}}
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
         * amount_total : 40.00
         * record_list : {"items":[{"game":{"consume":"20.00","consume_category":"下注成功赢钱","game_name":"开心牛仔","time":"1512120283"}}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":2}
         */

        private String amount_total;
        private RecordListBean record_list;

        public String getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(String amount_total) {
            this.amount_total = amount_total;
        }

        public RecordListBean getRecord_list() {
            return record_list;
        }

        public void setRecord_list(RecordListBean record_list) {
            this.record_list = record_list;
        }

        public static class RecordListBean {
            /**
             * items : [{"game":{"consume":"20.00","consume_category":"下注成功赢钱","game_name":"开心牛仔","time":"1512120283"}}]
             * next : 1
             * page : 1
             * pagesize : 20
             * pagetotal : 1
             * prev : 1
             * total : 2
             */

            private int next;
            private int page;
            private int pagesize;
            private int pagetotal;
            private int prev;
            private int total;
            private List<ItemsBean> items;

            public int getNext() {
                return next;
            }

            public void setNext(int next) {
                this.next = next;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getPagesize() {
                return pagesize;
            }

            public void setPagesize(int pagesize) {
                this.pagesize = pagesize;
            }

            public int getPagetotal() {
                return pagetotal;
            }

            public void setPagetotal(int pagetotal) {
                this.pagetotal = pagetotal;
            }

            public int getPrev() {
                return prev;
            }

            public void setPrev(int prev) {
                this.prev = prev;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean {
                /**
                 * game : {"consume":"20.00","consume_category":"下注成功赢钱","game_name":"开心牛仔","time":"1512120283"}
                 */

                private GameBean game;

                public GameBean getGame() {
                    return game;
                }

                public void setGame(GameBean game) {
                    this.game = game;
                }

                public static class GameBean {
                    /**
                     * consume : 20.00
                     * consume_category : 下注成功赢钱
                     * game_name : 开心牛仔
                     * time : 1512120283
                     */

                    private String consume;
                    private String consume_category;
                    private String game_name;
                    private String time;

                    public String getConsume() {
                        return consume;
                    }

                    public void setConsume(String consume) {
                        this.consume = consume;
                    }

                    public String getConsume_category() {
                        return consume_category;
                    }

                    public void setConsume_category(String consume_category) {
                        this.consume_category = consume_category;
                    }

                    public String getGame_name() {
                        return game_name;
                    }

                    public void setGame_name(String game_name) {
                        this.game_name = game_name;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }
                }
            }
        }
    }
}
