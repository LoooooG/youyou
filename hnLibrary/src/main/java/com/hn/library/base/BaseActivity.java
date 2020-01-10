package com.hn.library.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hn.library.global.HnConstants;
import com.hn.library.http.OnRequestErrCallBack;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.manager.HnAppManager;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.utils.HnAppStatusCheckUtil;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.utils.PermissionHelper;
import com.hn.library.view.HnLoadingDialog;

import butterknife.ButterKnife;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 基类Activity 主要封装一些工具类的使用，公共方法，配置
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public abstract class BaseActivity extends BaseLayoutActivity implements OnRequestErrCallBack {

    protected String TAG = getClass().getSimpleName();
    protected HnLoadingDialog loading;
    protected Bundle mBundle;
    protected Resources.Theme mTheme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
        ButterKnife.bind(this);

        mBundle = getIntent().getExtras();
        mTheme = getTheme();
        if (HnPrefUtils.getInt(HnConstants.PERMISSION_SIZE, 7) != PermissionHelper.getPerSize(this) && null != savedInstanceState) {
            try {
                Class classname = Class.forName("com.hotniao.video.activity.HnSplashActivity");
                Intent intent = new Intent(this,classname);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            HnPrefUtils.setInt(HnConstants.PERMISSION_SIZE, PermissionHelper.getPerSize(this));
        }


        onCreateNew(savedInstanceState);
        getInitData();
        HnAppManager.getInstance().addActivity(this);

    }


    /**
     * 修改状态栏颜色
     *
     * @param activity
     * @param colorResId
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 布局文件id
     *
     * @return
     */
    public abstract int getContentViewId();

    /**
     * 初始化view
     */
    public abstract void onCreateNew(Bundle savedInstanceState);

    /**
     * 初始化data
     */
    public abstract void getInitData();

    /**
     * 显示执行进度状态
     */
    public void showDoing(String TAG, DialogInterface.OnCancelListener listener) {
        if (isFinishing()) {
            return;
        }
        if (loading == null) {
            loading = HnUtils.progressLoading(this, TAG, listener);
//            loading = HnUtils.drawableLoading(this, TAG, listener);
        } else {
            if (loading.isShowing()) {
                loading.cancel();
            }
            loading.setTag(TAG);
            loading.setOnCancelListener(listener);
        }
        loading.show();
    }

    /**
     * 隐藏加载状态或执行状态
     */
    public void done() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }


    /**
     * 弹出对话框
     */
    public void alert(String title, String description, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(description)
                .setTitle(title)
                .setPositiveButton("确认", listener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    /**
     * 如果cookie失效之后，需要重新登录
     *
     * @param errCode
     * @param msg
     */
    @Override
    public void loginErr(int errCode, String msg) {
//        //采用Arouter框架进行界面跳转
//        ARouter.getInstance().build("/main/HnLoginActivity", "app").navigation();
        HnAppStatusCheckUtil.getInstance().checkAppActivityStatus(this, "com.hotniao.live.activity.HnLoginActivity");
    }


    /**
     * 根据传入的类(class)打开指定的activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        Intent itent = new Intent();
        itent.setClass(this, pClass);
        startActivity(itent);
    }

    /**
     * 根据传入的类(class)打开指定的activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass, Bundle bundle) {
        Intent itent = new Intent();
        itent.putExtras(bundle);
        itent.setClass(this, pClass);
        startActivity(itent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HnAppManager.getInstance().finishActivity(this);
    }


    /**
     * 改变视图加载状态  需要手动触发
     *
     * @param state
     * @param mHnLoadingLayout
     */
    public void setLoadViewState(int state, HnLoadingLayout mHnLoadingLayout) {
        try {
            if (mHnLoadingLayout == null) return;
            if (state != mHnLoadingLayout.getStatus()) {
                mHnLoadingLayout.setStatus(state);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 关闭刷新 需要手动触发
     */
    public void closeRefresh(PtrClassicFrameLayout mRefreshView) {
        if (mRefreshView != null) {
            mRefreshView.refreshComplete();
        }
    }

    /**
     * 展示一个切换动画
     */
    public void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }


}
