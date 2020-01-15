package com.hotniao.svideo.model;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：已下载的音乐
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMusicDownedModel {

    private String id;
    private String singName;
    private String singer;
    private String time;
    private String cover;
    private String localPath;
    private String serverPath;

    public HnMusicDownedModel() {
    }

    /**
     *
     * @param id
     * @param singName
     * @param singer
     * @param time
     * @param cover
     * @param localPath
     * @param serverPath
     */
    public HnMusicDownedModel(String id,String singName, String singer, String time, String cover, String localPath,String serverPath) {
        this.singName = singName;
        this.singer = singer;
        this.time = time;
        this.cover = cover;
        this.localPath = localPath;
        this.id=id;
        this.serverPath=serverPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSingName() {
        return singName;
    }

    public void setSingName(String singName) {
        this.singName = singName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
