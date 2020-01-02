package com.hotniao.live.biz.user.follow;

import android.app.Activity;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hotniao.live.R;
import com.hn.library.view.CommDialog;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.live.model.HnMyFocusModel;
import com.hn.library.global.HnUrl;
import com.hotniao.live.utils.HnUiUtils;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于关注界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/6 9:22
 * 修改人：Administrator
 * 修改时间：2017/9/6 9:22
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFollowBiz {
    public static final String CANCLE="cancelFollow";
    public static final String ADD="addFollow";

    private String  TAG="HnFollowBiz";
    private Activity context;
    private BaseRequestStateListener listener;


    public HnFollowBiz(Activity  context) {
        this.context=context;
    }

    public  void   setRegisterListener(BaseRequestStateListener listener){
        this.listener=listener;

    }

    /**
     * 网络请求：获取关注列表
     * @param page     页数
     */
    public void requestFollow(int page) {
        RequestParam param = new RequestParam();
        param.put("page", page+"");
        param.put("pagesize", 20+"");
        HnHttpUtils.getRequest(HnUrl.USER_FOCUS, param, "获取关注列表", new HnResponseHandler<HnMyFocusModel>(HnMyFocusModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                           if (listener != null) {
                               listener.requestSuccess("follow_list", response, model);
                           }
                } else  {
                    if(listener!=null){
                        listener.requestFail("follow_list",model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("follow_list",errCode,msg);
                }
            }
        });
    }

    /**
     * 取消关注弹窗
     * @param uid   用户is
     * @param position  位置
     */
    public void cancelFollow(final String uid, final int position) {
//        CommDialog.newInstance(context).setClickListen(new CommDialog.TwoSelDialog() {
//            @Override
//            public void leftClick() {
//            }
//            @Override
//            public void rightClick() {
//                cancelFollowHttp(uid, position);
//            }
//        }).setTitle(HnUiUtils.getString(R.string.cancle_focus)).setContent(HnUiUtils.getString(R.string.cancle_focus_cannot_receive_live_msg)).show();
        cancelFollowHttp(uid, position);
    }
    /**
     * 网络请求取消关注
     * @param uid   用户is
     * @param position  位置
     */
    private void cancelFollowHttp(String uid, final int position){
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
        HnUserControl.addFollow(uid, null,new HnUserControl.OnUserOperationListener() {
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
