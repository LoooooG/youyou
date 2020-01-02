package com.hotniao.livelibrary.ui.audience.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.EventBusBean;
import com.hn.library.ultraviewpager.UltraViewPager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.HnLiveListModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.ui.audience.fragment.HnAudienceRoomFragment;
import com.hotniao.livelibrary.widget.viewpager.HnVerticalScrollViewPager;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用户直播间 管理中转站
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
//@Route(path = "/live/HnAudienceActivity") // 必须标明注解
public class HnAudienceActivity extends BaseActivity {


    /**
     * 遮罩层
     */
    private HnVerticalScrollViewPager mVerticalViewPager;
    /**
     * 直播层
     */
    private FrameLayout mRoomContainer;
    private FrameLayout mFragmentContainer;
    private FragmentManager mFragmentManager;


    /**
     * 用户进入直播间的房间相关信息
     */
    private HnLiveListModel result;
    private List<HnLiveListModel.LiveListBean> list = new ArrayList<>();
    private PagerAdapter mPagerAdapter;

    /**
     * 直播房间
     */
    private HnAudienceRoomFragment mRoomFragment;
    /**
     * 房间fragment是否已经初始化
     */
    private boolean mInit = false;
    /**
     * 当前的位置
     */
    private int pos;
    private int mRoomUid = -1;
    private int mCurrentItem = 0;
    private FrameLayout mRootContent;


    /**
     * 正在看直播中
     */
    public static boolean mIsLiveing = false;

    @Override
    public int getContentViewId() {
        return R.layout.live_activity_audience_layout1;
    }



    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        mVerticalViewPager = (HnVerticalScrollViewPager) findViewById(R.id.view_hnVerticalScrollViewPager);
        mVerticalViewPager.setActivity(this);
        mRootContent = (FrameLayout) findViewById(R.id.root_content);


        mFragmentManager = getSupportFragmentManager();
        mRoomContainer = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.live_view_audience_room_layout, null);
        mFragmentContainer = (FrameLayout) mRoomContainer.findViewById(R.id.fragment_container);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsLiveing = true;
    }

    @Override
    public void getInitData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            result = bundle.getParcelable("data");
            list = result.getList();
            pos = result.getPos();
            if (list != null && pos < list.size()) {
                //初始化viewpager
                initViewPager();
            }
        }

    }

    /*
     * 初始化viewpager
     */
    private void initViewPager() {

        mVerticalViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrentItem = position;
            }

        });

        mVerticalViewPager.setScrollMode(UltraViewPager.ScrollMode.VERTICAL);
        mPagerAdapter = new PagerAdapter(list);
        mVerticalViewPager.setAdapter(mPagerAdapter);
        if (list.size() > 1) {
            int item = (Integer.MAX_VALUE / 2) % list.size();
            if (item == pos) {
                mVerticalViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
            } else {
                mVerticalViewPager.setCurrentItem((Integer.MAX_VALUE / 2) - item + pos);
            }
        } else {
            mVerticalViewPager.setCurrentItem(0);
        }
        mVerticalViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
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
                HnLogUtils.i(TAG, "page.id == " + /*page.getId()*/1 + ", position == " + position);

                if ((position < 0 && viewGroup.getId() != mCurrentItem)) {
                    View roomContainer = viewGroup.findViewById(R.id.room_container);
                    if (roomContainer != null && roomContainer.getParent() != null && roomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (roomContainer.getParent())).removeView(roomContainer);
                    }
                }
                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == mCurrentItem && position == 0 && mCurrentItem != mRoomUid) {
                    if (mRoomContainer.getParent() != null && mRoomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (mRoomContainer.getParent())).removeView(mRoomContainer);
                    }
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Close_Dialog, 0));
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Hide_Mask, 0));
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
        pos = mCurrentItem % list.size();
        HnLogUtils.i(TAG, "当前加载的位置:" + pos + "--->" + mCurrentItem);
        HnLiveListModel.LiveListBean bean = list.get(pos);
        //聊天室的fragment只加载一次，以后复用
        if (!mInit) {
            mRoomFragment = HnAudienceRoomFragment.newInstance(bean);
            mFragmentManager.beginTransaction().replace(R.id.fragment_container, mRoomFragment).commitAllowingStateLoss();
            mInit = true;
        } else {
            if (mRoomFragment == null) {
                mRoomFragment = HnAudienceRoomFragment.newInstance(bean);
                mFragmentManager.beginTransaction().replace(R.id.fragment_container, mRoomFragment).commitAllowingStateLoss();
                mInit = true;
            }

            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Update_Room_Info, bean));
        }
        viewGroup.addView(mRoomContainer);
        this.mRoomUid = mCurrentItem;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Live_Back, null));

            return true;
        }
        return false;
    }


    class PagerAdapter extends android.support.v4.view.PagerAdapter {


        private List<HnLiveListModel.LiveListBean> list;

        public PagerAdapter(List<HnLiveListModel.LiveListBean> list) {
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
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.live_activity_audience_mask_layout, null);
            int pos = position % list.size();
            HnLiveListModel.LiveListBean data = list.get(pos);
            //遮罩层
            FrescoImageView mFrescoImageView = (FrescoImageView) view.findViewById(R.id.fiv_mask);
            mFrescoImageView.setVisibility(View.VISIBLE);
            if (data != null) {
                String avator = data.getAvator();
                mFrescoImageView.setController(FrescoConfig.getController(avator));
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
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsLiveing = false;
    }
}
