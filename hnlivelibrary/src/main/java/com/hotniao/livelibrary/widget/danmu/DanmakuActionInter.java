package com.hotniao.livelibrary.widget.danmu;


import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：弹幕接口
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public interface DanmakuActionInter {
    /**
     * 添加弹幕
     */
    void addDanmu(HnReceiveSocketBean dan);

    /**
     * 移出弹幕
     */
    void pollDanmu();


}
