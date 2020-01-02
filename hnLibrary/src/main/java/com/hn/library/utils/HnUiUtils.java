package com.hn.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hn.library.HnBaseApplication;

import java.lang.reflect.Method;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：UI工具类
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class HnUiUtils {

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
    }

    /**
     * 设置标题栏的padding,避免全屏显示后内容显示在状态栏中
     */
    public static void setTitlePadding(Activity activity, LinearLayout llTitle) {
        //当系统版本为5.0以上版本时采用全屏显示 ,通过在Fragment中动态设置标题栏的padding和height来调整显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusHeight = HnUtils.getStatusBarHeight(activity);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTitle.getLayoutParams();
            params.height = statusHeight + HnDimenUtil.dp2px(activity, 48.0f);
            llTitle.setLayoutParams(params);
            llTitle.setPadding(0, statusHeight, 0, 0);
        }
    }

    public static void addStatusHeight2View(Activity activity, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusHeight = HnUtils.getStatusBarHeight(activity);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = statusHeight + params.height;
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置banner的高度
     * @param context
     * @param view
     */
    public static void setBannerHeight(Context context,View view){
        if(context==null||view==null)return;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = 250 * width / 750;
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
        params.height = height;//设置当前控件布局的高度
        view.setLayoutParams(params);//将设置好的布局参数应用到控件中
    }


    /**
     * 根据传入的类(class)打开指定的activity
     *
     * @param pClass
     */
    public static void openActivity(Context context,Class<?> pClass) {
        Intent itent = new Intent();
        itent.setClass(context, pClass);
        context.startActivity(itent);
    }
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return HnBaseApplication.getContext();
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
     * 判断是否有虚拟键
     * @return
     */
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }
    /**
     * 去掉虚拟键
     * @param window
     */
    public static void solveNavigationBar(Window window) {

        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * ios审核
     * @param view
     */
    public static void setIosApply(View view) {
        if (view == null) return;
//        if (HnBaseApplication.getmUserBean() != null && "2".equals(HnBaseApplication.getmUserBean().getApple_online())) {
//            view.setVisibility(View.VISIBLE);
//        } else {
//            view.setVisibility(View.GONE);
//        }
    }

}
