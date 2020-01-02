package com.hotniao.livelibrary.biz.anchor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseViewBiz;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.util.HnLiveUIUtils;
import com.hotniao.livelibrary.widget.danmu.DanmakuActionManager;
import com.hotniao.livelibrary.widget.dialog.HnUserLiveForbiddenDialog;
import com.hotniao.livelibrary.widget.dialog.privateLetter.HnPrivateLetterListDialog;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.lqr.emoji.EmotionLayout;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：主播端ui业务逻辑类，该类只做主播间的ui相关操作，不做其他事情
 * 创建人：Administrator
 * 创建时间：2017/9/15 16:32
 * 修改人：Administrator
 * 修改时间：2017/9/15 16:32
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAnchorViewBiz extends HnLiveBaseViewBiz {

    private String TAG = "HnAnchorViewBiz";
    private boolean isOpenAnim = false;


    /**
     * 显示私信列表适配器
     *
     * @param mActivity
     */
    public void showPriveteLetterListDialog(BaseActivity mActivity, boolean isAnchor, String anchor_id, String anchor_nick, String avator, String level, String gender) {
        HnPrivateLetterListDialog mHnPrivateLetterListDialog = HnPrivateLetterListDialog.getInstance(isAnchor, anchor_id, anchor_nick, avator, level, gender, "");
        mHnPrivateLetterListDialog.show(mActivity.getSupportFragmentManager(), "privateletter");
    }

    /**
     * 弹出发送消息界面
     *
     * @param mRlMessage       发送消息的父容器
     * @param mBottomContainer 底部按钮控制容器
     * @param etSendData       输入框
     */
    public void showMessageSendLayout(RelativeLayout mRlMessage, LinearLayout mBottomContainer, EditText etSendData, Context context) {
        if (mBottomContainer != null) {
            mBottomContainer.setVisibility(View.GONE);
        }
        if (mRlMessage != null) {
            mRlMessage.setVisibility(View.VISIBLE);
        }
        if (etSendData != null) {
            //自动获取焦点
            etSendData.setFocusable(true);
            etSendData.setFocusableInTouchMode(true);
            etSendData.requestFocus();
            openSoftKeyBoard(context);
        }

    }


    /**
     * 直播间视图变化时的ui处理
     *
     * @param bottom            底部距离
     * @param oldBottom         之前的底部距离
     * @param mRoomTopContainer 父容器
     * @param mElEmotion        表情控件
     * @param leftFiftLl        礼物控制器
     * @param rlMessage         消息父容器
     * @param llBottomContainer 底部按钮容器
     * @param context           上下文
     * @param keyHeight         阀值设置为屏幕高度的1/3
     * @param ivEmoj
     */
    public void onLayoutChnage(int bottom, int oldBottom, RelativeLayout mRoomTopContainer, EmotionLayout mElEmotion, LeftGiftControlLayout leftFiftLl,
                               RelativeLayout rlMessage, LinearLayout llBottomContainer, Context context, int keyHeight, ImageView ivEmoj) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {  //键盘弹出
            animateToHide(mRoomTopContainer, context);
            if (ivEmoj != null) ivEmoj.setImageResource(R.mipmap.smile);
            if (leftFiftLl != null) {
                setMargins(leftFiftLl, 0, 0, 0, HnLiveUIUtils.dip2px(context, -60));
            }
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {  //键盘隐藏
            if (mElEmotion != null && mElEmotion.getVisibility() == View.GONE) {
                if (rlMessage != null && rlMessage.getVisibility() == View.VISIBLE) {
                    rlMessage.setVisibility(View.GONE);
                }
                if (llBottomContainer != null && llBottomContainer.getVisibility() == View.GONE) {
                    llBottomContainer.setVisibility(View.VISIBLE);
                }
            }
            animateToShow(mRoomTopContainer, context);
            if (leftFiftLl != null) {
                setMargins(leftFiftLl, 0, 0, 0, 0);
            }
        }
    }

    /**
     * 界面touch事件处理  当用户点击界面触发touch事件时，将发送消息的布局/键盘影藏，底部/顶部的按钮显示出来。
     *
     * @param view              onTouch 返回的view
     * @param mElEmotion        表情控制容器
     * @param rlMessage         发送消息容器
     * @param llBottomContainer 底部按钮容器
     * @param mRoomTopContainer 顶部容器
     * @param context           上下文
     */
    public void onTouch(View view, EmotionLayout mElEmotion, RelativeLayout rlMessage, LinearLayout llBottomContainer, RelativeLayout mRoomTopContainer,
                        Context context) {
        if (!(view instanceof EditText) || !(view instanceof ImageView)) {
            if (rlMessage.getVisibility() == View.VISIBLE) {
                llBottomContainer.setVisibility(View.VISIBLE);
                rlMessage.setVisibility(View.GONE);

                mElEmotion.setVisibility(View.GONE);

                hideSoftKeyBoard(rlMessage, context);
                animateToShow(mRoomTopContainer, context);
            }
        }
    }


    /**
     * 设置边距
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    /**
     * 顶部容器动画显示
     */
    private void animateToShow(View mRoomTopContainer, Context context) {
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(mRoomTopContainer, "translationY", -(mRoomTopContainer.getHeight() + HnUtils.dip2px(context, 20)), 0);
        topAnim.setDuration(10);
        topAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpenAnim = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpenAnim = true;
            }
        });
        if (!isOpenAnim) {
            topAnim.start();
        }
    }

    /**
     * 顶部容器动画隐藏
     */
    private void animateToHide(View mRoomTopContainer, Context context) {

        ObjectAnimator topAnim = ObjectAnimator.ofFloat(mRoomTopContainer, "translationY", 0, -(mRoomTopContainer.getHeight() + HnUtils.dip2px(context, 20)));
        topAnim.setDuration(10);
        topAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpenAnim = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpenAnim = true;
            }
        });
        if (!isOpenAnim) {
            topAnim.start();
        }
    }


    /**
     * 触发弹幕
     *
     * @param danmakuActionManager 弹幕管理器
     * @param object               数据源
     */
    public void setDanmu(DanmakuActionManager danmakuActionManager, Object object) {
        HnReceiveSocketBean data = (HnReceiveSocketBean) object;
        if (data != null) {
            danmakuActionManager.addDanmu(data);
        }
    }


    /**
     * 主播禁播提示框
     *
     * @param mActivity activity
     * @param time      禁播时间   -1 ：永久禁播
     */
    public void showLiveForbidenDialog(AppCompatActivity mActivity, String time) {
        if (mActivity == null) return;
        HnUserLiveForbiddenDialog dialog = HnUserLiveForbiddenDialog.getInstance(time);
        dialog.show(mActivity.getSupportFragmentManager(), "HnUserLiveForbiddenDialog");
    }
}
