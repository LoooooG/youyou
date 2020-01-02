package com.hotniao.livelibrary.model.bean;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：live  ——socke推送
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnReceiveSocketBean  {
    /**
     * data : {"anchor":{"anchor_level":"测试内容mjl7","anchor_ranking":"测试内容5553","user_avatar":"测试内容h49c","user_dot":"测试内容73e1","user_id":"测试内容579p","user_level":"测试内容lpvs","user_nickname":"测试内容32zr"},"chat":{"content":"1234","time":1508911877},"user":{"user_avatar":"测试内容q8kp","user_coin":"0","user_id":"9","user_level":"0","user_nickname":"天行"}}
     * msg :
     * type : send_barrage
     */

    private DataBean data;
    private String msg;
    private String type;
    private String webSocketUri;

    private String notice;//公告

    public String getWebSocketUri() {
        return webSocketUri;
    }

    public void setWebSocketUri(String webSocketUri) {
        this.webSocketUri = webSocketUri;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class DataBean {

        private String  bigGiftAddress;

        private String anchor_user_id;

        public String getAnchor_user_id() {
            return anchor_user_id;
        }

        public void setAnchor_user_id(String anchor_user_id) {
            this.anchor_user_id = anchor_user_id;
        }

        public String getBigGiftAddress() {
            return bigGiftAddress;
        }

        public void setBigGiftAddress(String bigGiftAddress) {
            this.bigGiftAddress = bigGiftAddress;
        }
        /**
         * anchor : {"anchor_level":"测试内容mjl7","anchor_ranking":"测试内容5553","user_avatar":"测试内容h49c","user_dot":"测试内容73e1","user_id":"测试内容579p","user_level":"测试内容lpvs","user_nickname":"测试内容32zr"}
         * chat : {"content":"1234","time":1508911877}
         * user : {"user_avatar":"测试内容q8kp","user_coin":"0","user_id":"9","user_level":"0","user_nickname":"天行"}
         a         */



        private AnchorBean anchor;//主播信息
        private ChatBean chat;//弹幕   公聊信息
        private UserBean user;//发送者信息



        public AnchorBean getAnchor() {
            return anchor;
        }

        public void setAnchor(AnchorBean anchor) {
            this.anchor = anchor;
        }

        public ChatBean getChat() {
            return chat;
        }

        public void setChat(ChatBean chat) {
            this.chat = chat;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }



        public static class AnchorBean {
            /**
             * anchor_level : 测试内容mjl7
             * anchor_ranking : 测试内容5553
             * user_avatar : 测试内容h49c
             * user_dot : 测试内容73e1
             * user_id : 测试内容579p
             * user_level : 测试内容lpvs
             * user_nickname : 测试内容32zr
             */

            private String anchor_level;
            private String anchor_ranking;
            private String user_avatar;
            private String user_dot;
            private String user_id;
            private String user_level;
            private String user_nickname;

            public String getAnchor_level() {
                return anchor_level;
            }

            public void setAnchor_level(String anchor_level) {
                this.anchor_level = anchor_level;
            }

            public String getAnchor_ranking() {
                return anchor_ranking;
            }

            public void setAnchor_ranking(String anchor_ranking) {
                this.anchor_ranking = anchor_ranking;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_dot() {
                return user_dot;
            }

            public void setUser_dot(String user_dot) {
                this.user_dot = user_dot;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }
        }

        public static class ChatBean {
            /**
             * content : 1234
             * time : 1508911877
             */

            private String content;
            private String is_barrage_effect;//	是否高级弹幕，Y：是，N：否
            private int time;

            public String getIs_barrage_effect() {
                return is_barrage_effect;
            }

            public void setIs_barrage_effect(String is_barrage_effect) {
                this.is_barrage_effect = is_barrage_effect;
            }

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
        }

        public static class UserBean {
            /**
             * user_avatar : 测试内容q8kp
             * user_coin : 0
             * user_id : 9
             * user_level : 0
             * user_nickname : 天行
             */

            private String user_avatar;
            private String user_coin;
            private String user_id;
            private String user_level;
            private String user_nickname;
            private String prohibit_talk_time;//剩余禁言时间，-1为永久禁言
            private String is_anchor_admin;//	是否房管，Y：是，N：否
            private String is_first;//	是否第一次点赞，Y：是，N：否

            public String getIs_first() {
                return is_first;
            }

            public void setIs_first(String is_first) {
                this.is_first = is_first;
            }

            public String getProhibit_talk_time() {
                return prohibit_talk_time;
            }

            public void setProhibit_talk_time(String prohibit_talk_time) {
                this.prohibit_talk_time = prohibit_talk_time;
            }

            public String getIs_anchor_admin() {
                return is_anchor_admin;
            }

            public void setIs_anchor_admin(String is_anchor_admin) {
                this.is_anchor_admin = is_anchor_admin;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_coin() {
                return user_coin;
            }

            public void setUser_coin(String user_coin) {
                this.user_coin = user_coin;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }

            public String getUser_nickname() {
                return user_nickname;
            }

            public void setUser_nickname(String user_nickname) {
                this.user_nickname = user_nickname;
            }
        }








        /**
         * 用户进入直播间
         * fuser : {"user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_is_member":"Y","user_level":"0","user_nickname":"天行"}}
         * liveonlines : 2
         * reconnect :
         */

        private FuserBean fuser;
        private int liveonlines;
        private String reconnect;

        public FuserBean getFuser() {
            return fuser;
        }

        public void setFuser(FuserBean fuser) {
            this.fuser = fuser;
        }

        public int getLiveonlines() {
            return liveonlines;
        }

        public void setLiveonlines(int liveonlines) {
            this.liveonlines = liveonlines;
        }

        public String getReconnect() {
            return reconnect;
        }

        public void setReconnect(String reconnect) {
            this.reconnect = reconnect;
        }

        public static class FuserBean {
            /**
             * user : {"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_is_member":"Y","user_level":"0","user_nickname":"天行"}
             */

            private UserBean user;

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public static class UserBean {
                /**
                 * user_avatar : http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png
                 * user_id : 9
                 * user_is_member : Y
                 * user_level : 0
                 * user_nickname : 天行
                 */

                private String user_avatar;
                private String user_id;
                private String user_is_member;
                private String user_level;
                private String user_nickname;
                private String is_level_effect;//是否有等级特效，Y：是，N：否

                public String getIs_level_effect() {
                    return is_level_effect;
                }

                public void setIs_level_effect(String is_level_effect) {
                    this.is_level_effect = is_level_effect;
                }

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

                public String getUser_is_member() {
                    return user_is_member;
                }

                public void setUser_is_member(String user_is_member) {
                    this.user_is_member = user_is_member;
                }

                public String getUser_level() {
                    return user_level;
                }

                public void setUser_level(String user_level) {
                    this.user_level = user_level;
                }

                public String getUser_nickname() {
                    return user_nickname;
                }

                public void setUser_nickname(String user_nickname) {
                    this.user_nickname = user_nickname;
                }
            }
        }


        /**
         * 礼物
         * live_gift : {"live_gift_id":"1","live_gift_logo":"","live_gift_name":"大宝剑","live_gift_nunmer":1}
         */

        private LiveGiftBean live_gift;
        public LiveGiftBean getLive_gift() {
            return live_gift;
        }

        public void setLive_gift(LiveGiftBean live_gift) {
            this.live_gift = live_gift;
        }
        /**
         * anchor : {"anchor_level":"0","anchor_ranking":"0","user_avatar":"","user_dot":1000000005,"user_id":"8","user_level":"0","user_nickname":"2"}
         * live_gift : {"live_gift_id":"1","live_gift_logo":"","live_gift_name":"大宝剑","live_gift_nunmer":1}
         * user : {"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_coin":993820000000,"user_id":"9","user_level":"0","user_nickname":"天行"}
         */

        public static class LiveGiftBean {
            /**
             * live_gift_id : 1
             * live_gift_logo :
             * live_gift_name : 大宝剑
             * live_gift_nunmer : 1
             */

            private String live_gift_id;
            private String live_gift_logo;
            private String live_gift_name;
            private int live_gift_nunmer;

            public String getLive_gift_id() {
                return live_gift_id;
            }

            public void setLive_gift_id(String live_gift_id) {
                this.live_gift_id = live_gift_id;
            }

            public String getLive_gift_logo() {
                return live_gift_logo;
            }

            public void setLive_gift_logo(String live_gift_logo) {
                this.live_gift_logo = live_gift_logo;
            }

            public String getLive_gift_name() {
                return live_gift_name;
            }

            public void setLive_gift_name(String live_gift_name) {
                this.live_gift_name = live_gift_name;
            }

            public int getLive_gift_nunmer() {
                return live_gift_nunmer;
            }

            public void setLive_gift_nunmer(int live_gift_nunmer) {
                this.live_gift_nunmer = live_gift_nunmer;
            }
        }
    }


}
