package com.hn.library.manager;


import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.hn.library.base.BaseRequestStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：应用程序Activity管理类：用于Activity管理和应用程序退出
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class HnAppManager {

    private static List<AppCompatActivity> activitys = new ArrayList<AppCompatActivity>();
    private static HashMap<String,BaseRequestStateListener> requestListeners=new HashMap<>();
    private static HnAppManager instance;

    /**
     * 获取单一实例
     */
    public static HnAppManager getInstance() {
        if (instance == null) {
            instance = new HnAppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AppCompatActivity activity) {
        if (activitys == null) {
            activitys = new Stack<AppCompatActivity>();
        }
        activitys.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public AppCompatActivity currentActivity() {
        return activitys == null || activitys.size() == 0 ? null : activitys.get(activitys.size() - 1);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            activity.finish();
            activitys.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (AppCompatActivity activity : activitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?>... cls) {
        for (AppCompatActivity activity : activitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = activitys.size() - 1; i >= 0; i--) {
            if (null != activitys.get(i)) {
                AppCompatActivity activity = activitys.get(i);
                activity.finish();
                activity = null;
            }
        }
        activitys.clear();
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     */
    public void finishOthersActivity(Class<?> cls) {
        for (AppCompatActivity activity : activitys) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 获取指定的Activity
     */
    @SuppressWarnings("unchecked")
    public <T extends AppCompatActivity> T getActivity(Class<? extends AppCompatActivity> cls) {
        for (AppCompatActivity activity : activitys) {
            if (activity.getClass().equals(cls)) {
                return (T) activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void exit() {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess( android.os.Process.myPid());
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    /**
     * 将界面的请求添加集合中
     * @param listener
     * @param tag
     */
    public void addBaseRequestStateListener(BaseRequestStateListener listener,String  tag) {
        requestListeners.put(tag,listener);
    }

    /**
     * 从集合中清除请求
     * @param tag
     */
    public void removeBaseRequestStateListener(String  tag) {
        if(TextUtils.isEmpty(tag))  return;
        if(requestListeners==null||requestListeners.size()==0) return;
        if(requestListeners.get(tag)!=null){
            requestListeners.remove(tag);
        }
    }


    public BaseRequestStateListener getListener(String tag) {
        if(TextUtils.isEmpty(tag))  return  null;
        if(requestListeners==null||requestListeners.size()==0) return null;
        if(requestListeners.get(tag)!=null){
           return requestListeners.get(tag);
        }else{
            return  null;
        }
    }
}
