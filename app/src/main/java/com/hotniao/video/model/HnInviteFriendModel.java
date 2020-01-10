package com.hotniao.video.model;

import com.hn.library.http.BaseResponseModel;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：邀请页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnInviteFriendModel extends BaseResponseModel {

    /**
     * d : {"invite":{"banner_url":"http://api.greenlive.1booker.com/assets/images/invite/banner.png","invite_ratio_1":"0%","invite_ratio_2":"0%","invite_ratio_3":"0%","tips":"每个用户都有自己的邀请码，只要您邀请的用户注册时输入您的邀请码，对方充值时，您将获得一定的分成奖励。"},"parent_invite":{"user_avatar":"","user_id":"0","user_nickname":""},"share":{"content":"我在乐播直播，注册并输入我的邀请码FXEYJIAR4","logo":"http://api.greenlive.1booker.com/assets/images/logo.png","url":"h5.greenlive.1booker.com/register?invite_code=FXEYJIAR4"},"user":{"user_invite_code":"FXEYJIAR4","user_invite_coin_total":"0.00"}}
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
         * invite : {"banner_url":"http://api.greenlive.1booker.com/assets/images/invite/banner.png","invite_ratio_1":"0%","invite_ratio_2":"0%","invite_ratio_3":"0%","tips":"每个用户都有自己的邀请码，只要您邀请的用户注册时输入您的邀请码，对方充值时，您将获得一定的分成奖励。"}
         * parent_invite : {"user_avatar":"","user_id":"0","user_nickname":""}
         * share : {"content":"我在乐播直播，注册并输入我的邀请码FXEYJIAR4","logo":"http://api.greenlive.1booker.com/assets/images/logo.png","url":"h5.greenlive.1booker.com/register?invite_code=FXEYJIAR4"}
         * user : {"user_invite_code":"FXEYJIAR4","user_invite_coin_total":"0.00"}
         */

        private InviteBean invite;
        private ParentInviteBean parent_invite;
        private ShareBean share;
        private UserBean user;

        public InviteBean getInvite() {
            return invite;
        }

        public void setInvite(InviteBean invite) {
            this.invite = invite;
        }

        public ParentInviteBean getParent_invite() {
            return parent_invite;
        }

        public void setParent_invite(ParentInviteBean parent_invite) {
            this.parent_invite = parent_invite;
        }

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class InviteBean {
            /**
             * banner_url : http://api.greenlive.1booker.com/assets/images/invite/banner.png
             * invite_ratio_1 : 0%
             * invite_ratio_2 : 0%
             * invite_ratio_3 : 0%
             * tips : 每个用户都有自己的邀请码，只要您邀请的用户注册时输入您的邀请码，对方充值时，您将获得一定的分成奖励。
             */

            private String banner_url;
            private String invite_ratio_1;
            private String invite_ratio_2;
            private String invite_ratio_3;
            private String tips;

            public String getBanner_url() {
                return banner_url;
            }

            public void setBanner_url(String banner_url) {
                this.banner_url = banner_url;
            }

            public String getInvite_ratio_1() {
                return invite_ratio_1;
            }

            public void setInvite_ratio_1(String invite_ratio_1) {
                this.invite_ratio_1 = invite_ratio_1;
            }

            public String getInvite_ratio_2() {
                return invite_ratio_2;
            }

            public void setInvite_ratio_2(String invite_ratio_2) {
                this.invite_ratio_2 = invite_ratio_2;
            }

            public String getInvite_ratio_3() {
                return invite_ratio_3;
            }

            public void setInvite_ratio_3(String invite_ratio_3) {
                this.invite_ratio_3 = invite_ratio_3;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }
        }

        public static class ParentInviteBean {
            /**
             * user_avatar :
             * user_id : 0
             * user_nickname :
             */

            private String user_avatar;
            private String user_id;
            private String user_nickname;

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

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }
        }

        public static class ShareBean {
            /**
             * content : 我在火鸟直播，注册并输入我的邀请码FXEYJIAR4
             * logo : http://api.greenlive.1booker.com/assets/images/logo.png
             * url : h5.greenlive.1booker.com/register?invite_code=FXEYJIAR4
             */

            private String content;
            private String logo;
            private String url;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class UserBean {
            /**
             * user_invite_code : FXEYJIAR4
             * user_invite_coin_total : 0.00
             */

            private String user_invite_code;
            private String user_invite_total;
            private String user_invite_coin_total;

            public String getUser_invite_total() {
                return user_invite_total;
            }

            public void setUser_invite_total(String user_invite_total) {
                this.user_invite_total = user_invite_total;
            }

            public String getUser_invite_code() {
                return user_invite_code;
            }

            public void setUser_invite_code(String user_invite_code) {
                this.user_invite_code = user_invite_code;
            }

            public String getUser_invite_coin_total() {
                return user_invite_coin_total;
            }

            public void setUser_invite_coin_total(String user_invite_coin_total) {
                this.user_invite_coin_total = user_invite_coin_total;
            }
        }
    }
}
