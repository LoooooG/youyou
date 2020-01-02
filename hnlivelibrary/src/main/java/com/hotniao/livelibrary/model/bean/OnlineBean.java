package com.hotniao.livelibrary.model.bean;


public class OnlineBean {

    private String id;
    private String avatar;
    private String guardLvl;
    private String isVip;

    public OnlineBean(String id, String avatar, String guardLvl) {
        this.id = id;
        this.avatar = avatar;
        this.guardLvl = guardLvl;
    }

    public OnlineBean(String id, String avatar, String guardLvl, String isVip) {
        this.id = id;
        this.avatar = avatar;
        this.guardLvl = guardLvl;
        this.isVip = isVip;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGuardLvl() {
        return guardLvl;
    }

    public void setGuardLvl(String guardLvl) {
        this.guardLvl = guardLvl;
    }
}
