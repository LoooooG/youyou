package com.hn.library.loadstate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 自定义页面加载状态布局
 * 创建人：Kevin
 * 创建时间：2016/6/16 14:52
 * 修改人：Kevin
 * 修改时间：2016/6/16 14:52
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLoadingLayout extends FrameLayout {

    public final static int Success = 0;
    public final static int Empty = 1;
    public final static int Error = 2;
    public final static int No_Network = 3;
    public final static int Loading = 4;
    private int mState;

    private Context mContext;
    private View loadingPage;
    private View errorPage;
    private View emptyPage;
    private View networkPage;
    private View defineLoadingPage;

    private ImageView errorImg;
    private ImageView emptyImg;
    private ImageView networkImg;

    private TextView errorText;
    private TextView emptyText;
    private TextView networkText;

    private TextView errorReloadBtn;
    private TextView networkReloadBtn;

    private View contentView;
    private OnReloadListener listener;
    //是否一开始显示contentview，默认不显示
    private boolean isFirstVisible;

    //配置
    private static Config mConfig = new Config();
    private static String emptyStr = "暂无数据";
    private static String errorStr = "加载失败，请稍后重试···";
    private static String netwrokStr = "无网络连接，请检查网络···";
    private static String reloadBtnStr = "点击重试";
    private static int emptyImgId = R.drawable.home_live;
    private static int errorImgId = R.drawable.home_live;
    private static int networkImgId = R.drawable.home_net;
    private static int reloadBtnId = R.drawable.selector_btn_back_gray;
    private static int tipTextSize = 14;
    private static int buttonTextSize = 14;
    private static int tipTextColor = R.color.base_text_color_light;
    private static int buttonTextColor = R.color.base_text_color_light;
    private static int buttonWidth = -1;
    private static int buttonHeight = -1;
    private static int loadingLayoutId = R.layout.widget_loading_page;

    public HnLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HnLoadingLayout);
        isFirstVisible = a.getBoolean(R.styleable.HnLoadingLayout_isFirstVisible, false);
        a.recycle();
    }

    public HnLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public HnLoadingLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * contentView 当加载完成xml后执行
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("HnLoadingLayout can host only one direct child");
        }
        contentView = this.getChildAt(0);
        if (!isFirstVisible) {
            contentView.setVisibility(View.GONE);
        }
        build();
    }

    private void build() {

        loadingPage = LayoutInflater.from(mContext).inflate(loadingLayoutId, null);
        errorPage = LayoutInflater.from(mContext).inflate(R.layout.widget_error_page, null);
        emptyPage = LayoutInflater.from(mContext).inflate(R.layout.widget_empty_page, null);
        networkPage = LayoutInflater.from(mContext).inflate(R.layout.widget_nonetwork_page, null);
        defineLoadingPage = null;

        errorText = findViewById(errorPage, R.id.error_text);
        emptyText = findViewById(emptyPage, R.id.empty_text);
        networkText = findViewById(networkPage, R.id.no_network_text);

        errorImg = findViewById(errorPage, R.id.error_img);
        emptyImg = findViewById(emptyPage, R.id.empty_img);
        networkImg = findViewById(networkPage, R.id.no_network_img);

        errorReloadBtn = findViewById(errorPage, R.id.error_reload_btn);
        networkReloadBtn = findViewById(networkPage, R.id.no_network_reload_btn);

        errorReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onReload(v);
                }
            }
        });
        networkReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onReload(v);
                }
            }
        });

        errorText.setText(errorStr);
        emptyText.setText(emptyStr);
        networkText.setText(netwrokStr);

        errorText.setTextSize(tipTextSize);
        emptyText.setTextSize(tipTextSize);
        networkText.setTextSize(tipTextSize);

        errorText.setTextColor(ContextCompat.getColor(mContext, tipTextColor));
        emptyText.setTextColor(ContextCompat.getColor(mContext, tipTextColor));
        networkText.setTextColor(ContextCompat.getColor(mContext, tipTextColor));

        errorImg.setImageResource(errorImgId);
        emptyImg.setImageResource(emptyImgId);
        networkImg.setImageResource(networkImgId);


        errorReloadBtn.setBackgroundResource(reloadBtnId);
        networkReloadBtn.setBackgroundResource(reloadBtnId);

        errorReloadBtn.setText(reloadBtnStr);
        networkReloadBtn.setText(reloadBtnStr);

        errorReloadBtn.setTextSize(buttonTextSize);
        networkReloadBtn.setTextSize(buttonTextSize);

        errorReloadBtn.setTextColor(ContextCompat.getColor(mContext, buttonTextColor));
        networkReloadBtn.setTextColor(ContextCompat.getColor(mContext, buttonTextColor));

        if (buttonHeight != -1) {

            errorReloadBtn.setHeight(dp2px(mContext, buttonHeight));
            networkReloadBtn.setHeight(dp2px(mContext, buttonHeight));
        }
        if (buttonWidth != -1) {

            errorReloadBtn.setWidth(dp2px(mContext, buttonWidth));
            networkReloadBtn.setWidth(dp2px(mContext, buttonWidth));
        }

        this.addView(networkPage);
        this.addView(emptyPage);
        this.addView(errorPage);
        this.addView(loadingPage);
    }

    /**
     * 改变视图加载状态  需要手动触发
     *
     * @param state
     * @param mHnLoadingLayout
     */
    public void setLoadViewState(int state, HnLoadingLayout mHnLoadingLayout) {
        if (mHnLoadingLayout == null) return;
        if (state != mHnLoadingLayout.getStatus()) {
            mHnLoadingLayout.setStatus(state);
        }
    }

    /**
     * 设置当天状态
     *
     * @param status Success：加载成功;Loading：加载中;
     *               Empty:数据为空;Error:加载失败
     *               No_Network：无网络
     */
    public void setStatus(@Flavour int status) {

        this.mState = status;

        switch (status) {
            case Success:
                animateVisible(contentView);
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    animateGone(defineLoadingPage);
                } else {
                    animateGone(loadingPage);
                }
                break;

            case Loading:
                contentView.setVisibility(View.GONE);
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    animateVisible(defineLoadingPage);
                } else {
                    animateVisible(loadingPage);
                }
                break;

            case Empty:
                contentView.setVisibility(View.GONE);
                animateVisible(emptyPage);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    animateGone(defineLoadingPage);
                } else {
                    animateGone(loadingPage);
                }
                break;

            case Error:
                contentView.setVisibility(View.GONE);
                emptyPage.setVisibility(View.GONE);
                animateVisible(errorPage);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    animateGone(defineLoadingPage);
                } else {
                    animateGone(loadingPage);
                }
                break;

            case No_Network:
                contentView.setVisibility(View.GONE);
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.GONE);
                animateVisible(networkPage);
                if (defineLoadingPage != null) {
                    animateGone(defineLoadingPage);
                } else {
                    animateGone(loadingPage);
                }
                break;

            default:
                break;
        }

    }

    /**
     * View淡入
     */
    public void animateVisible(View view) {
        if (view == null) return;
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(450).setListener(null);
    }

    /**
     * View 淡出
     */
    public void animateGone(final View view) {
        if (view == null) return;
        view.animate().alpha(0f).setDuration(400).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 返回当前状态{Success, Empty, Error, No_Network, Loading}
     *
     * @return
     */
    public int getStatus() {

        return mState;
    }

    /**
     * 设置Empty状态提示文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public HnLoadingLayout setEmptyText(String text) {

        emptyText.setText(text);
        return this;
    }

    /**
     * 设置Error状态提示文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public HnLoadingLayout setErrorText(String text) {
        errorText.setText(text);
        return this;
    }

    /**
     * 设置No_Network状态提示文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public HnLoadingLayout setNoNetworkText(String text) {

        networkText.setText(text);
        return this;
    }

    /**
     * 设置Empty状态显示图片，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public HnLoadingLayout setEmptyImage(@DrawableRes int id) {


        emptyImg.setImageResource(id);
        return this;
    }

    /**
     * 设置Error状态显示图片，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public HnLoadingLayout setErrorImage(@DrawableRes int id) {

        errorImg.setImageResource(id);
        return this;
    }

    /**
     * 设置No_Network状态显示图片，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public HnLoadingLayout setNoNetworkImage(@DrawableRes int id) {

        networkImg.setImageResource(id);
        return this;
    }

    /**
     * 设置Empty状态提示文本的字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public HnLoadingLayout setEmptyTextSize(int sp) {

        emptyText.setTextSize(sp);
        return this;
    }

    /**
     * 设置Error状态提示文本的字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public HnLoadingLayout setErrorTextSize(int sp) {

        errorText.setTextSize(sp);
        return this;
    }

    /**
     * 设置No_Network状态提示文本的字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public HnLoadingLayout setNoNetworkTextSize(int sp) {

        networkText.setTextSize(sp);
        return this;
    }

    /**
     * 设置Empty状态图片的显示与否，仅对当前所在的地方有效
     *
     * @param bool
     * @return
     */
    public HnLoadingLayout setEmptyImageVisible(boolean bool) {

        if (bool) {
            emptyImg.setVisibility(View.VISIBLE);
        } else {
            emptyImg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置Error状态图片的显示与否，仅对当前所在的地方有效
     *
     * @param bool
     * @return
     */
    public HnLoadingLayout setErrorImageVisible(boolean bool) {

        if (bool) {
            errorImg.setVisibility(View.VISIBLE);
        } else {
            errorImg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置No_Network状态图片的显示与否，仅对当前所在的地方有效
     *
     * @param bool
     * @return
     */
    public HnLoadingLayout setNoNetworkImageVisible(boolean bool) {

        if (bool) {
            networkImg.setVisibility(View.VISIBLE);
        } else {
            networkImg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置ReloadButton的文本，仅对当前所在的地方有效
     *
     * @param text
     * @return
     */
    public HnLoadingLayout setReloadButtonText(@NonNull String text) {

        errorReloadBtn.setText(text);
        networkReloadBtn.setText(text);
        return this;
    }

    /**
     * 设置ReloadButton的文本字体大小，仅对当前所在的地方有效
     *
     * @param sp
     * @return
     */
    public HnLoadingLayout setReloadButtonTextSize(int sp) {

        errorReloadBtn.setTextSize(sp);
        networkReloadBtn.setTextSize(sp);
        return this;
    }

    /**
     * 设置ReloadButton的文本颜色，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public HnLoadingLayout setReloadButtonTextColor(@ColorRes int id) {

        errorReloadBtn.setTextColor(ContextCompat.getColor(mContext, id));
        networkReloadBtn.setTextSize(ContextCompat.getColor(mContext, id));
        return this;
    }

    /**
     * 设置ReloadButton的背景，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public HnLoadingLayout setReloadButtonBackgroundResource(@DrawableRes int id) {

        errorReloadBtn.setBackgroundResource(id);
        networkReloadBtn.setBackgroundResource(id);
        return this;
    }

    /**
     * 设置ReloadButton的监听器
     *
     * @param listener
     * @return
     */
    public HnLoadingLayout setOnReloadListener(OnReloadListener listener) {

        this.listener = listener;
        return this;
    }

    /**
     * 自定义加载页面，仅对当前所在的Activity有效
     *
     * @param view
     * @return
     */
    public HnLoadingLayout setLoadingPage(View view) {

        defineLoadingPage = view;
        this.removeView(loadingPage);
        defineLoadingPage.setVisibility(View.GONE);
        this.addView(view);
        return this;
    }

    /**
     * 自定义加载页面，仅对当前所在的地方有效
     *
     * @param id
     * @return
     */
    public HnLoadingLayout setLoadingPage(@LayoutRes int id) {

        this.removeView(loadingPage);
        View view = LayoutInflater.from(mContext).inflate(id, null);
        defineLoadingPage = view;
        defineLoadingPage.setVisibility(View.GONE);
        this.addView(view);
        return this;
    }

    /**
     * 获取当前自定义的loadingpage
     *
     * @return
     */
    public View getLoadingPage() {

        return defineLoadingPage;
    }


    /**
     * 获取全局使用的loadingpage
     *
     * @return
     */
    public View getGlobalLoadingPage() {

        return loadingPage;
    }

    @IntDef({Success, Empty, Error, No_Network, Loading})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flavour {

    }

    public interface OnReloadListener {

        void onReload(View v);
    }

    /**
     * 获取全局配置的class
     *
     * @return
     */
    public static Config getConfig() {

        return mConfig;
    }

    /**
     * 全局配置的Class，对所有使用到的地方有效
     */
    public static class Config {

        public Config setErrorText(@NonNull String text) {

            errorStr = text;
            return mConfig;
        }

        public Config setEmptyText(@NonNull String text) {

            emptyStr = text;
            return mConfig;
        }

        public Config setNoNetworkText(@NonNull String text) {

            netwrokStr = text;
            return mConfig;
        }

        public Config setReloadButtonText(@NonNull String text) {

            reloadBtnStr = text;
            return mConfig;
        }

        /**
         * 设置所有提示文本的字体大小
         *
         * @param sp
         * @return
         */
        public Config setAllTipTextSize(int sp) {

            tipTextSize = sp;
            return mConfig;
        }

        /**
         * 设置所有提示文本的字体颜色
         *
         * @param color
         * @return
         */
        public Config setAllTipTextColor(@ColorRes int color) {

            tipTextColor = color;
            return mConfig;
        }

        public Config setReloadButtonTextSize(int sp) {

            buttonTextSize = sp;
            return mConfig;
        }

        public Config setReloadButtonTextColor(@ColorRes int color) {

            buttonTextColor = color;
            return mConfig;
        }

        public Config setReloadButtonBackgroundResource(@DrawableRes int id) {

            reloadBtnId = id;
            return mConfig;
        }

        public Config setReloadButtonWidthAndHeight(int width_dp, int height_dp) {

            buttonWidth = width_dp;
            buttonHeight = height_dp;
            return mConfig;
        }

        public Config setErrorImage(@DrawableRes int id) {

            errorImgId = id;
            return mConfig;
        }

        public Config setEmptyImage(@DrawableRes int id) {

            emptyImgId = id;
            return this;
        }

        public Config setNoNetworkImage(@DrawableRes int id) {

            networkImgId = id;
            return mConfig;
        }

        public Config setLoadingPageLayout(@LayoutRes int id) {

            loadingLayoutId = id;
            return mConfig;
        }

    }

    public int dp2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public <T extends View> T findViewById(View v, int id) {
        return (T) v.findViewById(id);
    }
}
