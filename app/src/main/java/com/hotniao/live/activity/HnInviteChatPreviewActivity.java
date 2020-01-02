package com.hotniao.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.NetConstant;
import com.hn.library.model.HnLoginModel;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.adapter.InviteChatPreviewPagerAdapter;
import com.hotniao.live.fragment.HnInviteChatBeforeFragment;
import com.hotniao.live.fragment.HnUserHomeFragment;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.service.HnWebSocketService;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (C) 2019,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：yibaobao
 * 类描述：
 * 创建人：oyke
 * 创建时间：2019/1/29 13:59
 * 修改人：oyke
 * 修改时间：2019/1/29 13:59
 * 修改备注：
 * Version:  1.0.0
 */
public class    HnInviteChatPreviewActivity extends BaseActivity {


    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private String mUid;
    //在线离线状态
    private String type;
    private String avatar;

    private List<Fragment> fragments = new ArrayList<>();

    public static void launcher(final Activity activity, final String mUid, final String avator, final String type) {
        if (HnApplication.getmUserBean() == null) {
            HnUserControl.getProfile(new HnUserControl.OnUserInfoListener() {
                @Override
                public void onSuccess(String uid, HnLoginModel model, String response) {
                    if (activity == null) return;
                    start(activity, mUid, avator, type);
                }

                @Override
                public void onError(int errCode, String msg) {
                    if (activity == null) return;
                    if (!TextUtils.isEmpty(HnPrefUtils.getString(NetConstant.User.UID, ""))) {
                        start(activity, mUid, avator, type);
                    }
                }
            });
        } else {
            start(activity, mUid, avator, type);
        }

    }

    private static void start(Activity activity, String uid, String avator, String type) {
        if (TextUtils.isEmpty(uid) || uid.equals(HnApplication.getmUserBean().getUser_id())) {
            HnToastUtils.showToastShort("不能与自己快聊哦~");
            return;
        }
        activity.startActivity(new Intent(activity, HnInviteChatPreviewActivity.class).putExtra("uid", uid).putExtra("avator", avator).putExtra("type",type));
    }

    @Override
    public int getContentViewId() {
        if (!HnUiUtils.checkDeviceHasNavigationBar()) {
            Window window = getWindow();
            int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setFlags(flag, flag);
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        return R.layout.activity_invite_chat_preview;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
//        startService(new Intent(this, HnWebSocketService.class));
        mUid = getIntent().getStringExtra("uid");
        type = getIntent().getStringExtra("type");
        avatar = getIntent().getStringExtra("avator");

        fragments.add(HnInviteChatBeforeFragment.newInstance(mUid,type,avatar));
        fragments.add(HnUserHomeFragment.newInstance(mUid));
        viewPager.setAdapter(new InviteChatPreviewPagerAdapter(getSupportFragmentManager(),fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void getInitData() {

    }

    public void setCurrentPage(int page){
        viewPager.setCurrentItem(page);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (HnUiUtils.checkDeviceHasNavigationBar()) {
            if (hasFocus && Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            setCurrentPage(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //将数据返给SDK处理
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
