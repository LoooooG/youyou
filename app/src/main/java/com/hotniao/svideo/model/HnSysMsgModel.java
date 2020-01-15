package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSysMsgModel extends BaseResponseModel {

    /**
     * d : {"system_dialog":[{"content":"123","time":1508901989,"type":"system","unread":"2"}]}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        private List<SystemDialogBean> system_dialog;

        public List<SystemDialogBean> getSystem_dialog() {
            return system_dialog;
        }

        public void setSystem_dialog(List<SystemDialogBean> system_dialog) {
            this.system_dialog = system_dialog;
        }

        public static class SystemDialogBean {
            /**
             * content : 123
             * time : 1508901989
             * type : system
             * unread : 2
             */

            private String content;
            private int time;
            private String type;
            private String unread;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUnread() {
                return unread;
            }

            public void setUnread(String unread) {
                this.unread = unread;
            }
        }
    }
}
