package com.hotniao.livelibrary.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.hn.library.utils.HnDateUtils;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：倒计时类
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class MyCountDownTimer extends CountDownTimer {
    private TextView mTvTime;

    /**
     * @param millisInFuture    剩余时间  毫秒
     * @param countDownInterval 间隔时间  毫秒
     * @param mTvTime           控件
     */
    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView mTvTime) {
        super(millisInFuture, countDownInterval);
        this.mTvTime = mTvTime;
    }

    @Override
    public void onFinish() {
        if (mTvTime != null)
            mTvTime.setText("00");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String day = HnDateUtils.getDay(millisUntilFinished / 1000);
        if (mTvTime != null)
            mTvTime.setText(day);
    }

}
