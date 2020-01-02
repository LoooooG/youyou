package com.hotniao.livelibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：开播提醒推送数据
 * 创建人：mj
 * 创建时间：2017/9/28 17:41
 * 修改人：Administrator
 * 修改时间：2017/9/28 17:41
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveNoticeModel   {


    /**
     * type : live_notify
     * data : {"uid":"100015","nick":"MillerK","down_url":"rtmp://pili-live-rtmp.youbo.liveniao.com/hn-live-youbo/100015@480p"}
     */

    private String type;
    private DataBean data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean   implements Parcelable{
        /**
         * uid : 100015
         * nick : MillerK
         * down_url : rtmp://pili-live-rtmp.youbo.liveniao.com/hn-live-youbo/100015@480p
         */

        private String uid;
        private String nick;
        private String down_url;

        protected DataBean(Parcel in) {
            uid = in.readString();
            nick = in.readString();
            down_url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(uid);
            dest.writeString(nick);
            dest.writeString(down_url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }
    }
}
