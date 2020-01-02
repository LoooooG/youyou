package com.hotniao.livelibrary.model.bean;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class UserDialogBean {

    /**
     * dialog_id : 0
     * from_user : {"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_nickname":"天行"}
     * to_user : {"user_avatar":"","user_id":"8","user_nickname":"2"}
     * msg : qwe
     * time : 1508826721
     */

    private String dialog_id;
    private FromUserBean from_user;
    private ToUserBean to_user;
    private String msg;
    private String time;

    public String getDialog_id() {
        return dialog_id;
    }

    public void setDialog_id(String dialog_id) {
        this.dialog_id = dialog_id;
    }

    public FromUserBean getFrom_user() {
        return from_user;
    }

    public void setFrom_user(FromUserBean from_user) {
        this.from_user = from_user;
    }

    public ToUserBean getTo_user() {
        return to_user;
    }

    public void setTo_user(ToUserBean to_user) {
        this.to_user = to_user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class FromUserBean {
        /**
         * user_avatar : http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png
         * user_id : 9
         * user_nickname : 天行
         */

        private String user_avatar;
        private String user_id;
        private String user_nickname;

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

    public static class ToUserBean {
        /**
         * user_avatar :
         * user_id : 8
         * user_nickname : 2
         */

        private String user_avatar;
        private String user_id;
        private String user_nickname;

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
