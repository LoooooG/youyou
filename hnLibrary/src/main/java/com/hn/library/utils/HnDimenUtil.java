package com.hn.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：屏幕尺寸工具类
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class HnDimenUtil {


	/**
	 * 返回屏幕宽度(像素)
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 返回屏幕高度(像素, 包含了状态栏的高度)
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 得到的屏幕的宽度
	 */
	public static int getWidthPx(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.widthPixels;
	}

	/**
	 * 得到的屏幕的高度
	 */
	public static int getHeightPx(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.heightPixels;
	}

	/**
	 * 得到屏幕的dpi
	 * @param activity
	 * @return
	 */
	public static int getDensityDpi(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.densityDpi;
	}

	/**
	 * 返回状态栏/通知栏的高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}
}
