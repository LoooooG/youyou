package com.hotniao.svideo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hn.library.base.BaseFragment;

import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：fragment+viewpager  适配器
 * 创建人：Administrator
 * 创建时间：2017/9/11 10:19
 * 修改人：Administrator
 * 修改时间：2017/9/11 10:19
 * 修改备注：
 * Version:  1.0.0
 */
public class MyTabPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment>  mFragments;
    private List<String>  mTitles;

    public MyTabPagerAdapter(FragmentManager fm, List<Fragment>  mFragments, List<String>  mTitles) {
        super(fm);
        this.mFragments=mFragments;
        this.mTitles=mTitles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null|| mTitles.size() > 0)
            return mTitles.get(position);

        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragments.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }
}

