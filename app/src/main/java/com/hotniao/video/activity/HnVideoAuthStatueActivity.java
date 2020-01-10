package com.hotniao.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：视频认证状态
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVideoAuthStatueActivity extends BaseActivity {

    @BindView(R.id.mIvStatue)
    ImageView mIvStatue;
    @BindView(R.id.mTvState)
    TextView mTvState;
    @BindView(R.id.mTvDetail)
    TextView mTvDetail;
    @BindView(R.id.mTvSumbit)
    TextView mTvSumbit;
    @BindView(R.id.loading)
    HnLoadingLayout loading;

    private String mType = "4";

    //用户视频认证状态：0未认证 1认证中 2认证未通过 3认证通过 4审核中 5审核不通过 6审核通过
    public static void luncher(Activity activity, String type) {
        activity.startActivity(new Intent(activity, HnVideoAuthStatueActivity.class).putExtra("type", type));

    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_video_auth_statue;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
    }

    @Override
    public void getInitData() {
        mType = getIntent().getStringExtra("type");
        setStatue();

    }


    @OnClick({R.id.mIvBack, R.id.mTvSumbit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                finish();
                break;
            case R.id.mTvSumbit:
                HnVideoAuthApplyActivity.lunchor(this, HnVideoAuthApplyActivity.CheckNoPass);
                break;
        }
    }

    //用户视频认证状态：0未认证 1认证中 2认证未通过 3认证通过 4审核中 5审核不通过 6审核通过
    private void setStatue() {
        if ("4".equals(mType)) {
            mIvStatue.setImageResource(R.drawable.under_review);
            mTvState.setVisibility(View.VISIBLE);
            mTvState.setText(R.string.sublim_success);
            mTvDetail.setVisibility(View.INVISIBLE);
            mTvSumbit.setVisibility(View.GONE);
        } else {
            mIvStatue.setImageResource(R.drawable.bg_shbtg);
            mTvState.setVisibility(View.VISIBLE);
            mTvState.setText("审核不通过，你可以再次重新提交");
            mTvDetail.setText(HnApplication.getmUserBean().getUser_video_refuse_reason());
            mTvDetail.setVisibility(View.VISIBLE);
            mTvSumbit.setVisibility(View.VISIBLE);
        }
    }


}
