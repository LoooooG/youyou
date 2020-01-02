package com.hotniao.livelibrary.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @创建者 阳石柏
 * @创建时间 2016/8/23 17:01
 * @描述 ${私信列表数据}
 */
public class PrivateLetterList implements Serializable {


    /**
     * items : [{"messageDialog":{"id":"28","sender_uid":"10266","receiver_uid":"10206","last_content":"333","create_time":"1470991176","update_time":"1470992249","sender_read":"1","sender_remove":"0","receiver_remove":"0","receiver_read":"0","messages":"2","type":"guard"},"sender_name":"test001","sender_avatar":"def_user_icon.png","sender_sex":"女","sender_richlvl":"8","receiver_name":"Ice world","receiver_avatar":"./20160518/14635352579074.jpg","receiver_sex":"女","receiver_richlvl":"8"}]
     * page : 1
     * pagesize : 10
     * pagetotal : 1
     * total : 1
     * prev : 1
     * next : 1
     */

    private int page;
    private int pagesize;
    private int pagetotal;
    private int total;
    private int prev;
    private int next;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * messageDialog : {"id":"28","sender_uid":"10266","receiver_uid":"10206","last_content":"333","create_time":"1470991176","update_time":"1470992249","sender_read":"1","sender_remove":"0","receiver_remove":"0","receiver_read":"0","messages":"2","type":"guard"}
     * sender_name : test001
     * sender_avatar : def_user_icon.png
     * sender_sex : 女
     * sender_richlvl : 8
     * receiver_name : Ice world
     * receiver_avatar : ./20160518/14635352579074.jpg
     * receiver_sex : 女
     * receiver_richlvl : 8
     */

    private List<ItemsBean> items;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }


    public static class ItemsBean implements Serializable {
        /**
         * id : 28
         * sender_uid : 10266
         * receiver_uid : 10206
         * last_content : 333
         * create_time : 1470991176
         * update_time : 1470992249
         * sender_read : 1
         * sender_remove : 0
         * receiver_remove : 0
         * receiver_read : 0
         * messages : 2
         * type : guard
         */

        private MessageDialogBean messageDialog;
        private String            sender_name;
        private String            sender_avatar;
        private String            sender_sex;
        private String            sender_richlvl;
        private String            receiver_name;
        private String            receiver_avatar;
        private String            receiver_sex;
        private String            receiver_richlvl;
        private int               check;

        public int getCheck() {
            return check;
        }

        public void setCheck(int check) {
            this.check = check;
        }

        public MessageDialogBean getMessageDialog() {
            return messageDialog;
        }

        public void setMessageDialog(MessageDialogBean messageDialog) {
            this.messageDialog = messageDialog;
        }

        public String getSender_name() {
            return sender_name;
        }

        public void setSender_name(String sender_name) {
            this.sender_name = sender_name;
        }

        public String getSender_avatar() {
            return sender_avatar;
        }

        public void setSender_avatar(String sender_avatar) {
            this.sender_avatar = sender_avatar;
        }

        public String getSender_sex() {
            return sender_sex;
        }

        public void setSender_sex(String sender_sex) {
            this.sender_sex = sender_sex;
        }

        public String getSender_richlvl() {
            return sender_richlvl;
        }

        public void setSender_richlvl(String sender_richlvl) {
            this.sender_richlvl = sender_richlvl;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
        }

        public String getReceiver_avatar() {
            return receiver_avatar;
        }

        public void setReceiver_avatar(String receiver_avatar) {
            this.receiver_avatar = receiver_avatar;
        }

        public String getReceiver_sex() {
            return receiver_sex;
        }

        public void setReceiver_sex(String receiver_sex) {
            this.receiver_sex = receiver_sex;
        }

        public String getReceiver_richlvl() {
            return receiver_richlvl;
        }

        public void setReceiver_richlvl(String receiver_richlvl) {
            this.receiver_richlvl = receiver_richlvl;
        }

        @Override
        public String toString() {
            return "ItemsBean{" +
                    "messageDialog=" + messageDialog +
                    ", sender_name='" + sender_name + '\'' +
                    ", sender_avatar='" + sender_avatar + '\'' +
                    ", sender_sex='" + sender_sex + '\'' +
                    ", sender_richlvl='" + sender_richlvl + '\'' +
                    ", receiver_name='" + receiver_name + '\'' +
                    ", receiver_avatar='" + receiver_avatar + '\'' +
                    ", receiver_sex='" + receiver_sex + '\'' +
                    ", receiver_richlvl='" + receiver_richlvl + '\'' +
                    '}';
        }

        public static class MessageDialogBean implements Serializable {

            private String id;
            private String sender_uid;
            private String receiver_uid;
            private String last_content;
            private String create_time;
            private String update_time;
            private String sender_read;
            private String sender_remove;
            private String receiver_remove;
            private String receiver_read;
            private String messages;
            private String type;
            private String unread;

            public String getUnread() {
                return unread;
            }

            public void setUnread(String unread) {
                this.unread = unread;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSender_uid() {
                return sender_uid;
            }

            public void setSender_uid(String sender_uid) {
                this.sender_uid = sender_uid;
            }

            public String getReceiver_uid() {
                return receiver_uid;
            }

            public void setReceiver_uid(String receiver_uid) {
                this.receiver_uid = receiver_uid;
            }

            public String getLast_content() {
                return last_content;
            }

            public void setLast_content(String last_content) {
                this.last_content = last_content;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getSender_read() {
                return sender_read;
            }

            public void setSender_read(String sender_read) {
                this.sender_read = sender_read;
            }

            public String getSender_remove() {
                return sender_remove;
            }

            public void setSender_remove(String sender_remove) {
                this.sender_remove = sender_remove;
            }

            public String getReceiver_remove() {
                return receiver_remove;
            }

            public void setReceiver_remove(String receiver_remove) {
                this.receiver_remove = receiver_remove;
            }

            public String getReceiver_read() {
                return receiver_read;
            }

            public void setReceiver_read(String receiver_read) {
                this.receiver_read = receiver_read;
            }

            public String getMessages() {
                return messages;
            }

            public void setMessages(String messages) {
                this.messages = messages;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
