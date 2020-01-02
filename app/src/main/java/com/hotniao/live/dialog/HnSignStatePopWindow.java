package com.hotniao.live.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hn.library.utils.HnDimenUtil;
import com.hotniao.live.R;
import com.hotniao.live.utils.HnUiUtils;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSignStatePopWindow extends PopupWindow implements View.OnClickListener {


    private View parentView;
    private TextView mTvSign;
    private int popupWidth;
    private int popupHeight;
    private String mTip;

    public HnSignStatePopWindow(Context context, String tips) {
        super(context);
        this.mTip = tips;
        initView(context);
        setPopConfig();
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.popupwindow_sign_state, null);
        mTvSign = (TextView) parentView.findViewById(R.id.mTvSign);
        mTvSign.setOnClickListener(this);
        mTvSign.setText(mTip);
        setContentView(parentView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mTvSign:
                if (listener != null)
                    listener.itemClick();
                break;
        }
        dismiss();
    }

    /**
     * 配置弹出框属性
     *
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void setPopConfig() {
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        this.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        //添加pop窗口关闭事件
        this.setOnDismissListener(new poponDismissListener());
        // 允许点击外部消失
        this.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }


    /**
     * 设置显示在v下方(以v的左边距为开始位置)
     *
     * @param v
     */
    public void showUp(View v) {
        //在控件上方显示
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        try {
            //在控件上方显示
            showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - popupWidth / 2 - HnDimenUtil.dp2px(HnUiUtils.getContext(), 14), location[1] - popupHeight - HnDimenUtil.dp2px(HnUiUtils.getContext(), 10));
        } catch (Exception e) {
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void itemClick();

        void dismissLis();
    }


    class poponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            if (listener != null)
                listener.dismissLis();
        }

    }
}
