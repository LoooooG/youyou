package com.hotniao.livelibrary.ui.anchor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnStartLiveInfoModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.anchor.fragment.HnAnchorInfoFragment;
import com.hotniao.livelibrary.ui.anchor.fragment.HnAnchorLiveFragment;
import com.hotniao.livelibrary.ui.audience.fragment.HnTopEmptyFragment;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

//import com.hotniao.livelibrary.model.bean.HnStartLiveBean;
;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：主播直播间
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
//@Route(path = "/live/HnAnchorActivity") // 必须标明注解
public class HnAnchorActivity extends BaseActivity {

    private ViewPager mViewPager;
    /** 视频层*/
    private HnAnchorLiveFragment mLiveFragment;
    /** 交互层*/
    private HnAnchorInfoFragment mInfoFragment;

    /**直播之前服务器返回的相关数据*/
    private HnStartLiveInfoModel.DBean  result;
    /**直播类型标识符   0 免费 1 计时收费  2 门票收费 3 vip*/
    private int  live_type=0;

    /**
     * 正在直播中
     */
    public static  boolean mIsLiveing=false;

    @Override
    public int getContentViewId() {
        return R.layout.live_activity_anchor_layout;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
         setShowTitleBar(false);
        mViewPager= (ViewPager) findViewById(R.id.mViewPager);
    }

    @Override
    public void getInitData() {
        Bundle  buundle=getIntent().getExtras();
        if(buundle!=null){
            result= (HnStartLiveInfoModel.DBean) buundle.getSerializable("data");
            live_type=buundle.getInt("live_type");
            if(result!=null) {
                initFargment();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsLiveing=true;
    }

    /**
     * 初始化fragment
     * HnAnchorLiveFragment 视频fragment 在底层
     * HnAnchorInfoFragment 直播间信息 ，在上层
     */
    private void initFargment() {

        mLiveFragment = HnAnchorLiveFragment.newInstance(result.getPush_url(),result.getUser_avatar());
        mInfoFragment = HnAnchorInfoFragment.newInstance(result,live_type);

       getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.live_frame, mLiveFragment)
                .commitAllowingStateLoss();

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if (position == 0) {
                    return new HnTopEmptyFragment();

                } else if (position == 1) {

                    return mInfoFragment;

                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mViewPager.setCurrentItem(1);



//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.info_frame, mInfoFragment)
//                .commitAllowingStateLoss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Live_Back,null));
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HnLogUtils.i(TAG,"结束直播界面");
        mIsLiveing=false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
