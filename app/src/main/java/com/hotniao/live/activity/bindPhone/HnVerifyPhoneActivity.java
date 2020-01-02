package com.hotniao.live.activity.bindPhone;

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
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnEditText;
import com.hn.library.view.HnSendVerifyCodeButton;
import com.hotniao.live.R;
import com.hn.library.global.HnUrl;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.widget.HnButtonTextWatcher;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：验证手机号(已绑定手机)
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnVerifyPhoneActivity extends BaseActivity {
    @BindView(R.id.mTvPhone)
    TextView mTvPhone;

    @BindView(R.id.mTvNext)
    TextView mTvNext;
    @BindView(R.id.mEtCode)
    HnEditText mEtCode;
    @BindView(R.id.mBtnSendCode)
    HnSendVerifyCodeButton mBtnSendCode;
    private HnButtonTextWatcher mWatcher;
    private EditText[] mEts;

    public static void luncher(Activity activity, String phone){
        activity.startActivity(new Intent(activity,HnVerifyPhoneActivity.class).putExtra("phone",phone));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_verify_phone;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.bind_phone);
        setShowBack(true);
        mBtnSendCode.setEnble(true);

        mEts = new EditText[]{mEtCode};
        mWatcher = new HnButtonTextWatcher(mTvNext, mEts);
        mEtCode.addTextChangedListener(mWatcher);
    }

    @Override
    public void getInitData() {
        mTvPhone.setText(getIntent().getStringExtra("phone"));
    }


    @OnClick({R.id.mBtnSendCode, R.id.mTvNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnSendCode:
                if (mBtnSendCode.getIsStart()) return;
                sendSMS(mTvPhone.getText().toString().trim());
                break;
            case R.id.mTvNext:
                if(TextUtils.isEmpty(mEtCode.getText().toString().trim())){
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.log_input_yzm));

                    return;
                }
                nextStep(mEtCode.getText().toString().trim());


                break;
        }
    }

    /**
     *    * 发送验证码
     * @param phone//手机号
     */
    private void sendSMS(String phone) {
        HnHttpUtils.postRequest(HnUrl.VERIFY_CODE_CHANGE_PHONE, null, "VERIFY_CODE_CHANGE_PHONE", new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if(0==model.getC()){
                    mBtnSendCode.startCountDownTimer();
                    HnToastUtils.showToastShort(HnUiUtils.getString(R.string.send_sms_success));
                }else {
                    HnToastUtils.showToastShort(model.getM());
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }


    /**
        下一步  进入更换绑定页面
     * @param code//验证码
     */
    private void nextStep(final String code) {

        RequestParams mParam = new RequestParams();
        mParam.put("code", code);//用户名

        HnLogUtils.e(mParam.toString());
        HnHttpUtils.postRequest(HnUrl.VERIFY_CODE_JUDGECHANGE_PHONE, mParam, "VERIFY_CODE_JUDGECHANGE_PHONE", new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                if(0==model.getC()){
                    HnChangeBindPhoneActivity.luncher(HnVerifyPhoneActivity.this,code,getIntent().getStringExtra("phone"));
                }else {
                    HnToastUtils.showToastShort(model.getM());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }
}
