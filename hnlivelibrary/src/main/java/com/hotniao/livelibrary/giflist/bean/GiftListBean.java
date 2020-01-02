package com.hotniao.livelibrary.giflist.bean;

import android.animation.AnimatorSet;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物列表数据库实体数据
 * 创建人：mj
 * 创建时间：2017/10/17 19:08
 * 修改人：Administrator
 * 修改时间：2017/10/17 19:08
 * 修改备注：
 * Version:  1.0.0
 * @author Administrator
 */

public class GiftListBean      implements Parcelable{

    /**id*/
    private String gift_id;
    /**礼物名*/
    private String giftName;
    /**礼物价格*/
    private String giftCoin;
    /**礼物详情*/
    private String detail;
    /**礼物列表静态显示的图片地址*/
    private String  staticGiftUrl;
    /**礼物列表静态显示的图片本地存储地址*/
    private String  staticGiftLocalUrl;
    /**礼物列表动态显示的图片地址*/
    private String  dynamicGiftUrl;
    /**礼物列表动态显示的图片本地地址*/
    private String  dynamicGiftLocalUrl;
    /**用于大礼物资源包下载地址*/
    private String zipDownUrl;
    /**用于大礼物资源包本地存储地址*/
    private String zipDownLocalUrl;
    /**状态  下架/上架*/
    private String  state;

    /**标签 礼物大分类*/
    private String  mTabName;
    private String  mTabId;
    /**排序*/
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getmTabId() {
        return mTabId;
    }

    public void setmTabId(String mTabId) {
        this.mTabId = mTabId;
    }

    public String getmTabName() {
        return mTabName;
    }

    public void setmTabName(String mTabName) {
        this.mTabName = mTabName;
    }

    public GiftListBean() {
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getGiftCoin() {
        return giftCoin;
    }

    public void setGiftCoin(String giftCoin) {
        this.giftCoin = giftCoin;
    }

    public String getStaticGiftUrl() {
        return staticGiftUrl;
    }

    public void setStaticGiftUrl(String staticGiftUrl) {
        this.staticGiftUrl = staticGiftUrl;
    }

    public String getStaticGiftLocalUrl() {
        return staticGiftLocalUrl;
    }

    public void setStaticGiftLocalUrl(String staticGiftLocalUrl) {
        this.staticGiftLocalUrl = staticGiftLocalUrl;
    }

    public String getDynamicGiftUrl() {
        return dynamicGiftUrl;
    }

    public void setDynamicGiftUrl(String dynamicGiftUrl) {
        this.dynamicGiftUrl = dynamicGiftUrl;
    }

    public String getDynamicGiftLocalUrl() {
        return dynamicGiftLocalUrl;
    }

    public void setDynamicGiftLocalUrl(String dynamicGiftLocalUrl) {
        this.dynamicGiftLocalUrl = dynamicGiftLocalUrl;
    }

    public String getZipDownUrl() {
        return zipDownUrl;
    }

    public void setZipDownUrl(String zipDownUrl) {
        this.zipDownUrl = zipDownUrl;
    }

    public String getZipDownLocalUrl() {
        return zipDownLocalUrl;
    }

    public void setZipDownLocalUrl(String zipDownLocalUrl) {
        this.zipDownLocalUrl = zipDownLocalUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "GiftListBean{" +
                "gift_id='" + gift_id + '\'' +
                ", giftName='" + giftName + '\'' +
                ", detail='" + detail + '\'' +
                ", staticGiftUrl='" + staticGiftUrl + '\'' +
                ", staticGiftLocalUrl='" + staticGiftLocalUrl + '\'' +
                ", dynamicGiftUrl='" + dynamicGiftUrl + '\'' +
                ", dynamicGiftLocalUrl='" + dynamicGiftLocalUrl + '\'' +
                ", zipDownUrl='" + zipDownUrl + '\'' +
                ", zipDownLocalUrl='" + zipDownLocalUrl + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gift_id);
        dest.writeString(this.giftName);
        dest.writeString(this.giftCoin);
        dest.writeString(this.detail);
        dest.writeString(this.staticGiftUrl);
        dest.writeString(this.staticGiftLocalUrl);
        dest.writeString(this.dynamicGiftUrl);
        dest.writeString(this.dynamicGiftLocalUrl);
        dest.writeString(this.zipDownUrl);
        dest.writeString(this.zipDownLocalUrl);
        dest.writeString(this.state);
        dest.writeString(this.mTabName);
        dest.writeString(this.mTabId);
        dest.writeString(this.sort);
    }

    protected GiftListBean(Parcel in) {
        this.gift_id = in.readString();
        this.giftName = in.readString();
        this.giftCoin = in.readString();
        this.detail = in.readString();
        this.staticGiftUrl = in.readString();
        this.staticGiftLocalUrl = in.readString();
        this.dynamicGiftUrl = in.readString();
        this.dynamicGiftLocalUrl = in.readString();
        this.zipDownUrl = in.readString();
        this.zipDownLocalUrl = in.readString();
        this.state = in.readString();
        this.mTabName = in.readString();
        this.mTabId = in.readString();
        this.sort = in.readString();
    }

    public static final Creator<GiftListBean> CREATOR = new Creator<GiftListBean>() {
        @Override
        public GiftListBean createFromParcel(Parcel source) {
            return new GiftListBean(source);
        }

        @Override
        public GiftListBean[] newArray(int size) {
            return new GiftListBean[size];
        }
    };
}
