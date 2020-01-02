package com.hn.library.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

public class HnRecyclerGridDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public HnRecyclerGridDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();
        if(childAdapterPosition==0){
            outRect.set(2*space,2*space, space, space);
        }else if(childAdapterPosition==1){
            outRect.set(space, space*2, space*2, space);
        }else  if (childAdapterPosition % spanCount == 0) {
            outRect.set(space*2, space, space, space);
        } else {
            outRect.set(space, space ,space*2, space);
        }
    }
}
