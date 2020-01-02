package com.hotniao.livelibrary.biz.anchor;

import com.hn.library.base.BaseActivity;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseRequestBiz;
import com.hotniao.livelibrary.config.HnLiveUrl;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：网络业务逻辑类：该类不做其他操作，只做主播端的相关网络操作，请勿将其他操作放入其中
 * 创建人：mj
 * 创建时间：2017/9/16 10:48
 * 修改人：Administrator
 * 修改时间：2017/9/16 10:48
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorRequestBiz  extends HnLiveBaseRequestBiz {

    private String  TAG="HnAnchorRequestBiz";



    /**主播端信息回调接口*/
    private HnAnchorInfoListener listener;
    /**上下文*/
    private BaseActivity context;


    public HnAnchorRequestBiz(HnAnchorInfoListener listener, BaseActivity context) {
               this.listener=listener;
               this.context=context;
    }

    /**
     * 免费/vip直播调用
     * 网络请求：心跳包
     */
    public void requestToHeardBeat() {
        if(context==null)  return;
        HnHttpUtils.getRequest(HnLiveUrl.Anchot_Heart_Beat, null, TAG, new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                HnLogUtils.i(TAG,"心跳请求成功");
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnLogUtils.i(TAG,"心跳请求失败");
            }
        });
    }




}
