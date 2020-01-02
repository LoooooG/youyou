package com.hotniao.live.biz.user.vip;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hotniao.live.model.HnAliPayModel;
import com.hotniao.live.model.HnVipDataModel;
import com.hn.library.global.HnUrl;
import com.hotniao.live.model.HnWxPayModel;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理我的vip的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/8 13:23
 * 修改人：Administrator
 * 修改时间：2017/9/8 13:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnVipBiz {

    public static String Vip_Data="vip_list";
    public static String Vip_Data_Refresh="vip_list_refresh";
    public static String Buy_Vip_WX="buy_vip_wx";
    public static String Buy_Vip_ZFB="buy_vip_zfb";


    private String  TAG="HnVipBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;



    public HnVipBiz(BaseActivity  context) {
        this.context=context;
    }

    public  void   setBaseRequestStateListener(BaseRequestStateListener listener){
        this.listener=listener;
    }

    /**
     * 网络请求:获取vip列表
     */
    public void requestToVipList(final String  type) {

        HnHttpUtils.postRequest(HnUrl.VIP_INDEX, null, HnUrl.VIP_INDEX, new HnResponseHandler<HnVipDataModel>(HnVipDataModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if(listener!=null){
                        if(model.getD()!=null) {
                            listener.requestSuccess(type, response, model);
                        }
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail(type,model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail(type,errCode,msg);
                }
            }
        });


    }




    /**
     * 网络请求：购买vip
     * @param config_id  VIP购买配置id
     */
    public void requestToBuyVip(String config_id,String order_id) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams  param=new RequestParams();
        param.put("combo_id",config_id);
        param.put("order_id",order_id);
        HnHttpUtils.postRequest(HnUrl.Buy_vip, param, TAG, new HnResponseHandler<BaseResponseModel>(context,BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if(listener!=null){
                        listener.requestSuccess("buy_vip",response,model);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail("buy_vip",model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("buy_vip",errCode,msg);
                }
            }
        });

    }
    /**
     * 网络请求：购买vip
     * @param config_id  VIP购买配置id
     */
    public void buyVipWX(String config_id,String order_id) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams  param=new RequestParams();
        param.put("combo_id",config_id);
        param.put("order_id",order_id);
        param.put("pay_type","2");
        HnHttpUtils.postRequest(HnUrl.Buy_vip, param, TAG, new HnResponseHandler<HnWxPayModel>(HnWxPayModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if(listener!=null){
                        listener.requestSuccess(Buy_Vip_WX,response,model);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail(Buy_Vip_WX,model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail(Buy_Vip_WX,errCode,msg);
                }
            }
        });

    }
    /**
     * 网络请求：购买vip
     * @param config_id  VIP购买配置id
     */
    public void buyVipZFB(String config_id,String order_id) {
        if(listener!=null){
            listener.requesting();
        }
        RequestParams  param=new RequestParams();
        param.put("combo_id",config_id);
        param.put("order_id",order_id);
        param.put("pay_type","1");

        HnHttpUtils.postRequest(HnUrl.Buy_vip, param, TAG, new HnResponseHandler<HnAliPayModel>(HnAliPayModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if(listener!=null){
                        listener.requestSuccess(Buy_Vip_ZFB,response,model);
                    }
                } else  {
                    if(listener!=null){
                        listener.requestFail(Buy_Vip_ZFB,model.getC(),model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail(Buy_Vip_ZFB,errCode,msg);
                }
            }
        });

    }

}
