package com.hotniao.live.activity.withdraw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hn.library.view.HnSendVerifyCodeButton;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.biz.user.userinfo.HnPhoneAndPwdBiz;
import com.hotniao.live.model.HnApplyWithDrawModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.widget.HnButtonTextWatcher;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：提现获取验证码
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnWithDrawVerificationActivity extends BaseActivity {
    @BindView(R.id.mTvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.mTvPhone)
    TextView mTvPhone;
    @BindView(R.id.mEtCode)
    HnEditText mEtCode;
    @BindView(R.id.mBtnSendCode)
    HnSendVerifyCodeButton mBtnSendCode;

    private HnButtonTextWatcher mWatcher;
    private EditText[] mEts;

    private boolean isFromInvite = false;

    /**
     * 启动
     *
     * @param activity
     * @param money    提现金额
     * @param account  提现账号
     * @param type     提现方式
     */
    public static void luncher(Activity activity, String money, String account, String type, String name) {
        activity.startActivity(new Intent(activity, HnWithDrawVerificationActivity.class).putExtra("money", money)
                .putExtra("account", account).putExtra("type", type).putExtra("name", name));
    }

    public static void luncher(Activity activity, String money, String account, String type, String name, boolean isFromInvite) {
        activity.startActivity(new Intent(activity, HnWithDrawVerificationActivity.class).putExtra("money", money)
                .putExtra("account", account).putExtra("type", type).putExtra("name", name).putExtra("isFromInvite", isFromInvite));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_withdraw_verification;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.withdraw_coin);
        mTvPhone.setText(HnApplication.getmUserBean().getUser_phone());
        mEts = new EditText[]{mEtCode};
        mWatcher = new HnButtonTextWatcher(mTvSubmit, mEts);
        mEtCode.addTextChangedListener(mWatcher);
        mBtnSendCode.setEnble(true);
        isFromInvite = getIntent().getBooleanExtra("isFromInvite", false);
    }

    @Override
    public void getInitData() {

    }


    @OnClick({R.id.mBtnSendCode, R.id.mTvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnSendCode://获取验证码

                if (mBtnSendCode.getIsStart()) return;
                sendSMS(getIntent().getStringExtra("account"), getIntent().getStringExtra("money"), getIntent().getStringExtra("type"));
                break;
            case R.id.mTvSubmit://申请
                if (TextUtils.isEmpty(mEtCode.getText().toString().trim())) {
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.please_input_code));
                    return;
                }
                withDrow(getIntent().getStringExtra("account"), getIntent().getStringExtra("money"), getIntent().getStringExtra("type"), mEtCode.getText().toString().trim(), getIntent().getStringExtra("name"));
                break;
        }
    }


    /**
     * 验证码
     *
     * @param account 账号
     * @param cash    现金
     * @param pay     转账方式，支付宝
     */
    private void sendSMS(String account, String cash, String pay) {
        RequestParams mParam = new RequestParams();
        mParam.put("account", account);//用户名
        mParam.put("cash", cash);//用户名
        mParam.put("pay", pay);//用户名

        HnHttpUtils.postRequest(HnUrl.USER_WITHDRAW_VERCODE, mParam, HnUrl.USER_WITHDRAW_VERCODE, new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {
                    mBtnSendCode.startCountDownTimer();
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.send_sms_success));
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

    /**
     * 提现
     *
     * @param account 账号
     * @param cash    现金
     * @param pay     转账方式，支付宝
     */
    private void withDrow(String account, String cash, String pay, String code, String name) {
        RequestParams mParam = new RequestParams();
        mParam.put("account", account);//用户名
        mParam.put("cash", cash);//用户名
        mParam.put("pay", pay);//用户名
        mParam.put("code", code);//用户名
        mParam.put("name", name);//用户名

        HnHttpUtils.postRequest(isFromInvite ? HnUrl.INVITE_WITHDRAW : HnUrl.USER_WITHDRAW_ADD, mParam, HnUrl.USER_WITHDRAW_ADD, new HnResponseHandler<HnApplyWithDrawModel>(HnApplyWithDrawModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {//进入提现详情
                    if (model.getD().getWithdraw_log() != null)
                        HnWithDrawDetailActivity.luncher(HnWithDrawVerificationActivity.this, model.getD().getWithdraw_log().getId(), HnWithDrawDetailActivity.Apply, isFromInvite);
                    if (model.getD().getUser() != null)
                        HnApplication.getmUserBean().setUser_dot(model.getD().getUser().getUser_dot());
                    HnAppManager.getInstance().finishActivity(HnWithDrawWriteActivity.class);
                    HnAppManager.getInstance().finishActivity(HnWithDrawVerificationActivity.class);
                }
                if (model.getC() == 10058) {
                    HnToastUtils.showToastShort("提交金额不符合要求");
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }
}
