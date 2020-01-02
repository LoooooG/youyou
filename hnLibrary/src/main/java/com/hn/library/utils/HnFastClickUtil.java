package com.hn.library.utils;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：推推乐
 * 类描述：
 * 创建人：吴正星
 * 创建时间：2019/4/28 14:50
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnFastClickUtil {
    private static final int MIN_DELAY_TIME= 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
