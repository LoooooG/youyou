package com.hotniao.livelibrary.widget.danmu;


import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：弹幕管理
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class DanmakuActionManager implements DanmakuActionInter {
    public List<DanmakuChannel> channels = new LinkedList<>();

    public Queue<HnReceiveSocketBean> danEntities = new LinkedList<>();

    @Override
    public void addDanmu(HnReceiveSocketBean dan) {
        danEntities.add(dan);
        looperDan();
    }

    @Override
    public void pollDanmu() {
        looperDan();
    }

    public void addChannel(DanmakuChannel channel) {
        channels.add(channel);
    }

    public DanmakuActionManager() {

    }

    public void looperDan() {
        for (int i = 0; i < channels.size(); i++) {
            if (!channels.get(i).isRunning && danEntities.size() > 0) {
                HnReceiveSocketBean poll = danEntities.poll();  //检索并移除第一个元素
                channels.get(i).mStartAnimation(poll);
            }
        }
    }


    /**
     * 清除弹幕的所有显示视图以及数据
     */
    public void clearAll() {
        if (channels != null && channels.size() > 0){
            for (int i = 0; i < channels.size(); i++) {
                channels.get(i).isRunning=false;
                channels.get(i).clearChildView();
            }
        }
        if(danEntities!=null) {
            danEntities.clear();
        }
    }
}
