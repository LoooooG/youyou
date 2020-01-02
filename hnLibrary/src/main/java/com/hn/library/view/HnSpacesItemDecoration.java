package com.hn.library.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：RecyclerView_GridView设置item间距
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isHead;

    public HnSpacesItemDecoration(int space, boolean isHead) {
        this.space = space;
        this.isHead = isHead;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距

        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
        if(parent.getLayoutManager() instanceof  GridLayoutManager) {
            if (isHead) {
                if (0 == parent.getChildLayoutPosition(view)) {
                    outRect.bottom = space;
                } else if (parent.getChildLayoutPosition(view) % 2 != 0) {
                    outRect.bottom = space;
                    outRect.top = space;
                    outRect.left = 2 * space;
                    outRect.right = space;
                } else {
                    outRect.bottom = space;
                    outRect.top = space;
                    outRect.left = space;
                    outRect.right = 2 * space;
                }
            } else {
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.bottom = space;
                    outRect.top = 2 * space;
                    outRect.left = 2 * space;
                    outRect.right = space;
                } else if (parent.getChildLayoutPosition(view) == 1) {
                    outRect.bottom = space;
                    outRect.top = 2 * space;
                    outRect.left = space;
                    outRect.right = 2 * space;
                } else if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.bottom = space;
                    outRect.top = space;
                    outRect.left = 2 * space;
                    outRect.right = space;
                } else {
                    outRect.bottom = space;
                    outRect.top = space;
                    outRect.left = space;
                    outRect.right = 2 * space;
                }
            }
        }else {
            if (isHead) {
                if (0 == parent.getChildLayoutPosition(view)) {
                    outRect.bottom = space;
                } else {
                    outRect.bottom = space;
                    outRect.top = space;
                    outRect.left = 2 * space;
                    outRect.right =2 *  space;
                }
            } else {
                    outRect.bottom = space;
                    outRect.top = 2 * space;
                    outRect.left = 2 * space;
                    outRect.right =2 * space;
            }
        }

    }


}