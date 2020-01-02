package com.hotniao.livelibrary.widget.danmu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.global.NetConstant;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.util.EmojiUtil;
import com.hotniao.livelibrary.util.HnLiveScreentUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：弹幕操作
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class DanmakuChannel extends RelativeLayout {

    public boolean isRunning = false;
    public HnReceiveSocketBean mEntity;
    private DanmakuActionInter danAction;
    private String TAG = "DanmakuChannel";

    private Context context;
    private View mRootView;
    private List<View> views = new ArrayList<>();


    public DanmakuActionInter getDanAction() {
        return danAction;
    }

    public void setDanAction(DanmakuActionInter danAction) {
        this.danAction = danAction;
    }

    public DanmakuChannel(Context context) {
        this(context, null);

    }


    public DanmakuChannel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public DanmakuChannel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DanmakuChannel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mRootView = inflater.inflate(R.layout.live_danmaku_channel_layout, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setClipToOutline(false);
        }
    }


    public void setDanmakuEntity(HnReceiveSocketBean entity) {
        mEntity = entity;
    }

    public void mStartAnimation(final HnReceiveSocketBean entity) {
        isRunning = true;
        setDanmakuEntity(entity);

        if (mEntity != null) {

            final View view = View.inflate(getContext(), R.layout.live_item_live_danmu, null);
            TextView contentView = (TextView) view.findViewById(R.id.tv_content);
            TextView mTvContent2 = (TextView) view.findViewById(R.id.mTvContent2);
            TextView nickname = (TextView) view.findViewById(R.id.tv_nick);
            final FrescoImageView avatar = (FrescoImageView) view.findViewById(R.id.fiv_avatar);

            HnReceiveSocketBean.DataBean.UserBean fuser = entity.getData().getUser();
            try {
                String word = entity.getData().getChat().getContent();
                if ("Y".equals(entity.getData().getChat().getIs_barrage_effect())) {
                    EmojiUtil.danMuEmojiText(contentView, word, getContext(), true);
                    contentView.setVisibility(VISIBLE);
                    avatar.setVisibility(VISIBLE);
                    mTvContent2.setVisibility(GONE);
                } else {
                    EmojiUtil.danMuEmojiText(mTvContent2, word, getContext(), true);
                    contentView.setVisibility(GONE);
                    avatar.setVisibility(GONE);
                    mTvContent2.setVisibility(VISIBLE);
                }
            } catch (IOException e) {}

            String avator = fuser.getUser_avatar();
            if (!TextUtils.isEmpty(avator)) {
                avatar.setController(FrescoConfig.getController(avator));//头像
            }
            avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Click_Dan_Mu, entity));
                }
            });

            nickname.setText(fuser.getUser_nickname()); //昵称


            view.measure(-1, -1);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            final int leftMargin = HnLiveScreentUtils.getScreenW(getContext());
            //屏幕的宽度
            HnLogUtils.d(TAG, "leftMargin:" + leftMargin);
            //弹幕的宽度
            HnLogUtils.d(TAG, "measuredWidth:" + measuredWidth);

            ObjectAnimator anim = AnimationHelper.createTranslateAnim(view, leftMargin, -measuredWidth);
            anim.start();

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onAnimationEnd(Animator animation) {
                    //防止内存溢出
                    if (!((Activity) getContext()).isDestroyed()) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                view.clearAnimation();
                                DanmakuChannel.this.removeView(view);
                                if (danAction != null) {
                                    danAction.pollDanmu();
                                }
                            }
                        });
                    }
                    isRunning = false;
                }
                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            views.add(view);
            this.addView(view);
        }
    }


    /**
     *
     */
    public void clearChildView() {
        int count = this.getChildCount();
        for (int i = 0; i < views.size(); i++) {
            views.get(i).clearAnimation();
        }
        HnLogUtils.i(TAG, "清除子布局" + count);
        for (int i = 0; i < count; i++) {
            this.removeViewAt(i);
            HnLogUtils.i(TAG, "清除字布局");
        }

    }
}
