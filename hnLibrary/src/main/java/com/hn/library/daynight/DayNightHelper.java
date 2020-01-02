package com.hn.library.daynight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：夜间模式帮助类
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class DayNightHelper {

    private final static String PREF_NAME = "settings";
    private final static String MODE = "day_night_mode";


    private static SharedPreferences mSp;

    public static void init(Context context) {
        mSp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存模式设置
     *
     * @param mode
     * @return
     */
    public boolean setMode(DayNight mode) {
        return mSp.edit().putString(MODE, mode.getName()).commit();
    }

    /**
     * 夜间模式
     *
     * @return
     */
    public boolean isNight() {
        String mode = mSp.getString(MODE, DayNight.DAY.getName());
        if (DayNight.NIGHT.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日间模式
     *
     * @return
     */
    public boolean isDay() {
        String mode = mSp.getString(MODE, DayNight.DAY.getName());
        if (DayNight.DAY.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }
}
