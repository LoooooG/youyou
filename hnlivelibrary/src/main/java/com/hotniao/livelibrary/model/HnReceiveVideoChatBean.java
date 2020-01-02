package com.hotniao.livelibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：邀请主播推送
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnReceiveVideoChatBean {


    /**
     * data : {"chat_log":"测试内容s7hp","f_user_avatar":"测试内容wf3f","f_user_id":"测试内容984e","f_user_nickname":"测试内容nj41","play_flv":"","play_m3u8":"","play_rtmp":""}
     * type : private_chat
     */

    private DataBean data;
    private String type;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class DataBean implements Parcelable{
        @Override
        public String toString() {
            return "DataBean{" +
                    "amount='" + amount + '\'' +
                    ", duration='" + duration + '\'' +
                    ", coin_amount='" + coin_amount + '\'' +
                    ", user_coin='" + user_coin + '\'' +
                    ", chat_log='" + chat_log + '\'' +
                    ", f_user_avatar='" + f_user_avatar + '\'' +
                    ", f_user_id='" + f_user_id + '\'' +
                    ", f_user_nickname='" + f_user_nickname + '\'' +
                    ", play_flv='" + play_flv + '\'' +
                    ", play_m3u8='" + play_m3u8 + '\'' +
                    ", play_rtmp='" + play_rtmp + '\'' +
                    ", isCreate=" + isCreate +
                    ", price='" + price + '\'' +
                    ", vague='" + vague + '\'' +
                    ", pushUrl='" + pushUrl + '\'' +
                    '}';
        }

        /**
         * chat_log : 测试内容s7hp
         * f_user_avatar : 测试内容wf3f
         * f_user_id : 测试内容984e
         * f_user_nickname : 测试内容nj41
         * play_flv :
         * play_m3u8 :
         * play_rtmp :
         */

        private String amount;
        private String duration;
        private String  coin_amount;
        private String  user_coin;

        private String chat_log;

        private String f_user_avatar;
        private String f_user_id;
        private String f_user_nickname;

        private String play_flv;
        private String play_m3u8;
        private String play_rtmp;
        private boolean isCreate=false;
        private String  price;

        private String  vague;
        private String  pushUrl;

        public String getUser_coin() {
            return user_coin;
        }

        public void setUser_coin(String user_coin) {
            this.user_coin = user_coin;
        }

        public String getCoin_amount() {
            return coin_amount;
        }

        public void setCoin_amount(String coin_amount) {
            this.coin_amount = coin_amount;
        }

        public String getPushUrl() {
            return pushUrl;
        }

        public void setPushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
        }

        public String getVague() {
            return vague;
        }

        public void setVague(String vague) {
            this.vague = vague;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public boolean isCreate() {
            return isCreate;
        }

        public void setCreate(boolean create) {
            isCreate = create;
        }

        public String getChat_log() {
            return chat_log;
        }

        public void setChat_log(String chat_log) {
            this.chat_log = chat_log;
        }

        public String getF_user_avatar() {
            return f_user_avatar;
        }

        public void setF_user_avatar(String f_user_avatar) {
            this.f_user_avatar = f_user_avatar;
        }

        public String getF_user_id() {
            return f_user_id;
        }

        public void setF_user_id(String f_user_id) {
            this.f_user_id = f_user_id;
        }

        public String getF_user_nickname() {
            return f_user_nickname;
        }

        public void setF_user_nickname(String f_user_nickname) {
            this.f_user_nickname = f_user_nickname;
        }

        public String getPlay_flv() {
            return play_flv;
        }

        public void setPlay_flv(String play_flv) {
            this.play_flv = play_flv;
        }

        public String getPlay_m3u8() {
            return play_m3u8;
        }

        public void setPlay_m3u8(String play_m3u8) {
            this.play_m3u8 = play_m3u8;
        }

        public String getPlay_rtmp() {
            return play_rtmp;
        }

        public void setPlay_rtmp(String play_rtmp) {
            this.play_rtmp = play_rtmp;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.amount);
            dest.writeString(this.duration);
            dest.writeString(this.chat_log);
            dest.writeString(this.f_user_avatar);
            dest.writeString(this.f_user_id);
            dest.writeString(this.f_user_nickname);
            dest.writeString(this.play_flv);
            dest.writeString(this.play_m3u8);
            dest.writeString(this.play_rtmp);
            dest.writeByte(this.isCreate ? (byte) 1 : (byte) 0);
            dest.writeString(this.price);
            dest.writeString(this.vague);
            dest.writeString(this.pushUrl);
            dest.writeString(this.coin_amount);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.amount = in.readString();
            this.duration = in.readString();
            this.chat_log = in.readString();
            this.f_user_avatar = in.readString();
            this.f_user_id = in.readString();
            this.f_user_nickname = in.readString();
            this.play_flv = in.readString();
            this.play_m3u8 = in.readString();
            this.play_rtmp = in.readString();
            this.isCreate = in.readByte() != 0;
            this.price = in.readString();
            this.vague = in.readString();
            this.pushUrl = in.readString();
            this.coin_amount = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
