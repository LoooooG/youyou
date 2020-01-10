package com.hotniao.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.hn.library.base.BaseActivity;
import com.hn.library.ultraviewpager.UltraViewPager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.fragment.video.HnVideoDetailQnFragment;
import com.hotniao.video.model.HnVideoRoomSwitchModel;
import com.hotniao.livelibrary.widget.viewpager.HnVerticalScrollViewPager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoDetailActivity extends BaseActivity {
    @BindView(R.id.mViewPager)
    HnVerticalScrollViewPager mViewPager;

    private PagerAdapter mPagerAdapter;

    /**
     * 视频层
     */
    private FrameLayout mRoomContainer;
    private FragmentManager mFragmentManager;

    private HnVideoDetailQnFragment mRoomFragment;
    /**
     * 当前的位置
     */
    private int pos;
    private int mRoomUid = -1;
    private int mCurrentItem = 0;

    private List<HnVideoRoomSwitchModel.DBean> mList = new ArrayList<>();

    /**
     * 房间fragment是否已经初始化
     */
    private boolean mInit = false;

    public static void luncher(Activity activity, Bundle bundle) {
        activity.startActivity(new Intent(activity, HnVideoDetailActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(bundle));
    }

    @Override
    public int getContentViewId() {
        if (!HnUiUtils.checkDeviceHasNavigationBar()) {
            Window window = getWindow();
            int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setFlags(flag, flag);
        }else {
            if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        return R.layout.activity_video_detail;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);

        pos = getIntent().getExtras().getInt("pos", 0);
        mList.addAll((Collection<? extends HnVideoRoomSwitchModel.DBean>) getIntent().getExtras().getSerializable("data"));

        mFragmentManager = getSupportFragmentManager();
        mRoomContainer = (FrameLayout) LayoutInflater.from(this).inflate(com.hotniao.livelibrary.R.layout.live_view_audience_room_layout, null);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrentItem = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setScrollMode(UltraViewPager.ScrollMode.VERTICAL);
        mPagerAdapter = new PagerAdapter(mList);
        mViewPager.setAdapter(mPagerAdapter);
        if (mList.size() > 1) {
            int item = (Integer.MAX_VALUE / 2) % mList.size();
            if (item == pos) {
                mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
            } else {
                mViewPager.setCurrentItem((Integer.MAX_VALUE / 2) - item + pos);
            }
        } else {
            mViewPager.setCurrentItem(0);
        }
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            private float yPosition;


            public float getPosition() {
                return yPosition;
            }

            @Override
            public void transformPage(View page, float position) {

                page.setTranslationX(page.getWidth() * -position);
                yPosition = position * page.getHeight();
                page.setTranslationY(yPosition);

                ViewGroup viewGroup = (ViewGroup) page;
                HnLogUtils.i(TAG, "page.id == " + page.getId() + ", position == " + position);

                if ((position < 0 && viewGroup.getId() != mCurrentItem)) {
                    View roomContainer = viewGroup.findViewById(com.hotniao.livelibrary.R.id.room_container);
                    if (roomContainer != null && roomContainer.getParent() != null && roomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (roomContainer.getParent())).removeView(roomContainer);
                    }
                }
                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == mCurrentItem && position == 0 && mCurrentItem != mRoomUid) {
                    if (mRoomContainer.getParent() != null && mRoomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (mRoomContainer.getParent())).removeView(mRoomContainer);
                    }
                    loadVideoAndChatRoom(viewGroup, mCurrentItem);
                }
            }
        });
    }

    /**
     * 加载房间信息
     *
     * @param viewGroup
     * @param mCurrentItem
     */
    private void loadVideoAndChatRoom(ViewGroup viewGroup, int mCurrentItem) {
        pos = mCurrentItem % mList.size();
        HnVideoRoomSwitchModel.DBean bean = mList.get(pos);
        //聊天室的fragment只加载一次，以后复用
        if (!mInit) {
            mRoomFragment = HnVideoDetailQnFragment.newInstance(bean);
            mFragmentManager.beginTransaction().replace(com.hotniao.livelibrary.R.id.fragment_container, mRoomFragment).commitAllowingStateLoss();
            mInit = true;
        } else {
            if (mRoomFragment == null) {
                mRoomFragment = HnVideoDetailQnFragment.newInstance(bean);
                mFragmentManager.beginTransaction().replace(com.hotniao.livelibrary.R.id.fragment_container, mRoomFragment).commitAllowingStateLoss();
                mInit = true;
            }

            mRoomFragment.switchVideo(bean);
        }
        viewGroup.addView(mRoomContainer);
        this.mRoomUid = mCurrentItem;
    }


    @Override
    public void getInitData() {

    }

    class PagerAdapter extends android.support.v4.view.PagerAdapter {


        private List<HnVideoRoomSwitchModel.DBean> list;

        public PagerAdapter(List<HnVideoRoomSwitchModel.DBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list.size() > 1) {
                return Integer.MAX_VALUE;
            } else {
                return 1;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(com.hotniao.livelibrary.R.layout.live_activity_audience_mask_layout, null);
            //遮罩层
            FrescoImageView mFrescoImageView = (FrescoImageView) view.findViewById(com.hotniao.livelibrary.R.id.fiv_mask);
            mFrescoImageView.setVisibility(View.VISIBLE);
            if (list.get( position % list.size()) != null) {
                mFrescoImageView.setController(FrescoConfig.getController(list.get( position % list.size()).getCover()));
            }
            view.setId(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //将数据返给SDK处理
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (HnUiUtils.checkDeviceHasNavigationBar()) {
            if (hasFocus && Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
//        if (checkDeviceHasNavigationBar()) solveNavigationBar(getWindow());
    }


}
