package com.hn.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：SharedPreferences工具类
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrefUtils {

    private static final String PREF_NAME = "config";

    private static SharedPreferences sp;

    public static void init(Context context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public static void setStrings(String[] keys, String[] values) {
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.putString(keys[i], values[i]);
        }
        editor.commit();
    }

    public static void removeValue(String key) {
        sp.edit().remove(key).commit();
    }

}
