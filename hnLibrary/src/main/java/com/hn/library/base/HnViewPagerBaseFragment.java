package com.hn.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;

import butterknife.ButterKnife;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：底部Tab栏第二个对应的Fragment
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public abstract class HnViewPagerBaseFragment extends Fragment  {

    public FragmentActivity mActivity;

    /**视图控件是否初始化完成*/
    protected boolean isViewInitiated;

    /**Fragment是否可见*/
    protected boolean isVisibleToUser;

    /**数据是否初始化完成*/
    public boolean isDataInitiated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mActivity,getContentViewId(), null);
        ButterKnife.bind(this, view);
        initView(inflater,container,savedInstanceState);
        return view;
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

    /**
     * lazy initialize data
     */
    public abstract void fetchData();

    /**
     * 当Fragment可见的时候所执行的方法
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    /**
     * 改变视图加载状态  需要手动触发
     * @param state
     * @param mHnLoadingLayout
     */
    public   void setLoadViewState(int state, HnLoadingLayout mHnLoadingLayout) {
        if(mHnLoadingLayout==null)  return;
        if (state!=mHnLoadingLayout.getStatus()) {
            mHnLoadingLayout.setStatus(state);
        }
    }
    /**
     * 关闭刷新 需要手动触发
     */
    public  void closeRefresh(PtrClassicFrameLayout mRefreshView) {
        if (mRefreshView != null) {
            mRefreshView.refreshComplete();
        }
    }
    /**
     * prepare initialize data
     */
    private boolean prepareFetchData() {

        if (isVisibleToUser && isViewInitiated && (!isDataInitiated )) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
    /**
     * 根据传入的类(class)打开指定的activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        Intent intent = new Intent();
        intent.setClass(mActivity, pClass);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
