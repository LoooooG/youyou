package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2018,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：众赢
 * 类描述：
 * 创建人：李柯
 * 创建时间：2018/10/25 15:51
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBlackListBean extends BaseResponseModel {

    private DBean d;

    public  DBean getdBean() {
        return d;
    }

    public void setdBean(DBean dBean) {
        this.d = dBean;
    }

    public static class DBean {

       /* public BlackListBean getBlackList() {
            return blackList;
        }

        public void setBlackList(BlackListBean blackList) {
            this.blackList = blackList;
        }

        private BlackListBean blackList;

        public static class BlackListBean {
            @Override
            public String toString() {
                return "BlackListBean{" +
                        "items=" + items +
                        ", next=" + next +
                        ", page=" + page +
                        ", pagesize=" + pagesize +
                        ", pagetotal=" + pagetotal +
                        ", prev=" + prev +
                        ", total=" + total +
                        '}';
            }*/

            List<ItemsBean> items;
            int next;
            int page;
            int pagesize;
            int pagetotal;
            int prev;
            int total;

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

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

            public static class ItemsBean {
                int create_time;
                String user_avatar;
                int user_id;
                int user_level;
                String user_nickname;
                int user_sex;

                @Override
                public String toString() {
                    return "ItemsBean{" +
                            "create_time=" + create_time +
                            ", user_avatar='" + user_avatar + '\'' +
                            ", user_id=" + user_id +
                            ", user_level=" + user_level +
                            ", user_nickname='" + user_nickname + '\'' +
                            ", user_sex=" + user_sex +
                            '}';
                }

                public String getUser_avatar() {
                    return user_avatar;
                }

                public void setUser_avatar(String user_avatar) {
                    this.user_avatar = user_avatar;
                }

                public int getUser_id() {
                    return user_id;
                }

                public void setUser_id(int user_id) {
                    this.user_id = user_id;
                }

                public int getUser_level() {
                    return user_level;
                }

                public void setUser_level(int user_level) {
                    this.user_level = user_level;
                }

                public String getUser_nickname() {
                    return user_nickname;
                }

                public void setUser_nickname(String user_nickname) {
                    this.user_nickname = user_nickname;
                }

                public int getUser_sex() {
                    return user_sex;
                }

                public void setUser_sex(int user_sex) {
                    this.user_sex = user_sex;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }
            }
        }

    @Override
    public String toString() {
        return "HnBlackListBean{" +
                "d=" + d +
                '}';
    }
}
