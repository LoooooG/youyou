package com.hotniao.video.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.HnViewPagerBaseFragment;
import com.hn.library.tab.SlidingTabLayout;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnScrollViewPagerAdapter;
import com.hotniao.video.adapter.MyTabPagerAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.fragment.near.HnNearChatFragment;
import com.hotniao.video.fragment.near.HnNearLiveFragment;
import com.hotniao.video.utils.HnUiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：附近
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnNearActivity extends BaseActivity {
    @BindView(R.id.mTab)
    SlidingTabLayout mTab;
    @BindView(R.id.mVp)
    ViewPager mVp;


    private List<Fragment> mFragments = new ArrayList<>();
    private  List<String> mTitles=new ArrayList<>();
    @Override
    public int getContentViewId() {
        return R.layout.activity_near;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        mTitles.add(HnUiUtils.getString(R.string.main_near_chat));

        mFragments.add(new HnNearChatFragment());
        setShowTitleBar(false);
        mVp.setOffscreenPageLimit(mFragments.size());
        mVp.setAdapter(new MyTabPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        mTab.setViewPager(mVp);

    }


    @Override
    public void getInitData() {

    }


    @OnClick(R.id.mIbBack)
    public void onViewClicked() {
        finish();
    }
}
