package com.hotniao.svideo.biz.msg;

import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.model.GetSystemMsgModel;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理系统消息界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/12 11:04
 * 修改人：Administrator
 * 修改时间：2017/9/12 11:04
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSystemMsgBiz {


    private  String  TAG="HnSystemMsgBiz";
    private  BaseActivity context;

    private  BaseRequestStateListener listener;
    public   HnSystemMsgBiz(BaseActivity context) {
        this.context=context;
    }

    public  void   setBaseRequestStateListener(BaseRequestStateListener listener){
        this.listener=listener;
    }

    /**
     * 请求系统详情列表数据
     *
     * @param mPage  页数
     */
    public void requestSystemMessage( int mPage,String dialogId) {
        RequestParam param = new RequestParam();
        param.put("page", mPage + "");
        param.put("type",   "system");
        if(!TextUtils.isEmpty(dialogId))param.put("dialog_id",dialogId);
        HnHttpUtils.getRequest(HnUrl.SYSTEM_MESSAGE, param, TAG, new HnResponseHandler<GetSystemMsgModel>(GetSystemMsgModel.class) {
            @Override
            public void hnSuccess(String response) {
                if(model.getC()==0){
                    if(listener!=null){
                        listener.requestSuccess("System_Msg_Detail_List",response,model);
                    }
                }else{
                    if(listener!=null){
                        listener.requestFail("System_Msg_Detail_List",model.getC(),model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                listener.requestFail("System_Msg_Detail_List",errCode,msg);
                }
        }
        });
    }


    /**
     * 网络请求:退出系统消息详情  服务端清除对应的未读消息
     * @param
     */
    public void requestToExitSysMsgDetail( ){

        RequestParams param=new RequestParams();
        param.put("uid","0");
        HnHttpUtils.postRequest(HnUrl.Exit_Msg_Detail, param, TAG, new HnResponseHandler<BaseResponseModel>(context,BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {

            }

            @Override
            public void hnErr(int errCode, String msg) {

            }
        });
    }
}
