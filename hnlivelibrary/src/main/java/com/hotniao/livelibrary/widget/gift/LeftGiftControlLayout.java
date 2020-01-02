package com.hotniao.livelibrary.widget.gift;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.hotniao.livelibrary.R;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：礼物动画显示布局
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class LeftGiftControlLayout extends LinearLayout implements LeftGiftsItemLayout.LeftGiftAnimationStatusListener {

    /**
     * 礼物1
     */
    private LeftGiftsItemLayout mFirstItemGift;

    /**
     * 礼物2
     */
    private LeftGiftsItemLayout mSecondItemGift;

    /**
     * 礼物队列(在多个线程中使用此List)
     */
    private CopyOnWriteArrayList<GiftModel> mGiftQueue;

    /**
     * 礼物开始动画
     */
    private Animation mGiftEnterAnimation1;

    private Animation mGiftEnterAnimation2;

    /**
     * 礼物结束动画
     */
    private Animation mGiftExitAnimation1;

    private Animation mGiftExitAnimation2;


    public LeftGiftControlLayout(Context context) {
        super(context);
        init(context);
    }

    public LeftGiftControlLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            getGiftsLayout();
        }
        super.onLayout(changed, l, t, r, b);
    }

    private void init(Context context) {
        mGiftQueue = new CopyOnWriteArrayList<>();
        mGiftEnterAnimation1 = AnimationUtils.loadAnimation(context, R.anim.live_drop_from_left);
        mGiftEnterAnimation1.setFillAfter(true);
        mGiftEnterAnimation2 = AnimationUtils.loadAnimation(context, R.anim.live_drop_from_left);
        mGiftEnterAnimation2.setFillAfter(true);

        mGiftExitAnimation1 = AnimationUtils.loadAnimation(context, R.anim.live_hide_to_top);
        mGiftExitAnimation1.setFillAfter(true);
        mGiftExitAnimation2 = AnimationUtils.loadAnimation(context, R.anim.live_hide_to_top);
        mGiftExitAnimation2.setFillAfter(true);

        mGiftExitAnimation1.setAnimationListener(new GiftExitAnimationListener());
        mGiftExitAnimation2.setAnimationListener(new GiftExitAnimationListener());
    }

    private void getGiftsLayout() {
        if (getChildCount() != 0) {
            mFirstItemGift = (LeftGiftsItemLayout) findViewById(R.id.gift1);
            mSecondItemGift = (LeftGiftsItemLayout) findViewById(R.id.gift2);

            mSecondItemGift.setGiftAnimationListener(this);
            mFirstItemGift.setGiftAnimationListener(this);

            if (mFirstItemGift != null) {
                mFirstItemGift.getIndex();
            }

            if (mSecondItemGift != null) {
                mSecondItemGift.getIndex();
            }
        }
    }

    @Override
    public void dismiss(int index) {
        if (index == 0) {
            mFirstItemGift.startAnimation(mGiftExitAnimation1);
        } else if (index == 1) {
            mSecondItemGift.startAnimation(mGiftExitAnimation2);
        }
    }

    /**
     * 礼物退出动画监听
     */
    private class GiftExitAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            if (mSecondItemGift.getCurrentShowStatus() == LeftGiftsItemLayout.SHOWEND) {
                mSecondItemGift.setVisibility(View.INVISIBLE);
                mSecondItemGift.setCurrentShowStatus(LeftGiftsItemLayout.WAITING);
            }

            if (mFirstItemGift.getCurrentShowStatus() == LeftGiftsItemLayout.SHOWEND) {
                mFirstItemGift.setVisibility(View.INVISIBLE);
                mFirstItemGift.setCurrentShowStatus(LeftGiftsItemLayout.WAITING);
            }

            showGift();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * 显示礼物
     */
    public synchronized void showGift() {

        if (isEmpty()) {
            return;
        }

        if (mSecondItemGift.getCurrentShowStatus() == LeftGiftsItemLayout.WAITING) {
            mSecondItemGift.setData(getGift());
            mSecondItemGift.setVisibility(View.VISIBLE);
            mSecondItemGift.startAnimation(mGiftEnterAnimation1);
            mSecondItemGift.startGiftAnimation();
        } else if (mFirstItemGift.getCurrentShowStatus() == LeftGiftsItemLayout.WAITING) {
            mFirstItemGift.setData(getGift());
            mFirstItemGift.setVisibility(View.VISIBLE);
            mFirstItemGift.startAnimation(mGiftEnterAnimation2);
            mFirstItemGift.startGiftAnimation();
        }
    }

    /**
     * 加入礼物
     *
     * @param data
     */
    public synchronized void loadGift(GiftModel data) {

        if (mGiftQueue != null) {

            if (mSecondItemGift != null) {
                if (mSecondItemGift.getCurrentShowStatus() == LeftGiftsItemLayout.SHOWING) {
                    if (mSecondItemGift.getCurrentGiftId().equals(data.getGiftId()) && mSecondItemGift.getCurrentSendUserId().equals(data.getSendUserId())) {
                        mSecondItemGift.setGiftCount(data.getGiftCuont());
                        return;
                    }
                }
            }

            if (mFirstItemGift != null) {
                if (mFirstItemGift.getCurrentShowStatus() == LeftGiftsItemLayout.SHOWING) {
                    if (mFirstItemGift.getCurrentGiftId().equals(data.getGiftId()) && mFirstItemGift.getCurrentSendUserId().equals(data.getSendUserId())) {
                        mFirstItemGift.setGiftCount(data.getGiftCuont());
                        return;
                    }
                }
            }


            addGiftQueue(data);
        }
    }


    private synchronized void addGiftQueue(GiftModel data) {

        if (mGiftQueue != null) {

            if (mGiftQueue.size() == 0) {
                mGiftQueue.add(data);
                showGift();
                return;
            }

            boolean isHave = false;
            int a = -1;
            for (GiftModel gift : mGiftQueue) {
                if (gift.getGiftId().equals(data.getGiftId()) &&
                        gift.getSendUserId().equals(data.getSendUserId())) {
                    isHave = true;
                    a = mGiftQueue.indexOf(gift);
                }
            }
            if (isHave) {
                mGiftQueue.get(a).setGiftCuont(mGiftQueue.get(a).getGiftCuont() + data.getGiftCuont());
            } else {
                mGiftQueue.add(data);
            }
            showGift();
        }
    }


    /**
     * 取出礼物
     */
    private synchronized GiftModel getGift() {
        GiftModel gift = null;
        if (mGiftQueue.size() != 0) {
            gift = mGiftQueue.get(0);
            mGiftQueue.remove(0);
        }
        return gift;
    }

    /**
     * 清除所有礼物
     */
    public synchronized void cleanAll() {
        if(mFirstItemGift!=null){
            mFirstItemGift.clearAnimation();
            mFirstItemGift.setVisibility(INVISIBLE);
            mFirstItemGift.setCurrentShowStatus(LeftGiftsItemLayout.WAITING);
        }
        if (mSecondItemGift != null) {
            mSecondItemGift.clearAnimation();
            mSecondItemGift.setVisibility(INVISIBLE);
            mSecondItemGift.setCurrentShowStatus(LeftGiftsItemLayout.WAITING);
        }
        if(mGiftQueue!=null){
            mGiftQueue.clear();
        }

//        mSecondItemGift.setCurrentShowStatus(LeftGiftsItemLayout.SHOWEND);
//        mFirstItemGift.setCurrentShowStatus(LeftGiftsItemLayout.SHOWEND);


    }




    /**
     * 礼物是否为空
     *
     * @return
     */
    public synchronized boolean isEmpty() {
        if (mGiftQueue == null || mGiftQueue.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
