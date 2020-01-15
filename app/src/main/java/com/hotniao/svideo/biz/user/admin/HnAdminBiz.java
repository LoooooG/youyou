package com.hotniao.svideo.biz.user.admin;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.model.HnSearchAdminModel;
import com.loopj.android.http.RequestParams;

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
public class HnAdminBiz {
    public static  final String  Search_Admin="search_admin";
    public static  final String  Add_Admin="add_admin";
    public static  final String  Delete_Admin="delete_admin";

    private String  TAG="HnFansBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;


    public HnAdminBiz(BaseActivity  context) {
        this.context=context;
    }

    public  void   setRegisterListener(BaseRequestStateListener listener){
        this.listener=listener;

    }


    /**
     * 网络请求搜索房管
     * @param kw  关键字
     */
    public void searchAdmin(String kw,int page) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("kw", kw);
        param.put("page", page);
        param.put("pagesize", 20);
        HnHttpUtils.postRequest(HnUrl.LIVE_ROOMADMIN_SEARCH, param,HnUrl.LIVE_ROOMADMIN_SEARCH, new HnResponseHandler<HnSearchAdminModel>(context,HnSearchAdminModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(Search_Admin, response, model);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail(Search_Admin,model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail(Search_Admin,errCode,msg);
                }
            }
        });
    }
    /**
     * 网络请求取消房管
     * @param uid   用户is
     * @param position  位置
     */
    public void cancelAdmin(final String uid, final int position) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("user_id", uid);
        HnHttpUtils.postRequest(HnUrl.LIVE_ROOMADMIN_DELETE, param,HnUrl.LIVE_ROOMADMIN_DELETE, new HnResponseHandler<BaseResponseModel>(context,BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(Delete_Admin, uid, position);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail(Delete_Admin,model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail(Delete_Admin,errCode,msg);
                }
            }
        });
    }

    /**
     * 网络请求：添加房管
     * @param uid  用户id
     * @param position  位置
     */
    public void addAdmin(final String uid, final int position) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("user_id", uid);
        HnHttpUtils.postRequest(HnUrl.LIVE_ROOMADMIN_ADD, param, HnUrl.LIVE_ROOMADMIN_ADD, new HnResponseHandler<BaseResponseModel>(context,BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(Add_Admin, uid, position);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail(Add_Admin,model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail(Add_Admin,errCode,msg);
                }
            }
        });
    }


}
