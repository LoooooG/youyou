package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：未读消息数
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnNoReadMessageModel extends BaseResponseModel {

    /**
     * d : {"unread":{"system_message":"2","total":"2","user_chat":"0"}}
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
         * unread : {"system_message":"2","total":"2","user_chat":"0"}
         */

        private UnreadBean unread;

        public UnreadBean getUnread() {
            return unread;
        }

        public void setUnread(UnreadBean unread) {
            this.unread = unread;
        }

        public static class UnreadBean {
            /**
             * system_message : 2
             * total : 2
             * user_chat : 0
             */

            private String system_message;
            private String total;
            private String user_chat;
            private String video_message;

            public String getVideo_message() {
                return video_message;
            }

            public void setVideo_message(String video_message) {
                this.video_message = video_message;
            }

            public String getSystem_message() {
                return system_message;
            }

            public void setSystem_message(String system_message) {
                this.system_message = system_message;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getUser_chat() {
                return user_chat;
            }

            public void setUser_chat(String user_chat) {
                this.user_chat = user_chat;
            }
        }
    }
}
