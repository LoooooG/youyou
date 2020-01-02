package com.hotniao.livelibrary.biz.livebase;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播间公共接口回调
 * 创建人：mj
 * 创建时间：2017/9/20 10:26
 * 修改人：Administrator
 * 修改时间：2017/9/20 10:26
 * 修改备注：
 * Version:  1.0.0
 */
public interface HnLiveBaseListener {
    /**
     * 网络请求成功
     * @param type        类型  标识符
     * @param response    返回的响应数据
     * @param obj         实体等数据   根据实际需求而定
     */
    void   requestSuccess(String  type,String response,Object obj);

    /**
     * 网络请求失败
     * @param type     类型 标识符
     * @param code     错误码
     * @param msg      错误信息
     */
    void  requestFail(String  type,int code,String msg);
}
