package com.videolibrary.videochat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.util.HnLiveDateUtils;
import com.hotniao.livelibrary.widget.blur.BitmapBlur;
import com.videolibrary.R;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频聊天结束
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnOverChatVideoActivity extends BaseActivity {
    private FrescoImageView mIvImg;
    private TextView mTvLong;
    private TextView mTvDot;
    private TextView mTvType;
    private TextView mTvDotName;
    private LinearLayout mLLCoin;


    public static void luncher(Activity activity, String cover, String time, String dot, String coin, boolean type) {
        activity.startActivity(new Intent(activity, HnOverChatVideoActivity.class).putExtra("cover", cover).putExtra("time", time)
                .putExtra("dot", dot).putExtra("coin", coin).putExtra("type", type));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_over_chat_video;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        setShowBack(false);

        mIvImg = findViewById(R.id.mIvImg);
        mTvLong = findViewById(R.id.mTvLong);
        mTvDot = findViewById(R.id.mTvDot);
        mTvDotName = findViewById(R.id.mTvDotName);
        mTvType = findViewById(R.id.mTvType);
        mLLCoin = findViewById(R.id.mLLCoin);
        HnUiUtils.setIosApply(mLLCoin);
        findViewById(R.id.mIvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String mChatTime = getIntent().getStringExtra("time");
        if (TextUtils.isEmpty(mChatTime)) {
            mTvLong.setText("00:00");
        } else {
            try {
                mTvLong.setText(HnLiveDateUtils.getLiveTime(Long.parseLong(mChatTime)));
            } catch (Exception e) {
                mTvLong.setText("00:00");
            }
        }


        if (!getIntent().getBooleanExtra("type", true)) {
            mTvType.setText("消费：");
            mTvDot.setText(getIntent().getStringExtra("coin"));
            mTvDotName.setText(HnBaseApplication.getmConfig().getCoin());
        } else {
            mTvDot.setText(getIntent().getStringExtra("dot"));
            mTvDotName.setText(HnBaseApplication.getmConfig().getDot());
        }

        if (TextUtils.isEmpty(getIntent().getStringExtra("cover"))) {
            mIvImg.setController(FrescoConfig.getBlurController(R.drawable.bg_live));
        } else {
            mIvImg.setController(FrescoConfig.getBlurController(getIntent().getStringExtra("cover")));
        }

    }

    @Override
    public void getInitData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
