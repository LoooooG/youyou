package com.hotniao.video.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.manager.HnAppManager;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.biz.live.anchorAuth.HnAnchorAuthenticationBiz;
import com.hn.library.global.HnUrl;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.video.widget.HnButtonTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：实名认证界面(用户)
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnAuthenticationActivity extends BaseActivity implements BaseRequestStateListener {

    @BindView(R.id.apply_name_tv)
    EditText mApplyNameTv;
    @BindView(R.id.apply_idcard_tv)
    EditText mApplyIdcardTv;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_check)
    EditText mEtCheck;
    @BindView(R.id.et_img_1)
    EditText mEtImg1;
    @BindView(R.id.et_img_2)
    EditText mEtImg2;
    @BindView(R.id.et_img_3)
    EditText mEtImg3;
    @BindView(R.id.apply_imgone_img)
    FrescoImageView mApplyImgoneImg;
    @BindView(R.id.apply_imgtwo_img)
    FrescoImageView mApplyImgtwoImg;
    @BindView(R.id.apply_imgthree_img)
    FrescoImageView mApplyImgThreeImg;
    @BindView(R.id.apply_ensure_btn)
    TextView mApplyEnsureBtn;
    @BindView(R.id.checkbox)
    CheckBox mCheckBox;
    @BindView(R.id.tv_zheng)
    TextView mTvZheng;
    @BindView(R.id.tv_fan)
    TextView mTvFan;
    @BindView(R.id.tv_handle_zheng)
    TextView mTvHandleZheng;

    private final static String select_one = "select_one";
    private final static String select_two = "select_two";
    private final static String select_three = "select_three";
    private EditText[] mEts;
    private HnButtonTextWatcher mWatcher;

    //是否同意开播提醒  0:不支持  1：支持
    private boolean isCheck = true;
    //身份证正面照
    private String onePath;
    //身份证反面照
    private String twoPath;
    //手持身份证照
    private String threePath;
    //开播提醒H5地址
    private String rule_url;
    //主播实名认证逻辑类，处理主播认证详情相关业务
    private HnAnchorAuthenticationBiz mHnAnchorAuthenticationBiz;


    @OnClick({R.id.apply_ensure_btn, R.id.apply_imgone_img, R.id.apply_imgtwo_img, R.id.apply_imgthree_img, R.id.tv_live_pro})
    public void onClick(View view) {
        HnUtils.hideSoftInputFrom(HnAuthenticationActivity.this, mApplyIdcardTv, mEtPhone, mApplyIdcardTv);
        switch (view.getId()) {
            case R.id.apply_imgone_img://身份证正面
                mHnAnchorAuthenticationBiz.uploadPicFile(select_one);
                break;
            case R.id.apply_imgtwo_img://身份证反面
                mHnAnchorAuthenticationBiz.uploadPicFile(select_two);
                break;
            case R.id.apply_imgthree_img:
                mHnAnchorAuthenticationBiz.uploadPicFile(select_three);
                break;
            case R.id.apply_ensure_btn: //提交
                //姓名
                String userName = mApplyNameTv.getText().toString().trim();
                //手机号
                String phone = mEtPhone.getText().toString().trim();
                //身份证
                String userIdCard = mApplyIdcardTv.getText().toString().trim();
                mHnAnchorAuthenticationBiz.requestToCommitAnchorApply(userName, phone, userIdCard, onePath, twoPath, threePath, isCheck);
                break;
            case R.id.tv_live_pro://开播规则协议
                HnWebActivity.luncher(HnAuthenticationActivity.this, getString(R.string.user_start_agree), HnUrl.LIVE_AGREEMENT, "live");
                break;

        }
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_auth_detail;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        //初始化视图
        initView();
        //设置组件监听
        setListener();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setShowBack(true);
        setTitle(R.string.real_name_title);
        mHnAnchorAuthenticationBiz = new HnAnchorAuthenticationBiz(this);
        mHnAnchorAuthenticationBiz.setLoginListener(this);
    }

    /**
     * 设置组件监听
     */
    private void setListener() {
        mEts = new EditText[]{mApplyNameTv, mEtPhone, mEtCheck};
        mWatcher = new HnButtonTextWatcher(mApplyEnsureBtn, mEts);
        mApplyNameTv.addTextChangedListener(mWatcher);
        mApplyIdcardTv.addTextChangedListener(mWatcher);
        mEtPhone.addTextChangedListener(mWatcher);
        mEtCheck.addTextChangedListener(mWatcher);
        mEtImg1.addTextChangedListener(mWatcher);
        mEtImg2.addTextChangedListener(mWatcher);
        mEtImg3.addTextChangedListener(mWatcher);
        mEtCheck.setText("1");
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isCheck = true;
                    mEtCheck.setText("1");
                } else {
                    isCheck = false;
                    mEtCheck.setText("");
                }
            }
        });
    }

    @Override
    public void getInitData() {


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
        if (isFinishing()) return;
        if ("Commit_Anchor_Apply".equals(type)) {//主播申请
            openActivity(HnAuthStateActivity.class);
            HnAppManager.getInstance().finishActivity(HnBeforeLiveSettingActivity.class);
            finish();
        } else if ("upload_pic_file".equals(type)) {//上传身份证照
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.upload_succeed));
            String key = response;
            String select = (String) obj;
            HnLogUtils.i(TAG, "key：" + key);
            if (!TextUtils.isEmpty(key)) {
                updateUI(select, key);
            }
        }


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
        if ("Commit_Anchor_Apply".equals(type)) {//主播申请
            HnToastUtils.showToastShort(msg);
        } else if ("upload_pic_file".equals(type)) {//上传身份证照
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.upload_fail));
        } else if ("get_qiniu_token".equals(type)) {//获取七牛token
            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 成功上传图片后，更新界面
     *
     * @param select 位置标识 标识添加的第几张  身份证正面照/身份证正面照/手持身份证正面照
     * @param key    图片地址 用于显示和上传给自己的服务器
     */
    private void updateUI(String select, String key) {
        switch (select) {
            case "select_one"://身份证正面照
                if (!TextUtils.isEmpty(key)) {
                    mApplyImgoneImg.setController(FrescoConfig.getController(key));
                    mApplyImgoneImg.setVisibility(View.VISIBLE);
                    onePath = key;
                    mTvZheng.setVisibility(View.GONE);
                    mEtImg1.setText(key);
                }
                break;
            case "select_two"://身份证反面照
                if (!TextUtils.isEmpty(key)) {
                    mApplyImgtwoImg.setController(FrescoConfig.getController(key));
                    mApplyImgThreeImg.setVisibility(View.VISIBLE);
                    twoPath = key;
                    mTvFan.setVisibility(View.GONE);
                    mEtImg2.setText(key);
                }
                break;
            case "select_three"://手持身份证正面照
                if (!TextUtils.isEmpty(key)) {
                    mApplyImgThreeImg.setController(FrescoConfig.getController(key));
                    mApplyImgThreeImg.setVisibility(View.VISIBLE);
                    threePath = key;
                    mTvHandleZheng.setVisibility(View.GONE);
                    mEtImg3.setText(key);
                }
                break;
        }
    }
}
