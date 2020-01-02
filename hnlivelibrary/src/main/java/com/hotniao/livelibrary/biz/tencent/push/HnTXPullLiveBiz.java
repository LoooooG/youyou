package com.hotniao.livelibrary.biz.tencent.push;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：腾讯推流api操作类
 * 创建人：mj
 * 创建时间：2017/10/11 12:45
 * 修改人：Administrator
 * 修改时间：2017/10/11 12:45
 * 修改备注：
 * Version:  1.0.0
 */
public class HnTXPullLiveBiz {


    private String  TAG="HnTXPushLiveBiz";

    /**
     * 单例
     */
    private HnTXPullLiveBiz() {

    }
    public static class HnTXPullLiveBizHolder {
        private static HnTXPullLiveBiz instance = new HnTXPullLiveBiz();
    }
    public static HnTXPullLiveBiz getInstance() {
        return HnTXPullLiveBizHolder.instance;
    }






}
