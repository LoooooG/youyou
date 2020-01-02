package com.hotniao.livelibrary.model.bean;

/**
 * @创建者 mj
 * @创建时间 2016/12/21 16:39
 * @描述 ${私信Bean}
 */
public class PrivateChatBean {

    /**uid*/
    private  String  uid;
    /**头像*/
    private  String  avator;
    /**昵称*/
    private  String  nick;
    /**消息内容*/
    private  String  msgContent;
    /**时间*/
    private  String  add_time;
    /**用于标识适配器显示不同的视图*/
    private  String     showMsgTyoe;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getShowMsgTyoe() {
        return showMsgTyoe;
    }

    public void setShowMsgTyoe(String showMsgTyoe) {
        this.showMsgTyoe = showMsgTyoe;
    }
}
