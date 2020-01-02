package com.hotniao.livelibrary.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hn.library.HnBaseApplication;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：获取屏幕相关工具类
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class SystemUtils {


    /**屏幕分辨率高**/
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**statusBar高度**/
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    /**可见屏幕高度**/
    public static int getAppHeight(Activity paramActivity) {

        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }


    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static  int getSoftButtonsBarHeight(Activity paramActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        paramActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        paramActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public static int getAppContentHeight(Activity paramActivity) {
        return SystemUtils.getScreenHeight(paramActivity) - SystemUtils.getStatusBarHeight(paramActivity)
                - SystemUtils.getActionBarHeight(paramActivity) - SystemUtils.dip2px(248);
    }

    /**获取actiobar高度**/
    public static int getActionBarHeight(Activity paramActivity) {
        if (true) {
            return SystemUtils.dip2px(56);
        }
        int[] attrs = new int[] { android.R.attr.actionBarSize };
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0, SystemUtils.dip2px(56));
    }

    /**dp转px**/
    public static int dip2px(int dipValue) {
        float reSize = HnBaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    /**键盘是否在显示**/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = SystemUtils.getScreenHeight(paramActivity) - SystemUtils.getStatusBarHeight(paramActivity)
                - SystemUtils.getAppHeight(paramActivity);
        return height != 0;
    }


    /**关闭键盘**/
    public static void hideSoftInput(View paramEditText) {
        ((InputMethodManager)HnBaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }


    /**显示键盘**/
    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager)HnBaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, 0);
            }
        });
    }
}
