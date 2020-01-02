package com.hn.library;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.model.HnConfigModel;
import com.hn.library.model.HnLoginBean;
import com.hn.library.utils.CrashHandlerUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnPrefUtils;

import java.io.File;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 自定义全局 application 主要进全局引用,行存储全局变量,全局配置/设置,初始化等相关工作
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnBaseApplication extends MultiDexApplication {

    private static Context mContext;


    //用户信息
    public static HnLoginBean mUserBean;
    //配置信息
    public static HnConfigModel.DBean mConfig;

    public static HnConfigModel.DBean getmConfig() {
        return mConfig;
    }

    public static void setmConfig(HnConfigModel.DBean mConfig) {
        HnBaseApplication.mConfig = mConfig;
    }

    public static HnLoginBean getmUserBean() {
        return mUserBean;
    }

    public static void setmUserBean(HnLoginBean mUserBean) {
        HnBaseApplication.mUserBean = mUserBean;
    }

    /**
     * 获取全局的Context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //初始化SharedPreferences
        HnPrefUtils.init(this);
        DayNightHelper.init(this);
        //初始化Fresco

       /*ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();*/
        Fresco.initialize(this, FrescoConfig.getImagePipelineConfig(this));
        //全局捕获异常初始化
        CrashHandlerUtils.getInstance().init(this);
    }
}
