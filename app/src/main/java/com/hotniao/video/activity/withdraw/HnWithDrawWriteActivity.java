package com.hotniao.video.activity.withdraw;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.activity.bindPhone.HnFirstBindPhoneActivity;
import com.hotniao.video.model.CheckStatueModel;
import com.hotniao.video.model.HnWithDrawIdModel;
import com.hotniao.livelibrary.control.HnUserControl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：填写提现账户
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnWithDrawWriteActivity extends BaseActivity {
    @BindView(R.id.mEtZfb)
    EditText mEtZfb;
    @BindView(R.id.mEtMoney)
    EditText mEtMoney;
    @BindView(R.id.mTvMoney)
    TextView mTvMoney;
    @BindView(R.id.mTvAllDraw)
    TextView mTvAllDraw;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_remind)
    TextView mTvRemind;

    private boolean isAllDraw = true;
    private String money = "0";
    private String type = "";

    private int status = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_withdraw_write;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.withdraw_coin);
        getData();
        mTvAllDraw.setSelected(isAllDraw);
    }

    @Override
    public void getInitData() {
        check();
    }


    @OnClick({R.id.mTvNext, R.id.mTvAllDraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvAllDraw://全部提现按钮
//                if (isAllDraw) {
//                    isAllDraw = false;
//                    mTvAllDraw.setSelected(isAllDraw);
//                    mEtMoney.setEnabled(!isAllDraw);
//
//                } else {
                isAllDraw = true;
                mTvAllDraw.setSelected(isAllDraw);
                mEtMoney.setText(money);
                mEtMoney.setSelection(money.length());
//                mEtMoney.setEnabled(!isAllDraw);
//                }
                break;
            case R.id.mTvNext://下一步
                if (status == -1) {
                    HnToastUtils.showToastShort("请稍后再试");
                    check();
                    return;
                } else if (status == 0) {
                    HnToastUtils.showToastShort("不可以提现");
                    return;
                } else if (status == 1) {
                    HnToastUtils.showToastShort("正在审核中");
                    return;
                } else if (status == 2) {
                    //HnToastUtils.showToastShort("可以提现");
                }
                if (TextUtils.isEmpty(mEtZfb.getText().toString().trim())) {
                    HnToastUtils.showToastShort(mEtZfb.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    HnToastUtils.showToastShort(etName.getHint().toString());
                    return;
                }
                if (!HnRegexUtils.isMobileExact(mEtZfb.getText().toString().trim()) && !HnRegexUtils.isEmail(mEtZfb.getText().toString().trim())) {
                    HnToastUtils.showToastShort(getString(R.string.please_true_zfb_id));
                    return;
                }
                if (TextUtils.isEmpty(mEtMoney.getText().toString().trim())) {
                    HnToastUtils.showToastShort(getString(R.string.please_write_withdraw_money));
                    return;
                }
                if (Double.parseDouble(mEtMoney.getText().toString().trim()) <= 0) {
                    HnToastUtils.showToastShort(getString(R.string.with_draw_dayu_zreo));
                    return;
                }
                if (Double.parseDouble(mEtMoney.getText().toString().trim()) > Double.parseDouble(money)) {
                    HnToastUtils.showToastShort(getString(R.string.the_amount_is_more_than_the_withdrawal_amount));
                    return;
                }

                if (Float.valueOf(mEtMoney.getText().toString()) % 100 != 0) {
                    HnToastUtils.showToastShort("提现金额必须为100的整倍数");
                    return;
                }
                if (TextUtils.isEmpty(HnApplication.mUserBean.getUser_phone())) {
                    openActivity(HnFirstBindPhoneActivity.class);
                    return;
                }
                HnWithDrawVerificationActivity.luncher(HnWithDrawWriteActivity.this, mEtMoney.getText().toString(), mEtZfb.getText().toString(), type,etName.getText().toString().trim());
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
            @Override
            public void onSuccess(String uid, HnLoginModel model, String response) {
            }

            @Override
            public void onError(int errCode, String msg) {
                HnToastUtils.showCenterToast(msg);
            }
        });
    }

    private void getData() {
        HnHttpUtils.postRequest(HnUrl.USER_WITHDRAW_INDEX, null, HnUrl.USER_WITHDRAW_INDEX, new HnResponseHandler<HnWithDrawIdModel>(HnWithDrawIdModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {
                    if (model.getD().getUser() == null) return;
                    type = model.getD().getUser().getPay();
                    mEtZfb.setHint(HnUiUtils.getString(R.string.please_write_your) + model.getD().getUser().getPay() + HnUiUtils.getString(R.string.withdraw_id));
                    mTvMoney.setText(((int) (Float.valueOf(model.getD().getUser().getCash()) / 100) * 100) + HnUiUtils.getString(R.string.unit));
                    money = model.getD().getUser().getCash();
                    if (!TextUtils.isEmpty(model.getD().getUser().getAccount())) {
                        mEtZfb.setText(model.getD().getUser().getAccount());
//                        mEtZfb.setEnabled(false);
                    }
                    mTvRemind.setText("提现金额（"+"超过{"+model.getD().getMin_withdraw_num()+"}可提现"+"，24小时可提现一次）");
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }


    private void check() {
        HnHttpUtils.postRequest(HnUrl.CHECK_STATUS, null, HnUrl.CHECK_STATUS, new HnResponseHandler<CheckStatueModel>(CheckStatueModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if (model.getC() == 0) {
                    status = model.getD().getStatus();
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }
}
