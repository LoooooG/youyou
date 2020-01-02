package com.hotniao.livelibrary.widget.gift;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：礼物数据
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class Gift {

    /**
     * id : 10026
     * name : fd
     * icon : ./20160510/14628741269031.png
     * flv :
     * description : fdf
     * consume : 99999999
     * gain : 10
     * richexp : 10
     * anchorexp : 10
     * classid : 1
     * week : 1
     */

    private String id;
    private String name;
    private String icon;
    private String flv;
    private String description;
    private String consume;
    private String gain;
    private String richexp;
    private String anchorexp;
    private String classid;
    private String week;
    private boolean isRedBag = false;

    public boolean isRedBag() {
        return isRedBag;
    }

    public void setRedBag(boolean redBag) {
        isRedBag = redBag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFlv() {
        return flv;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }

    public String getRichexp() {
        return richexp;
    }

    public void setRichexp(String richexp) {
        this.richexp = richexp;
    }

    public String getAnchorexp() {
        return anchorexp;
    }

    public void setAnchorexp(String anchorexp) {
        this.anchorexp = anchorexp;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
