package com.hotniao.livelibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：定位信息
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLocationEntity  implements Parcelable{

    private  String mLat;//经度
    private String mLng;//纬度
    private String mCity;
    private String mProvince;

    public HnLocationEntity(String mLat, String mLng, String mCity,String mProvince) {
        this.mLat = mLat;
        this.mLng = mLng;
        this.mCity = mCity;
        this.mProvince = mProvince;
    }

    public String getmProvince() {
        return mProvince;
    }

    public void setmProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mLat);
        dest.writeString(this.mLng);
        dest.writeString(this.mCity);
        dest.writeString(this.mProvince);
    }

    protected HnLocationEntity(Parcel in) {
        this.mLat = in.readString();
        this.mLng = in.readString();
        this.mCity = in.readString();
        this.mProvince = in.readString();
    }

    public static final Creator<HnLocationEntity> CREATOR = new Creator<HnLocationEntity>() {
        @Override
        public HnLocationEntity createFromParcel(Parcel source) {
            return new HnLocationEntity(source);
        }

        @Override
        public HnLocationEntity[] newArray(int size) {
            return new HnLocationEntity[size];
        }
    };
}
