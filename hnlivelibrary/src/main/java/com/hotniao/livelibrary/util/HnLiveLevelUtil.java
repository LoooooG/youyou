package com.hotniao.livelibrary.util;

import android.text.TextUtils;

import com.hotniao.livelibrary.R;
import com.reslibrarytwo.HnSkinTextView;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：等级显示
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */

public class HnLiveLevelUtil {

    /**
     * 设置用户等级
     *
     * @param tvLevel
     * @param level
     */
    public static void setAudienceLevBg(HnSkinTextView tvLevel, String level, boolean flag) {
        if (TextUtils.isEmpty(level) || "null".equals(level)) {
            level = "0";
        }
        if (flag) {
            tvLevel.setText(level);
        }

        try {
            if (Integer.parseInt(level) >= 0 && Integer.parseInt(level) < 11) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_1);
                tvLevel.setLeftDrawable(R.drawable.stars);
            } else if (Integer.parseInt(level) >= 11 && Integer.parseInt(level) < 21) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_2);
                tvLevel.setLeftDrawable(R.drawable.moon);
            } else if (Integer.parseInt(level) >= 21 && Integer.parseInt(level) < 31) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_3);
                tvLevel.setLeftDrawable(R.drawable.sun);
            } else if (Integer.parseInt(level) >= 31 && Integer.parseInt(level) < 41) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_4);
                tvLevel.setLeftDrawable(R.drawable.red_diamond);
            } else if (Integer.parseInt(level) >= 41 && Integer.parseInt(level) < 51) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_5);
                tvLevel.setLeftDrawable(R.drawable.blue_diamond);
            } else if (Integer.parseInt(level) >= 51 && Integer.parseInt(level) < 61) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_6);
                tvLevel.setLeftDrawable(R.drawable.yellow_diamond);
            } else if (Integer.parseInt(level) >= 61 && Integer.parseInt(level) < 71) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_7);
                tvLevel.setLeftDrawable(R.drawable.copper_crown);
            } else if (Integer.parseInt(level) >= 71 && Integer.parseInt(level) < 81) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_8);
                tvLevel.setLeftDrawable(R.drawable.silver_crown);
            } else if (Integer.parseInt(level) >= 81 && Integer.parseInt(level) < 91) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_9);
                tvLevel.setLeftDrawable(R.drawable.yellow_crown);
            } else if (Integer.parseInt(level) >= 91 && Integer.parseInt(level) < 101) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_shape_9);
                tvLevel.setLeftDrawable(R.drawable.yellow_crown);
            }
        } catch (Exception e) {
            tvLevel.setBackgroundResource(R.drawable.tv_level_shape_1);
            tvLevel.setLeftDrawable(R.drawable.stars);
        }
    }


    /**
     * 设置主播等级
     *
     * @param tvLevel
     * @param level
     */
    public static void setAnchorLevBg(HnSkinTextView tvLevel, String level, boolean flag) {
        if (TextUtils.isEmpty(level) || "null".equals(level)) {
            level = "0";
        }
        if (flag) {
            tvLevel.setText(level);
        }

        try {
            if (Integer.parseInt(level) >= 0 && Integer.parseInt(level) < 11) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_1);
                tvLevel.setLeftDrawable(R.drawable.anch_stars);
            } else if (Integer.parseInt(level) >= 11 && Integer.parseInt(level) < 21) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_2);
                tvLevel.setLeftDrawable(R.drawable.anch_moon);
            } else if (Integer.parseInt(level) >= 21 && Integer.parseInt(level) < 31) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_3);
                tvLevel.setLeftDrawable(R.drawable.anch_red_diamond);
            } else if (Integer.parseInt(level) >= 31 && Integer.parseInt(level) < 41) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_4);
                tvLevel.setLeftDrawable(R.drawable.anch_highest);
            } else if (Integer.parseInt(level) >= 41 && Integer.parseInt(level) < 51) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_5);
                tvLevel.setLeftDrawable(R.drawable.anch_sun);
            } else if (Integer.parseInt(level) >= 51 && Integer.parseInt(level) < 61) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_6);
                tvLevel.setLeftDrawable(R.drawable.anch_silver_crown);
            } else if (Integer.parseInt(level) >= 61 && Integer.parseInt(level) < 71) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_6);
                tvLevel.setLeftDrawable(R.drawable.anch_silver_crown);
            } else if (Integer.parseInt(level) >= 71 && Integer.parseInt(level) < 81) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_6);
                tvLevel.setLeftDrawable(R.drawable.anch_silver_crown);
            } else if (Integer.parseInt(level) >= 81 && Integer.parseInt(level) < 91) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_6);
                tvLevel.setLeftDrawable(R.drawable.anch_silver_crown);
            } else if (Integer.parseInt(level) >= 91 && Integer.parseInt(level) < 101) {
                tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_6);
                tvLevel.setLeftDrawable(R.drawable.anch_silver_crown);
            }
        } catch (Exception e) {
            tvLevel.setBackgroundResource(R.drawable.tv_level_anchor_shape_1);
            tvLevel.setLeftDrawable(R.drawable.anch_stars);
        }
    }

}
