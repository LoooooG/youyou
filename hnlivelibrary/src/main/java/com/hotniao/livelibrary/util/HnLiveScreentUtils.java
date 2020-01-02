package com.hotniao.livelibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import static com.lqr.emoji.LQREmotionKit.dip2px;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/9/14 17:02
 * 修改人：Administrator
 * 修改时间：2017/9/14 17:02
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveScreentUtils {


    private static int   screenW;
    private static int   screenH;
    private static float screenDensity;

    public static int getScreenW(Context context) {
        if (screenW == 0) {
            initScreen(context);
        }
        return screenW;
    }

    public static int getScreenH(Context context) {
        if (screenH == 0) {
            initScreen(context);
        }
        return screenH;
    }

    public static float getScreenDensity(Context context) {
        if (screenDensity == 0) {
            initScreen(context);
        }
        return screenDensity;
    }

    private static void initScreen(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenDensity(context) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }


    public static int getAppContentHeight(Activity paramActivity) {
        return HnLiveSystemUtils.getScreenHeight(paramActivity) - HnLiveSystemUtils.getStatusBarHeight(paramActivity)
                -  getActionBarHeight(paramActivity) - dip2px(248);
    }


    /**获取actiobar高度**/
    public static int getActionBarHeight(Activity paramActivity) {
        if (true) {
            return  dip2px(56);
        }
        int[] attrs = new int[] { android.R.attr.actionBarSize };
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0,  dip2px(56));
    }

}
