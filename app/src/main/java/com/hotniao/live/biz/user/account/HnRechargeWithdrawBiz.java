package com.hotniao.live.biz.user.account;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hotniao.live.model.PayLogModel;
import com.hotniao.live.model.WithdrawLogModel;
import com.hn.library.global.HnUrl;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于账单明细充值/提现的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/11 10:45
 * 修改人：Administrator
 * 修改时间：2017/9/11 10:45
 * 修改备注：
 * Version:  1.0.0
 */
public class HnRechargeWithdrawBiz {

    private String  TAG="HnRechargeWithdrawBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;

    public HnRechargeWithdrawBiz(BaseActivity  context) {
        this.context=context;
    }

    public  void   setBaseRequestStateListener(BaseRequestStateListener listener){
        this.listener=listener;
    }


    /**
     * 网络请求：获取充值列表数据
     * @param mPage   页数
     */
    public void requestToRechargeList(int mPage) {
        RequestParam  param=new RequestParam();
        param.put("page",mPage+"");
        param.put("pagesize",20+"");
        HnHttpUtils.getRequest(HnUrl.API_GETPAYLOGS, param, TAG, new HnResponseHandler<PayLogModel>(context,PayLogModel.class) {

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if(listener!=null){
                            listener.requestSuccess("recharge_list", response, model);

                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail("recharge_list",model.getC(),model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("recharge_list",errCode,msg);
                }
            }
        });
    }


    /**
     * 网络请求：获取提现列表数据
     * @param mPage   页数
     */
    public void requestToWithdrawList(int mPage) {
        RequestParam  param=new RequestParam();
        param.put("page",mPage+"");
        param.put("pagesize",20+"");
        HnHttpUtils.getRequest(HnUrl.API_GETWITHDRAWLOGS, param, TAG, new HnResponseHandler<WithdrawLogModel>(context,WithdrawLogModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if(listener!=null){
                        listener.requestSuccess("withdraw_list", response, model);

                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail("withdraw_list",model.getC(),model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("withdraw_list",errCode,msg);
                }
            }
        });
    }

}
