package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：私信模块
 * 创建人：刘龙龙
 * 创建时间：2017年9月8日
 * 修改人：刘龙龙
 * 修改时间：2017年9月8日
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateLetterListModel extends BaseResponseModel{


    /**
     * d : {"user_dialogs":{"items":[{"chat_room_id":"9_8","content":"啊啊","time":"0","unread":"1","user_avatar":"","user_id":"8","user_nickname":"2"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}}
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
         * user_dialogs : {"items":[{"chat_room_id":"9_8","content":"啊啊","time":"0","unread":"1","user_avatar":"","user_id":"8","user_nickname":"2"}],"next":1,"page":1,"pagesize":20,"pagetotal":1,"prev":1,"total":1}
         */

        private UserDialogsBean user_dialog;

        public UserDialogsBean getUser_dialogs() {
            return user_dialog;
        }

        public void setUser_dialogs(UserDialogsBean user_dialog) {
            this.user_dialog = user_dialog;
        }

        public static class UserDialogsBean {
            /**
             * items : [{"chat_room_id":"9_8","content":"啊啊","time":"0","unread":"1","user_avatar":"","user_id":"8","user_nickname":"2"}]
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
                 * chat_room_id : 9_8
                 * content : 啊啊
                 * time : 0
                 * unread : 1
                 * user_avatar :
                 * user_id : 8
                 * user_nickname : 2
                 */

                private String chat_room_id;
                private String content;
                private String time;
                private String unread;
                private String user_avatar;
                private String user_id;
                private String user_nickname;
                private boolean showPriLetterStr;

                public boolean isShowPriLetterStr() {
                    return showPriLetterStr;
                }

                public void setShowPriLetterStr(boolean showPriLetterStr) {
                    this.showPriLetterStr = showPriLetterStr;
                }

                public String getChat_room_id() {
                    return chat_room_id;
                }

                public void setChat_room_id(String chat_room_id) {
                    this.chat_room_id = chat_room_id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getUnread() {
                    return unread;
                }

                public void setUnread(String unread) {
                    this.unread = unread;
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
}
