package com.hotniao.livelibrary.model.webscoket;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：私聊消息数据  用于装载webspcket推送的私聊消息数据，任何模块都可以调用
 * 创建人：mj
 * 创建时间：2017年9月8日
 * 修改人：
 * 修改时间：2017年9月8日
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateMsgModel {


    /**
     * data : {"dialog":{"dialog_id":"53","from_user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_nickname":"天行"},"msg":"1234","time":1508922309,"to_user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}}}
     * msg : 私信
     * type : send_chat
     */

    private DataBean data;
    private String msg;
    private String type;

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
        /**
         * dialog : {"dialog_id":"53","from_user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_nickname":"天行"},"msg":"1234","time":1508922309,"to_user":{"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}}
         */
        private String  chat_room_id;

        public String getChat_room_id() {
            return chat_room_id;
        }

        public void setChat_room_id(String chat_room_id) {
            this.chat_room_id = chat_room_id;
        }

        private DialogBean dialog;

        public DialogBean getDialog() {
            return dialog;
        }

        public void setDialog(DialogBean dialog) {
            this.dialog = dialog;
        }

        public static class DialogBean {
            /**
             * dialog_id : 53
             * from_user : {"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png","user_id":"9","user_nickname":"天行"}
             * msg : 1234
             * time : 1508922309
             * to_user : {"user_avatar":"http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png","user_id":"10","user_nickname":"刘测试"}
             */

            private String dialog_id;
            private FromUserBean from_user;
            private String msg;
            private int time;
            private ToUserBean to_user;

            public String getDialog_id() {
                return dialog_id;
            }

            public void setDialog_id(String dialog_id) {
                this.dialog_id = dialog_id;
            }

            public FromUserBean getFrom_user() {
                return from_user;
            }

            public void setFrom_user(FromUserBean from_user) {
                this.from_user = from_user;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public ToUserBean getTo_user() {
                return to_user;
            }

            public void setTo_user(ToUserBean to_user) {
                this.to_user = to_user;
            }

            public static class FromUserBean {
                /**
                 * user_avatar : http://static.greenlive.1booker.com//upload/image/20171017/1508229933469682.png
                 * user_id : 9
                 * user_nickname : 天行
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

            public static class ToUserBean {
                /**
                 * user_avatar : http://static.greenlive.1booker.com//upload/image/20171020/1508464692883115.png
                 * user_id : 10
                 * user_nickname : 刘测试
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
        }
    }
}
