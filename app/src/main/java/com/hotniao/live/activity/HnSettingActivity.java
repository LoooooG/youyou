package com.hotniao.live.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.HnBaseApplication;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnConstants;
import com.hn.library.manager.HnAppManager;
import com.hn.library.update.HnAppUpdateService;
import com.hn.library.utils.ClipBoardUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.live.HnApplication;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.biz.set.HnSetBiz;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.live.utils.HnUserUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：个人中心--设置
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSettingActivity extends BaseActivity implements BaseRequestStateListener {


    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.rl_tixing)
    RelativeLayout rlTixing;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_carch)
    TextView tvCarch;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.user_exit_tv)
    TextView userExitTv;
    @BindView(R.id.mTvV)
    TextView mTvV;
    @BindView(R.id.mIvUpData)
    ImageView mIvUpData;

    //用户id
    private String uid;
    //设置逻辑类，用户处理我的设置业务
    private HnSetBiz mHnSetBiz;


    @OnClick({R.id.tv_copy, R.id.rl_phone, R.id.rl_pwd, R.id.rl_tixing, R.id.rl_cache, R.id.rl_about, R.id.user_exit_tv, R.id.mRlMark, R.id.mRlUpData})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_copy://复制
                if (TextUtils.isEmpty(uid)) return;
                ClipBoardUtil.to(uid);
                HnToastUtils.showCenterToast(HnUiUtils.getString(R.string.id_clip));
                break;
            case R.id.rl_phone://手机号
//                 openActivity(HnBindViewPhoneActivity.class);
                break;
            case R.id.rl_pwd://黑名单
                /*if (TextUtils.isEmpty(HnApplication.getmUserBean().getUser_phone())) {
                    CommDialog.newInstance(HnSettingActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                        @Override
                        public void leftClick() {
                        }

                        @Override
                        public void rightClick() {
                            openActivity(HnFirstBindPhoneActivity.class);
                        }
                    }).setTitle(getString(R.string.bind_phone)).setContent(getString(R.string.you_not_bind_phone_please_bind)).show();
                } else {
                    openActivity(HnChangePwdActivity.class);
                }*/

               startActivity(new Intent(this,HnBlackListActivity.class));


                break;
            case R.id.rl_tixing://开播提醒
                openActivity(HnLiveNoticeActivity.class);
                break;
            case R.id.rl_cache://缓存清理
                mHnSetBiz.cleanCache();
                break;
            case R.id.rl_about://关于
                openActivity(HnAboutActivity.class);
                break;
            case R.id.user_exit_tv://退出

                CommDialog.newInstance(HnSettingActivity.this).setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                        mHnSetBiz.executeExit();

                    }
                }).setTitle(getString(R.string.logout_login)).setContent(getString(R.string.sure_logiut)).show();
                break;
            case R.id.mRlMark://评分
                break;
            case R.id.mRlUpData:
                try {
                    if (Double.parseDouble(HnBaseApplication.getmConfig().getVersion().getCode()) > HnUtils.getVersionCode(HnSettingActivity.this)) {
                        updataVer();
                    } else {
                        HnToastUtils.showToastShort(getString(R.string.is_new_v));
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle(R.string.set);
        setShowBack(true);
        mHnSetBiz = new HnSetBiz(this);
        mHnSetBiz.setBaseRequestStateListener(this);
        mHnSetBiz.getAppCache();

        mTvV.setText("V " + getVersion() + "");
       try {
           String code = HnBaseApplication.getmConfig().getVersion().getCode();
           if (Double.parseDouble(code) > HnUtils.getVersionCode(HnSettingActivity.this)) {
               mIvUpData.setVisibility(View.VISIBLE);
           } else {
               mIvUpData.setVisibility(View.GONE);
           }
       }catch (Exception e){}
    }

    @Override
    public void getInitData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getString(HnConstants.Intent.DATA);
            if (!TextUtils.isEmpty(uid)) {
                tvUserId.setText(uid);
            }
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
        if ("logout".equals(type)) {//请求退出
            openActivity(HnLoginActivity.class);
            HnAppManager.getInstance().finishActivity(HnMainActivity.class);
            finish();
        } else if ("app_cache".equals(type)) {//app缓存大小
            tvCarch.setText((String) obj);
        } else if ("app_cache_cear_success".equals(type)) {//成功清除缓存
            tvCarch.setText("0.0M");
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.str_clear_cache_succ));
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
        if ("logout".equals(type)) {//请求退出
            openActivity(HnLoginActivity.class);
            HnAppManager.getInstance().finishActivity(HnMainActivity.class);
            finish();
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 更新版本
     */
    private void updataVer() {
        CommDialog.newInstance(HnSettingActivity.this)
                .setClickListen(new CommDialog.TwoSelDialog() {
                    @Override
                    public void leftClick() {
                    }

                    @Override
                    public void rightClick() {
                        String mDownUrl = HnApplication.getmConfig().getVersion().getDownload_url();
                        if (!TextUtils.isEmpty(mDownUrl)) {
                            Intent intent = new Intent();
                            intent.setData(Uri.parse(mDownUrl));
                            intent.setAction(Intent.ACTION_VIEW);
                            startActivity(intent);
                        }

                    }
                })
                .setTitle("检测到最新版本，是否立即更新")
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
