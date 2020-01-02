package com.hotniao.live.biz.user.fans;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.live.model.HnMyFansModel;
import com.hn.library.global.HnUrl;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于粉丝界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/6 9:22
 * 修改人：Administrator
 * 修改时间：2017/9/6 9:22
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFansBiz {

    private String  TAG="HnFansBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;


    public HnFansBiz(BaseActivity  context) {
        this.context=context;
    }

    public  void   setRegisterListener(BaseRequestStateListener listener){
        this.listener=listener;

    }

    /**
     * 网络请求：获取粉丝列表
     * @param page     页数
     */
    public void requestFans(int page) {
        RequestParam param = new RequestParam();
        param.put("page", page+"");
        param.put("pagesize", 20+"");
        HnHttpUtils.getRequest(HnUrl.USER_FANS, param, "获取粉丝列表", new HnResponseHandler<HnMyFansModel>(context,HnMyFansModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                           if (listener != null) {
                               listener.requestSuccess("fans_list", response, model);
                           }
                } else  {
                    if(listener!=null){
                        listener.requestFail("fans_list",model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("fans_list",errCode,msg);
                }
            }
        });
    }

    /**
     * 网络请求取消关注
     * @param uid   用户is
     * @param position  位置
     */
    public void cancelFollow(String uid, final int position) {
        if(listener!=null){
            listener.requesting();
        }
        HnUserControl.cancelFollow(uid, new HnUserControl.OnUserOperationListener() {
            @Override
            public void onSuccess(String uid, Object obj, String response) {
                if (listener != null) {
                    listener.requestSuccess("cancelFollow", response, position);
                }
            }
            @Override
            public void onError(String uid, int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("cancelFollow",errCode,msg);
                }
            }
        });
    }

    /**
     * 网络请求：添加关注
     * @param uid  用户id
     * @param position  位置
     */
    public void addFollow(String uid, final int position) {
        if(listener!=null){
            listener.requesting();
        }
        HnUserControl.addFollow(uid,null, new HnUserControl.OnUserOperationListener() {
            @Override
            public void onSuccess(String uid, Object obj, String response) {
                if (listener != null) {
                    listener.requestSuccess("addFollow", response, position);
                }
            }

            @Override
            public void onError(String uid, int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("addFollow",errCode,msg);
                }
            }
        });
    }
}
