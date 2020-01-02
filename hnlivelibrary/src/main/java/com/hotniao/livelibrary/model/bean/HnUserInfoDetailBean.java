package com.hotniao.livelibrary.model.bean;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：存储用户信息
 * 创建人：mj
 * 创建时间：2017/9/13 10:21
 * 修改人：Administrator
 * 修改时间：2017/9/13 10:21
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUserInfoDetailBean {

    /**
     * anchor_level : 0
     * anchor_ranking : 1
     * user_avatar :
     * user_birth :
     * user_collect_total : 0
     * user_consume_total : 0
     * user_fans_total : 0
     * user_follow_total : 0
     * user_id : 8
     * user_intro :
     * user_is_anchor : N
     * is_anchor_admin : N
     * user_is_member : N
     * user_level : 0
     * user_member_expire_time : 0
     * user_nickname : 2
     * user_sex : 1
     */
    private String chat_room_id;
    private String anchor_level;
    private String anchor_ranking;
    private String user_avatar;
    private String user_birth;
    private String user_collect_total;
    private String user_consume_total;
    private String user_fans_total;
    private String user_follow_total;
    private String user_id;
    private String user_intro;
    private String user_is_anchor;
    private String is_anchor_admin;//是否主播的管理员，Y：是，N：否
    private String user_is_member;
    private String user_level;
    private String user_member_expire_time;
    private String user_nickname;
    private String user_sex;
    private String is_follow;
    private String is_card_effect;//	是否有信息卡特效，Y：是，N：否

    private String private_price;//私聊价格
    private List<String> rank;

    private List<String> gift_img;
    private boolean is_black;
    private String is_online;//是否在线 0 不在 1在
    private String share_url;
    private String user_constellation;
    private String user_emotional_state;
    private String user_hobby;
    private String user_home_town;
    private String total_gift;

    public String getTotal_gift() {
        return total_gift;
    }

    public void setTotal_gift(String total_gift) {
        this.total_gift = total_gift;
    }

    public List<String> getGift_img() {
        return gift_img;
    }

    public void setGift_img(List<String> gift_img) {
        this.gift_img = gift_img;
    }

    public boolean isIs_black() {
        return is_black;
    }

    public void setIs_black(boolean is_black) {
        this.is_black = is_black;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getUser_constellation() {
        return user_constellation;
    }

    public void setUser_constellation(String user_constellation) {
        this.user_constellation = user_constellation;
    }

    public String getUser_emotional_state() {
        return user_emotional_state;
    }

    public void setUser_emotional_state(String user_emotional_state) {
        this.user_emotional_state = user_emotional_state;
    }

    public String getUser_hobby() {
        return user_hobby;
    }

    public void setUser_hobby(String user_hobby) {
        this.user_hobby = user_hobby;
    }

    public String getUser_home_town() {
        return user_home_town;
    }

    public void setUser_home_town(String user_home_town) {
        this.user_home_town = user_home_town;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_profession() {
        return user_profession;
    }

    public void setUser_profession(String user_profession) {
        this.user_profession = user_profession;
    }

    public String getUser_register_time() {
        return user_register_time;
    }

    public void setUser_register_time(String user_register_time) {
        this.user_register_time = user_register_time;
    }

    private String user_img;
    private String user_profession;
    private String user_register_time;


    public String getIs_card_effect() {
        return is_card_effect;
    }

    public void setIs_card_effect(String is_card_effect) {
        this.is_card_effect = is_card_effect;
    }

    public String getPrivate_price() {
        return private_price;
    }

    public void setPrivate_price(String private_price) {
        this.private_price = private_price;
    }

    public List<String> getRank() {
        return rank;
    }

    public void setRank(List<String> rank) {
        this.rank = rank;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
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

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_collect_total() {
        return user_collect_total;
    }

    public void setUser_collect_total(String user_collect_total) {
        this.user_collect_total = user_collect_total;
    }

    public String getUser_consume_total() {
        return user_consume_total;
    }

    public void setUser_consume_total(String user_consume_total) {
        this.user_consume_total = user_consume_total;
    }

    public String getUser_fans_total() {
        return user_fans_total;
    }

    public void setUser_fans_total(String user_fans_total) {
        this.user_fans_total = user_fans_total;
    }

    public String getUser_follow_total() {
        return user_follow_total;
    }

    public void setUser_follow_total(String user_follow_total) {
        this.user_follow_total = user_follow_total;
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

    public String getUser_is_anchor() {
        return user_is_anchor;
    }

    public void setUser_is_anchor(String user_is_anchor) {
        this.user_is_anchor = user_is_anchor;
    }

    public String getIs_anchor_admin() {
        return is_anchor_admin;
    }

    public void setUser_is_anchor_admin(String is_anchor_admin) {
        this.is_anchor_admin = is_anchor_admin;
    }

    public String getUser_is_member() {
        return user_is_member;
    }

    public void setUser_is_member(String user_is_member) {
        this.user_is_member = user_is_member;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getUser_member_expire_time() {
        return user_member_expire_time;
    }

    public void setUser_member_expire_time(String user_member_expire_time) {
        this.user_member_expire_time = user_member_expire_time;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }
}
