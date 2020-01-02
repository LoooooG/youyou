package com.hotniao.livelibrary.giflist;

import com.hotniao.livelibrary.giflist.bean.GiftListBean;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：礼物下载状态监听
 * 创建人：Administrator
 * 创建时间：2017/10/18 18:24
 * 修改人：Administrator
 * 修改时间：2017/10/18 18:24
 * 修改备注：
 * Version:  1.0.0
 */
public interface HnDonwloadGiftStateListener {


    /**
     * 礼物下载成功
     * @param data    礼物数据
     * @param isShow  下载完成后是否显示
     * @param obj     数据
     */
    void downloadGiftSuccess(boolean isShow,GiftListBean  data,Object obj);

    /**
     * 礼物下载失败
     * @param code   错误码
     * @param msg    原因
     * @param data   礼物数据
     */
    void downloadGiftFail(int code,String msg,GiftListBean  data);
}
