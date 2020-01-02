package com.hn.library.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hn.library.global.NetConstant;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.hn.library.global.NetConstant.User.User_Forbidden;
import static com.hn.library.utils.HnLogUtils.TAG;
import static com.hn.library.utils.HnUtils.getPackageName;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：判断当前应用是否在前台
 * 创建人：mj
 * 创建时间：2017/6/9 10:09
 * 修改人：Administrator
 * 修改时间：2017/6/9 10:09
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAppStatusCheckUtil {


    private HnAppStatusCheckUtil() {
    }

    private   static class  AppStatusCheckUtilHolder{
        private static HnAppStatusCheckUtil instance=new HnAppStatusCheckUtil();

    }
    public static HnAppStatusCheckUtil getInstance(){
        return  AppStatusCheckUtilHolder.instance;
    }

    public void checkAppStatus(final Context context){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                            e.onNext(isBackground(context));
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isBackground) throws Exception {
                if(isBackground){

                }else{


                }
            }
        });

    }

    /**
     * 检测某个界面是否是可见的，避免重复的启动该界面
     * @param context
     * @param classPath
     */
    public void checkAppActivityStatus(final Context context, final String classPath){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(isForeground(context,classPath));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isBackground) throws Exception {
                        if(isBackground){

                        }else{
                            //采用Arouter框架进行界面跳转
                            String uid=  HnPrefUtils.getString(NetConstant.User.UID,"");
                            if(TextUtils.isEmpty(uid)) return;
                            //避免在接到禁用账户通知时,还有网络请求报未登录错误没从而进入登录界面
                            boolean mUser_Forbidden=HnPrefUtils.getBoolean(User_Forbidden,false);
                            if(!mUser_Forbidden) {
                                ARouter.getInstance().build("/main/HnLoginActivity", "app").navigation();
                            }
                        }
                    }
                });

    }






    /**
     * 当前应用在前台还是后台
     * @param context
     * @return
     */
    private boolean isBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo running : runnings) {
            if (running.processName.equals(getPackageName())) {
                if (running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    HnLogUtils.i("前台", running.processName);
                    return true;
                } else {
                    HnLogUtils.i("后台", running.processName);
                    return false;

                }
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context     上下文
     * @param className   某个界面名称
     *
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            Log.i(TAG,"可见的界面是:"+cpn.getClassName());
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
