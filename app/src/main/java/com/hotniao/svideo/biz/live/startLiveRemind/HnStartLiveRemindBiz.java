package com.hotniao.svideo.biz.live.startLiveRemind;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hotniao.svideo.model.HnLiveNoticeModel;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.utils.HnUiUtils;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理开播提醒界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/7 17:38
 * 修改人：Administrator
 * 修改时间：2017/9/7 17:38
 * 修改备注：
 * Version:  1.0.0
 */
public class HnStartLiveRemindBiz {

    private  String  TAG="HnStartLiveRemindBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;
    public   HnStartLiveRemindBiz(BaseActivity context) {
        this.context=context;
    }

    public  void   setBaseRequestStateListener(BaseRequestStateListener listener){
        this.listener=listener;
    }

    /**
     * 网络请求：获取开播提醒用户列表
     * @param mPage  页数
     */
    public void requestToLiveNoticeList(int mPage) {
        RequestParam param = RequestParam.builder(HnUiUtils.getContext());
        param.put("page", mPage + "");
        HnHttpUtils.getRequest(HnUrl.USER_FOLLOW_REMINDS, param, "开播提醒", new HnResponseHandler<HnLiveNoticeModel>(HnLiveNoticeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("live_notice_list", response, model);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail("live_notice_list",model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("live_notice_list",errCode,msg);
                }
            }
        });
    }


    /**
     * 网络请求： 设置-开播提醒
     * @param uid      关注的用户ID,0表示修改全部的
     * @param type      Y：提醒，N：取消
     * @param pos       位置
     */
    public void requestToSetLiveNoticeWay(String uid, final String type, final int pos) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams param=new RequestParams();
        param.put("user_id",uid);
        param.put("is_remind",type);
        HnHttpUtils.postRequest(HnUrl.SET_LIVE_REMIND, param, TAG, new HnResponseHandler<BaseResponseModel>(context,BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("set_live_notice", type,pos);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail("set_live_notice",model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("set_live_notice",errCode,msg);
                }
            }
        });
    }
}
