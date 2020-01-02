package com.hn.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.hn.library.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.Integer.parseInt;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：日期相关
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */

public class DateUtils {

    public static final String[] constellationArray = {"水瓶座", "双鱼座", "牡羊座",
            "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22,
            23, 23, 23, 23, 22, 22};

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
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }


    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    /**
     * 判断时间是否在时间段内 *
     *
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd   结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(String strDate, String strDateBegin,

                                   String strDateEnd) {

        // 截取当前时间时分秒
        long strDateH = Long.parseLong(strDate.substring(11, 13));
        long strDateM = Long.parseLong(strDate.substring(14, 16));
        long strDateS = Long.parseLong(strDate.substring(17, 19));

        // 截取开始时间时分秒
        long strDateBeginH = Long.parseLong(strDateBegin.substring(0, 2));
        long strDateBeginM = Long.parseLong(strDateBegin.substring(3, 5));
        long strDateBeginS = Long.parseLong(strDateBegin.substring(6, 8));

        // 截取结束时间时分秒
        long strDateEndH = Long.parseLong(strDateEnd.substring(0, 2));
        long strDateEndM = Long.parseLong(strDateEnd.substring(3, 5));
        long strDateEndS = Long.parseLong(strDateEnd.substring(6, 8));


        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {

            // 当前时间小时数在开始时间和结束时间小时数之间
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {

                return true;

                // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM

                    && strDateM <= strDateEndM) {

                return true;

                // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间

            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM

                    && strDateS >= strDateBeginS && strDateS <= strDateEndS) {

                return true;

            }

            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数

            else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM <= strDateEndM) {

                return true;

                // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数

            } else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM == strDateEndM && strDateS <= strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void setTimeShow(String time, TextView textView, Context context) {

        try {
            String[] split = time.split(" ");

            if (DateUtils.IsToday(time)) {

                boolean isLingChen = DateUtils.isInDate(time, "00:00:00", "04:59:59");
                boolean isAm = DateUtils.isInDate(time, "05:00:00", "11:59:59");
                boolean isPm = DateUtils.isInDate(time, "12:00:00", "17:59:59");
                boolean isEve = DateUtils.isInDate(time, "18:00:00", "23:59:59");

                if (isLingChen) {

                    String tim = split[1];
                    String[] strings = tim.split(":");
                    String s = strings[0] + ":" + strings[1];

                    textView.setText(context.getResources().getString(R.string.letter_lingc) + s);

                } else if (isAm) {

                    String tim = split[1];
                    String[] strings = tim.split(":");
                    String s = strings[0] + ":" + strings[1];

                    textView.setText(context.getResources().getString(R.string.letter_am) + s);

                } else if (isPm) {

                    String tim = split[1];
                    String[] strings = tim.split(":");
                    int i = parseInt(strings[0]);

                    String s = "";

                    if (i == 12) {
                        s = i + "" + ":" + strings[1];
                    } else {
                        s = i - 12 + "" + ":" + strings[1];
                    }

                    textView.setText(context.getResources().getString(R.string.letter_pm) + s);

                } else if (isEve) {

                    String tim = split[1];
                    String[] strings = tim.split(":");
                    String s = parseInt(strings[0]) - 12 + "" + ":" + strings[1];

                    textView.setText(context.getResources().getString(R.string.letter_eve) + s);
                }
            } else if (DateUtils.IsYesterday(time)) {

                String tim = split[1];
                String[] strings = tim.split(":");
                String s = strings[0] + ":" + strings[1];

                textView.setText(context.getResources().getString(R.string.letter_yesterday) + s);
            } else {
                textView.setText(split[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回两个日期之间相差多少天。
     *
     * @param startDate 格式"yyyy/MM/dd"
     * @param endDate   格式"yyyy/MM/dd"
     * @return 整数。
     */
    public static int getDiffDaysnew(String startDate, String endDate) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date sDate = ft.parse(startDate);
            Date eDate = ft.parse(endDate);
            diff = eDate.getTime() - sDate.getTime();
            diff = diff / 86400000;// 1000*60*60*24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) diff;

    }

    public static int getDiffDaysnew2(String startDate, String endDate) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sDate = ft.parse(startDate);
            Date eDate = ft.parse(endDate);
            diff = eDate.getTime() - sDate.getTime();
            diff = diff / 86400000;// 1000*60*60*24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) diff;

    }

    public static int getDiffYear(String startDate, String endDate) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sDate = ft.parse(startDate);
            Date eDate = ft.parse(endDate);
            diff = eDate.getTime() - sDate.getTime();
            diff = diff / 86400000;// 1000*60*60*24;
            diff = diff / (30 * 12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) diff;

    }

    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }

    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    public static String date2Constellation(String time) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(time);
            c.setTime(date);

            String constellation = date2Constellation(c);
            System.out.println("星座：" + constellation);
            return constellation;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取星座
     *
     * @param data 格式"yyyy-MM-dd"
     */
    public static String getUserStar(String data) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(data);
            c.setTime(date);
            String constellation = date2Constellation(c);
            return constellation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAge(String birthday) {
        if(TextUtils.isEmpty(birthday))return 0;
        SimpleDateFormat formatBirthday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int yearBirthday = 0, monthBirthday = 0, dayBirthday = 0;
        try {
            Date dateBirthday = formatBirthday.parse(birthday);
            Calendar calendarBirthday = Calendar.getInstance();
            calendarBirthday.setTime(dateBirthday);
            yearBirthday = calendarBirthday.get(Calendar.YEAR);
            monthBirthday = calendarBirthday.get(Calendar.MONTH);
            dayBirthday = calendarBirthday.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatMoment = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendarMoment = Calendar.getInstance();
        int yearMoment = 0, monthMoment = 0, dayMoment = 0;
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time = format.format(date);

            Date dateMoment = formatMoment.parse(time);
            calendarMoment.setTime(dateMoment);
            yearMoment = calendarMoment.get(Calendar.YEAR);
            monthMoment = calendarMoment.get(Calendar.MONTH);
            dayMoment = calendarMoment.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (yearBirthday > yearMoment || yearBirthday == yearMoment && monthBirthday > monthMoment || yearBirthday == yearMoment && monthBirthday == monthMoment && dayBirthday > dayMoment) {
            return 0;
        } else {
            int yearAge = yearMoment - yearBirthday;
            int monthAge = monthMoment - monthBirthday;
            int dayAge = dayMoment - dayBirthday;
            //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减
            if (dayAge < 0) {
                monthAge -= 1;
                calendarMoment.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数
                dayAge = dayAge + calendarMoment.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            if (monthAge < 0) {
                monthAge = (monthAge + 12) % 12;
                yearAge--;
            }
            String year, month, day;
            if (yearAge < 10) {
                year = "0" + yearAge + "年";
            } else {
                year = yearAge + "年";
            }
            if (monthAge < 10) {
                month = "0" + monthAge + "月";
            } else {
                month = monthAge + "月";
            }
            if (dayAge < 10) {
                day = "0" + dayAge + "天";
            } else {
                day = dayAge + "天";
            }
            return yearAge;
        }
    }
}
