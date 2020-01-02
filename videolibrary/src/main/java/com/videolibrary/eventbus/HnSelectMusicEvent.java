package com.videolibrary.eventbus;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：选择音乐
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSelectMusicEvent {

    private String id;
    private String name;
    private String path;

    public HnSelectMusicEvent() {

    }

    public HnSelectMusicEvent(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
