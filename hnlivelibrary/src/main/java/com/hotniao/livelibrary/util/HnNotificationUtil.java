package com.hotniao.livelibrary.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.receiver.HnNetWorkStatusReceiver;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：通知栏管理类
 * 创建人：mj
 * 创建时间：2017/5/13 10:37
 * 修改人：Administrator
 * 修改时间：2017/5/13 10:37
 * 修改备注：
 * Version:  1.0.0
 */
public class HnNotificationUtil {

    private   String TAG="NotificationUtil";
    /**通知栏管理器*/
    private   NotificationManager       manager;
    /**通知栏*/
    private   Notification              notification;
    private   Notification.Builder      builder;
    /**收集所有的通知栏id*/
    private static List<Integer> notificationIds=new ArrayList<>();
    /**实例*/
    private  static HnNotificationUtil     instance;
    /**上下文*/
    private Context       context;

    /**
     * 单例
     */
    public static HnNotificationUtil getInstance() {
        if (null == instance) {
            synchronized (HnNotificationUtil.class) {
                if (null == instance) {
                    instance = new HnNotificationUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 设置上下文
     * @param context
     * @return
     */
    public  HnNotificationUtil  setContext(Context context){
        if(manager==null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        this.context=context;
        return instance;
    }

    /**
     * 初始化Notification
     * @param title       标题
     * @param content     文本
     * @param action      标识操作
     * @param data        携带数据
     */
    public  int notify(String title,String content,String action,Bundle data){
        int notificationId=notificationIds.size()+1;
        builder = new Notification.Builder(context);
        notification = builder
                .setAutoCancel(false)//是否自动取消
                .setSmallIcon(R.drawable.logo)//小图标
                .setContentText(content)//文本内容
                .setContentTitle(title).//标题
                setWhen(System.currentTimeMillis())//时间显示
                .build();

        //发送广播
        Intent intentClick = new Intent(context, HnNetWorkStatusReceiver.class);
        intentClick.putExtras(data);
        intentClick.setAction(action);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(context, notificationId, intentClick,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntentClick);
        notification=builder.build();
        notification.flags=Notification.FLAG_AUTO_CANCEL;//设置自动取消
        //使用默认的声音、振动、闪光
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(notificationId, notification);
        HnLogUtils.i(TAG,"notificationId:"+notificationId);
        notificationIds.add(notificationId);
        return  notificationId;

    }


    /**
     * 初始化Notification
     * @param title       标题
     * @param content     文本
     * @param action      标识操作
     * @param data        携带数据

     */
    public  int notify(String title,String content,String action,String data){
        int notificationId=notificationIds.size()+1;
        builder = new Notification.Builder(context);
        notification = builder
                .setAutoCancel(false)//是否自动取消
                .setSmallIcon(R.drawable.logo)//小图标
                .setContentText(content)//文本内容
                .setContentTitle(title).//标题
                setWhen(System.currentTimeMillis())//时间显示
//                .setTicker(ticker)//ticker
                .build();

        //发送广播
        Intent intentClick = new Intent(context, HnNetWorkStatusReceiver.class);
        Bundle bundle=new Bundle();
        bundle.putString("data",data);
        intentClick.putExtras(bundle);
        intentClick.setAction(action);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(context, notificationId, intentClick,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntentClick);
        notification=builder.build();
        notification.flags=Notification.FLAG_AUTO_CANCEL;//设置自动取消
        //使用默认的声音、振动、闪光
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(notificationId, notification);
        HnLogUtils.i(TAG,"notificationId:"+notificationId);
        notificationIds.add(notificationId);
        return  notificationId;
    }


    /**
     * 清除所有的通知
     * @param
     */
    public void clearNotifications() {
        if(manager!=null) {
            for (int i = 0; i < notificationIds.size(); i++) {
                int id = notificationIds.get(i);
                manager.cancel(id);
                HnLogUtils.i(TAG, "取消的通知栏id：" + id);
            }
            notificationIds.clear();
        }
    }


    /**
     * 清除指定id
     * @param id
     */
    public void clearNotifications(int   id) {
            if(manager!=null) {
                manager.cancel(id);
            }
    }
}
