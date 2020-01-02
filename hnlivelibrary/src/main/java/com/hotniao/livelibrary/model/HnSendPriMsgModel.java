package com.hotniao.livelibrary.model;

import com.hn.library.http.BaseResponseModel;
import com.hotniao.livelibrary.model.bean.UserDialogBean;

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

public class HnSendPriMsgModel extends BaseResponseModel {

    /**
     * d : {"dialog":{"dialog_id":"9","from_user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"},"msg":"1234","time":1508896486,"to_user":{"user_avatar":"","user_id":"8","user_nickname":"2"}}}
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
         * dialog : {"dialog_id":"9","from_user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"},"msg":"1234","time":1508896486,"to_user":{"user_avatar":"","user_id":"8","user_nickname":"2"}}
         */

        private UserDialogBean dialog;

        public UserDialogBean getDialog() {
            return dialog;
        }

        public void setDialog(UserDialogBean dialog) {
            this.dialog = dialog;
        }


    }
}
