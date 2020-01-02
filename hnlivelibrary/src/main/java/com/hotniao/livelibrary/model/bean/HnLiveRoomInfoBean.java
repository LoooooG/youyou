package com.hotniao.livelibrary.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：进入房间的相关信息
 * 创建人：mj
 * 创建时间：2017/9/19 19:39
 * 修改人：Administrator
 * 修改时间：2017/9/19 19:39
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveRoomInfoBean implements Parcelable {


    /**
     * anchor : {"anchor_ranking":"0","is_follow":"N","user_avatar":"","user_dot":"1","user_id":"8","user_nickname":""}
     * live : {"anchor_category_id":"1","anchor_live_img":"123","anchor_live_onlines":"2","anchor_live_pay":"0","anchor_live_play_flv":"http://10888.liveplay.myqcloud.com/live/10888_8.flv","anchor_live_play_m3u8":"http://10888.liveplay.myqcloud.com/live/10888_8.m3u8","anchor_live_play_rtmp":"rtmp://10888.liveplay.myqcloud.com/live/10888_8","anchor_live_title":"123","barrage_fee":"0"}
     * notices : ["string1","string2","string3","string4","string5"]
     * user : {"free_time":"0","is_anchor_admin":"N","is_pay":"Y","user_coin":"","user_id":"1","user_nickname":""}
     * ws_url :
     */

    private AnchorBean anchor;
    private LiveBean live;
    private UserBean user;
    private String ws_url;


    public AnchorBean getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorBean anchor) {
        this.anchor = anchor;
    }

    public LiveBean getLive() {
        return live;
    }

    public void setLive(LiveBean live) {
        this.live = live;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getWs_url() {
        return ws_url;
    }

    public void setWs_url(String ws_url) {
        this.ws_url = ws_url;
    }



    public static class AnchorBean implements Parcelable {
        /**
         * anchor_ranking : 0
         * is_follow : N
         * user_avatar :
         * user_dot : 1
         * user_id : 8
         * user_nickname :
         */

        private String anchor_ranking;
        private String is_follow;
        private String user_avatar;
        private String user_dot;
        private String user_id;
        private String user_nickname;
        private String anchor_level;
        private String user_level;
        private String chat_room_id;

        public String getChat_room_id() {
            return chat_room_id;
        }

        public void setChat_room_id(String chat_room_id) {
            this.chat_room_id = chat_room_id;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getAnchor_level() {
            return anchor_level;
        }

        public void setAnchor_level(String anchor_level) {
            this.anchor_level = anchor_level;
        }

        public String getAnchor_ranking() {
            return anchor_ranking;
        }

        public void setAnchor_ranking(String anchor_ranking) {
            this.anchor_ranking = anchor_ranking;
        }

        public String getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(String is_follow) {
            this.is_follow = is_follow;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_dot() {
            return user_dot;
        }

        public void setUser_dot(String user_dot) {
            this.user_dot = user_dot;
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

        public AnchorBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.anchor_ranking);
            dest.writeString(this.is_follow);
            dest.writeString(this.user_avatar);
            dest.writeString(this.user_dot);
            dest.writeString(this.user_id);
            dest.writeString(this.user_nickname);
            dest.writeString(this.anchor_level);
            dest.writeString(this.user_level);
            dest.writeString(this.chat_room_id);
        }

        protected AnchorBean(Parcel in) {
            this.anchor_ranking = in.readString();
            this.is_follow = in.readString();
            this.user_avatar = in.readString();
            this.user_dot = in.readString();
            this.user_id = in.readString();
            this.user_nickname = in.readString();
            this.anchor_level = in.readString();
            this.user_level = in.readString();
            this.chat_room_id = in.readString();
        }

        public static final Creator<AnchorBean> CREATOR = new Creator<AnchorBean>() {
            @Override
            public AnchorBean createFromParcel(Parcel source) {
                return new AnchorBean(source);
            }

            @Override
            public AnchorBean[] newArray(int size) {
                return new AnchorBean[size];
            }
        };
    }

    public static class LiveBean implements Parcelable {
        /**
         * anchor_category_id : 1
         * anchor_live_img : 123
         * anchor_live_onlines : 2
         * anchor_live_pay : 0
         * anchor_live_play_flv : http://10888.liveplay.myqcloud.com/live/10888_8.flv
         * anchor_live_play_m3u8 : http://10888.liveplay.myqcloud.com/live/10888_8.m3u8
         * anchor_live_play_rtmp : rtmp://10888.liveplay.myqcloud.com/live/10888_8
         * anchor_live_title : 123
         * barrage_fee : 0
         */

        private String anchor_category_id;
        private String anchor_game_category_code;
        private String anchor_game_category_id;
        private String anchor_live_img;
        private String anchor_live_onlines;
        private String anchor_live_pay;
        private String anchor_live_play_flv;
        private String anchor_live_play_m3u8;
        private String anchor_live_play_rtmp;
        private String anchor_live_title;
        private String barrage_fee;
        private String anchor_live_fee;
        private String share_url;
        private List<String> notices;
        public List<String> getNotices() {
            return notices;
        }

        public String getAnchor_game_category_code() {
            return anchor_game_category_code;
        }

        public void setAnchor_game_category_code(String anchor_game_category_code) {
            this.anchor_game_category_code = anchor_game_category_code;
        }

        public String getAnchor_game_category_id() {
            return anchor_game_category_id;
        }

        public void setAnchor_game_category_id(String anchor_game_category_id) {
            this.anchor_game_category_id = anchor_game_category_id;
        }

        public void setNotices(List<String> notices) {
            this.notices = notices;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getAnchor_live_fee() {
            return anchor_live_fee;
        }

        public void setAnchor_live_fee(String anchor_live_fee) {
            this.anchor_live_fee = anchor_live_fee;
        }

        public String getAnchor_category_id() {
            return anchor_category_id;
        }

        public void setAnchor_category_id(String anchor_category_id) {
            this.anchor_category_id = anchor_category_id;
        }

        public String getAnchor_live_img() {
            return anchor_live_img;
        }

        public void setAnchor_live_img(String anchor_live_img) {
            this.anchor_live_img = anchor_live_img;
        }

        public String getAnchor_live_onlines() {
            return anchor_live_onlines;
        }

        public void setAnchor_live_onlines(String anchor_live_onlines) {
            this.anchor_live_onlines = anchor_live_onlines;
        }

        public String getAnchor_live_pay() {
            return anchor_live_pay;
        }

        public void setAnchor_live_pay(String anchor_live_pay) {
            this.anchor_live_pay = anchor_live_pay;
        }

        public String getAnchor_live_play_flv() {
            return anchor_live_play_flv;
        }

        public void setAnchor_live_play_flv(String anchor_live_play_flv) {
            this.anchor_live_play_flv = anchor_live_play_flv;
        }

        public String getAnchor_live_play_m3u8() {
            return anchor_live_play_m3u8;
        }

        public void setAnchor_live_play_m3u8(String anchor_live_play_m3u8) {
            this.anchor_live_play_m3u8 = anchor_live_play_m3u8;
        }

        public String getAnchor_live_play_rtmp() {
            return anchor_live_play_rtmp;
        }

        public void setAnchor_live_play_rtmp(String anchor_live_play_rtmp) {
            this.anchor_live_play_rtmp = anchor_live_play_rtmp;
        }

        public String getAnchor_live_title() {
            return anchor_live_title;
        }

        public void setAnchor_live_title(String anchor_live_title) {
            this.anchor_live_title = anchor_live_title;
        }

        public String getBarrage_fee() {
            return barrage_fee;
        }

        public void setBarrage_fee(String barrage_fee) {
            this.barrage_fee = barrage_fee;
        }

        public LiveBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.anchor_category_id);
            dest.writeString(this.anchor_live_img);
            dest.writeString(this.anchor_live_onlines);
            dest.writeString(this.anchor_live_pay);
            dest.writeString(this.anchor_live_play_flv);
            dest.writeString(this.anchor_live_play_m3u8);
            dest.writeString(this.anchor_live_play_rtmp);
            dest.writeString(this.anchor_live_title);
            dest.writeString(this.barrage_fee);
            dest.writeString(this.anchor_live_fee);
            dest.writeString(this.share_url);
            dest.writeStringList(this.notices);
        }

        protected LiveBean(Parcel in) {
            this.anchor_category_id = in.readString();
            this.anchor_live_img = in.readString();
            this.anchor_live_onlines = in.readString();
            this.anchor_live_pay = in.readString();
            this.anchor_live_play_flv = in.readString();
            this.anchor_live_play_m3u8 = in.readString();
            this.anchor_live_play_rtmp = in.readString();
            this.anchor_live_title = in.readString();
            this.barrage_fee = in.readString();
            this.anchor_live_fee = in.readString();
            this.share_url = in.readString();
            this.notices = in.createStringArrayList();
        }

        public static final Creator<LiveBean> CREATOR = new Creator<LiveBean>() {
            @Override
            public LiveBean createFromParcel(Parcel source) {
                return new LiveBean(source);
            }

            @Override
            public LiveBean[] newArray(int size) {
                return new LiveBean[size];
            }
        };
    }

    public static class UserBean implements Parcelable {
        /**
         * free_time : 0
         * is_anchor_admin : N
         * is_pay : Y
         * user_coin :
         * user_id : 1
         * user_nickname :
         */

        private String free_time;
        private String is_anchor_admin;
        private String is_pay;
        private String user_coin;
        private String user_id;
        private String user_nickname;
        private String user_is_member;

        public String getUser_is_member() {
            return user_is_member;
        }

        public void setUser_is_member(String user_is_member) {
            this.user_is_member = user_is_member;
        }

        public String getFree_time() {
            return free_time;
        }

        public void setFree_time(String free_time) {
            this.free_time = free_time;
        }

        public String getIs_anchor_admin() {
            return is_anchor_admin;
        }

        public void setIs_anchor_admin(String is_anchor_admin) {
            this.is_anchor_admin = is_anchor_admin;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getUser_coin() {
            return user_coin;
        }

        public void setUser_coin(String user_coin) {
            this.user_coin = user_coin;
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

        public UserBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.free_time);
            dest.writeString(this.is_anchor_admin);
            dest.writeString(this.is_pay);
            dest.writeString(this.user_coin);
            dest.writeString(this.user_id);
            dest.writeString(this.user_nickname);
            dest.writeString(this.user_is_member);
        }

        protected UserBean(Parcel in) {
            this.free_time = in.readString();
            this.is_anchor_admin = in.readString();
            this.is_pay = in.readString();
            this.user_coin = in.readString();
            this.user_id = in.readString();
            this.user_nickname = in.readString();
            this.user_is_member = in.readString();
        }

        public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel source) {
                return new UserBean(source);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.anchor, flags);
        dest.writeParcelable(this.live, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.ws_url);
    }

    public HnLiveRoomInfoBean() {
    }

    protected HnLiveRoomInfoBean(Parcel in) {
        this.anchor = in.readParcelable(AnchorBean.class.getClassLoader());
        this.live = in.readParcelable(LiveBean.class.getClassLoader());
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.ws_url = in.readString();
    }

    public static final Parcelable.Creator<HnLiveRoomInfoBean> CREATOR = new Parcelable.Creator<HnLiveRoomInfoBean>() {
        @Override
        public HnLiveRoomInfoBean createFromParcel(Parcel source) {
            return new HnLiveRoomInfoBean(source);
        }

        @Override
        public HnLiveRoomInfoBean[] newArray(int size) {
            return new HnLiveRoomInfoBean[size];
        }
    };
}
