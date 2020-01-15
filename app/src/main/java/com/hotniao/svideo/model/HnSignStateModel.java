package com.hotniao.svideo.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：是否签到
 *
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSignStateModel extends BaseResponseModel {


    /**
     * d : {"user_signin":{"is_signin":"Y","tips":"今天还没有签到哦！"}}
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
         * user_signin : {"is_signin":"Y","tips":"今天还没有签到哦！"}
         */

        private UserSigninBean user_signin;

        public UserSigninBean getUser_signin() {
            return user_signin;
        }

        public void setUser_signin(UserSigninBean user_signin) {
            this.user_signin = user_signin;
        }

        public static class UserSigninBean {
            /**
             * is_signin : Y
             * tips : 今天还没有签到哦！
             */

            private String is_signin;
            private String tips;

            public String getIs_signin() {
                return is_signin;
            }

            public void setIs_signin(String is_signin) {
                this.is_signin = is_signin;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }
        }
    }
}
