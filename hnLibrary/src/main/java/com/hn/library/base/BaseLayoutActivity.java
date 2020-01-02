package com.hn.library.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hn.library.R;
import com.hn.library.daynight.DayNightHelper;
import com.hn.library.utils.HnDimenUtil;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 基类Activity 主要封装公共标题栏，和容器布局
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class BaseLayoutActivity extends AppCompatActivity {

    protected AppCompatImageButton mBack;
    private AppCompatTextView    mTitle;
    protected   AppCompatTextView    mSubtitle;
    private FrameLayout          mContentLayout;
    protected RelativeLayout mRlParent;
    private Toolbar              mToolbar;
    private boolean              isTitleBarShow;

    protected DayNightHelper mDayNightHelper;
    public boolean mIsDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDayNightHelper = new DayNightHelper();
        mIsDay = mDayNightHelper.isDay();
        setTheme(mIsDay?R.style.DayTheme:R.style.NightTheme);

        super.setContentView(R.layout.activity_base_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRlParent = (RelativeLayout) findViewById(R.id.mRlParent);
        mBack = (AppCompatImageButton) findViewById(R.id.toolbar_back);
        mTitle = (AppCompatTextView) findViewById(R.id.toolbar_title);
        mContentLayout = (FrameLayout) findViewById(R.id.layout_content);
        mSubtitle = (AppCompatTextView) findViewById(R.id.toolbar_subtitle);

        //默认显示标题栏
        setShowTitleBar(true);
        //默认不显示返回按钮
        setShowBack(false);
        //默认不显示子标题
        setShowSubTitle(false);
    }



    /**
     * 设置标题栏显示或者隐藏
     *
     * @param isShow true则显示,false为不显示
     */
    public boolean setShowTitleBar(boolean isShow) {
        if (mToolbar != null) {
            mToolbar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        isTitleBarShow = isShow;
        return isTitleBarShow;
    }

    /**
     * 是否显示返回按钮
     *
     * @param isShow true则显示,false为不显示
     */
    public void setShowBack(boolean isShow) {
        if (!isTitleBarShow)
            return;
        if (mToolbar != null) {
            mBack.setVisibility(isShow ? View.VISIBLE : View.GONE);
            mBack.setOnClickListener(isShow ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            } : null);

            if (isShow) {
                mTitle.setPadding(0, 0, 0, 0);
            } else {
                mTitle.setPadding(HnDimenUtil.dp2px(this, 16), 0, 0, 0);
            }
        }

    }

    /**
     * 是否显示子标题
     *
     * @param isShow true则显示,false为不显示
     */
    public void setShowSubTitle(boolean isShow) {
        if (!isTitleBarShow)
            return;
        if (mSubtitle != null) {
            mSubtitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置标题内容
     *
     * @param titleId
     */
    public void setTitle(@StringRes int titleId) {
        if (!isTitleBarShow)
            return;
        mTitle.setText(titleId);
    }

    /**
     * 设置标题内容
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        if (!isTitleBarShow)
            return;
        mTitle.setText(title);
    }

    public void setToolbarWhite() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.white));
        mBack.setImageResource(R.drawable.ic_back_black);
        mTitle.setTextColor(getResources().getColor(R.color.black));
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTitle.setLayoutParams(lp);

    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }

}  