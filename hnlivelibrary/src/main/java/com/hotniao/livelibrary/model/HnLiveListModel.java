package com.hotniao.livelibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播列表数据实体。该数据由客户端自己拼装
 * 创建人：mj
 * 创建时间：2017/10/12 15:58
 * 修改人：Administrator
 * 修改时间：2017/10/12 15:58
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveListModel    implements   Parcelable  {


    /**点击的位置*/
    private int  pos;

    private List<LiveListBean>  list;





    /**
     * 直播列表数据 房间需要
     */
   public static   class  LiveListBean  implements Parcelable{
        /**列表对应拉流地址*/
        private String      pullUrl;
        /**列表对应主播id*/
        private String   uid;
        /**列表对应主播头像*/
        private String  avator;


        public LiveListBean() {
        }

        protected LiveListBean(Parcel in) {
            pullUrl = in.readString();
            uid = in.readString();
            avator = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(pullUrl);
            dest.writeString(uid);
            dest.writeString(avator);
        }

        public LiveListBean(String pullUrl, String uid, String avator) {
            this.pullUrl = pullUrl;
            this.uid = uid;
            this.avator = avator;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<LiveListBean> CREATOR = new Creator<LiveListBean>() {
            @Override
            public LiveListBean createFromParcel(Parcel in) {
                return new LiveListBean(in);
            }

            @Override
            public LiveListBean[] newArray(int size) {
                return new LiveListBean[size];
            }
        };

        public String getPullUrl() {
            return pullUrl;
        }

        public void setPullUrl(String pullUrl) {
            this.pullUrl = pullUrl;
        }

        public String getUid() {
            return uid;
        }

        public String getAvator() {
            return avator;
        }

        public void setAvator(String avator) {
            this.avator = avator;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    public HnLiveListModel() {
    }

    protected HnLiveListModel(Parcel in) {
        pos = in.readInt();
        list = in.createTypedArrayList(LiveListBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pos);
        dest.writeTypedList(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HnLiveListModel> CREATOR = new Creator<HnLiveListModel>() {
        @Override
        public HnLiveListModel createFromParcel(Parcel in) {
            return new HnLiveListModel(in);
        }

        @Override
        public HnLiveListModel[] newArray(int size) {
            return new HnLiveListModel[size];
        }
    };

    public List<LiveListBean> getList() {
        return list;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setList(List<LiveListBean> list) {
        this.list = list;
    }

}
