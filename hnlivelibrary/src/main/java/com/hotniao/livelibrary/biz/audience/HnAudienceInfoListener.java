package com.hotniao.livelibrary.biz.audience;

import com.hotniao.livelibrary.biz.livebase.HnLiveBaseListener;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：接口回调：用于对用户端的数据交互回调
 * 创建人：Administrator
 * 创建时间：2017/9/14 20:21
 * 修改人：Administrator
 * 修改时间：2017/9/14 20:21
 * 修改备注：
 * Version:  1.0.0
 */
public interface HnAudienceInfoListener extends HnLiveBaseListener {


    /**
     * 当免费观看时间到了触发
     */
    void  freeLookFinish();

    /**
     * vip到期
     */
    void vipComeDue();


}
