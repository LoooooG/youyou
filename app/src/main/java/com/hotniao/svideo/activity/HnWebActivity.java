package com.hotniao.svideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.svideo.R;
import com.hotniao.svideo.widget.HnWebViewWithProgress;

import butterknife.BindView;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：用于加载H5界面
 * 创建人：mj
 * 创建时间：
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnWebActivity extends BaseActivity implements HnLoadingLayout.OnReloadListener {

    public static final String Sign = "sign";
    public static final String Level = "level";
    public static final String Help = "help";
    public static final String About = "about";
    public static final String Banner = "banner";
    public static final String LoginAgree = "login_agree";
    public static final String RechergeAgree = "recherge_agree";


    @BindView(R.id.detail_webview)
    HnWebViewWithProgress mDetailWebview;
    @BindView(R.id.activity_hn_web_page)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.mIvBack)
    AppCompatImageButton mIvBack;
    @BindView(R.id.mIvBackClose)
    ImageView mIvBackClose;
    @BindView(R.id.bar_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.bar_subtitle)
    TextView mTvSubtitle;

    private WebView webView;

    public static void luncher(Activity activity, String title, String url, String type) {
        activity.startActivity(new Intent(activity, HnWebActivity.class).putExtra("title", title)
                .putExtra("url", url).putExtra("type", type));
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_hn_web_page;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowTitleBar(false);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack(); //goBack()表示返回WebView的上一页面
                } else {
                    finish();
                }
            }
        });
        mIvBackClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (Help.equals(getIntent().getStringExtra("type"))) {
            mTvSubtitle.setVisibility(View.VISIBLE);
            mTvSubtitle.setText(R.string.i_have_feekback);
            mTvSubtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openActivity(HnFeedBackActivity.class);
                }
            });
        }else {
            mTvSubtitle.setVisibility(View.GONE);
        }

             /*初始化视图*/
        initView();
             /*初始化webview*/
        initWebView();
        mTvTitle.setText(getIntent().getStringExtra("title"));

//        syncCookie();
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        if (About.equals(getIntent().getStringExtra("type"))) {
            HnLogUtils.i("HnWebActivity", "加载Url:" + getIntent().getStringExtra("url") + "&&access_token=" + token);
            webView.loadUrl(getIntent().getStringExtra("url") + "&&access_token=" + token);
        } else {
            HnLogUtils.i("HnWebActivity", "加载Url:" + getIntent().getStringExtra("url") + "?access_token=" + token);
            webView.loadUrl(getIntent().getStringExtra("url") + "?access_token=" + token);
        }
    }

    // 设置cookie
    public void syncCookie() {
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            cookieManager.setCookie(getIntent().getStringExtra("url"), "access_token=" + token);//cookies是在HttpClient中获得的cookie
            CookieSyncManager.getInstance().sync();
        }

    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setOnReloadListener(this);
        setShowBack(true);


    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        webView = mDetailWebview.getWebView();
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                }
            }

        });
    }

    @Override
    public void getInitData() {

    }


    @Override
    public void onReload(View v) {
        HnLogUtils.i("HnWebActivity", "加载Url:" + getIntent().getStringExtra("url"));
        webView.loadUrl(getIntent().getStringExtra("url"));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null)
            webView.reload();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            } else {
                finish();
                return true;
            }
        }
        return false;
    }

}
