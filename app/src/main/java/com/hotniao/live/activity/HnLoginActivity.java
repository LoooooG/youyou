package com.hotniao.live.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnRegexUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.FrescoImageView;
import com.hn.library.view.HnEditText;
import com.hn.library.view.HnSendVerifyCodeButton;
import com.hotniao.live.HnApplication;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.biz.home.HnHomeCate;
import com.hotniao.live.biz.login.HnLoginBiz;
import com.hotniao.live.eventbus.HnMultiEvent;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.utils.HnUserUtil;
import com.hotniao.live.widget.HnButtonTextWatcher;
import com.hotniao.livelibrary.biz.HnLocationBiz;
import com.hotniao.livelibrary.model.HnLocationEntity;
import com.imlibrary.login.TCLoginMgr;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：登录
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/main/HnLoginActivity", group = "app")
public class HnLoginActivity extends BaseActivity implements BaseRequestStateListener {
    private static final String TAG = "HnLoginActivity";
    /**
     * 接口返回登录状态失效
     */
    public static final String LOGIN_FAILURE = "loginFailure";

    @BindView(R.id.iv_icon)
    FrescoImageView mIvIcon;
    @BindView(R.id.et_phone)
    HnEditText mEtPhone;
    @BindView(R.id.et_pwd)
    HnEditText mEtPwd;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_for_pwd)
    TextView tvForPwd;
    @BindView(R.id.tv_reg_pro)
    TextView tvRegPro;
    @BindView(R.id.mIvEye)
    ImageView mIvEye;
    @BindView(R.id.mLlRoot)
    LinearLayout mLlRoot;
    @BindView(R.id.mLlContent)
    LinearLayout mLlContent;
    @BindView(R.id.mBtnSendCode)
    HnSendVerifyCodeButton mBtnSendCode;

    private Intent inte;
    private HnButtonTextWatcher mWatcher;
    private EditText[] mEts;
    private boolean isLookPwd = false;
    private ShareAction mShareAction;
    private UMShareAPI mUMShareAPI;
    //定位信息
    private HnLocationBiz mHnLocationBiz;
   private HnLocationEntity mLocEntity;
    /**
     * 设备id
     */
    private String mAndroidId;
    /**
     * 手机号
     */
    private String mPhoneStr;
    /**
     * 密码
     */
    private String mPasswordStr;

    private HnLoginBiz mHnLoginBiz;

    /**
     * 腾讯云登录
     */
    private TCLoginMgr mTcLoginMgr;


    /**
     * 登录失效
     *
     * @param activity
     * @param isLoginFailure
     */
    public static void luncher(Activity activity, boolean isLoginFailure) {
        activity.startActivity(new Intent(activity, HnLoginActivity.class).putExtra(LOGIN_FAILURE, isLoginFailure));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        mUMShareAPI = UMShareAPI.get(this);
        mShareAction = new ShareAction(this);


        HnHomeCate.getCateData();
        HnHomeCate.getVideoCateData();
        //标题栏
        setShowBack(false);
        setShowTitleBar(false);
        setTitle(R.string.login);
        //初始化数据
        initData();
        //设置监听
        setListener();


        HnAppManager.getInstance().finishActivity(HnVideoDetailActivity.class);
        HnAppManager.getInstance().finishActivity(HnPlayBackVideoActivity.class);
        HnAppManager.getInstance().finishActivity(HnMainActivity.class);

    }

    @Override
    public void getInitData() {

    }

    /**
     * 初始化数据
     */
    private void initData() {


        //设置下划线
        tvRegPro.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvRegPro.getPaint().setAntiAlias(true);//抗锯齿
        tvRegPro.setText(Html.fromHtml(getString(R.string.log_pro)));


        EventBus.getDefault().register(this);
        mAndroidId = HnUserUtil.getUniqueid();
        //初始化登录业务逻辑类
        mHnLoginBiz = new HnLoginBiz(this);
        mHnLoginBiz.setLoginListener(this);
        Bundle bundle = getIntent().getExtras();

        /**
         * 退出IM
         */
        TCLoginMgr.imLogout();
        /**
         * 退出极光
         */
        HnApplication.logout();
        /**
         * 清理缓存
         */
        clearCacheData();
        if (bundle != null) {
            if (bundle.getBoolean("isMulLogin", false)) {
                mHnLoginBiz.executeExit(getIntent().getStringExtra("msg"));
            }
        }
        //判断登录状态是否失效
        boolean mLoginFailure = getIntent().getBooleanExtra(LOGIN_FAILURE, false);
        if (mLoginFailure) {
            CommDialog.newInstance(this).setClickListen(new CommDialog.OneSelDialog() {
                @Override
                public void sureClick() {

                }
            }).setTitle(HnUiUtils.getString(R.string.log_out_nitify)).setCanceledOnOutside(false)
                    .setContent(getString(R.string.login_failure_again_login)).show();
        }


    }

    /**
     * 清理缓存
     */
    private void clearCacheData() {
        HnPrefUtils.setString(NetConstant.User.USER_INFO, "");
        HnPrefUtils.setBoolean(NetConstant.User.IS_ANCHOR, false);
        HnPrefUtils.setString(NetConstant.User.ANCHOR_CHAT_CATEGORY, "");
        HnPrefUtils.setString(NetConstant.User.UID, "");
        HnPrefUtils.setString(NetConstant.User.TOKEN, "");
        HnPrefUtils.setString(NetConstant.User.Webscket_Url, "");
        HnPrefUtils.setString(NetConstant.User.Unread_Count, "0");
        HnPrefUtils.setBoolean(NetConstant.User.User_Forbidden, false);
    }

    /**
     * 对控件设置监听 当用户输入数据时，才可提交
     */
    private void setListener() {
        mEts = new EditText[]{mEtPhone, mEtPwd};
        mWatcher = new HnButtonTextWatcher(mTvLogin, mEts);
        mEtPhone.addTextChangedListener(mWatcher);
        mEtPwd.addTextChangedListener(mWatcher);


        mLlRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom - bottom > 100) {
                    ViewCompat.animate(mIvIcon).scaleX(0).scaleY(0).setDuration(500).start();
//                    ViewCompat.animate(mLlBottom).scaleX(0).scaleY(0).setDuration(100).start();
                    ViewCompat.animate(mLlContent).translationY((float) (-1.4 * mIvIcon.getMeasuredHeight())).setDuration(500).start();
                } else if (bottom - oldBottom > 100) {
                    ViewCompat.animate(mIvIcon).scaleX(1).scaleY(1).setDuration(500).start();
//                    ViewCompat.animate(mLlBottom).scaleX(1).scaleY(1).setDuration(100).start();
                    ViewCompat.animate(mLlContent).translationY(0).setDuration(500).start();
                }
            }
        });
    }


    @OnClick({R.id.tv_login, R.id.tv_reg_pro, R.id.tv_for_pwd, R.id.mTvRegist,
            R.id.mIvEye, R.id.mIvQq, R.id.mIvSina, R.id.mIvWx, R.id.mBtnSendCode})
    public void onClick(View view) {
        mPhoneStr = mEtPhone.getText().toString().trim();
        mPasswordStr = mEtPwd.getText().toString().trim();
        HnUtils.hideSoftInputFrom(mEtPhone, HnLoginActivity.this);
        HnUtils.hideSoftInputFrom(mEtPwd, HnLoginActivity.this);
        switch (view.getId()) {
            case R.id.tv_login: //手机登录
//                initLocation();
                mHnLoginBiz.executeLogin(mPhoneStr, mPasswordStr, "phone", mAndroidId,"");
                break;

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


            case R.id.tv_for_pwd:  //忘记密码
                inte = new Intent(HnLoginActivity.this, HnForgetPasswordActivity.class);
                inte.putExtra("phone", mPhoneStr);
                startActivity(inte);
                break;
            case R.id.tv_reg_pro:  //条款声明
                HnWebActivity.luncher(HnLoginActivity.this, getResources().getString(R.string.log_pro), HnUrl.Web_Url + "login", HnWebActivity.LoginAgree);
                break;
            case R.id.mTvRegist:
                inte = new Intent(HnLoginActivity.this, HnRegistActivity.class);
                startActivity(inte);

                break;

            case R.id.mIvEye:
//                if (!isLookPwd) {
//                    isLookPwd = true;
//                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    mIvEye.setImageResource(R.drawable.eye_on);
//                    mEtPwd.setSelection(mEtPwd.getText().toString().length());
//
//                } else {
//                    isLookPwd = false;
//                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    mIvEye.setImageResource(R.drawable.eye_off);
//                    mEtPwd.setSelection(mEtPwd.getText().toString().length());
//                }

                break;
            case R.id.mIvWx:
                mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.mIvQq:
                mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.mIvSina:
                mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            } else {
                HnToastUtils.showToastShort("禁止定位权限无法通过手机登陆");
            }
        }
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        mHnLocationBiz = HnLocationBiz.getInsrance();
        mLocEntity = new HnLocationEntity("0", "0", "北京市", "北京市");
        mHnLocationBiz.startLocation(this);
        mHnLocationBiz.setOnLocationListener(new HnLocationBiz.OnLocationListener() {
            @Override
            public void onLocationSuccess(String province, String city, String address, String latitudeResult, String longitudeResult) {
                mLocEntity = null;
                mLocEntity = new HnLocationEntity(latitudeResult, longitudeResult, city, province);
                HnLogUtils.d(TAG,"mLocEntity.getmCity():"+mLocEntity.getmCity());
                mHnLoginBiz.executeLogin(mPhoneStr, mPasswordStr, "phone", mAndroidId,mLocEntity.getmCity());
            }

            @Override
            public void onLocationFail(String errorRease, int code) {
//                HnToastUtils.showToastShort("定位失败,errorCode=" + code);
                Dialog dialog = new AlertDialog.Builder(HnLoginActivity.this)
                        .setTitle("开启定位权限")
                        .setMessage("是否前往设置打开定位权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }



    /**
     * 请求开始
     */
    @Override
    public void requesting() {
        mTvLogin.setEnabled(false);
        showDoing(getResources().getString(R.string.loading), null);
    }

    /**
     * 请求成功
     *
     * @param type
     * @param response
     * @param obj
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        HnUtils.hideSoftInputFrom(mEtPhone, HnLoginActivity.this);
        HnUtils.hideSoftInputFrom(mEtPwd, HnLoginActivity.this);
        loginTcIm();

    }

    /**
     * 登录腾讯云通信
     */
    private void loginTcIm() {
        if (isFinishing()) return;
        mTvLogin.setEnabled(true);
        if (mTcLoginMgr == null) {
            mTcLoginMgr = TCLoginMgr.getInstance();

        }
        mTcLoginMgr.setTCLoginCallback(new TCLoginMgr.TCLoginCallback() {
            @Override
            public void onSuccess() {
                done();
                mTcLoginMgr.removeTCLoginCallback();
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


        mTvLogin.setEnabled(true);
        openActivity(HnMainActivity.class);
        HnAppManager.getInstance().finishActivity(HnLoginActivity.class);
    }

    /**
     * 请求失败
     *
     * @param type
     * @param code
     * @param msg
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        done();
        HnToastUtils.showToastShort(msg);
        mTvLogin.setEnabled(true);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMultiLogin(HnMultiEvent event) {
        String loginType = HnPrefUtils.getString(HnConstants.LogInfo.PLATFORMNAME, "");
        String phone = HnPrefUtils.getString(HnConstants.LogInfo.USER_PHONE, "");
        String password = HnPrefUtils.getString(HnConstants.LogInfo.USER_PASSWORD, "");
        if ("phone".equalsIgnoreCase(loginType)) {
            if (mEtPwd != null && mEtPhone != null) {
                mEtPhone.setText(phone);
                mEtPhone.setSelection(phone.length());
                mEtPwd.setText(password);
                mEtPwd.setSelection(password.length());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUMShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        HnAppManager.getInstance().finishActivity(HnLoginActivity.class);
        HnAppManager.getInstance().exit();
    }

    /**
     * 退出返回
     */
/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //退出应用程序
            finish();
            HnAppManager.getInstance().exit();
        }
        return super.onKeyDown(keyCode, event);
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 登录回调
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            HnLogUtils.d(TAG, data.get("access_token")+","+data.get("openid")+","+data);
            if (platform == SHARE_MEDIA.QQ) {
                mHnLoginBiz.executeLogin(data.get("access_token"), "", HnLoginBiz.QQ, data.get("openid"),"");
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                mHnLoginBiz.executeLogin(data.get("access_token"), "", HnLoginBiz.WX, data.get("openid"),"");
            } else if (platform == SHARE_MEDIA.SINA) {
                mHnLoginBiz.executeLogin(data.get("access_token"), "", HnLoginBiz.SINA, data.get("uid"),"");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            HnToastUtils.showToastShort("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            HnToastUtils.showToastShort("登录取消");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    /**
     * @param phone//手机号
     */
    private void sendSMS(String phone) {

        RequestParams mParam = new RequestParams();
        mParam.put("phone", phone);//用户名

        HnLogUtils.e(mParam.toString());
        HnHttpUtils.postRequest(HnUrl.VERIFY_CODE_LOGIN, mParam, "VERIFY_CODE_REGISTER", new HnResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                mBtnSendCode.startCountDownTimer();
                HnToastUtils.showToastShort(com.hn.library.utils.HnUiUtils.getString(R.string.send_sms_success_notice_receive));
            }

            @Override
            public void hnErr(int errCode, String msg) {
                HnToastUtils.showToastShort(msg);
            }
        });
    }

}
