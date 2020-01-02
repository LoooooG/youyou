package com.hotniao.livelibrary.model;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：已在线人数推动数据
 * 创建人：mj
 * 创建时间：2017/9/20 16:11
 * 修改人：Administrator
 * 修改时间：2017/9/20 16:11
 * 修改备注：
 * Version:  1.0.0
 */
public class HnOnlineModel {

    /**
     * data : {"onlines":"2","users":[{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_nickname":"天行"}]}
     * msg :
     * type : room_users
     */

    private DataBean data;
    private String msg;
    private String type;
    private String webSocketUri;

    public String getWebSocketUri() {
        return webSocketUri;
    }

    public void setWebSocketUri(String webSocketUri) {
        this.webSocketUri = webSocketUri;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class DataBean {
        /**
         * onlines : 2
         * users : [{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_nickname":"天行"}]
         */

        private String onlines;
        private List<UsersBean> users;

        public String getOnlines() {
            return onlines;
        }

        public void setOnlines(String onlines) {
            this.onlines = onlines;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public static class UsersBean {
            /**
             * user_avatar : http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png
             * user_id : 9
             * user_nickname : 天行
             */

            private String user_avatar;
            private String user_id;
            private String user_nickname;
            private String user_is_member;

            public String getUser_is_member() {
                return user_is_member;
            }

            public void setUser_is_member(String user_is_member) {
                this.user_is_member = user_is_member;
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
