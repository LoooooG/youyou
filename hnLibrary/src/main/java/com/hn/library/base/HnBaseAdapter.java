package com.hn.library.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述： 基类适配器
 * 创建人：Kevin
 * 创建时间：2016/8/8 16:23
 * 修改人：Kevin
 * 修改时间：2016/8/8 16:23
 * 修改备注：
 * Version:  1.0.0
 */
public abstract class HnBaseAdapter<T> extends BaseAdapter {

    /**
     * 数据源
     */
    private List<T>        list;
    /**
     * 布局加载器
     */
    private LayoutInflater inflater;
    /**
     * item对应的布局文件
     */
    private int            resId;

    /**
     * 有参构造方法
     *
     * @param resId   布局文件的id
     * @param list    数据源
     * @param context 上下文
     */
    public HnBaseAdapter(int resId, List<T> list, Context context) {
        this.resId = resId;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }



    /**
     * 获取数据源总的个数
     *
     * @return 个数
     */
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();

        }
        return 0;
    }

    /**
     * 获取position位置上的数据源
     *
     * @param position item在列表中的位置
     * @return 数据
     */
    @Override
    public T getItem(int position) {
        if (list != null) {
            return list.get(position);

        }
        return null;
    }

    /**
     * 获取对应的Id，
     * 在没有Header的情况下position和id是一样的
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取对应的View
     *
     * @param position    位置
     * @param convertView 复用的View
     * @param parent      容器类控件
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取ViewHolder对象
        HnViewHolder vh = HnViewHolder.getViewHolder(convertView, resId, inflater, parent);
        // 设置内容，交给用户去实现
        setContent(vh, getItem(position));
        // 返回复用的View
        return vh.getConvertView();
    }

    public abstract void setContent(HnViewHolder vh, T item);
}
