package com.hotniao.live.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.fragment.HnPlatformListFragment;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：粉丝排行榜
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnFansContributeListActivity")
public class HnFansContributeListActivity extends BaseActivity {
    @BindView(R.id.mRg)
    RadioGroup mRg;
    @BindView(R.id.mVp)
    ViewPager mVp;

    @Override
    public int getContentViewId() {
        return R.layout.activity_fans_contribute_list;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.fans_con_list);
        setShowBack(true);


        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position=0;
                switch (checkedId){
                    case R.id.mRbDay:
                        position=0;
                        break;
                    case R.id.mRbWeek:
                        position=1;
                        break;
                    case R.id.mRbTotal:
                        position=2;
                        break;
                }
                mVp.setCurrentItem(position);
            }
        });

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton childAt = (RadioButton) mRg.getChildAt(position);
                childAt.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVp.setOffscreenPageLimit(3);
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = HnPlatformListFragment.newInstance();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isAnchor", false);
                bundle.putString("url", HnUrl.LIVE_RANK_ANCHORFANS);//接口
                bundle.putString("type",HnPlatformListFragment.FansContribute);//排行榜类型：粉丝贡献榜，平台榜
                bundle.putString("anchorId",getIntent().getStringExtra("userId"));//主播id
                bundle.putInt("fansType",position);//贡献榜类型
                fragment.setArguments(bundle);
                return fragment ;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    @Override
    public void getInitData() {

    }


}
