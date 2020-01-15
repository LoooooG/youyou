package com.hotniao.svideo.fragment.modify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hotniao.svideo.R;
import com.hotniao.svideo.base.BaseScollFragment;
import com.hotniao.svideo.widget.HnWebViewWithProgress;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: LooooG
 * created on: 2020/1/8 15:28
 * description: 首页签到页面
 */
public class HnSignFragment extends BaseScollFragment implements HnLoadingLayout.OnReloadListener {

    @BindView(R.id.detail_webview)
    HnWebViewWithProgress mDetailWebview;
    @BindView(R.id.activity_hn_web_page)
    HnLoadingLayout mHnLoadingLayout;

    private WebView webView;

    @Override
    public int getContentViewId() {
        return R.layout.home_sign_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*初始化视图*/
        initView();
        /*初始化webview*/
        initWebView();

        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        HnLogUtils.i("HnSignFragment", "加载Url:" + HnUrl.USER_SIGNIN_DETAIL + "?access_token=" + token);
        webView.loadUrl(HnUrl.USER_SIGNIN_DETAIL + "?access_token=" + token);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setOnReloadListener(this);
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
    protected void initData() {

    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getScrollableView() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null)
            webView.reload();
    }

    @Override
    public void onReload(View v) {
        String token = HnPrefUtils.getString(NetConstant.User.TOKEN, "");
        HnLogUtils.i("HnSignFragment", "加载Url:" + HnUrl.USER_SIGNIN_DETAIL + "?access_token=" + token);
        webView.loadUrl(HnUrl.USER_SIGNIN_DETAIL + "?access_token=" + token);
    }
}
