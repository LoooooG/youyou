package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：获取随机开通私聊的主播
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHomeHotAnchorChatModel extends BaseResponseModel {


    /**
     * d : {"anchor":[{"user_avatar":"测试内容13b8","user_id":"测试内容9gvv"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<AnchorBean> anchor;

        public List<AnchorBean> getAnchor() {
            return anchor;
        }

        public void setAnchor(List<AnchorBean> anchor) {
            this.anchor = anchor;
        }

        public static class AnchorBean {
            /**
             * user_avatar : 测试内容13b8
             * user_id : 测试内容9gvv
             */

            private String user_avatar;
            private String user_id;

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
        }
    }
}
