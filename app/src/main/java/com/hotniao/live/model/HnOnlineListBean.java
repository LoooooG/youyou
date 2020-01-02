package com.hotniao.live.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * create by Mr.x
 * on 9/8/2018
 */

public class HnOnlineListBean extends BaseResponseModel{


    /**
     * d : {"items":[{"user_avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEKD0SkjdicVd1pPI1XkPFIQ7zibjBibPhjJvzmoibQj8kWS6Ut3M9NYQp5JfsRuTWcbV0XCpEiafK8hRAQ/132","user_id":"6666666","user_nickname":"Angle丶Pisces"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
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
         * items : [{"user_avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEKD0SkjdicVd1pPI1XkPFIQ7zibjBibPhjJvzmoibQj8kWS6Ut3M9NYQp5JfsRuTWcbV0XCpEiafK8hRAQ/132","user_id":"6666666","user_nickname":"Angle丶Pisces"}]
         * next : 1
         * page : 1
         * pagesize : 20
         * pagetotal : 1
         * prev : 1
         * total : 1
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
             * user_avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEKD0SkjdicVd1pPI1XkPFIQ7zibjBibPhjJvzmoibQj8kWS6Ut3M9NYQp5JfsRuTWcbV0XCpEiafK8hRAQ/132
             * user_id : 6666666
             * user_nickname : Angle丶Pisces
             */

            private String user_avatar;
            private String user_id;
            private String user_nickname;
            private String dialog_id;

            public String getDialog_id() {
                return dialog_id;
            }

            public void setDialog_id(String dialog_id) {
                this.dialog_id = dialog_id;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
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
