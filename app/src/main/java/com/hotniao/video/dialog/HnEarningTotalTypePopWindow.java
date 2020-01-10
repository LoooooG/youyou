package com.hotniao.video.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.hn.library.utils.HnDimenUtil;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnBeforeLiveSettingActivity;
import com.hotniao.video.utils.HnUiUtils;

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

public class HnEarningTotalTypePopWindow extends PopupWindow implements View.OnClickListener {
    //day：日，week：周，month：月，all：总

    public static final String DAY="day";
    public static final String WEEK="week";
    public static final String MONTH="month";
    public static final String SEASON="quarter";
    public static final String TOTAL="all";

    private View parentView;

    public HnEarningTotalTypePopWindow(Context context) {
        super(context);
        initView(context);
        setPopConfig();
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.popupwindow_total_earning_type, null);
        parentView.findViewById(R.id.mTvDay).setOnClickListener(this);
        parentView.findViewById(R.id.mTvWeek).setOnClickListener(this);
        parentView.findViewById(R.id.mTvMonth).setOnClickListener(this);
        parentView.findViewById(R.id.mTvSeason).setOnClickListener(this);
        parentView.findViewById(R.id.mTvTotal).setOnClickListener(this);
        setContentView(parentView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvDay:
                if(listener!=null)
                    listener.itemClick(HnUiUtils.getString(R.string.earning_day),DAY);

                break;
            case R.id.mTvWeek:
                if(listener!=null)
                    listener.itemClick(HnUiUtils.getString(R.string.earning_week),WEEK);
                break;
            case R.id.mTvMonth:
                if(listener!=null)
                    listener.itemClick(HnUiUtils.getString(R.string.earning_month),MONTH);
                break;
            case R.id.mTvSeason:
                if(listener!=null)
                    listener.itemClick(HnUiUtils.getString(R.string.earning_season),SEASON);
                break;
            case R.id.mTvTotal:
                if(listener!=null)
                    listener.itemClick(HnUiUtils.getString(R.string.earning_total),TOTAL);
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
        this.setWidth(HnDimenUtil.dp2px(HnUiUtils.getContext(),172));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        //添加pop窗口关闭事件
        this.setOnDismissListener(new poponDismissListener());
        // 允许点击外部消失
        this.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
    }


    /**
     * 设置显示在v下方(以v的左边距为开始位置)
     *
     * @param v
     */
    public void showUp(View v) {
        //在控件上方显示
        this.showAsDropDown(v,0,- HnDimenUtil.dp2px(HnUiUtils.getContext(),16));
    }

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener {
        void itemClick(String name, String type);
        void dismissLis();
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            if(listener!=null)
                listener.dismissLis();
        }

    }
}
