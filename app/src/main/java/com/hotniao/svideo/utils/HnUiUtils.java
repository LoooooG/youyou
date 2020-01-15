package com.hotniao.svideo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrFrameLayout;
import com.hotniao.svideo.HnApplication;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：UI工具类
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnUiUtils {

    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //全屏显示
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        Window window = activity.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    /**
     * 设置标题栏的padding,避免全屏显示后内容显示在状态栏中
     */
    public static void setTitlePadding(Activity activity, ViewGroup llTitle) {
        //当系统版本为5.0以上版本时采用全屏显示 ,通过在Fragment中动态设置标题栏的padding和height来调整显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusHeight = getStatusBarHeight(activity);
            ViewGroup.LayoutParams params = llTitle.getLayoutParams();
            //params.height = statusHeight + HnDimenUtil.dip2px(activity, 48.0f);
            //llTitle.setLayoutParams(params);
            llTitle.setPadding(0, statusHeight, 0, 0);
        }
    }

    /**
     * 获得状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 设置刷新模式  是否可以加载更多
     *
     * @param mPtr     刷新控件
     * @param mPage    页数
     * @param pageSize 每页条数
     * @param dataSize 数据长度
     */
    public static void setRefreshMode(PtrClassicFrameLayout mPtr, int mPage, int pageSize, int dataSize) {
        try {
            if (mPtr == null) return;
            if (dataSize < (pageSize * mPage)) {
                mPtr.setMode(PtrFrameLayout.Mode.REFRESH);
            } else {
                mPtr.setMode(PtrFrameLayout.Mode.BOTH);
            }
        } catch (Exception e) {
        }
    }

    public static void setRefreshModeNone(PtrClassicFrameLayout mPtr, int mPage, int pageSize, int dataSize) {
        try {
            if (mPtr == null) return;
            if (dataSize < (pageSize * mPage)) {
                mPtr.setMode(PtrFrameLayout.Mode.NONE);
            } else {
                mPtr.setMode(PtrFrameLayout.Mode.LOAD_MORE);
            }
        } catch (Exception e) {
        }
    }


    /**
     * 得到上下文
     */
    public static Context getContext() {
        return HnApplication.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }


    /**
     * ios审核
     * @param view
     */
    public static void setIosApply(View view) {
        if (view == null) return;
//        if (HnApplication.getmUserBean() != null && "2".equals(HnApplication.getmUserBean().getApple_online())) {
//            view.setVisibility(View.VISIBLE);
//        } else {
//            view.setVisibility(View.GONE);
//        }
    }

}
