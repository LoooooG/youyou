package com.hn.library.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.hn.library.HnBaseApplication;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： Toast工具类
 * 创建人：Kevin
 * 创建时间：2016/5/13 9:43
 * 修改人：Kevin
 * 修改时间：2016/5/13 9:43
 * 修改备注：
 * Version:  1.0.0
 */
public class HnToastUtils {
    private static Context context = HnBaseApplication.getContext();

    /**
     * 传入string类型  时间短
     *
     * @param msg
     */
    public static void showToastShort(String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 传人string类型 时间长
     *
     * @param msg
     */
    public static void showToastLong(String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 传人int类型 时间短
     *
     * @param strRes
     */
    public static void showToastShort(int strRes) {
        showToast(context, context.getString(strRes), Toast.LENGTH_SHORT);
    }

    /**
     * 传人int类型 时间长
     *
     * @param strRes
     */
    public static void showToastLong(int strRes) {
        showToast(context, context.getString(strRes), Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    /**
     * 指定显示在屏幕中间的Toast
     */
    public static  void showCenterToast(String msg) {
        Toast toast = Toast.makeText(HnBaseApplication.getContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /**
     * 指定显示在屏幕中间的Toast
     */
    public static  void showCenterLongToast(String msg) {
        Toast toast = Toast.makeText(HnBaseApplication.getContext(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /**
     * 指定显示在屏幕中位的Toast
     */
    public static  void showGravityToast( String msg, int gravity) {
        Toast toast = Toast.makeText(HnBaseApplication.getContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }
    /**
     * 传人int类型 指定时间长
     * 指定显示在屏幕中间的Toast
     */
    public static  void showCenterToast(int strRes) {
       showCenterToast(context.getString(strRes));
    }

    /**
     * 传人int类型 指定时间长
     * 指定显示在屏幕中位的Toast
     */
    public static  void showGravityToast( int strRes, int gravity) {
       showGravityToast(context.getString(strRes),gravity);
    }
}
