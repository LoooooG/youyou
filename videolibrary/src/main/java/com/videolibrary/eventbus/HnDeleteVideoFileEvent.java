package com.videolibrary.eventbus;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnDeleteVideoFileEvent {
   private  boolean  saveVideoFile;

    public HnDeleteVideoFileEvent(boolean saveVideoFile) {
        this.saveVideoFile = saveVideoFile;
    }

    public boolean isSaveVideoFile() {
        return saveVideoFile;
    }

    public void setSaveVideoFile(boolean saveVideoFile) {
        this.saveVideoFile = saveVideoFile;
    }
}
