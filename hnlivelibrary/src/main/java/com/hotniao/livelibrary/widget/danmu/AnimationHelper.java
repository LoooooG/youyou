package com.hotniao.livelibrary.widget.danmu;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：弹幕动画
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class AnimationHelper {

    /**
     * 创建平移动画
     */
    public static ObjectAnimator createTranslateAnim(View view,int fromX, int toX) {

        ObjectAnimator tlAnim = ObjectAnimator.ofFloat(view, "translationX", fromX, toX);
        tlAnim.setInterpolator(new DecelerateInterpolator());
        tlAnim.setDuration(10000);
        return tlAnim;
    }
}
