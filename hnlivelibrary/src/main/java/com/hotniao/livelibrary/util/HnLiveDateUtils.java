package com.hotniao.livelibrary.util;

import android.content.Context;

import com.hn.library.utils.DateUtils;
import com.hotniao.livelibrary.R;

import static java.lang.Integer.parseInt;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播时间相关工具类
 * 创建人：mj
 * 创建时间：2017/9/14 20:27
 * 修改人：Administrator
 * 修改时间：2017/9/14 20:27
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveDateUtils {

    /**
     * 直播时间格式化
     * @param liveTime
     * @return
     */
    public static String getLiveTime(long liveTime) {
        //时
        long hour = liveTime / 3600;
        String sHour = String.valueOf(hour);
        if (sHour.length() < 2) {
            sHour = "0" + sHour;
        }
        //分
        long min = (liveTime - (hour * 3600)) / 60;
        String sMin = String.valueOf(min);
        if (sMin.length() < 2) {
            sMin = "0" + min;
        }
        //秒
        long sec = (liveTime - (hour * 3600)) % 60;
        String sSec = String.valueOf(sec);
        if (sSec.length() < 2) {
            sSec = "0" + sSec;
        }
        return  sHour+":"+sMin+":"+sSec;
    }

    public static String setTimeShow(String time, Context context) {

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


                    return  context.getResources().getString(R.string.letter_lingc) + s;

                } else if (isAm) {

                    String tim = split[1];
                    String[] strings = tim.split(":");
                    String s = strings[0] + ":" + strings[1];

                    return context.getResources().getString(R.string.letter_am) + s;

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

                    return context.getResources().getString(R.string.letter_pm) + s;

                } else if (isEve) {

                    String tim = split[1];
                    String[] strings = tim.split(":");
                    String s = parseInt(strings[0]) - 12 + "" + ":" + strings[1];

                    return context.getResources().getString(R.string.letter_eve) + s;
                }
            } else if (DateUtils.IsYesterday(time)) {

                String tim = split[1];
                String[] strings = tim.split(":");
                String s = strings[0] + ":" + strings[1];

                return  context.getResources().getString(R.string.letter_yesterday) + s;
            } else {
                return split[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  "";
    }
}
