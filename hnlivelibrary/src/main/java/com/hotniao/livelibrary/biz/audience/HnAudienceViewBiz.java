package com.hotniao.livelibrary.biz.audience;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import android.widget.ToggleButton;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.livebase.HnLiveBaseViewBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.fragment.HnPayDialogFragment;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.hotniao.livelibrary.util.HnLiveUIUtils;
import com.hotniao.livelibrary.widget.danmu.DanmakuActionManager;
import com.hotniao.livelibrary.widget.dialog.HnBalanceNotEnoughDialog;
import com.hotniao.livelibrary.widget.dialog.HnVIPOutDialog;
import com.hotniao.livelibrary.widget.dialog.privateLetter.HnPrivateLetterListDialog;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;
import com.lqr.emoji.EmotionLayout;
import com.reslibrarytwo.HnSkinTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：用户端ui业务逻辑类，该类只做用户端的ui相关操作，不做其他事情
 * 创建人：Administrator
 * 创建时间：2017/9/15 16:32
 * 修改人：Administrator
 * 修改时间：2017/9/15 16:32
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAudienceViewBiz extends HnLiveBaseViewBiz {

    private String TAG = "HnAnchorViewBiz";
    private boolean isOpenAnim = false;


    /**
     * 显示私信列表适配器
     *
     * @param mActivity   上下文
     * @param isAnchor    是否是主播     当不是主播时，需要传递后面的字段数据。用于默认添加一条主播聊天信息；主播自己后续字段可为空
     * @param anchor_id   主播id
     * @param anchor_nick 主播的头像
     * @param avator      主播的头像
     * @param level       主播级别
     * @param gender      主播性别
     */
    public void showPriveteLetterListDialog(BaseActivity mActivity, boolean isAnchor, String anchor_id, String anchor_nick, String avator, String level, String gender, String mChatRoomId) {
        HnPrivateLetterListDialog mHnPrivateLetterListDialog = HnPrivateLetterListDialog.getInstance(isAnchor, anchor_id, anchor_nick, avator, level, gender, mChatRoomId);
        mHnPrivateLetterListDialog.show(mActivity.getSupportFragmentManager(), "privateletter");
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
     * @param ivEmoji
     */
    public void onLayoutChnage(int bottom, int oldBottom, RelativeLayout mRoomTopContainer, EmotionLayout mElEmotion, LeftGiftControlLayout leftFiftLl,
                               RelativeLayout rlMessage, LinearLayout llBottomContainer, Context context, int keyHeight, ImageView ivEmoji) {



        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {  //键盘弹出
            animateToHide(mRoomTopContainer, context);
            if (ivEmoji != null) ivEmoji.setImageResource(R.mipmap.smile);
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
    public void onTouch(View view, EmotionLayout mElEmotion, RelativeLayout rlMessage, LinearLayout llBottomContainer, RelativeLayout mRoomTopContainer, Context context) {
        if (!(view instanceof EditText)) {
            if (rlMessage.getVisibility() == View.VISIBLE) {
                llBottomContainer.setVisibility(View.VISIBLE);
                rlMessage.setVisibility(View.GONE);
                if (mElEmotion != null)
                    mElEmotion.setVisibility(View.GONE);
                hideSoftKeyBoard(rlMessage, context);
                animateToShow(mRoomTopContainer, context);
            }
            if (mElEmotion != null)
                mElEmotion.setVisibility(View.GONE);
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
    public void animateToShow(View mRoomTopContainer, Context context) {
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
     * 用户进场特效  等级需在15级以上
     *
     * @param context     上下文
     * @param userLevel   等级
     * @param nick        用户昵称
     * @param llEnterRoom
     * @param tvLevel
     * @param tvHighLevel
     */
//    public void liveEffect(final Context context, String isLvEffect,String isVip, String nick, final LinearLayout llEnterRoom, TextView tvLevel, TextView tvHighLevel) {
//
//        try {
//            if (Integer.parseInt(userLevel) >= 15) {
//
//                if (llEnterRoom.getVisibility() == View.INVISIBLE) {
//                    llEnterRoom.setVisibility(View.VISIBLE);
//                    HnLiveLevelUtil.setAudienceLevBg(tvLevel, userLevel, true);
//                    tvHighLevel.setText(nick);
//                    Animation highLevelEnterAnima = AnimationUtils.loadAnimation(context, R.anim.highlevel_drop_from_left);
//                    highLevelEnterAnima.setFillAfter(true);
//                    llEnterRoom.startAnimation(highLevelEnterAnima);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Animation highLevelExitAnima = AnimationUtils.loadAnimation(context, R.anim.hide_to_top);
//                            highLevelExitAnima.setFillAfter(true);
//                            llEnterRoom.startAnimation(highLevelExitAnima);
//                            llEnterRoom.setVisibility(View.INVISIBLE);
//                        }
//                    }, 3000);
//                }
//            }
//
//        } catch (Exception e) {
//            HnLogUtils.i(TAG, "触发进场动画失败：" + e.getMessage());
//        }
//    }


    /**
     * 根据直播类型和状态弹出提示框
     *
     * @param mActivity          上下文
     * @param live_type          直播类型 0：免费，1：VIP，2：门票，3：计时
     * @param isBananceNotEnough 是否处于金额不够  true：金额不够  false 免费试看结束
     * @param live_Price         直播需要支付的金额
     * @param isClixkBuy         是否是点击购买按钮
     */
    public void showHintDialog(BaseActivity mActivity, String live_type, boolean isBananceNotEnough, String live_Price, boolean isClixkBuy, FrameLayout mFrame) {
        if (mActivity == null || mFrame == null) return;
        try {
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Show_Mask, 0));
            EventBus.getDefault().post(new HnLiveEvent(1, HnLiveConstants.EventBus.Close_Dialog_Show_Mark, 0));
            if (isClixkBuy) {
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mFramePay, HnPayDialogFragment.newInstance(live_Price, live_type))
                        .commitAllowingStateLoss();
                mFrame.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new HnLiveEvent(1001, HnLiveConstants.EventBus.Scroll_viewPager, false));
            } else {
                if (isBananceNotEnough) {//金额不足
                    int enoughType = HnBalanceNotEnoughDialog.FistPay;
                    if ("3".equals(live_type) && View.VISIBLE != mFrame.getVisibility()) {
                        enoughType = HnBalanceNotEnoughDialog.AgainPayEnough;
                    }

                    HnBalanceNotEnoughDialog mHnBalanceNotEnoughDialog = HnBalanceNotEnoughDialog.newInstance(enoughType);
                    mHnBalanceNotEnoughDialog.show(mActivity.getSupportFragmentManager(), "HnBalanceNotEnoughDialog");

                } else {
                    mActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mFramePay, HnPayDialogFragment.newInstance(live_Price, live_type))
                            .commitAllowingStateLoss();
                    mFrame.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new HnLiveEvent(1001, HnLiveConstants.EventBus.Scroll_viewPager, false));
                }
            }
        } catch (Exception e) {
        }

    }

    /**
     * 显示vip到时对话框
     */
    public void showVIPOoutTimeDialog(BaseActivity activity) {
        HnVIPOutDialog mHnVIPOutDialog = HnVIPOutDialog.newInstance();
        mHnVIPOutDialog.show(activity.getSupportFragmentManager(), "HnVIPOutDialog");
    }

    /**
     * 清除左上角数据 当切换直播间时需要将直播间的数据清空
     *
     * @param context
     * @param fivHeader
     * @param tvName
     * @param tvPeopleNumber
     * @param tvFollow
     * @param tvId
     */
    public void clearLeftTopViewData(Context context, FrescoImageView fivHeader, TextView tvName, TextView tvPeopleNumber, TextView tvFollow, TextView tvId) {
        fivHeader.setImageURI("111");
        tvName.setText("");
        tvPeopleNumber.setText("0人");
        tvFollow.setVisibility(View.GONE);
        tvId.setText(context.getResources().getString(R.string.live_u_hao));
    }

    /**
     * 输入视图重置  当切换直播间时需要将直播间的数据清空
     *
     * @param etSendData
     * @param rlMessage
     * @param llBottomContainer
     * @param mElEmotion
     * @param mToggleButton
     * @param mRoomTopContainer
     */
    public void clearInputView(AppCompatActivity mActivity, EditText etSendData, RelativeLayout rlMessage, LinearLayout llBottomContainer, EmotionLayout mElEmotion, ToggleButton mToggleButton, RelativeLayout mRoomTopContainer) {

        etSendData.setText("");
        if (rlMessage.getVisibility() == View.VISIBLE) {
            llBottomContainer.setVisibility(View.VISIBLE);
            rlMessage.setVisibility(View.GONE);
            if (mElEmotion!=null&&mElEmotion.getVisibility() == View.VISIBLE) {
                mElEmotion.setVisibility(View.GONE);
            }

            hideSoftKeyBoard(rlMessage, mActivity);
            animateToShow(mRoomTopContainer, mActivity);

        }
        mToggleButton.setChecked(false);
    }

    /**
     * 主播离开直播间的数据重置  当切换直播间时需要将直播间的数据清空   清除进场动画
     *
     * @param mLlEnterRoom
     * @param mEnterData
     * @param highLevelEntenerAnima
     * @param highLevelExitAnima
     */
    public void clearEnterRoomAnim(LinearLayout mLlEnterRoom, List<HnReceiveSocketBean.DataBean.FuserBean> mEnterData, Animation highLevelEntenerAnima, Animation highLevelExitAnima) {
        try {
            mEnterData.clear();
            mLlEnterRoom.setVisibility(View.GONE);
            highLevelEntenerAnima.cancel();
            highLevelExitAnima.cancel();

        } catch (Exception e) {
        }
    }


    /**
     * 主播离开直播间的数据重置  当切换直播间时需要将直播间的数据清空
     *
     * @param mActivity
     * @param viewBg
     * @param mHeaderIcon
     * @param tvNick
     * @param tvStart
     * @param ivSex
     * @param tvUserLeaveLevel
     * @param tvLiveLevel
     */
    public void clearAnchorLeaveRoomView(Context mActivity, View viewBg, FrescoImageView mHeaderIcon, TextView tvNick, TextView tvStart, ImageView ivSex, HnSkinTextView tvUserLeaveLevel, TextView tvLiveLevel) {
        viewBg.setVisibility(View.GONE);
        //主播头像
        mHeaderIcon.setImageURI("111");
        tvNick.setText("");
        //主播name
        tvStart.setText(mActivity.getResources().getString(R.string.live_follow_anchor));
        ivSex.setImageResource(R.mipmap.man);
        //用户等级
        HnLiveLevelUtil.setAudienceLevBg(tvUserLeaveLevel, "1", true);
        //主播等级
        tvLiveLevel.setVisibility(View.GONE);
    }

    /**
     * 清除动画视图  当切换直播间时需要将直播间的数据清空
     *
     * @param leftFiftLl
     * @param mBigGiftActionManager
     * @param danmakuActionManager
     */
    public void clearAnimtionView(LeftGiftControlLayout leftFiftLl, BigGiftActionManager mBigGiftActionManager, DanmakuActionManager danmakuActionManager) {
        /**
         * 清除礼物动画
         */
        leftFiftLl.cleanAll();
        /**
         * 清除大礼物
         */
        mBigGiftActionManager.clearAll();
        /**
         * 弹幕移除
         */
        danmakuActionManager.clearAll();
    }

    /**
     * 设置直播间里主播离开房间的视图界面ui数据
     *
     * @param context          上下文
     * @param anchor           主播信息
     * @param viewBg           离开房间父视图
     * @param mHeaderIcon      头像
     * @param tvNick           昵称
     * @param tvStart          关注
     * @param ivSex            性别
     * @param tvUserLeaveLevel 用户等级
     * @param tvLiveLevel      主播等级
     */
    public String setAnchorLeaveRoomViewData(Context context, HnUserInfoDetailBean anchor, View viewBg, FrescoImageView mHeaderIcon, TextView tvNick, TextView tvStart, ImageView ivSex, HnSkinTextView tvUserLeaveLevel, TextView tvLiveLevel) {
        String isFollow = "N";
        viewBg.setVisibility(View.VISIBLE);
        //主播头像
        String avator = anchor.getUser_avatar();
        mHeaderIcon.setController(FrescoConfig.getController(avator));
        //主播name
        String name = anchor.getUser_nickname();
        tvNick.setText(name);

        //是否已关注
        String isFollows = anchor.getIs_follow();
        if (TextUtils.isEmpty(isFollows) || "N".equals(isFollows)) {//未关注
            isFollow = "N";
            tvStart.setText(context.getResources().getString(R.string.live_follow_anchor));
        } else {
            isFollow = "Y";
            tvStart.setText(context.getResources().getString(R.string.live_search_on_follow));
        }
        //性别
        String sex = anchor.getUser_sex();
        if ("1".equals(sex)) {//男
            ivSex.setImageResource(R.mipmap.man);
        } else {
            ivSex.setImageResource(R.mipmap.girl);
        }
        //用户等级
        String userLvel = anchor.getUser_level();
        HnLiveLevelUtil.setAudienceLevBg(tvUserLeaveLevel, userLvel, true);
        //主播等级
        String liveLevel = anchor.getAnchor_level();
        tvLiveLevel.setText(liveLevel);
        tvLiveLevel.setVisibility(View.VISIBLE);
        return isFollow;
    }
}
