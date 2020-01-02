package com.hn.library.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hn.library.R;


/**
 * 警告提示
 * Created by WuJinZhou on 2015/8/8.
 */
public class HNAlert extends Dialog implements View.OnClickListener {
    private TextView msgTv;
    private Button ok;
    private View.OnClickListener listener;
    /**
     * 构造对话框
     */
    public static HNAlert builder(Context context, int theme){
        return new HNAlert(context,theme);
    }

    public HNAlert(Context context) {
        super(context);
    }

    public HNAlert(Context context, int theme) {
        super(context, theme);
    }

    protected HNAlert(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(int layoutResId) {
        if(layoutResId!=0) {
            super.setContentView(layoutResId);
            msgTv = (TextView) findViewById(R.id.px_alert_msg);
            ok = (Button) findViewById(R.id.px_alert_ok);
            ok.setOnClickListener(this);
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 设置对话框视图
     * @param layoutResId 视图资源id
     */
    public HNAlert setView(int layoutResId){
        setContentView(layoutResId);
        return this;
    }

    /**
     * 点触其它区域，退出对话框
     * @param cancel true 触其它区域，退出对话框，false 触其它区域，不退出对话框
     */
    public HNAlert isCanceledOnTouchOutside(boolean cancel){
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置警告提示信息
     * @param msg 警告提示信息
     */
    public HNAlert setMsg(String msg) {
        if (msgTv!= null) {
            msgTv.setText(msg);
        }
        return this;
    }

    public HNAlert addOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if(listener!=null){
           setCanceledOnTouchOutside(false);
           listener.onClick(v);
        }
    }

}
