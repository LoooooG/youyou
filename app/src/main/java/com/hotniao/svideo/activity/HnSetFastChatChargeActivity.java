package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.svideo.HnApplication;
import com.hotniao.svideo.R;
import com.hotniao.svideo.biz.chat.HnFastChatBiz;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：设置约聊收费 - 弃用
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSetFastChatChargeActivity extends BaseActivity implements BaseRequestStateListener {
    @BindView(R.id.mEtMoney)
    EditText mEtMoney;
    @BindView(R.id.mTvCoinName)
    TextView mTvCoinName;

    private HnFastChatBiz mFastChatBiz;

    public static void luncher(Activity activity, String price) {
        activity.startActivity(new Intent(activity, HnSetFastChatChargeActivity.class).putExtra("price", price));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_set_fast_chat_charge;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        mFastChatBiz = new HnFastChatBiz(this);
        mFastChatBiz.setBaseRequestStateListener(this);
        setShowBack(true);
        setTitle("约聊收费设置");
        setShowSubTitle(true);
        mSubtitle.setText(R.string.save);
        mSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEtMoney.getText().toString().trim())) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.please_input_charge_money));
                    return;
                }
                if (0 >= Integer.parseInt(mEtMoney.getText().toString().trim())) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.charge_money_more_zero));
                    return;
                }
                //TODO 不能设置
//                mFastChatBiz.openChatVideo(Integer.parseInt(mEtMoney.getText().toString().trim()) + "", 2);
            }
        });
        if (!TextUtils.isEmpty(getIntent().getStringExtra("price"))) {
            mEtMoney.setText(getIntent().getStringExtra("price"));
            mEtMoney.setSelection((getIntent().getStringExtra("price") + "").length());
        }

        mTvCoinName.setText(String.format(HnUiUtils.getString(R.string.charge_name), HnApplication.getmConfig().getCoin()));
    }

    @Override
    public void getInitData() {

    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (HnFastChatBiz.ChatVideoOperation.equals(type)) {
            HnToastUtils.showToastShort(getString(R.string.save_success));
            finish();
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnFastChatBiz.ChatVideoOperation.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }
}
