package com.hotniao.livelibrary.widget.gift.bigGift;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：大礼物 -- 操作
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class BigGiftChannel extends RelativeLayout {

    private String TAG = "BigGiftChannel";
    private Context context;
    public boolean isRunning = false;
    public HnReceiveSocketBean.DataBean mEntity;
    private BigGiftActionInter danAction;
    private TextView mSendInfo;
    private SurfaceView mBigGiftView;

    private static boolean isShowBigGift = true;
    private SilkyAnimation silkyAnimation;
    private BigGiftPlayListener listener;

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public BigGiftActionInter getDanAction() {
        return danAction;
    }

    public void setDanAction(BigGiftActionInter danAction) {
        this.danAction = danAction;
    }

    public BigGiftChannel(Context context) {
        this(context, null);

    }


    public BigGiftChannel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public BigGiftChannel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BigGiftChannel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        final View view = View.inflate(getContext(), R.layout.live_layout_big_gift, null);
        mBigGiftView = (SurfaceView) view.findViewById(R.id.sv_anim);
        mSendInfo = (TextView) view.findViewById(R.id.send_info);

        silkyAnimation = new SilkyAnimation.Builder(mBigGiftView)
                .setCacheCount(2)
                .setScaleType(SilkyAnimation.SCALE_TYPE_CENTER)
                .setRepeatMode(SilkyAnimation.MODE_ONCE)
                .setAnimationListener(new SilkyAnimation.AnimationStateListener() {
                    @Override
                    public void onStart() {
                        Log.e("aa", "aa");
                        if (listener != null) listener.onPlayBegin();
                    }

                    @Override
                    public void onFinish() {
                        Log.e("aa", "aa");
                        if (listener != null) listener.onPlayFInish();

                    }
                })
                .build();
        this.addView(view);
    }


    public void setDanmakuEntity(HnReceiveSocketBean.DataBean entity) {
        mEntity = entity;
    }


    /**
     * 开始大动画
     *
     * @param entity
     * @param listener
     */
    public void startBigGiftAnimation2(final HnReceiveSocketBean.DataBean entity, BigGiftPlayListener listener) {
        isRunning = true;
        setDanmakuEntity(entity);
        if (this.listener == null) this.listener = listener;
        if (mEntity != null) {
            String userNick = entity.getUser().getUser_nickname();  //赠送者
            String giftName = entity.getLive_gift().getLive_gift_name();  //礼物名称


            if (entity == null || TextUtils.isEmpty(entity.getBigGiftAddress())) return;
            //从文件读取 file resources
            final File file = new File(entity.getBigGiftAddress());
            if (file == null||!file.exists()) {
                return;
            }

            if (mSendInfo.getVisibility() == View.GONE && isShowBigGift) {
                mSendInfo.setVisibility(View.VISIBLE);
                mSendInfo.setText(userNick + context.getString(R.string.live_biggift_send) + giftName);
            }
            if (mBigGiftView.getVisibility() != VISIBLE && isShowBigGift) {
                mBigGiftView.setVisibility(VISIBLE);
            }


            silkyAnimation.start(file);
        }
    }


    /**
     * 播放完之后清除出子视图
     */
    public void clearChildView() {
        if (mSendInfo != null) {
            mSendInfo.setVisibility(GONE);
        }
        if (mBigGiftView != null) {
            mBigGiftView.setVisibility(GONE);
        }
    }

    public static void setShow(String type) {
        if (HnLiveConstants.EventBus.Show_Mask.equals(type)) {//显示遮罩层
            isShowBigGift = false;
        } else if (HnLiveConstants.EventBus.Hide_Mask.equals(type)) {//隐藏遮罩层
            isShowBigGift = true;
        }
    }


}
