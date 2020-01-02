package com.hotniao.livelibrary.ui.beauty.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import com.hotniao.livelibrary.R;

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

public class HnBeautyUtils {

    /**
     * 滤镜定义
     */
    public static final int FILTERTYPE_NONE         = 0;    //无特效滤镜
    public static final int FILTERTYPE_langman      = 1;    //浪漫滤镜
    public static final int FILTERTYPE_qingxin      = 2;    //清新滤镜
    public static final int FILTERTYPE_weimei       = 3;    //唯美滤镜
    public static final int FILTERTYPE_fennen 		= 4;    //粉嫩滤镜
    public static final int FILTERTYPE_huaijiu 		= 5;    //怀旧滤镜
    public static final int FILTERTYPE_landiao 		= 6;    //蓝调滤镜
    public static final int FILTERTYPE_qingliang 	= 7;    //清凉滤镜
    public static final int FILTERTYPE_rixi 		= 8;    //日系滤镜
    private static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
    public static Bitmap getFilterBitmap(Resources resources, int filterType) {
        Bitmap bmp = null;
        switch (filterType) {
            case FILTERTYPE_langman:
                bmp = decodeResource(resources, R.drawable.filter_langman);
                break;
            case FILTERTYPE_qingxin:
                bmp = decodeResource(resources, R.drawable.filter_qingxin);
                break;
            case FILTERTYPE_weimei:
                bmp = decodeResource(resources, R.drawable.filter_weimei);
                break;
            case FILTERTYPE_fennen:
                bmp = decodeResource(resources, R.drawable.filter_fennen);
                break;
            case FILTERTYPE_huaijiu:
                bmp = decodeResource(resources, R.drawable.filter_huaijiu);
                break;
            case FILTERTYPE_landiao:
                bmp = decodeResource(resources, R.drawable.filter_landiao);
                break;
            case FILTERTYPE_qingliang:
                bmp = decodeResource(resources, R.drawable.filter_qingliang);
                break;
            case FILTERTYPE_rixi:
                bmp = decodeResource(resources, R.drawable.filter_rixi);
                break;
            default:
                bmp = null;
                break;
        }
        return bmp;
    }

    public static String getGreenFileName(int index) {
        String strGreenFileName;
        switch (index) {
            case 0:
                strGreenFileName = "";
                break;
            case 1:
                strGreenFileName = "green_1.mp4";
                break;
            case 2:
                strGreenFileName = "green_2.mp4";
                break;
            default:
                strGreenFileName = "";
                break;
        }
        return strGreenFileName;
    }

    /**
     * 根据比例转化实际数值为相对值
     * @param gear 档位
     * @param max 最大值
     * @param curr 当前值
     * @return 相对值
     */
    public static int filtNumber(int gear, int max, int curr) {
        return curr / (max / gear);
    }
}
