package com.hotniao.live.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.HnEditText;
import com.hn.library.view.HnSendVerifyCodeButton;
import com.hotniao.live.HnApplication;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.biz.register.HnRegisterBiz;
import com.hn.library.global.HnUrl;
import com.hn.library.model.HnLoginModel;
import com.hotniao.live.widget.HnButtonTextWatcher;
import com.imlibrary.login.TCLoginMgr;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：注册
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnRegistActivity extends BaseActivity implements BaseRequestStateListener {


    @BindView(R.id.mEtPhone)
    HnEditText mEtPhone;
    @BindView(R.id.mBtnSendCode)
    HnSendVerifyCodeButton mBtnSendCode;
    @BindView(R.id.mEtYzm)
    HnEditText mEtYzm;
    @BindView(R.id.et_pwd)
    HnEditText mEtPwd;
    @BindView(R.id.mIvEye)
    ImageView mIvEye;
    @BindView(R.id.mEtYqm)
    HnEditText mEtYqm;
    @BindView(R.id.cb_prot)
    CheckBox mCbProt;
    @BindView(R.id.mTvRule)
    TextView mTvRule;
    @BindView(R.id.mTvRegister)
    TextView mTvRegister;


    private EditText[] mEts;
    private HnButtonTextWatcher mWatcher;
    //密码查看
    private boolean isLookPwd = false;
    //注册业务逻辑类，用户处理注册相关业务
    private HnRegisterBiz mHnRegisterBiz;

    /**
     * 腾讯云登录
     */
    private TCLoginMgr mTcLoginMgr;

    @Override
    public int getContentViewId() {
        return R.layout.activity_regist;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        //标题栏
        setShowTitleBar(true);
        setShowBack(true);
        setTitle(R.string.regisiter);
        //初始化注册业务逻辑类
        mHnRegisterBiz = new HnRegisterBiz(this);
        mHnRegisterBiz.setRegisterListener(this);
    }

    @Override
    public void getInitData() {
        mEts = new EditText[]{mEtPhone, mEtPwd, mEtYzm};
        mWatcher = new HnButtonTextWatcher(mTvRegister, mBtnSendCode, mEts);
        mEtPhone.addTextChangedListener(mWatcher);
        mEtPwd.addTextChangedListener(mWatcher);
        mEtYzm.addTextChangedListener(mWatcher);
    }


    @OnClick({R.id.mBtnSendCode, R.id.mIvEye, R.id.mTvRule, R.id.mTvRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnSendCode:
                if (TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                    HnToastUtils.showToastShort(R.string.log_input_phone);
                    return;
                }
                if (!HnRegexUtils.isMobileExact(mEtPhone.getText().toString().trim())) {
                    HnToastUtils.showToastShort(R.string.log_input_okphone);
                    return;
                }

                if (mBtnSendCode.getIsStart()) return;
                sendSMS(mEtPhone.getText().toString().trim());

                break;
            case R.id.mIvEye:
                if (!isLookPwd) {
                    isLookPwd = true;
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvEye.setImageResource(R.drawable.eye_on);
                    mEtPwd.setSelection(mEtPwd.getText().toString().length());

                } else {
                    isLookPwd = false;
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvEye.setImageResource(R.drawable.eye_off);
                    mEtPwd.setSelection(mEtPwd.getText().toString().length());
                }

                break;
            case R.id.mTvRule:
                HnWebActivity.luncher(HnRegistActivity.this, getString(R.string.youbo_pro_title), HnUrl.REGISTER_AGREEMENT, "regist");
                break;
            case R.id.mTvRegister:
                if (!mCbProt.isChecked()) {
                    HnToastUtils.showToastShort(getString(R.string.please_user_register_prot));
                    return;
                }

                String phone = mEtPhone.getText().toString();
                String mYzm = mEtYzm.getText().toString();
                String mPwd = mEtPwd.getText().toString();
                String mYqm = mEtYqm.getText().toString();
                mHnRegisterBiz.requestToRegisterFirstStep(phone, mYzm, mPwd, mYqm);


                break;
        }
    }

    @Override
    public void requesting() {
        mTvRegister.setEnabled(false);
        showDoing(HnUiUtils.getString(R.string.regiesting), null);
    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
//        done();

        HnUtils.hideSoftInputFrom(mEtPhone, HnRegistActivity.this);
        HnUtils.hideSoftInputFrom(mEtPwd, HnRegistActivity.this);
        mTvRegister.setEnabled(true);
        HnLoginModel model = (HnLoginModel) obj;
        if (model != null) {
            HnApplication.setmUserBean(model.getD());
            HnToastUtils.showToastShort(getString(R.string.register_success));
            loginTcIm();
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        done();
        HnToastUtils.showToastShort(msg);
        mTvRegister.setEnabled(true);

    }


    /**
     * 登录腾讯云通信
     */
    private void loginTcIm() {
        if (isFinishing()) return;
        if (mTcLoginMgr == null) {
            mTcLoginMgr = TCLoginMgr.getInstance();

        }
        mTcLoginMgr.setTCLoginCallback(new TCLoginMgr.TCLoginCallback() {
            @Override
            public void onSuccess() {
                mTcLoginMgr.removeTCLoginCallback();
                done();
                toHomeActivty();

            }

            @Override
            public void onFailure(int code, String msg) {
                done();
                HnToastUtils.showToastShort(msg);
            }
        });
        mTcLoginMgr.imLogin(HnApplication.getmUserBean().getTim().getAccount(), HnApplication.getmUserBean().getTim().getSign(),
                HnApplication.getmUserBean().getTim().getApp_id(), HnApplication.getmUserBean().getTim().getAccount_type());
    }

    private void toHomeActivty() {
        openActivity(HnMainActivity.class);
        finish();
        HnAppManager.getInstance().finishActivity(HnLoginActivity.class);
    }


    /**
     * @param phone//手机号
     */
    private void sendSMS(String phone) {

        RequestParams mParam = new RequestParams();
        mParam.put("phone", phone);//用户名

        HnLogUtils.e(mParam.toString());
        HnHttpUtils.postRequest(HnUrl.VERIFY_CODE_REGISTER, mParam, "VERIFY_CODE_REGISTER", new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                mBtnSendCode.startCountDownTimer();
                HnToastUtils.showToastShort(HnUiUtils.getString(R.string.send_sms_success_notice_receive));
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }


}
