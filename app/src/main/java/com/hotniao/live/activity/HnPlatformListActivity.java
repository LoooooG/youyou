package com.hotniao.live.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.eventbus.HnCountdownEvent;
import com.hotniao.live.fragment.HnPlatformListFragment;
import com.hotniao.livelibrary.util.MyCountDownTimer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：平台排行榜
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnPlatformListActivity extends BaseActivity {
    @BindView(R.id.mRg)
    RadioGroup mRg;
    @BindView(R.id.mVp)
    ViewPager mVp;
    @BindView(R.id.mTvTime)
    TextView mTvTime;
    private MyCountDownTimer mCTimer;
    @Override
    public int getContentViewId() {
        return R.layout.activity_platform_list;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.platform_list);
        setShowBack(true);
        EventBus.getDefault().register(this);

        mVp.setOffscreenPageLimit(2);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position=0;
                switch (checkedId){
                    case R.id.mRbAnchor:
                        position=0;
                        break;
                    case R.id.mRbUser:
                        position=1;
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
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = HnPlatformListFragment.newInstance();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isAnchor", position==0);//是否主播榜
                bundle.putString("url",position==0?HnUrl.LIVE_RANK_ANCHOR:HnUrl.LIVE_RANK_USER);//请求接口
                bundle.putString("type",HnPlatformListFragment.PlatfromList);
                fragment.setArguments(bundle);
                return fragment ;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    @Override
    public void getInitData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTime(HnCountdownEvent event){
        if(event!=null){
            //倒计时
            String deadline = event.getTime();
            if(!TextUtils.isEmpty(deadline)){
                long nowTime = System.currentTimeMillis();
                long l = Long.parseLong(deadline)*1000 - nowTime;
                mCTimer = new MyCountDownTimer(l, 1000,mTvTime);
                mCTimer.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mCTimer!=null){
            mCTimer.cancel();
            mCTimer=null;
        }
    }
}
