package com.hotniao.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.HnEditText;
import com.hotniao.live.R;
import com.hotniao.live.biz.user.userinfo.HnPhoneAndPwdBiz;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.utils.HnUserUtil;
import com.hotniao.live.widget.HnButtonTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：修改密码
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnChangePwdActivity extends BaseActivity implements BaseRequestStateListener {


    @BindView(R.id.et_old_pwd)
    HnEditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    HnEditText etNewPwd;
    @BindView(R.id.et_new_pwd_again)
    HnEditText etNewPwdAgain;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.mIvEye1)
    ImageView mIvEye1;
    @BindView(R.id.mIvEye2)
    ImageView mIvEye2;
    @BindView(R.id.mIvEye3)
    ImageView mIvEye3;

    private boolean isVisiable1 = true, isVisiable2 = true, isVisiable3 = true;

    private HnButtonTextWatcher mWatcher;
    private HnPhoneAndPwdBiz mHnPhoneAndPwdBiz;
    private EditText[] mEts;


    @Override
    public int getContentViewId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.str_alter_pwd);
        mHnPhoneAndPwdBiz = new HnPhoneAndPwdBiz(this);
        mHnPhoneAndPwdBiz.setBaseRequestStateListener(this);
        //对控件进行字符输入监听
        mEts = new EditText[]{etOldPwd, etNewPwd, etNewPwdAgain};
        mWatcher = new HnButtonTextWatcher(tvCommit, mEts);
        etOldPwd.addTextChangedListener(mWatcher);
        etNewPwd.addTextChangedListener(mWatcher);
        etNewPwdAgain.addTextChangedListener(mWatcher);
    }


    /**
     * 提交数据到服务器
     */
    @OnClick({R.id.mIvEye1, R.id.mIvEye2, R.id.mIvEye3, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvEye1:
                HnUserUtil.switchPwdisVis(etOldPwd, mIvEye1, isVisiable1);
                isVisiable1 = !isVisiable1;

                break;
            case R.id.mIvEye2:
                HnUserUtil.switchPwdisVis(etNewPwd, mIvEye2, isVisiable2);
                isVisiable2 = !isVisiable2;

                break;
            case R.id.mIvEye3:
                HnUserUtil.switchPwdisVis(etNewPwdAgain, mIvEye3, isVisiable3);
                isVisiable3 = !isVisiable3;

                break;
            case R.id.tv_commit://提交数据到服务器
                HnUtils.hideSoftInputFrom(etOldPwd, HnChangePwdActivity.this);
                HnUtils.hideSoftInputFrom(etNewPwd, HnChangePwdActivity.this);
                HnUtils.hideSoftInputFrom(etNewPwdAgain, HnChangePwdActivity.this);
                //旧密码
                String curPwd = etOldPwd.getText().toString().trim();
                //新密码
                String newPwd = etNewPwd.getText().toString().trim();
                //新密码确认
                String mConfirm = etNewPwdAgain.getText().toString().trim();
                mHnPhoneAndPwdBiz.editUserPwd(curPwd, newPwd, mConfirm);
                break;
        }
    }


    /**
     * 请求中
     */
    @Override
    public void requesting() {
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
        done();
        HnToastUtils.showToastShort(HnUiUtils.getString(R.string.str_alter_succeed));

        HnAppManager.getInstance().finishActivity();
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
    }


    @Override
    public void getInitData() {
    }


}
