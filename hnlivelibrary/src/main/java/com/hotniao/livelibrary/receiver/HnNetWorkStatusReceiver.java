package com.hotniao.livelibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.NetConstant;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnNetUtil;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：直播
 * 类描述：全局广播接收者
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnNetWorkStatusReceiver extends BroadcastReceiver {


    private String TAG="NetWorkStatusReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
         String action=intent.getAction();
         HnLogUtils.i(TAG,"接收到广播:"+action);
         if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){//获取网络状态改变广播
             HnLogUtils.i(TAG,"接收到网络状态改变广播");
             int netWorkState = HnNetUtil.getNetWorkState(context);
             EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Net_Change,netWorkState));
        }else if(HnWebscoketConstants.System_Msg.equals(action)){//接收系统消息通知栏的点击事件
             String uid=  HnPrefUtils.getString(NetConstant.User.UID,"");
             if(TextUtils.isEmpty(uid)) return;
             //跳入系统消息列表界面
             ARouter.getInstance().build("/app/HnSystemMessageActivity").navigation();
         }else if(HnWebscoketConstants.Live_Notify.equals(action)){//接收到开播提醒通知栏的操作后
             String uid=  HnPrefUtils.getString(NetConstant.User.UID,"");
             if(TextUtils.isEmpty(uid)) return;
             String  anchord_id = intent.getStringExtra("data");
             if(!TextUtils.isEmpty(anchord_id)){
                 EventBus.getDefault().post(new EventBusBean(0, HnLiveConstants.EventBus.Join_To_Room,anchord_id));
             }

         }
    }


}
