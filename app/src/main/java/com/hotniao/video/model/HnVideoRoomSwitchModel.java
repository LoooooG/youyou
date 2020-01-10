package com.hotniao.video.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hn.library.http.BaseResponseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频切换数据
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoRoomSwitchModel extends BaseResponseModel {


    private List<DBean> d;

    public List<DBean> getD() {
        return d;
    }

    public void setD(List<DBean> d) {
        this.d = d;
    }

    public static class DBean implements Serializable {
        /**
         * cover : https://lebolive-1255651273.image.myqcloud.com/image/20180105/1515114926762708.jpg
         * id : 2
         */

        private String cover;
        private String id;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }
}
