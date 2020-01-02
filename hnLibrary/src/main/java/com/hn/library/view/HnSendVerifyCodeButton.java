package com.hn.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.R;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：自定义倒计时发送验证码的按钮
 * 创建人：Kevin
 * 创建时间：2016/5/6 15:09
 * 修改人：Kevin
 * 修改时间：2016/5/6 15:09
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSendVerifyCodeButton extends LinearLayout {
    private Runnable mRunnable = new MyRunnable(this);
    private TextView tvAccessVerify;
    private TextView tvScending;
    private RelativeLayout mRlVerify;
    private CountDownTimer mCountDownTimer;
    private OnVerifyClickListener mVerifyClickListener;
    private Context mContext;
    private boolean isShowBg;

    //对外提供接口,处理访问网络数据的操作
    public interface OnVerifyClickListener {
        void onVerify();
    }

    public HnSendVerifyCodeButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        View.inflate(context, R.layout.button_verify, this);
        this.tvAccessVerify = (TextView) findViewById(R.id.verify_true);
        this.mRlVerify = (RelativeLayout) findViewById(R.id.mRlVerify);
        this.tvScending = (TextView) findViewById(R.id.verify_false);
//		this.tvAccessVerify.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (mVerifyClickListener != null) {
//					mVerifyClickListener.onVerify();
//				}
//			}
//		});
    }


    public void setOnVerifyClickListener(OnVerifyClickListener verifyClickListener) {
        this.mVerifyClickListener = verifyClickListener;
    }

    public void setEnble(boolean isShowBg) {
        this.isShowBg = isShowBg;
        if (isShowBg) {
            if (tvAccessVerify.getVisibility() == VISIBLE) {
                mRlVerify.setBackgroundResource(R.drawable.verify_shape_main);
                tvAccessVerify.setTextColor(getResources().getColor(R.color.comm_text_color_black));
            }
        } else {
            if (tvScending.getVisibility() != VISIBLE) {
                tvAccessVerify.setTextColor(getResources().getColor(R.color.comm_text_color_black_ss));
                mRlVerify.setBackgroundResource(R.drawable.verify_shape);
            }
        }
    }

    /**
     * 验证码发送结束，按钮变成有颜色
     */
    private void setVerifyVisible() {
        this.tvScending.setVisibility(View.GONE);
        this.tvAccessVerify.setVisibility(View.VISIBLE);
        if (isShowBg) {
            mRlVerify.setBackgroundResource(R.drawable.verify_shape_main);
            tvAccessVerify.setTextColor(getResources().getColor(R.color.comm_text_color_black));
        } else {
            tvAccessVerify.setTextColor(getResources().getColor(R.color.comm_text_color_black_ss));
            mRlVerify.setBackgroundResource(R.drawable.verify_shape);
        }
        stopCountDownTimer();
    }

    /**
     * 显示正在发送(*s)
     */
    private void setVerifyUnVisible() {
        this.tvScending.setVisibility(View.VISIBLE);
        mRlVerify.setBackgroundResource(R.drawable.verify_shape);
        this.tvAccessVerify.setVisibility(View.GONE);
    }

    public boolean getIsStart() {
        return VISIBLE == this.tvScending.getVisibility() ? true : false;
    }

    /**
     * 开启计时
     */
    public void startCountDownTimer() {
        setVerifyUnVisible();
        new Handler().postDelayed(this.mRunnable, 0);
    }

    /**
     * 停止计时
     */
    private void stopCountDownTimer() {
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.cancel();
        }
    }

    /**
     * 开启子线程
     */
    class MyRunnable implements Runnable {
        final HnSendVerifyCodeButton sendVerifyCodeButton;

        MyRunnable(HnSendVerifyCodeButton sendVerifyCodeButton) {
            this.sendVerifyCodeButton = sendVerifyCodeButton;
        }

        @Override
        public void run() {
            //从60s开始以秒为单位倒计时
            this.sendVerifyCodeButton.mCountDownTimer = new MyCountDownTimer(this, 60000L, 1000).start();
        }

    }

    /**
     * 设定一个计时器
     */
    class MyCountDownTimer extends CountDownTimer {
        final MyRunnable mRunnable;

        public MyCountDownTimer(MyRunnable runnable, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//millisInFuture：倒计时的总时间，countDownInterval:计时的时间间隔

            this.mRunnable = runnable;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            this.mRunnable.sendVerifyCodeButton.tvScending.setText("" + ((int) (millisUntilFinished / 1000)) + "s");

        }

        @Override
        public void onFinish() {
            this.mRunnable.sendVerifyCodeButton.mCountDownTimer = null;
            this.mRunnable.sendVerifyCodeButton.setVerifyVisible();
        }

    }
}
