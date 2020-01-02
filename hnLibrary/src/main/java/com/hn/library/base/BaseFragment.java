package com.hn.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrFrameLayout;

import butterknife.ButterKnife;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 基类Fragment,所对应的Fragment适合手动管理
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();
    public BaseActivity mActivity;
    protected View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = View.inflate(mActivity, getContentViewId(), null);
        ButterKnife.bind(this, mRootView);
        initView(inflater, container, savedInstanceState);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    /**
     * this method must be implement by child activity, instead of setContentView in child activity
     */
    public abstract int getContentViewId();

    /**
     * 子类实现此抽象方法返回view进行展示
     *
     * @param inflater
     * @return
     */
    public abstract void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initData();

    /**
     * 事件处理
     */
    protected void initEvent() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
