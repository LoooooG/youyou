package com.hotniao.livelibrary.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：动态设置是否可以左右滑动
 * 创建人：mj
 * 创建时间：2017/10/16 15:22
 * 修改人：Administrator
 * 修改时间：2017/10/16 15:22
 * 修改备注：
 * Version:  1.0.0
 */
public class HnControlScrollViewPager extends ViewPager {


    /**是否可以滑动*/
    private boolean isCanScroll=true;

    private HnControlScrollListener listener;

    public HnControlScrollViewPager(Context context) {
        super(context);
    }


    public HnControlScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            //允许滑动则应该调用父类的方法


            return super.onTouchEvent(ev);
        } else {
            //禁止滑动则不做任何操作，直接返回true即可
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll)
            return super.onInterceptTouchEvent(arg0);
        else {
            return false;
        }
    }

    //设置是否允许滑动，true是可以滑动，false是禁止滑动
    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }


    public      interface    HnControlScrollListener{
        void   IsCanScroll(boolean isCanScroll);
    }

    public  void  setControlScrollListener(HnControlScrollListener listener){
          this.listener=listener;
    }

}
