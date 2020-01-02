package com.hotniao.livelibrary.widget.viewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;

import com.hn.library.base.EventBusBean;
import com.hn.library.ultraviewpager.UltraViewPager;
import com.hn.library.utils.HnLogUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：动态设置是否可以上下滑动    该类采用两种方案：1：传递Actviity自动判断是否有键盘弹起，若键盘弹起，则不进行滑动
 * 2：自己动态设置isCanScroll来进行动态控制是否可以滑动，不可传递Activity
 * 创建人：mj
 * 创建时间：2017/10/16 15:22
 * 修改人：Administrator
 * 修改时间：2017/10/16 15:22
 * 修改备注：
 * Version:  1.0.0
 */
public class HnVerticalScrollViewPager extends UltraViewPager {


    /**
     * 是否可以滑动
     */
    private boolean isCanScroll = true;
    /**
     * 上下文
     */
    private Activity mActivity;
    private Context mContext;
    /**
     * 监听器
     */
    private HnVerticalScrollListener listener;

    public HnVerticalScrollViewPager(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public HnVerticalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    //    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
////        if (mActivity != null) {
////            if (!isSoftShowing()) {
////                //允许滑动则应该调用父类的方法
////                return super.onTouchEvent(ev);
////            } else {
////                //禁止滑动则不做任何操作，直接返回true即可
////                return true;
////            }
////        } else {
////            if (isCanScroll) {
////                //允许滑动则应该调用父类的方法
////                return super.onTouchEvent(ev);
////            } else {
////                //禁止滑动则不做任何操作，直接返回true即可
////                return true;
////            }
////        }
//        HnLogUtils.e("processEvent--99999999999999999");
//        return super.onTouchEvent(ev);
//    }
//
//
//
    float mInitX = 0;
    float mInitY = 0;
    boolean isMoving = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        VelocityTracker mVelTracker = VelocityTracker.obtain();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录初始位置
                mInitX = event.getRawX();
                mInitY = event.getRawY();
                mVelTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - mInitX;
                float dy = event.getRawY() - mInitY;
                //根据初始位置计算移动方向与距离，判断是否为滑动手势
                if (Math.abs(dx) > mTouchSlop && Math.abs(dx) > (Math.abs(dy) * 2)) {
                    isMoving = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int distance = (int) (event.getRawX() - mInitX);
                mVelTracker.addMovement(event);
                mVelTracker.computeCurrentVelocity(100);
//                获取x方向上的速度
                float velocityX = mVelTracker.getXVelocity(-1);
                if (isMoving) {
                    //假如为滑动手势，启动相应动画（右滑隐藏 左滑出现）
                    if (distance >= getResources().getDisplayMetrics().widthPixels / 5 || velocityX > 1000f) {
                        EventBus.getDefault().post(new EventBusBean(0, "onTouch", true));
                    } else if (distance < 0 - getResources().getDisplayMetrics().widthPixels / 5) {
                        EventBus.getDefault().post(new EventBusBean(0, "onTouch", false));
                    } else {
                        EventBus.getDefault().post(new EventBusBean(0, "onTouch", false));
                    }
                    isMoving = false;
                } else {
                    EventBus.getDefault().post(new EventBusBean(0, "onTouch", false));
                }
                mVelTracker.clear();
                mVelTracker.recycle();
                mInitX = 0;
                mInitY = 0;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    //设置是否允许滑动，true是可以滑动，false是禁止滑动
    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }


    public interface HnVerticalScrollListener {
        void IsCanScroll(boolean isCanScroll);
    }

    public void setVerticalScrollListener(HnVerticalScrollListener listener) {
        this.listener = listener;
    }


    /**
     * 判断键盘弹起
     *
     * @return
     */
    private boolean isSoftShowing() {
        if (mActivity == null) return false;
        //获取当前屏幕内容的高度
        int screenHeight = mActivity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);


        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        return screenHeight - /*metrics.heightPixels*/rect.bottom > 150;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }


}
