package com.hotniao.live;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hn.library.HnBaseApplication;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnLogUtils;
import com.imlibrary.business.InitBusiness;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.tencent.TIMLogLevel;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：自定义全局 application 主要进全局引用,行存储全局变量,全局配置/设置,初始化等相关工作
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnApplication extends HnBaseApplication {

    public static Context mContext;
    /**
     * 所有本地音乐
     * <p>
     * {@Link com.live.mini.activity.LocalMp3Activity}
     */
    public static List<String> allMp3 = new ArrayList<>();
    private static int mMainThreadId;
    private static Handler mMainThreadHandler;

//    static {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
//    }

    /**
     * get方法
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取主线程id
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 设置极光别名
     */
    public static void login(String alias) {
        alias = TextUtils.isEmpty(alias) ? HnHttpUtils.getUniqueid() : alias;
        JPushInterface.resumePush(getContext());
        JPushInterface.setAlias(getContext(), alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                HnLogUtils.e("JPush 设置别名 " + (i == 0 ? "成功" : "失败") + " " + s + " i:" + i);
            }
        });
    }

    public static void logout() {
        JPushInterface.stopPush(getContext());
        JPushInterface.setAlias(getContext(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                HnLogUtils.e("极光 取消别名 " + (i == 0 ? "成功" : "失败") + " " + s);
            }
        });
    }

    /**
     * 程序入口
     */
    @Override
    public void onCreate() {
        super.onCreate();


        /**上下文*/
        mContext = getApplicationContext();

        /**主线程ID*/
        mMainThreadId = android.os.Process.myTid();

        /**主线程Handler*/
        mMainThreadHandler = new Handler();

        HnLogUtils.init(BuildConfig.DEBUG, "Hn");
        //微信朋友圈和微信分享
        PlatformConfig.setWeixin("wx1bff55e20ba7ff84", "816a3e43552058b85e42cd5b0957b973");//huoliao
        //QQ分享
        PlatformConfig.setQQZone("1108817124", "TXBQw3ZoF5EIfn98");//moyan
        //新浪分享
        PlatformConfig.setSinaWeibo("458521347", "fb04d9753f21e7dbc84e3b87ca96230b");//
        //回调新浪
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/opencallback";


        //TODO:自定义表情初始化
        LQREmotionKit.init(this, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        });

        //Arouter  路由框架配置
        if (HnLogUtils.LOG_DEBUG) {      // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        initLoadingLayout();

        //腾讯sdk初始化配置
        TXLiveBase.setConsoleEnabled(true);
        TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        initTXImSdk();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);            // 初始化 JPush

    }

    /**
     * 腾讯云通信初始化
     */
    private void initTXImSdk() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
    }

    /**
     * 初始化全局加载状态
     */
    private void initLoadingLayout() {
        //背景色
        HnLoadingLayout.getConfig()
                .setEmptyText(getString(R.string.no_data))
                .setEmptyImage(R.drawable.empty_com);
    }


}
