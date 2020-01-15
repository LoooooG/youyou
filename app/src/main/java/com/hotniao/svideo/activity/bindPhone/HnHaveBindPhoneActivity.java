package com.hotniao.svideo.activity.bindPhone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hotniao.svideo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：已绑定手机
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnHaveBindPhoneActivity extends BaseActivity {
    @BindView(R.id.mTvPhone)
    TextView mTvPhone;

    public static void luncher(Activity activity, String phone) {
        activity.startActivity(new Intent(activity, HnHaveBindPhoneActivity.class).putExtra("phone", phone));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_have_bind_phone;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.hava_bind_phone);
        setShowBack(true);

        mTvPhone.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    public void getInitData() {

    }


    @OnClick(R.id.mTvChange)
    public void onClick() {
        HnVerifyPhoneActivity.luncher(HnHaveBindPhoneActivity.this, getIntent().getStringExtra("phone"));

    }
}
