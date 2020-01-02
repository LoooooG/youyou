package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2018,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：众赢
 * 类描述：
 * 创建人：李柯
 * 创建时间：2018/10/23 10:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnEspensesRecordModel extends BaseResponseModel {
    private DBean d;

    public DBean getdBean() {
        return d;
    }

    public void setdBean(DBean dBean) {
        this.d = dBean;
    }

    public static class DBean {
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
            private String time;
            private String user_nickname;
            private String consume;

            public String getConsume() {
                return consume;
            }

            public void setConsume(String consume) {
                this.consume = consume;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }


        }
    }
}
