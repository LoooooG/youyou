package com.hotniao.livelibrary.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hn.library.utils.HnPrefUtils;

import static com.hotniao.livelibrary.util.HnLiveScreentUtils.getActionBarHeight;
import static com.lqr.emoji.LQREmotionKit.dip2px;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：获取屏幕相关工具类
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveSystemUtils {


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





    /**键盘是否在显示**/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = HnLiveSystemUtils.getScreenHeight(paramActivity) - HnLiveSystemUtils.getStatusBarHeight(paramActivity)
                - HnLiveSystemUtils.getAppHeight(paramActivity);
        return height != 0;
    }


    public static int getKeyboardHeight(Activity paramActivity) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, paramActivity.getResources().getDisplayMetrics());
        if (height == 0) {
            height = HnPrefUtils.getInt("KeyboardHeight", 787);//787为默认软键盘高度 基本差不离
        }else{
            HnPrefUtils.setInt("KeyboardHeight", height);
        }
        return height;
    }



    /**显示键盘**/
    public static void showKeyBoard(final Context context, final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, 0);
            }
        });
    }

    /**关闭键盘**/
    public static void hideSoftInput(Context context,View paramEditText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    public static int getAppContentHeight(Activity paramActivity) {
        return  getScreenHeight(paramActivity) -  getStatusBarHeight(paramActivity)
                -  getActionBarHeight(paramActivity) -  dip2px(248);
    }
}
