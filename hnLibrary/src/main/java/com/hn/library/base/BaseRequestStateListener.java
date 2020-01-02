package com.hn.library.base;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/9/1 18:37
 * 修改人：Administrator
 * 修改时间：2017/9/1 18:37
 * 修改备注：
 * Version:  1.0.0
 */
public interface BaseRequestStateListener {

    //请求中
    void   requesting();

    //请求成功
    void   requestSuccess(String type,String response,Object obj);

    //请求失败
    void   requestFail(String type,int code,String msg);
}
