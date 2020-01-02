package com.hotniao.livelibrary.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class DataTimeUtils {


    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    public DataTimeUtils() {
    }

    public static String getTimestampString(long timeStamp) {
        if (timeStamp == 0) return "";

        String var1 = null;
        String var2 = Locale.getDefault().getLanguage();
        boolean var3 = var2.startsWith("zh");

        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp );
        Date currenTimeZone = inputTime.getTime();

        long var4 = currenTimeZone.getTime();
        if (isSameDay(var4)) {
            if (var3) {
                var1 = "HH:mm";
            } else {
                var1 = "HH:mm";
            }
        } else if (isYesterday(var4)) {
            if (!var3) {
                return "Yesterday " + (new SimpleDateFormat("HH:mm", Locale.ENGLISH)).format(currenTimeZone);
            }

            var1 = "昨天 HH:mm";
        } else if (var3) {
            var1 = "yyyy.MM.dd HH:mm";
        } else {
            var1 = "yyyy.MM.dd HH:mm";
        }

        return var3 ? (new SimpleDateFormat(var1, Locale.CHINESE)).format(currenTimeZone) : (new SimpleDateFormat(var1, Locale.ENGLISH)).format(currenTimeZone);
    }

    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if (var4 < 0L) {
            var4 = -var4;
        }

        return (var4/1000 )< 300L;
    }

    private static boolean isSameDay(long var0) {
        TimeInfo var2 = getTodayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    private static boolean isYesterday(long var0) {
        TimeInfo var2 = getYesterdayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    @SuppressLint({"SimpleDateFormat"})
    public static Date StringToDate(String var0, String var1) {
        SimpleDateFormat var2 = new SimpleDateFormat(var1);
        Date var3 = null;

        try {
            var3 = var2.parse(var0);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return var3;
    }

    @SuppressLint({"DefaultLocale"})
    public static String toTime(int var0) {
        var0 /= 1000;
        int var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            int var4 = var1 / 60;
            var1 %= 60;
        }

        int var3 = var0 % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var3)});
    }

    @SuppressLint({"DefaultLocale"})
    public static String toTimeBySecond(int var0) {
        int var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            int var4 = var1 / 60;
            var1 %= 60;
        }

        int var3 = var0 % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var3)});
    }

    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();

        var0.add(Calendar.DATE, -1);
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        var4.add(Calendar.DATE, -1);
        var4.set(Calendar.HOUR_OF_DAY, 23);
        var4.set(Calendar.MINUTE, 59);
        var4.set(Calendar.SECOND, 59);
        var4.set(Calendar.MILLISECOND, 999);
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    public static TimeInfo getTodayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        var4.set(Calendar.HOUR_OF_DAY, 23);
        var4.set(Calendar.MINUTE, 59);
        var4.set(Calendar.SECOND, 59);
        var4.set(Calendar.MILLISECOND, 999);
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.add(Calendar.DATE, -2);
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        var4.add(Calendar.DATE, -2);
        var4.set(Calendar.HOUR_OF_DAY, 23);
        var4.set(Calendar.MINUTE, 59);
        var4.set(Calendar.SECOND, 59);
        var4.set(Calendar.MILLISECOND, 999);
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    public static TimeInfo getCurrentMonthStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.add(Calendar.DATE, 1);
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    public static TimeInfo getLastMonthStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.add(Calendar.MONTH, -1);
        var0.add(Calendar.DATE, 1);
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        var4.add(Calendar.MONTH, -1);
        var4.add(Calendar.DATE, 1);
        var4.set(Calendar.HOUR_OF_DAY, 23);
        var4.set(Calendar.MINUTE, 59);
        var4.set(Calendar.SECOND, 59);
        var4.set(Calendar.MILLISECOND, 999);
        var4.roll(Calendar.DATE, -1);
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }

    static class TimeInfo {
        private long startTime;
        private long endTime;

        public TimeInfo() {
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long var1) {
            this.startTime = var1;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long var1) {
            this.endTime = var1;
        }
    }


    /**
     * 判断给定字符串时间是否为今日(效率不是很高，不过也是一种方法)
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();


    /**
     * 判断是否为今天(效率比较高)
     *
     * @param timeStamp 传入的 时间戳   秒
     * @return true今天 false不是
     */
    public static boolean IsToday(long timeStamp) {

        if (timeStamp == 0) return false;
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();

        long var4 = currenTimeZone.getTime();
        if (isSameDay(var4)) {
            return true;
        }
        return false;

    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param timeStamp 传入的 时间戳   秒
     * @return true今天 false不是
     */
    public static boolean IsYesterday(long timeStamp)  {
        if (timeStamp == 0) return false;
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();

        long var4 = currenTimeZone.getTime();
        if (isYesterday(var4)) {
            return true;

        }
        return false;
    }


    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    /**
     * 将字符串转为时间戳
     *
     * @param string
     * @return
     * @throws ParseException
     */
    public static long getTimeFromString(String string) throws ParseException {
        if (TextUtils.isEmpty(string)) {
            return System.currentTimeMillis();
        }

        long time;
        try {
            time = Long.valueOf(string) * 1000l;
        } catch (NumberFormatException e) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            time = simpleDateFormat.parse(string).getTime();
        }

        return time;
    }

}
