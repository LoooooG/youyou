package com.hotniao.video.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hotniao.video.R;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：继承自RelativeLayout封装的WebView加载控件
 * 创建人：Kevin
 * 创建时间：2017/3/29 19:18
 * 修改人：Kevin
 * 修改时间：2017/3/29 19:18
 * 修改备注：
 * Version:  1.0.0
 */
public class HnWebViewWithProgress extends RelativeLayout {


    private Context context;
    public        WebView        mWebView           = null;
    //水平进度条
    private       ProgressBar    progressBar        = null;
    //包含圆形进度条的布局
    private       RelativeLayout progressBar_circle = null;
    //进度条样式,Circle表示为圆形，Horizontal表示为水平
    private       int            mProgressStyle     = ProgressStyle.Horizontal.ordinal();
    //默认水平进度条高度
    public static int            DEFAULT_BAR_HEIGHT = 8;
    //水平进度条的高
    private       int            mBarHeight         = DEFAULT_BAR_HEIGHT;

    public enum ProgressStyle {
        Horizontal,
        Circle
    }


    public HnWebViewWithProgress(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public HnWebViewWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WebViewWithProgress);
        mProgressStyle = attributes.getInt(R.styleable.WebViewWithProgress_progressStyle, ProgressStyle.Horizontal.ordinal());
        mBarHeight = attributes.getDimensionPixelSize(R.styleable.WebViewWithProgress_barHeight, DEFAULT_BAR_HEIGHT);
        attributes.recycle();
        init();
    }

    private void init() {
        mWebView = new WebView(context);
        this.addView(mWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
            progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            HnWebViewWithProgress.this.addView(progressBar, LayoutParams.MATCH_PARENT, mBarHeight);
        } else {
            progressBar_circle = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.progress_circle, null);
            HnWebViewWithProgress.this.addView(progressBar_circle, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if(context==null)return;
                if (newProgress == 100) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (progressBar_circle != null) {
                        progressBar_circle.setVisibility(View.GONE);
                    }
                } else {
                    if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar_circle.setVisibility(View.VISIBLE);
                    }
                }
            }


        });
    }


    public WebView getWebView() {
        return mWebView;
    }


}
