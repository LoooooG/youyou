package com.hotniao.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hotniao.video.R;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：自定义对话框
 * 创建人：Kevin
 * 创建时间：2017/3/29 19:18
 * 修改人：Kevin
 * 修改时间：2017/3/29 19:18
 * 修改备注：
 * Version:  1.0.0
 */
public class HnNetDialog extends Dialog implements View.OnClickListener {

    private boolean isOkDismiss = true;//点击确定按钮，是否马上退出窗口对话
    private TextView titleView, descView;
    private Button ok, cancel, btPlay;
    private View.OnClickListener listener;

    /**
     * 构造对话框
     */
    public static HnNetDialog builder(Context context, int theme) {
        return new HnNetDialog(context, theme);
    }

    public HnNetDialog(Context context) {
        super(context);
    }

    public HnNetDialog(Context context, int theme) {
        super(context, theme);
    }

    protected HnNetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(int layoutResId) {
        if (layoutResId != 0) {
            super.setContentView(layoutResId);
            titleView = (TextView) findViewById(R.id.px_dialog_title);
            descView = (TextView) findViewById(R.id.px_dialog_description);
            cancel = (Button) findViewById(R.id.px_dialog_cancel);
            cancel.setOnClickListener(this);
            btPlay = (Button) findViewById(R.id.bt_play);
            btPlay.setOnClickListener(this);
            ok = (Button) findViewById(R.id.px_dialog_ok);
            ok.setOnClickListener(this);
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 设置对话框视图
     *
     * @param layoutResId 视图资源id
     */
    public HnNetDialog setView(int layoutResId) {
        setContentView(layoutResId);
        return this;
    }

    /**
     * 点触其它区域，退出对话框
     *
     * @param cancel true 触其它区域，退出对话框，false 触其它区域，不退出对话框
     */
    public HnNetDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title 对话框标题
     */
    public HnNetDialog setTitle(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
        return this;
    }

    /**
     * 设置对话框提示信息
     *
     * @param description 对话框提示信息
     */
    public HnNetDialog setDescription(String description) {
        if (descView != null) {
            descView.setText(description);
        }
        return this;
    }

    /**
     * 设置对话框是否在点击确定按钮后退出
     *
     * @param isOkDismiss 默认为true，点击确定后，对话框退出，false 点击确定后对话框不退出
     */
    public HnNetDialog isOkDismiss(boolean isOkDismiss) {
        this.isOkDismiss = isOkDismiss;
        return this;
    }

    /**
     * 设置确定按钮文字
     * 在设调用setView（layoutResId）之后调用
     */
    public HnNetDialog setOKText(String okTxt) {
        if (ok != null) {
            ok.setText(okTxt);
        }
        return this;
    }

    /**
     * 设置取消按钮文字
     * 在设调用setView（layoutResId）之后调用
     */
    public HnNetDialog setCancelText(String cancelTxt) {
        if (cancel != null) {
            cancel.setText(cancelTxt);
        }
        return this;
    }

    /**
     * 设置对话框确定按钮文字颜色
     *
     * @param colorId 对话框确定按钮文字颜色
     */
    public HnNetDialog okBtnTxtColor(int colorId) {
        if (colorId != 0) {
            ok.setTextColor(getContext().getResources().getColor(colorId));
        }
        return this;
    }

    /**
     * 设置对话框取消按钮文字颜色
     *
     * @param colorId 对话框取消按钮文字颜色
     */
    public HnNetDialog cancelBtnTxtColor(int colorId) {
        if (colorId != 0) {
            cancel.setTextColor(getContext().getResources().getColor(colorId));
        }
        return this;
    }

    /**
     * 对话框，确定按钮点击事件处理回调
     *
     * @param l 监听回调
     */
    public HnNetDialog addListener(View.OnClickListener l) {
        listener = l;
        return this;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.px_dialog_ok || v.getId() == R.id.bt_play) {

            if (listener != null) {
                listener.onClick(v);
            }

            if (isOkDismiss) {
                dismiss();
            }

        } else {
            dismiss();
        }
    }
}