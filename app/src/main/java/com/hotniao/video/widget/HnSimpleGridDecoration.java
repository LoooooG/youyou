package com.hotniao.video.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hotniao.video.HnApplication;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：HnMall_V1.0
 * 类描述：
 * 创建人：kevinxie
 * 创建时间：2017/3/7 10:21
 * 修改人：
 * 修改时间：2017/3/7 10:21
 * 修改备注：
 * Version:  1.0.0
 */
public class HnSimpleGridDecoration extends RecyclerView.ItemDecoration {

    private int horizontalSpaceDip = 5;
    private int verticalSpaceDip = 5;
    private int leftSpaceDip = 0;
    private int topSpaceDip = 0;
    private int rightSpaceDip = 0;
    private int bottomSpaceDip = 0;


    public HnSimpleGridDecoration(int horizontalSpaceDip, int verticalSpaceDip) {
        this.horizontalSpaceDip = horizontalSpaceDip;
        this.verticalSpaceDip = verticalSpaceDip;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if(childAdapterPosition==0){
            super.getItemOffsets( outRect,  view,  parent,  state);
            return;
        }
        childAdapterPosition-=1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager){
            int  spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            if (childAdapterPosition % spanCount == 0) {
                //第一列
                if (childAdapterPosition < spanCount) {     //第一行
                    outRect.set(leftSpaceDip, topSpaceDip, dp2px(horizontalSpaceDip) / 2, 0);
                } else if (childAdapterPosition >= parent.getChildCount() - spanCount) {     //最后一行
                    outRect.set(leftSpaceDip, dp2px(verticalSpaceDip), dp2px(horizontalSpaceDip) / 2, bottomSpaceDip);
                } else {
                    outRect.set(leftSpaceDip, dp2px(verticalSpaceDip), dp2px(horizontalSpaceDip) / 2, 0);
                }
            } else if ((childAdapterPosition + 1) % spanCount == 0) {
                //最后一列
                if (childAdapterPosition < spanCount) {
                    outRect.set(dp2px(horizontalSpaceDip) / 2, topSpaceDip, rightSpaceDip, 0);
                } else if (childAdapterPosition >= parent.getChildCount() - spanCount) {     //最后一行
                    outRect.set(dp2px(horizontalSpaceDip) / 2, dp2px(verticalSpaceDip), rightSpaceDip, bottomSpaceDip);
                } else {
                    outRect.set(dp2px(horizontalSpaceDip) / 2, dp2px(verticalSpaceDip), rightSpaceDip, 0);
                }
            } else {
                if (childAdapterPosition < spanCount) {
                    outRect.set(dp2px(horizontalSpaceDip) / 2, topSpaceDip, dp2px(horizontalSpaceDip) / 2, 0);
                } else if (childAdapterPosition >= parent.getChildCount() - spanCount) {     //最后一行
                    outRect.set(dp2px(horizontalSpaceDip) / 2, dp2px(verticalSpaceDip), dp2px(horizontalSpaceDip) / 2, bottomSpaceDip);
                } else {
                    outRect.set(dp2px(horizontalSpaceDip) / 2, dp2px(verticalSpaceDip), dp2px(horizontalSpaceDip) / 2, 0);
                }
            }
        }

    }

    public int dp2px(float dpValue) {
        final float scale = HnApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
