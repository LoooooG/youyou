package com.hotniao.svideo.biz.set;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.svideo.R;
import com.hn.library.view.CommDialog;
import com.hotniao.svideo.model.HnUserExitMode;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.utils.HnUiUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理设置界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/7 14:48
 * 修改人：Administrator
 * 修改时间：2017/9/7 14:48
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSetBiz {

    private  String  TAG="HnSetBiz";
    private  BaseActivity context;

    private HnCacheDealUtils  mHnCacheDealUtils;

    private BaseRequestStateListener listener;
    public   HnSetBiz(BaseActivity context) {
        this.context=context;
        mHnCacheDealUtils=new HnCacheDealUtils(context);
    }

    public  void   setBaseRequestStateListener(BaseRequestStateListener listener){
        this.listener=listener;
    }


    /**
     * 执行网络请求退出
     */
    public void executeExit() {
        //发送eventbus关闭服务
        EventBus.getDefault().post(new EventBusBean(0,"stop_websocket_service",null));
        HnPrefUtils.setString(NetConstant.User.UID,"");
        HnPrefUtils.setString(NetConstant.User.Webscket_Url,"");
        HnPrefUtils.setString(NetConstant.User.TOKEN,"");
        if(listener!=null){
            listener.requesting();
        }
        HnHttpUtils.getRequest(HnUrl.USER_EXIT, null, "logout", new HnResponseHandler<HnUserExitMode>(context,HnUserExitMode.class) {

            @Override
            public void hnSuccess(String response) {
                if(listener!=null){
                    listener.requestSuccess("logout",response,model);
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if(listener!=null){
                    listener.requestFail("logout",errCode,msg);
                }
            }
        });
    }

    /**
     * 清除数据
     */
    private void   clearData(){
        HnPrefUtils.setString(HnConstants.LogInfo.OPENID, "");
        HnPrefUtils.setString(HnConstants.LogInfo.USERNAME, "");
        HnPrefUtils.setString(HnConstants.Sp.USER_ID, "");
        HnPrefUtils.setString(NetConstant.User.UID,"");
        HnPrefUtils.setString(NetConstant.User.TOKEN,"");
    }
    /**
     * 获取App的缓存大小
     */
    public void getAppCache() {
        Observable.just(cacheSize())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        if(listener!=null){
                            listener.requestSuccess("app_cache",result,result);
                        }
                    }
                });

    }
    /**
     * 计算缓存
     */
    public String cacheSize() {
        if(context==null||mHnCacheDealUtils==null)  return  "0M";
        return mHnCacheDealUtils.getCacheSize();
    }

    /**
     * 清除缓存
     */
    public void cleanCache() {
        if(context==null)  return;
        CommDialog.newInstance(context).setClickListen(new CommDialog.TwoSelDialog() {
            @Override
            public void leftClick() {}
            @Override
            public void rightClick() {
                mHnCacheDealUtils.clearCache();
                if(listener!=null){
                    listener.requestSuccess("app_cache_cear_success",null,"0M");
                }
            }
        }).setTitle(HnUiUtils.getString(R.string.str_clear_cache_title)).setContent( HnUiUtils.getString(R.string.str_clear_cache_info)).show();

    }




}
