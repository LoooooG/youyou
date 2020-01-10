package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取主播信息
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnGetChatAnchorInfoModel extends BaseResponseModel {


    /**
     * d : {"coin_name":"测试内容5ozh","is_follow":false,"price":"2","user_avatar":"https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png","user_constellation":"","user_id":"274","user_intro":"","user_nickname":"乐播01041439432","user_video":"测试内容f94q","user_video_cover":"测试内容022y"}
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
         * coin_name : 测试内容5ozh
         * is_follow : false
         * price : 2
         * user_avatar : https://lebolive-1255651273.image.myqcloud.com/image/2018/01/04/1515055476057.png
         * user_constellation :
         * user_id : 274
         * user_intro :
         * user_nickname : 乐播01041439432
         * user_video : 测试内容f94q
         * user_video_cover : 测试内容022y
         */

        private String coin_name;
        private boolean is_follow;
        private String price;
        private String user_avatar;
        private String user_constellation;
        private String user_id;
        private String user_intro;
        private String user_nickname;
        private String user_video;
        private String user_video_cover;
        private String chat_room_id;

        public String getChat_room_id() {
            return chat_room_id;
        }

        public void setChat_room_id(String chat_room_id) {
            this.chat_room_id = chat_room_id;
        }

        public String getCoin_name() {
            return coin_name;
        }

        public void setCoin_name(String coin_name) {
            this.coin_name = coin_name;
        }

        public boolean isIs_follow() {
            return is_follow;
        }

        public void setIs_follow(boolean is_follow) {
            this.is_follow = is_follow;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_constellation() {
            return user_constellation;
        }

        public void setUser_constellation(String user_constellation) {
            this.user_constellation = user_constellation;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_intro() {
            return user_intro;
        }

        public void setUser_intro(String user_intro) {
            this.user_intro = user_intro;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getUser_video() {
            return user_video;
        }

        public void setUser_video(String user_video) {
            this.user_video = user_video;
        }

        public String getUser_video_cover() {
            return user_video_cover;
        }

        public void setUser_video_cover(String user_video_cover) {
            this.user_video_cover = user_video_cover;
        }
    }
}
