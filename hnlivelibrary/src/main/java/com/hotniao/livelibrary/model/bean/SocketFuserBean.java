package com.hotniao.livelibrary.model.bean;

import java.io.Serializable;

/**
 * @创建者 阳石柏
 * @创建时间 2016/8/22 20:48
 * @描述 ${websocket推送过来的用户信息}
 */
public class SocketFuserBean implements Serializable{


    /**
     * addr : 广东-深圳-福田区
     */

    private String addr;
    private String id;
    private String nick;
    private String icons;
    private String avatar;
    private String richlvl;
    private String anchorlvl;
    private String viplvl;
    private String mountid;
    private String superadmin;
    private String logintype;
    private int    guardlvl;
    private int    admin;
    private String sex;
    private String lvlname;
    private int    followed;
    private String coin;
    private String dot;
    private String livetitle;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getLivetitle() {
        return livetitle;
    }

    public void setLivetitle(String livetitle) {
        this.livetitle = livetitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRichlvl() {
        return richlvl;
    }

    public void setRichlvl(String richlvl) {
        this.richlvl = richlvl;
    }

    public String getAnchorlvl() {
        return anchorlvl;
    }

    public void setAnchorlvl(String anchorlvl) {
        this.anchorlvl = anchorlvl;
    }

    public String getViplvl() {
        return viplvl;
    }

    public void setViplvl(String viplvl) {
        this.viplvl = viplvl;
    }

    public String getMountid() {
        return mountid;
    }

    public void setMountid(String mountid) {
        this.mountid = mountid;
    }

    public String getSuperadmin() {
        return superadmin;
    }

    public void setSuperadmin(String superadmin) {
        this.superadmin = superadmin;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public int getGuardlvl() {
        return guardlvl;
    }

    public void setGuardlvl(int guardlvl) {
        this.guardlvl = guardlvl;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLvlname() {
        return lvlname;
    }

    public void setLvlname(String lvlname) {
        this.lvlname = lvlname;
    }

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }
}
