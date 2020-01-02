package com.hotniao.livelibrary.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tencent.rtmp.TXLiveConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播模块ui工具
 * 创建人：mj
 * 创建时间：2017/9/14 12:48
 * 修改人：Administrator
 * 修改时间：2017/9/14 12:48
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveUIUtils {


    /**
     * 设置全屏
     * @param activity
     */
    public static void setFullScreen(Activity activity){
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
     * 将13位时间戳装换为天
     *
     * @param time
     */
    public static String timestamp2Date(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        //yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = null;
        if (time.length() == 13) {
            format = sdf.format(new Date(toLong(time)));
        }
        return format;
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * @param content
     * @return
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */

    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }

    }

    /**
     * @param s
     * @return
     * @description 返回字符串里中文字或者全角字符的个数
     */

    private static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**dp转px**/
    public static int dip2px(Context mContext, int dipValue) {
        float reSize = mContext.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }






}
