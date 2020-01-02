package com.hn.library.base;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 自定义封装ViewHolder
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public class HnViewHolder {

    /** 保存ViewHolder中控件，和Map很相似，只是Key只能是Integer类型 */
    private SparseArray<View> mSparseArray;

    /** 复用的View对象 */
    private View convertView;

    /**
     * @param resId
     * @param inflater
     * @param parent
     */
    private HnViewHolder(int resId, LayoutInflater inflater, ViewGroup parent) {
        mSparseArray = new SparseArray<>();
        convertView = inflater.inflate(resId, parent, false);
        convertView.setTag(this);
    }

    /**
     * 给外界提供一个获取ViewHolder对象的静态方法，
     *
     * @param convertView 复用的View
     * @param resId       item对应的布局文件
     * @param inflater    布局加载器
     * @param parent      容器类控件
     * @return
     */
    public static HnViewHolder getViewHolder(View convertView, int resId, LayoutInflater inflater, ViewGroup parent) {

        if (convertView == null) {
            // 表示没有复用的View，那么就需要加载布局文件，同时需要实例化ViewHolder
            return new HnViewHolder(resId, inflater, parent);

        }
        return (HnViewHolder) convertView.getTag();
    }

    /**
     * 根据id获取对应的View
     *
     * @param resId
     * @return
     */
    public View getViews(@IdRes int resId) {
        View view = mSparseArray.get(resId);
        if (view == null) {
            view = convertView.findViewById(resId);
            mSparseArray.put(resId, view);
        }
        return view;
    }

    /**
     * 给对应的item中的TextView设置值
     *
     * @param resId View的id
     * @param text  需要设置的值
     */
    public void setTextViewText(int resId, String text) {
        View view = getViews(resId);
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setText(text);
        } else {
            throw new ClassCastException("类型转换异常。。。");
        }
    }

    /**
     * 给对应的item中的TextView设置值
     *
     * @param resId View的id
     * @param text  对应的值
     */
    public void setTextViewText(int resId, CharSequence text) {
        View view = getViews(resId);
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setText(text);
        } else {
            throw new ClassCastException("类型转换异常。。。");
        }

    }


    /**
     * 给外界提供一个获取复用View的方法
     *
     * @return
     */
    public View getConvertView() {
        return convertView;
    }
}
