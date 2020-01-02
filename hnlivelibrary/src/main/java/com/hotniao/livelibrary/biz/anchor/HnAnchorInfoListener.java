package com.hotniao.livelibrary.biz.anchor;

import com.hotniao.livelibrary.biz.livebase.HnLiveBaseListener;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：接口回调：用于对主播端的数据交互回调
 * 创建人：Administrator
 * 创建时间：2017/9/14 20:21
 * 修改人：Administrator
 * 修改时间：2017/9/14 20:21
 * 修改备注：
 * Version:  1.0.0
 */
public interface HnAnchorInfoListener extends HnLiveBaseListener {

    /**
     * 计时器
     * @param date   秒数
     * @param time   转化好的时间
     */
     void  showTimeTask(long date,String time,String type);


}
