package com.hotniao.live.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.HnDimenUtil;
import com.hotniao.live.R;
import com.hotniao.live.utils.AnimUtil;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.widget.imagepager.CircleIndicator;
import com.hotniao.live.widget.imagepager.PinchImageView;
import com.hotniao.live.widget.imagepager.PinchImageViewPager;
import com.hotniao.livelibrary.widget.CirclePageIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class HnPhotoPagerActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    PinchImageViewPager mViewPager;
    @BindView(R.id.circle_indicator_view)
    CircleIndicator mCircleIndicator;


    public static final String KEY_START_POS = "start_pos";
    public static final String KEY_START_X = "start_x";
    public static final String KEY_START_Y = "start_y";
    public static final String KEY_START_W = "start_w";
    public static final String KEY_START_H = "start_h";
    public static final String KEY_PHOTO_LIST = "photo_list";

    protected ArrayList<String> mPhotoList;

    protected long ANIM_TIME = 300;
    protected ValueAnimator mValueAnimator;

    //开始时的位置(ViewPager的位置)
    protected int mStartPosition;
    //动画开始 坐标x,y 和宽高
    protected int mStartX, mStartY, mStartW, mStartH;
    private boolean isToFinish = false;


    @Override
    public int getContentViewId() {
        return R.layout.activity_photo_pager;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        final Bundle extras = getIntent().getExtras();
        mStartPosition = extras.getInt(KEY_START_POS);
        mPhotoList = extras.getStringArrayList(KEY_PHOTO_LIST);
        mStartX = extras.getInt(KEY_START_X);
        mStartY = extras.getInt(KEY_START_Y);
        mStartW = extras.getInt(KEY_START_W);
        mStartH = extras.getInt(KEY_START_H);
    }

    @Override
    public void getInitData() {
        startAnimation();
        ImageAdapter imageAdapter = new ImageAdapter();
        mViewPager.setAdapter(imageAdapter);
        mCircleIndicator.setViewPager(mViewPager);

        mViewPager.setCurrentItem(mStartPosition);

        mViewPager.addOnPageChangeListener(new PinchImageViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                PinchImageViewPager.ItemInfo curItemInfo = mViewPager.getCurItemInfo();
                if (curItemInfo != null) {
                    ViewGroup viewGroup = (ViewGroup) curItemInfo.object;
                    mViewPager.setMainPinchImageView((PinchImageView) viewGroup.getChildAt(0));
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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

    private void startAnimation() {
        mCircleIndicator.setVisibility(View.GONE);
        mValueAnimator = AnimUtil.startArgb(mRlParent, Color.TRANSPARENT, Color.BLACK, ANIM_TIME);
        //        final ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        //        layoutParams.width = mStartW;
        //        layoutParams.height = mStartH;
        //        mViewPager.setLayoutParams(layoutParams);
        final int screenWidth = HnDimenUtil.getScreenWidth(this);
        final int screenHeight = HnDimenUtil.getScreenHeight(this);
        mViewPager.setX(mStartX + mStartW / 2 - screenWidth / 2);
        mViewPager.setY(mStartY + mStartH / 2 - screenHeight / 2);
        mViewPager.setScaleX((mStartW + 0f) / screenWidth);
        mViewPager.setScaleY((mStartH + 0f) / screenHeight);
        mViewPager.animate().x(0).y(0).scaleX(1).scaleY(1).setInterpolator(new DecelerateInterpolator()).setDuration(ANIM_TIME).start();
        mViewPager.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mPhotoList.size() > 1) {
                    mCircleIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animToFinish() {
        if (isToFinish) {
            return;
        }
        isToFinish = true;
        mCircleIndicator.setVisibility(View.GONE);
        AnimUtil.startArgb(mRlParent, Color.BLACK, Color.TRANSPARENT, ANIM_TIME);
        ViewCompat.animate(mViewPager).alpha(0).scaleX(0.2f).scaleY(0.2f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(ANIM_TIME)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Window window = getWindow();
                        WindowManager.LayoutParams attributes = window.getAttributes();
                        attributes.alpha = 0;
                        window.setAttributes(attributes);
                        isToFinish = false;
                        finish();
                    }
                }).start();
    }

    @Override
    public void onBackPressed() {
//        animToFinish();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mValueAnimator != null)
            mValueAnimator.cancel();
        if (mViewPager != null)
            mViewPager.animate().cancel();
    }

    class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPhotoList == null ? 0 : mPhotoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            RelativeLayout layout = new RelativeLayout(HnPhotoPagerActivity.this);
            PinchImageView imageView = new PinchImageView(HnPhotoPagerActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            final ProgressBar bar = new ProgressBar(HnPhotoPagerActivity.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(160, 160);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            bar.setLayoutParams(params);
            bar.setIndeterminateDrawable(HnUiUtils.getResources().getDrawable(R.drawable.loading));
            bar.setIndeterminate(true);

            String url = mPhotoList.get(position);

            Glide.with(HnPhotoPagerActivity.this.getApplicationContext())
                    .load(HnUrl.setImageUrl(url))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            bar.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .placeholder(R.drawable.default_live)
                    .into(imageView);

            layout.addView(imageView, new RelativeLayout.LayoutParams(-1, -1));
            layout.addView(bar);
            container.addView(layout, new ViewGroup.LayoutParams(-1, -1));


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    animToFinish();
                    finish();
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
